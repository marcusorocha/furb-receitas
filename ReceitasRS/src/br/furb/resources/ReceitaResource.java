package br.furb.resources;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import br.furb.receitas.bean.ReceitaBean;
import br.furb.receitas.bean.UsuarioBean;
import br.furb.receitas.dao.ReceitaDAO;
import br.furb.receitas.dao.UsuarioDAO;
import br.furb.utils.StringUtils;

@Path("receita")
public class ReceitaResource
{	
	@Context
	SecurityContext sc;
	
	/**
	 * Listar todas as receitas cadastradas
	 * <br><br>
	 * caminho: <b>/rs/receita</b>
	 * método: <b>GET</b>
	 * 
	 * @return Um objeto JSON com a lista de todas as receitas cadastradas
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@PermitAll
	public Response obterTodas()
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
	 * Retorna uma receita pelo seu identificador
	 * <br><br>
	 * caminho: <b>/rs/receita/<i>[OID_RECEITA]</i></b>
	 * método: <b>GET</b>
	 * 
	 * @return Um objeto JSON com as informações do cabeçalho da receita
	 */
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@PermitAll
	public Response obter(@PathParam("id") String id)
	{
		try
		{
			Integer oid = Integer.valueOf(id);
			
			ReceitaBean receita = ReceitaDAO.localizar(oid);
			
			return Response.ok(receita).build();
		}
		catch (SQLException sqlEx)
		{			
			return Response.serverError().entity("Erro de SQL").build();
		}
	}
	
	/**
	 * Incluir uma receita para um usuário
	 * <br><br>
	 * caminho: <b>/rs/receitas/incluir</b>
	 * 
	 * @param usuario <code>FormParam</code> contendo o nome do usuário
	 * 
	 * @param descricao <code>FormParam</code> contento a descrição da receita
	 * 
	 * @param id <code>FormParam</code> contendo o id da receita, se esse parametro 
	 * for igual à "zero", "vazio" ou "null", a especiaria será incluída, caso contrário,
	 * a especiaria será atualizada.
	 * 
	 * @return Um objeto JSON com as informações da receita caso o usuário exista 
	 * e a gravação dos dados seja bem sucedida, caso contrário, será retornado um
	 * objeto vazio.  
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@RolesAllowed("Autorizado")
	public Response salvar(@FormParam("descricao") String descricao, @FormParam("id") String id)
	{		
		try
		{
			UsuarioBean usuario = UsuarioDAO.localizar(sc.getUserPrincipal().getName());
			
			if (usuario != null)
			{			
				if (!StringUtils.IsNullOrEmpty(descricao))
				{				
					ReceitaBean receita = new ReceitaBean();
					receita.setDescricao(descricao);
					receita.setUsuario(usuario.getOID());
					
					if (!StringUtils.IsNullOrEmpty(id))
						receita.setOID(Integer.parseInt(id));
					
					if (ReceitaDAO.salvar(receita))
						return Response.ok(receita).build();
				}
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
	 * caminho: <b>/rs/receita/remover</b>
	 * 
	 * @param usuario <code>FormParam</code> contendo o nome do usuário
	 * 
	 * @param receita <code>FormParam</code> contento o identificado da receita
	 * 
	 * @return Um objeto JSON com as informações da receita caso o usuário exista 
	 * e a exclusão dos dados seja bem sucedida, caso contrário, será retornado um
	 * objeto vazio.  
	 */
	@DELETE
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("Autorizado")
	public Response removerReceita(@PathParam("id") String id)
	{		
		try
		{
			UsuarioBean usuario = UsuarioDAO.localizar(sc.getUserPrincipal().getName());
			
			if (usuario != null)
			{
				if (!StringUtils.IsNullOrEmpty(id))
				{					
					Integer oidReceita = Integer.parseInt(id);
					
					ReceitaBean r = ReceitaDAO.localizar(oidReceita);
					
					if (r != null)
						if (r.getUsuario() == usuario.getOID())
							if (ReceitaDAO.excluir(oidReceita))
								return Response.ok(r).build();
				}
			}
			return Response.noContent().build();
		}
		catch (SQLException sqlEx)
		{
			return Response.serverError().entity("Ocorreu um erro durante a consulta. Tente novamente mais tarde").build();
		}
	}
	
	/**
	 * Listas as receitas de um usuário específico
	 * <br><br>
	 * caminho: <b>/rs/receita/usuario/<i>[usuario]</i></b>
	 * 
	 * @param nomeUsuario <code>PathParam</code> contendo o nome do usuário
	 * 
	 * @return Um objeto JSON com a lista de receitas do usuário caso o usuário
	 * exteja cadastrado e possua alguma receita cadastrada, caso contrário será
	 * retornado um objeto JSON vazio.
	 */
	@GET
	@Path("usuario/{usuario}")
	@Produces(MediaType.APPLICATION_JSON)
	@PermitAll
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
	 * caminho: <b>/rs/receita/contendo</b>
	 * 
	 * @param especiarias <code>FormParam</code> contendo a lista de especiarias separadas por vírgula.
	 * 
	 * @return Um objeto JSON com a lista de receitas contendo alguma das especiarias caso exista,
	 * caso contrário, será retornado um objeto JSON de lista vazia.
	 */
	@POST
	@Path("contendo")
	@Produces(MediaType.APPLICATION_JSON)
	@PermitAll
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
	 * Lista de receitas com a descrição passada por parâmetro
	 * <br><br>
	 * caminho: <b>/rs/receita/descricao</b>
	 * 
	 * @param especiarias <code>FormParam</code> contendo a lista de especiarias separadas por vírgula.
	 * 
	 * @return Um objeto JSON com a lista de receitas contendo alguma das especiarias caso exista,
	 * caso contrário, será retornado um objeto JSON de lista vazia.
	 */
	@POST
	@Path("descricao")
	@Produces(MediaType.APPLICATION_JSON)
	@PermitAll
	public Response listarReceitasPorDescricao(@FormParam("descricao") String descricao)
	{		
		try
		{
			if (descricao != null && !descricao.isEmpty())
			{
				List<ReceitaBean> receitas = ReceitaDAO.listarComDescricao(descricao);
			
				return Response.ok(receitas).build();
			}
			return Response.noContent().build();
		}
		catch (SQLException sqlEx)
		{
			return Response.serverError().entity("Ocorreu um erro durante a consulta. Tente novamente mais tarde").build();
		}
	}
}