package br.com.dobar.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.dobar.daos.entities.Cartao;
import br.com.dobar.daos.entities.Credor;
import br.com.dobar.daos.entities.Devedor;
import br.com.dobar.daos.entities.Tipo;

public class Utils {

	/**
	 * 
	 * @return
	 */
	public static List<String> getListNomeDevedores(List<Devedor> list) {

		List<String> listDevedores = new ArrayList<String>();
		for (Devedor devedor : list) {
			listDevedores.add(devedor.getNome());
		}
		return listDevedores;
	}

	/**
	 * 
	 * @param list
	 * @return
	 */
	public static List<String> getListNomeCredores(List<Credor> list) {

		List<String> listDevedores = new ArrayList<String>();
		for (Credor credor : list) {
			listDevedores.add(credor.getRazao());
		}
		return listDevedores;
	}

	/**
	 * 
	 * @param list
	 * @return
	 */
	public static List<String> getListCartoes(List<Cartao> list) {

		List<String> listCartoes = new ArrayList<String>();
		for (Cartao cartao : list) {
			listCartoes.add(cartao.getOperadora());
		}
		return listCartoes;
	}
	/**
	 * 
	 * @param list
	 * @return
	 */
	public static List<String> getListCartoesWithCartao(List<Cartao> list) {

		List<String> listCartoes = new ArrayList<String>();
		for (Cartao cartao : list) {
			listCartoes.add("CARTAO: "+cartao.getOperadora());
		}
		return listCartoes;
	}

	/**
	 * 
	 * @param list
	 * @return
	 */
	public static List<String> getListTipos(List<Tipo> list) {
		List<String> listTipos = new ArrayList<String>();
		for (Tipo tipo : list) {
			listTipos.add(tipo.getTipo());
		}
		return listTipos;
	}

	/**
	 * 
	 * @return
	 */
	public static List<Integer> getListDias() {
		List<Integer> dias = new ArrayList<Integer>();
		for (int i = 0; i < 31; i++) {
			dias.add(i + 1);
		}
		return dias;
	}

	/**
	 * 
	 * @param dia
	 * @param mes
	 * @param ano
	 * @return
	 */
	public static String getDDMMYYYY(int dia, int mes, int ano) {
		return (dia > 9 ? dia : "0" + dia) + "/" + (mes > 9 ? mes : "0" + mes)
				+ "/" + ano;
	}

	/**
	 * 
	 * @param dia
	 * @param mes
	 * @param ano
	 * @return
	 */
	public static int[] getProximoMes(int dia, int mes, int ano) {

		if (mes == Constantes.DEZEMBRO) {
			mes = Constantes.JANEIRO;
			ano += 1;
		} else {
			mes += 1;
		}
		if (mes == Constantes.FEVEREIRO) {
			if (dia > 28) {
				dia = 28;
			}
		}
		if (dia > 30) {
			dia = 30;
		}
		int[] data = { dia, mes, ano };
		return data;
	}

	/**
	 * 
	 * @param strDate
	 * @return
	 */
	public static java.sql.Date getProximoMes(String strDate) {
		String[] vtDate = strDate.split("-");
		int dia = Integer.parseInt(vtDate[2]);
		int mes = Integer.parseInt(vtDate[1]);
		int ano = Integer.parseInt(vtDate[0]);
		int[] prox = Utils.getProximoMes(dia, mes, ano);
		return getSqlDate(getDDMMYYYY(prox[Constantes.DIA],
				prox[Constantes.MES], prox[Constantes.ANO]));
	}

	/**
	 * 
	 * @param string
	 * @param tamanho
	 * @return
	 */
	public static String completaDigitos(String string, int tamanho) {
		StringBuilder builder = new StringBuilder(string);
		while (builder.length() < tamanho) {
			builder.insert(0, "0");
		}
		return builder.toString();
	}

	/**
	 * 
	 * @param string
	 * @param tamanho
	 * @return
	 */
	public static String completaEspacos(String string, int tamanho) {
		StringBuilder builder = new StringBuilder(string);
		while (builder.length() < tamanho) {
			builder.append(" ");
		}
		return builder.toString();
	}

	/**
	 * 
	 * @param strdate
	 * @return
	 */
	public static java.sql.Date getSqlDate(String strdate) {
		if (strdate.contains("/")) {
			String[] vdate = strdate.split("/");
			strdate = vdate[Constantes.ANO] + "-" + vdate[Constantes.MES] + "-"
					+ vdate[Constantes.DIA];
		}
		java.sql.Date sqlDate = null;
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");

		try {
			sqlDate = new java.sql.Date(formater.parse(strdate).getTime());
		} catch (ParseException ex) {
			ex.getMessage();
		}
		return sqlDate;
	}

	/**
	 * 
	 * @param strDate
	 * @return
	 */
	public static String getDDMMYYYY(String strDate) {
		int[] vdate = splitDate(strDate);
		return getDDMMYYYY(vdate[Constantes.DIA], vdate[Constantes.MES],
				vdate[Constantes.ANO]);
	}

	/**
	 * 
	 * @param strDate
	 * @param retornar
	 * @return
	 */
	public static String getMesAnterior(String strDate, int retornar) {
		int[] vdate = splitDate(strDate);
		int dia = vdate[Constantes.DIA];
		int mes = vdate[Constantes.MES];
		int ano = vdate[Constantes.ANO];

		for (int i = 0; i < retornar; i++) {
			if (mes > Constantes.JANEIRO) {
				mes--;
			} else {
				mes = Constantes.DEZEMBRO;
				ano--;
			}
		}
		if (mes == Constantes.FEVEREIRO) {
			if (dia > 28) {
				dia = 28;
			}
		}
		if (dia > 30) {
			dia = 30;
		}

		return getDDMMYYYY(dia, mes, ano);
	}

	/**
	 * 
	 * @return
	 */
	public static int[] splitDate(String strDate) {
		String[] vdate;
		int dia;
		int mes;
		int ano;
		if (strDate.contains("/")) {
			vdate = strDate.split("/");
			dia = Integer.parseInt(vdate[0]);
			mes = Integer.parseInt(vdate[1]);
			ano = Integer.parseInt(vdate[2]);
		} else {
			vdate = strDate.split("-");
			dia = Integer.parseInt(vdate[2]);
			mes = Integer.parseInt(vdate[1]);
			ano = Integer.parseInt(vdate[0]);
		}
		return new int[] { dia, mes, ano };
	}

	/**
	 * 
	 * @return
	 */
	public static String getUltimoDiaMesAtual() {
		Calendar calendar = Calendar.getInstance();
		int dia = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		int mes = calendar.get(Calendar.MONTH) + 1;
		int ano = calendar.get(Calendar.YEAR);
		return getDDMMYYYY(dia, mes, ano);
	}
}
