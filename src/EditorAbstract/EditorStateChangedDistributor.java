package EditorAbstract;

import java.util.ArrayList;

import ActionListeners.EditorStateChangeListener;
import Params.ParameterEditor;

public class EditorStateChangedDistributor 
{
	private ParameterEditor pe;
	
	public EditorStateChangedDistributor(ParameterEditor pe)
	{
		this.pe = pe;
	}
	
	private ArrayList<EditorStateChangeListener> 
		editorChangeListeners = new ArrayList<EditorStateChangeListener>(),
		editorChangeListenersRemove = new ArrayList<EditorStateChangeListener>();
	
	public void addEditorChangeListener(EditorStateChangeListener editorChangeListener)
	{
		editorChangeListeners.add(editorChangeListener);
	}
	
	public void removeEditorChangeListener(EditorStateChangeListener editorChangeListener)
	{
		if(editorChangeListeners.contains(editorChangeListener))
		{
			editorChangeListenersRemove.add(editorChangeListener);
		}
	}
	
	public void notifyEditorChangeListener()
	{
		for(EditorStateChangeListener escl : editorChangeListenersRemove)
		{
			editorChangeListeners.remove(escl);
		}
		for(EditorStateChangeListener escl : editorChangeListeners)
		{
			escl.notifyEditorChanged(pe);
		}
	}
}
