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
import Params.KeepSelection;
import WidgetComponentDialogs.ShiftDialog;
import WidgetComponents.JButtonArray;
import WidgetUtility.WidgetBuildController;

public class HttpRequestHandler implements HttpHandler 
{
	private static final String 
		REQUEST_TYPE_HEADER_KEY = "Get-request-type",
		FUNCTION_TYPE = "Joystick_Button";
	private static final int
		SHIFT_AMOUNT = 30;
	
	
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
				
				
				if(ba.getKeepSelection().size() > 0)
				{
					boolean positive = responseXml.endsWith("true");
					
					if(responseXml.equals("LEFTBUMPER"))
					{
						((JFrame) ba.getTopLevelAncestor()).setExtendedState(Frame.ICONIFIED);
						ba.performMinimize();
						ba.performRestore();
					}
					else if(responseXml.equals("RIGHTBUMPER"))
					{
						((JFrame) ba.getTopLevelAncestor()).setExtendedState(Frame.ICONIFIED);
						ba.performMinimize();
					}
					
					else if(responseXml.startsWith("RIGHTX"))
					{
						//shift
						for(KeepSelection ks : ba.getKeepSelection())
						{
							if(ks.getFrame().getExtendedState() == Frame.NORMAL)
							{
								ShiftDialog.updateKeep(ks, true, false, positive?SHIFT_AMOUNT:-SHIFT_AMOUNT);
							}
						}
					}
					else if(responseXml.startsWith("RIGHTY"))
					{
						//shift
						for(KeepSelection ks : ba.getKeepSelection())
						{
							if(ks.getFrame().getExtendedState() == Frame.NORMAL)
							{
								ShiftDialog.updateKeep(ks, false, true, positive?-SHIFT_AMOUNT:SHIFT_AMOUNT);
							}
						}
					}
				}//End open bookmarks req.
				if(responseXml.equals("START"))
				{
					JFrame f = ((JFrame) ba.getTopLevelAncestor());
					int 
						state = f.getExtendedState();
					f.setExtendedState( (state == Frame.NORMAL)
							?Frame.ICONIFIED
							:Frame.NORMAL
					);
				}
				else if(responseXml.equals("BACK"))
				{
					AbstractButton ab = (AbstractButton) WidgetBuildController.getInstance().findRefByName(
							LaunchUrlActionListener.CLOSE_LAUNCH_ACTION_EVENT).getInstance(); //TODO.
					ab.doClick();
				}
				
				else if(responseXml.equals("A"))
				{
					//select highlighed
				}
				else if(responseXml.equals("X"))
				{
					//select menu
				}
				else if(responseXml.equals("Y"))
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

