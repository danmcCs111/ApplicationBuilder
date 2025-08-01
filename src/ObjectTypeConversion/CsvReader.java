package ObjectTypeConversion;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import Properties.LoggingMessages;

public class CsvReader 
{
	private File f;
	private ArrayList<ArrayList<String>> capturedCsvValues = new ArrayList<ArrayList<String>>();
	
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
		      printCapturedValues();
		     
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void printCapturedValues()
	{
		int count = 0;
		for(ArrayList<String> row : capturedCsvValues)
		{
			LoggingMessages.printOut("row : " + count);
			for(String c : row)
			{
				LoggingMessages.printOut(c);
			}
			count++;
		}
		
		WeatherGrabCsvConverter wgc = new WeatherGrabCsvConverter(this);//TODO.
//		wgc.printValues();
	}
	
}
