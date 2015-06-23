package br.furb.receitas.bean;

import java.io.Serializable;

public class ReceitaBean implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private int oid;
	private String descricao;
	private int usuario;
	
	public ReceitaBean() { } 
	
	public int getOID()
	{
		return oid;
	}
	
	public void setOID(int oid)
	{
		this.oid = oid;
	}
	
	public String getDescricao()
	{
		return descricao;
	}
	
	public void setDescricao(String descricao)
	{
		this.descricao = descricao;
	}

	public int getUsuario()
	{
		return usuario;
	}

	public void setUsuario(int usuario)
	{
		this.usuario = usuario;
	}
	
	@Override
	public String toString() 
	{
		return getDescricao();
	}
}
