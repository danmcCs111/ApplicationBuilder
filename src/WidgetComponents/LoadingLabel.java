package WidgetComponents;

import javax.swing.JLabel;

public class LoadingLabel extends JLabel implements ButtonArrayLoadingNotification 
{
	private static final long serialVersionUID = 1L;

	public LoadingLabel()
	{
		this.setText("Loading...");
	}
	
	@Override
	public void updateCount(int count, int totalCount) 
	{
		this.setText("Loading: " + count + " / " + totalCount);
	}
	
}
