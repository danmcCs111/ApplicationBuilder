package WidgetComponents;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import ObjectTypeConversion.PageParserCollection;

public class GeoNamesUploader extends JPanel 
{
	private static final long serialVersionUID = 1L;
	
	private static PageParserCollection 
		pageParserCollection = null;

	private JButton
		collectButton;
	
	public GeoNamesUploader()
	{
		
	}
	
	private void buildWidgets()
	{
		collectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
	}
	
	public static void setPageParserCollection(PageParserCollection pp)
	{
		pageParserCollection = pp;
	}
}
