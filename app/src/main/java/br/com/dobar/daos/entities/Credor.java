package br.com.dobar.daos.entities;

public class Credor {
	private Long idCredor;
	private String razao;
	private String telefone;
	private String email;
	private String ativo;

	/**
	 * 
	 */
	public Credor() {

	}

	/**
	 * 
     * @param razao
	 * @param telefone
	 * @param email
	 * @param ativo
	 */
	public Credor(String razao, String telefone, String email, String ativo) {
		this.razao = razao;
		this.telefone = telefone;
		this.email = email;
		this.ativo = ativo;
	}

	/**
	 * 
	 * @return
	 */
	public Long getIdCredor() {
		return idCredor;
	}

	/**
	 * 
	 * @param idCredor
	 */
	public void setIdCredor(Long idCredor) {
		this.idCredor = idCredor;
	}

	/**
	 * 
	 * @return
	 */
	public String getRazao() {
		return razao;
	}

	/**
	 * 
	 * @param razao
	 */
	public void setRazao(String razao) {
		this.razao = razao;
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

	/**
	 * 
	 * @return
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * 
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * 
	 * @return
	 */
	public String getAtivo() {
		return ativo;
	}

	/**
	 * 
	 * @param ativo
	 */
	public void setAtivo(String ativo) {
		this.ativo = ativo;
	}

}
