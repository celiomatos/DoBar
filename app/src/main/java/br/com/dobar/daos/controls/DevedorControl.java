package br.com.dobar.daos.controls;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.dobar.daos.Dao;
import br.com.dobar.daos.DatabaseHelper.DBHDevedor;
import br.com.dobar.daos.entities.Devedor;

public class DevedorControl extends Dao {

	/**
	 * 
	 * @param context
	 */
	public DevedorControl(Context context) {
		super(context);
	}

	/**
	 * 
	 * @param devedor
	 * @return
	 */
	public Long create(Devedor devedor) {
		ContentValues values = new ContentValues();
		values.put(DBHDevedor.NOME, devedor.getNome());
		values.put(DBHDevedor.TELEFONE, devedor.getTelefone());
		values.put(DBHDevedor.EMAIL, devedor.getEmail());
		values.put(DBHDevedor.ATIVO, devedor.getAtivo());
		Long result = getDb().insert(DBHDevedor.TABELA, null, values);
		close();
		return result;
	}

	/**
	 * 
	 * @param devedor
	 * @return
	 */
	public int update(Devedor devedor) {
		ContentValues values = new ContentValues();
		values.put(DBHDevedor.NOME, devedor.getNome());
		values.put(DBHDevedor.TELEFONE, devedor.getTelefone());
		values.put(DBHDevedor.EMAIL, devedor.getEmail());
		values.put(DBHDevedor.ATIVO, devedor.getAtivo());
		int result = getDb().update(DBHDevedor.TABELA, values, "_id = ?",
				new String[] { String.valueOf(devedor.getIdDevedor()) });
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
		int result = getDb().delete(DBHDevedor.TABELA, "_id = ?", whereArgs);
		close();
		return result;
	}

	/**
	 * 
	 * @return
	 */
	public List<Devedor> getListByNome(String devnome, boolean somenteAtivo){
		String sql = null;
		if (somenteAtivo) {
			sql = "select * from tbdevedor where devnome like ? and dvativo = '1' order by devnome";
		}else{
			sql = "select * from tbdevedor where devnome like ? order by devnome";
		}
		Cursor cursor = getDb().rawQuery(
				sql,
				new String[] { devnome + "%" });
		List<Devedor> list = new ArrayList<Devedor>();
		while (cursor.moveToNext()) {
			Devedor devedor = new Devedor();
			devedor.setIdDevedor(cursor.getLong(cursor
					.getColumnIndex(DBHDevedor._ID)));
			devedor.setNome(cursor.getString(cursor
					.getColumnIndex(DBHDevedor.NOME)));
			devedor.setTelefone(cursor.getString(cursor
					.getColumnIndex(DBHDevedor.TELEFONE)));
			devedor.setEmail(cursor.getString(cursor
					.getColumnIndex(DBHDevedor.EMAIL)));
			devedor.setAtivo(cursor.getString(cursor
					.getColumnIndex(DBHDevedor.ATIVO)));

			list.add(devedor);
		}
		cursor.close();
		close();
		return list;
	}
	/**
	 * 
	 * @param devnome
	 * @return
	 */
	public Devedor getDevedorByNome(String devnome){
		String sql = "select * from tbdevedor where devnome = ?";
		
		Cursor cursor = getDb().rawQuery(sql,	new String[] {devnome});
		Devedor devedor = null;
		while (cursor.moveToNext()) {
			devedor = new Devedor();
			devedor.setIdDevedor(cursor.getLong(cursor
					.getColumnIndex(DBHDevedor._ID)));
			devedor.setNome(cursor.getString(cursor
					.getColumnIndex(DBHDevedor.NOME)));
			devedor.setTelefone(cursor.getString(cursor
					.getColumnIndex(DBHDevedor.TELEFONE)));
			devedor.setEmail(cursor.getString(cursor
					.getColumnIndex(DBHDevedor.EMAIL)));
			devedor.setAtivo(cursor.getString(cursor
					.getColumnIndex(DBHDevedor.ATIVO)));
			
		}
		cursor.close();
		close();
		return devedor;
	}
}
