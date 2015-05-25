package br.furb.resources;

import java.sql.SQLException;

import javax.ws.rs.FormParam;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.furb.receitas.bean.UsuarioBean;
import br.furb.receitas.dao.UsuarioDAO;

@Path("usuarios")
public class UsuariosResource
{	
	@OPTIONS
	@Path("login")
	public Response login() 
	{
	    return Response.ok().build();
	}
	
	/**
	 * Autenticação do usuário
	 * 
	 * caminho: /rs/usuarios/login
	 * 
	 * @param nome <code>String</code> contendo o nome do usuário. 
	 * 
	 * @param senha <code>String</code> contendo a senha do usuário.
	 * 
	 * @return Um objeto <code>Response</code> com o código de retorno http e com o 
	 * o objeto JSON com os dados do usuário caso a autenticação seja bem sucedida.
	 * 		
	 */
	@POST
	@Path("login")
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(@FormParam("nome") String nome, @FormParam("senha") String senha)
	{
		try
		{
			if (nome != null)
			{
				UsuarioBean usuario = UsuarioDAO.localizar(nome);
				
				if (usuario != null)
					if (usuario.getSenha().equals(senha))
						return Response.ok(usuario).build();
			}
			return Response.noContent().build();
		}
		catch (SQLException sqlEx)
		{			
			return Response.serverError().build();
		}
	}
}
