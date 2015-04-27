package br.com.receitas.testes;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import br.com.receitas.bean.EspeciariaBean;
import br.com.receitas.bean.IngredienteBean;
import br.com.receitas.bean.ReceitaBean;
import br.com.receitas.bean.UsuarioBean;
import br.com.receitas.dao.EspeciariaDAO;
import br.com.receitas.dao.IngredienteDAO;
import br.com.receitas.dao.ReceitaDAO;
import br.com.receitas.dao.UsuarioDAO;

public class CadastrosTeste 
{	
	@Before
	public void executeBeforeTest()
	{
		try
		{	
			// A exclusão deve ser ordenada para não haver 
			// problemas com as chaves estrangeiras			
			IngredienteDAO.excluirTodos();
			ReceitaDAO.excluirTodos();
			UsuarioDAO.excluirTodos();
			EspeciariaDAO.excluirTodos();
			
			// Cadastrar um usuário para os testes de cadastros de receitas
			UsuarioBean marcus = new UsuarioBean();
			marcus.setNome("marcus");
			marcus.setSenha("semsenha");
			marcus.setEmail("marcusorocha@gmail.com");
			
			UsuarioDAO.incluir(marcus);
			
			EspeciariaBean ovos = new EspeciariaBean("ovos");
			EspeciariaBean trigo = new EspeciariaBean("trigo");			
			EspeciariaBean sal = new EspeciariaBean("sal");			
			EspeciariaBean oleo = new EspeciariaBean("oleo");
			EspeciariaBean cenoura = new EspeciariaBean("cenoura");
			EspeciariaBean acucar = new EspeciariaBean("açucar");
			EspeciariaBean fermento = new EspeciariaBean("fermento");
			
			EspeciariaDAO.incluir(ovos);
			EspeciariaDAO.incluir(trigo);
			EspeciariaDAO.incluir(sal);
			EspeciariaDAO.incluir(oleo);
			EspeciariaDAO.incluir(cenoura);
			EspeciariaDAO.incluir(acucar);
			EspeciariaDAO.incluir(fermento);
		}
		catch (SQLException sqlEx)
		{
			System.out.println("Erro ao preparar os testes: " + sqlEx.getMessage());
			
			fail();
		}
	}
	
	@Test 
	public void testaInclusaoReceitas()
	{		
		try
		{
			UsuarioBean usuario = UsuarioDAO.localizar("marcus");
			
			// Inicio - Receita de Bolo de Cenoura
			
			ReceitaBean boloCenoura = new ReceitaBean();
			boloCenoura.setDescricao("Bolo de Cenoura");
			boloCenoura.setUsuario(usuario.getOID());
		
			ReceitaDAO.incluir(boloCenoura);
			
			EspeciariaBean ovo = EspeciariaDAO.localizar("ovos");
			EspeciariaBean trigo = EspeciariaDAO.localizar("trigo");			
			EspeciariaBean oleo = EspeciariaDAO.localizar("oleo");
			EspeciariaBean cenoura = EspeciariaDAO.localizar("cenoura");
			EspeciariaBean acucar = EspeciariaDAO.localizar("açucar");
			EspeciariaBean fermento = EspeciariaDAO.localizar("açucar");
			
			IngredienteBean xicarasOleo = new IngredienteBean(boloCenoura, oleo);			
			xicarasOleo.setUnidade("xícara (chá)");
			xicarasOleo.setQuantidade(0.5);
			
			IngredienteBean cenouras = new IngredienteBean(boloCenoura, cenoura, 3, "média");			
			IngredienteBean ovos = new IngredienteBean(boloCenoura, ovo, 4);			
			IngredienteBean xicarasAcucar = new IngredienteBean(boloCenoura, acucar, 2, "xícara (chá)");			
			IngredienteBean xicarasTrigo = new IngredienteBean(boloCenoura, trigo, 2.5, "xícara (chá)");			
			IngredienteBean colherFermento = new IngredienteBean(boloCenoura, fermento, 1, "colher (sopa)");
			
			IngredienteDAO.incluir(cenouras);
			IngredienteDAO.incluir(ovos);
			IngredienteDAO.incluir(xicarasAcucar);
			IngredienteDAO.incluir(xicarasTrigo);
			IngredienteDAO.incluir(colherFermento);
			
			// Final - Receita de Bolo de Cenoura
			
			assertTrue(true);
		}
		catch(SQLException sqlEx)
		{
			System.out.println("Erro ao incluir receitas: " + sqlEx.getMessage());
			
			fail();
		}
	}
}
