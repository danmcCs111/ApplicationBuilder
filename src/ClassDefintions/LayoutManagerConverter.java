package ClassDefintions;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;

public class LayoutManagerConverter implements StringToObjectConverter {

	public static LayoutManager getLayoutManager(String arg0)
	{
		LayoutManager lm = null;
		if("BorderLayout".equals(arg0))
		{
			lm = new BorderLayout();
		}
		else if("GridLayout".equals(arg0))
		{
			lm = new GridLayout();
		}
		else if("FlowLayout".equals(arg0))
		{
			lm = new FlowLayout();
		}
		
		return lm;
	}

	@Override
	public int numberOfArgs() {
		return 1;
	}

	@Override
	public Object conversionCall(String... args) {
		return getLayoutManager(args[0]);
	}
	
	@Override
	public String toString()
	{
		return LayoutManagerConverter.class.toString();
	}
}
