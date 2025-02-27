package Properties;

import java.util.ArrayList;
import java.util.List;

public interface LoggingMessages 
{
	public static final String COMBINE_DELIMITER=", ";
	
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
		return combine(COMBINE_DELIMITER, out);
	}
	public static String combine(String delimit, Object ...out)
	{
		StringBuffer sb = new StringBuffer();
		for(Object s : out)
		{
			sb.append(s.toString() + delimit);
		}
		return (String) sb.subSequence(0, sb.length() - delimit.length());
	}
	public static String combine(List<?> out)
	{
		return combine(COMBINE_DELIMITER, out);
	}
	public static String combine(String delimit, List<?> out)
	{
		return combine(delimit, out.toArray(new Object[] {}));
	}
	public static String combine(ArrayList<List<?>> out)
	{
		return combine(COMBINE_DELIMITER, out);
	}
	public static String combine(String delimit, ArrayList<List<?>> out)
	{
		StringBuilder sb = new StringBuilder();
		for(List<?> ss: out)
		{
			sb.append(combine(delimit, ss));
		}
		return sb.toString();
	}
}
