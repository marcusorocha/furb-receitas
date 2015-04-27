package br.com.receitas.bean;

public class EspeciariaBean
{
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
