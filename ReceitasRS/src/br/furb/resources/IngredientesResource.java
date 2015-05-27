package br.furb.resources;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.furb.receitas.bean.IngredienteBean;
import br.furb.receitas.dao.IngredienteDAO;

@Path("ingredientes")
public class IngredientesResource
{
	/**
	 * Lista de ingredientes de uma receita
	 * <br><br>
	 * caminho: <b>/rs/ingredientes/lista-receita/<i>[oid_receita]</i></b>
	 * 
	 * @param receita <code>PathParam</code> contendo o código da receita
	 * 
	 * @return Um objeto <code>Response</code> com o código de retorno http e com o 
	 * o objeto JSON com uma lista de ingredientes de uma receita caso a requisição
	 * seja bem sucessida.
	 * 
	 */
	@GET
	@Path("lista-receita/{receita}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listarEspeciarias(@PathParam("receita") String receita)
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
