package ApplicationBuilder;

import javax.swing.JFrame;

import Properties.WidgetTextProperties;

public class BuilderWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final String SOURCE_FILE = "src\\ApplicationBuilder\\data\\WidgetBuild.xml";
	
	public BuilderWindow()
	{
		new CommandBuildController(SOURCE_FILE);
		
//		addMenuButtons();
		
		setTitle(WidgetTextProperties.APPLICATION_TITLE.getPropertiesValue());
		setLocation(150, 150);
		
//		BorderLayout bl = new BorderLayout();
//		scrPane = new JScrollPane(innerPanel);
//		BorderLayout bl2 = new BorderLayout();
//		innerPanel2.setLayout(bl2);
//		innerPanel2.add(scrPane, BorderLayout.NORTH);
//		this.setLayout(bl);
//		this.add(innerPanel2, BorderLayout.CENTER);
//		
//		createNavigationButtons();
//		addChannelButtons();
//		
//		this.setSize(getWindowWidth(), winHeight);
//		this.addWindowListener(new RokuLauncherWindowListener(this));
//		this.addComponentListener(new RokuLauncherComponentListener(this));
		
		this.setSize(480, 640);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
