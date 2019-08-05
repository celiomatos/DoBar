package br.com.dobar.daos.controls;

import java.math.BigDecimal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.dobar.daos.Dao;
import br.com.dobar.daos.DatabaseHelper.DBHCartao;
import br.com.dobar.daos.DatabaseHelper.DBHCredor;
import br.com.dobar.daos.DatabaseHelper.DBHDebito;
import br.com.dobar.daos.DatabaseHelper.DBHDevedor;
import br.com.dobar.daos.DatabaseHelper.DBHTipo;
import br.com.dobar.daos.entities.Cartao;
import br.com.dobar.daos.entities.Credor;
import br.com.dobar.daos.entities.Debito;
import br.com.dobar.daos.entities.Devedor;
import br.com.dobar.daos.entities.Tipo;
import br.com.dobar.utils.Constantes;

public class DebitoControl extends Dao {

	/**
	 * 
	 * @param context
	 */
	public DebitoControl(Context context) {
		super(context);
	}

	/**
	 * 
	 * @param debito
	 * @return
	 */
	public Long create(Debito debito) {

		ContentValues values = new ContentValues();

		values.put(DBHDebito.IDCREDOR, debito.getCredor().getIdCredor());
		values.put(DBHDebito.FORMPAG, debito.getFormPagamento());
		if (debito.getFormPagamento() == Constantes.CARTAO) {
			values.put(DBHDebito.IDCARTAO, debito.getCartao().getIdcartao());
		}
		values.put(DBHDebito.DATADEBITO, debito.getDataDebito());
		values.put(DBHDebito.VLDEBITO, debito.getVlDebito().toString());
		values.put(DBHDebito.ORDEM, debito.getOrdem());
		values.put(DBHDebito.IDDEVEDOR, debito.getDevedor().getIdDevedor());
		values.put(DBHDebito.IDTIPO, debito.getTipo().getIdTipo());
		values.put(DBHDebito.DESCRICAO, debito.getDescricao());

		Long result = getDb().insert(DBHDebito.TABELA, null, values);
		close();
		return result;
	}

	/**
	 * 
	 * @param debito
	 * @return
	 */
	public int update(Debito debito) {

		ContentValues values = new ContentValues();

		values.put(DBHDebito.IDCREDOR, debito.getCredor().getIdCredor());
		values.put(DBHDebito.FORMPAG, debito.getFormPagamento());
		if (debito.getFormPagamento() == Constantes.CARTAO) {
			values.put(DBHDebito.IDCARTAO, debito.getCartao().getIdcartao());
		}
		values.put(DBHDebito.DATADEBITO, debito.getDataDebito());
		values.put(DBHDebito.VLDEBITO, debito.getVlDebito().toString());
		values.put(DBHDebito.ORDEM, debito.getOrdem());
		values.put(DBHDebito.IDDEVEDOR, debito.getDevedor().getIdDevedor());
		values.put(DBHDebito.IDTIPO, debito.getTipo().getIdTipo());
		values.put(DBHDebito.DESCRICAO, debito.getDescricao());

		int result = getDb().update(DBHDebito.TABELA, values, "_id = ?",
				new String[] { String.valueOf(debito.getIddebito()) });
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
		int result = getDb().delete(DBHDebito.TABELA, "_id = ?", whereArgs);
		close();
		return result;
	}

	/**
	 * 
	 * @param ordem
	 * @return
	 */
	public Debito getDebitoByOrdem(int ordem) {
		Cursor cursor = getDb().rawQuery(
				"select * from tbdebito where ordem = ?",
				new String[] { String.valueOf(ordem) });	
		Debito debito = null;
		while (cursor.moveToNext()) {
			debito = new Debito();
			debito.setIddebito(cursor.getLong(cursor.getColumnIndex(DBHDebito._ID)));			
		}
		cursor.close();
		close();
		return debito;
	}

	/**
	 * 
	 * @param iddebito
	 * @return
	 */
	public Debito getDebitoById(Long iddebito) {
		Cursor cursor = getDb()
				.rawQuery(
						"select d._id, d.datadebito, d.formpag, d.sincro, d.vldebito, d.descricao, "
								+ "d.ordem, c.razao, dv.devnome, t.tipo, d.idcartao from tbdebito d inner join "
								+ "tbcredor c on(d.idcredor = c._id) inner join tbdevedor dv "
								+ "on(d.iddevedor = dv._id) inner join tbtipo t on(d.idtipo = t._id) "
								+ "where d._id = ?",
						new String[] { String.valueOf(iddebito) });
		Debito debito = new Debito();
		while (cursor.moveToNext()) {
			debito.setIddebito(cursor.getLong(cursor
					.getColumnIndex(DBHDebito._ID)));
			debito.setFormPagamento(cursor.getInt(cursor
					.getColumnIndex(DBHDebito.FORMPAG)));
			debito.setDataDebito(cursor.getString(cursor
					.getColumnIndex(DBHDebito.DATADEBITO)));
			debito.setDescricao(cursor.getString(cursor
					.getColumnIndex(DBHDebito.DESCRICAO)));
			debito.setOrdem(cursor.getInt(cursor
					.getColumnIndex(DBHDebito.ORDEM)));
			debito.setSincro(cursor.getString(cursor
					.getColumnIndex(DBHDebito.SINCRO)));
			debito.setVlDebito(new BigDecimal(cursor.getString(cursor
					.getColumnIndex(DBHDebito.VLDEBITO))));

			// tbdevedor
			debito.setDevedor(new Devedor());
			debito.getDevedor().setNome(
					cursor.getString(cursor.getColumnIndex(DBHDevedor.NOME)));

			// tbtipo
			debito.setTipo(new Tipo());
			debito.getTipo().setTipo(
					cursor.getString(cursor.getColumnIndex(DBHTipo.TIPO)));
			// tbcredor
			debito.setCredor(new Credor());
			debito.getCredor().setRazao(
					cursor.getString(cursor.getColumnIndex(DBHCredor.RAZAO)));
			// verifica se o debito foi feito no cartao
			if (debito.getFormPagamento() == Constantes.CARTAO) {

				debito.setCartao(new Cartao());
				debito.getCartao().setIdcartao(
						cursor.getLong(cursor
								.getColumnIndex(DBHDebito.IDCARTAO)));

				Cursor cursorTemp = getDb().rawQuery(
						"select * from tbcartao where _id = ?",
						new String[] { String.valueOf(debito.getCartao()
								.getIdcartao()) });

				while (cursorTemp.moveToNext()) {
					debito.getCartao().setOperadora(
							cursorTemp.getString(cursorTemp
									.getColumnIndex(DBHCartao.OPERADORA)));
					debito.getCartao().setTelefone(
							cursorTemp.getString(cursorTemp
									.getColumnIndex(DBHCartao.TELEFONE)));
				}

				cursorTemp.close();
			}
		}
		cursor.close();
		close();
		return debito;
	}

}
