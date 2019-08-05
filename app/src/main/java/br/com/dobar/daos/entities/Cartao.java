package br.com.dobar.daos.entities;


public class Cartao {

	private Long idcartao;
	private String operadora;
	private String bandeira;
	private Integer diaFechamento;
	private Integer diaVencimento;
	private String emUso;
	private Integer limite;
	private String telefone;

	/**
	 * 
	 */
	public Cartao() {		
	}

	/**
	 * 
	 * @param operadora
	 * @param bandeira
	 * @param diaFechamento
	 * @param diaVencimento
	 * @param emUso
	 * @param limite
	 * @param telefone
	 */
	public Cartao(String operadora, String bandeira, String diaFechamento,
			String diaVencimento, String emUso, String limite, String telefone) {		
		this.operadora = operadora;
		this.bandeira = bandeira;
		this.diaFechamento = Integer.parseInt(diaFechamento);
		this.diaVencimento = Integer.parseInt(diaVencimento);
		this.emUso = emUso;
		this.limite = Integer.parseInt(limite);
		this.telefone = telefone;
	}

	/**
	 * 
	 * @return
	 */
	public Long getIdcartao() {
		return idcartao;
	}

	/**
	 * 
	 * @param idcartao
	 */
	public void setIdcartao(Long idcartao) {
		this.idcartao = idcartao;
	}

	/**
	 * 
	 * @return
	 */
	public String getOperadora() {
		return operadora;
	}

	/**
	 * 
	 * @param operadora
	 */
	public void setOperadora(String operadora) {
		this.operadora = operadora;
	}

	/**
	 * 
	 * @return
	 */
	public String getBandeira() {
		return bandeira;
	}

	/**
	 * 
	 * @param bandeira
	 */
	public void setBandeira(String bandeira) {
		this.bandeira = bandeira;
	}

	/**
	 * 
	 * @return
	 */
	public Integer getDiaFechamento() {
		return diaFechamento;
	}

	/**
	 * 
	 * @param diaFechamento
	 */
	public void setDiaFechamento(Integer diaFechamento) {
		this.diaFechamento = diaFechamento;
	}

	/**
	 * 
	 * @return
	 */
	public Integer getDiaVencimento() {
		return diaVencimento;
	}

	/**
	 * 
	 * @param diaVencimento
	 */
	public void setDiaVencimento(Integer diaVencimento) {
		this.diaVencimento = diaVencimento;
	}

	/**
	 * 
	 * @return
	 */
	public String getEmUso() {
		return emUso;
	}

	/**
	 * 
	 * @param emUso
	 */
	public void setEmUso(String emUso) {
		this.emUso = emUso;
	}

	/**
	 * 
	 * @return
	 */
	public Integer getLimite() {
		return limite;
	}

	/**
	 * 
	 * @param limite
	 */
	public void setLimite(Integer limite) {
		this.limite = limite;
	}

	/**
	 * 
	 * @return
	 */
	public String getTelefone() {
		return telefone;
	}

	/**
	 * 
	 * @param telefone
	 */
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

}
