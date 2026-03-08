package ApplicationBuilder;

import java.io.File;
import java.util.ArrayList;

import HttpDatabaseResponse.DatabaseResponseNode;
import HttpDatabaseResponse.HttpDatabaseResponse;
import ObjectTypeConversion.FileSelection;
import Properties.LoggingMessages;
import Properties.PathUtility;

public class UpdateDuration 
{
	private static final String 
		KEY_PATH = "./Properties/api-keys/youtube-api-key.txt",
		YOUTUBE_API_OPERATION = "collectDuration",
		YOUTUBE_SQL_TYPE = "SQLite",
		FILEPATH = new FileSelection("./tmp.sql").getFullPath(),
		PLUGIN_JAR_LOCATION = PathUtility.getCurrentDirectory() + "/" + 
			"plugin-projects/YouTube-API-list/YoutubeApiList/youtubeApiList.sh",
		GET_PARENT_IDS = "SELECT VideoId_Video_VideoDatabase FROM video;",
		GET_VIDEO_IDS = "SELECT Id_VideoYoutube_VideoYoutubeDatabase FROM videoYoutube " +
				"WHERE ParentID_VideoYoutube_VideoYoutubeDatabase=<arg0> " +
				"AND Duration_VideoYoutube_VideoYoutubeDatabase is null;";
	
	private static String 
		key;
	
	private static String getKey()
	{
		if(key != null)
			return key;
		
		FileSelection fs = new FileSelection(KEY_PATH);
		key = PathUtility.readFileToString(new File(fs.getFullPath())).replace("\n", "").replace(" ", "");
		
		return key;
	}
	
	private static String [] buildUpdateFile(int parentId, ArrayList<String> videoIds)
	{
		String varArg = "";
		for(String vid : videoIds)
		{
			varArg += vid + " ";
		}
		String [] argsYou = new String [] {
				PLUGIN_JAR_LOCATION + " " +
				YOUTUBE_API_OPERATION + " " + 
				YOUTUBE_SQL_TYPE + " " + 
				getKey() + " " +
				parentId + " " + 
				"@zzz" + " " +
				-1 + " " + 
				FILEPATH + " " +
				varArg
		};
		
		return argsYou;
	}
	
	public static void main(String [] args)
	{
		String sql = GET_PARENT_IDS;
		String response = QueryUpdateTool.executeQuery(sql);
		HttpDatabaseResponse hdr = new HttpDatabaseResponse();
		ArrayList <ArrayList <DatabaseResponseNode>> drnsList = hdr.parseResponse(response);
		for(ArrayList<DatabaseResponseNode> drns : drnsList)
		{
			for(DatabaseResponseNode dn : drns)
			{
				String parentId = dn.getNodeAttributes().get("content");
				LoggingMessages.printOut(parentId);
				
				sql = GET_VIDEO_IDS;
				sql = sql.replace("<arg0>", parentId);
				response = QueryUpdateTool.executeQuery(sql);
				HttpDatabaseResponse hdr2 = new HttpDatabaseResponse();
				ArrayList <ArrayList <DatabaseResponseNode>> drnsList2 = hdr2.parseResponse(response);
				ArrayList<String> videoIds = new ArrayList<String>();
				for(ArrayList<DatabaseResponseNode> drns2 : drnsList2)
				{
					for(DatabaseResponseNode dn2 : drns2)
					{
						String videoId = dn2.getNodeAttributes().get("content");
						videoIds.add(videoId);
					}
				}
				String [] argsYou = buildUpdateFile(Integer.parseInt(parentId), videoIds);
				//run plugin.
				ShellHeadlessExecutor.run(argsYou, true);
				
				//run insert & image grab jobs. use loading screen.
				String contents = PathUtility.readFileToString(new File(FILEPATH));
				if(contents != null)
				{
					LoggingMessages.printOut(contents);
					QueryUpdateTool.executeUpdate(contents);
				}
			}
		}
	}
}
