package br.furb.resources;

import java.sql.SQLException;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import br.furb.receitas.bean.UsuarioBean;
import br.furb.receitas.dao.UsuarioDAO;
import br.furb.utils.UserController;
import br.furb.utils.UserInfo;

@Path("usuarios")
public class UsuariosResource
{		
	@Context
	SecurityContext sc; 
	
	@OPTIONS
	@Path("login")
	public Response login() 
	{
	    return Response.ok().build();
	}
	
	/**
	 * Autenticação do usuário
	 * <br><br>
	 * caminho: <b>/rs/usuarios/login</b>
	 * 
	 * @param nome <code>FormParam</code> contendo o nome do usuário. 
	 * 
	 * @param senha <code>FormParam</code> contendo a senha do usuário.
	 * 
	 * @return Um objeto <code>Response</code> com o código de retorno http e com o 
	 * o objeto JSON com os dados do usuário caso a autenticação seja bem sucedida.
	 * 		
	 */
	@POST
	@Path("login")
	@Produces(MediaType.APPLICATION_JSON)
	@PermitAll
	public Response login(@FormParam("nome") String nome, @FormParam("senha") String senha)
	{		
		try
		{			
			if (nome != null)
			{
				UsuarioBean usuario = UsuarioDAO.localizar(nome);
				
				if (usuario != null)
					if (usuario.getSenha().equals(senha))
					{
						UserController uc = UserController.getInstance();
						
						UserInfo ui = uc.getUsuarioLogado(usuario);
						if (ui == null)
							ui = uc.addUsuario(usuario);
						
						JSONObject jo = new JSONObject();
						jo.put("token", ui.getToken());
						
						return Response.ok(jo).build();
					}
			}
			return Response.noContent().build();
		}
		catch (SQLException | JSONException ex)
		{			
			return Response.serverError().build();
		}
	}
	
	/**
	 * Cadastro de usuário
	 * <br><br>
	 * caminho: <b>/rs/usuarios/incluir</b>
	 * 
	 * @param nome <code>FormParam</code> contendo o nome do usuário.
	 * 
	 * @param senha <code>FormParam</code> contendo a senha do usuário.
	 * 
	 * @param email <code>FormParam</code> contento o e-mail do usuário.
	 * 
	 * @return Um objeto <code>Response</code> com o código de retorno http e com o 
	 * o objeto JSON com os dados do usuário caso o cadastro seja bem sucedido. Caso
	 * o usuário ja estiver cadastrado, o retorno será um conteúdo vazio (noContent).
	 * 		
	 */
	@POST
	@Path("incluir")
	@Produces(MediaType.APPLICATION_JSON)
	@DenyAll
	public Response incluir(@FormParam("nome") String nome, @FormParam("senha") String senha, @FormParam("email") String email)
	{
		try
		{
			if (nome != null && !nome.isEmpty())
			{
				UsuarioBean usuario = UsuarioDAO.localizar(nome);
				
				if (usuario == null)
				{
					usuario = new UsuarioBean();
					usuario.setNome(nome);
					usuario.setEmail(email);
					usuario.setSenha(senha);
					
					return Response.ok(usuario).build();
				}
			}
			return Response.noContent().build();
		}
		catch (SQLException sqlEx)
		{			
			return Response.serverError().build();
		}
	}
	
	@GET
	@Path("session")
	@Produces(MediaType.TEXT_PLAIN)
	@PermitAll
	public String getSessionId( @Context HttpServletRequest request )
	{
		return request.getSession(true).getId();
	}
	
	@GET
	@Path("logado")
	@Produces(MediaType.TEXT_PLAIN)
	@RolesAllowed("Autorizado")
	public Response getUsuarioLogado()
	{
		return Response.ok(sc.getUserPrincipal().getName()).build();
	}
}
