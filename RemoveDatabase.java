import java.io.File;
public class RemoveDatabase
{
	public int removedb(String dbname)
	{
		File file=new File(dbname);
		if(file.exists())
		{
			String[] entries = file.list();
			for(String s: entries){
			    File currentFile = new File(file.getPath(),s);
			    currentFile.delete();
				}
			file.delete();
			return 1;
		}
		else
		{
			return -1;
		}
	}
} 