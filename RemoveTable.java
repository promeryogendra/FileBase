import java.io.*;
import java.util.*;

public class RemoveTable
{
	public RemoveTable()
	{

	}
	public int removetb(String dbname,String tbname)
	{
		File db=new File(dbname);
		if(db.exists())
		{
			File tb=new File(db+File.separator+tbname+".txt");
			if(tb.exists())
			{
				if(tb.delete())
				{
					return 1;
				}
				else
				{
					return 2;
				}
			}
			else
			{
				return 3;
			}
		}
		else
		{
			return 4;
		}
	}
}