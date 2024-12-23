package ApplicationBuilder;

import javax.swing.JFrame;

public abstract class RedrawableFrame extends JFrame
{
	private static final long serialVersionUID = 1L;

	public abstract void clearInnerPanels();
	public abstract void rebuildInnerPanels();
}
