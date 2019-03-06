import java.io.*;
import java.util.*;

public class ClearTable
{
	public ClearTable()
	{

	}
	public int cleartb(String dbname,String tbname) throws FileNotFoundException
	{
		File db=new File(dbname);
		if(db.exists())
		{
			File tb=new File(db+File.separator+tbname+".txt");
			if(tb.exists())
			{
				PrintWriter writer = new PrintWriter(tb);
				writer.print("");
				writer.close();
				return 1;
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