package ActionListenersImpl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ObjectTypeConversion.CsvReader;

public class CsvReaderListener implements ActionListener 
{
	private CsvReader csvReader;
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(csvReader != null)
		{
			csvReader.read();
		}
	}
	
	public void setCsvReader(CsvReader csvReader) 
	{
		this.csvReader = csvReader;
	}

}
