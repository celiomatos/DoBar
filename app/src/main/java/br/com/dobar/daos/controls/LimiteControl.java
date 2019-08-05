package br.com.dobar.daos.controls;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.dobar.daos.Dao;
import br.com.dobar.daos.DatabaseHelper.DBHDevedor;
import br.com.dobar.daos.DatabaseHelper.DBHLimite;
import br.com.dobar.daos.entities.Devedor;
import br.com.dobar.daos.entities.Limite;

public class LimiteControl extends Dao {

	/**
	 * 
	 * @param context
	 */
	public LimiteControl(Context context) {
		super(context);

	}

	/**
	 * 
	 * @param limite
	 * @return
	 */
	public Long create(Limite limite) {
		ContentValues values = new ContentValues();
		values.put(DBHLimite.DIAMES, limite.getDia());
		values.put(DBHLimite.LIMITEPESSOAL, limite.getLimitePessoal());
		values.put(DBHLimite.IDDEVEDOR, limite.getDevedor().getIdDevedor());
		Long result = getDb().insert(DBHLimite.TABELA, null, values);
		close();
		return result;
	}

	/**
	 * 
	 * @param limite
	 * @return
	 */
	public int update(Limite limite) {
		ContentValues values = new ContentValues();
		values.put(DBHLimite.DIAMES, limite.getDia());
		values.put(DBHLimite.LIMITEPESSOAL, limite.getLimitePessoal());
		values.put(DBHLimite.IDDEVEDOR, limite.getDevedor().getIdDevedor());
		int result = getDb().update(DBHLimite.TABELA, values, "_id = ?",
				new String[] { String.valueOf(limite.getIdLimite()) });
		close();
		return result;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public Integer excluir(Long id) {
		String whereArgs[] = new String[] { id.toString() };
		int result = getDb().delete(DBHLimite.TABELA, "_id = ?", whereArgs);
		close();
		return result;
	}

	/**
	 * 
	 * @return
	 */
	public List<Limite> getList(String devedor) {
		Cursor cursor = getDb().rawQuery(
				"select * from tblimite l inner join tbdevedor d "
						+ "on(l.iddevedor = d._id) where d.devnome like ?",
				new String[] { devedor + "%" });
		List<Limite> list = new ArrayList<Limite>();
		while (cursor.moveToNext()) {

			Limite limite = new Limite();

			limite.setIdLimite(cursor.getLong(cursor
					.getColumnIndex(DBHLimite._ID)));
			limite.setDia(Integer.parseInt(cursor.getString(cursor
					.getColumnIndex(DBHLimite.DIAMES))));
			limite.setLimitePessoal(Integer.parseInt(cursor.getString(cursor
					.getColumnIndex(DBHLimite.LIMITEPESSOAL))));
			limite.setDevedor(new Devedor());
			limite.getDevedor().setIdDevedor(
					cursor.getLong(cursor.getColumnIndex(DBHLimite.IDDEVEDOR)));
			limite.getDevedor().setNome(
					cursor.getString(cursor.getColumnIndex(DBHDevedor.NOME)));

			list.add(limite);
		}
		cursor.close();
		close();
		return list;
	}
	/**
	 * 
	 * @param iddevedor
	 * @param dia
	 * @return
	 */
	public Limite getLimiteByDevedorAndDia(Long iddevedor, int dia){
		Cursor cursor = getDb().rawQuery(
				"select * from tblimite where iddevedor = ? and diames = ?",
				new String[] { String.valueOf(iddevedor), String.valueOf(dia) });
		
		Limite limite = null;
		while (cursor.moveToNext()) {
			limite = new Limite();
			limite.setIdLimite(cursor.getLong(cursor.getColumnIndex(DBHLimite._ID)));
			limite.setDia(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBHLimite.DIAMES))));
			limite.setLimitePessoal(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBHLimite.LIMITEPESSOAL))));
			limite.setDevedor(new Devedor());
			limite.getDevedor().setIdDevedor(cursor.getLong(cursor.getColumnIndex(DBHLimite.IDDEVEDOR)));						
		}
		cursor.close();
		close();
		return limite;
	}
}
