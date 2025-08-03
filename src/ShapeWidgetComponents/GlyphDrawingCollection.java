package ShapeWidgetComponents;

import java.awt.Point;
import java.util.HashMap;

public class GlyphDrawingCollection 
{
	private HashMap<Point, String> glyphs = new HashMap<Point, String>();
	private HashMap<Point, ShapeStyling> shapeStylingGlyphs = new HashMap<Point, ShapeStyling>();
	
	public GlyphDrawingCollection()
	{
		
	}
	
	public void addGlyph(Point p, String glyph)
	{
		this.glyphs.put(p, glyph);
	}
	
	public void setGlyphs(HashMap<Point, String> glyphs)
	{
		this.glyphs = glyphs;
	}
	
	public HashMap<Point, String> getGlyphs()
	{
		return this.glyphs;
	}
	
	public void addShapeStylingGlyphs(Point p, ShapeStyling ss)
	{
		this.shapeStylingGlyphs.put(p, ss);
	}
	
	public HashMap<Point, ShapeStyling> getShapeStylingGlyphs()
	{
		return this.shapeStylingGlyphs;
	}
}
