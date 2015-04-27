package br.com.receitas.bean;

public class IngredienteBean
{
	private int oid;	
	private int receita;
	private int especiaria;
	
	private String unidade;
	private double quantidade;
	
	public IngredienteBean()
	{
		this.receita = 0;
		this.especiaria = 0;
		this.unidade = "";
		this.quantidade = 0.0;
	}
	
	public IngredienteBean(ReceitaBean receita, EspeciariaBean especiaria)
	{
		this(receita, especiaria, 0);
	}
	
	public IngredienteBean(ReceitaBean receita, EspeciariaBean especiaria, double quantidade)
	{		
		this(receita, especiaria, quantidade, "");
	}
	
	public IngredienteBean(ReceitaBean receita, EspeciariaBean especiaria, double quantidade,  String unidade)
	{		
		this.receita = receita.getOID();
		this.especiaria = especiaria.getOID();
		this.unidade = unidade;
		this.quantidade = quantidade;
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
	
	public int getEspeciaria()
	{
		return especiaria;
	}
	
	public void setEspeciaria(int especiaria)
	{
		this.especiaria = especiaria;
	}
	
	public String getUnidade()
	{
		return unidade;
	}
	
	public void setUnidade(String unidade)
	{
		this.unidade = unidade;
	}
	
	public double getQuantidade()
	{
		return quantidade;
	}
	
	public void setQuantidade(double quantidade)
	{
		this.quantidade = quantidade;
	}
}
