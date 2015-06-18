package br.furb.receitas.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.furb.receitas.bean.IngredienteBean;
import br.furb.receitas.db.ConexaoSQL;

public class IngredienteDAO 
{
	private static final String TABELA_ID = "INGREDIENTE_ID";
	
	public static boolean salvar(IngredienteBean ingrediente) throws SQLException
	{
		if (ingrediente.getOID() == 0)
			return incluir(ingrediente);
		else
			return atualizar(ingrediente);
	}
	
	public static boolean incluir(IngredienteBean ingrediente) throws SQLException
	{
		String sql = "insert into INGREDIENTE (id, id_receita, id_especiaria, unidade, quantidade) values (?, ?, ?, ?, ?)";
		
		ConexaoSQL conSQL = new ConexaoSQL();
		try
		{
			conSQL.abrirConexao();
			
			ingrediente.setOID(conSQL.proximoID(TABELA_ID));
			
			PreparedStatement ps = conSQL.getConexao().prepareStatement(sql);
			try
			{
				ps.setInt(1, ingrediente.getOID());
				ps.setInt(2, ingrediente.getReceita());
				ps.setInt(3, ingrediente.getOIDEspeciaria());
				ps.setString(4, ingrediente.getUnidade());
				ps.setDouble(5, ingrediente.getQuantidade());
				
				return ps.executeUpdate() > 0;
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
	
	public static boolean atualizar(IngredienteBean ingrediente) throws SQLException
	{
		String sql = "update INGREDIENTE set id_receita = ?, id_especiaria = ?, unidade = ?, quantidade = ? where id = ?";
		
		ConexaoSQL conSQL = new ConexaoSQL();
		try
		{
			conSQL.abrirConexao();		
			
			PreparedStatement ps = conSQL.getConexao().prepareStatement(sql);
			try
			{				
				ps.setInt(1, ingrediente.getReceita());
				ps.setInt(2, ingrediente.getOIDEspeciaria());
				ps.setString(3, ingrediente.getUnidade());
				ps.setDouble(4, ingrediente.getQuantidade());
				
				ps.setInt(5, ingrediente.getOID());
				
				return ps.executeUpdate() > 0;
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
	
	public static boolean excluir(int id) throws SQLException
	{
		String sql = "delete from INGREDIENTE where id = ?";
		
		ConexaoSQL conSQL = new ConexaoSQL();
		try
		{
			conSQL.abrirConexao();
			
			PreparedStatement ps = conSQL.getConexao().prepareStatement(sql);
			try
			{
				ps.setInt(1, id);
				
				return ps.executeUpdate() > 0;
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
		String sql = "delete from INGREDIENTE";
		
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
	
	public static IngredienteBean localizar(int oid) throws SQLException
	{
		String sql = "select * from INGREDIENTE where id = ?";
		
		ConexaoSQL conSQL = new ConexaoSQL();
		try
		{
			conSQL.abrirConexao();
			
			PreparedStatement ps = conSQL.getConexao().prepareStatement(sql);
			try
			{
				ps.setInt(1, oid);
				
				ResultSet rs = ps.executeQuery();
				if (rs.next())
				{
					IngredienteBean ingrediente = new IngredienteBean();
					
					ingrediente.setOID(rs.getInt("id"));
					ingrediente.setReceita(rs.getInt("id_receita"));
					ingrediente.setOIDEspeciaria(rs.getInt("id_especiaria"));
					ingrediente.setUnidade(rs.getString("unidade"));
					ingrediente.setQuantidade(rs.getDouble("quantidade"));
					
					return ingrediente;
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
	
	public static List<IngredienteBean> listarDaReceita(int receita) throws SQLException
	{
		String sql = "select * from INGREDIENTE where id_receita = ?";
		
		List<IngredienteBean> ingredientes = new ArrayList<IngredienteBean>();
		
		ConexaoSQL conSQL = new ConexaoSQL();
		try
		{
			conSQL.abrirConexao();
			
			PreparedStatement ps = conSQL.getConexao().prepareStatement(sql);
			try
			{
				ps.setInt(1, receita);
				
				ResultSet rs = ps.executeQuery();
				
				while (rs.next())
				{
					IngredienteBean ingrediente = new IngredienteBean();
					
					ingrediente.setOID(rs.getInt("id"));
					ingrediente.setReceita(rs.getInt("id_receita"));
					ingrediente.setOIDEspeciaria(rs.getInt("id_especiaria"));
					ingrediente.setUnidade(rs.getString("unidade"));
					ingrediente.setQuantidade(rs.getDouble("quantidade"));
					
					ingredientes.add(ingrediente);
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
		
		return ingredientes;
	}
}
