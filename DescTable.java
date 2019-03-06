import java.io.*;
import java.util.*;


import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
public class DescTable
{
	public DescTable()
	{

	}
	public int descTable(String dbname,String tbname)throws IOException
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
				   			String schema_string=lines.get(i).substring(lines.get(i).indexOf("(")+1,lines.get(i).indexOf(")"));
				   			System.out.println("Description about the Table "+tbname);
				   			System.out.println("*______________________*");
				   			String schema_pair1[]=schema_string.split(",");
				   			String schema_pair[][]=new String[schema_pair1.length][2];
				   			for(int j=0;j<schema_pair1.length;j++)
				   				schema_pair[j]=schema_pair1[j].split("-");
				   			for (int j=0;j<schema_pair.length ;j++ )
				   				System.out.println(schema_pair[j][0]+" - "+schema_pair[j][1]);	
				   			System.out.println("*______________________*");
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