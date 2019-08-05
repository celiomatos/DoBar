package br.com.dobar.daos.entities;

import java.math.BigDecimal;

public class Parcela {
	
	private Long idparcela;
	private BigDecimal vlParcela;
	private String nrParcela;
	private String dataVencimento;
	private String pago;
	private String dataPagamento;
	private Debito debito;
	private Devedor devedor;

	/**
	 * 
	 */
	public Parcela() {
	}

	/**
	 * 
	 * @param vlParcela
	 * @param nrParcela
	 * @param dataVencimento
	 * @param pago
	 * @param dataPagamento
	 */
	public Parcela(String vlParcela, String nrParcela,
			String dataVencimento, String pago, String dataPagamento) {
		this.vlParcela = new BigDecimal(vlParcela);
		this.nrParcela = nrParcela;
		this.dataVencimento = dataVencimento;
		this.pago = pago;
		this.dataPagamento = dataPagamento;
	}

	/**
	 * 
	 * @return
	 */
	public Long getIdparcela() {
		return idparcela;
	}

	/**
	 * 
	 * @param idparcela
	 */
	public void setIdparcela(Long idparcela) {
		this.idparcela = idparcela;
	}

	/**
	 * 
	 * @return
	 */
	public BigDecimal getVlParcela() {
		return vlParcela;
	}

	/**
	 * 
	 * @param vlParcela
	 */
	public void setVlParcela(BigDecimal vlParcela) {
		this.vlParcela = vlParcela;
	}

	/**
	 * 
	 * @return
	 */
	public String getNrParcela() {
		return nrParcela;
	}

	/**
	 * 
	 * @param nrParcela
	 */
	public void setNrParcela(String nrParcela) {
		this.nrParcela = nrParcela;
	}

	/**
	 * 
	 * @return
	 */
	public String getDataVencimento() {
		return dataVencimento;
	}

	/**
	 * 
	 * @param dataVencimento
	 */
	public void setDataVencimento(String dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	/**
	 * 
	 * @return
	 */
	public String getPago() {
		return pago;
	}

	/**
	 * 
	 * @param pago
	 */
	public void setPago(String pago) {
		this.pago = pago;
	}

	/**
	 * 
	 * @return
	 */
	public String getDataPagamento() {
		return dataPagamento.toString();
	}

	/**
	 * 
	 * @param dataPagamento
	 */
	public void setDataPagamento(String dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	/**
	 * 
	 * @return
	 */
	public Debito getDebito() {
		return debito;
	}

	/**
	 * 
	 * @param debito
	 */
	public void setDebito(Debito debito) {
		this.debito = debito;
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
