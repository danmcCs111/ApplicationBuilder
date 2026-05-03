package HttpDatabaseRequest;

import java.io.IOException;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;

import ActionListeners.ArrayActionListener;
import HttpDatabaseRequest.HttpRequestHandler.ProcessType;
import WidgetComponents.JButtonArray;

public class HttpRequestProcessor 
{
	private static int 
		portNumber = 9090;//static, 1 per process.
	
	private JButtonArray
		ba;
	private ProcessType 
		procType;
	private ArrayActionListener
		aal;
	
	public HttpRequestProcessor(ArrayActionListener aal, JButtonArray ba, ProcessType procType)
	{
		this.aal = aal;
		this.ba = ba;
		this.procType = procType;
	}
	
	public HttpRequestProcessor(ArrayActionListener aal, ProcessType procType)
	{
		this.aal = aal;
		this.procType = procType;
	}
	
	public static void setPortNumber(int portNumber)
	{
		HttpRequestProcessor.portNumber = portNumber;
	}
	
	public static int getPortNumber()
	{
		return HttpRequestProcessor.portNumber;
	}

	public void listenHttp()
	{
		try {
			HttpServer server = HttpServer.create(new InetSocketAddress(portNumber), 1);
			if(ba != null)
			{
				server.createContext("/", new HttpRequestHandler(aal, ba, procType));
			}
			else
			{
				server.createContext("/", new HttpRequestHandler(aal, procType));
			}
	        server.setExecutor(null); // Use the default executor
	        server.start();
	        System.out.println("Server is running on port " + portNumber);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
