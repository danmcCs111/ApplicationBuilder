package ObjectTypeConversionEditors;

import java.awt.Component;
import java.awt.Dimension;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

import Params.ParameterEditor;
import Properties.LoggingMessages;

public class TimestampEditor extends JSpinner implements ParameterEditor 
{
	private static final long serialVersionUID = 1L;
	
	public TimestampEditor()
	{
		buildWidgets();
	}
	
	private void buildWidgets()
	{
		Calendar cal1 = Calendar.getInstance();
		Date d1 = cal1.getTime();
		SpinnerDateModel model = new SpinnerDateModel(d1, null, null, java.util.Calendar.DAY_OF_MONTH);
		this.setModel(model);
	}
	
	@Override
	public Component getComponentEditor() 
	{
		return this;
	}

	@Override
	public void setComponentValue(Object value) 
	{
		Timestamp timestamp = (Timestamp) value;
		SpinnerDateModel sm = (SpinnerDateModel) this.getModel();
		sm.setValue(new Date(timestamp.getTime()));
	}

	@Override
	public String[] getComponentValue() 
	{
		return new String[] {((Timestamp)getComponentValueObj()).getTime() + ""};
	}

	@Override
	public Object getComponentValueObj() 
	{
		long time = ((SpinnerDateModel)this.getModel()).getDate().getTime();
		return new Timestamp(time);
	}
	
	@Override
	public String getParameterDefintionString() 
	{
		return Timestamp.class.getName();
	}

	@Override
	public String getComponentXMLOutput() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}
	 
	public static void main(String [] args)
	{
		JFrame f = new JFrame();
		TimestampEditor tse = new TimestampEditor();
		tse.setComponentValue(new Timestamp(1767732941000L));//Jan 6
		f.add(tse);
		f.setMinimumSize(new Dimension(200,100));
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		LoggingMessages.printOut(tse.getComponentValue()[0]);
		LoggingMessages.printOut(tse.getComponentValueObj().toString());
		f.setVisible(true);
	}
	
}
