package br.com.receitas.bean;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ReceitaBean 
{
	private int oid;
	private String descricao;
	private int usuario;
	
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
}
