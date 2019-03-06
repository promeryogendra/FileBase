import java.io.*;
import java.util.*;

public class UseDatabase
{
	public UseDatabase()
	{

	}
	public int usedb(String dbname)
	{
		File db=new File(dbname);
		if(db.exists())
		{
			return 1;
		}
		else
		{
			return 2;
		}
	}
}