package ApplicationBuilder;

import java.util.ArrayList;
import java.util.List;

public class LoggingMessages 
{
	public static void printOut(String ... out)
	{
		System.out.println(combine(out));
	}
	
	public static void printOut(String label, List<String> out)
	{
		System.out.println(label + combine(out));
	}
	
	public static void printOut(List<String> out)
	{
		System.out.println(combine(out));
	}
	
	public static void printNewLine()
	{
		System.out.println();
	}
	
	
	/*******Utils**************/
	public static String combine(Object ...out)
	{
		StringBuffer sb = new StringBuffer();
		for(Object s : out)
		{
			sb.append(s.toString() + ", ");
		}
		return (String) sb.subSequence(0, sb.length()-2);
	}
	public static String combine(List<?> out)
	{
		return combine( out.toArray(new Object[] {}));
	}
	public static String combine(ArrayList<List<?>> out)
	{
		StringBuilder sb = new StringBuilder();
		for(List<?> ss: out)
		{
			sb.append(combine(ss));
		}
		return sb.toString();
	}
}
