package WidgetComponents;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import HttpDatabaseRequest.HttpDatabaseRequest;
import HttpDatabaseRequest.PageParser;
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
				String dragDropString = (String) t.getTransferData(DataFlavor.stringFlavor);
				
				//TODO
				PageParser youtube = new PageParser();
				youtube.isParser(dragDropString);
				youtube.setDomainMatch("youtube.com");
				youtube.addImageMatchAndReplace("https://yt3.googleusercontent.com([^\"])*(\")", 
						new ArrayList<String>(Arrays.asList(new String [] {"\""}))
						);
				youtube.addTitleMatchAndReplace("<title>([^<])*</title>", 
						new ArrayList<String>(Arrays.asList(new String [] {"<title>","</title>"}))
						);
				
				if(youtube.isParser(dragDropString))
				{
					
					dragDropString = HttpDatabaseRequest.addHttpsIfMissing(dragDropString);
					LoggingMessages.printOut("drag and drop value: " + dragDropString);
					String resp = HttpDatabaseRequest.executeGetRequest(dragDropString);
					LoggingMessages.printOut("response");
					
					String imageDownload = youtube.getImageUrl(resp);
					String title = youtube.getTitle(resp);
					
					LoggingMessages.printOut(imageDownload);
					LoggingMessages.printOut(title);
					ldds.notifyLinkTitleAndImageUrl(new String [] {dragDropString, title, imageDownload});
				}
			}
		} catch (UnsupportedFlavorException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
