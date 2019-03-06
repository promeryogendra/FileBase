
import java.util.regex.*;
import java.io.*;
import java.util.*;

public class Verification
{
	
	public boolean check_identifier(String identifier)
	{
		if (identifier == null || identifier.length() == 0)
	     {
	        return false;
	     }

	     char[] c = identifier.toCharArray();
	     if (!Character.isJavaIdentifierStart(c[0]))
	     {
	        return false;
	     }

	     for (int i = 1; i < c.length; i++)
	     {
	        if (!Character.isJavaIdentifierPart(c[i]))
	        {
	           return false;
	        }
	     }
	     return true;
	}
	public int verify_newdb(String commands[])
	{
		if(commands.length==2 && commands[0].equals("newdb"))
		{
			if(check_identifier(commands[1])==true)
			{
				return 1;
			}
			else
			{
				return 0;
			}
		}
		else if(commands.length==1)
		{
			return -1;
		}
		else
		{
			return 2;
		}
	}
	public int verify_rmdb(String commands[])
	{
		if(commands.length==2 && commands[0].equals("rmdb"))
		{
			if(check_identifier(commands[1])==true)
			{
				return 1;
			}
			else
			{
				System.out.println("No Such Database Found..");
				return 0;
			}
		}
		else if(commands.length==1)
			return 2;
		return -1;
	}
	public int verify_showdb(String commands[])
	{
		if(commands.length==1 && commands[0].equals("showdb"))
			return 1;
		else
			return 0;
	}
	public int verify_rmtb(String commands[])
	{
		if(commands.length==2 && commands[0].equals("rmtb"))
		{
			if(check_identifier(commands[1])==true)
			{
				return 1;
			}
			else
			{
				System.out.println("No Such Table Exist.....");
				return 0;
			}
		}
		else if(commands.length==1)
		{
			return 2;
		}
		return -1;
	}
	public int verify_usedb(String commands[])
	{
		if(commands.length==2 && commands[0].equals("usedb"))
		{
			if(check_identifier(commands[1])==true)
			{
				return 1;
			}
			else
			{
				return 0;
			}
		}
		else if(commands.length==1)
		{
			return -1;
		}
		else
		{
			return 2;
		}
	}
	public int verify_selecteddb(String commands[])
	{
		if(commands.length==1 && commands[0].equals("selecteddb"))
			return 1;
		else
			return 0;
	}
	public int verify_showtb(String commands[])
	{
		if(commands.length==1 && commands[0].equals("showtb"))
			return 1;
		else
			return 0;
	}
	public int verify_cleartb(String commands[])
	{
		if(commands.length==2 && commands[0].equals("cleartb"))
		{
			if(check_identifier(commands[1])==true)
			{
				return 1;
			}
			else
			{
				System.out.println("No Such Table Found..");
				return 0;
			}
		}
		else if(commands.length==1)
			return 2;
		return -1;
	}
	public int verify_cleardb(String commands[])
	{
		if(commands.length==1 && commands[0].equals("cleardb"))
		{
			return 1;
		}
		return -1;
	}
	public int verify_newtb(String command,String commands[])
	{
		boolean status=Pattern.matches("newtb\\s*[a-zA-Z_]{1}[A-Za-z_0-9]*\\s*\\(([\\s]*[a-zA-Z_]{1}[A-Za-z_0-9]*\\s*(int|string|real)\\s*)(,+([\\s]*[a-zA-Z_]{1}[A-Za-z_0-9]*\\s*(int|string|real)\\s*))*\\)",command);
		String tbname=commands[1];
		if(tbname.contains("("))
		{
	        tbname= tbname.substring(0, tbname.indexOf("(")); 
     	}
		if(status==false && commands[0].equals("newtb") && !check_identifier(tbname))
		{
			return 1;
		}
		else if(status==false && commands[0].equals("newtb") && check_identifier(tbname))
		{
			return 2;
		}
		else if(status)
		{
			return 3;
		}
		else
		{	
			return -1;
		}
	}
	public int verify_adddata(String command,String commands[])
	{
		boolean status=Pattern.matches("adddata\\s+([a-zA-Z_]{1}[a-zA-Z_0-9]*)\\s*\\(\\s*(\\((\\s*'[^']*'\\s*)(,\\s*'[^']*'\\s*)*\\))\\s*(,\\s*(\\((\\s*'[^']*'\\s*)(,\\s*'[^']*'\\s*)*\\)))*\\s*\\)",command);
		String tbname=commands[1];
		if(tbname.contains("("))
		{
	        tbname= tbname.substring(0, tbname.indexOf("("));
     	}
		if(status==false && commands[0].equals("newtb") && !check_identifier(tbname))
		{
			return 1;
		}
		else if(status==false && commands[0].equals("newtb") && check_identifier(tbname))
		{
			return 2;
		}
		else if(status)
		{
			return 3;
		}
		else
		{	
			return -1;
		}
	}
	public int verify_showschema(String commands[])
	{
		if(commands.length==2 && commands[0].equals("showschema"))
		{
			if(check_identifier(commands[1])==true)
			{
				return 1;
			}
			else
			{
				System.out.println("No Such Table Found..");
				return 0;
			}
		}
		else if(commands.length==1)
			return 2;
		return -1;
	}
	public int verify_desc(String commands[])
	{
		if(commands.length==2 && commands[0].equals("desc"))
		{
			if(check_identifier(commands[1])==true)
			{
				return 1;
			}
			else
			{
				System.out.println("No Such Table Exists...");
				return 0;
			}
		}
		else if(commands.length==1)
			return 2;
		return -1;
	}
	public int verify_showall(String command,String commands[])
	{
		boolean status=Pattern.matches("showall\\s+from\\s*\\(\\s*((\\s*[a-zA-Z_]{1}[a-zA-Z_0-9]*\\([a-zA-Z_]{1}[a-zA-Z_0-9]*\\))|([a-zA-Z_]{1}[a-zA-Z_0-9]*\\s*))+\\s*(,\\s*((\\s*[a-zA-Z_]{1}[a-zA-Z_0-9]*\\([a-zA-Z_]{1}[a-zA-Z_0-9]*\\))|([a-zA-Z_]{1}[a-zA-Z_0-9]*\\s*)))*\\s*\\)",command);
		if(status==true)
			return 1;
		else
		{
			status=Pattern.matches("",command);
			System.out.println("Came to showall where condition....");
			//show all if condition cheking
			return 2;
		}
	}
	public int verify_showsome(String command,String commands[])
	{
		boolean status=Pattern.matches("showsome\\s*\\(\\s*(([a-zA-Z_]{1}[a-zA-Z_0-9]*\\s*).([a-zA-Z_]{1}[a-zA-Z_0-9]*\\s*))+\\s*(,\\s*([a-zA-Z_]{1}[a-zA-Z_0-9]*\\s*).([a-zA-Z_]{1}[a-zA-Z_0-9]*\\s*))*\\)\\s*from\\s*\\((((\\s*[a-zA-Z_]{1}[a-zA-Z_0-9]*\\([a-zA-Z_]{1}[a-zA-Z_0-9]*\\))|\\s*[a-zA-Z_]{1}[a-zA-Z_0-9]*)+\\s*(,(\\s*([a-zA-Z_]{1}[a-zA-Z_0-9]*\\([a-zA-Z_]{1}[a-zA-Z_0-9]*\\)\\s*)|(\\s*[a-zA-Z_]{1}[a-zA-Z_0-9]*\\s*)))*)*\\)",command);
		if(status==true)
			return 1;
		else
		{
			status=Pattern.matches("",command);
			System.out.println("Came to showsome where condition....");
			boolean status1=Pattern.matches("showsome\\s*\\(\\s*(([a-zA-Z_]{1}[a-zA-Z_0-9]*\\s*).([a-zA-Z_]{1}[a-zA-Z_0-9]*\\s*))+\\s*(,\\s*([a-zA-Z_]{1}[a-zA-Z_0-9]*\\s*).([a-zA-Z_]{1}[a-zA-Z_0-9]*\\s*))*\\)\\s*from\\s*\\((((\\s*[a-zA-Z_]{1}[a-zA-Z_0-9]*\\([a-zA-Z_]{1}[a-zA-Z_0-9]*\\))|\\s*[a-zA-Z_]{1}[a-zA-Z_0-9]*)+\\s*(,(\\s*([a-zA-Z_]{1}[a-zA-Z_0-9]*\\([a-zA-Z_]{1}[a-zA-Z_0-9]*\\)\\s*)|(\\s*[a-zA-Z_]{1}[a-zA-Z_0-9]*\\s*)))*)*\\)\\s*if\\s*\\(.*\\)\\s*",command);
			if(status1)
			{
				return 3;
			}
			return 2;
		}
	}
}