package br.com.dobar.beans;

public class GroupItem {

	private String strGrupo;
	private String strValor;
	private String strEmail;
	private String strFone;

	/**
	 * 
	 * @return
	 */
	public String getStrGrupo() {
		return strGrupo;
	}

	/**
	 * 
	 * @param strGrupo
	 */
	public void setStrGrupo(String strGrupo) {
		this.strGrupo = strGrupo;
	}

	/**
	 * 
	 * @return
	 */
	public String getStrValor() {
		return strValor;
	}

	/**
	 * 
	 * @param strValor
	 */
	public void setStrValor(String strValor) {
		this.strValor = strValor;
	}

	/**
	 * 
	 * @return
	 */
	public String getStrEmail() {
		return strEmail;
	}

	/**
	 * 
	 * @param strEmail
	 */
	public void setStrEmail(String strEmail) {
		this.strEmail = strEmail;
	}

	/**
	 * 
	 * @return
	 */
	public String getStrFone() {
		return strFone;
	}

	/**
	 * 
	 * @param strFone
	 */
	public void setStrFone(String strFone) {
		this.strFone = strFone;
	}

	/**
	 * 
	 */
	@Override
	public boolean equals(Object obj) {

		if (obj instanceof GroupItem) {
			if (((GroupItem) obj).strGrupo.equalsIgnoreCase(this.getStrGrupo())) {
				return true;
			}
		}
		return false;
	}

}
