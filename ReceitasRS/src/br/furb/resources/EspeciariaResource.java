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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.furb.receitas.bean.EspeciariaBean;
import br.furb.receitas.dao.EspeciariaDAO;

@Path("especiaria")
public class EspeciariaResource
{
	/**
	 * Listar todas as especiarias cadastradas
	 * <br><br>
	 * caminho: <b>/rs/especiaria</b>
	 * método: <b>GET</b>
	 * 
	 * @return Um objeto JSON com a lista de todas as especiarias cadastradas
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@PermitAll
	public Response obterTodas()
	{		
		try
		{
			List<EspeciariaBean> especiarias = EspeciariaDAO.listarTodas();
			
			return Response.ok(especiarias).build();
		}
		catch (SQLException sqlEx)
		{			
			return Response.serverError().entity("Erro de SQL").build();
		}
	}
	
	/**
	 * Retorna uma especiaria pelo seu identificador
	 * <br><br>
	 * caminho: <b>/rs/especiarias/<i>[OID_ESPECIARIA]</i></b>
	 * método: <b>GET</b>
	 * 
	 * @return Um objeto JSON com as informações da especiaria
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
			
			EspeciariaBean especiaria = EspeciariaDAO.localizar(oid);
			
			return Response.ok(especiaria).build();
		}
		catch (SQLException sqlEx)
		{			
			return Response.serverError().entity("Erro de SQL").build();
		}
	}
	
	/**
	 * Salvar (incluir/atualizar) uma especiaria
	 * <br><br>
	 * caminho: <b>/rs/especiaria</b>
	 * método: <b>POST</b>
	 * 
	 * @param especiaria Objeto em formato JSON representando uma especiaria, exemplo: <code>{ oid = "72", nome = "ovos" }</code>
	 * 
	 * @return Um objeto JSON com as informações da especiaria caso gravação dos dados seja bem sucedida, 
	 * caso contrário, será retornado um objeto vazio.
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed("Autorizado")
	public Response salvar(EspeciariaBean especiaria)
	{		
		try
		{
			if (EspeciariaDAO.salvar(especiaria))
				return Response.ok(especiaria).build();
			
			return Response.noContent().build();
		}
		catch (SQLException ex)
		{
			return Response.serverError().entity("Erro na inclusão do objeto").build();
		}
	}
	
	/**
	 * Salvar (incluir/atualizar) uma especiaria
	 * <br><br>
	 * caminho: <b>/rs/especiaria</b>
	 * método: <b>POST</b>
	 * 
	 * @param nome <code>FormParam</code> contendo o nome da especiaria
	 * 
	 * @param id <code>FormParam</code> contendo o id da especiaria, se esse parametro 
	 * for igual à "zero", "vazio" ou "null", a especiaria será incluída, caso contrário,
	 * a especiaria será atualizada.
	 * 
	 * @return Um objeto JSON com as informações da especiaria caso gravação dos dados seja bem sucedida, 
	 * caso contrário, será retornado um objeto vazio.
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@RolesAllowed("Autorizado")
	public Response salvarFORM(@FormParam("nome") String nome, @FormParam("id") String id)
	{
		if (nome != null && !nome.isEmpty())
		{
			EspeciariaBean e = new EspeciariaBean(nome);
			
			if (id != null && !id.isEmpty())
				e.setOID(Integer.parseInt(id));
			
			return salvar(e);
		}			
		return Response.noContent().build();
	}
	
	/**
	 * Excluir uma especiaria
	 * <br><br>
	 * caminho: <b>/rs/especiaria/<i>[OID_ESPECIARIA]</i></b>
	 * método: <b>DELETE</b>
	 * 
	 * @return Um objeto JSON com as informações da especiaria recém excluída caso a 
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
			
			EspeciariaBean especiaria = EspeciariaDAO.localizar(oid);
			
			if (especiaria != null)
				if (EspeciariaDAO.excluir(oid))
					return Response.ok(especiaria).build();
			
			return Response.noContent().build();
		}
		catch (SQLException sqlEx)
		{			
			return Response.serverError().entity("Erro de SQL").build();
		}
	}
}
