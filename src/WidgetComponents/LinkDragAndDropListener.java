package WidgetComponents;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.IOException;

import HttpDatabaseRequest.HttpDatabaseRequest;
import ObjectTypeConversion.PageParser;
import ObjectTypeConversion.PageParserCollection;
import ObjectTypeConversion.ParseAttributes;
import Properties.LoggingMessages;
import WidgetComponentInterfaces.LinkDragAndDropSubscriber;

public class LinkDragAndDropListener extends DropTargetAdapter
{
	private LinkDragAndDropSubscriber ldds;
	private PageParserCollection pageParserCollection;
	
	public LinkDragAndDropListener(LinkDragAndDropSubscriber ldds, PageParserCollection pageParserCollection)
	{
		this.ldds = ldds;
		this.pageParserCollection = pageParserCollection;
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
				
				for(PageParser pp : pageParserCollection.getPageParsers())
				{
					if(pp.isParser(dragDropString))
					{
						ParseAttributes pas = new ParseAttributes(pp.getParseAttributes());
						
						dragDropString = HttpDatabaseRequest.addHttpsIfMissing(dragDropString);
						LoggingMessages.printOut("drag and drop value: " + dragDropString);
						String resp = HttpDatabaseRequest.executeGetRequest(dragDropString);
						LoggingMessages.printOut("response");
						
						String imageDownload = pp.getAttributesFromResponse(pas.valueOf("Image"), resp, true)[0];
						String title = pp.getAttributesFromResponse(pas.valueOf("Title"), resp, true)[0];
						
						LoggingMessages.printOut(imageDownload);
						LoggingMessages.printOut(title);
						ldds.notifyLinkTitleAndImageUrl(new String [] {dragDropString, title, imageDownload});
					}
				}
			}
		} catch (UnsupportedFlavorException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
