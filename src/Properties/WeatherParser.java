package Properties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeatherParser 
{
	private static ArrayList<String> valuesKey = new ArrayList<String>();
	private static HashMap<Integer, String[]> keyAndValues = new HashMap<Integer, String[]>();
	private static String lastKey = null;
	private static int keyCount = 0;
	
	private static final String [] SWAP_REGEX = {"[{]+","[}]+"};
	private static final String [] SWAP_PLACE = {"{","}"};
	
	synchronized//TODO b/c of "key count"
	public static ArrayList<String> parse(String text) 
	{
		ArrayList<String> values = new ArrayList<String>();
		printAndReturn(text, 0);
		for(int i =0; i < keyCount; i++)
		{
			String rec = i + " | " + keyAndValues.get(i)[0] + keyAndValues.get(i)[1];
			LoggingMessages.printOut(rec);
			LoggingMessages.printNewLine();
			values.add(rec);
		}
		keyCount = 0;
		return values;
	}
	
	synchronized
	public static void printAndReturn(String text, int delimit)
	{
		String [] ss = text.split(SWAP_REGEX[delimit], 2);
		if(ss != null && ss.length > 1 && ss[1].length() > 0)
		{
			String [] vs2 = ss[0].split(",");
			String print = LoggingMessages.combine(vs2);
			
			Pattern pattern = Pattern.compile("}");
			Matcher matcher = pattern.matcher(print);
			int count = 0; 
			while(matcher.find())
				count++;
			
			if(!print.isEmpty() && count == 0)
			{
				if(print.endsWith(":"))
				{
					if(lastKey != null)
					{
						keyAndValues.put(keyCount++, new String [] { lastKey, LoggingMessages.combine(valuesKey)});
						valuesKey = new ArrayList<String>();
					}
					lastKey = print;
				}
				valuesKey.add(print + (SWAP_PLACE[delimit].equals("}") ? "}," : SWAP_PLACE[delimit]));
			}
			printAndReturn(ss[0], delimit == 0 ? 1 : 0);
			printAndReturn(ss[1], delimit);
		}
	}
}
