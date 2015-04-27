package br.furb.resources;

import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.furb.receitas.bean.UsuarioBean;
import br.furb.receitas.dao.UsuarioDAO;

@Path("usuarios")
public class UsuariosResource
{
	/**
	 * 
	 * @param user
	 * @param password
	 * @return Response 
	 */	
	@GET
	@Path("login")
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(@HeaderParam("nome") String nome, @HeaderParam("senha") String senha) 
	{
		try
		{
			UsuarioBean usuario = UsuarioDAO.localizar(nome);
			
			if (usuario != null)
				if (usuario.getSenha().equals(senha))
					return Response.ok(usuario).build();		
			
			return Response.noContent().build();
		}
		catch (SQLException sqlEx)
		{			
			return Response.serverError().build();
		}
	}	
}
