package br.com.dobar.daos.controls;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.dobar.daos.Dao;
import br.com.dobar.daos.DatabaseHelper.DBHCartao;
import br.com.dobar.daos.entities.Cartao;

public class CartaoControl extends Dao {

	/**
	 * 
	 * @param context
	 */
	public CartaoControl(Context context) {
		super(context);
	}

	/**
	 * 
	 * @param cartao
	 * @return
	 */
	public Long create(Cartao cartao) {
		ContentValues values = new ContentValues();
		values.put(DBHCartao.OPERADORA, cartao.getOperadora());
		values.put(DBHCartao.BANDEIRA, cartao.getBandeira());
		values.put(DBHCartao.FECHAMENTO, cartao.getDiaFechamento());
		values.put(DBHCartao.VENCIMENTO, cartao.getDiaVencimento());
		values.put(DBHCartao.LIMITE, cartao.getLimite());
		values.put(DBHCartao.EMUSO, cartao.getEmUso());
		values.put(DBHCartao.TELEFONE, cartao.getTelefone());

		Long result = getDb().insert(DBHCartao.TABELA, null, values);
		close();
		return result;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public Integer delete(Long id) {
		String whereArgs[] = new String[] { id.toString() };
		int result = getDb().delete(DBHCartao.TABELA, "_id = ?", whereArgs);
		close();
		return result;
	}

	/**
	 * 
	 * @param cartao
	 * @return
	 */
	public int update(Cartao cartao) {
		ContentValues values = new ContentValues();
		values.put(DBHCartao.OPERADORA, cartao.getOperadora());
		values.put(DBHCartao.BANDEIRA, cartao.getBandeira());
		values.put(DBHCartao.FECHAMENTO, cartao.getDiaFechamento());
		values.put(DBHCartao.VENCIMENTO, cartao.getDiaVencimento());
		values.put(DBHCartao.LIMITE, cartao.getLimite());
		values.put(DBHCartao.EMUSO, cartao.getEmUso());
		values.put(DBHCartao.TELEFONE, cartao.getTelefone());

		int result = getDb().update(DBHCartao.TABELA, values, "_id = ?",
				new String[] { String.valueOf(cartao.getIdcartao()) });
		close();
		return result;
	}

	/**
	 * 
	 * @param operadora
	 * @return
	 */
	public List<Cartao> getListByOperadora(String operadora,
			boolean somenteAtivo) {
		String sql = null;
		if (somenteAtivo) {
			sql = "select * from tbcartao where operadora like ? and emuso = '1' order by operadora";
		} else {
			sql = "select * from tbcartao where operadora like ? order by operadora";
		}

		Cursor cursor = getDb().rawQuery(sql, new String[] { operadora + "%" });

		List<Cartao> list = new ArrayList<Cartao>();
		while (cursor.moveToNext()) {
			Cartao cartao = new Cartao();
			cartao.setIdcartao(cursor.getLong(cursor
					.getColumnIndex(DBHCartao._ID)));
			cartao.setOperadora(cursor.getString(cursor
					.getColumnIndex(DBHCartao.OPERADORA)));
			cartao.setBandeira(cursor.getString(cursor
					.getColumnIndex(DBHCartao.BANDEIRA)));
			cartao.setDiaFechamento(cursor.getInt(cursor
					.getColumnIndex(DBHCartao.FECHAMENTO)));
			cartao.setDiaVencimento(cursor.getInt(cursor
					.getColumnIndex(DBHCartao.VENCIMENTO)));
			cartao.setLimite(cursor.getInt(cursor
					.getColumnIndex(DBHCartao.LIMITE)));
			cartao.setEmUso(cursor.getString(cursor
					.getColumnIndex(DBHCartao.EMUSO)));
			cartao.setTelefone(cursor.getString(cursor
					.getColumnIndex(DBHCartao.TELEFONE)));

			list.add(cartao);
		}
		cursor.close();
		close();
		return list;
	}

	/**
	 * 
	 * @param idcartao
	 * @return
	 */
	public String getSaldo(Long idcartao) {
		Cursor cursor = getDb()
				.rawQuery(
						"select sum(p.vlparcela) saldo from tbparcela p "
								+ "inner join tbdebito d on(p.iddebito = d._id) " +
								"where p.pago = '0' and d.idcartao = ?",
						new String[] { String.valueOf(idcartao) });
		String saldo = null;
		while(cursor.moveToNext()){
			saldo = cursor.getString(cursor.getColumnIndex("saldo"));
		}
		cursor.close();
		close();
		return saldo;
	}
	/**
	 * 
	 * @param operadora
	 * @param bandeira
	 * @return
	 */
	public Cartao getCartaoByOperadoraBanderira(String operadora,	String bandeira) {
		String sql =  "select * from tbcartao where operadora = ? and bandeira = ?";
		

		Cursor cursor = getDb().rawQuery(sql, new String[] { operadora, bandeira});

		Cartao cartao = null;
		while (cursor.moveToNext()) {
			cartao = new Cartao();
			cartao.setIdcartao(cursor.getLong(cursor
					.getColumnIndex(DBHCartao._ID)));
			cartao.setOperadora(cursor.getString(cursor
					.getColumnIndex(DBHCartao.OPERADORA)));
			cartao.setBandeira(cursor.getString(cursor
					.getColumnIndex(DBHCartao.BANDEIRA)));
			cartao.setDiaFechamento(cursor.getInt(cursor
					.getColumnIndex(DBHCartao.FECHAMENTO)));
			cartao.setDiaVencimento(cursor.getInt(cursor
					.getColumnIndex(DBHCartao.VENCIMENTO)));
			cartao.setLimite(cursor.getInt(cursor
					.getColumnIndex(DBHCartao.LIMITE)));
			cartao.setEmUso(cursor.getString(cursor
					.getColumnIndex(DBHCartao.EMUSO)));
			cartao.setTelefone(cursor.getString(cursor
					.getColumnIndex(DBHCartao.TELEFONE)));			
		}
		cursor.close();
		close();
		return cartao;
	}
}
