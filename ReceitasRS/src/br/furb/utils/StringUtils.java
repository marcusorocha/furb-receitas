package br.furb.utils;


public class StringUtils
{
	public static boolean IsNullOrEmpty(String s)
	{
		if (s == null)
			return true;
		
		return s.isEmpty();			
	}

}
