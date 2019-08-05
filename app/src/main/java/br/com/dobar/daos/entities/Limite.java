package br.com.dobar.daos.entities;

public class Limite {

	private Long idLimite;
	private Integer dia;
	private Integer limitePessoal;
	private Devedor devedor;

	/**
	 * 
	 * @return
	 */
	public Long getIdLimite() {
		return idLimite;
	}

	/**
	 * 
	 * @param idLimite
	 */
	public void setIdLimite(Long idLimite) {
		this.idLimite = idLimite;
	}

	/**
	 * 
	 * @return
	 */
	public Integer getDia() {
		return dia;
	}

	/**
	 * 
	 * @param dia
	 */
	public void setDia(Integer dia) {
		this.dia = dia;
	}

	/**
	 * 
	 * @return
	 */
	public Integer getLimitePessoal() {
		return limitePessoal;
	}

	/**
	 * 
	 * @param limitePessoal
	 */
	public void setLimitePessoal(Integer limitePessoal) {
		this.limitePessoal = limitePessoal;
	}

	/**
	 * 
	 * @return
	 */
	public Devedor getDevedor() {
		return devedor;
	}

	/**
	 * 
	 * @param devedor
	 */
	public void setDevedor(Devedor devedor) {
		this.devedor = devedor;
	}

}
