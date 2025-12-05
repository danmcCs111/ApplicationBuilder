package WidgetComponents;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.IOException;

import Properties.LoggingMessages;

public class DragAndDropListener extends DropTargetAdapter
{
	@Override
	public void drop(DropTargetDropEvent dtde) 
	{
		dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
		Transferable t = dtde.getTransferable();
		try {
			String s = (String) t.getTransferData(DataFlavor.stringFlavor);
			LoggingMessages.printOut("data: " + s);
		} catch (UnsupportedFlavorException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
