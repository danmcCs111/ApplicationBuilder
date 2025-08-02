package ActionListeners;

import ObjectTypeConversion.CsvReader;

public interface CsvReaderSubscriber 
{
	public void notify(CsvReader csvReader);
}
