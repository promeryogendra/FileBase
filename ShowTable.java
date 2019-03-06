import java.io.*;
import java.util.*;

public class ShowTable
{
	public ShowTable()
	{

	}
	public ArrayList<String> getTables(String dbname)
	{
		ArrayList<String> tables = new ArrayList<String>();
		int i=0;
		File db=new File(dbname);
		if(db.exists())
		{
			String[] entries = db.list();
			for(String s: entries)
			{
				if(s.contains(".txt"))
				{
					int last=s.lastIndexOf(".txt");  
					s=s.substring(0,last);
					tables.add(s);
				}
			}
			return tables;
		}
		else
		{
			System.out.println("No Database Found.....");
			return null;
		}
	}
}