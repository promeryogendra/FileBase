import java.io.*;
import java.util.*;
import java.util.regex.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
public class AddData
{
	public AddData()
	{

	}

	public int findLastIndex(String command,char x)
	{
		for(int i=command.length()-1;i>=0;i--)
		{
			if(command.charAt(i)==x)
			{
				return i;
			}
		}
		return -1;
	}
	public int findFirstIndex(String command,char x)
	{
		for(int i=0;i<command.length();i++)
		{
			if(command.charAt(i)==x)
			{
				return i;
			}
		}
		return -1;
	}

	public int adddata(String dbname,String command,String commands[])
	{
		try
		{
			File db=new File(dbname);
			if(db.exists())
			{
				String tbname=commands[1];
				if(tbname.contains("("))
				{
			        tbname= tbname.substring(0, tbname.indexOf("("));
		     	}
		     	String data_String_combine_pair=command.substring(findFirstIndex(command,'('),findLastIndex(command,')')+1);
		     	data_String_combine_pair=data_String_combine_pair.trim();
		     	ArrayList<String[]> data_rows = new ArrayList<String[]>();
		     	int i=0;
		     	boolean status=false;
		     	String temp="";
		     	for(i=0;i<data_String_combine_pair.length();)
		     	{
		     		if(data_String_combine_pair.charAt(i)=='(')
		     		{
		     			i++;
		     			while(i<=data_String_combine_pair.length()-1 && data_String_combine_pair.charAt(i)!=')')
		     			{
		     				temp=temp+data_String_combine_pair.charAt(i++);
		     			}
		     			temp=temp.substring(1,temp.length());
		     			data_rows.add((temp.trim()).split("\\s*,\\s*"));
		     			temp="";
		     		}
		     		else
		     			i++;
		     	}
		     	File schema_file=new File(db+File.separator+"schema");
		     	if(!schema_file.exists())
		     	{
					System.out.println("Read/Write Operation permission denied....");
					return -1;
		     	}
		     	String schema_string="";
		     	Path path = Paths.get(db+File.separator+"schema");
			    List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
			   	for(i=0;i<lines.size();i++)
			   	{
			   		if(lines.get(i).substring(0,lines.get(i).indexOf("(")).equals(tbname))
			   		{
			   			schema_string=lines.get(i).substring(lines.get(i).indexOf("(")+1,lines.get(i).length()-1);
			   			break;
			   		}
			   	}

			   	String schema_check_array[]=(schema_string.split(","));
			   	String schema_check[][]=new String[schema_check_array.length][2];
			   	String schema_data_type[]=new String[schema_check.length];

			   	for(i=0;i<schema_check_array.length;i++)
			   	{
			   		schema_check[i]=schema_check_array[i].split("-");
			   		schema_data_type[i]=schema_check[i][1];
			   	}

			   	boolean insert=true;
		     	for(i=0;i<data_rows.size();i++)
		     		for(int j=0;j<data_rows.get(i).length;j++)
		     			data_rows.get(i)[j]=data_rows.get(i)[j].replaceAll("'","");

		     	Path path_data_file = Paths.get(db+File.separator+tbname+".txt");
			    List<String> data_of_file = Files.readAllLines(path_data_file, StandardCharsets.UTF_8);
		     	for(i=0;i<data_rows.size();i++)
		     	{
		     		String data_string="";
	     			DataCheck dataCheck=new DataCheck();
	     			if(schema_check.length>0 && schema_check.length==data_rows.get(i).length && dataCheck.dataCheck(data_rows.get(i),schema_data_type))
	     			{
	     				data_string=data_rows.get(i)[0];
	     				for(int j=1;j<data_rows.get(i).length;j++)
							data_string=data_string+"'"+data_rows.get(i)[j];
	     				data_of_file.add(data_string);
	     			}
	     			else
	     			{
	     				insert=false;
	     				break;
	     			}
		     	}
		     	if(insert==true)
     			{
     				Files.write(path_data_file, data_of_file, StandardCharsets.UTF_8);
	    		 	return 1;
     			}
	     		else
     			{
     				return 2;
     			}
			}
			else
			{
				return -1;
			}
		}
		catch(Exception e)
		{
			System.out.println("Exception"+e);
			return -1;
		}
	}
}