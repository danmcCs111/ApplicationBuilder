package ActionListeners;

import java.awt.event.ActionListener;

public enum ActionListenersRegistered {

		ComponentComboBoxActionListener(ComponentComboBoxActionListener.class, ""),
		OpenDetailsActionListener(OpenDetailsActionListener.class, ""),
		OpenParameterEditorActionListener(OpenParameterEditorActionListener.class, "");
	
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
	
	public ActionListenersRegistered getActionListener(String classXmlText)
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
