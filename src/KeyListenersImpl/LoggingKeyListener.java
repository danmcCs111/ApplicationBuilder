package KeyListenersImpl;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import Properties.LoggingMessages;

public class LoggingKeyListener implements KeyListener 
{
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		LoggingMessages.printOut(e.getKeyChar() + " typed.");
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		LoggingMessages.printOut(e.getKeyChar() + " pressed.");
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		LoggingMessages.printOut(e.getKeyChar() + " released.");
	}

}
