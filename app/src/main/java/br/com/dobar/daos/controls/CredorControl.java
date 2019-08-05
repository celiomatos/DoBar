package br.com.dobar.daos.controls;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.dobar.daos.Dao;
import br.com.dobar.daos.DatabaseHelper.DBHCredor;
import br.com.dobar.daos.entities.Credor;

public class CredorControl extends Dao {

	/**
	 * 
	 * @param context
	 */
	public CredorControl(Context context) {
		super(context);

	}

	/**
	 * 
	 * @param credor
	 * @return
	 */
	public Long create(Credor credor) {
		ContentValues values = new ContentValues();
		values.put(DBHCredor.RAZAO, credor.getRazao());
		values.put(DBHCredor.TELEFONE, credor.getTelefone());
		values.put(DBHCredor.EMAIL, credor.getEmail());
		values.put(DBHCredor.ATIVO, credor.getAtivo());
		Long result = getDb().insert(DBHCredor.TABELA, null, values);
		close();
		return result;
	}

	/**
	 * 
	 * @param credor
	 * @return
	 */
	public int update(Credor credor) {
		ContentValues values = new ContentValues();
		values.put(DBHCredor.RAZAO, credor.getRazao());
		values.put(DBHCredor.TELEFONE, credor.getTelefone());
		values.put(DBHCredor.EMAIL, credor.getEmail());
		values.put(DBHCredor.ATIVO, credor.getAtivo());
		int result = getDb().update(DBHCredor.TABELA, values, "_id = ?",
				new String[] { String.valueOf(credor.getIdCredor()) });
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
		int result = getDb().delete(DBHCredor.TABELA, "_id = ?", whereArgs);
		close();
		return result;
	}
	/**
	 * 
	 * @param razao
	 * @return
	 */
	public List<Credor> getListByRazao(String razao, boolean somenteAtivo){
		String sql = null;
		if (somenteAtivo) {
			sql = "select * from tbcredor where razao like ? and ativo = '1' order by razao";
		}else{
			sql = "select * from tbcredor where razao like ? order by razao";
		}
		
		Cursor cursor = getDb().rawQuery(sql, new String[]{razao + "%"});
		List<Credor> list = new ArrayList<Credor>();
		while(cursor.moveToNext()){
			Credor credor = new Credor();
			credor.setIdCredor(cursor.getLong(cursor.getColumnIndex(DBHCredor._ID)));
			credor.setRazao(cursor.getString(cursor.getColumnIndex(DBHCredor.RAZAO)));
			credor.setTelefone(cursor.getString(cursor.getColumnIndex(DBHCredor.TELEFONE)));
			credor.setEmail(cursor.getString(cursor.getColumnIndex(DBHCredor.EMAIL)));
			credor.setAtivo(cursor.getString(cursor.getColumnIndex(DBHCredor.ATIVO)));
			
			list.add(credor);
		}
		cursor.close();
		close();
		return list;
	}
	
	/**
	 * 
	 * @param razao
	 * @return
	 */
	public Credor getCredorByRazao(String razao){
		String sql = "select * from tbcredor where razao = ?";
				
		Cursor cursor = getDb().rawQuery(sql, new String[]{razao});
		Credor credor = null;
		
		while(cursor.moveToNext()){	
			credor = new Credor();
			credor.setIdCredor(cursor.getLong(cursor.getColumnIndex(DBHCredor._ID)));
			credor.setRazao(cursor.getString(cursor.getColumnIndex(DBHCredor.RAZAO)));
			credor.setTelefone(cursor.getString(cursor.getColumnIndex(DBHCredor.TELEFONE)));
			credor.setEmail(cursor.getString(cursor.getColumnIndex(DBHCredor.EMAIL)));
			credor.setAtivo(cursor.getString(cursor.getColumnIndex(DBHCredor.ATIVO)));
					
		}
		cursor.close();
		close();
		return credor;
	}
}
