package ObjectTypeConversion;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import ActionListeners.CsvReaderSubscriber;
import Properties.LoggingMessages;

public class CsvReader
{
	private File f;
	private ArrayList<ArrayList<String>> capturedCsvValues = new ArrayList<ArrayList<String>>();
	private ArrayList<CsvReaderSubscriber> subs = new ArrayList<CsvReaderSubscriber>();
	
	public CsvReader(String arg)
	{
		this.f = new File(arg);
	}
	
	public String getAbsolutePath()
	{
		return f.getAbsolutePath();
	}
	
	public ArrayList<ArrayList<String>> getCapturedCsvValues()
	{
		return this.capturedCsvValues;
	}
	
	public void read()
	{
		LoggingMessages.printOut(f.getAbsolutePath());
		try {
			Scanner reader = new Scanner(f);
		      while (reader.hasNextLine()) 
		      {
		        String data = reader.nextLine();
		        ArrayList<String> row = new ArrayList<String>();
		        row.addAll(Arrays.asList(data.split("\",\"")));//TODO. forcing quotes.
		        capturedCsvValues.add(row);
		      }
		      reader.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		for(CsvReaderSubscriber sub : subs)
		{
			sub.notify(this);
		}
	}
	
	public void addCsvReaderSubscriber(CsvReaderSubscriber sub)
	{
		subs.add(sub);
	}
	
}
