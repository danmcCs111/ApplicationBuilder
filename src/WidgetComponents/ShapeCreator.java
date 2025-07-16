package WidgetComponents;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Label;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import Graphics2D.CurveShape;
import ShapeEditorListeners.ClearActionListener;
import ShapeEditorListeners.DrawActionListener;
import ShapeEditorListeners.DrawInputActionListener;
import ShapeEditorListeners.DrawMouseListener;
import ShapeEditorListeners.SliderChangeListener;

public class ShapeCreator extends JPanel 
{
	private static final long serialVersionUID = 3005L;
	
	private static final String [] CURVE_DIRECTIONS = new String [] {
			"",
			"Enter x, y", 
			"Enter x2, y2", 
			"Enter Control Point 1", 
			"Enter Control Point 2"
	};
	private static final String [] LINE_DIRECTIONS = new String [] {
			"",
			"Enter x, y", 
			"Enter x2, y2"
	};
	public static Dimension CONTROL_POINT_SIZE = new Dimension(5,5);
	
	private double sliderLastValue = 50;
	private int 
		scaleFactor = 1,
		directionsIndex = 0, 
		controlPointSelectedIndex = -1,
		controlPointShapeSelectedIndex = -1,
		numShapes = 0;
	private boolean mousePressed = false;
	private Point [] curvePoints = new Point [4];
	private ArrayList<ArrayList<Point>> 
		listControlPoints = new ArrayList<ArrayList<Point>>(),
		listControlPointsScaled = new ArrayList<ArrayList<Point>>();
	private ArrayList<Shape> 
		shapes = new ArrayList<Shape>(),
		shapesScaled = new ArrayList<Shape>();
	private Mode mode;
	
	private JSlider slider;
	private Label sliderLabel;
	private JLabel directionsLabel;
	private JButton addCurve;
	private JPanel 
		top, 
		draw;
	
	public ShapeCreator()
	{
		this.setLayout(new BorderLayout());
		
		top = new JPanel();
		draw = new JPanel();
		directionsLabel = new JLabel();
		JButton b = new JButton("draw");
		sliderLabel = new Label();
		slider = new JSlider();
		JButton c = new JButton("clear");
		sliderLabel.setText(slider.getValue()+"");
		addCurve = new JButton("Add Cubic Curve");
		
		c.addActionListener(new ClearActionListener(this));
		addCurve.addActionListener(new DrawInputActionListener(this));
		slider.addChangeListener(new SliderChangeListener(this));
		b.addActionListener(new DrawActionListener(this));
		draw.addMouseListener(new DrawMouseListener(this));
		
		top.add(directionsLabel);
		top.add(b);
		top.add(c);
		top.add(slider);
		top.add(sliderLabel);
		top.add(addCurve);
		this.add(top, BorderLayout.NORTH);
		this.add(draw, BorderLayout.CENTER);
	}
	
	public void setMode(Mode mode)
	{
		this.mode = mode;
	}
	
	public Mode getMode()
	{
		return this.mode;
	}
	
	public JSlider getSlider()
	{
		return this.slider;
	}
	
	public Label getSliderLabel()
	{
		return this.sliderLabel;
	}
	
	public JLabel getDirectionsLabel()
	{
		return this.directionsLabel;
	}
	
	public JButton getAddCurveButton()
	{
		return this.addCurve;
	}
	
	public ArrayList<Shape> getShapes()
	{
		return this.shapes;
	}
	
	public ArrayList<Shape> getShapesScaled()
	{
		return this.shapesScaled;
	}
	
	public void setShapesScaled(ArrayList<Shape> shapesRepl)
	{
		this.shapesScaled = shapesRepl;
	}
	
	public ArrayList<ArrayList<Point>> getControlPoints()
	{
		return this.listControlPoints;
	}
	
	public ArrayList<ArrayList<Point>> getControlPointsScaled()
	{
		return this.listControlPointsScaled;
	}
	
	public void setControlPointsScaled(ArrayList<ArrayList<Point>> listControlPointsScaledRepl)
	{
		this.listControlPointsScaled = listControlPointsScaledRepl;
	}
	
	public int getDirectionsIndex()
	{
		return this.directionsIndex;
	}
	
	public void incrementDirectionsIndex(int inc)
	{
		this.directionsIndex += inc;
	}
	
	public void setDirectionsIndex(int index)
	{
		this.directionsIndex = index;
	}
	
	public int getControlPointSelectedIndex()
	{
		return this.controlPointSelectedIndex;
	}
	public void incrementControlPointSelectedIndex(int inc)
	{
		this.controlPointSelectedIndex += inc;
	}
	public void setControlPointSelectedIndex(int index)
	{
		this.controlPointSelectedIndex = index;
	}
	
	public int getControlPointShapeSelectedIndex()
	{
		return this.controlPointShapeSelectedIndex;
	}
	public void incrementControlPointShapeSelectedIndex(int inc)
	{
		this.controlPointShapeSelectedIndex += inc;
	}
	public void setControlPointShapeSelectedIndex(int index)
	{
		this.controlPointShapeSelectedIndex = index;
	}
	
	public int getNumShapes()
	{
		return this.numShapes;
	}
	public void incrementNumShapes(int inc)
	{
		this.numShapes += inc;
	}
	public void setNumShapes(int index)
	{
		this.numShapes = index;;
	}
	
	public void setScaleFactor(int scaleFactor)
	{
		this.scaleFactor = scaleFactor;
	}
	
	public Point getRelativePoint(MouseEvent e)
	{
		Dimension d = top.getSize();
		Point p = e.getPoint();
		p = new Point(p.x, p.y + d.height);
		return p;
	}
	
	public void setMousePressed(boolean mousePressed)
	{
		this.mousePressed = mousePressed;
	}
	
	public boolean getMousePressed()
	{
		return this.mousePressed;
	}
	
	public Point [] getCurvePoints()
	{
		return this.curvePoints;
	}
	
	protected void scaleSize()
	{
		for(int i = 0; i < numShapes; i++)
		{
			Shape s = shapesScaled.get(i);
			if(s instanceof CurveShape)
			{
				CurveShape cs = ((CurveShape) s);
				
				Point pCtrl1 = new Point(
						(int)scale(cs.getCtrlP1().getX()), 
						(int)scale(cs.getCtrlP1().getY())
				);
				Point pCtrl2 = new Point(
						(int)scale(cs.getCtrlP2().getX()), 
						(int)scale(cs.getCtrlP2().getY())
				);
				Point p1 = new Point(
						(int)scale(cs.getP1().getX()), 
						(int)scale(cs.getP1().getY())
				);
				Point p2 = new Point(
						(int)scale(cs.getP2().getX()), 
						(int)scale(cs.getP2().getY())
				);
				
				cs = new CurveShape();
				cs.setCurve(p1, pCtrl1, pCtrl2, p2);
				shapesScaled.set(i, cs);
			}
			ArrayList<Point> ps = listControlPointsScaled.get(i);
			for(int j = 0; j < ps.size(); j++)
			{
				Point p = ps.get(j);
				Point pRe = new Point((int)scale(p.getX()), (int)scale(p.getY()));
				ps.set(j, pRe);
			}
			listControlPointsScaled.set(i, ps);
		}
	}
	
	protected int scale(int original)
	{
		return (int) scale((double)original);
	}
	
	protected double scale(double original)
	{
		int val = slider.getValue();//0-100
		double resize = (original * ((val / sliderLastValue) * scaleFactor));
		return resize;
	}

	public void drawAll()
	{
		if(sliderLastValue != slider.getValue())
		{
			scaleSize();
		}
		else if(shapesScaled.size() != shapes.size() || listControlPointsScaled.size() != listControlPoints.size())
		{
			createCopyShapesAndControlPoints();
		}
		
		clearAll();
		drawShapes(shapesScaled);
		drawControlPoints(listControlPointsScaled);
		sliderLastValue = slider.getValue();
	}
	
	protected void createCopyShapesAndControlPoints()
	{
		shapesScaled = (ArrayList<Shape>) shapes.stream().collect(Collectors.toList());
		listControlPointsScaled = new ArrayList<ArrayList<Point>>();
		for(ArrayList<Point> controlPoints : listControlPoints)
		{
			listControlPointsScaled.add((ArrayList<Point>) controlPoints.stream().collect(Collectors.toList()));
		}
	}
	
	public void addControlPoint(Point p)
	{
		if(listControlPoints.size() <= numShapes)
		{
			listControlPoints.add(new ArrayList<Point>());
		}
		listControlPoints.get(numShapes).add(p);
		drawControlPoint(p);
	}
	
	protected void drawShape(Shape shape)
	{
		Graphics2D g2d = (Graphics2D) this.getGraphics();
		g2d.setColor(Color.black);
		g2d.draw(shape);
	}
	protected void drawShapes(ArrayList<Shape> shapes)
	{
		for(Shape s : shapes)
		{
			drawShape(s);
		}
	}
	protected void drawControlPoint(Point p)
	{
		Rectangle r = new Rectangle(p);
		r.setSize(CONTROL_POINT_SIZE.width, CONTROL_POINT_SIZE.height);
		Graphics2D g2d = (Graphics2D) this.getGraphics();
		g2d.setColor(Color.black);
		g2d.draw(r);
	}
	protected void drawControlPoints(ArrayList<ArrayList<Point>> listOfControlPoints)
	{
		for(ArrayList<Point> controlPoints : listOfControlPoints)
		{
			for(Point p : controlPoints)
			{
				drawControlPoint(p);
			}
		}
	}
	
	public void clearAll()
	{
		Graphics2D g2d = (Graphics2D) this.getGraphics();
		super.paint(g2d);
	}
	
	public enum Mode
	{
		Line("Line", LINE_DIRECTIONS),
		Curve("Curve", CURVE_DIRECTIONS);
		
		private String modeText;
		private String [] directions;
		
		private Mode(String modeText, String [] directions)
		{
			this.modeText = modeText;
			this.directions = directions;
		}
		
		public String getModeText()
		{
			return this.modeText;
		}
		public String [] getDirections()
		{
			return this.directions;
		}
	}
	
}
