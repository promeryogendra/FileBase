import java.io.File;
public class NewDatabase
{
	public int createdb(String dbname)
	{
		File file=new File(dbname);
		if(file.exists())
		{
			return -1;
		}
		else
		{
			if(file.mkdir())
			{
				return 1;
			}
		}
		return 0;
	}
} 