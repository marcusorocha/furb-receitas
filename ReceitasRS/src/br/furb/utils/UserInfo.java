package br.furb.utils;

import br.furb.receitas.bean.UsuarioBean;

public class UserInfo
{
	private UsuarioBean usuario;
	private String token;
	
	public UserInfo(UsuarioBean usuario, String token)
	{
		this.usuario = usuario;
		this.token = token;
	}
	
	public UsuarioBean getUsuario()
	{
		return usuario;
	}
	
	public String getToken()
	{
		return token;
	}
}
