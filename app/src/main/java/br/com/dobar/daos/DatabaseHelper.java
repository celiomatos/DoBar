package br.com.dobar.daos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String BANCO_DADOS = "debtdb";
	private static int VERSAO = 1;

	/**
	 * 
	 * @param context
	 */
	public DatabaseHelper(Context context) {
		super(context, BANCO_DADOS, null, VERSAO);

	}

	/**
	 * 
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL("create table if not exists tbcartao("
				+ "_id INTEGER PRIMARY KEY, " + "operadora TEXT NOT NULL, "
				+ "bandeira TEXT NOT NULL, " + "fechamento INTEGER NOT NULL, "
				+ "vencimento INTEGER NOT NULL, " + "limite INTEGER NOT NULL, "
				+ "emuso BOOLEAN DEFAULT 'TRUE', cttelefone TEXT, "
				+ "CONSTRAINT cartaounique UNIQUE(operadora, bandeira))");

		db.execSQL("create table if not exists tbcredor("
				+ "_id INTEGER PRIMARY KEY, " + "razao TEXT NOT NULL UNIQUE, "
				+ "telefone TEXT, email TEXT," + "ativo BOOLEAN DEFAULT 'TRUE')");

		db.execSQL("create table if not exists tbdevedor("
				+ "_id INTEGER PRIMARY KEY, "
				+ "devnome TEXT NOT NULL UNIQUE, "
				+ "dvtelefone TEXT, dvemail TEXT," + "dvativo BOOLEAN DEFAULT 'TRUE')");

		db.execSQL("create table if not exists tbtipo("
				+ "_id INTEGER PRIMARY KEY, " + "tipo TEXT NOT NULL UNIQUE)");

		db.execSQL("create table if not exists tblimite("
				+ "_id INTEGER PRIMARY KEY, "
				+ "diames INTEGER NOT NULL, "
				+ "limitepessoal INTEGER NOT NULL, "
				+ "iddevedor INTEGER NOT NULL, "
				+ "FOREIGN KEY(iddevedor) REFERENCES tbdevedor(_id) ON DELETE CASCADE," 
				+ "CONSTRAINT limiteunique UNIQUE(iddevedor, diames))");

		db.execSQL("create table if not exists tbdebito("
				+ "_id INTEGER PRIMARY KEY,"
				+ "datadebito DATE NOT NULL,"
				+ "formpag INTEGER NOT NULL CHECK(formpag IN(0, 1, 2)),"
				+ "sincro BOOLEAN NOT NULL DEFAULT 'FALSE',"
				+ "vldebito NUMERIC(6,2) NOT NULL,"
				+ "descricao TEXT,"
				+ "ordem INTEGER NOT NULL UNIQUE,"
				+ "idcartao INTEGER,"
				+ "idtipo INTEGER,"
				+ "idcredor INTEGER,"
				+ "iddevedor INTEGER,"
				+ "FOREIGN KEY(idcartao) REFERENCES tbcartao(_id) ON DELETE SET NULL,"
				+ "FOREIGN KEY(idtipo) REFERENCES tbtipo(_id) ON DELETE SET NULL,"
				+ "FOREIGN KEY(idcredor) REFERENCES tbcredor(_id) ON DELETE CASCADE,"
				+ "FOREIGN KEY(iddevedor) REFERENCES tbdevedor(_id) ON DELETE CASCADE)");

		db.execSQL("create table if not exists tbparcela("
				+ "_id INTEGER PRIMARY KEY, "
				+ "vlparcela NUMERIC(6,2) NOT NULL, "
				+ "nrparcela TEXT NOT NULL, "
				+ "datavencimento DATE NOT NULL, "
				+ "pago BOOLEAN NOT NULL DEFAULT 'FALSE', "
				+ "datapagamento DATE NOT NULL, "
				+ "iddebito INTEGER, "
				+ "iddevedor INTEGER, "
				+ "FOREIGN KEY(iddebito) REFERENCES tbdebito(_id) ON DELETE CASCADE, "
				+ "FOREIGN KEY(iddevedor) REFERENCES tbdevedor(_id) ON DELETE CASCADE, "
				+ "CONSTRAINT parcelaunique UNIQUE(iddebito, nrparcela))");
	}

	/**
	 * 
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	/**
	 * 
	 * @author cmatos
	 * 
	 */
	public static class DBHCartao {

		public static final String TABELA = "tbcartao";
		public static final String _ID = "_id";
		public static final String OPERADORA = "operadora";
		public static final String BANDEIRA = "bandeira";
		public static final String FECHAMENTO = "fechamento";
		public static final String VENCIMENTO = "vencimento";
		public static final String LIMITE = "limite";
		public static final String EMUSO = "emuso";
		public static final String TELEFONE = "cttelefone";
		public static final String COLUNAS[] = new String[] { _ID, OPERADORA,
				BANDEIRA, FECHAMENTO, VENCIMENTO, LIMITE, EMUSO, TELEFONE };

	}

	/**
	 * 
	 * @author cmatos
	 * 
	 */
	public static class DBHCredor {

		public static final String TABELA = "tbcredor";
		public static final String _ID = "_id";
		public static final String RAZAO = "razao";
		public static final String TELEFONE = "telefone";
		public static final String EMAIL = "email";
		public static final String ATIVO = "ativo";
		public static final String COLUNAS[] = new String[] { _ID, RAZAO,
				TELEFONE, ATIVO };

	}

	/**
	 * 
	 * @author cmatos
	 * 
	 */
	public static class DBHDevedor {

		public static final String TABELA = "tbdevedor";
		public static final String _ID = "_id";
		public static final String NOME = "devnome";
		public static final String TELEFONE = "dvtelefone";
		public static final String EMAIL = "dvemail";
		public static final String ATIVO = "dvativo";
		public static final String COLUNAS[] = new String[] { _ID, NOME,
				TELEFONE, EMAIL, ATIVO };

	}

	/**
	 * 
	 * @author cmatos
	 * 
	 */
	public static class DBHTipo {

		public static final String TABELA = "tbtipo";
		public static final String _ID = "_id";
		public static final String TIPO = "tipo";
		public static final String COLUNAS[] = new String[] { _ID, TIPO };

	}

	/**
	 * 
	 * @author cmatos
	 * 
	 */
	public static class DBHLimite {

		public static final String TABELA = "tblimite";
		public static final String _ID = "_id";
		public static final String DIAMES = "diames";
		public static final String LIMITEPESSOAL = "limitepessoal";
		public static final String IDDEVEDOR = "iddevedor";
		public static final String COLUNAS[] = new String[] { _ID, DIAMES,
				LIMITEPESSOAL , IDDEVEDOR};

	}

	/**
	 * 
	 * @author cmatos
	 * 
	 */
	public static class DBHDebito {

		public static final String TABELA = "tbdebito";
		public static final String _ID = "_id";
		public static final String DATADEBITO = "datadebito";
		public static final String FORMPAG = "formpag";
		public static final String SINCRO = "sincro";
		public static final String VLDEBITO = "vldebito";
		public static final String DESCRICAO = "descricao";
		public static final String IDCARTAO = "idcartao";
		public static final String ORDEM = "ordem";
		public static final String IDTIPO = "idtipo";
		public static final String IDCREDOR = "idcredor";
		public static final String IDDEVEDOR = "iddevedor";

		public static final String COLUNAS[] = new String[] { _ID, };

	}

	/**
	 * 
	 * @author cmatos
	 * 
	 */
	public static class DBHParcela {

		public static final String TABELA = "tbparcela";
		public static final String _ID = "_id";
		public static final String VLPARCELA = "vlparcela";
		public static final String NRPARCELA = "nrparcela";
		public static final String DATAVENCIMENTO = "datavencimento";
		public static final String PAGO = "pago";
		public static final String DATAPAGAMENTO = "datapagamento";
		public static final String IDDEBITO = "iddebito";
		public static final String IDDEVEDOR = "iddevedor";
		public static final String COLUNAS[] = new String[] { _ID, };

	}
}
