package WidgetComponents;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.IOException;

import HttpDatabaseRequest.HttpDatabaseRequest;
import HttpDatabaseRequest.YoutubePageParser;
import Properties.LoggingMessages;
import WidgetComponentInterfaces.LinkDragAndDropSubscriber;

public class LinkDragAndDropListener extends DropTargetAdapter
{
	private LinkDragAndDropSubscriber ldds;
	
	public LinkDragAndDropListener(LinkDragAndDropSubscriber ldds)
	{
		this.ldds = ldds;
	}
	
	@Override
	public void drop(DropTargetDropEvent dtde) 
	{
		dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
		Transferable t = dtde.getTransferable();
		try {
			if(t.isDataFlavorSupported(DataFlavor.stringFlavor))
			{
				String s = (String) t.getTransferData(DataFlavor.stringFlavor);
				if(YoutubePageParser.isYoutube(s))
				{
					//TODO
					s = HttpDatabaseRequest.addHttpsIfMissing(s);
					LoggingMessages.printOut("data: " + s);
					String resp = HttpDatabaseRequest.executeGetRequest(s);
					LoggingMessages.printOut("response");
					
					String imageDownload = YoutubePageParser.getImageUrl(resp);
					String title = YoutubePageParser.getTitle(resp);
					
					LoggingMessages.printOut(imageDownload);
					LoggingMessages.printOut(title);
					ldds.notifyLinkTitleAndImageUrl(new String [] {title, imageDownload});
				}
			}
		} catch (UnsupportedFlavorException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
