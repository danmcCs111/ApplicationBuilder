package HttpDatabaseRequest;

import java.awt.Point;
import java.io.IOException;
import java.net.InetSocketAddress;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.sun.net.httpserver.HttpServer;

import ActionListeners.ArrayActionListener;
import Graphics2D.GraphicsUtil;
import HttpDatabaseRequest.HttpRequestHandler.ProcessType;
import WidgetComponents.JButtonArray;

public class HttpRequestProcessor 
{
	private static int 
		portNumber = 9090;//static, 1 per process.
	
	private JButtonArray
		ba;
	private HttpRequestHandler
		hrh;
	private Point
		dialogLocation = null;

	public HttpRequestProcessor(JButtonArray ba, ProcessType procType, ArrayActionListener ... aals)
	{
		this.ba = ba;
		hrh = new HttpRequestHandler(ba, procType, aals);
	}
	
	public HttpRequestProcessor(ProcessType procType, ArrayActionListener ... aals)
	{
		hrh = new HttpRequestHandler(procType, aals);
	}
	
	public void setDialogLocation(Point dialogLocation)
	{
		this.dialogLocation = dialogLocation;
	}
	
	public void setArrayActionListener(ArrayActionListener aal, int index)
	{
		hrh.setArrayActionListener(aal, index);
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
				server.createContext("/", hrh);
			}
			else
			{
				server.createContext("/", hrh);
			}
	        server.setExecutor(null); // Use the default executor
	        server.start();
	        System.out.println("Server is running on port " + portNumber);
		} catch (IOException e) {
			JOptionPane optionPane = new JOptionPane(e.getMessage(), JOptionPane.ERROR_MESSAGE);
			JDialog dialog = optionPane.createDialog(ba, "Error");
			if(ba == null)
			{
				if(dialogLocation != null)
				{
					dialog.setLocation(dialogLocation);
				}
			}
			else
			{
				GraphicsUtil.centerWindow(ba, dialog);
			}
			dialog.setVisible(true);
			e.printStackTrace();
			System.exit(1);
		}
	}
}
