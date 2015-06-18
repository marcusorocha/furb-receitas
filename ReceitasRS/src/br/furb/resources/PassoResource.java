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

import br.furb.receitas.bean.PassoBean;
import br.furb.receitas.bean.ReceitaBean;
import br.furb.receitas.bean.UsuarioBean;
import br.furb.receitas.dao.PassoDAO;
import br.furb.receitas.dao.ReceitaDAO;
import br.furb.receitas.dao.UsuarioDAO;
import br.furb.utils.StringUtils;

@Path("passo")
public class PassoResource
{
	
	@Context
	SecurityContext sc; 
	
	/**
	 * Retorna um passo pelo seu identificador
	 * <br><br>
	 * caminho: <b>/rs/passo/<i>[OID_PASSO]</i></b>
	 * método: <b>GET</b>
	 * 
	 * @return Um objeto JSON com as informações do passo
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
			
			PassoBean passo = PassoDAO.localizar(oid);
			
			return Response.ok(passo).build();
		}
		catch (SQLException sqlEx)
		{			
			return Response.serverError().entity("Erro de SQL").build();
		}
	}
	
	/**
	 * Salvar (incluir/atualizar) um passo
	 * <br><br>
	 * caminho: <b>/rs/passo</b>
	 * método: <b>POST</b>
	 * 
	 * @param passo Objeto em formato JSON representando um passo, exemplo: <code>{ oid = "72", receita = "50", sequencia = "4", descricao = "deixar fever por aproximadamente 10 min" }</code>
	 * 
	 * @return Um objeto JSON com as informações do passo caso gravação dos dados seja bem sucedida, 
	 * caso contrário, será retornado um objeto vazio.
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed("Autorizado")
	public Response salvar(PassoBean passo)
	{		
		try
		{
			UsuarioBean usuario = UsuarioDAO.localizar(sc.getUserPrincipal().getName());
			
			if (usuario != null)
			{
				ReceitaBean r = ReceitaDAO.localizar(passo.getReceita());
				
				if (r != null)
				{
					if (r.getUsuario() == usuario.getOID())
					{	
						if (PassoDAO.salvar(passo))
							return Response.ok(passo).build();
					}
				}
			}
			
			return Response.noContent().build();
		}
		catch (SQLException ex)
		{
			return Response.serverError().entity("Erro na inclusão do objeto").build();
		}
	}
	
	/**
	 * Salvar (incluir/atualizar) um passo
	 * <br><br>
	 * caminho: <b>/rs/passo</b>
	 * método: <b>POST</b>
	 * 
	 * @param receita <code>FormParam</code> contendo o id da receita
	 * 
	 * @param sequencia <code>FormParam</code> contendo a sequencia do passo
	 * 
	 * @param descricao <code>FormParam</code> contendo a descricao do passo
	 * 
	 * @param id <code>FormParam</code> contendo o id do passo, se esse parametro 
	 * for igual à "zero", "vazio" ou "null", a especiaria será incluída, caso contrário,
	 * a especiaria será atualizada.
	 * 
	 * @return Um objeto JSON com as informações do passo caso gravação dos dados seja bem sucedida, 
	 * caso contrário, será retornado um objeto vazio.
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@RolesAllowed("Autorizado")
	public Response salvarFORM(@FormParam("receita") String receita, 
							   @FormParam("sequencia") String sequencia,
							   @FormParam("descricao") String descricao,
							   @FormParam("id") String id)
	{
		if (!StringUtils.IsNullOrEmpty(receita) && 
			!StringUtils.IsNullOrEmpty(sequencia) &&
			!StringUtils.IsNullOrEmpty(descricao))
		{
			int oid_receita = Integer.parseInt(receita);
			int i_sequencia = Integer.parseInt(sequencia);			
			
			if (oid_receita > 0 && i_sequencia > 0)
			{			
				PassoBean passo = new PassoBean();
				
				passo.setReceita(oid_receita);
				passo.setSequencia(i_sequencia);
				passo.setDescricao(descricao);				
				
				if (!StringUtils.IsNullOrEmpty(id))
					passo.setOID(Integer.parseInt(id));
				
				return salvar(passo);
			}
		}			
		return Response.noContent().build();
	}
	
	/**
	 * Excluir um passo
	 * <br><br>
	 * caminho: <b>/rs/passo/<i>[OID_PASSO]</i></b>
	 * método: <b>DELETE</b>
	 * 
	 * @return Um objeto JSON com as informações do passo recém excluído caso a 
	 * exclusão seja bem sucedida, caso contrário, será retornado um objeto vazio.
	 */
	@DELETE
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("Autorizado")
	public Response remover(@PathParam("id") String id)
	{
		try
		{
			Integer oid = Integer.valueOf(id);
			
			PassoBean passo = PassoDAO.localizar(oid);
			
			if (passo != null)
			{
				UsuarioBean usuario = UsuarioDAO.localizar(sc.getUserPrincipal().getName());
				
				if (usuario != null)
				{
					ReceitaBean r = ReceitaDAO.localizar(passo.getReceita());
					
					if (r != null)
					{
						if (r.getUsuario() == usuario.getOID())
						{	
							if (PassoDAO.excluir(oid))
								return Response.ok(passo).build();
						}
					}
				}
			}
			
			return Response.noContent().build();
		}
		catch (SQLException sqlEx)
		{			
			return Response.serverError().entity("Erro de SQL").build();
		}
	}
	
	/**
	 * Lista de passos de uma receita
	 * <br><br>
	 * caminho: <b>/rs/passo/receita/<i>[oid_receita]</i></b>
	 * 
	 * @param receita <code>PathParam</code> contendo o código da receita
	 * 
	 * @return Um objeto <code>Response</code> com o código de retorno http e com o 
	 * o objeto JSON com uma lista de passos de uma receita caso a requisição
	 * seja bem sucessida.
	 * 
	 */
	@GET
	@Path("receita/{receita}")
	@Produces(MediaType.APPLICATION_JSON)
	@PermitAll
	public Response obterDaReceita(@PathParam("receita") String receita)
	{		
		try
		{
			Integer oidReceita = Integer.parseInt(receita);
			
			List<PassoBean> passos = PassoDAO.listarDaReceita(oidReceita);
			
			return Response.ok(passos).build();
		}
		catch (SQLException sqlEx)
		{			
			return Response.serverError().entity("Erro de SQL").build();
		}
		catch (NumberFormatException nfe)
		{
			return Response.serverError().entity("Falha na conversão do código da receita").build();
		}
	}
}
