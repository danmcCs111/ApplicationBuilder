package ObjectTypeConversionEditors;

public class IntegerEditor extends IntEditor
{

	private static final long serialVersionUID = 1L;
	
	@Override
	public String getParameterDefintionString() 
	{
		return Integer.class.getName();
	}
}
