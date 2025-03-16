package WidgetComponents;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Label;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.CubicCurve2D;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Graphics2D.CurveShape;
import Properties.LoggingMessages;

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
	private static Dimension CONTROL_POINT_SIZE = new Dimension(5,5);
	private Point [] curvePoints = new Point [4];
	private ArrayList<ArrayList<Point>> 
		listControlPoints = new ArrayList<ArrayList<Point>>(),
		listControlPointsScaled = new ArrayList<ArrayList<Point>>();
	private double sliderLastValue = 50;
	private int 
		scaleFactor = 1,
		directionsIndex = 0, 
		controlPointSelectedIndex = -1,
		controlPointShapeSelectedIndex = -1,
		numShapes = 0;
	private boolean mousePressed = false;
	private Mode mode;
	private ArrayList<Shape> 
		shapes = new ArrayList<Shape>(),
		shapesScaled = new ArrayList<Shape>();
	private JSlider slider;
	private JPanel top, draw;
	
	public ShapeCreator()
	{
		this.setLayout(new BorderLayout());
		top = new JPanel();
		draw = new JPanel();
		
		JLabel directionsLabel = new JLabel();
		
		JButton b = new JButton("draw");
		b.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				drawAll();
			}
		});
		Label l = new Label();
		slider = new JSlider();
		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				l.setText(slider.getValue()+"");
			}
		});
		JButton c = new JButton("clear");
		c.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clearAll();
			}
		});
		l.setText(slider.getValue()+"");
		JButton addCurve = new JButton("Add Cubic Curve");
		addCurve.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mode = Mode.Curve;
				directionsLabel.setText(mode.getDirections()[++directionsIndex]);
				addCurve.setVisible(false);
			}
		});
		
		draw.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) 
			{
				mousePressed = true;
				LoggingMessages.printOut(mousePressed + "");
				if(mode == null && mousePressed)
				{
					Point p = getRelativePoint(e);
					LoggingMessages.printOut(p + "");
					int count = 0, outerCount = 0;
					for(ArrayList<Point> controlPoints : listControlPointsScaled)
					{
						for(Point cp : controlPoints)
						{
							if(p.x >= cp.x && p.x <= cp.x + CONTROL_POINT_SIZE.width)
							{
								if(p.y >= cp.y && p.y <= cp.y + CONTROL_POINT_SIZE.height)
								{
									controlPointSelectedIndex = count;
									controlPointShapeSelectedIndex = outerCount;
									break;
								}
							}
							count++;
						}
						outerCount++;
					}
				}
			}
			
			@Override
			public void mouseReleased(MouseEvent e) 
			{
				super.mouseReleased(e);
				mousePressed = false;
				if(controlPointSelectedIndex != -1)
				{
					Point p = getRelativePoint(e);
					listControlPointsScaled.get(controlPointShapeSelectedIndex).set(controlPointSelectedIndex, p);
					Shape s = shapesScaled.get(controlPointShapeSelectedIndex);
					if(s instanceof CurveShape)
					{
						ArrayList<Point> cps = listControlPointsScaled.get(controlPointShapeSelectedIndex);
						s = new CurveShape();
						((CubicCurve2D) s).setCurve(cps.get(0), cps.get(2), cps.get(3), cps.get(1));
						shapesScaled.set(controlPointShapeSelectedIndex, s);
					}
					drawAll();
				}
				controlPointSelectedIndex = -1;
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if(mode == Mode.Curve)
				{
					Point p = getRelativePoint(e);
					curvePoints[directionsIndex-1] = p;
					addControlPoint(p);
					LoggingMessages.printOut(p + "");
					
					if(directionsIndex + 1 >= mode.getDirections().length)
					{
						LoggingMessages.printOut(LoggingMessages.combine(curvePoints));
						directionsIndex = 0;
						addCurve.setVisible(true);
						CurveShape curveShape = new CurveShape();
						curveShape.setCurve(curvePoints[0], curvePoints[2], curvePoints[3], curvePoints[1]);
						shapes.add(curveShape);
						drawAll();
						mode = null;
						directionsLabel.setText("");
						numShapes++;
					}
					else
					{
						directionsLabel.setText(mode.getDirections()[++directionsIndex]);
					}
				}
			}
		});
		
		top.add(directionsLabel);
		top.add(b);
		top.add(c);
		top.add(slider);
		top.add(l);
		top.add(addCurve);
		this.add(top, BorderLayout.NORTH);
		this.add(draw, BorderLayout.CENTER);
	}
	
	public void setScaleFactor(int scaleFactor)
	{
		this.scaleFactor = scaleFactor;
	}
	
	protected Point getRelativePoint(MouseEvent e)
	{
		Dimension d = top.getSize();
		Point p = e.getPoint();
		p = new Point(p.x, p.y + d.height);
		return p;
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

	protected void drawAll()
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
	
	protected void addControlPoint(Point p)
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
	
	protected void clearAll()
	{
		Graphics2D g2d = (Graphics2D) this.getGraphics();
		super.paint(g2d);
	}
	
	private enum Mode
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
