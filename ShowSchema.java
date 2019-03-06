import java.io.*;
import java.util.*;


import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
public class ShowSchema
{
	public ShowSchema()
	{

	}
	public int showSchema(String dbname,String tbname)throws IOException
	{
		File db=new File(dbname);
		if(db.exists())
		{
			File tb=new File(db+File.separator+tbname+".txt");
			if(tb.exists())
			{
				File schema=new File(db+File.separator+"schema");
				if(schema.exists())
				{
					Path path = Paths.get(db+File.separator+"schema");
				    List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
				   	for(int i=0;i<lines.size();i++)
				   	{
				   		if(lines.get(i).substring(0,lines.get(i).indexOf("(")).equals(tbname))
				   		{
				   			System.out.println(lines.get(i));
				   			return 4;
				   		}
				   	}
				   	return 3;
				}
				else
				{
					System.out.println("Schema File is missing in Database....");
					return 2;
				}
			}
			else
			{
				return 1;
			}
		}
		else
		{
			System.out.println("No Database Found.....");
			return -1;
		}
	}
}