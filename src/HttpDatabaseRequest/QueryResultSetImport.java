package HttpDatabaseRequest;

import java.util.ArrayList;

import org.w3c.dom.Node;

import Properties.LoggingMessages;
import Properties.XmlNodeReader;

public class QueryResultSetImport extends XmlNodeReader 
{

	@Override
	public Object createNewObjectFromNode(Node n, ArrayList<String> attributes, int counter, String parentNode) 
	{
		LoggingMessages.printOut(n.getNodeValue());
		return null;
	}

	@Override
	public String getFileTypeTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFileTypeFilter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDefaultDirectoryRelative() {
		// TODO Auto-generated method stub
		return null;
	}

}
