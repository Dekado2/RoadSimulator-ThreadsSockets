package server;

import java.io.IOException;
import java.net.ServerSocket;

import util.Constants;

public class Server {

	
	private static ServerSocket serverS;
	
	/**
	 * when first socket accepted - start human and change flag to false
	 */
	private static boolean flag = true;
	
	public static void main(String[] args) throws InterruptedException {
		
		Road road = Road.getInstance();
		
		try {
			System.out.println("###########################");
			System.out.println("#### Server Side is up ####");
			System.out.println("###########################");
			
			serverS = new ServerSocket(Constants.PORT);
			
			// if debug is false - print map on server side, otherwise print accepted messages
			
			if (!Constants.DEBUG) {
				new Thread(() -> {
					while (flag) {
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					
					boolean toPrint = true;
					while (toPrint) {
						road.printRoad();
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						toPrint = road.isEntityOnRoad();
					}
					
					road.printRoad();
				} ).start();
			}

			// listen to all connection requests
			
			while (true) {
				new RequestHandler(serverS.accept()).start();
				if (flag)
				{
					road.startHumans();
					flag=false;
					Thread.sleep(1000);
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	} 
	
}
