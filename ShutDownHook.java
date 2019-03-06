public class ShutDownHook extends Thread 
{
 
	@Override
	public void run(){
		System.out.println();
		System.out.println();
		System.out.println("Sorry! your FileBase Application is exiting.");
		int i=0;
		while(i++!=1)
			{
			try
			{
				Thread.sleep(200);
				System.out.print(".");
				Thread.sleep(200);
				System.out.print(".");
				Thread.sleep(200);
				System.out.print(".");
			}
			catch(Exception e)
			{

			}
		}
	}
}