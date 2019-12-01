package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import util.Constants;
import util.E_Direction;

public class RequestHandler extends Thread {

	private Socket socket;

	private Car entity;

	public RequestHandler(Socket socket) {
		this.socket = socket;
	}

	/**
	 * For each message you get - call to relevant method
	 * When you get FIN message - terminate the loop 
	 */
	@Override
	public void run() {
		try (DataInputStream di = new DataInputStream(socket.getInputStream());
			DataOutputStream os = new DataOutputStream(socket.getOutputStream());) {
			boolean bool = false;
			while (bool==false)
			{
				String[] str = di.readUTF().split(" ");
			if (str[1].equals("REG"))
				handleRegister(str, os);
			else if (str[1].equals("RDY"))
				handleReady(str, os);
			else if (str[1].equals("MOV"))
				handleMove(str, os);
			else if (str[1].equals("FIN"))
				if (str[0].equals(entity.getEntityName()))
					bool=true;
			}
			os.close();
			di.close();
			socket.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * registration answer to send:
	 * 			ACK/NACK REG
	 * @param msg
	 * @param os
	 */
	private void handleRegister(String[] msg, DataOutputStream os) {
		try {
				if (msg[0].matches(".*\\d+.*"))
				{
					int id;
					id=Integer.valueOf(msg[0]);
					if (msg[2].equals("CAR"))
					{
					  if (msg[3].contains("FCL:") && msg[4].contains("BCL:"))
					  {
						String[] fclCoords = msg[3].split(":");
						String[] frontCoords =fclCoords[1].split(",");
						String[] bclCoords = msg[4].split(":");
						String[] backCoords = bclCoords[1].split(",");
						RoadPart front = Road.getInstance().road[Integer.valueOf(frontCoords[0])][Integer.valueOf(frontCoords[1])];
						RoadPart back = Road.getInstance().road[Integer.valueOf(backCoords[0])][Integer.valueOf(backCoords[1])];
						entity = new Car (id,front,back);
						if (entity.getEntityName()==null || entity.getFrontCell()==null || entity.getBackCell()==null || entity.getDirection()==null)
							os.writeUTF("NACK REG");
						else
						{
							Road.getInstance().addCar(entity);
							os.writeUTF("ACK REG");
						}
					  }
					  else
						  os.writeUTF("NACK REG");
				    }
			        else if (msg[2].equals("TRUCK"))
			        {
				       if (msg[3].contains("FCL:") && msg[4].contains("MCL:") && msg[5].contains("BCL:"))
				       {
				    	    String[] fclCoords = msg[3].split(":");
							String[] frontCoords =fclCoords[1].split(",");
				    	    String[] mclCoords = msg[4].split(":");
				    	    String[] middleCoords = mclCoords[1].split(",");
				    	    String[] bclCoords = msg[5].split(":");
							String[] backCoords = bclCoords[1].split(",");
							RoadPart front = Road.getInstance().road[Integer.valueOf(frontCoords[0])][Integer.valueOf(frontCoords[1])];
							RoadPart middle = Road.getInstance().road[Integer.valueOf(middleCoords[0])][Integer.valueOf(middleCoords[1])];
							RoadPart back = Road.getInstance().road[Integer.valueOf(backCoords[0])][Integer.valueOf(backCoords[1])];
							entity = new Truck (id,front,middle,back);
							if (entity.getEntityName()==null || entity.getFrontCell()==null || ((Truck)entity).getMiddleCell()==null || entity.getBackCell()==null || entity.getDirection()==null)
								os.writeUTF("NACK REG");
							else
							{
								Road.getInstance().addCar(entity);
								os.writeUTF("ACK REG");
							}
				       }
				       else
				    	   os.writeUTF("NACK REG");
			        }
			        else
			              os.writeUTF("NACK REG");
		       }
			   else
			         os.writeUTF("NACK REG");
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * ready answer to send:
	 * 			ACK/NACK RDY CDR:currDirection PDR:possibleDirections
	 * @param msg
	 * @param os
	 */
	private void handleReady(String[] msg, DataOutputStream os) {
		try {
			if (msg[0].equals(entity.getEntityName()))
			    os.writeUTF("ACK RDY CDR:" + entity.getDirection() + " PDR:" + getPossibleDirections());
			else
		         os.writeUTF("NACK RDY");
	} catch (IOException e) {
		e.printStackTrace();
	}	
	}

	/**
	 * move answer to send:
	 * 			ACK/NACK MOV YES
	 * 			ACK/NACK MOV NO PDR:possibleDirections
	 * @param msg
	 * @param os
	 */
	private void handleMove(String[] msg, DataOutputStream os) {
		try {
			if (msg[0].equals(entity.getEntityName()))
			{
				if (msg[2].contains("DIR:"))
				{
					String[] direction=msg[2].split(":");
					try {
					entity.setDirection(E_Direction.valueOf(direction[1]));
					}
                   catch (IllegalArgumentException i){
                	   os.writeUTF("NACK MOV");
                   }
					entity.moveCar(entity.getDirection());
					if (entity.isFinished()==true)
					{
						entity.setDirection(null);
						os.writeUTF("ACK MOV YES");
					}
					else
						os.writeUTF("ACK MOV NO PDR:" + getPossibleDirections());
				}
				else
					os.writeUTF("NACK MOV");
			}
			else
		         os.writeUTF("NACK MOV");
	} catch (IOException e) {
		e.printStackTrace();
	}	
	}
	
	/**
	 * Get all possible directions of entity front cell, and return them as String, separated by ','
	 * @return
	 */
	private String getPossibleDirections() {
		if (entity.getFrontCell()==null)
			return null;
		else
		{
		  if (entity.getFrontCell().getRow()==Constants.RIGHT_TO_LEFT[0] || entity.getFrontCell().getRow()==Constants.RIGHT_TO_LEFT[0]+10)
		      {
			    if (entity.getFrontCell().getCol()==Constants.DOWN_TO_UP[0] || entity.getFrontCell().getCol()==Constants.DOWN_TO_UP[0]+10)
				    return "UP,LEFT";
			    else if (entity.getFrontCell().getCol()==Constants.UP_TO_DOWN[0] || entity.getFrontCell().getCol()==Constants.UP_TO_DOWN[0]+10)
				    return "LEFT,DOWN";
			    else
				    return "LEFT";
		      }
		  else if (entity.getFrontCell().getRow()==Constants.LEFT_TO_RIGHT[0] || entity.getFrontCell().getRow()==Constants.LEFT_TO_RIGHT[0]+10)
		      {
			    if (entity.getFrontCell().getCol()==Constants.DOWN_TO_UP[1]-10 || entity.getFrontCell().getCol()==Constants.DOWN_TO_UP[1])
				    return "UP,RIGHT";
			    else if (entity.getFrontCell().getCol()==Constants.UP_TO_DOWN[1]-10 || entity.getFrontCell().getCol()==Constants.UP_TO_DOWN[1])
				    return "RIGHT,DOWN";
			    else
				    return "RIGHT";
		      }
		  else if (entity.getFrontCell().getCol()==Constants.UP_TO_DOWN[0] || entity.getFrontCell().getCol()==Constants.UP_TO_DOWN[0]+10)
			  return "DOWN";
		  else if (entity.getFrontCell().getCol()==Constants.DOWN_TO_UP[0] || entity.getFrontCell().getCol()==Constants.DOWN_TO_UP[0]+10)
			  return "UP";
		  else return null;
		}
	}
	
}
