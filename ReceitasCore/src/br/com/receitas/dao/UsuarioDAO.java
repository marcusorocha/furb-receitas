package br.com.receitas.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import br.com.receitas.bean.UsuarioBean;
import br.com.receitas.db.ConexaoSQL;

public class UsuarioDAO
{
	private static final String TABELA_ID = "USUARIO_ID";
	
	public static void incluir(UsuarioBean usuario) throws SQLException
	{
		String sql = "insert into USUARIO (id, nome, senha, email) values (?, ?, ?, ?)";
		
		ConexaoSQL conSQL = new ConexaoSQL();
		try
		{
			conSQL.abrirConexao();
			
			usuario.setOID(conSQL.proximoID(TABELA_ID));
			
			PreparedStatement ps = conSQL.getConexao().prepareStatement(sql);
			try
			{
				ps.setInt(1, usuario.getOID());
				ps.setString(2, usuario.getNome());
				ps.setString(3, usuario.getSenha());
				ps.setString(4, usuario.getEmail());
				
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
		String sql = "delete from USUARIO";
		
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
	
	public static UsuarioBean localizar(String nome) throws SQLException
	{
		String sql = "select * from USUARIO where upper(nome) = upper(?)";
		
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
					UsuarioBean usuario = new UsuarioBean();
					
					usuario.setOID(rs.getInt("id"));
					usuario.setNome(rs.getString("nome"));
					usuario.setEmail(rs.getString("email"));
					usuario.setSenha(rs.getString("senha"));			
					
					if (rs.getDate("ultimoacesso") != null)
						usuario.setUltimoAcesso(rs.getDate("ultimoacesso"));
					
					return usuario;
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
