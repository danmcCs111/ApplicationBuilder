package ActionListeners;

import java.awt.event.ActionListener;

public enum ActionListenersRegistered {

	ComponentComboBoxActionListener(ComponentComboBoxActionListener.class, "ComponentComboBoxActionListener"),
	OpenDetailsActionListener(OpenDetailsActionListener.class, "OpenDetailsActionListener"),
	OpenParameterEditorActionListener(OpenParameterEditorActionListener.class, "OpenParameterEditorActionListener"),
	MinimizeActionListener(MinimizeActionListener.class, "MinimizeActionListener"),
	NavigationButtonActionListener(NavigationButtonActionListener.class, "NavigationButtonActionListener"),
	ReloadActionListener(ReloadActionListener.class, "ReloadActionListener"),
	ExitActionListener(ExitActionListener.class, "ExitActionListener"),
	LaunchActionListener(LaunchActionListener.class, "LaunchActionListener");
	
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
			if(alr.getClassXmlText().equals(classXmlText))
			{
				retVal = alr;
				break;
			}
		}
		return retVal;
	}
}
