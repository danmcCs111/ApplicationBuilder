package WidgetComponents;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class WeatherGraphMouseMotionListener implements MouseMotionListener
{
	private List<Integer> xPoints;
	private HashMap<Date, Number> plotPoints;
	private HashMap<Integer, Date> xLocationAndDate = new HashMap<Integer, Date>();
	private JFrame f = new JFrame();
	private JTextArea toolTipLabel = new JTextArea();
	private SimpleDateFormat sdf;
	private JComponent parent;
	private int
		limitXLow,
		limitXHigh,
		limitYLow,
		limitYHigh;
	
	public WeatherGraphMouseMotionListener(
			HashMap<Date, Point> xYPoints,
			HashMap<Date, Number> plotPoints,
			int limitXLow,
			int limitXHigh,
			int limitYLow,
			int limitYHigh,
			SimpleDateFormat sdf,
			JComponent parent
	)
	{
		this.limitXLow = limitXLow;
		this.limitXHigh = limitXHigh;
		this.limitYLow = limitYLow;
		this.limitYHigh = limitYHigh;
		this.parent = parent;
		this.sdf = sdf;
		
		this.xPoints = getXValues(xYPoints);
		Collections.sort(this.xPoints);
		
		this.plotPoints = plotPoints;
		
		toolTipLabel.setEditable(false);
		toolTipLabel.setEnabled(false);
		f.setUndecorated(true);
		f.setResizable(false);
		f.add(toolTipLabel);
		f.setVisible(false);
	}
	
	public List<Integer> getXValues(HashMap<Date, Point> xYPoints)
	{
		List<Integer> sort = new ArrayList<Integer>();
		for(Date d : xYPoints.keySet())
		{
			Point p = xYPoints.get(d);
			sort.add(p.x);
			xLocationAndDate.put(p.x, d);
		}
		return sort;
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {}

	@Override
	public void mouseMoved(MouseEvent e) 
	{
		Point p = e.getPoint();
		int xClosest = -1;
		int xClosestDiff = -1;
		Point toolTipLoc = p;
		
		if(limitXLow > p.x || limitXHigh < p.x || limitYLow > p.y || limitYHigh < p.y)
		{
			f.setVisible(false);
			return;
		}
		
		for(Integer vp : xPoints)
		{
			int tmpDiff = Math.abs(p.x-vp);
			if(xClosestDiff == -1 || tmpDiff < xClosestDiff)
			{
				xClosest = vp;
				xClosestDiff = tmpDiff;
			}
			else
			{
				break;
			}
		}
		toolTipLabel.setText(getToolTipText(xClosest));
		
		f.setLocation(
				parent.getLocation().x + parent.getRootPane().getParent().getX() + xClosest, 
				parent.getLocation().y + parent.getRootPane().getParent().getY() + toolTipLoc.y);
		
		f.pack();
		f.setVisible(true);
		
	}
	
	public String getToolTipText(int xVal)
	{
		String toolTipText = "";
		Date d = xLocationAndDate.get(xVal);
		String dateStr = sdf.format(d);
		toolTipText += dateStr + "\n";
		toolTipText += plotPoints.get(d);
		
		return toolTipText;
	}

}
