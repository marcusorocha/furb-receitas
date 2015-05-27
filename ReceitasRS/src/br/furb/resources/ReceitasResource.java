package br.furb.resources;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.furb.receitas.bean.ReceitaBean;
import br.furb.receitas.bean.UsuarioBean;
import br.furb.receitas.dao.ReceitaDAO;
import br.furb.receitas.dao.UsuarioDAO;

@Path("receitas")
public class ReceitasResource
{
	
	/**
	 * Listar todas as receitas cadastradas
	 * <br><br>
	 * caminho: <b>/rs/receitas/lista-todas</b>
	 * 
	 * @return Um objeto JSON com a lista de todas as receitas cadastradas
	 */
	@GET
	@Path("lista-todas")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listarReceitas()
	{			
		try
		{
			List<ReceitaBean> receitas = ReceitaDAO.listarTodas();
			
			return Response.ok(receitas).build();
		}
		catch (SQLException sqlEx)
		{			
			return Response.serverError().entity("Erro de SQL").build();
		}
	}
	
	/**
	 * Listas as receitas de um usuário específico
	 * <br><br>
	 * caminho: <b>/rs/receitas/usuario/<i>[usuario]</i></b>
	 * 
	 * @param nomeUsuario <code>PathParam</code> contendo o nome do usuário
	 * 
	 * @return Um objeto JSON com a lista de receitas do usuário caso o usuário
	 * exteja cadastrado e possua alguma receita cadastrada, caso contrário será
	 * retornado um objeto JSON vazio.
	 */
	@GET
	@Path("lista-usuario/{usuario}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listarReceitas(@PathParam("usuario") String nomeUsuario)
	{		
		try
		{
			UsuarioBean usuario = UsuarioDAO.localizar(nomeUsuario);
			
			if (usuario != null)
			{			
				List<ReceitaBean> receitas = ReceitaDAO.listarDoUsuario(usuario.getOID());
				
				return Response.ok(receitas).build();
			}
			return Response.noContent().build();
		}
		catch (SQLException sqlEx)
		{			
			return Response.serverError().entity("Erro de SQL").build();
		}
	}
	
	/**
	 * Lista de receitas contendo alguma das especiarias
	 * <br><br>
	 * caminho: <b>/rs/receitas/lista-contendo</b>
	 * 
	 * @param especiarias <code>FormParam</code> contendo a lista de especiarias separadas por vírgula.
	 * 
	 * @return Um objeto JSON com a lista de receitas contendo alguma das especiarias caso exista,
	 * caso contrário, será retornado um objeto JSON de lista vazia.
	 */
	@POST
	@Path("lista-contendo")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listarReceitasContendo(@FormParam("especiarias") String especiarias)
	{		
		try
		{
			if (especiarias != null && !especiarias.isEmpty())
			{
				List<ReceitaBean> receitas = ReceitaDAO.listarComEspeciarias(especiarias.split(","));
			
				return Response.ok(receitas).build();
			}
			return Response.noContent().build();
		}
		catch (SQLException sqlEx)
		{
			return Response.serverError().entity("Ocorreu um erro durante a consulta. Tente novamente mais tarde").build();
		}
	}
	
	/**
	 * Incluir uma receita para um usuário
	 * <br><br>
	 * caminho: <b>/rs/receitas/incluir</b>
	 * 
	 * @param nomeUsuario <code>FormParam</code> contendo o nome do usuário
	 * 
	 * @param descricao <code>FormParam</code> contento a descrição da receita
	 * 
	 * @return Um objeto JSON com as informações da receita caso o usuário exista 
	 * e a gravação dos dados seja bem sucedida, caso contrário, será retornado um
	 * objeto vazio.  
	 */
	@POST
	@Path("incluir")
	@Produces(MediaType.APPLICATION_JSON)
	public Response incluirReceita(@FormParam("usuario") String nomeUsuario, @FormParam("descricao") String descricao)
	{		
		try
		{
			UsuarioBean usuario = UsuarioDAO.localizar(nomeUsuario);
			
			if (usuario != null)
			{			
				ReceitaBean receita = new ReceitaBean();
				receita.setDescricao(descricao);
				receita.setUsuario(usuario.getOID());
				
				ReceitaDAO.incluir(receita);
			
				return Response.ok(receita).build();
			}
			return Response.noContent().build();
		}
		catch (SQLException sqlEx)
		{
			return Response.serverError().entity("Ocorreu um erro durante a consulta. Tente novamente mais tarde").build();
		}
	}
	
	/**
	 * Remover uma receita de um usuário
	 * <br><br>
	 * caminho: <b>/rs/receitas/remover</b>
	 * 
	 * @param nomeUsuario <code>FormParam</code> contendo o nome do usuário
	 * 
	 * @param receita <code>FormParam</code> contento o identificado da receita
	 * 
	 * @return Um objeto JSON com as informações da receita caso o usuário exista 
	 * e a exclusão dos dados seja bem sucedida, caso contrário, será retornado um
	 * objeto vazio.  
	 */
	@POST
	@Path("remover")
	@Produces(MediaType.APPLICATION_JSON)
	public Response removerReceita(@FormParam("usuario") String nomeUsuario, @FormParam("receita") String receita)
	{		
		try
		{
			UsuarioBean usuario = UsuarioDAO.localizar(nomeUsuario);
			
			if (usuario != null)
			{
				Integer oidReceita = Integer.parseInt(receita);
				
				ReceitaBean r = ReceitaDAO.localizar(oidReceita);
				
				boolean removido = ReceitaDAO.excluir(oidReceita);
				
				if (removido)
					return Response.ok(r).build();
			}
			return Response.noContent().build();
		}
		catch (SQLException sqlEx)
		{
			return Response.serverError().entity("Ocorreu um erro durante a consulta. Tente novamente mais tarde").build();
		}
	}	
	
}
