package br.furb.resources;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.GET;
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
	@GET
	@Path("lista-todas")
	@Produces(MediaType.TEXT_PLAIN)
	public String listarReceitas()
	{			
		return null;
	}
	
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
			return Response.serverError().build();
		}
	}
}
