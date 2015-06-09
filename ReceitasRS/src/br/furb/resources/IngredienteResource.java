package br.furb.resources;

import java.sql.SQLException;
import java.util.List;

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

import br.furb.receitas.bean.IngredienteBean;
import br.furb.receitas.dao.IngredienteDAO;
import br.furb.utils.StringUtils;

@Path("ingrediente")
public class IngredienteResource
{	
	/**
	 * Retorna um ingrediente pelo seu identificador
	 * <br><br>
	 * caminho: <b>/rs/ingrediente/<i>[OID_INGREDIENTE]</i></b>
	 * método: <b>GET</b>
	 * 
	 * @return Um objeto JSON com as informações do ingrediente
	 */
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response obter(@PathParam("id") String id)
	{
		try
		{
			Integer oid = Integer.valueOf(id);
			
			IngredienteBean ingrediente = IngredienteDAO.localizar(oid);
			
			return Response.ok(ingrediente).build();
		}
		catch (SQLException sqlEx)
		{			
			return Response.serverError().entity("Erro de SQL").build();
		}
	}
	
	/**
	 * Salvar (incluir/atualizar) um ingrediente
	 * <br><br>
	 * caminho: <b>/rs/ingrediente</b>
	 * método: <b>POST</b>
	 * 
	 * @param ingrediente Objeto em formato JSON representando um ingrediente, exemplo: <code>{ oid = "72", receita = "50", especiaria = "4", unidade = "kg", quantidade = "2" }</code>
	 * 
	 * @return Um objeto JSON com as informações do ingrediente caso gravação dos dados seja bem sucedida, 
	 * caso contrário, será retornado um objeto vazio.
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response salvar(IngredienteBean ingrediente)
	{		
		try
		{
			if (IngredienteDAO.salvar(ingrediente))
				return Response.ok(ingrediente).build();
			
			return Response.noContent().build();
		}
		catch (SQLException ex)
		{
			return Response.serverError().entity("Erro na inclusão do objeto").build();
		}
	}
	
	/**
	 * Salvar (incluir/atualizar) um ingrediente
	 * <br><br>
	 * caminho: <b>/rs/ingrediente</b>
	 * método: <b>POST</b>
	 * 
	 * @param receita <code>FormParam</code> contendo o id da receita
	 * 
	 * @param especiaria <code>FormParam</code> contendo o id da especiaria
	 * 
	 * @param unidade <code>FormParam</code> contendo a descricao da unidade de especiarias
	 * 
	 * @param quantidade <code>FormParam</code> contendo a quantidade de especiarias utilizada
	 * 
	 * @param id <code>FormParam</code> contendo o id da especiaria, se esse parametro 
	 * for igual à "zero", "vazio" ou "null", a especiaria será incluída, caso contrário,
	 * a especiaria será atualizada.
	 * 
	 * @return Um objeto JSON com as informações do ingrediente caso gravação dos dados seja bem sucedida, 
	 * caso contrário, será retornado um objeto vazio.
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response salvarFORM(@FormParam("receita") String receita, 
							   @FormParam("especiaria") String especiaria, 							
							   @FormParam("quantidade") String quantidade, 
							   @FormParam("unidade") String unidade,
							   @FormParam("id") String id)
	{
		if (!StringUtils.IsNullOrEmpty(receita) && !StringUtils.IsNullOrEmpty(especiaria))
		{
			int oid_receita = Integer.parseInt(receita);
			int oid_especiaria = Integer.parseInt(especiaria);
			double qtde = Double.parseDouble(quantidade);
			
			if (oid_receita > 0 && oid_especiaria > 0 && qtde > 0.0)
			{			
				IngredienteBean ingrediente = new IngredienteBean();
				
				ingrediente.setReceita(oid_receita);
				ingrediente.setEspeciaria(oid_especiaria);				
				ingrediente.setQuantidade(qtde);			
				
				if (!StringUtils.IsNullOrEmpty(unidade))
					ingrediente.setUnidade(unidade);
				
				if (!StringUtils.IsNullOrEmpty(id))
					ingrediente.setOID(Integer.parseInt(id));
				
				return salvar(ingrediente);
			}
		}			
		return Response.noContent().build();
	}
	
	/**
	 * Excluir um ingrediente
	 * <br><br>
	 * caminho: <b>/rs/ingrediente/<i>[OID_INGREDIENTE]</i></b>
	 * método: <b>DELETE</b>
	 * 
	 * @return Um objeto JSON com as informações do ingrediente recém excluído caso a 
	 * exclusão seja bem sucedida, caso contrário, será retornado um objeto vazio.
	 */
	@DELETE
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response remover(@PathParam("id") String id)
	{
		try
		{
			Integer oid = Integer.valueOf(id);
			
			IngredienteBean ingrediente = IngredienteDAO.localizar(oid);
			
			if (ingrediente != null)
				if (IngredienteDAO.excluir(oid))
					return Response.ok(ingrediente).build();
			
			return Response.noContent().build();
		}
		catch (SQLException sqlEx)
		{			
			return Response.serverError().entity("Erro de SQL").build();
		}
	}
	
	/**
	 * Lista de ingredientes de uma receita
	 * <br><br>
	 * caminho: <b>/rs/ingrediente/receita/<i>[oid_receita]</i></b>
	 * 
	 * @param receita <code>PathParam</code> contendo o código da receita
	 * 
	 * @return Um objeto <code>Response</code> com o código de retorno http e com o 
	 * o objeto JSON com uma lista de ingredientes de uma receita caso a requisição
	 * seja bem sucessida.
	 * 
	 */
	@GET
	@Path("receita/{receita}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response obterDaReceita(@PathParam("receita") String receita)
	{		
		try
		{
			Integer oidReceita = Integer.parseInt(receita);
			
			List<IngredienteBean> ingredientes = IngredienteDAO.listarDaReceita(oidReceita);
			
			return Response.ok(ingredientes).build();
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
