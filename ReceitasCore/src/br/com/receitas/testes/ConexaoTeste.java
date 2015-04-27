package br.com.receitas.testes;

import static org.junit.Assert.*;

import org.junit.Test;

import br.com.receitas.db.ConexaoSQL;

public class ConexaoTeste {

	@Test
	public void testarConexao() 
	{
		try
		{
			ConexaoSQL conSQL = new ConexaoSQL();
			try
			{
				conSQL.abrirConexao();
							
				assertNotNull(conSQL.getConexao());
			}
			finally
			{
				conSQL.fecharConexao();
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			
			fail();
		}
	}
	
	@Test
	public void testarProximoID()
	{
		try
		{
			ConexaoSQL conSQL = new ConexaoSQL();
			try
			{
				conSQL.abrirConexao();
				
				int pid = conSQL.proximoID("RECEITA_ID");
							
				assertTrue(pid > 0);
			}
			finally
			{
				conSQL.fecharConexao();
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			
			fail();
		}
	}

}
