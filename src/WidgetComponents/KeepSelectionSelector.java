package WidgetComponents;

import java.util.ArrayList;

import Params.KeepSelection;

public class KeepSelectionSelector 
{
	private KeepSelection
		ks;
	private int 
		keepIndex = 0;
	private ArrayList<KeepSelection>
		keeps;
	
	public KeepSelectionSelector()
	{
		
	}
	
	public void setKeeps(ArrayList<KeepSelection> ks)
	{
		this.keeps = ks;
	}
	
	public KeepSelection getSelectedKeep()
	{
		return keeps.get(keepIndex);
	}
}
