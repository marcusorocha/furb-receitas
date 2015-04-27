package br.furb.receitas.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.furb.receitas.bean.ReceitaBean;
import br.furb.receitas.db.ConexaoSQL;

public class ReceitaDAO 
{
	private static final String TABELA_ID = "RECEITA_ID";
	
	public static void incluir(ReceitaBean receita) throws SQLException
	{
		String sql = "insert into RECEITA (id, descricao, id_usuario) values (?, ?, ?)";
		
		ConexaoSQL conSQL = new ConexaoSQL();
		try
		{
			conSQL.abrirConexao();
			
			receita.setOID(conSQL.proximoID(TABELA_ID));
			
			PreparedStatement ps = conSQL.getConexao().prepareStatement(sql);
			try
			{
				ps.setInt(1, receita.getOID());
				ps.setString(2, receita.getDescricao());
				ps.setInt(3, receita.getUsuario());
				
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
		String sql = "delete from RECEITA";
		
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
	
	public static ReceitaBean localizar(int oid) throws SQLException
	{
		String sql = "select * from RECEITA where id = ?";		
		
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
					ReceitaBean receita = new ReceitaBean();
					
					receita.setOID(rs.getInt("id"));
					receita.setUsuario(rs.getInt("id_usuario"));
					receita.setDescricao(rs.getString("descricao"));
					
					return receita;
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
	
	public static List<ReceitaBean> listarDoUsuario(int usuario) throws SQLException
	{
		String sql = "select * from RECEITA where id_usuario = ?";
		
		List<ReceitaBean> receitas = new ArrayList<ReceitaBean>();
		
		ConexaoSQL conSQL = new ConexaoSQL();
		try
		{
			conSQL.abrirConexao();
			
			PreparedStatement ps = conSQL.getConexao().prepareStatement(sql);
			try
			{
				ps.setInt(1, usuario);
				
				ResultSet rs = ps.executeQuery();
				
				while (rs.next())
				{
					ReceitaBean receita = new ReceitaBean();
					
					receita.setOID(rs.getInt("id"));
					receita.setUsuario(rs.getInt("id_usuario"));
					receita.setDescricao(rs.getString("descricao"));
					
					receitas.add(receita);
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
		
		return receitas;
	}
}
