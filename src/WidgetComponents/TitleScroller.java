package WidgetComponents;

import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JTextField;

import ActionListeners.ArrayActionListener;
import ActionListenersImpl.LaunchUrlActionListener;
import Properties.StringUtility;
import WidgetComponentInterfaces.PostWidgetBuildProcessing;

public class TitleScroller extends JTextField implements ArrayActionListener, PostWidgetBuildProcessing
{
	private static final long serialVersionUID = 1L;
	
	private static String
		REPLACE_VALUE="<arg>",
		FORMAT_VIDEO="[ <arg> ] ",
		FORMAT_CHANNEL=" -- <arg>",
		FORMAT_VIDEO_CHANNEL = "",
		DEFAULT_TEXT = "<stopped>";
	private Thread
		spanThread;
	private boolean 
		scroll = false;
	private int
		scrollOverCountFront = 100,
		scrollOverCountBack = 25,
		scrollCountOverBackJumpPosition = 1000,
		scrollSpeedMillis = 200;
	
	public TitleScroller()
	{
		LaunchUrlActionListener.addArrayActionListener(this);
	}

	public void setFormatVideo(String formatVideo)
	{
		FORMAT_VIDEO = formatVideo;
	}
	public void setFormatChannel(String formatChannel)
	{
		FORMAT_CHANNEL = formatChannel;
	}
	public void setReplaceValue(String replValue)
	{
		REPLACE_VALUE = replValue;
	}
	public void setDefaultTitle(String defaultTitle)
	{
		DEFAULT_TEXT = defaultTitle;
	}
	public void setScrollOption(boolean scroll)
	{
		this.scroll = scroll;
	}
	public void setScrollSpeedMillis(int scrollSpeed)
	{
		this.scrollSpeedMillis = scrollSpeed;
	}
	public void setScrollOverCountFront(int scrollCountOverFront)
	{
		this.scrollOverCountFront = scrollCountOverFront;
	}
	public void setScrollOverCountBack(int scrollCountOverBack)
	{
		this.scrollOverCountBack = scrollCountOverBack;
	}
	public void setScrollOverCountBackJumpPosition(int scrollCountOverBackJumpPosition)
	{
		this.scrollCountOverBackJumpPosition = scrollCountOverBackJumpPosition;
	}
	
	private void startScrollThread()
	{
		Runnable r = new Runnable() 
		{
			@Override
			public void run() 
			{
				String chnlPat = FORMAT_CHANNEL;
				chnlPat = chnlPat.replaceAll(REPLACE_VALUE, "");
				String lastText = TitleScroller.this.getText();
				int 
					frontCount = 0,
					backCount = 0,
					position = 0,
					dir = 1;
				
				while(true)
				{
					if(TitleScroller.this.getText().contains(chnlPat))
					{
						if(position >= TitleScroller.this.getText().length())
						{
							if(backCount < scrollOverCountBack)
							{
								backCount++;
								position = TitleScroller.this.getText().length();
							}
							else
							{
								backCount=0;
								position -= scrollCountOverBackJumpPosition;
								if(position < 0) position = 0;
								dir = -1;
							}
						}
						else if(position <= 0)
						{
							if(frontCount < scrollOverCountFront)
							{
								frontCount++;
								position = 0;
							}
							else
							{
								frontCount = 0;
								position = 0;
								dir = 1;
							}
						}
						
						if(!lastText.equals(TitleScroller.this.getText()))
						{
							//reset.
							position = 0;
							dir = 1;
							lastText = TitleScroller.this.getText();
						}
						TitleScroller.this.setCaretPosition(position);
						TitleScroller.this.validate();
						//scroll.
						position += dir;
					}
					try {
						Thread.sleep(scrollSpeedMillis);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		spanThread = new Thread(r);
		spanThread.start();
	}
	
	@Override
	public void urlSelect(AbstractButton newButton) 
	{
		if(spanThread == null && scroll)
		{
			startScrollThread();
		}
		
		if(newButton == null)
		{
			TitleScroller.this.setText(DEFAULT_TEXT);
			TitleScroller.this.setToolTipText(null);
			return;
		}
		
		if(newButton instanceof JButtonLengthLimited)
		{
			JButtonLengthLimited jbll = (JButtonLengthLimited)newButton;
			String 
				textParent = jbll.getHighlightButton().getText(),
				textChild = jbll.getText();
			if(!textChild.equals(textParent))
			{
				TitleScroller.this.setText(
					StringUtility.replaceArg(
						FORMAT_VIDEO_CHANNEL, 
						REPLACE_VALUE, 
						new String[] {textParent, textChild}
					)
				);
				TitleScroller.this.setToolTipText(getText());
				TitleScroller.this.setCaretPosition(0);
				return;//
			}
		}
		
		TitleScroller.this.setText(
			StringUtility.replaceArg(
				FORMAT_VIDEO, REPLACE_VALUE, newButton.getText()
			)
		);
		TitleScroller.this.setToolTipText(getText());
		TitleScroller.this.setCaretPosition(0);
	}
	
	@Override
	public void addArrayActionListener() {
		LaunchUrlActionListener.addArrayActionListener(this);
	}

	@Override
	public void removeArrayActionListener() {
		LaunchUrlActionListener.removeArrayActionListener(this);
	}

	@Override
	public void addStripFilter(String filter) {
		// TODO Auto-generated method stub
	}
	@Override
	public void addActionListener(ActionListener actionListener) {
		// TODO Auto-generated method stub
	}

	@Override
	public void postExecute() 
	{
		FORMAT_VIDEO_CHANNEL = FORMAT_VIDEO + FORMAT_CHANNEL;
	}

}
