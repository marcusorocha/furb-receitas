package br.furb.receitas.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

public class ConexaoSQL 
{		
	// Constantes de erros da base de dados
	public static final int CHAVE_UNICA_VIOLADA = 2627;
	
	// Create a variable for the connection string.
	private final String servidor = "54.207.45.112";
	private final String baseDeDados = "RECEITAS";
	private final String usuario = "furb";
	private final String senha = "sql@2015";
	
	private Connection conexao;
	
	public ConexaoSQL()
	{
		conexao = null;
	}
	
	public void abrirConexao() throws SQLException
	{
		if (conexao == null)
		{
			SQLServerDataSource ds = new SQLServerDataSource();
			ds.setIntegratedSecurity(false);
			ds.setServerName(servidor);
			ds.setPortNumber(1433);
			ds.setDatabaseName(baseDeDados);
			ds.setUser(usuario);
			ds.setPassword(senha);
			
			conexao = ds.getConnection();
		}
	}
	
	public void fecharConexao() throws SQLException
	{
		if (conexao != null)
		{
			conexao.close();
			conexao = null;
		}
	}
	
	public Connection getConexao()
	{
		return conexao;
	}
	
	public Integer proximoID(String tabela) throws SQLException
	{
		if (conexao == null)
			abrirConexao();
		
		CallableStatement cs = conexao.prepareCall("{call dbo.PROXIMO_ID(?, ?)}");
		
		// Apenas para deixar registrado que pode ser passados os parâmetros
		// da StoreProcedure tanto por índice quanto pelo nome do parâmetro.
		//cs.setString("A_TABELA", tabela);
		//cs.registerOutParameter("A_PROX", Types.INTEGER);
		
		cs.setString(1, tabela);
		cs.registerOutParameter(2, Types.INTEGER);
		
		cs.execute();
		
		return cs.getInt(2);
	}
	
	public String gerarParametros(int quantidade)
	{
		StringBuilder sb = new StringBuilder();
		while (sb.length() < quantidade) 
		{ 
		    if (sb.length() > 0) sb.append(',');
		    sb.append("?");
		}
		return sb.toString();
	}

}
