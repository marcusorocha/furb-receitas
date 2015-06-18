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
	
	private static ReceitaBean popularReceita(ResultSet rs) throws SQLException
	{
		ReceitaBean receita = new ReceitaBean();
		
		receita.setOID(rs.getInt("id"));
		receita.setUsuario(rs.getInt("id_usuario"));
		receita.setDescricao(rs.getString("descricao"));
		
		return receita;
	}
	
	private static List<ReceitaBean> popularReceitas(ResultSet rs) throws SQLException
	{
		List<ReceitaBean> receitas = new ArrayList<ReceitaBean>();
		
		while (rs.next())
			receitas.add(popularReceita(rs));
		
		return receitas;
	}
	
	public static boolean salvar(ReceitaBean receita) throws SQLException
	{
		if (receita.getOID() == 0)
			return incluir(receita);
		else
			return atualizar(receita);
	}
	
 	public static boolean incluir(ReceitaBean receita) throws SQLException
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
 	
 	public static boolean atualizar(ReceitaBean receita) throws SQLException
	{
		String sql = "update RECEITA set descricao = ?, id_usuario = ? where id = ?";
		
		ConexaoSQL conSQL = new ConexaoSQL();
		try
		{
			conSQL.abrirConexao();		
			
			PreparedStatement ps = conSQL.getConexao().prepareStatement(sql);
			try
			{								
				ps.setString(1, receita.getDescricao());
				ps.setInt(2, receita.getUsuario());
				ps.setInt(3, receita.getOID());
				
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
 		String sql = "delete from RECEITA where id = ?";
 		
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
					return popularReceita(rs);
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
		
		ConexaoSQL conSQL = new ConexaoSQL();
		try
		{
			conSQL.abrirConexao();
			
			PreparedStatement ps = conSQL.getConexao().prepareStatement(sql);
			try
			{
				ps.setInt(1, usuario);
				
				return popularReceitas(ps.executeQuery());
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
	
	public static List<ReceitaBean> listarTodas() throws SQLException
	{
		String sql = "select * from RECEITA";		
		
		ConexaoSQL conSQL = new ConexaoSQL();
		try
		{
			conSQL.abrirConexao();
			
			PreparedStatement ps = conSQL.getConexao().prepareStatement(sql);
			try
			{
				return popularReceitas(ps.executeQuery());
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
	
	public static List<ReceitaBean> listarComEspeciarias(String[] especiarias) throws SQLException
	{	
		ConexaoSQL conSQL = new ConexaoSQL();
		try
		{
			String condicoes = conSQL.gerarCondicoes(especiarias.length, "C.NOME", "LIKE", "OR");
			
			String sql = " SELECT * FROM RECEITA A WHERE A.ID IN "
					   + " ( "
					   + "   SELECT B.ID_RECEITA FROM INGREDIENTE B "
					   + "   INNER JOIN ESPECIARIA C ON C.ID = B.ID_ESPECIARIA "
					   + "   WHERE " + condicoes
					   + " ) ";
						
			conSQL.abrirConexao();
			
			PreparedStatement ps = conSQL.getConexao().prepareStatement(sql);
			try
			{				
				for (int i = 0; i < especiarias.length; i++)
					ps.setString(i + 1, especiarias[i].trim());
				
				return popularReceitas(ps.executeQuery());
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
	
	public static List<ReceitaBean> listarComDescricao(String descricao) throws SQLException
	{	
		ConexaoSQL conSQL = new ConexaoSQL();
		try
		{			
			String sql = " SELECT * FROM RECEITA WHERE DESCRICAO LIKE ?";
						
			conSQL.abrirConexao();
			
			PreparedStatement ps = conSQL.getConexao().prepareStatement(sql);
			try
			{				
				ps.setString(1, "%"+ descricao +"%");
				
				return popularReceitas(ps.executeQuery());
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

}
