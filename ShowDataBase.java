import java.io.*;
import java.util.*;
public class ShowDataBase
{
	public ShowDataBase()
	{

	}
	public ArrayList<String> getDatabases()
	{
		ArrayList<String> databases = new ArrayList<String>();
		int i=0;
		File file=new File(System.getProperty("user.dir"));
		if(file.exists() &&  file.isDirectory())
		{
			String listOfFiles[]=file.list();
			for(String s: listOfFiles){
			    File currentFile = new File(file.getPath(),s);
			    if (currentFile.isDirectory())
			    {
			    	databases.add(s);
			    }
			}
			return databases;
		}
		else
		{
			return null;
		}
	}
}