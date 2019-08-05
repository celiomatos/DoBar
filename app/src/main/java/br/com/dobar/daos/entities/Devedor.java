package br.com.dobar.daos.entities;

public class Devedor {

	private Long idDevedor;
	private String nome;
	private String telefone;
	private String email;
	private String ativo;

	/**
	 * 
	 */
	public Devedor() {
	}

	/**
	 * 
	 * @param nome
	 * @param telefone
	 * @param email
	 * @param ativo
	 */
	public Devedor(String nome, String telefone, String email, String ativo) {
		this.nome = nome;
		this.telefone = telefone;
		this.email = email;
		this.ativo = ativo;
	}

	/**
	 * 
	 * @return
	 */
	public Long getIdDevedor() {
		return idDevedor;
	}

	/**
	 * 
	 * @param idDevedor
	 */
	public void setIdDevedor(Long idDevedor) {
		this.idDevedor = idDevedor;
	}

	/**
	 * 
	 * @return
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * 
	 * @param nome
	 */
	public void setNome(String nome) {
		this.nome = nome;
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
