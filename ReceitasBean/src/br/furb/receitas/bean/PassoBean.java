package br.furb.receitas.bean;

public class PassoBean
{
	private int oid;
	private int receita;
	private int sequencia;
	private String descricao;
	
	public PassoBean()
	{
		receita = 0;
		sequencia = 0;
		descricao = "";
	}
	
	public PassoBean(ReceitaBean receita, int sequencia, String descricao)
	{
		this.receita = receita.getOID();
		this.sequencia = sequencia;
		this.descricao = descricao;
	}
	
	public int getOID()
	{
		return oid;
	}

	public void setOID(int oid)
	{
		this.oid = oid;
	}

	public int getReceita()
	{
		return receita;
	}
	
	public void setReceita(int receita)
	{
		this.receita = receita;
	}

	public int getSequencia()
	{
		return sequencia;
	}

	public void setSequencia(int sequencia)
	{
		this.sequencia = sequencia;
	}

	public String getDescricao()
	{
		return descricao;
	}

	public void setDescricao(String descricao)
	{
		this.descricao = descricao;
	}

}