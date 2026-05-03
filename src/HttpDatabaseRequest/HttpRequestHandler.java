package HttpDatabaseRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import ActionListeners.ArrayActionListener;
import WidgetComponents.JButtonArray;

public class HttpRequestHandler implements HttpHandler
{
	public static final String 
		REQUEST_TYPE_HEADER_KEY = "Get-request-type",
		FUNCTION_TYPE_LAUNCH_URL = "URL_Launch",
		FUNCTION_TYPE_ADD_SUBSCRIBER_LAUNCH_URL = "URL_Launch_Add_Subscriber",
		FUNCTION_TYPE_JOYSTICK = "Joystick_Button";

	private ProcessType 
		proc;
	private ArrayActionListener 
		aal;
	private HttpLaunchUrlRequest
		hlur;
	
	public enum ProcessType{
		parent,
		child
	}
	
	public HttpRequestHandler(ArrayActionListener aal, JButtonArray ba, ProcessType proc)
	{
		this.aal = aal;
		HttpJoystickFuctionRequest.setButtonArray(ba);
		this.proc = proc;
		hlur = new HttpLaunchUrlRequest();
	}
	
	public HttpRequestHandler(ArrayActionListener aal, ProcessType proc)
	{
		this.aal = aal;
		this.proc = proc;
		hlur = new HttpLaunchUrlRequest();
	}
	
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
	
	private String execute(Headers h, String result)
	{
		String responseXml = result;
		if(h.containsKey(REQUEST_TYPE_HEADER_KEY))
		{
			if(h.get(REQUEST_TYPE_HEADER_KEY).contains(FUNCTION_TYPE_JOYSTICK))
			{
				HttpJoystickFuctionRequest.process(responseXml);
			}
			else if(h.get(REQUEST_TYPE_HEADER_KEY).contains(FUNCTION_TYPE_LAUNCH_URL))
			{
				hlur.processLaunch(aal, responseXml, proc);
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

