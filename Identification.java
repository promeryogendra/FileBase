public class Identification
{
	String keywords[]={"newdb","rmdb","showdb","rmtb","usedb","selecteddb","showtb","cleartb","cleardb","newtb","adddata","showschema","showall","showsome","desc","","exit"};
	public int Identify(String command)
	{
		int i;
		for(i=0;i<keywords.length;i++)
		{
			if(command.compareTo(keywords[i])==0)
			{
				return i;
			}
		}
		return -1;
	}
}