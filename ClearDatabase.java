import java.io.*;
import java.util.*;

public class ClearDatabase
{
	public ClearDatabase()
	{

	}
	public int cleardb(String dbname)
	{
		File db=new File(dbname);
		if(db.exists())
		{
			String[] entries = db.list();
			if(entries.length==0 || entries==null)
			{
				return 3;
			}
			for(String s: entries)
			{
				File tb=new File(db+File.separator+s);
				if(tb.exists())
					tb.delete();
			}
			return 1;
		}
		else
		{
			return 2;
		}
	}
}