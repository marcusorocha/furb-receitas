package br.furb.receitas.testes;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.BeforeClass;
import org.junit.Test;

import br.furb.receitas.bean.EspeciariaBean;
import br.furb.receitas.bean.IngredienteBean;
import br.furb.receitas.bean.ReceitaBean;
import br.furb.receitas.bean.UsuarioBean;
import br.furb.receitas.dao.EspeciariaDAO;
import br.furb.receitas.dao.IngredienteDAO;
import br.furb.receitas.dao.ReceitaDAO;
import br.furb.receitas.dao.UsuarioDAO;
import br.furb.receitas.db.ConexaoSQL;

public class CadastrosTeste 
{		
	@BeforeClass
	public static void executeBeforeTest()
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
			
			UsuarioBean gielez = new UsuarioBean();
			gielez.setNome("gielez");
			gielez.setSenha("semsenha");
			gielez.setEmail("gielezfg@gmail.com");
			
			UsuarioBean paulo = new UsuarioBean();
			paulo.setNome("paulo");
			paulo.setSenha("semsenha");
			paulo.setEmail("pauloweber@hotmail.com.br");
			
			UsuarioDAO.incluir(marcus);
			UsuarioDAO.incluir(gielez);
			UsuarioDAO.incluir(paulo);
			
			// Cadastrar as especiarias utilizadas nos testes de receitas
			EspeciariaBean ovos = new EspeciariaBean("ovos");
			EspeciariaBean trigo = new EspeciariaBean("trigo");			
			EspeciariaBean sal = new EspeciariaBean("sal");			
			EspeciariaBean oleo = new EspeciariaBean("oleo");
			EspeciariaBean cenoura = new EspeciariaBean("cenoura");
			EspeciariaBean acucar = new EspeciariaBean("açucar");
			EspeciariaBean fermento = new EspeciariaBean("fermento");
			EspeciariaBean leiteCondensado = new EspeciariaBean("leite condensado");
			EspeciariaBean margarinaSemSal = new EspeciariaBean("margarina sem sal");
			EspeciariaBean chocolateEmPo = new EspeciariaBean("chocolate em pó");
			EspeciariaBean bacon = new EspeciariaBean("bacon");
			
			EspeciariaDAO.incluir(ovos);
			EspeciariaDAO.incluir(trigo);
			EspeciariaDAO.incluir(sal);
			EspeciariaDAO.incluir(oleo);
			EspeciariaDAO.incluir(cenoura);
			EspeciariaDAO.incluir(acucar);
			EspeciariaDAO.incluir(fermento);
			EspeciariaDAO.incluir(leiteCondensado);
			EspeciariaDAO.incluir(margarinaSemSal);
			EspeciariaDAO.incluir(chocolateEmPo);
			EspeciariaDAO.incluir(bacon);
		}
		catch (SQLException sqlEx)
		{
			System.out.println("Erro ao preparar os testes: " + sqlEx.getMessage());
			
			fail();
		}
	}
	
	@Test
	public void testaUsuarioDuplicado()
	{
		try
		{
			UsuarioBean marcus = new UsuarioBean();
			marcus.setNome("marcus");
			marcus.setSenha("semsenha");
			marcus.setEmail("marcusorocha@gmail.com");
			
			UsuarioDAO.incluir(marcus);
			
			fail();
		}
		catch(SQLException sqlEx)
		{
			// Deve ocorrer um erro de cadastro duplicado (chave única violada)
			assertEquals(ConexaoSQL.CHAVE_UNICA_VIOLADA, sqlEx.getErrorCode());
		}
	}
	
	@Test
	public void testaUsuarioEncontrado()
	{
		try
		{
			UsuarioBean usuario = UsuarioDAO.localizar("marcus");
			
			assertTrue(usuario != null);
		}
		catch(SQLException sqlEx)
		{
			System.out.println("Erro ao incluir receitas: " + sqlEx.getMessage());
			
			fail();
		}
	}
	
	@Test
	public void testaUsuarioNaoEncontrado()
	{
		try
		{
			UsuarioBean usuario = UsuarioDAO.localizar("jose");
			
			assertTrue(usuario == null);
		}
		catch(SQLException sqlEx)
		{
			System.out.println("Erro ao incluir receitas: " + sqlEx.getMessage());
			
			fail();
		}
	}
	
	@Test 
	public void testaInclusaoReceitaBoloCenoura()
	{		
		try
		{
			UsuarioBean usuario = UsuarioDAO.localizar("marcus");
			
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
			
			IngredienteBean xicarasOleo = new IngredienteBean(boloCenoura, oleo, 0.5, "xícara (chá)");			
			IngredienteBean cenouras = new IngredienteBean(boloCenoura, cenoura, 3, "média");			
			IngredienteBean ovos = new IngredienteBean(boloCenoura, ovo, 4);			
			IngredienteBean xicarasAcucar = new IngredienteBean(boloCenoura, acucar, 2, "xícara (chá)");			
			IngredienteBean xicarasTrigo = new IngredienteBean(boloCenoura, trigo, 2.5, "xícara (chá)");			
			IngredienteBean colherFermento = new IngredienteBean(boloCenoura, fermento, 1, "colher (sopa)");
			
			IngredienteDAO.incluir(xicarasOleo);
			IngredienteDAO.incluir(cenouras);
			IngredienteDAO.incluir(ovos);
			IngredienteDAO.incluir(xicarasAcucar);
			IngredienteDAO.incluir(xicarasTrigo);
			IngredienteDAO.incluir(colherFermento);
			
			assertTrue(true);
		}
		catch(SQLException sqlEx)
		{
			System.out.println("Erro ao incluir receitas: " + sqlEx.getMessage());
			
			fail();
		}
	}
	
	@Test 
	public void testaInclusaoReceitaBrigadeiro()
	{		
		try
		{
			UsuarioBean usuario = UsuarioDAO.localizar("gielez");
			
			ReceitaBean brigadeiro = new ReceitaBean();
			brigadeiro.setDescricao("Brigadeiro");
			brigadeiro.setUsuario(usuario.getOID());
			
			ReceitaDAO.incluir(brigadeiro);
			
			EspeciariaBean leiteCondensado = EspeciariaDAO.localizar("leite condensado");
			EspeciariaBean margarinaSemSal = EspeciariaDAO.localizar("margarina sem sal");
			EspeciariaBean chocolateEmPo = EspeciariaDAO.localizar("chocolate em pó");			
			
			IngredienteBean lataLeiteConsensado = new IngredienteBean(brigadeiro, leiteCondensado, 1, "lata");			
			IngredienteBean colherMargarina = new IngredienteBean(brigadeiro, margarinaSemSal, 1, "colher de sopa");
			IngredienteBean colheresChocolate = new IngredienteBean(brigadeiro, chocolateEmPo, 7, "colher rasa de sopa");
			
			IngredienteDAO.incluir(lataLeiteConsensado);
			IngredienteDAO.incluir(colherMargarina);
			IngredienteDAO.incluir(colheresChocolate);
			
			assertTrue(true);
		}
		catch(SQLException sqlEx)
		{
			System.out.println("Erro ao incluir receitas: " + sqlEx.getMessage());
			
			fail();
		}
	}
	
	@Test 
	public void testaInclusaoReceitaTirinhasBacon()
	{		
		try
		{
			UsuarioBean usuario = UsuarioDAO.localizar("paulo");
			
			ReceitaBean tirinhasBacon = new ReceitaBean();
			tirinhasBacon.setDescricao("Tirinhas de Bacon");
			tirinhasBacon.setUsuario(usuario.getOID());
			
			ReceitaDAO.incluir(tirinhasBacon);
			
			EspeciariaBean bacon = EspeciariaDAO.localizar("bacon");
			
			IngredienteBean bacons = new IngredienteBean(tirinhasBacon, bacon, 7, "tirinha");
			
			IngredienteDAO.incluir(bacons);
			
			assertTrue(true);
		}
		catch(SQLException sqlEx)
		{
			System.out.println("Erro ao incluir receitas: " + sqlEx.getMessage());
			
			fail();
		}
	}
}
