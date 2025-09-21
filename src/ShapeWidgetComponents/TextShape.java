package ShapeWidgetComponents;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import Properties.LoggingMessages;

public class TextShape implements Shape
{
	private int characterSpacing = 15;
	private ArrayList<Shape> glyphShapes = new ArrayList<Shape>();
	private PathIteratorAccumilated piAcc;
	
	public TextShape(String text, Point startPoint, Font font, Graphics2D g2d)
	{
		ArrayList<PathIterator> pis = new ArrayList<PathIterator>();
		FontRenderContext frc = g2d.getFontRenderContext();
		GlyphVector gv = font.createGlyphVector(frc, text);
		Point tmpPoint = new Point(startPoint);
		for(int i = 0; i < text.getBytes().length; i++)
		{
			int xShift = (int)(startPoint.getX() + (i * characterSpacing));
			tmpPoint.x = xShift;
			gv.setGlyphPosition(i, tmpPoint);
			GeneralPath gp = (GeneralPath) gv.getGlyphOutline(i);
			Shape shape = gp;
			LoggingMessages.printOut(shape.getClass()+" ");//TODO
			glyphShapes.add(shape);
			pis.add(shape.getPathIterator(g2d.getTransform()));
		}
		piAcc = new PathIteratorAccumilated(pis);
	}
	
	public ArrayList<Shape> getGlyphShapes()
	{
		return glyphShapes;
	}
	
	public static void main(String [] args)
	{
		JFrame f = new JFrame();
		f.setMinimumSize(new Dimension(500,500));
		f.setDefaultCloseOperation(3);
		f.setVisible(true);
		f.setEnabled(true);
		JPanel panel = new JPanel();
		f.add(panel);
		
		Font font = new Font(Font.SANS_SERIF, 0, 15);
		
		TextShape ts = new TextShape("123 help", new Point(15,15), font, (Graphics2D)f.getGraphics());
		ShapeDrawingCollectionGraphics.drawShape(panel, ts, Color.blue);
	}
	
	@Override
	public Rectangle getBounds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Rectangle2D getBounds2D() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean contains(double x, double y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean contains(Point2D p) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean intersects(double x, double y, double w, double h) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean intersects(Rectangle2D r) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean contains(double x, double y, double w, double h) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean contains(Rectangle2D r) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public PathIterator getPathIterator(AffineTransform at) {
		return piAcc;
	}

	@Override
	public PathIterator getPathIterator(AffineTransform at, double flatness) {
		return piAcc;
	}
	
}
