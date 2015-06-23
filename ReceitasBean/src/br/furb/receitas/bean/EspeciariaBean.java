package br.furb.receitas.bean;

import java.io.Serializable;

public class EspeciariaBean implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int oid;
	private String nome;
	
	public EspeciariaBean()
	{
		this("");
	}
	
	public EspeciariaBean(String nome)
	{
		this.nome = nome;
	}
	
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

}
