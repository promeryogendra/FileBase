import java.io.*;
import java.util.*;
import java.util.regex.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
public class ShowSome
{
	public static String wanted_temp[][][]=null;
	public static String variable_real_names[]=null;
	public static String wanted_data[]=null;
	public ShowSome()
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
	public static int findFirstIndex(String command,char x)
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
	public static String[] get_table_string(String command)
	{
		int index=-1,index1=-1,index2=-1,index3=-1;
		Pattern pattern = Pattern.compile("\\)\\s*from\\s*\\(");
	    Matcher matcher = pattern.matcher(command);
	   	if(matcher.find())
	   	{
	   		index1=matcher.start();
	    	index=matcher.end();
	    }
	    String l[]=new String[3];
		if(index!=-1 && index1!=-1)
		{
			l[0]=command.substring(findFirstIndex(command,'(')+1,index1);
			l[1]=command.substring(index,command.length()-1);
			return l;
		}
		return null;
	}
	public static String[] get_table_string1(String command)
	{
		int index=-1,index1=-1,index2=-1,index3=-1;
		Pattern pattern = Pattern.compile("\\)\\s*from\\s*\\(");
	    Matcher matcher = pattern.matcher(command);
	   	if(matcher.find())
	   	{
	   		index1=matcher.start();
	    	index=matcher.end();
	    }
	    pattern=Pattern.compile("\\s*if\\s*\\(");
	    matcher=pattern.matcher(command);
	    String l[]=new String[3];
	    if(matcher.find())
	    {
	    	index2=matcher.end()-1;
	    	index3=command.length();
	    }
		if(index!=-1 && index1!=-1)
		{
			l[0]=command.substring(findFirstIndex(command,'(')+1,index1);
			l[1]=command.substring(index,matcher.start()-1);
			l[2]=command.substring(index2,index3);
			return l;
		}
		return null;
	}
	public static String[][] get_tables_names_varialble_names(String command)
	{
		String splitted_string[]=command.split("\\s*,\\s*");
		String tables_names_varial_names[][]=new String[splitted_string.length][2];
		for(int i=0;i<splitted_string.length;i++)
		{
			tables_names_varial_names[i]=splitted_string[i].split("\\.");
		}
		return tables_names_varial_names;
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
	public static boolean main_error=false;
    public static boolean testing_error=false;
    public static boolean inner_testing_error=false;
    public static boolean inner_testing_enter=false;
    public static temp_obj getvariable_data(String command)
    {
    	String arr[]=new String[2];
    	List<String> not_needed_temp=new ArrayList<String>();
   		List<String> not_needed_temp1=new ArrayList<String>();
   		List<String> not_needed_temp2=new ArrayList<String>();
    	{
	   		for(int i=0;i<wanted_temp.length;i++)
	   		{
	   			String wanted_temp1[][]=wanted_temp[i];
	   			for(int j=0;j<wanted_temp1.length;j++)
	   			{
	   				not_needed_temp.add(wanted_temp1[j][0]);
	   				not_needed_temp1.add(wanted_temp1[j][1]);
	   				not_needed_temp2.add(wanted_temp1[j][2]);
	   			}
	   		}
			int count=Collections.frequency(not_needed_temp,command);
			int count1=Collections.frequency(not_needed_temp1,command);
			if(count==0&&count1==0)
			{
				temp_obj obj=new temp_obj();
				obj.output=0;
				obj.output_error="Variable "+command+" Not found in tables...";
				return obj;
			}
			if((count!=0&&count1!=0)||(count>1||count1>1))
			{
				temp_obj obj=new temp_obj();
				obj.output=0;
				obj.output_error="Ambiguity at "+command+" ....";
				return obj;
			}
		}
		int index=-1;
		int k1=not_needed_temp.indexOf(command);
		if(k1!=-1)
			index=k1;
		else
		{
			k1=not_needed_temp1.indexOf(command);
			if(k1!=-1)
				index=k1;
			else
			{
				temp_obj obj=new temp_obj();
				obj.output=0;
				obj.output_error="variable "+command+" Not found...";
				return obj;
			}
		}
		if(index!=-1)
		{
			switch(not_needed_temp2.get(index))
			{
				case "int":
					String da=wanted_data[index];
					try
					{
						int integer=Integer.parseInt(da);
						temp_obj obj=new temp_obj();
				    	obj.output=2;
				    	obj.output_int=integer;
				    	return obj;
					}
					catch(Exception e)
					{
						temp_obj obj=new temp_obj();
				    	obj.output=0;
				    	obj.output_error="Error due to Database converting data...";
				    	return obj;
					}
				case "string":
				{
					String da1=wanted_data[index];
					temp_obj obj=new temp_obj();
			    	obj.output=1;
			    	obj.output_string=da1;
			    	return obj;
				}
				case "double":
					String da2=wanted_data[index];
					try
					{
						double dou=Double.parseDouble(da2);
						temp_obj obj=new temp_obj();
				    	obj.output=3;
				    	obj.output_double=dou;
				    	return obj;
					}
					catch(Exception e)
					{
						temp_obj obj=new temp_obj();
				    	obj.output=0;
				    	obj.output_error="Error due to Database converting data...";
				    	return obj;
					}
				default:
					temp_obj obj=new temp_obj();
			    	obj.output=0;
			    	obj.output_error="Error due to data not found...";
			    	return obj;
			}
		}
		else
		{
			temp_obj obj=new temp_obj();
	    	obj.output=0;
	    	obj.output_error="Error due to data not found...";
	    	return obj;
		}
    }
	public int showSome_withif(String dbname,String command,String commands[])
	{
		System.out.println("i am at showsome without if");
		try
		{
			File db=new File(dbname);
			if(db.exists())
			{
				String pp[]=get_table_string1(command);
				if(pp==null)
				{
					System.out.println("Enter command correctly....");
					return -1;
				}
		     	String table_String_combine_pair="";
	     		String tables_names_varialble_names[][]=get_tables_names_varialble_names(pp[0]);
	     		String tables_names_as_operaiton_names[][]=get_tables_names_as_operaiton_names(pp[1]);
		     	
		     	for(int i=0;i<tables_names_as_operaiton_names.length-1;i++)
		     		table_String_combine_pair=table_String_combine_pair+tables_names_as_operaiton_names[i][0]+",";
		     	table_String_combine_pair=table_String_combine_pair+tables_names_as_operaiton_names[tables_names_as_operaiton_names.length-1][0];
		     	variable_real_names=new String[tables_names_varialble_names.length];

		     	for(int i=0;i<tables_names_varialble_names.length;i++)
		     		variable_real_names[i]=tables_names_varialble_names[i][0]+"."+tables_names_varialble_names[i][1];

		     	String variable_with_database[]=new String[tables_names_varialble_names.length];
	     		List<String> check_table_list=new ArrayList<String>();
	     		List<String> check_alias_table_list=new ArrayList<String>();
		     	{
		     		for(int i=0;i<tables_names_as_operaiton_names.length;i++)
		     		{
		     			check_table_list.add(tables_names_as_operaiton_names[i][0]);
		     			check_alias_table_list.add(tables_names_as_operaiton_names[i][1]);
		     		}
		     		for(int i=0;i<tables_names_varialble_names.length;i++)
		     		{
		     			int count=Collections.frequency(check_table_list,tables_names_varialble_names[i][0]);
		     			if(count>=2)
		     			{
		     				System.out.println("Ambiguity at the table \""+tables_names_varialble_names[i][0]+"\"Please check it....");
		     				return -1;
		     			}
		     			int count1=Collections.frequency(check_alias_table_list,tables_names_varialble_names[i][0]);
		     			if(count1>=2)
		     			{
		     				System.out.println("Two Tables using the "+tables_names_varialble_names[i][0]+" Variable for Alias operation...");
		     				return -1;
		     			}
		     			if(count1!=0 && count!=0)
		     			{
		     				System.out.println("Variable \""+tables_names_varialble_names[i][0]+"\" is used in Table name and also Alias variable...");
		     				return -1;
		     			}
		     			if(count==0 && count1==0)
		     			{
		     				System.out.println("Variable \""+tables_names_varialble_names[i][0]+"\" Not found in tables and Alias variables...");
		     				return -1;
		     			}
		     			int index=check_alias_table_list.indexOf(tables_names_varialble_names[i][0]);
		     			if(index>=0)
		     			{
		     				variable_with_database[i]=check_table_list.get(index)+"."+tables_names_varialble_names[i][1];
		     			}
		     			else
		     			{
		     				int index1=check_table_list.indexOf(tables_names_varialble_names[i][0]);
		     				variable_with_database[i]=check_table_list.get(index1)+"."+tables_names_varialble_names[i][1];
		     			}
		     		}
		     	}
		     	File schema_file=new File(db+File.separator+"schema");
		     	if(!schema_file.exists())
		     	{
					System.out.println("Read/Write Operation permission denied....");
					return -1;
		     	}
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
			   	int tables_variable_count[]=new int[tables.length];
			   	for(int i=0,k=0;i<schema_string_devided.length;i++)
			   	{
			   		tables_variable_count[i]=schema_string_devided[i].length;
			   		for (int j=0;j<schema_string_devided[i].length;j++)
			   		{
			   			tables_variable_list.add(tables[i]+"."+schema_string_devided[i][j][0]);
			   		}
			   	}

			   	List<String[]> tables_variable_list_temp=new ArrayList<String[]>();
			   	wanted_temp=new String[schema_strings.length][][];
			   	for(int i=0,m=0;i<tables_variable_count.length;i++)
			   	{
			   		String tb=tables_names_as_operaiton_names[i][0];
			   		String alias=tables_names_as_operaiton_names[i][1];
			   		String wanted_temp1[][]=new String[tables_variable_count[i]][];
			   		for(int j=0;j<tables_variable_count[i];j++)
			   		{
			   			String wanted_temp2[]=new String[3]; 
			   			String arr[]=new String[2];
			   			arr[0]=tables_variable_list.get(m++);
			   			wanted_temp2[0]=arr[0];
			   			String tmp_var=arr[0].substring(arr[0].indexOf('.')+1,arr[0].length());
			   			if(alias!="")
			   			{
			   				arr[1]=alias+"."+tmp_var;
			   				wanted_temp2[1]=arr[1];
			   			}
			   			else
			   			{
			   				wanted_temp2[1]="";
			   				arr[1]=tb+"."+tmp_var;
			   			}
			   			wanted_temp2[2]=schema_string_devided[i][j][1];
			   			wanted_temp1[j]=wanted_temp2;
			   			tables_variable_list_temp.add(arr);
			   		}
			   		wanted_temp[i]=wanted_temp1;
			   	}
			   	{
			   		List<String> not_needed_temp=new ArrayList<String>();
			   		List<String> not_needed_temp1=new ArrayList<String>();
			   		for(int i=0;i<wanted_temp.length;i++)
			   		{
			   			String wanted_temp1[][]=wanted_temp[i];
			   			for(int j=0;j<wanted_temp1.length;j++)
			   			{
			   				not_needed_temp.add(wanted_temp1[j][0]);
			   				if(!wanted_temp1[j][1].equals(""))
			   					not_needed_temp1.add(wanted_temp1[j][1]);
			   			}
			   		}
			   		for(int i=0;i<variable_real_names.length;i++)
			   		{
			   			int count=Collections.frequency(not_needed_temp,variable_real_names[i]);
			   			int count1=Collections.frequency(not_needed_temp1,variable_real_names[i]);
			   			if(count==0&&count1==0)
			   			{
			   				System.out.println("Variable "+variable_real_names[i]+" Not found in tables...");
							return -1;
			   			}
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
			   	if(real_data_field_count==0)
			   		return -1;
			   	System.out.println("*____________________*");
			   	for(int i=0;i<variable_real_names.length;i++)
			   	{
			   		System.out.printf("%-15s",variable_real_names[i]);
			   	}
			   	System.out.println("\n\n");
			   	List<String> variable_with_database_list= Arrays.asList(variable_with_database);

			   	List<String> arr_tb_var=new ArrayList<String>();
			   	List<String> arr_alias_var=new ArrayList<String>();
			   	
			   	for(int i=0;i<tables_variable_list_temp.size();i++)
			   	{
			   		arr_tb_var.add(tables_variable_list_temp.get(i)[0]);
			   		arr_alias_var.add(tables_variable_list_temp.get(i)[1]);
			   	}
			   	real_data_field_count=0;
			   	for(int i=0;i<real_data.length;i++)
			   	{
			   		wanted_data=real_data[i];
			   		boolean check_boolean=testing(pp[2]);
				   	if(main_error)
				   	{
				   		main_error=false;
				   		System.out.println("Error in Command ....");
				   		return -1;
				   	}
				   	if(!check_boolean)
				   	{
				   		main_error=false;
				   		continue;
				   	}
				   	real_data_field_count++;
			   		int temp_count=variable_real_names.length;
			   		int index_arr[]=new int[temp_count];
			   		for(int m=0;m<temp_count;m++)
			   		{
			   			int k1=arr_alias_var.indexOf(variable_real_names[m]);
			   			if(k1!=-1)
			   				index_arr[m]=k1;
			   			else
			   			{
			   				k1=arr_tb_var.indexOf(variable_real_names[m]);
			   				if(k1!=-1)
			   					index_arr[m]=k1;
			   				else
			   				{
			   					System.out.println("variable "+variable_real_names[m]+" Not found...");
			   					return -1;
			   				}
			   			}
			   		}
			   		for(int j=0;j<temp_count;j++)
			   		{
			   			System.out.printf("%-15s",real_data[i][index_arr[j]]);
			   		}
			   		System.out.println("\n");
			   	}
			   	System.out.println("____________________");
			   	System.out.println("\n"+"No of Rows :- "+real_data_field_count+"...");
			   	System.out.println("*____________________*");
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
	public int showSome_withoutif(String dbname,String command,String commands[])
	{
		try
		{
			File db=new File(dbname);
			if(db.exists())
			{
				String pp[]=get_table_string(command);
				if(pp==null)
				{
					System.out.println("Enter command correctly....");
					return -1;
				}
		     	String table_String_combine_pair="";
	     		String tables_names_varialble_names[][]=get_tables_names_varialble_names(pp[0]);
	     		String tables_names_as_operaiton_names[][]=get_tables_names_as_operaiton_names(pp[1]);
		     	
		     	for(int i=0;i<tables_names_as_operaiton_names.length-1;i++)
		     		table_String_combine_pair=table_String_combine_pair+tables_names_as_operaiton_names[i][0]+",";
		     	table_String_combine_pair=table_String_combine_pair+tables_names_as_operaiton_names[tables_names_as_operaiton_names.length-1][0];
		     	String variable_real_names[]=new String[tables_names_varialble_names.length];

		     	for(int i=0;i<tables_names_varialble_names.length;i++)
		     		variable_real_names[i]=tables_names_varialble_names[i][0]+"."+tables_names_varialble_names[i][1];

		     	String variable_with_database[]=new String[tables_names_varialble_names.length];
	     		List<String> check_table_list=new ArrayList<String>();
	     		List<String> check_alias_table_list=new ArrayList<String>();
		     	{
		     		for(int i=0;i<tables_names_as_operaiton_names.length;i++)
		     		{
		     			check_table_list.add(tables_names_as_operaiton_names[i][0]);
		     			check_alias_table_list.add(tables_names_as_operaiton_names[i][1]);
		     		}
		     		for(int i=0;i<tables_names_varialble_names.length;i++)
		     		{
		     			int count=Collections.frequency(check_table_list,tables_names_varialble_names[i][0]);
		     			if(count>=2)
		     			{
		     				System.out.println("Ambiguity at the table \""+tables_names_varialble_names[i][0]+"\"Please check it....");
		     				return -1;
		     			}
		     			int count1=Collections.frequency(check_alias_table_list,tables_names_varialble_names[i][0]);
		     			if(count1>=2)
		     			{
		     				System.out.println("Two Tables using the "+tables_names_varialble_names[i][0]+" Variable for Alias operation...");
		     				return -1;
		     			}
		     			if(count1!=0 && count!=0)
		     			{
		     				System.out.println("Variable \""+tables_names_varialble_names[i][0]+"\" is used in Table name and also Alias variable...");
		     				return -1;
		     			}
		     			if(count==0 && count1==0)
		     			{
		     				System.out.println("Variable \""+tables_names_varialble_names[i][0]+"\" Not found in tables and Alias variables...");
		     				return -1;
		     			}
		     			int index=check_alias_table_list.indexOf(tables_names_varialble_names[i][0]);
		     			if(index>=0)
		     			{
		     				variable_with_database[i]=check_table_list.get(index)+"."+tables_names_varialble_names[i][1];
		     			}
		     			else
		     			{
		     				int index1=check_table_list.indexOf(tables_names_varialble_names[i][0]);
		     				variable_with_database[i]=check_table_list.get(index1)+"."+tables_names_varialble_names[i][1];
		     			}
		     		}
		     	}
		     	File schema_file=new File(db+File.separator+"schema");
		     	if(!schema_file.exists())
		     	{
					System.out.println("Read/Write Operation permission denied....");
					return -1;
		     	}
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
			   	int tables_variable_count[]=new int[tables.length];
			   	for(int i=0,k=0;i<schema_string_devided.length;i++)
			   	{
			   		tables_variable_count[i]=schema_string_devided[i].length;
			   		for (int j=0;j<schema_string_devided[i].length;j++)
			   		{
			   			tables_variable_list.add(tables[i]+"."+schema_string_devided[i][j][0]);
			   		}
			   	}
			   	List<String[]> tables_variable_list_temp=new ArrayList<String[]>();
			   	for(int i=0,m=0;i<tables_variable_count.length;i++)
			   	{
			   		String tb=tables_names_as_operaiton_names[i][0];
			   		String alias=tables_names_as_operaiton_names[i][1];
			   		for(int j=0;j<tables_variable_count[i];j++)
			   		{
			   			String arr[]=new String[2];
			   			arr[0]=tables_variable_list.get(m++);
			   			String tmp_var=arr[0].substring(arr[0].indexOf('.')+1,arr[0].length());
			   			if(alias!="")
			   			{
			   				arr[1]=alias+"."+tmp_var;
			   			}
			   			else
			   			{
			   				arr[1]=tb+"."+tmp_var;
			   			}
			   			tables_variable_list_temp.add(arr);
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
			   	for(int i=0;i<variable_real_names.length;i++)
			   	{
			   		System.out.printf("%-15s",variable_real_names[i]);
			   	}
			   	System.out.println("\n\n");
			   	List<String> variable_with_database_list= Arrays.asList(variable_with_database);

			   	List<String> arr_tb_var=new ArrayList<String>();
			   	List<String> arr_alias_var=new ArrayList<String>();
			   	
			   	for(int i=0;i<tables_variable_list_temp.size();i++)
			   	{
			   		arr_tb_var.add(tables_variable_list_temp.get(i)[0]);
			   		arr_alias_var.add(tables_variable_list_temp.get(i)[1]);
			   	}
			   	for(int i=0;i<real_data.length;i++)
			   	{
			   		int temp_count=variable_real_names.length;
			   		int index_arr[]=new int[temp_count];
			   		for(int m=0;m<temp_count;m++)
			   		{
			   			int k1=arr_alias_var.indexOf(variable_real_names[m]);
			   			if(k1!=-1)
			   				index_arr[m]=k1;
			   			else
			   			{
			   				k1=arr_tb_var.indexOf(variable_real_names[m]);
			   				if(k1!=-1)
			   					index_arr[m]=k1;
			   			}
			   		}
			   		for(int j=0;j<temp_count;j++)
			   		{
			   			System.out.printf("%-15s",real_data[i][index_arr[j]]);
			   		}
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

		public static boolean condition_checking(String command)
	    {
	        //pattern checking of conditionp
	        boolean result = false;
	        for (String conj : command.split("\\|\\|")) {
	            boolean b = true;
	            for (String litteral : conj.split("&&"))
	                b &= Boolean.parseBoolean(litteral.trim());
	            result |= b;
	        }
	        return result;
	    }
	    public static temp_obj relational_condition_checking(List<temp_obj> check_obj)
	    {
	        if(check_obj.size()==1)
	        {
	            temp_obj t=check_obj.get(0);
	            if(t.output==4 && t.operator_present==false)
	            {
	                return t;
	            }
	            else
	            {
	                return null;
	            }
	        }
	        if(check_obj.size()>2)
	            return null;
	        {
	            temp_obj t1=check_obj.get(0);
	            temp_obj t2=check_obj.get(1);
	            if(t1.output==0 || t1.output==-1 || t2.output==0 || t2.output==-1)
	                return null;
	            temp_obj obj=new temp_obj();
	            if(t1.operator_present && !t2.operator_present)
	            {
	                switch(t1.operator)
	                {
	                    case "==":
	                        {
	                            if(t1.output==4||t2.output==4)
	                            {
	                                if(t1.output==4&&t2.output==4)
	                                {
	                                    obj.output=4;
	                                    obj.output_boolean=(t1.output_boolean==t2.output_boolean);
	                                    return obj;
	                                }
	                                else
	                                {
	                                    obj.output=0;
	                                    obj.output_error="operands beside == operator are 1 is boolean and another is invalid";
	                                    return obj;
	                                }
	                            }
	                            else if(t1.output==1||t2.output==1)
	                            {
	                                if(t1.output==1&&t2.output==1)
	                                {
	                                    obj.output=4;
	                                    obj.output_boolean=((t1.output_string).equals(t2.output_string));
	                                    return obj;
	                                }
	                                else
	                                {
	                                    obj.output=0;
	                                    obj.output_error="operands beside == operator are 1 is string and another is invalid";
	                                    return obj;
	                                }
	                            }
	                            else if(t1.output==3||t2.output==3)
	                            {
	                                if(t1.output==2&&t2.output==3)
	                                {
	                                    double d=(double)t1.output_int;
	                                    obj.output=4;
	                                    obj.output_boolean=(d==(t2.output_double));
	                                    return obj;
	                                }
	                                else if(t1.output==3&&t2.output==2)
	                                {
	                                    double d=(double)t2.output_int;
	                                    obj.output=4;
	                                    obj.output_boolean=(d==(t1.output_double));
	                                    return obj;
	                                }
	                                else if(t1.output==3&&t2.output==3)
	                                {
	                                    obj.output=4;
	                                    obj.output_boolean=(t1.output_boolean==t2.output_boolean);
	                                    return obj;
	                                }
	                                else
	                                {
	                                    return null;
	                                }
	                            }
	                            else if(t1.output==2&&t2.output==2)
	                            {
	                                obj.output=4;
	                                obj.output_boolean=(t1.output_int==t2.output_int);
	                                return obj;
	                            }
	                            else
	                            {
	                                return null;
	                            }
	                        }
	                    case "!=":
	                        {
	                            if(t1.output==4||t2.output==4)
	                            {
	                                if(t1.output==4&&t2.output==4)
	                                {
	                                    obj.output=4;
	                                    obj.output_boolean=(t1.output_boolean!=t2.output_boolean);
	                                    return obj;
	                                }
	                                else
	                                {
	                                    obj.output=0;
	                                    obj.output_error="operands beside == operator are 1 is boolean and another is invalid";
	                                    return obj;
	                                }
	                            }
	                            else if(t1.output==1||t2.output==1)
	                            {
	                                if(t1.output==1&&t2.output==1)
	                                {
	                                    obj.output=4;
	                                    obj.output_boolean=!((t1.output_string).equals(t2.output_string));
	                                    return obj;
	                                }
	                                else
	                                {
	                                    obj.output=0;
	                                    obj.output_error="operands beside != operator are 1 is string and another is invalid";
	                                    return obj;
	                                }
	                            }
	                            else if(t1.output==3||t2.output==3)
	                            {
	                                if(t1.output==2&&t2.output==3)
	                                {
	                                    double d=(double)t1.output_int;
	                                    obj.output=4;
	                                    obj.output_boolean=(d!=(t2.output_double));
	                                    return obj;
	                                }
	                                else if(t1.output==3&&t2.output==2)
	                                {
	                                    double d=(double)t2.output_int;
	                                    obj.output=4;
	                                    obj.output_boolean=(d!=(t1.output_double));
	                                    return obj;
	                                }
	                                else if(t1.output==3&&t2.output==3)
	                                {
	                                    obj.output=4;
	                                    obj.output_boolean=(t1.output_boolean!=t2.output_boolean);
	                                    return obj;
	                                }
	                                else
	                                {
	                                    return null;
	                                }
	                            }
	                            else if(t1.output==2&&t2.output==2)
	                            {
	                                obj.output=4;
	                                obj.output_boolean=(t1.output_int!=t2.output_int);
	                                return obj;
	                            }
	                            else
	                            {
	                                return null;
	                            }
	                        }
	                    case "<=":
	                        {
	                            if(t1.output==4||t2.output==4)
	                            {
	                                obj.output=0;
	                                obj.output_error="boolean can't work with <=...";
	                                return obj;
	                            }
	                            else if(t1.output==1||t2.output==1)
	                            {
	                                obj.output=0;
	                                obj.output_error="string can't work with <=...";
	                                return obj;
	                            }
	                            else if(t1.output!=0&&t2.output!=0&&t1.output!=-1&&t2.output!=-1)
	                            {
	                                if(t1.output==2&&t2.output==3)
	                                {
	                                    double d=(double)t1.output_int;
	                                    obj.output=4;
	                                    obj.output_boolean=(d<=(t2.output_double));
	                                    return obj;
	                                }
	                                else if(t1.output==3&&t2.output==2)
	                                {
	                                    double d=(double)t2.output_int;
	                                    obj.output=4;
	                                    obj.output_boolean=((t1.output_double)<=d);
	                                    return obj;
	                                }
	                                else if(t1.output==3&&t2.output==3)
	                                {
	                                    obj.output=4;
	                                    obj.output_boolean=(t1.output_double<=t2.output_double);
	                                    return obj;
	                                }
	                                else if(t1.output==2&&t2.output==2)
	                                {
	                                    obj.output=4;
	                                    obj.output_boolean=(t1.output_int<=t2.output_int);
	                                    return obj;
	                                }
	                                else
	                                {
	                                    return null;
	                                }
	                            }
	                            else
	                            {
	                                return null;
	                            }
	                        }
	                    case "<<":
	                        {
	                            if(t1.output==4||t2.output==4)
	                            {
	                                obj.output=0;
	                                obj.output_error="boolean can't work with <<...";
	                                return obj;
	                            }
	                            else if(t1.output==1||t2.output==1)
	                            {
	                                obj.output=0;
	                                obj.output_error="string can't work with <<...";
	                                return obj;
	                            }
	                            else if(t1.output!=0&&t2.output!=0&&t1.output!=-1&&t2.output!=-1)
	                            {
	                                if(t1.output==2&&t2.output==3)
	                                {
	                                    double d=(double)t1.output_int;
	                                    obj.output=4;
	                                    obj.output_boolean=(d<(t2.output_double));
	                                    return obj;
	                                }
	                                else if(t1.output==3&&t2.output==2)
	                                {
	                                    double d=(double)t2.output_int;
	                                    obj.output=4;
	                                    obj.output_boolean=((t1.output_double)<d);
	                                    return obj;
	                                }
	                                else if(t1.output==3&&t2.output==3)
	                                {
	                                    obj.output=4;
	                                    obj.output_boolean=(t1.output_double<t2.output_double);
	                                    return obj;
	                                }
	                                else if(t1.output==2&&t2.output==2)
	                                {
	                                    obj.output=4;
	                                    obj.output_boolean=(t1.output_int<t2.output_int);
	                                    return obj;
	                                }
	                                else
	                                {
	                                    return null;
	                                }
	                            }
	                            else
	                            {
	                                return null;
	                            }
	                        }
	                    case ">=":
	                        {
	                            if(t1.output==4||t2.output==4)
	                            {
	                                obj.output=0;
	                                obj.output_error="boolean can't work with >=...";
	                                return obj;
	                            }
	                            else if(t1.output==1||t2.output==1)
	                            {
	                                obj.output=0;
	                                obj.output_error="string can't work with >=...";
	                                return obj;
	                            }
	                            else if(t1.output!=0&&t2.output!=0&&t1.output!=-1&&t2.output!=-1)
	                            {
	                                if(t1.output==2&&t2.output==3)
	                                {
	                                    double d=(double)t1.output_int;
	                                    obj.output=4;
	                                    obj.output_boolean=(d>=(t2.output_double));
	                                    return obj;
	                                }
	                                else if(t1.output==3&&t2.output==2)
	                                {
	                                    double d=(double)t2.output_int;
	                                    obj.output=4;
	                                    obj.output_boolean=((t1.output_double)>=d);
	                                    return obj;
	                                }
	                                else if(t1.output==3&&t2.output==3)
	                                {
	                                    obj.output=4;
	                                    obj.output_boolean=(t1.output_double>=t2.output_double);
	                                    return obj;
	                                }
	                                else if(t1.output==2&&t2.output==2)
	                                {
	                                    obj.output=4;
	                                    obj.output_boolean=(t1.output_int>=t2.output_int);
	                                    return obj;
	                                }
	                                else
	                                {
	                                    return null;
	                                }
	                            }
	                            else
	                            {
	                                return null;
	                            }
	                        }
	                    case ">>":
	                        {
	                            if(t1.output==4||t2.output==4)
	                            {
	                                obj.output=0;
	                                obj.output_error="boolean can't work with >>...";
	                                return obj;
	                            }
	                            else if(t1.output==1||t2.output==1)
	                            {
	                                obj.output=0;
	                                obj.output_error="string can't work with >>...";
	                                return obj;
	                            }
	                            else if(t1.output!=0&&t2.output!=0&&t1.output!=-1&&t2.output!=-1)
	                            {
	                                if(t1.output==2&&t2.output==3)
	                                {
	                                    double d=(double)t1.output_int;
	                                    obj.output=4;
	                                    obj.output_boolean=(d>(t2.output_double));
	                                    return obj;
	                                }
	                                else if(t1.output==3&&t2.output==2)
	                                {
	                                    double d=(double)t2.output_int;
	                                    obj.output=4;
	                                    obj.output_boolean=((t1.output_double)>d);
	                                    return obj;
	                                }
	                                else if(t1.output==3&&t2.output==3)
	                                {
	                                    obj.output=4;
	                                    obj.output_boolean=(t1.output_double>t2.output_double);
	                                    return obj;
	                                }
	                                else if(t1.output==2&&t2.output==2)
	                                {
	                                    obj.output=4;
	                                    obj.output_boolean=(t1.output_int>t2.output_int);
	                                    return obj;
	                                }
	                                else
	                                {
	                                    return null;
	                                }
	                            }
	                            else
	                            {
	                                return null;
	                            }
	                        }
	                    default:
	                        return null;
	                }
	            }
	            else
	            {
	                return null;
	            }
	        }
	    }
	    public static boolean evaluation_function(String command)
	    {
	        temp_obj obj=relational_testing(command);
	        if(obj==null)
	        {
	            System.out.println(command+" Error in relational statement....");
	            if(inner_testing_enter)
	            {
	                inner_testing_enter=false;
	                inner_testing_error=true;
	            }
	            else
	            {
	                main_error=true;
	            }
	            return false;
	        }
	        if(obj.output==0)
	        {
	            if(inner_testing_enter)
	            {
	                inner_testing_enter=false;
	                inner_testing_error=true;
	            }
	            else
	            {
	                main_error=true;
	            }
	            System.out.println("Error "+obj.output_error);
	            return false;
	        }
	        else if(obj.output==4)
	        {
	                return obj.output_boolean;
	        }
	        else
	        {
	            if(inner_testing_enter)
	            {
	                inner_testing_enter=false;
	                inner_testing_error=true;
	            }
	            else
	            {
	                main_error=true;
	            }
	            return false;
	        }
	    }
	    public static temp_obj function(String command)
	    {
	        temp_obj obj=arithmetic_testing(command);
	        if(obj==null)
	        {
	            return null;
	        }
	        if(obj.output==0||obj.output==-1)
	        {
	            System.out.println(obj.output_error);
	            return null;
	        }
	        return obj;
	    }
	    public static temp_obj arithmetic_function(String command)
	    {
	        if((command.trim()).isEmpty())
	        {
	            temp_obj obj=new temp_obj();
	            obj.output=0;
	            obj.output_error="Given Opeand is not valid...";
	            return obj;
	        }
	        String int_reg="[0-9]+";
	        String int_reg1="-([0-9]+)";
	        String string_regex="('([^'])*')";
	        String double_reg="[0-9]+\\.[0-9]+";
	        String double_reg1="-([0-9]+\\.[0-9]+)";
	        String boolean_reg="(true|false)";
	        String variable_reg="[a-zA-Z_]{1}[a-zA-Z_0-9]*\\.[a-zA-Z_]{1}[a-zA-Z_0-9]*";
	        temp_obj obj=new temp_obj();
	        if(Pattern.matches(boolean_reg,command))
	        {
	            obj.output=4;
	            if(command.equals("true"))
	            {
	                obj.output_boolean=true;
	            }
	            else
	            {
	                obj.output_boolean=false;
	            }
	            return obj;
	        }
	        else if(Pattern.matches(int_reg,command))
	        {
	            String command1=command;
	            command=command.replaceFirst("^0+(?!$)","");
	            try
	            {
	                int integer=Integer.parseInt(command);
	                obj.output=2;
	                obj.output_int=integer;
	            }
	            catch(NumberFormatException e)
	            {
	                obj.output=0;
	                obj.output_error=command1+" is too length integer ";
	            }
	            catch(Exception e)
	            {
	                obj.output=0;
	                obj.output_error=command1+" is not Integer ";
	            }
	            return obj;
	        }
	        else if(Pattern.matches(int_reg1,command))
	        {
	            String command1=command;
	            char sym='-';
	            command=command.substring(1,command.length());
	            command=command.replaceFirst("^0+(?!$)","");
	            command=sym+command;
	            try
	            {
	                int integer=Integer.parseInt(command);
	                obj.output=2;
	                obj.output_int=integer;
	            }
	            catch(NumberFormatException e)
	            {
	                obj.output=0;
	                obj.output_error=command1+" is too length integer ";
	            }
	            catch(Exception e)
	            {
	                obj.output=0;
	                obj.output_error=command1+" is not Integer";
	            }
	            return obj;
	        }
	        else if(Pattern.matches(double_reg,command))
	        {
	            String command1=command;
	            command=command.replaceFirst("^0+(?!$)","");
	            try
	            {
	                double integer=Double.parseDouble(command);
	                obj.output=3;
	                obj.output_double=integer;
	            }
	            catch(NumberFormatException e)
	            {
	                obj.output=0;
	                obj.output_error=command1+" is too length double ";
	            }
	            catch(Exception e)
	            {
	                obj.output=0;
	                obj.output_error=command1+" is not double";
	            }
	            return obj;
	        }
	        else if(Pattern.matches(double_reg1,command))
	        {
	            String command1=command;
	            char sym='-';
	            command=command.substring(1,command.length());
	            command=command.replaceFirst("^0+(?!$)","");
	            command=sym+command;
	            try
	            {
	                double integer=Double.parseDouble(command);
	                obj.output=3;
	                obj.output_double=integer;
	            }
	            catch(NumberFormatException e)
	            {
	                obj.output=0;
	                obj.output_error=command1+" is too length double ";
	            }
	            catch(Exception e)
	            {
	                obj.output=0;
	                obj.output_error=command1+" is not double";
	            }
	            return obj;
	        }
	        else if(Pattern.matches(string_regex,command))
	        {
	            String command1=command;
	            command=command.substring(1,command.length()-1);
	            obj.output=1;
	            obj.output_string=command;
	            return obj;
	        }
	        else if(Pattern.matches(variable_reg,command))
	        {
	            return getvariable_data(command);
	        }
	        else
	        {
	            return null;
	        }
	    }
	    public static temp_obj arithmetic_condition_checking(List<temp_obj> list)
	    {
	    	
	        temp_obj temp=new temp_obj();
	        if(list.size()==1)
	        {
	            return list.get(0);
	        }
	        else
	        {
	            for(int i=0;;)
	            {
	                if(i==list.size()-1||list.size()==1)
	                {
	                    break;
	                }
	                temp_obj obj=list.get(i);
	                if(obj.operator_present)
	                {
	                    switch(obj.operator)
	                    {
	                        case "*":
	                            {
	                                temp_obj t1=list.get(i);
	                                temp_obj t2=list.get(i+1);
	                                if((t1.output==2||t1.output==3)&&(t2.output==2||t2.output==3))
	                                {
	                                    if(t1.output==2&&t2.output==2)
	                                    {
	                                        t2.output=2;
	                                        t2.output_int=t1.output_int*t2.output_int;
	                                    }
	                                    else if(t1.output==2&&t2.output==3)
	                                    {
	                                        t2.output=3;
	                                        t2.output_double=t1.output_int*t2.output_double;
	                                    }
	                                    else if(t1.output==3&&t2.output==2)
	                                    {
	                                        t2.output=3;
	                                        t2.output_double=t1.output_double*t2.output_int;
	                                    }
	                                    else
	                                    {
	                                        t2.output=3;
	                                        t2.output_double=t1.output_double*t2.output_double;
	                                    }
	                                    list.remove(t1);
	                                    i--;
	                                }
	                                else
	                                {
	                                    temp.output=0;
	                                    temp.output_error="Operands beside * are not valid";
	                                    return temp;
	                                }
	                            }
	                            break;
	                        case "/":
	                            {
	                                temp_obj t1=list.get(i);
	                                temp_obj t2=list.get(i+1);
	                                if((t1.output==2||t1.output==3)&&(t2.output==2||t2.output==3))
	                                {
	                                    if(t1.output==2&&t2.output==2)
	                                    {
	                                        if(t2.output_int==0)
	                                        {
	                                            temp.output=0;
	                                            temp.output_error="devide by 0";
	                                            return temp;
	                                        }
	                                        t2.output=2;
	                                        t2.output_int=t1.output_int/t2.output_int;
	                                    }
	                                    else if(t1.output==2&&t2.output==3)
	                                    {
	                                        if(t2.output_double==0)
	                                        {
	                                            temp.output=0;
	                                            temp.output_error="devide by 0.0";
	                                            return temp;
	                                        }
	                                        t2.output=3;
	                                        t2.output_double=t1.output_int/t2.output_double;
	                                    }
	                                    else if(t1.output==3&&t2.output==2)
	                                    {
	                                        if(t2.output_int==0)
	                                        {
	                                            temp.output=0;
	                                            temp.output_error="devide by 0";
	                                            return temp;
	                                        }
	                                        t2.output=3;
	                                        t2.output_double=t1.output_double/t2.output_int;
	                                    }
	                                    else
	                                    {
	                                        if(t2.output_double==0.0)
	                                        {
	                                            temp.output=0;
	                                            temp.output_error="devide by 0.0";
	                                            return temp;
	                                        }
	                                        t2.output=3;
	                                        t2.output_double=t1.output_double/t2.output_double;
	                                    }
	                                    list.remove(t1);
	                                    i--;
	                                }
	                                else
	                                {
	                                    temp.output_error="operands beside / are not valid";
	                                    return temp;
	                                }
	                            }
	                            break;
	                        case "%":
	                            {
	                                temp_obj t1=list.get(i);
	                                temp_obj t2=list.get(i+1);
	                                if((t1.output==2||t1.output==3)&&(t2.output==2||t2.output==3))
	                                {
	                                    if(t1.output==2&&t2.output==2)
	                                    {
	                                        if(t2.output_int==0)
	                                        {
	                                            temp.output=0;
	                                            temp.output_error="devide by 0";
	                                            return temp;
	                                        }
	                                        t2.output=2;
	                                        t2.output_int=t1.output_int%t2.output_int;
	                                    }
	                                    else if(t1.output==2&&t2.output==3)
	                                    {
	                                        if(t2.output_double==0)
	                                        {
	                                            temp.output=0;
	                                            temp.output_error="devide by 0.0";
	                                            return temp;
	                                        }
	                                        t2.output=3;
	                                        t2.output_double=t1.output_int%t2.output_double;
	                                    }
	                                    else if(t1.output==3&&t2.output==2)
	                                    {
	                                        if(t2.output_int==0)
	                                        {
	                                            temp.output=0;
	                                            temp.output_error="devide by 0";
	                                            return temp;
	                                        }
	                                        t2.output=3;
	                                        t2.output_double=t1.output_double%t2.output_int;
	                                    }
	                                    else
	                                    {
	                                        if(t2.output_double==0.0)
	                                        {
	                                            temp.output=0;
	                                            temp.output_error="devide by 0.0";
	                                            return temp;
	                                        }
	                                        t2.output=3;
	                                        t2.output_double=t1.output_double%t2.output_double;
	                                    }
	                                    list.remove(t1);
	                                    i--;
	                                }
	                                else
	                                {
	                                    temp.output=0;
	                                    temp.output_error="operands beside % are not valid";
	                                    return temp;
	                                }
	                            }
	                            break;
	                        default:
	                            break;
	                    }
	                }
	                else
	                {
	                    temp.output=0;
	                    temp.output_error="No operator present between two operands.";
	                    return temp;
	                }
	                i++;
	            }
	        }
	        {
	            if(list.size()==1)
	            {
	                return list.get(0);
	            }
	            else
	            {
	                for(int i=0;;)
	                {
	                    if(i==list.size()-1||list.size()==1)
	                    {
	                        break;
	                    }
	                    temp_obj obj=list.get(i);
	                    if(obj.output==0)
	                    {
	                        temp.output=0;
	                        temp.output_error="Operands are not valid...";
	                        return temp;
	                    }
	                    if(obj.operator_present)
	                    {
	                        switch(obj.operator)
	                        {
	                            case "+":
	                                {
	                                    temp_obj t1=list.get(i);
	                                    temp_obj t2=list.get(i+1);
	                                    if((t1.output==2||t1.output==3)&&(t2.output==2||t2.output==3))
	                                    {
	                                        if(t1.output==2&&t2.output==2)
	                                        {
	                                            t2.output=2;
	                                            t2.output_int=t1.output_int+t2.output_int;
	                                        }
	                                        else if(t1.output==2&&t2.output==3)
	                                        {
	                                            t2.output=3;
	                                            t2.output_double=t1.output_int+t2.output_double;
	                                        }
	                                        else if(t1.output==3&&t2.output==2)
	                                        {
	                                            t2.output=3;
	                                            t2.output_double=t1.output_double+t2.output_int;
	                                        }
	                                        else
	                                        {
	                                            t2.output=3;
	                                            t2.output_double=t1.output_double+t2.output_double;
	                                        }
	                                        list.remove(t1);
	                                        i--;
	                                    }
	                                    else if(t1.output==4 || t2.output==4)
	                                    {
	                                        if(t1.output==1 && t2.output==4)
	                                        {
	                                            t2.output=1;
	                                            t2.output_string=t1.output_string+t2.output_boolean;
	                                        }
	                                        else if(t1.output==4 && t2.output==1)
	                                        {
	                                            t2.output=1;
	                                            t2.output_string=t1.output_boolean+t2.output_string;
	                                        }
	                                        else
	                                        {
	                                            temp.output=0;
	                                            temp.output_error="operator with boolean operand and other Invalid Operand..";
	                                            return temp;
	                                        }
	                                        list.remove(t1);
	                                        i--;
	                                    }
	                                    else if(t1.output==1 || t2.output==1)
	                                    {
	                                        if(t1.output==1)
	                                        {
	                                            switch(t2.output)
	                                            {
	                                                case 1:
	                                                    t2.output=1;
	                                                    t2.output_string=t1.output_string+t2.output_string;
	                                                    break;
	                                                case 2:
	                                                    t2.output=1;
	                                                    t2.output_string=t1.output_string+t2.output_int;
	                                                    break;
	                                                case 3:
	                                                    t2.output=1;
	                                                    t2.output_string=t1.output_string+t2.output_double;
	                                                    break;
	                                                case 4:
	                                                    t2.output=1;
	                                                    t2.output_string=t1.output_string+t2.output_boolean;
	                                                    break;
	                                                default:
	                                                    temp.output=0;
	                                                    temp.output_error="Invalid operator found..";
	                                                    return temp;
	                                            }
	                                        }
	                                        else
	                                        {
	                                            switch(t1.output)
	                                            {
	                                                case 1:
	                                                    t2.output=1;
	                                                    t2.output_string=t1.output_string+t2.output_string;
	                                                    break;
	                                                case 2:
	                                                    t2.output=1;
	                                                    t2.output_string=t1.output_int+t2.output_string;
	                                                    break;
	                                                case 3:
	                                                    t2.output=1;
	                                                    t2.output_string=t1.output_double+t2.output_string;
	                                                    break;
	                                                case 4:
	                                                    t2.output=1;
	                                                    t2.output_string=t1.output_boolean+t2.output_string;
	                                                    break;
	                                                default:
	                                                    temp.output=0;
	                                                    temp.output_error="Invalid operator found..";
	                                                    return temp;
	                                            }
	                                        }
	                                        list.remove(t1);
	                                        i--;
	                                    }
	                                    else
	                                    {
	                                        temp.output=0;
	                                        temp.output_error="Operands beside + are not valid";
	                                        return temp;
	                                    }
	                                }
	                                break;
	                            case "-":
	                                {
	                                    temp_obj t1=list.get(i);
	                                    temp_obj t2=list.get(i+1);
	                                    if((t1.output==2||t1.output==3)&&(t2.output==2||t2.output==3))
	                                    {
	                                        if(t1.output==2&&t2.output==2)
	                                        {
	                                            t2.output=2;
	                                            t2.output_int=t1.output_int-t2.output_int;
	                                        }
	                                        else if(t1.output==2&&t2.output==3)
	                                        {
	                                            t2.output=3;
	                                            t2.output_double=t1.output_int-t2.output_double;
	                                        }
	                                        else if(t1.output==3&&t2.output==2)
	                                        {
	                                            t2.output=3;
	                                            t2.output_double=t1.output_double-t2.output_int;
	                                        }
	                                        else
	                                        {
	                                            t2.output=3;
	                                            t2.output_double=t1.output_double-t2.output_double;
	                                        }
	                                        list.remove(t1);
	                                        i--;
	                                    }
	                                    else
	                                    {
	                                        temp.output=0;
	                                        temp.output_error="Operands beside - are not valid";
	                                        return temp;
	                                    }
	                                }
	                                break;
	                            default:
	                                temp.output=0;
	                                temp.output_error="Invalid Operator present..";
	                                return temp;
	                        }
	                    }
	                    else
	                    {
	                        temp.output=0;
	                        temp.output_error="No operator present between two operands.";
	                        return temp;
	                    }
	                    i++;
	                }
	            }
	            if(list.size()==0)
	                return null;
	            else if(list.size()==1)
	            {
	                return list.get(0);
	            }
	            else
	            {
	                temp.output=0;
	                temp.output_error="Invalid arithmetic statement...";
	                return temp;
	            }
	        }
	    }
	    public static temp_obj arithmetic_testing(String command)
	    {
	        List<temp_obj> check_obj=new ArrayList<temp_obj>();
	        List<String[]> command_array=arithmetic_devide(command);
	        if(command_array==null||command_array.size()==0)
	        {
	            temp_obj obj=new temp_obj();
	            obj.output=0;
	            obj.output_error="Error due to Invalid satement";
	            return obj;
	        }
	        else if(command_array.size()>=1)
	        {
	            if(command_array.size()==1 && command.equals(command_array.get(0)[0]))
	            {
	                temp_obj obj=arithmetic_function(command_array.get(0)[0]);
	                if(obj==null)
	                {
	                    return null;
	                }
	                if(obj.output!=0)
	                {
	                    check_obj.add(obj);
	                    return arithmetic_condition_checking(check_obj);
	                }
	                else
	                {
	                    {
	                    	String variable_reg="[a-zA-Z_]{1}[a-zA-Z_0-9]*\\.[a-zA-Z_]{1}[a-zA-Z_0-9]*";
	                    	Pattern pattern = Pattern.compile(variable_reg);
						    Matcher matcher = pattern.matcher(command_array.get(0)[0]);
						   	if(matcher.find())
						   	{
						   		System.out.println(obj.output_error);
						   		return null;
						   	}
	                        inner_testing_enter=true;
	                        boolean value=testing(command_array.get(0)[0]);
	                        inner_testing_enter=false;
	                        if(inner_testing_error==false)
	                        {
	                            temp_obj temp=new temp_obj();
	                            temp.output=4;
	                            temp.output_boolean=value;
	                            check_obj.add(temp);
	                            return arithmetic_condition_checking(check_obj);
	                        }
	                        inner_testing_error=false;
	                    }
	                    System.out.println(obj.output_error);
	                    return null;
	                }
	            }
	            else if(command_array.size()==1 && !command.equals(command_array.get(0)[0]))
	            {
	                temp_obj value=arithmetic_testing(command_array.get(0)[0]);
	                if(value==null)
	                {
	                    {
	                        inner_testing_enter=true;
	                        boolean value1=testing(command_array.get(0)[0]);
	                        inner_testing_enter=false;
	                        if(inner_testing_error==false)
	                        {
	                            temp_obj temp=new temp_obj();
	                            temp.output=4;
	                            temp.output_boolean=value1;
	                            check_obj.add(temp);
	                            return arithmetic_condition_checking(check_obj);
	                        }
	                        inner_testing_error=false;
	                    }
	                    return null;
	                }
	                if(value.output!=0)
	                {
	                    check_obj.add(value);
	                    return arithmetic_condition_checking(check_obj);
	                }
	                else
	                {
	                    {
	                    	String variable_reg="[a-zA-Z_]{1}[a-zA-Z_0-9]*\\.[a-zA-Z_]{1}[a-zA-Z_0-9]*";
	                    	Pattern pattern = Pattern.compile(variable_reg);
						    Matcher matcher = pattern.matcher(command_array.get(0)[0]);
						   	if(matcher.find())
						   	{
						   		System.out.println(value.output_error);
						   		return null;
						   	}
	                        inner_testing_enter=true;
	                        boolean value1=testing(command_array.get(0)[0]);
	                        inner_testing_enter=false;
	                        if(inner_testing_error==false)
	                        {
	                            temp_obj temp=new temp_obj();
	                            temp.output=4;
	                            temp.output_boolean=value1;
	                            check_obj.add(temp);
	                            return arithmetic_condition_checking(check_obj);
	                        }
	                        inner_testing_error=false;
	                    }
	                    System.out.println(value.output_error);
	                    return null;
	                }
	            }
	            for(int i=0;i<command_array.size();i++)
	            {
	                if(((command_array.get(i)[0]).trim()).isEmpty())
	                {
	                    temp_obj obj=new temp_obj();
	                    obj.output=0;
	                    obj.output_error="Error due to no statement beside arithmetic operator....";
	                    return obj;
	                }
	                List<String[]> command_array1=arithmetic_devide(command_array.get(i)[0]);
	                if(command_array1==null || command_array1.size()==0)
	                {
	                    temp_obj obj=new temp_obj();
	                    obj.output=0;
	                    obj.output_error="Error due Invalid Statement....";
	                    return obj;
	                }
	                if(command_array1.size()==1)
	                {
	                    if(((command_array1.get(0)[0]).trim()).isEmpty())
	                    {
	                        temp_obj obj=new temp_obj();
	                        obj.output=0;
	                        obj.output_error="Error due to no statement beside arithmetic operator....";
	                        return obj;
	                    }
	                    if(command_array.get(i)[0].equals(command_array1.get(0)[0]))
	                    {
	                        temp_obj value=arithmetic_function(command_array.get(i)[0]);
	                        if(value==null)
	                        {
	                            return null;
	                        }
	                        if(value.output!=0)
	                        {
	                            if(i==command_array.size()-1)
	                                check_obj.add(value);
	                            else
	                            {
	                                value.operator_present=true;
	                                value.operator=command_array.get(i)[1];
	                                check_obj.add(value);
	                            }
	                        }
	                        else
	                        {
	                        	String variable_reg="[a-zA-Z_]{1}[a-zA-Z_0-9]*\\.[a-zA-Z_]{1}[a-zA-Z_0-9]*";
		                    	Pattern pattern = Pattern.compile(variable_reg);
							    Matcher matcher = pattern.matcher(command_array.get(0)[0]);
							   	if(matcher.find())
							   	{
							   		System.out.println(value.output_error);
							   		return null;
							   	}
	                            inner_testing_enter=true;
	                            boolean value1=testing(command_array.get(i)[0]);
	                            inner_testing_enter=false;
	                            if(inner_testing_error==false)
	                            {
	                                temp_obj temp=new temp_obj();
	                                temp.output=4;
	                                temp.output_boolean=value1;
	                                if(i!=command_array.size()-1)
	                                {
	                                    temp.operator_present=true;
	                                    temp.operator=command_array.get(i)[1];
	                                }
	                                check_obj.add(temp);
	                            }
	                            else
	                            {
	                                inner_testing_error=false;
	                                System.out.println(value.output_error);
	                                return null;
	                            }
	                        }
	                    }
	                    else
	                    {
	                        temp_obj value=arithmetic_testing(command_array1.get(0)[0]);
	                        if(value==null)
	                        {
	                            inner_testing_enter=true;
	                            boolean value1=testing(command_array1.get(0)[0]);
	                            inner_testing_enter=false;
	                            if(inner_testing_error==false)
	                            {
	                                temp_obj temp=new temp_obj();
	                                temp.output=4;
	                                temp.output_boolean=value1;
	                                if(i==command_array.size()-1)
	                                {
	                                    temp.operator_present=true;
	                                    temp.operator=command_array.get(i)[1];
	                                }
	                                check_obj.add(temp);
	                            }
	                            else
	                            {
	                                inner_testing_error=false;
	                                return null;
	                            }
	                        }
	                        if(value.output!=0)
	                        {
	                            if(i==command_array.size()-1)
	                                check_obj.add(value);
	                            else
	                            {
	                                value.operator_present=true;
	                                value.operator=command_array.get(i)[1];
	                                check_obj.add(value);
	                            }
	                        }
	                        else
	                        {
	                        	String variable_reg="[a-zA-Z_]{1}[a-zA-Z_0-9]*\\.[a-zA-Z_]{1}[a-zA-Z_0-9]*";
		                    	Pattern pattern = Pattern.compile(variable_reg);
							    Matcher matcher = pattern.matcher(command_array.get(0)[0]);
							   	if(matcher.find())
							   	{
							   		System.out.println(value.output_error);
							   		return null;
							   	}
	                            inner_testing_enter=true;
	                            boolean value1=testing(command_array1.get(0)[0]);
	                            inner_testing_enter=false;
	                            if(inner_testing_error==false)
	                            {
	                                temp_obj temp=new temp_obj();
	                                temp.output=4;
	                                temp.output_boolean=value1;
	                                if(i==command_array.size()-1)
	                                {
	                                    temp.operator_present=true;
	                                    temp.operator=command_array.get(i)[1];
	                                }
	                                check_obj.add(temp);
	                            }
	                            else
	                            {
	                                inner_testing_error=false;
	                                System.out.println(value.output_error);
	                                return null;
	                            }
	                        }
	                    }
	                }
	                else if(command_array1.size()>=2)
	                {
	                    List<temp_obj> sub_check_obj=new ArrayList<temp_obj>();
	                    for(int j=0;j<command_array1.size();j++)
	                    {
	                        if(((command_array1.get(j)[0]).trim()).isEmpty())
	                        {
	                            temp_obj obj=new temp_obj();
	                            obj.output=0;
	                            obj.output_error="Error due to no statement beside arithmetic operator....";
	                            return obj;
	                        }
	                        temp_obj value=arithmetic_testing(command_array1.get(j)[0]);
	                        if(value==null)
	                        {
	                            inner_testing_enter=true;
	                            boolean value1=testing(command_array1.get(j)[0]);
	                            inner_testing_enter=false;
	                            if(inner_testing_error==false)
	                            {
	                                temp_obj temp=new temp_obj();
	                                temp.output=4;
	                                temp.output_boolean=value1;
	                                if(i==command_array.size()-1)
	                                {
	                                    temp.operator_present=true;
	                                    temp.operator=command_array1.get(j)[1];
	                                }
	                                sub_check_obj.add(temp);
	                            }
	                            else
	                            {
	                                inner_testing_error=false;
	                                return null;
	                            }
	                        }
	                        if(value.output!=0)
	                        {
	                            if(j==command_array1.size()-1)
	                                sub_check_obj.add(value);
	                            else
	                            {
	                                value.operator_present=true;
	                                value.operator=command_array1.get(j)[1];
	                                sub_check_obj.add(value);
	                            }
	                        }
	                        else
	                        {
	                        	String variable_reg="[a-zA-Z_]{1}[a-zA-Z_0-9]*\\.[a-zA-Z_]{1}[a-zA-Z_0-9]*";
		                    	Pattern pattern = Pattern.compile(variable_reg);
							    Matcher matcher = pattern.matcher(command_array.get(0)[0]);
							   	if(matcher.find())
							   	{
							   		System.out.println(value.output_error);
							   		return null;
							   	}
	                            inner_testing_enter=true;
	                            boolean value1=testing(command_array1.get(j)[0]);
	                            inner_testing_enter=false;
	                            if(inner_testing_error==false)
	                            {
	                                temp_obj temp=new temp_obj();
	                                temp.output=4;
	                                temp.output_boolean=value1;
	                                if(i==command_array.size()-1)
	                                {
	                                    temp.operator_present=true;
	                                    temp.operator=command_array1.get(j)[1];
	                                }
	                                sub_check_obj.add(temp);
	                            }
	                            else
	                            {
	                                inner_testing_error=false;
	                                System.out.println(value.output_error);
	                                return null;
	                            }
	                        }
	                    }
	                    temp_obj value=arithmetic_condition_checking(sub_check_obj);
	                    if(value==null)
	                        return null;
	                    if(value.output!=0)
	                    {
	                        if(i==command_array.size()-1)
	                            check_obj.add(value);
	                        else
	                        {
	                            value.operator_present=true;
	                            value.operator=command_array.get(i)[1];
	                            check_obj.add(value);
	                        }
	                    }
	                    else
	                    {
	                        System.out.println(value.output_error);
	                        return null;
	                    }
	                }
	                else
	                {
	                    temp_obj obj=new temp_obj();
	                    obj.output=0;
	                    obj.output_error="Error two or more realational operators present in single statement....";
	                    return obj;
	                }
	            }
	            temp_obj final_obj=arithmetic_condition_checking(check_obj);
	            return final_obj;
	        }
	        else
	        {
	            temp_obj obj=new temp_obj();
	            obj.output=0;
	            obj.output_error="Given statement is invalid due to no operation....";
	            return obj;
	        }
	    }
	    public static List<String[]> arithmetic_devide(String command)
	    {
	        try
	        {
	            {
	                int inner_check=check_inner(command);
	                if(inner_check==4 || inner_check==3 || inner_check==0)
	                {
	                    command="("+command+")";
	                }
	                if(check_inner(command)!=2)
	                    return null;
	            }
	            String operators_list[]={"+","*","-","/","%"};
	            List<String> divided_list=Arrays.asList(operators_list);
	            int second=1;
	            boolean opened=false;
	            int inner=0;
	            boolean in=false;
	            command=remove_spaces(command);
	            String command2=command;
	            int openindex=-1,closeindex=-1;
	            List<String[]> command_array=new ArrayList<String[]>();
	            for(int i=1;i<command.length()-1;i++)
	            {
	                if(command.charAt(i)=='\'' && in==false)
	                    in=true;
	                else if(command.charAt(i)=='\'' && in==true)
	                    in=false;
	                if( in==false && opened==false && divided_list.contains(command.charAt(i)+""))
	                {
	                    String temp[]=new String[2];
	                    temp[0]=command.substring(second,i);
	                    temp[1]=command.charAt(i)+"";
	                    command_array.add(temp);
	                    second=i+1;
	                    openindex=-1;
	                    closeindex=-1;
	                }
	                else if(command.charAt(i)=='(' && opened==false && in==false)
	                {
	                    openindex=i;
	                    closeindex=-1;
	                    opened=true;
	                }
	                else if(command.charAt(i)=='(' && opened==true && in==false)
	                {
	                    inner++;
	                }
	                else if(command.charAt(i)==')' && opened==true && inner==0 && in==false)
	                {
	                    closeindex=i+1;
	                    openindex=-1;
	                    opened=false;
	                }
	                 else if(command.charAt(i)==')' && opened==true && inner!=0 && in==false)
	                {
	                    inner--;
	                }
	            }
	            String temp[]=new String[2];
	            if(openindex!=-1 && closeindex!=-1)
	                temp[0]=command.substring(openindex,closeindex);
	            else
	                temp[0]=command.substring(second,command.length()-1);
	            command_array.add(temp);
	            return command_array;
	        }
	        catch(Exception e)
	        {
	            System.out.println("Error found in "+command+"....");
	            return null;
	        }
	    }
	    public static boolean testing(String command)
	    {
	        if(main_error)
	            return false;
	        List<String[]> command_array=getcommands(command);
	        String check_string="";
	        if(command_array==null || command_array.size()==0)
	        {
	            System.out.println("Error in condition..."+command);
	            if(inner_testing_enter)
	            {
	                inner_testing_enter=false;
	                inner_testing_error=true;
	            }
	            else
	            {
	                main_error=true;
	            }
	            return false;
	        }
	        if(command_array.size()==1 && command.equals(command_array.get(0)[0]))
	        {
	            boolean value=evaluation_function(command_array.get(0)[0]);
	            check_string=check_string+value;
	            return condition_checking(check_string);
	        }
	        for(int i=0;i<command_array.size();i++)
	        {
	            List<String[]> command_array1=getcommands(command_array.get(i)[0]);
	            if(command_array1==null||command_array1.size()==0)
	            {
	                System.out.println("Error in sub condition...."+command_array.get(i)[0]);
	                if(inner_testing_enter)
	                {
	                    inner_testing_enter=false;
	                    inner_testing_error=true;
	                }
	                else
	                {
	                    main_error=true;
	                }
	                return false;
	            }
	            if(command_array1.size()==1)
	            {
	                if(command_array.get(i)[0].equals(command_array1.get(0)[0]))
	                {
	                    boolean value=evaluation_function(command_array.get(i)[0]);
	                    if(i==command_array.size()-1)
	                        check_string=check_string+value;
	                    else
	                        check_string=check_string+value+command_array.get(i)[1];
	                }
	                else
	                {
	                    String substring="";
	                    boolean value=testing(command_array1.get(0)[0]);
	                    if(i==command_array.size()-1)
	                        check_string=check_string+condition_checking(""+value);
	                    else
	                        check_string=check_string+condition_checking(""+value)+command_array.get(i)[1];
	                }
	            }
	            else
	            {
	                String substring="";
	                for(int j=0;j<command_array1.size();j++)
	                {
	                    boolean value=testing(command_array1.get(j)[0]);
	                    if(j==command_array1.size()-1)
	                        substring=substring+value;
	                    else
	                        substring=substring+value+command_array1.get(j)[1];
	                }
	                if(i==command_array.size()-1)
	                    check_string=check_string+condition_checking(substring);
	                else
	                    check_string=check_string+condition_checking(substring)+command_array.get(i)[1];
	            }
	        }
	        return condition_checking(check_string);
	    }
	    public static List<String[]> getcommands(String command)
	    {
	        try
	        {
	            command=remove_spaces(command);
	            {
	                int inner_check=check_inner(command);
	                if(inner_check==4 || inner_check==3 || inner_check==0)
	                {
	                    command="("+command+")";
	                }
	                if(check_inner(command)!=2)
	                    return null;
	            }
	            List<String[]> command_array=new ArrayList<String[]>();
	            int second=1;
	            boolean opened=false;
	            int inner=0;
	            boolean in=false;
	            String command2=command;
	            String list[]={"&&","||"};
	            List<String> conditional= Arrays.asList(list);
	            int openindex=-1,closeindex=-1;
	            for(int i=1;i<command.length()-1;i++)
	            {
	                if(command.charAt(i)=='\'' && in==false)
	                    in=true;
	                else if(command.charAt(i)=='\'' && in==true)
	                    in=false;
	                if(conditional.contains(command.charAt(i-1)+""+command.charAt(i)) && in==false && opened==false)
	                {
	                    String temp[]=new String[2];
	                    temp[0]=command.substring(second,i-1);
	                    temp[1]=command.charAt(i-1)+""+command.charAt(i);
	                    command_array.add(temp);
	                    second=i+1;
	                    openindex=-1;
	                    closeindex=-1;
	                }
	                else if(command.charAt(i)=='(' && opened==false && in==false)
	                {
	                    openindex=i;
	                    closeindex=-1;
	                    opened=true;
	                }
	                else if(command.charAt(i)=='(' && opened==true && in==false)
	                {
	                    inner++;
	                }
	                else if(command.charAt(i)==')' && opened==true && inner==0 && in==false)
	                {
	                    closeindex=i+1;
	                    openindex=-1;
	                    opened=false;
	                }
	                 else if(command.charAt(i)==')' && opened==true && inner!=0 && in==false)
	                {
	                    inner--;
	                }

	            }
	            String temp[]=new String[2];
	            if(openindex!=-1 && closeindex!=-1)
	                temp[0]=command.substring(openindex,closeindex);
	            else
	                temp[0]=command.substring(second,command.length()-1);
	            command_array.add(temp);
	            return command_array;
	        }
	        catch(Exception e)
	        {
	            System.out.println("Error found in "+command+"....");
	            return null;
	        }
	    }
	    public static temp_obj relational_testing(String command)
	    {
	        List<temp_obj> check_obj=new ArrayList<temp_obj>();
	        List<String[]> command_array=devide(command);
	        if(command_array==null||command_array.size()==0)
	        {
	            temp_obj obj=new temp_obj();
	            obj.output=0;
	            obj.output_error="Error due to no statement";
	            return obj;
	        }
	        else if(command_array.size()==1 || command_array.size()==2)
	        {
	            if(command_array.size()==1 && command.equals(command_array.get(0)[0]))
	            {
	                temp_obj obj=function(command_array.get(0)[0]);
	                if(obj==null)
	                {
	                    return null;
	                }
	                if(obj.output!=0)
	                {
	                    check_obj.add(obj);
	                    return obj;
	                }
	                else
	                {
	                    System.out.println(obj.output_error);
	                    return null;
	                }
	            }
	            else if(command_array.size()==1 && !command.equals(command_array.get(0)[0]))
	            {
	                temp_obj value=relational_testing(command_array.get(0)[0]);
	                if(value==null)
	                {
	                    return null;
	                }
	                if(value.output!=0)
	                {
	                    check_obj.add(value);
	                    return relational_condition_checking(check_obj);
	                }
	                else
	                {
	                    System.out.println(value.output_error);
	                    return null;
	                }
	            }
	            for(int i=0;i<command_array.size();i++)
	            {
	                if(((command_array.get(i)[0]).trim()).isEmpty())
	                {
	                    temp_obj obj=new temp_obj();
	                    obj.output=0;
	                    obj.output_error="Error due to no statement beside relational operator....";
	                    return obj;
	                }
	                List<String[]> command_array1=devide(command_array.get(i)[0]);
	                if(command_array1.size()==0 || command_array1==null)
	                {
	                    temp_obj obj=new temp_obj();
	                    obj.output=0;
	                    obj.output_error="Error due to no statement....";
	                    return obj;
	                }
	                if(command_array1.size()==1)
	                {
	                    if(((command_array1.get(0)[0]).trim()).isEmpty())
	                    {
	                        temp_obj obj=new temp_obj();
	                        obj.output=0;
	                        obj.output_error="Error due to no statement beside realational operator....";
	                        return obj;
	                    }
	                    if(command_array.get(i)[0].equals(command_array1.get(0)[0]))
	                    {
	                        temp_obj value=function(command_array.get(i)[0]);
	                        if(value==null)
	                        {
	                            return null;
	                        }
	                        if(value.output!=0)
	                        {
	                            if(i==command_array.size()-1)
	                                check_obj.add(value);
	                            else
	                            {
	                                value.operator_present=true;
	                                value.operator=command_array.get(0)[1];
	                                check_obj.add(value);
	                            }
	                        }
	                        else
	                        {
	                            System.out.println(value.output_error);
	                            return null;
	                        }
	                    }
	                    else
	                    {
	                        temp_obj value=relational_testing(command_array1.get(0)[0]);
	                        if(value==null)
	                        {
	                            return null;
	                        }
	                        if(value.output!=0)
	                        {
	                            if(i==command_array.size()-1)
	                                check_obj.add(value);
	                            else
	                            {
	                                value.operator_present=true;
	                                value.operator=command_array.get(i)[1];
	                                check_obj.add(value);
	                            }
	                        }
	                        else
	                        {
	                            System.out.println(value.output_error);
	                            return null;
	                        }
	                    }
	                }
	                else if(command_array1.size()==2)
	                {
	                    List<temp_obj> sub_check_obj=new ArrayList<temp_obj>();
	                    for(int j=0;j<command_array1.size();j++)
	                    {
	                        if(((command_array1.get(j)[0]).trim()).isEmpty())
	                        {
	                            temp_obj obj=new temp_obj();
	                            obj.output=0;
	                            obj.output_error="Error due to no statement beside realational operator....";
	                            return obj;
	                        }
	                        temp_obj value=relational_testing(command_array1.get(j)[0]);
	                        if(value==null)
	                        {
	                            return null;
	                        }
	                        if(value.output!=0)
	                        {
	                            if(j==command_array1.size()-1)
	                                sub_check_obj.add(value);
	                            else
	                            {
	                                value.operator_present=true;
	                                value.operator=command_array1.get(j)[1];
	                                sub_check_obj.add(value);
	                            }
	                        }
	                        else
	                        {
	                            System.out.println(value.output_error);
	                            return null;
	                        }
	                    }
	                    temp_obj value=relational_condition_checking(sub_check_obj);
	                    if(value==null)
	                    {
	                        return null;
	                    }
	                    if(value.output!=0)
	                    {
	                        if(i==command_array.size()-1)
	                            check_obj.add(value);
	                        else
	                        {
	                            value.operator_present=true;
	                            value.operator=command_array.get(i)[1];
	                            check_obj.add(value);
	                        }
	                    }
	                    else
	                    {
	                        System.out.println(value.output_error);
	                        return null;
	                    }
	                }
	                else
	                {
	                    temp_obj obj=new temp_obj();
	                    obj.output=0;
	                    obj.output_error="Error two or more realational operators present in single statement....";
	                    return obj;
	                }
	            }
	            temp_obj pinal_obj=relational_condition_checking(check_obj);
	            return pinal_obj;
	        }
	        else
	        {
	            System.out.println("Error two or more realational operators present in single statement....");
	            return null;
	        }
	    }
	    public static List<String[]> devide(String command)
	    {
	        try
	        {
	            {
	                int inner_check=check_inner(command);
	                if(inner_check==4 || inner_check==3 || inner_check==0)
	                {
	                    command="("+command+")";
	                }
	                if(check_inner(command)!=2)
	                    return null;
	            }
	            String operators_list[]={"==","!=","<<",">>","<=",">="};
	            List<String> divided_list=Arrays.asList(operators_list);
	            int second=1;
	            boolean opened=false;
	            int inner=0;
	            boolean in=false;
	            command=remove_spaces(command);
	            String command2=command;
	            int openindex=-1,closeindex=-1;
	            List<String[]> command_array=new ArrayList<String[]>();
	            for(int i=1;i<command.length()-1;i++)
	            {
	                if(command.charAt(i)=='\'' && in==false)
	                    in=true;
	                else if(command.charAt(i)=='\'' && in==true)
	                    in=false;
	                if(divided_list.contains(command.charAt(i-1)+""+command.charAt(i))&& in==false && opened==false)
	                {
	                    String temp[]=new String[2];
	                    temp[0]=command.substring(second,i-1);
	                    temp[1]=command.charAt(i-1)+""+command.charAt(i);
	                    command_array.add(temp);
	                    second=i+1;
	                    openindex=-1;
	                    closeindex=-1;
	                }
	                else if(command.charAt(i)=='(' && opened==false && in==false)
	                {
	                    openindex=i;
	                    closeindex=-1;
	                    opened=true;
	                }
	                else if(command.charAt(i)=='(' && opened==true && in==false)
	                {
	                    inner++;
	                }
	                else if(command.charAt(i)==')' && opened==true && inner==0 && in==false)
	                {
	                    closeindex=i+1;
	                    openindex=-1;
	                    opened=false;
	                }
	                 else if(command.charAt(i)==')' && opened==true && inner!=0 && in==false)
	                {
	                    inner--;
	                }
	            }
	            String temp[]=new String[2];
	            if(openindex!=-1 && closeindex!=-1)
	                temp[0]=command.substring(openindex,closeindex);
	            else
	                temp[0]=command.substring(second,command.length()-1);
	            command_array.add(temp);
	            return command_array;
	        }
	        catch(Exception e)
	        {
	            System.out.println("Error found in "+command+"....");
	            return null;
	        }
	    }
	    public static int check_inner(String command)
	    {
	        int inner=0,m=0;
	        boolean opened=false,in=false;
	        if(command.charAt(0)!='(')
	            return 0;
	        opened=false;
	        for(int i=1;i<command.length();i++)
	        {
	            if(command.charAt(i)=='\'')
	            {
	                if(!in)
	                    in=true;
	                else
	                    in=false;
	            }
	            else if(!in && command.charAt(i)=='(')
	            {
	                if(opened==false)
	                {
	                    m++;
	                    opened=true;
	                }
	                else if(opened==true)
	                {
	                    inner++;
	                }
	            }
	            else if(!in && command.charAt(i)==')')
	            {
	                if(opened==true)
	                {
	                    if(inner==0)
	                    {
	                        m++;
	                        opened=false;
	                    }
	                    if(inner>0)
	                    {
	                        inner--;
	                    }
	                }
	                else
	                {
	                    if(i!=command.length()-1)
	                        return 4;
	                    else
	                        return 2;
	                }
	            }
	        }
	        if(opened==false && inner==0 && !in)
	        {
	            if(m%2==1)
	                return 2;
	            else
	                return 1;
	        }
	        else
	        {
	            return 3;
	        }
	    }
	    public static String remove_spaces(String command)
	    {
	        int index=0;
	        boolean in=false,out=true;
	        for(int i=1;i<command.length()-1;)
	        {
	            if((i==1 && in==false &&  command.charAt(i)=='\'') || (in==false && command.charAt(i)=='\''))
	            {
	                in=true;
	                out=false;
	                i++;
	                continue;
	            }
	            else if((i==1 && in==true &&  command.charAt(i)=='\'') || (in==true &&  command.charAt(i)=='\''))
	            {
	                in=false;
	                out=true;
	                i++;
	                continue;
	            }
	            if(in==false && command.charAt(i)==' ' || command.charAt(i)=='\t')
	            {
	                command=command.substring(0,i)+command.substring(i+1,command.length());
	                continue;
	            }
	            i++;
	        }
	        return command;
	    }
}
class temp_obj
{
    /*
    output=
        0 for error
        1 for string
        2 int
        3 double
        4 boolean
    */
    int output=-1;//0
    String output_error=null;//1
    String output_string=null;//2
    int output_int=0;//3
    double output_double=0.0;//4
    boolean output_boolean=false;//5
    boolean operator_present=false;
    String operator="";
    public temp_obj()
    {

    }
}