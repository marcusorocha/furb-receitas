package br.furb.resources;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.Consumes;
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
import br.furb.utils.StringUtils;

@Path("receita")
public class ReceitaResource
{
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
	public Response salvar(@FormParam("usuario") String usuario, 
						   @FormParam("descricao") String descricao,
						   @FormParam("id") String id)
	{		
		try
		{
			if (!StringUtils.IsNullOrEmpty(usuario) && !StringUtils.IsNullOrEmpty(descricao))
			{
				UsuarioBean u = UsuarioDAO.localizar(usuario);
				
				if (u != null)
				{			
					ReceitaBean receita = new ReceitaBean();
					receita.setDescricao(descricao);
					receita.setUsuario(u.getOID());
					
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
	@POST
	@Path("remover")
	@Produces(MediaType.APPLICATION_JSON)
	public Response removerReceita(@FormParam("usuario") String usuario, @FormParam("receita") String receita)
	{		
		try
		{
			if (!StringUtils.IsNullOrEmpty(usuario) && !StringUtils.IsNullOrEmpty(receita))
			{
				UsuarioBean u = UsuarioDAO.localizar(usuario);
				
				if (u != null)
				{
					Integer oidReceita = Integer.parseInt(receita);
					
					ReceitaBean r = ReceitaDAO.localizar(oidReceita);
					
					if (r != null)
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
	 * caminho: <b>/rs/receitas/contendo/{lista_especiarias}</b>
	 * 
	 * @param especiarias <code>FormParam</code> contendo a lista de especiarias separadas por vírgula.
	 * 
	 * @return Um objeto JSON com a lista de receitas contendo alguma das especiarias caso exista,
	 * caso contrário, será retornado um objeto JSON de lista vazia.
	 */
	@POST
	@Path("contendo/{especiarias}")
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
}
