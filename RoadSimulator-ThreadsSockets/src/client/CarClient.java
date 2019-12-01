package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;

import javafx.util.Pair;
import util.Constants;
import util.E_Direction;

public class CarClient extends Thread {
	
	/**
	 * helper variable
	 */
	private static volatile int carsCounter;
	
	/**
	 * car's id
	 */
	private int id;
	
	/**
	 * car status
	 */
	private boolean isFinished;
	
	/**
	 * pair of front cell coordinates: key = row & value = col
	 */
	private Pair<Integer, Integer> front;
	
	/**
	 * pair of back cell coordinates: key = row & value = col
	 */
	private Pair<Integer, Integer> back;
	
	/**
	 * car's direction
	 */
	private E_Direction direction;

	public CarClient(Pair<Integer, Integer> front, Pair<Integer, Integer> back) {
		this.id = ++carsCounter;
		this.front = front;
		this.back = back;
		isFinished = false;
	}

	protected int getCarId() {
		return this.id;
	}
	
	protected void setDirection(E_Direction direction) {
		this.direction = direction;
	}

	/**
	 * 1. Register this entity at server
	 * 2. Send ready msg and update current direction
	 * 3. While this entity on the road:
	 * 		Move entity
	 * 4. Send finish
	 */
	public void run() {
		try {
			boolean bool=false;
			Socket s = new Socket (Constants.LOCALHOST, Constants.PORT);
			DataOutputStream dos = new DataOutputStream(s.getOutputStream());
			DataInputStream dis = new DataInputStream(s.getInputStream());
			bool=sendRegistration(dis, dos);
			if (bool==true)
			{
			this.direction=sendReady(dis, dos);
			while (isFinished==false)
				this.direction=sendMove(dis, dos);
			dos.writeUTF(id + " FIN");
			}
			dos.close();
			dis.close();
			s.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * registration message to send:
	 * 			ID REG CAR FCL:x,y BCL:x,y
	 * @param is
	 * @param os
	 * @return
	 */
	protected boolean sendRegistration(DataInputStream is, DataOutputStream os) {
		try {
			os.writeUTF(id + " REG CAR" + " FCL:" + front.getKey() + "," + front.getValue() + " BCL:" + back.getKey() + "," + back.getValue());
			String str = is.readUTF();
			if (str.equals("ACK REG"))
				return true;
			else if (str.equals("NACK REG"))
				return false;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * ready message to send:
	 * 			ID RDY
	 * @param is
	 * @param os
	 * @return
	 */
	protected E_Direction sendReady(DataInputStream is, DataOutputStream os) {
		try {
			os.writeUTF(id + " RDY");
			String s = is.readUTF();
			if (s.contains("NACK RDY"))
				return null;
			else if (s.contains("ACK RDY"))
			{
				String[] msg = s.split(" ");
				String[] direction = msg[2].split(":");
				E_Direction dir=E_Direction.valueOf(direction[1]);
				if (!(msg[3].contains(",")))
					return dir;
				else
				{
					String[] pdr=msg[3].split(":");
					String[] possibleDirections = pdr[1].split(",");
					dir=E_Direction.valueOf(possibleDirections[0]);
					E_Direction dir2=E_Direction.valueOf(possibleDirections[1]);
					Random random = new Random();
					int value = random.nextInt(2);
					if (value==1)
						return dir;
					else
						return dir2;
				}		
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * move message to send:
	 * 			ID MOV DIR:direction
	 * @param is
	 * @param os
	 * @return
	 */
	protected E_Direction sendMove(DataInputStream is, DataOutputStream os) {
		try {
			os.writeUTF(id + " MOV DIR:" + direction);
			String s=is.readUTF();
			if (s.contains("NACK MOV"))
				return null;
			else if (s.contains("ACK MOV"))
			{
			   if (s.contains("YES"))
			   {
				   isFinished=true;
				   return null;
			   }
			   else if (s.contains("NO"))
			   {
				   String[] msg = s.split(" ");
				   String[] pdr = msg[3].split(":");
				   if (!(pdr[1].contains(",")))
						return E_Direction.valueOf(pdr[1]);
				   else
				   {
					    String[] possibleDirections = pdr[1].split(",");
					    E_Direction dir=E_Direction.valueOf(possibleDirections[0]);
						E_Direction dir2=E_Direction.valueOf(possibleDirections[1]);
						Random random = new Random();
						int value = random.nextInt(2);
						if (value==1)
							return dir;
						else
							return dir2;
				   }
			   }
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
