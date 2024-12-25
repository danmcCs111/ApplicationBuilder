package WidgetExtensions;

import java.util.HashMap;
import java.util.List;

import javax.swing.JComponent;

/**
 * Holds a variable number of Components
 */
public class Collection extends JComponent
{
	private static final long serialVersionUID = 1L;
	
	public HashMap<String, List<JComponent>> collectionNameAndList = new HashMap<String, List<JComponent>>();
	
}
