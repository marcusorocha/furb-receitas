package br.furb.receitas.bean;

import java.io.Serializable;

public class IngredienteBean implements Serializable
{
	private static final long serialVersionUID = 6054651650818599552L;
	
	private int oid;	
	private int receita;
	private String nome;
	private int especiaria;
	private String unidade;
	private double quantidade;
	
	public IngredienteBean()
	{
		this.oid = 0;
		this.receita = 0;
		this.nome = "";
		this.especiaria = 0;
		this.unidade = "";
		this.quantidade = 0.0;
	}
	
	public IngredienteBean(ReceitaBean receita, String nome)
	{
		this(receita, nome, 0);
	}
	
	public IngredienteBean(ReceitaBean receita, String nome, double quantidade)
	{		
		this(receita, nome, quantidade, "");
	}
	
	public IngredienteBean(ReceitaBean receita, String nome, double quantidade, String unidade)
	{		
		this.receita = receita.getOID();
		this.nome = nome;
		this.unidade = unidade;
		this.quantidade = quantidade;
	}
	
	public IngredienteBean(ReceitaBean receita, EspeciariaBean especiaria, double quantidade)
	{		
		this(receita, especiaria, quantidade, "");
	}
	
	public IngredienteBean(ReceitaBean receita, EspeciariaBean especiaria, double quantidade,  String unidade)
	{		
		this(receita, especiaria.getNome(), quantidade, unidade);		
		
		this.especiaria = especiaria.getOID();
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
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) 
	{
		this.nome = nome;
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
	
	@Override
	public String toString() 
	{
		return getNome();
	}

}
