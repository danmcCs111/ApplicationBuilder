package HttpDatabaseRequest;

import java.awt.Frame;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.AbstractButton;
import javax.swing.JFrame;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import ActionListenersImpl.LaunchUrlActionListener;
import WidgetComponents.JButtonArray;
import WidgetUtility.WidgetBuildController;

public class HttpRequestHandler implements HttpHandler 
{
	private static final String 
		REQUEST_TYPE_HEADER_KEY = "Get-request-type",
		FUNCTION_TYPE = "Joystick_Button";
	
	@Override
	public void handle(HttpExchange exchange) throws IOException 
	{
		Headers h = exchange.getRequestHeaders();
		InputStream is = exchange.getRequestBody();
		String result = readFromInputStreamToString(is);
		
		String response = "This is the response " + "\n";
		response += getRequestHeaderAsString(h);

		String responseXml = execute(h, result);
		
		exchange.sendResponseHeaders(200, response.length() + responseXml.length());
		OutputStream os = exchange.getResponseBody();
		os.write(response.getBytes());
		os.write(responseXml.getBytes());
		os.close();
	}
	
	private static String readFromInputStreamToString(InputStream is)
	{
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String result = br.lines().collect(Collectors.joining("\n"));
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private static String execute(Headers h, String result)
	{
		String responseXml = result;
		if(h.containsKey(REQUEST_TYPE_HEADER_KEY))
		{
			if(h.get(REQUEST_TYPE_HEADER_KEY).contains(FUNCTION_TYPE))
			{
				System.out.println(responseXml);
				//TODO. map input to actions.
				JButtonArray ba = (JButtonArray) WidgetBuildController.getInstance().findRefByName(
						"channels").getInstance(); //TODO. Mapping file. handle navigation
				
				if(responseXml.equals("LEFTBUMPER"))
				{
					ba.performRestore();
				}
				if(responseXml.equals("RIGHTBUMPER"))
				{
					((JFrame) ba.getTopLevelAncestor()).setExtendedState(Frame.ICONIFIED);
					ba.performMinimize();
				}
				if(responseXml.equals("BACK"))
				{
					AbstractButton ab = (AbstractButton) WidgetBuildController.getInstance().findRefByName(
							LaunchUrlActionListener.CLOSE_LAUNCH_ACTION_EVENT).getInstance(); //TODO.
					ab.doClick();
				}
				if(responseXml.equals("A"))
				{
					//select highlighed
				}
				if(responseXml.equals("X"))
				{
					//select menu
				}
				if(responseXml.equals("Y"))
				{
					//open video list
				}
			}
		}
		
		return responseXml;
	}
	
	private static String getRequestHeaderAsString(Headers h)
	{
		String retResponse = "";
		for(String key : h.keySet())
		{
			retResponse += "[KEY] " + key + " ---> ";
			List<String> headers = h.get(key);
			for(String s : headers)
				retResponse += "[VALUE] " + s + "\n";
		}
		return retResponse;
	}
	

}

