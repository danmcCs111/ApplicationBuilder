package HttpDatabaseRequest;

import java.io.IOException;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;

import WidgetComponents.JButtonArray;

public class HttpRequestProcessor 
{
	private JButtonArray
		ba;
	private static int 
		portNumber = 9090;//static, 1 per process.
	
	public HttpRequestProcessor(JButtonArray ba)
	{
		this.ba = ba;
	}
	
	public HttpRequestProcessor()
	{
		
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
				server.createContext("/", new HttpRequestHandler(ba));
			}
			else
			{
				server.createContext("/", new HttpRequestHandler());
			}
	        server.setExecutor(null); // Use the default executor
	        server.start();
	        System.out.println("Server is running on port " + portNumber);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
