package br.furb.receitasweb;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.furb.receitas.bean.UsuarioBean;
import br.furb.receitas.dao.UsuarioDAO;

/**
 * Servlet implementation class Login
 */
@WebServlet("/login")
public class Login extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() 
    {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String nomeusuario = request.getParameter("nome_usuario").toString();
		try
		{
			UsuarioBean usuario = UsuarioDAO.localizar(nomeusuario);
			
			if (usuario != null)
				response.sendRedirect("sucesso.jsp");
			else
				response.sendRedirect("erro.jsp");
		}
		catch(SQLException sqlEx)
		{
			response.sendRedirect("erro.jsp");
		}
	}

}
