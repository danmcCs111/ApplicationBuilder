package ActionListenersImpl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ObjectTypeConversion.WavReader;
import WidgetUtility.WidgetBuildController;

public class WavReaderListener implements ActionListener 
{
	private WavReader wavReader;
	
	public WavReaderListener()
	{
		setWavReader((WavReader) WidgetBuildController.getInstance().getAppObject(WavReader.class.getName()));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		wavReader.read();
	}
	
	public void setWavReader(WavReader wavReader) 
	{
		this.wavReader = wavReader;
	}
	
}
