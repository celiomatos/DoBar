package br.com.dobar.daos.controls;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.dobar.daos.Dao;
import br.com.dobar.daos.DatabaseHelper.DBHTipo;
import br.com.dobar.daos.entities.Tipo;

public class TipoControl extends Dao {

	/**
	 * 
	 * @param context
	 */
	public TipoControl(Context context) {
		super(context);

	}

	/**
	 * 
	 * @param tipo
	 * @return
	 */
	public Long create(Tipo tipo) {
		ContentValues values = new ContentValues();
		values.put(DBHTipo.TIPO, tipo.getTipo());
		Long result = getDb().insert(DBHTipo.TABELA, null, values);
		close();
		return result;
	}

	/**
	 * 
	 * @param tipo
	 * @return
	 */
	public int update(Tipo tipo) {
		ContentValues values = new ContentValues();
		values.put(DBHTipo.TIPO, tipo.getTipo());
		int result = getDb().update(DBHTipo.TABELA, values, "_id = ?",
				new String[] { String.valueOf(tipo.getIdTipo()) });
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
		int result = getDb().delete(DBHTipo.TABELA, "_id = ?", whereArgs);
		close();
		return result;
	}

	/**
	 * 
	 * @param nmtipo
	 * @return
	 */
	public List<Tipo> getListByTipo(String nmtipo) {
		Cursor cursor = getDb().rawQuery(
				"select * from tbtipo where tipo like ? order by tipo",
				new String[] { nmtipo + "%" });
		List<Tipo> list = new ArrayList<Tipo>();
		while (cursor.moveToNext()) {

			Tipo tipo = new Tipo();

			tipo.setIdTipo(cursor.getLong(cursor.getColumnIndex(DBHTipo._ID)));
			tipo.setTipo(cursor.getString(cursor.getColumnIndex(DBHTipo.TIPO)));

			list.add(tipo);
		}
		cursor.close();
		close();
		return list;
	}
	/**
	 * 
	 * @param nmtipo
	 * @return
	 */
	public Tipo getTipoByTipo(String nmtipo) {
		Cursor cursor = getDb().rawQuery(
				"select * from tbtipo where tipo = ?",
				new String[] { nmtipo});
		Tipo tipo = null;
		while (cursor.moveToNext()) {

			tipo = new Tipo();

			tipo.setIdTipo(cursor.getLong(cursor.getColumnIndex(DBHTipo._ID)));
			tipo.setTipo(cursor.getString(cursor.getColumnIndex(DBHTipo.TIPO)));
		}
		cursor.close();
		close();
		return tipo;
	}
}
