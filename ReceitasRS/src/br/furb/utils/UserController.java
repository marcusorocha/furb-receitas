package br.furb.utils;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import br.furb.receitas.bean.UsuarioBean;

public class UserController
{
	private static UserController instance;
	private List<UserInfo> logados;
	
	private UserController()
	{
		logados = new ArrayList<UserInfo>();
	}
	
	public static UserController getInstance()
	{
		if (instance == null)
			instance = new UserController();
		
		return instance;
	}
	
	public UserInfo addUsuario(UsuarioBean usuario)
	{
		String token = gerarToken(usuario);
		
		UserInfo userInfo = new UserInfo(usuario, token);
		
		logados.add(userInfo);
		
		return userInfo;
	}
	
	public UserInfo getUsuarioLogado(String token)
	{
		for (UserInfo u : logados)
			if (u.getToken().equals(token))
				return u;
		
		return null;
	}
	
	public UserInfo getUsuarioLogado(UsuarioBean usuario)
	{
		for (UserInfo u : logados)
			if (u.getUsuario().equals(usuario))
				return u;
		
		return null;
	}
	
	public static String gerarToken(UsuarioBean usuario)
	{
		SecureRandom random = new SecureRandom();
		
		return new BigInteger(130, random).toString(32);		
	}
}