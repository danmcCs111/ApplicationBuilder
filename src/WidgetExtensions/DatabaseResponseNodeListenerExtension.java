package WidgetExtensions;

import java.util.ArrayList;

import HttpDatabaseResponse.DatabaseResponseNode;

public interface DatabaseResponseNodeListenerExtension 
{
	public void setResults(ArrayList<ArrayList<DatabaseResponseNode>> results);
}
