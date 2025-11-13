package ObjectTypeConversion;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.UIManager;

public class LookAndFeelClassName
{
	private static HashMap<String, String> lookAndFeels = new HashMap<String, String>();
	static
	{
		lookAndFeels.put("System", UIManager.getSystemLookAndFeelClassName());
		lookAndFeels.put("CrossPlatform", UIManager.getCrossPlatformLookAndFeelClassName());
	};
	private static ArrayList<String> keyList = new ArrayList<String>(lookAndFeels.keySet());
	private String key;
	
	public LookAndFeelClassName(String arg1)
	{
		key = arg1;
	}
	
	public static String [] getLookAndFeelKeys()
	{
		return (String[]) keyList.toArray(new String[] {});
	}
	
	public String getKey()
	{
		return key;
	}
	
	public String getLookAndFeel()
	{
		return lookAndFeels.get(key);
	}
	
	@Override
	public String toString()
	{
		return getKey();
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o instanceof LookAndFeelClassName)
		{
			return this.getKey().equals(((LookAndFeelClassName) o).getKey());
		}
		return false;
	}

}
