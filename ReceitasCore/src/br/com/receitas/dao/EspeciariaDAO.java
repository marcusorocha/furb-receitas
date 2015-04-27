package br.com.receitas.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import br.com.receitas.bean.EspeciariaBean;
import br.com.receitas.db.ConexaoSQL;

public class EspeciariaDAO 
{
	private static final String TABELA_ID = "ESPECIARIA_ID";
	
	public static void incluir(EspeciariaBean especiaria) throws SQLException
	{
		String sql = "insert into ESPECIARIA (id, nome) values (?, ?)";
		
		ConexaoSQL conSQL = new ConexaoSQL();
		try
		{
			conSQL.abrirConexao();
			
			especiaria.setOID(conSQL.proximoID(TABELA_ID));
			
			PreparedStatement ps = conSQL.getConexao().prepareStatement(sql);
			try
			{
				ps.setInt(1, especiaria.getOID());
				ps.setString(2, especiaria.getNome());
				
				ps.executeUpdate();
			}
			finally
			{
				ps.close();
			}
		}
		finally
		{
			conSQL.fecharConexao();
		}
	}
	
	public static void excluirTodos() throws SQLException
	{
		String sql = "delete from ESPECIARIA";
		
		ConexaoSQL conSQL = new ConexaoSQL();
		try
		{
			conSQL.abrirConexao();
			
			Statement st = conSQL.getConexao().createStatement();
			try
			{
				st.executeUpdate(sql);
			}
			finally
			{
				st.close();
			}
		}
		finally
		{
			conSQL.fecharConexao();
		}
	}
	
	public static EspeciariaBean localizar(String nome) throws SQLException
	{
		String sql = "select * from ESPECIARIA where upper(nome) = upper(?)";
		
		ConexaoSQL conSQL = new ConexaoSQL();
		try
		{
			conSQL.abrirConexao();
			
			PreparedStatement ps = conSQL.getConexao().prepareStatement(sql);
			try
			{
				ps.setString(1, nome);
				
				ResultSet rs = ps.executeQuery();
				if (rs.next())
				{
					EspeciariaBean especiaria = new EspeciariaBean();
					
					especiaria.setOID(rs.getInt("id"));
					especiaria.setNome(rs.getString("nome"));
					
					return especiaria;
				}
			}
			finally
			{
				ps.close();
			}
		}
		finally
		{
			conSQL.fecharConexao();
		}
		
		return null;
	}
	
	public static EspeciariaBean localizar(int id) throws SQLException
	{
		String sql = "select * from ESPECIARIA where id = ?";
		
		ConexaoSQL conSQL = new ConexaoSQL();
		try
		{
			conSQL.abrirConexao();
			
			PreparedStatement ps = conSQL.getConexao().prepareStatement(sql);
			try
			{
				ps.setInt(1, id);
				
				ResultSet rs = ps.executeQuery();
				if (rs.next())
				{
					EspeciariaBean especiaria = new EspeciariaBean();
					
					especiaria.setOID(rs.getInt("id"));
					especiaria.setNome(rs.getString("nome"));
					
					return especiaria;
				}
			}
			finally
			{
				ps.close();
			}
		}
		finally
		{
			conSQL.fecharConexao();
		}
		
		return null;
	}
}
