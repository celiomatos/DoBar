package br.com.dobar.daos.entities;

import java.math.BigDecimal;

public class Debito {

	private Long iddebito;
	private String dataDebito;
	private Integer formPagamento;
	private String sincro;
	private BigDecimal vlDebito;
	private String descricao;
	private Integer ordem;
	private Cartao cartao;
	private Tipo tipo;
	private Credor credor;
	private Devedor devedor;

	/**
	 * 
	 */
	public Debito() {
	}

	/**
	 * 
	 * @param dataDebito
	 * @param formPagamento
	 * @param vlDebito
	 * @param descricao
	 * @param ordem
	 */
	public Debito(String dataDebito, String formPagamento,
			String vlDebito, String descricao, String ordem) {
		this.dataDebito = dataDebito;
		this.formPagamento = Integer.parseInt(formPagamento);
		this.vlDebito = new BigDecimal(vlDebito);
		this.descricao = descricao;
		this.ordem = Integer.parseInt(ordem);
	}

	/**
	 * 
	 * @return
	 */
	public Long getIddebito() {
		return iddebito;
	}

	/**
	 * 
	 * @param iddebito
	 */
	public void setIddebito(Long iddebito) {
		this.iddebito = iddebito;
	}

	/**
	 * 
	 * @return
	 */
	public String getDataDebito() {
		return dataDebito;
	}

	/**
	 * 
	 * @param dataDebito
	 */
	public void setDataDebito(String dataDebito) {
		this.dataDebito = dataDebito;
	}

	/**
	 * 
	 * @return
	 */
	public Integer getFormPagamento() {
		return formPagamento;
	}

	/**
	 * 
	 * @param formPagamento
	 */
	public void setFormPagamento(Integer formPagamento) {
		this.formPagamento = formPagamento;
	}

	/**
	 * 
	 * @return
	 */
	public String getSincro() {
		return sincro;
	}

	/**
	 * 
	 * @param sincro
	 */
	public void setSincro(String sincro) {
		this.sincro = sincro;
	}

	/**
	 * 
	 * @return
	 */
	public BigDecimal getVlDebito() {
		return vlDebito;
	}

	/**
	 * 
	 * @param vlDebito
	 */
	public void setVlDebito(BigDecimal vlDebito) {
		this.vlDebito = vlDebito;
	}

	/**
	 * 
	 * @return
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * 
	 * @param descricao
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	/**
	 * 
	 * @return
	 */
	public Integer getOrdem() {
		return ordem;
	}

	/**
	 * 
	 * @param ordem
	 */
	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	/**
	 * 
	 * @return
	 */
	public Cartao getCartao() {
		return cartao;
	}

	/**
	 * 
	 * @param cartao
	 */
	public void setCartao(Cartao cartao) {
		this.cartao = cartao;
	}

	/**
	 * 
	 * @return
	 */
	public Tipo getTipo() {
		return tipo;
	}

	/**
	 * 
	 * @param tipo
	 */
	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	/**
	 * 
	 * @return
	 */
	public Credor getCredor() {
		return credor;
	}

	/**
	 * 
	 * @param credor
	 */
	public void setCredor(Credor credor) {
		this.credor = credor;
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
