package ApplicationBuilder;

public class LoggingMessages {
	
	public static void printOut(String ... out)
	{
		for(String s : out)
		{
			System.out.println(s);
		}
	}
	public static void printNewLine()
	{
		System.out.println();
	}
}
