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
import java.util.ArrayList;

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
	private static Dimension CONTROL_POINT_SIZE = new Dimension(5,5);
	public Point [] curvePoints = new Point [4];
	ArrayList<Point> controlPoints = new ArrayList<Point>();
	
	private Mode mode;
	private int directionsIndex = 0; 
	
	private ArrayList<Shape> shapes = new ArrayList<Shape>();
	private JSlider slider;
	private JPanel top, draw;
	private int 
		scaleFactor = 1,
		width = 100,
		height = 100;
	
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
				clearOdometer();
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
			public void mouseClicked(MouseEvent e) {
				if(mode == Mode.Curve)
				{
					Dimension d = top.getSize();
					Point p = e.getPoint();
					p = new Point(p.x, p.y + d.height);
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
	
	public void setWidth(int width)
	{
		this.width = width;
	}
	
	public void setHeight(int height)
	{
		this.height = height;
	}
	
	public void setScaleFactor(int scaleFactor)
	{
		this.scaleFactor = scaleFactor;
	}
	
	protected void stretchSize()
	{
		int val = slider.getValue();//0-100
		int resizeWidth = (int) (width * ((val / 50.0) * scaleFactor));
		int resizeHeight = (int) (height * ((val / 50.0) * scaleFactor));
		//TODO
	}

	protected void drawAll()
	{
		stretchSize();
		clearOdometer();
		Graphics2D g2d = (Graphics2D) this.getGraphics();
		g2d.setColor(Color.black);
		for(Shape s : shapes)
		{
			if(s != null)
			{
				g2d.draw(s);
			}
		}
		drawControlPoints(controlPoints);
	}
	
	protected void addControlPoint(Point p)
	{
		controlPoints.add(p);
		drawControlPoint(p);
	}
	
	protected void drawControlPoint(Point p)
	{
		Rectangle r = new Rectangle(p);
		r.setSize(CONTROL_POINT_SIZE.width, CONTROL_POINT_SIZE.height);
		Graphics2D g2d = (Graphics2D) this.getGraphics();
		g2d.setColor(Color.black);
		g2d.draw(r);
	}
	protected void drawControlPoints(ArrayList<Point> controlPoints)
	{
		for(Point p : controlPoints)
		{
			drawControlPoint(p);
		}
	}
	
	protected void clearOdometer()
	{
		Graphics2D g2d = (Graphics2D) this.getGraphics();
		super.paint(g2d);
	}
	
	private enum Mode
	{
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
