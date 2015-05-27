package br.furb.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogUtil
{
	private LogUtil()
	{
		// Classe estática, sem construtor.
	}
	
	public static void logar(String texto)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		
		String momento = sdf.format(new Date());
		
		System.out.printf("%s ::: %s", momento, texto);
		System.out.println();
	}
}
