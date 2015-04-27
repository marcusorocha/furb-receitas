package br.com.receitas.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.receitas.bean.IngredienteBean;
import br.com.receitas.db.ConexaoSQL;

public class IngredienteDAO 
{
	private static final String TABELA_ID = "INGREDIENTE_ID";
	
	public static void incluir(IngredienteBean ingrediente) throws SQLException
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
				ps.setInt(3, ingrediente.getEspeciaria());
				ps.setString(4, ingrediente.getUnidade());
				ps.setDouble(5, ingrediente.getQuantidade());
				
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
					ingrediente.setEspeciaria(rs.getInt("id_especiaria"));
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
