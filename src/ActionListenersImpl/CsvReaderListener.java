package ActionListenersImpl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ObjectTypeConversion.CsvReader;
import WidgetUtility.WidgetBuildController;

public class CsvReaderListener implements ActionListener 
{
	private CsvReader csvReader;
	
	public CsvReaderListener()
	{
		setCsvReader((CsvReader) WidgetBuildController.getInstance().getAppObject(CsvReader.class.getName()));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		csvReader.read();
	}
	
	public void setCsvReader(CsvReader csvReader) 
	{
		this.csvReader = csvReader;
	}
	
}
