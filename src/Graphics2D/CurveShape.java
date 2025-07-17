package Graphics2D;

import java.awt.Point;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Point2D;

public class CurveShape extends CubicCurve2D
{
	public CurveShape(Point p1, Point c1, Point c2, Point p2)
	{
		this.setCurve(p1, c1, c2, p2);
	}
	private double
		x1,y1,
		x2,y2,
		cx1,cy1,
		cx2,cy2;
	
	@Override
	public double getX1() 
	{
		return x1;
	}

	@Override
	public double getY1() 
	{
		return y1;
	}

	@Override
	public Point2D getP1() 
	{
		return new Point((int) x1, (int) y1);
	}

	@Override
	public double getCtrlX1() 
	{
		return cx1;
	}

	@Override
	public double getCtrlY1() 
	{
		return cy1;
	}

	@Override
	public Point2D getCtrlP1() 
	{
		return new Point((int) cx1, (int) cy1);
	}

	@Override
	public double getCtrlX2() 
	{
		return cx2;
	}

	@Override
	public double getCtrlY2() 
	{
		return cy2;
	}

	@Override
	public Point2D getCtrlP2() 
	{
		return new Point((int) cx2, (int) cy2);
	}

	@Override
	public double getX2() 
	{
		return x2;
	}

	@Override
	public double getY2() 
	{
		return y2;
	}

	@Override
	public Point2D getP2() 
	{
		return new Point((int) x2, (int) y2);
	}

	@Override
	public void setCurve(double x1, double y1, double ctrlx1, double ctrly1, double ctrlx2, double ctrly2, double x2,
			double y2) 
	{
		this.x1 = x1;
		this.y1 = y1;
		this.cx1 = ctrlx1;
		this.cy1 = ctrly1;
		this.cx2 = ctrlx2;
		this.cy2 = ctrly2;
		this.x2 = x2;
		this.y2 = y2;
		
	}
}
