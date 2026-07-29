package ApplicationBuilder;

import java.io.File;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.Arrays;

import Properties.LoggingMessages;
import Properties.PathUtility;

public class GeoNamesToSql 
{
	private static String []
		ADMIN_INSERT_DEF = new String [] 
		{
			"AdminName1_GeoLocation_GeoNamesDatabase, AdminCode1_GeoLocation_GeoNamesDatabase, ",
			"AdminName2_GeoLocation_GeoNamesDatabase, AdminCode2_GeoLocation_GeoNamesDatabase, ",
			"AdminName3_GeoLocation_GeoNamesDatabase, AdminCode3_GeoLocation_GeoNamesDatabase, "
		};
	private static String
		INSERT_INTO = "INSERT INTO GeoLocation (",
		INSERT_INTO_DEF_PREFIX = "CountryCode_GeoLocation_GeoNamesDatabase, PostalCode_GeoLocation_GeoNamesDatabase, PlaceName_GeoLocation_GeoNamesDatabase, ",
		INSERT_DEF_SUFFIX = "Latitude_GeoLocation_GeoNamesDatabase, Longitude_GeoLocation_GeoNamesDatabase, Accuracy_GeoLocation_GeoNamesDatabase)",
		INSERT_DEF_SUFFIX_MISS_ACC = "Latitude_GeoLocation_GeoNamesDatabase, Longitude_GeoLocation_GeoNamesDatabase)",
		VALUES_PREFIX = " VALUES (",
		VALUES_SUFFIX = " );",
		SPLIT = "@,@";
	
	private String 
		saveFile = "";
	
	public GeoNamesToSql()
	{
		
	}
	
	public String buildFrontValues(String [] col)
	{
		String countryCode = col[0];
		String postalCode = col[1];
		String placeName = col[2];
		
		return "'" + countryCode + "', '" + postalCode + "', '" + placeName + "', ";
	}
	
	public String buildMiddleValues(String [] col, boolean accuracyIncluded)
	{
		String retStr = "";
		int 
			frontVals = 3,
			endVals = accuracyIncluded ? 3 : 2;
		
		for(int i = frontVals; i < col.length-endVals; i+=2)
		{
			retStr += "'" + col[i] + "', ";
			retStr += "'" + col[i+1] + "', ";
		}
		
		return retStr;
	}
	
	public String buildEndingValues(String [] col)
	{
		String accuracy = col[col.length-1];
		String longitude = col[col.length-2];
		String latitude = col[col.length-3];
		
		return latitude + ", " + longitude + ", " + accuracy;
	}
	
	public String buildEndingValuesMinusAccuracy(String [] col)
	{
		String longitude = col[col.length-1];
		String latitude = col[col.length-2];
		
		return latitude + ", " + longitude;
	}
	
	public String processArgs(String [] args)
	{
		StringBuffer sb = new StringBuffer();
		for(int p = 0; p < args.length; p++)//last is output.
		{
			String path = args[p];
			ArrayList<String> records = PathUtility.readFileToStringArray(new File(path));
			int count = 0;
			for(String rec : records)
			{
				rec = rec.replaceAll("[\t]+", SPLIT);
				String [] col = rec.split(SPLIT);
				String 
				insertValue = VALUES_PREFIX,
				headerInsert = INSERT_INTO;
				
				if(col.length == 5)
				{
					//missing accuracy.
					headerInsert += INSERT_INTO_DEF_PREFIX;
					headerInsert += INSERT_DEF_SUFFIX_MISS_ACC;
					
					insertValue += buildFrontValues(col);
					insertValue += buildEndingValuesMinusAccuracy(col);
				}
				else if(col.length > 6 && col.length % 2 == 1)
				{
					int admins = ((col.length-6) / 2);
					
					headerInsert += INSERT_INTO_DEF_PREFIX;
					for(int i = 0; i < admins; i++)
					{
						headerInsert += ADMIN_INSERT_DEF[i];
					}
					headerInsert += INSERT_DEF_SUFFIX;
					
					insertValue += buildFrontValues(col);
					insertValue += buildMiddleValues(col, false);
					insertValue += buildEndingValuesMinusAccuracy(col);
				}
				else if(col.length == 6)
				{
					//no admin.
					headerInsert += INSERT_INTO_DEF_PREFIX;
					headerInsert += INSERT_DEF_SUFFIX;
					
					insertValue += buildFrontValues(col);
					insertValue += buildEndingValues(col);
				}
				else if(col.length > 6 && col.length % 2 == 0)
				{
					int admins = ((col.length-6) / 2);
					
					headerInsert += INSERT_INTO_DEF_PREFIX;
					for(int i = 0; i < admins; i++)
					{
						headerInsert += ADMIN_INSERT_DEF[i];
					}
					headerInsert += INSERT_DEF_SUFFIX;
					
					insertValue += buildFrontValues(col);
					insertValue += buildMiddleValues(col, true);
					insertValue += buildEndingValues(col);
				}
				else
				{
					LoggingMessages.printOut("fix: " + LoggingMessages.combine(col));
					continue;
				}
				insertValue += VALUES_SUFFIX;
				count++;
				LoggingMessages.printOut(count + "/" + records.size());
				sb.append(headerInsert + insertValue + "\n");
			}
		}
		return sb.toString();
	}
	
	public static void main(String [] args)
	{
		GeoNamesToSql gns = new GeoNamesToSql();
		gns.saveFile = args[args.length-1];
		String [] argP = Arrays.copyOf(args, args.length-1);
		String insertStr = gns.processArgs(argP);
		PathUtility.writeStringToFile(new File(gns.saveFile), insertStr);
	}
}
