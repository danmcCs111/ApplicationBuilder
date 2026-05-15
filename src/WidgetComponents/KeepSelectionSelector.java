package WidgetComponents;

import java.util.ArrayList;
import java.util.Collections;

import Params.KeepSelection;

public class KeepSelectionSelector 
{
	private int 
		keepIndex = 0;
	private JButtonArray
		ba;
	
	private boolean 
		selected = true;
	
	public KeepSelectionSelector(JButtonArray ba)
	{
		this.ba = ba;
	}
	
	public void setSelected(boolean sel)
	{
		selected = sel;
	}
	
	public boolean isSelected()
	{
		return selected;
	}
	
	public void decrementIndex()
	{
		if((keepIndex - 1 >=  0) && keepIndex -1 <= getKeeps().size()-1)
		{
			keepIndex--;
		}
		else
		{
			keepIndex = getKeeps().size()-1;
		}
	}
	
	public void advanceIndex()
	{
		if(keepIndex + 1 <= getKeeps().size()-1)
		{
			keepIndex++;
		}
		else
		{
			keepIndex = 0;
		}
	}

	public ArrayList<KeepSelection> getKeeps()
	{
		ArrayList<KeepSelection> kss = ba.getKeepSelection();
		Collections.sort(kss, new KeepSelection());
		return kss;
	}
	
	public KeepSelection getSelectedKeep()
	{
		return getKeeps().get(keepIndex);
	}
}
