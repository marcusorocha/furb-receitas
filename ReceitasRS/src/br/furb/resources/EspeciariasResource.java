package br.furb.resources;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.furb.receitas.bean.EspeciariaBean;
import br.furb.receitas.dao.EspeciariaDAO;

@Path("especiarias")
public class EspeciariasResource
{

	/**
	 * Listar todas as especiarias cadastradas
	 * <br><br>
	 * caminho: <b>/rs/especiarias/lista-todas</b>
	 * 
	 * @return Um objeto JSON com a lista de todas as especiarias cadastradas
	 */
	@GET
	@Path("lista-todas")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listarEspeciarias()
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
	
}