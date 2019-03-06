import java.io.*;
import java.util.*;
import java.util.regex.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
public class ShowAll
{
	public ShowAll()
	{

	}
	public static int index(String names[] ,String tbname)
	{
		if(names.length==0)
			return -1;
		for(int i=0;i<names.length;i++)
			if(names[i].equals(tbname))
				return i;
		return -1;
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
	public static String get_table_string(String command)
	{
		int index=-1,index1=-1;
		Pattern pattern = Pattern.compile("\\s*from\\s*\\(");
	    Matcher matcher = pattern.matcher(command);
	   	if(matcher.find())
	   	{
	   		index1=matcher.start();
	    	index=matcher.end();
	    }
		if(index!=-1 && index1!=-1)
		{
			return command.substring(index,command.length()-1);
		}
		return null;
	}
	public static String[][] get_tables_names_as_operaiton_names(String command)
	{
		String splitted_string[]=command.split("\\s*,\\s*");
		String tables_names_as_operaiton_names[][]=new String[splitted_string.length][2];
		for(int i=0;i<splitted_string.length;i++)
		{
			splitted_string[i]=splitted_string[i].trim();
			if(splitted_string[i].contains("("))
			{
				String temp[]=splitted_string[i].split("\\(");
				temp[1]=temp[1].replace(")","");
				tables_names_as_operaiton_names[i]=temp;
			}
			else
			{
				tables_names_as_operaiton_names[i][0]=splitted_string[i];
				tables_names_as_operaiton_names[i][1]="";
			}
		}
		return tables_names_as_operaiton_names;
	}

	public static int get_dulplicate_present_or_not(List<String> list)
	{
		for(String temp:list)
		{
			if(!temp.equals("")&&Collections.frequency(list,temp)>2)
				return list.indexOf(temp);
		}
		return -1;
	}

	public int showAll_withoutif(String dbname,String command,String commands[])
	{
		try
		{
			File db=new File(dbname);
			if(db.exists())
			{
		     	String table_String_combine_pair="";
		     	
		     	String pp=get_table_string(command);
		     	String tables_names_as_operaiton_names[][ ]=get_tables_names_as_operaiton_names(pp);

		     	for(int i=0;i<tables_names_as_operaiton_names.length-1;i++)
		     		table_String_combine_pair=table_String_combine_pair+tables_names_as_operaiton_names[i][0]+",";
		     	table_String_combine_pair=table_String_combine_pair+tables_names_as_operaiton_names[tables_names_as_operaiton_names.length-1][0];

		     	{
		     		List<String> check_table_list=new ArrayList<String>();
		     		List<String> check_alias_table_list=new ArrayList<String>();
		     		for(int i=0;i<tables_names_as_operaiton_names.length;i++)
		     		{
		     			check_table_list.add(tables_names_as_operaiton_names[i][0]);
		     			check_alias_table_list.add(tables_names_as_operaiton_names[i][1]);
		     		}
		     		List<String> temp_list=new ArrayList<String>(check_table_list);
		     		temp_list.retainAll(check_alias_table_list);
		     		if(temp_list.size()!=0)
	     			{
	     				System.out.println("Variable \""+temp_list.get(0)+"\" is used in Table name and also Alias variable...");
	     				return -1;
	     			}
	     			int duplicate_alias=get_dulplicate_present_or_not(check_alias_table_list);
	     			if(duplicate_alias!=-1)
	     			{
	     				System.out.println("Two Tables using the "+check_alias_table_list.get(duplicate_alias)+" Variable for Alias operation...");
	     				return -1;
	     			}
		     	}
		     	File schema_file=new File(db+File.separator+"schema");
		     	if(!schema_file.exists())
		     	{
					System.out.println("Read/Write Operation permission denied....");
					return -1;
		     	}
		     	table_String_combine_pair=table_String_combine_pair.trim();
		     	String tables[]=table_String_combine_pair.split("\\s*,\\s*");
		     	for(int i=0;i<tables.length;i++)
		     	{
		     		File tb_check=new File(dbname+File.separator+tables[i]+".txt");
		     		if(!tb_check.exists())
		     		{
		     			System.out.println("\""+tables[i]+"\" Table Not Found...");
		     			return -1;
		     		}
		     	}

		     	List<String> table_list= Arrays.asList(tables);

		     	String schema_strings[]=new String[tables.length];

		     	Path path = Paths.get(db+File.separator+"schema");
			    {
			    	List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
			    	String schema_names[]=new String[lines.size()];
			    	String temp_scema[]=new String[lines.size()];
			    	for(int i=0;i<lines.size();i++)
			    	{
			    		schema_names[i]=lines.get(i).substring(0,lines.get(i).indexOf("("));
			    		temp_scema[i]=lines.get(i).substring(lines.get(i).indexOf("(")+1,lines.get(i).length()-1);
			    	}
				   	for(int i=0,j=0;i<tables.length;i++)
				   	{
				   		int index=index(schema_names,tables[i]);
				   		if(index>=0)
				   		{
				   			schema_strings[j++]=temp_scema[index];
				   		}
				   	}
			   	}
			   	String schema_string_devided[][][]=new String[schema_strings.length][][];
			   	for(int i=0;i<schema_strings.length;i++)
			   	{
			   		String temp[]=schema_strings[i].split(",");
			   		schema_string_devided[i]=new String[temp.length][2];
			   		for(int j=0;j<temp.length;j++)
			   		{
			   			schema_string_devided[i][j]=temp[j].split("-");
			   		}
			   	}
			   	List<String> tables_variable_list=new ArrayList<String>();
			   	List<String> duplicate_table_variables=new ArrayList<String>();
			   	int tables_variable_count[]=new int[tables.length];
			   	for(int i=0,k=0;i<schema_string_devided.length;i++)
			   	{
			   		tables_variable_count[i]=schema_string_devided[i].length;
			   		for (int j=0;j<schema_string_devided[i].length;j++)
			   		{
			   			tables_variable_list.add(tables[i]+"."+schema_string_devided[i][j][0]);
			   			if(tables_names_as_operaiton_names[i][1].equals(""))
			   				duplicate_table_variables.add(tables[i]+"."+schema_string_devided[i][j][0]);
			   			else
			   				duplicate_table_variables.add(tables_names_as_operaiton_names[i][1]+"."+schema_string_devided[i][j][0]);
			   		}
			   	}

			   	List<List> data_list=new ArrayList<List>();
			   	for(int i=0;i<tables.length;i++)
			   	{
			   		Path path1= Paths.get(dbname+File.separator+tables[i]+".txt");
			    	List<String> data_lines = Files.readAllLines(path1, StandardCharsets.UTF_8);
			    	List<String[]> data=new ArrayList<String[]>();
				   	for(int j=0;j<data_lines.size();j++)
				   	{
				   		data.add(data_lines.get(j).split("'"));
				   	}
				   	if(data.size()==0)
				   	{
				   		System.out.println("Empty Set ...");
			   			return -1;
				   	}
				   	data_list.add(data);
			   	}
			   	String real_data[][]=null;
			   	int real_data_field_count=0;
			   	for(int i=0;i<tables.length;i++)
			   	{
			   		if(real_data==null)
			   		{
			   			List<String[]> temp=data_list.get(i);
			   			if(temp.size()==0)
			   			{
			   				System.out.println("Empty Set ...");
			   				return -1;
			   			}
			   			String temp_data[][]=new String[data_list.get(i).size()][tables_variable_count[i]];
			   			real_data_field_count=tables_variable_count[i];
			   			for(int j=0;j<temp.size();j++)
			   			{
			   				temp_data[j]=temp.get(j);
			   			}
			   			real_data=temp_data;
			   		}
			   		else
			   		{
			   			List<String[]> temp=data_list.get(i);
			   			if(temp.size()==0)
			   			{
			   				System.out.println("Empty Set ...");
			   				return -1;
			   			}
			   			String temp_data[][]=new String[real_data.length*tables_variable_count[i]][real_data_field_count+tables_variable_count[i]];
			   			int real_data_field_count_temp=real_data_field_count+tables_variable_count[i];
			   			int row_ind=0;
			   			for(int j=0;j<real_data.length;j++)
			   			{
			   				for(int k=0;k<temp.size();k++)
			   				{
			   					int real_col_ind=real_data_field_count,m=0;
			   					for(m=0;m<real_col_ind;m++)
			   					{
			   						temp_data[row_ind][m]=real_data[j][m];
			   					}
			   					String[] temp1_data=temp.get(k);
			   					for(int f=0;f<temp1_data.length;f++,m++)
			   					{
			   						temp_data[row_ind][m]=temp1_data[f];
			   					}
			   					row_ind++;
			   				}
			   			}
			   			real_data_field_count=real_data_field_count_temp;
			   			real_data=temp_data;
			   		}
			   	}
			   	System.out.println("\n"+"No of Rows :- "+real_data_field_count+"...");
			   	if(real_data_field_count==0)
			   		return -1;
			   	System.out.println("*____________________*");
			   	for(int i=0;i<tables_variable_list.size();i++)
			   	{
			   		System.out.printf("%-15s",duplicate_table_variables.get(i));
			   	}
			   	System.out.println("\n\n");
			   	for(int i=0;i<real_data.length;i++)
			   	{
			   		for(int j=0;j<real_data_field_count;j++)
			   			System.out.printf("%-15s",real_data[i][j]);
			   		System.out.println("\n");
			   	}
			   	System.out.println("____________________");
     			return -1;
			}
			else
			{
				System.out.println("Database not found...");
				return -1;
			}
		}
		catch(Exception e)
		{
			System.out.println("Exception"+e);
			return -1;
		}
	}
	public int showallwithif(String dbname,String command,String commands[])
	{
		return 0;
	}
}