package ShapeWidgetComponents;

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


public class TextShape implements Shape
{
	private double characterSpacingPercent = .25;
	private ArrayList<Shape> glyphShapes = new ArrayList<Shape>();
	private PathIteratorAccumilated piAcc;
	private String text;
	private Font font;
	private Graphics2D g2d;
	
	public TextShape(String text, Point startPoint, Font font, Graphics2D g2d)
	{
		this.text = text;
		this.font = font;
		this.g2d = g2d;
		int xAdjSpacing = (int)((double)font.getSize() * characterSpacingPercent);
		ArrayList<PathIterator> pis = new ArrayList<PathIterator>();
		FontRenderContext frc = g2d.getFontRenderContext();
		GlyphVector gv = font.createGlyphVector(frc, text);
		Point tmpPoint = new Point(startPoint);
		for(int i = 0; i < text.toCharArray().length; i++)
		{
			if(i > 0)
			{
				int xAdj;
				if(text.toCharArray()[i-1] == ' ')
				{
					xAdj = tmpPoint.x + xAdjSpacing * 2;
				}
				else
				{
					Rectangle bounds = glyphShapes.get(glyphShapes.size()-1).getBounds();
					xAdj = bounds.x + bounds.width + xAdjSpacing;
				}
				
				tmpPoint.x = xAdj;
			}
			gv.setGlyphPosition(i, tmpPoint);
			GeneralPath gp = (GeneralPath) gv.getGlyphOutline(i);
			Shape shape = gp;
			glyphShapes.add(shape);
			pis.add(shape.getPathIterator(g2d.getTransform()));
		}
		piAcc = new PathIteratorAccumilated(pis);
	}
	
	public void setCharacterSpacingPercent(double characterSpacingPercent)
	{
		this.characterSpacingPercent = characterSpacingPercent;
	}
	
	public String getText()
	{
		return this.text;
	}
	
	public Font getFont()
	{
		return this.font;
	}
	
	public Graphics2D getGraphics()
	{
		return this.g2d;
	}
	
	public ArrayList<Shape> getGlyphShapes()
	{
		return glyphShapes;
	}
	
	@Override
	public Rectangle getBounds() 
	{
		int startx = glyphShapes.get(0).getBounds().x;
		int starty = glyphShapes.get(0).getBounds().y;
		Rectangle last = glyphShapes.get(glyphShapes.size()-1).getBounds();
		int endx = (last.x + last.width) - startx;
		int endy = (last.y + last.height)- starty;
		return new Rectangle(startx, starty, endx, endy);
	}

	@Override
	public Rectangle2D getBounds2D() 
	{
		Rectangle bound = getBounds();
		return new Rectangle2D.Double(bound.getX(), bound.getY(), bound.getWidth(), bound.getHeight());
	}

	@Override
	public boolean contains(double x, double y) 
	{
		return getBounds().contains(x, y);
	}

	@Override
	public boolean contains(Point2D p) 
	{
		return getBounds().contains(p);
	}

	@Override
	public boolean intersects(double x, double y, double w, double h) 
	{
		return getBounds2D().intersects(x, y, w, h);
	}

	@Override
	public boolean intersects(Rectangle2D r) 
	{
		return getBounds2D().intersects(r);
	}

	@Override
	public boolean contains(double x, double y, double w, double h) 
	{
		return getBounds2D().contains(x, y, w, h);
	}

	@Override
	public boolean contains(Rectangle2D r) 
	{
		return getBounds2D().contains(r);
	}

	@Override
	public PathIterator getPathIterator(AffineTransform at) 
	{
		ArrayList<PathIterator> pis = new ArrayList<PathIterator>();
		for(Shape shape : getGlyphShapes())
		{
			pis.add(shape.getPathIterator(at));
		}
		piAcc = new PathIteratorAccumilated(pis);
		return piAcc;
	}

	@Override
	public PathIterator getPathIterator(AffineTransform at, double flatness) 
	{
		ArrayList<PathIterator> pis = new ArrayList<PathIterator>();
		for(Shape shape : getGlyphShapes())
		{
			pis.add(shape.getPathIterator(at));
		}
		piAcc = new PathIteratorAccumilated(pis);
		return piAcc;
	}
	
}
