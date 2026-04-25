package HttpDatabaseRequest;

import java.io.IOException;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;

import WidgetComponents.JButtonArray;

public class HttpRequestProcessor 
{
	private JButtonArray
		ba;
	private int 
		portNumber = 9090;
	
	public HttpRequestProcessor(JButtonArray ba)
	{
		this.ba = ba;
	}

	public void listenHttp()
	{
		try {
			HttpServer server = HttpServer.create(new InetSocketAddress(portNumber), 1);
	        server.createContext("/", new HttpRequestHandler(ba));
	        server.setExecutor(null); // Use the default executor
	        server.start();
	        System.out.println("Server is running on port " + portNumber);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
