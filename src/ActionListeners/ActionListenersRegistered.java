package ActionListeners;

import java.awt.event.ActionListener;

/**
 * TODO Placeholder for loading from configuration file?
 */
public enum ActionListenersRegistered {

	ComponentComboBoxActionListener(ComponentComboBoxActionListener.class, ComponentComboBoxActionListener.class.getName()),
	OpenDetailsActionListener(OpenDetailsActionListener.class, OpenDetailsActionListener.class.getName()),
	OpenParameterEditorActionListener(OpenParameterEditorActionListener.class, OpenParameterEditorActionListener.class.getName()),
	MinimizeActionListener(MinimizeActionListener.class, MinimizeActionListener.class.getName()),
	NavigationButtonActionListener(NavigationButtonActionListener.class, NavigationButtonActionListener.class.getName()),
	ReloadActionListener(ReloadActionListener.class, ReloadActionListener.class.getName()),
	ExitActionListener(ExitActionListener.class, ExitActionListener.class.getName()),
	LaunchActionListener(LaunchActionListener.class, LaunchActionListener.class.getName());
	
	private Class<? extends ActionListener> clazz;
	private String classXmlText;
	
	private ActionListenersRegistered (Class<? extends ActionListener> clazz, String classXmlText)
	{
		this.clazz = clazz;
		this.classXmlText = classXmlText;
	}
	
	public Class<? extends ActionListener> getClazz()
	{
		return this.clazz;
	}
	
	public String getClassXmlText()
	{
		return this.classXmlText;
	}
	
	public static ActionListenersRegistered getActionListener(String classXmlText)
	{
		ActionListenersRegistered retVal = null;
		for(ActionListenersRegistered alr : ActionListenersRegistered.values())
		{
			if(alr.getClassXmlText().endsWith(classXmlText))
			{
				retVal = alr;
				break;
			}
		}
		return retVal;
	}
}
