package br.furb.receitas.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.furb.receitas.bean.EspeciariaBean;
import br.furb.receitas.db.ConexaoSQL;

public class EspeciariaDAO 
{
	private static final String TABELA_ID = "ESPECIARIA_ID";
	
	public static boolean salvar(EspeciariaBean especiaria) throws SQLException
	{
		if (especiaria.getOID() == 0)
			return incluir(especiaria);
		else
			return atualizar(especiaria);
	}
	
	public static boolean incluir(EspeciariaBean especiaria) throws SQLException
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
	
	public static boolean atualizar(EspeciariaBean especiaria) throws SQLException
	{
		String sql = "update ESPECIARIA set nome = ? where id = ?";
		
		ConexaoSQL conSQL = new ConexaoSQL();
		try
		{
			conSQL.abrirConexao();		
			
			PreparedStatement ps = conSQL.getConexao().prepareStatement(sql);
			try
			{				
				ps.setString(1, especiaria.getNome());				
				ps.setInt(2, especiaria.getOID());
				
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
		String sql = "delete from ESPECIARIA where id = ?";
		
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
	
	public static EspeciariaBean localizar(int oid) throws SQLException
	{
		String sql = "select * from ESPECIARIA where id = ?";
		
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
	
	public static List<EspeciariaBean> listarTodas() throws SQLException
	{
		List<EspeciariaBean> especiarias = new ArrayList<EspeciariaBean>();
		
		ConexaoSQL conSQL = new ConexaoSQL();
		try
		{
			String sql = "select * from ESPECIARIA";
			
			conSQL.abrirConexao();
			
			PreparedStatement ps = conSQL.getConexao().prepareStatement(sql);
			try
			{
				ResultSet rs = ps.executeQuery();
				while (rs.next())
				{
					EspeciariaBean especiaria = new EspeciariaBean();
					
					especiaria.setOID(rs.getInt("id"));
					especiaria.setNome(rs.getString("nome"));
					
					especiarias.add(especiaria);
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
		
		return especiarias;
	}

}