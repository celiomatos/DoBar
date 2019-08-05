package br.com.dobar.daos.entities;

public class Tipo {

	private Long idTipo;
	private String tipo;
	
	/**
	 * 
	 */

	public Tipo() {		
	}

	/**
	 * 
	 * @param tipo
	 */
	public Tipo(String tipo) {		
		this.tipo = tipo;
	}

	/**
	 * 
	 * @return
	 */
	public Long getIdTipo() {
		return idTipo;
	}

	/**
	 * 
	 * @param idTipo
	 */
	public void setIdTipo(Long idTipo) {
		this.idTipo = idTipo;
	}

	/**
	 * 
	 * @return
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * 
	 * @param tipo
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
}
