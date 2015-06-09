package br.furb.receitas.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.furb.receitas.bean.PassoBean;
import br.furb.receitas.db.ConexaoSQL;

public class PassoDAO
{
	private static final String TABELA_ID = "PASSO_ID";
	
	public static boolean salvar(PassoBean passo) throws SQLException
	{
		if (passo.getOID() == 0)
			return incluir(passo);
		else
			return atualizar(passo);
	}
	
	public static boolean incluir(PassoBean passo) throws SQLException
	{
		String sql = "insert into PASSO (id, id_receita, sequencia, descricao) values (?, ?, ?, ?)";
		
		ConexaoSQL conSQL = new ConexaoSQL();
		try
		{
			conSQL.abrirConexao();
			
			passo.setOID(conSQL.proximoID(TABELA_ID));
			
			PreparedStatement ps = conSQL.getConexao().prepareStatement(sql);
			try
			{
				ps.setInt(1, passo.getOID());
				ps.setInt(2, passo.getReceita());
				ps.setInt(3, passo.getSequencia());
				ps.setString(4, passo.getDescricao());
				
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
	
	public static boolean atualizar(PassoBean passo) throws SQLException
	{
		String sql = "update PASSO set id_receita = ?, sequencia = ?, descricao = ? where id = ?";
		
		ConexaoSQL conSQL = new ConexaoSQL();
		try
		{
			conSQL.abrirConexao();		
			
			PreparedStatement ps = conSQL.getConexao().prepareStatement(sql);
			try
			{				
				ps.setInt(1, passo.getReceita());
				ps.setInt(2, passo.getSequencia());
				ps.setString(3, passo.getDescricao());
				ps.setInt(4, passo.getOID());
				
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
	
	public static boolean excluir(int oid) throws SQLException
	{
		String sql = "delete from PASSO where id = ?";
		
		ConexaoSQL conSQL = new ConexaoSQL();
		try
		{
			conSQL.abrirConexao();
			
			PreparedStatement ps = conSQL.getConexao().prepareStatement(sql);
			try
			{
				ps.setInt(1, oid);
				
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
		String sql = "delete from PASSO";
		
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
	
	public static PassoBean localizar(int oid) throws SQLException
	{
		String sql = "select * from PASSO where id = ?";
		
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
					PassoBean passo = new PassoBean();
					
					passo.setOID(rs.getInt("id"));
					passo.setReceita(rs.getInt("id_receita"));
					passo.setSequencia(rs.getInt("sequencia"));
					passo.setDescricao(rs.getString("descricao"));
					
					return passo;
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
	
	public static List<PassoBean> listarDaReceita(int receita) throws SQLException
	{
		List<PassoBean> passos = new ArrayList<PassoBean>();
		
		ConexaoSQL conSQL = new ConexaoSQL();
		try
		{
			String sql = "select * from PASSO where id_receita = ?";
			
			conSQL.abrirConexao();
			
			PreparedStatement ps = conSQL.getConexao().prepareStatement(sql);
			try
			{
				ps.setInt(1, receita);
				
				ResultSet rs = ps.executeQuery();
				while (rs.next())
				{
					PassoBean passo = new PassoBean();
					
					passo.setOID(rs.getInt("id"));
					passo.setReceita(rs.getInt("id_receita"));
					passo.setSequencia(rs.getInt("sequencia"));
					passo.setDescricao(rs.getString("descricao"));
					
					passos.add(passo);
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
		
		return passos;
	}
}
