import java.io.*;
import java.util.*;
import java.util.regex.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class NewTable
{
	public NewTable()
	{

	}
	public int newtb(String dbname,String command,String tbname) throws IOException
	{
		boolean filestatus=false;
		if(tbname.contains("("))
		{
	        tbname= tbname.substring(0, tbname.indexOf("("));
     	}
		File db=new File(dbname);
		if(db.exists())
		{
			File file=new File(db+File.separator+tbname+".txt");
			if(file.exists())
			{
				System.out.println("Already table exists....");
				return 0;
			}
			else
			{
				String only_variable_commad=command.trim();
				only_variable_commad=only_variable_commad.substring(only_variable_commad.indexOf("(") + 1, only_variable_commad.indexOf(")"));
				only_variable_commad=only_variable_commad.trim();
				String variable_combine_pair[]=only_variable_commad.split(",");
				String variable_pair[][]=new String[variable_combine_pair.length][2];
				for(int i=0;i<variable_combine_pair.length;i++)
				{
					variable_combine_pair[i]=variable_combine_pair[i].trim();
					variable_pair[i]=variable_combine_pair[i].split("\\s+");
					String[] data_types = new String[]{"int", "string", "real"};
        			List<String> datatypes = Arrays.asList(data_types);
					if(variable_pair[i][0].equals("int") || variable_pair[i][0].equals("string") || variable_pair[i][0].equals("real"))
						return 6;
					if(!datatypes.contains(variable_pair[i][1]))
						return 7;
				}				
				File schema=new File(db+File.separator+"schema");
				try
				{
					schema.createNewFile();
				}
				catch(Exception e)
				{
					System.out.println("Read/Write Operation permission denied....");
					return -1;
				}
				if(schema.exists())
				{
					Path path = Paths.get(db+File.separator+"schema");
				    List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
				   	for(int i=0;i<lines.size();i++)
				   	{
				   		if(lines.get(i).substring(0,lines.get(i).indexOf("(")).equals(tbname))
				   		{
				   			lines.remove(i);
				   			filestatus=true;
				   		}
				   	}
				    Files.write(path, lines, StandardCharsets.UTF_8);
				    String schema_string=tbname+"(";
				    for(int i=0;i<variable_pair.length;i++)
				    {
				    	if(i!=variable_pair.length-1)
				    		schema_string=schema_string+variable_pair[i][0]+"-"+variable_pair[i][1]+",";
				    	else
				    		schema_string=schema_string+variable_pair[i][0]+"-"+variable_pair[i][1]+")";
				    }
				    lines.add(schema_string);
				    Files.write(path, lines, StandardCharsets.UTF_8);
				    File tb=new File(db+File.separator+tbname+".txt");
				    if(tb.exists())
				    {

				    }
				    else
				    {
				    	tb.createNewFile();
				    }
					return 1;
				}
			}
		}
		return -1;
	}
}