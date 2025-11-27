package Graphics2D;

import java.awt.Shape;
import java.awt.geom.Path2D;
import java.util.ArrayList;

import DrawModes.GeneralPathDrawMode.DrawPaths;

public class GeneralPathShape extends Path2D.Float
{
	static final int INIT_SIZE = 20;
	private static final long serialVersionUID = 1L;
	
	private ArrayList<DrawPaths> drawPaths = new ArrayList<DrawPaths>();
	
	public ArrayList<DrawPaths> getDrawPaths()
	{
		return this.drawPaths;
	}
	
	public void setDrawPaths(ArrayList<DrawPaths> drawPaths)
	{
		this.drawPaths = drawPaths;
	}
	
	public void clearDrawPaths()
	{
		this.drawPaths = new ArrayList<DrawPaths>();
	}
	
    public GeneralPathShape() {
        super(WIND_NON_ZERO, INIT_SIZE);
    }

    public GeneralPathShape(int rule) {
        super(rule, INIT_SIZE);
    }
    public GeneralPathShape(int rule, int initialCapacity) {
        super(rule, initialCapacity);
    }
    public GeneralPathShape(Shape s) {
        super(s, null);
    }

}
