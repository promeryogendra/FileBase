import java.io.*;
import java.util.*;

public class DataCheck
{
	public DataCheck()
	{

	}
	public boolean dataCheck(String data[],String type[])
	{
		boolean status=true;
		for(int i=0;i<data.length;i++)
		{
			switch(type[i])
			{
				case "int":
					try
					{
						int a=Integer.parseInt(data[i]);
					}
					catch(Exception e)
					{
						status=false;
					}
					break;
				case "string":
					break;
				case "real":
					try
					{
						Double a=Double.parseDouble(data[i]);
					}
					catch(Exception e)
					{
						status=false;
					}
					break;
			}
			if(status==false)
				return false;
		}
		return true;
	}
}