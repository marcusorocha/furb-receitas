package br.com.receitas.bean;

import java.util.Date;

public class UsuarioBean 
{
	private int oid;
	private String nome;
	private String senha;
	private String email;
	private Date ultimoAcesso;
	
	public int getOID()
	{
		return oid;
	}
	
	public void setOID(int oid)
	{
		this.oid = oid;
	}
	
	public String getNome()
	{
		return nome;
	}
	
	public void setNome(String nome)
	{
		this.nome = nome;
	}
	
	public String getSenha()
	{
		return senha;
	}
	
	public void setSenha(String senha)
	{
		this.senha = senha;
	}
	
	public String getEmail()
	{
		return email;
	}
	
	public void setEmail(String email)
	{
		this.email = email;
	}
	
	public Date getUltimoAcesso()
	{
		return ultimoAcesso;
	}
	
	public void setUltimoAcesso(Date ultimoAcesso)
	{
		this.ultimoAcesso = ultimoAcesso;
	}
}