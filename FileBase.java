import java.io.*;
import java.util.*;

public class FileBase
{

	public static boolean selected_database=false;
	public static String selected_database_name=null;
	public static boolean c=false;

	public FileBase()
	{

	}
	public static String trimTrailingBlanks( String str)
	{
	    if( str == null)
	      return null;
	    int len = str.length();
	    for( ; len > 0; len--)
	    {
	      if( ! Character.isWhitespace( str.charAt( len - 1)))
	         break;
	    }
	    return str.substring( 0, len);
	} 

	public static void main(String args[])throws IOException,Exception
	{
		try
		{
			System.out.println("\n\n\nWelcome To FileBase......");
			Scanner scanner = new Scanner(System.in);
			BufferedReader reader =new BufferedReader(new InputStreamReader(System.in));
			ShutDownHook shutDownHook=new ShutDownHook();
			Runtime.getRuntime().addShutdownHook(shutDownHook);
			System.out.print("\n\nDo You Want To Enter Into FileBase (Y/N)   : ");


			String do_you_want_to_continue=scanner.next();
			if(do_you_want_to_continue.equals("Y") || do_you_want_to_continue.equals("y"))
			{	
				System.out.println("\n\n\nFileBase is DataManagementSystem totally based on Files..\nFor using FileBase no other installation is required...\n\n\n");
				String FileBase_Command="";
				System.out.print("fb>>> ");
				while(true)
				{
					String line=reader.readLine();
					if(line.length()!=-1)
					{
						line=line.trim();
						if(line.length()!=-1)
						{
							if(line.length()>0 )
							{
								if( line.charAt(line.length()-1)!=';')
								{
									if(c==true)
										FileBase_Command=trimTrailingBlanks(FileBase_Command)+" "+line+" ";
									else
										FileBase_Command=trimTrailingBlanks(FileBase_Command)+line+" ";
									System.out.print("    > ");
								}
								else
								{
									if(line.equals(";"))
										FileBase_Command=trimTrailingBlanks(FileBase_Command)+line;
									else
										FileBase_Command=FileBase_Command+line;
									break;
								}
							}
							else
							{
								System.out.print("    > ");
								c=true;
							}
						}
						else
						{
							System.out.print("    > ");
							c=true;
						}
					}
					else
					{
						System.out.print("    > ");
						c=true;
					}
				}
				System.out.println(FileBase_Command);
				while(true)
				{
					FileBase_Command=FileBase_Command.trim();
					FileBase_Command=trimTrailingBlanks(FileBase_Command.substring(0,FileBase_Command.length()-1));
					Identification identification=new Identification();
					String commands[]=FileBase_Command.split(" ");
					int chooser=identification.Identify(commands[0]);
					Verification verification;
					int status;
					switch(chooser)
					{
						case 0:
							verification=new Verification();
							commands=FileBase_Command.split("\\s+");
							status=verification.verify_newdb(commands);
							if(status==1)
							{
								NewDatabase newdb=new NewDatabase();
								int result=newdb.createdb(commands[1]);
								switch(result)
								{
									case 1:
										System.out.println("Database created successfully...\n");
										break;
									case -1: 
										System.out.println("Database is already exits....\n");
										break;
								}
							}
							else if(status==0)
							{
								System.out.println("Enter Database Name with Identifier rules...");
							}
							else if(status==-1)
							{
								System.out.println("Enter Database Name to create....");
							}
							else
							{
								System.out.println("Enter command correctly to perform operation...");
							}
							break;
						case 1:
							verification=new Verification();
							commands=FileBase_Command.split("\\s+");
							status=verification.verify_rmdb(commands);
							if(status==1)
							{
								RemoveDatabase rmdb=new RemoveDatabase();
								int result=rmdb.removedb(commands[1]);
								switch(result)
								{
									case 1: 
										if(selected_database && selected_database_name.equals(commands[1]))
										{
											selected_database=false;
											selected_database_name="";
											System.out.println("Database deleted successfully...\n");
										}
										else
										{
											System.out.println("Database deleted successfully...\n");
										}
										break;
									case -1: 
										System.out.println("No Such Database Found...\n");
										break;
								}
							}
							else if(status==2)
								System.out.println("Enter Database Name to Remove....");
							else if(status==-1)
							{
								System.out.println("Enter Command correctly to perform action..");
							}
							break;

						case 2:
								verification=new Verification();
								status=verification.verify_showdb(commands);
								if(status==1)
								{
									ShowDataBase showDataBase =new ShowDataBase();
									ArrayList<String> databases=showDataBase.getDatabases();
									if(databases==null || databases.size()==0)
									{
										System.out.println("Empty....");
									}
									else
									{
										System.out.println("\n*___________________*");
										for(int i=0;i<databases.size();i++)
										{
											System.out.println(databases.get(i));
										}
										System.out.println("\n*___________________*");
									}
								}
								else
								{
									System.out.println("Enter Command correctly to perform action..");
								}
							break;
						case 3:
							verification=new Verification();
							commands=FileBase_Command.split("\\s+");
							status=verification.verify_rmtb(commands);
							if(status==1)
							{
								RemoveTable rmtb=new RemoveTable();
								if(selected_database)
								{
									int result=rmtb.removetb(selected_database_name,commands[1]);
									switch(result)
									{
										case 1: 
											System.out.println("Table deleted successfully...\n");
											break;
										case 2: 
											System.out.println("Can't Delete Table please try again...\n");
											break;
										case 3:
											System.out.println("No Such Table Found...\n");
											break;
										case 4:
											System.out.println("No Database Selected....");
											break;
									}
								}
								else
								{
									System.out.println("No Database Selected....");
								}
							}
							else if(status==2)
								System.out.println("Enter Table Name to Remove....");
							else if(status==-1)
							{
								System.out.println("Enter Command correctly to perform action..");
							}
							break;
						case 4:
							verification=new Verification();
							commands=FileBase_Command.split("\\s+");
							status=verification.verify_usedb(commands);
							if(status==1)
							{
								UseDatabase usedb=new UseDatabase();
								int result=usedb.usedb(commands[1]);
								switch(result)
								{
									case 1:
										if(selected_database)
											System.out.println("Database Changed...\n");
										else
											System.out.println("Database Selected...\n");
										selected_database=true;
										selected_database_name=commands[1];
										break;
									case 2:
										System.out.println("No Such Database Found....\n");
										break;
								}
							}
							else if(status==0)
							{
								System.out.println("Enter Database Name with Identifier rules...");
							}
							else if(status==-1)
							{
								System.out.println("Enter Database Name to create....");
							}
							else
							{
								System.out.println("Enter command correctly to perform operation...");
							}
							break;
						case 5:
							verification=new Verification();
							status=verification.verify_selecteddb(commands);
							if(status==1)
							{
								if(selected_database && selected_database_name!=null)
								{
									System.out.println("\nSelected Database is :-   \" "+selected_database_name+"\"");
								}
								else
								{
									System.out.println("\nNo Database Selected ");
								}
							}
							else
							{
								System.out.println("Enter Command correctly to perform action..");
							}
							break;
						case 6:
							verification=new Verification();
							commands=FileBase_Command.split("\\s+");
							status=verification.verify_showtb(commands);
							if(status==1)
							{
								if(selected_database && selected_database_name!=null)
								{
									ShowTable showTable =new ShowTable();
									ArrayList<String> tables=showTable.getTables(selected_database_name);
									if(tables==null || tables.size()==0)
									{
										System.out.println("Empty.....");
									}
									else
									{
										System.out.println("\n*___________________*");
										for(int i=0;i<tables.size();i++)
										{
											System.out.println(tables.get(i));
										}
										System.out.println("\n*___________________*");
									}
								}
								else
								{
									System.out.println("No Database Selected.....");
								}
							}
							else
							{
								System.out.println("Enter Command correctly to perform action..");
							}
							break;
						case 7:
							verification=new Verification();
							commands=FileBase_Command.split("\\s+");
							status=verification.verify_cleartb(commands);
							if(status==1)
							{
								ClearTable cleartb=new ClearTable();
								if(selected_database)
								{
									int result=cleartb.cleartb(selected_database_name,commands[1]);
									switch(result)
									{
										case 1:
											System.out.println("Table Clear successfully...\n");
											break;
										case 3:
											System.out.println("No Such Table Found...\n");
											break;
										case 4:
											System.out.println("No Database Found....");
											break;
									}
								}
								else
								{
									System.out.println("No Database selected....");
								}
							}
							else if(status==2)
								System.out.println("Enter Table Name to clear....");
							else if(status==-1)
							{
								System.out.println("Enter Command correctly to perform action..");
							}
							break;
						case 8:
							verification=new Verification();
							status=verification.verify_cleardb(commands);
							if(status==1)
							{
								ClearDatabase cleardb=new ClearDatabase();
								if(selected_database)
								{
									int result=cleardb.cleardb(selected_database_name);
									switch(result)
									{
										case 1:
											System.out.println("Database Clear successfully...\n");
											break;
										case 2:
											System.out.println("No Such Database Found...\n");
											break;
										case 3:
											System.out.println("Database is Empty....\n");
											break;
									}
								}
								else
								{
									System.out.println("No Database selected....");
								}
							}
							else
							{
								System.out.println("Enter Command correctly to perform action..");
							}
							break;
						case 9:
							if(selected_database)
							{
								verification=new Verification();
								commands=FileBase_Command.split("\\s+");
								status=verification.verify_newtb(FileBase_Command,commands);
								if(status==1)
								{
									System.out.println("Check table name formate....");
								}
								else if(status==2)
								{
									System.out.println("Check the fields and formate once again....");
								}
								else if(status==3)
								{
									NewTable newtb=new NewTable();
									int result=newtb.newtb(selected_database_name,FileBase_Command,commands[1]);
									switch(result)
									{
										case 1:
											System.out.println("Table successfully created.....");
											break;
										case 6:
											System.out.println("Variable name Should not be data type....");
											break;
										case 7:
											System.out.println("incompatible Data type......");
											break;
									}
								}
								else
								{
									System.out.println("Enter command correctly to perform action......");
								}
							}
							else
							{
								System.out.println("No Database selected....");
							}
							break;
						case 10:
							if(selected_database && selected_database_name!=null)
							{
								verification=new Verification();
								commands=FileBase_Command.split("\\s+");
								status=verification.verify_adddata(FileBase_Command,commands);
								if(status==1)
								{
									System.out.println("No Such Table Exists....");
								}
								else if(status==2)
								{
									System.out.println("Please Check Data Input Formate...");
								}
								else if(status==3)
								{
									AddData addData=new AddData();
									int result=addData.adddata(selected_database_name,FileBase_Command,commands);
									switch(result)
									{
										case 1:
											System.out.println("Data successfully inserted....");
											break;
										case 2:
											System.out.println("Enter data matching with schema....");
											break;
										case -1:
											System.out.println("Database not Found....");
											break;
									}
								}
								else
								{

								}
							}
							else
							{
								System.out.println("No Database Selected....");
							}
							break;
						case 11:
							verification=new Verification();
							commands=FileBase_Command.split("\\s+");
							status=verification.verify_showschema(commands);
							if(status==1)
							{
								ShowSchema showSchema=new ShowSchema();
								if(selected_database)
								{
									int result=showSchema.showSchema(selected_database_name,commands[1]);
									switch(result)
									{
										case 1: 
											System.out.println("No such table found...\n");
											break;
										case 3:
											System.out.println("No schema available please create again...\n");
											break;
									}
								}
								else
								{
									System.out.println("No Database Selected....");
								}
							}
							else if(status==2)
								System.out.println("Enter Table Name to Remove....");
							else if(status==-1)
							{
								System.out.println("Enter Command correctly to perform action..");
							}
							break;
						case 12:
							verification=new Verification();
							commands=FileBase_Command.split("\\s+");
							status=verification.verify_showall(FileBase_Command,commands);
							if(status==1)
							{
								if(selected_database && !selected_database_name.isEmpty())
								{
									ShowAll showAll=new ShowAll();
									int result=showAll.showAll_withoutif(selected_database_name,FileBase_Command,commands);
								}
								else
								{
									System.out.println("No Database Selected....");
								}
							}
							else if(status==2)
							{

							}
							else
							{

							}
							break;
						case 13:
							verification=new Verification();
							commands=FileBase_Command.split("\\s+");
							status=verification.verify_showsome(FileBase_Command,commands);
							if(status==1)
							{
								if(selected_database && !selected_database_name.isEmpty())
								{
									ShowSome showSome=new ShowSome();
									int result=showSome.showSome_withoutif(selected_database_name,FileBase_Command,commands);
									showSome=null;
								}
								else
								{
									System.out.println("No Database Selected....");
								}
							}
							else if(status==3)
							{
								if(selected_database && !selected_database_name.isEmpty())
								{
									ShowSome showSome=new ShowSome();
									int result=showSome.showSome_withif(selected_database_name,FileBase_Command,commands);
									showSome=null;
								}
								else
								{
									System.out.println("No Database Selected....");
								}
							}
							else
							{
								System.out.println("Enter correct showSome command to execute....");
							}
							break;
						case 14:
							verification=new Verification();
							commands=FileBase_Command.split("\\s+");
							status=verification.verify_desc(commands);
							if(status==1)
							{
								DescTable descTable=new DescTable();
								int result=descTable.descTable(selected_database_name,commands[1]);
								switch(result)
								{
									case 3:
										System.out.println("Sorry no schema found...\nManually you done some changes in FileSystem...");
										break;
									case 1:
										System.out.println("No such Table Exists....");
										break;
								}
							}
							else if(status==2)
							{
								System.out.println("Enter Table Name to displaying description...");
							}
							else if(status==-1)
							{
								System.out.println("Enter command correctly to perform action...");
							}
							break;
						case 15:
							
							break;
						case 16:
							System.out.print("\nDo You Want To Exit (Y/N)   : ");

							do_you_want_to_continue=scanner.next();
							if(do_you_want_to_continue.equals("Y") || do_you_want_to_continue.equals("y"))
							{
								System.out.println("FileBase Exiting......\n");
								System.exit(0);
							}	

							break;
						
					}

					if(!selected_database)
						System.out.print("\nfb>>> ");
					else
						System.out.print("\nfb$"+selected_database_name+"$>>> ");
					FileBase_Command="";
					while(true)
					{
						String line=reader.readLine();
						if(line.length()!=-1)
						{
							line=line.trim();
							if(line.length()!=-1)
							{
								if(line.length()>0 )
								{
									if( line.charAt(line.length()-1)!=';')
									{
										if(c==true)
											FileBase_Command=trimTrailingBlanks(FileBase_Command)+" "+line+" ";
										else
											FileBase_Command=trimTrailingBlanks(FileBase_Command)+line+" ";
										System.out.print("    > ");
									}
									else
									{
										if(line.equals(";"))
											FileBase_Command=trimTrailingBlanks(FileBase_Command)+line;
										else
											FileBase_Command=FileBase_Command+line;
										break;
									}
								}
								else
								{
									System.out.print("    > ");
									c=true;
								}
							}
							else
							{
								System.out.print("    > ");
								c=true;
							}
						}
						else
						{
							System.out.print("    > ");
							c=true;
						}
					}
					System.out.println();
				}
			}
			else
				{
					System.out.println("Exiting FileBase......");
				}
			}
			catch(Exception e)
			{
				System.out.println(e);
			}
		}
	}