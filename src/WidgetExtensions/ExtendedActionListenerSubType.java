package WidgetExtensions;

import ActionListeners.ActionListenerSubTypeExtension;

public class ExtendedActionListenerSubType implements ExtendedAttributeStringParam
{
	private ActionListenerSubTypeExtension actionListenerExtension;
	private ExtendedActionListenerSubTypeAttribute subTypeAttribute;
	
	public ExtendedActionListenerSubType(ActionListenerSubTypeExtension ale, ExtendedActionListenerSubTypeAttribute subTypeAttribute)
	{
		this.actionListenerExtension = ale;
		this.subTypeAttribute = subTypeAttribute;
	}
	
	@Override
	public void applyMethod(String arg0)
	{
		try {
			Class<?> clazz = Class.forName(arg0);
			actionListenerExtension.setActionListenerSubTypeExtension(clazz, subTypeAttribute.getSubTypeAttribute());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		ActionListener [] al = component.getListeners(ActionListener.class);
//		applySubTypeActionListener(al, listenerSubType);
	}
	
//	private static void applySubTypeActionListener(ActionListener [] actionListener, String listenerSubType)
//	{
//		for(ActionListener al : actionListener)
//		{
//			ActionListenersRegistered alr = ActionListenersRegistered.getActionListener(al.getClass().getName());
//			if(alr.acceptsActionListenerSubType())
//			{
//				ActionListenerSubTypeExtension ale = (ActionListenerSubTypeExtension) al;
//			}
//		}
//	}
}
