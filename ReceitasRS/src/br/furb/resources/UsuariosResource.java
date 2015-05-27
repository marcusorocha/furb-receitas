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
import br.furb.utils.LogUtil;

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
	public Response login(@FormParam("nome") String nome, @FormParam("senha") String senha)
	{
		try
		{
			LogUtil.logar("Parâmetro nome: " + nome);
			LogUtil.logar("Parâmetro senha: " + senha);
			
			if (nome != null)
			{
				UsuarioBean usuario = UsuarioDAO.localizar(nome);
				
				LogUtil.logar("Usuário: " + usuario.toString());
				
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
}
