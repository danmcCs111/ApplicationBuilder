package ActionListeners;

import java.awt.event.ActionListener;
import java.util.Arrays;

/**
 * TODO Placeholder for loading from configuration file? or directory?
 */
public enum ActionListenersRegistered 
{
	ComponentComboBoxActionListener(ComponentComboBoxActionListener.class),
	OpenDetailsActionListener(OpenDetailsActionListener.class),
	OpenParameterEditorActionListener(OpenParameterEditorActionListener.class),
	MinimizeActionListener(MinimizeActionListener.class),
	NavigationButtonActionListener(NavigationButtonActionListener.class),
	ReloadActionListener(ReloadActionListener.class),
	ExitActionListener(ExitActionListener.class),
	LaunchActionListener(LaunchActionListener.class);
	
	private Class<? extends ActionListener> clazz;
	private String classXmlText;
	
	private ActionListenersRegistered (Class<? extends ActionListener> clazz)
	{
		this.clazz = clazz;
		this.classXmlText = clazz.getName();
	}
	
	public Class<? extends ActionListener> getClazz()
	{
		return this.clazz;
	}
	
	public String getClassXmlText()
	{
		return this.classXmlText;
	}
	
	public boolean acceptsActionListenerSubType()
	{
		boolean isAl = Arrays.asList(clazz.getDeclaredClasses()).contains(ActionListenerSubTypeExtension.class);
		return isAl;
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
