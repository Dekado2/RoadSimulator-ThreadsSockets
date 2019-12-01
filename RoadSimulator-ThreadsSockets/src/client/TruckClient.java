package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javafx.util.Pair;

public class TruckClient extends CarClient {

	/**
	 * pair of front cell coordinates: key = row & value = col
	 */
	private Pair<Integer, Integer> front;
	/**
	 * pair of middle cell coordinates: key = row & value = col
	 */
	private Pair<Integer, Integer> middle;
	/**
	 * pair of back cell coordinates: key = row & value = col
	 */
	private Pair<Integer, Integer> back;
	
	public TruckClient(Pair<Integer, Integer> front, Pair<Integer, Integer> middle, Pair<Integer, Integer> back) {
		super(front, back);
		this.front = front;
		this.middle = middle;
		this.back = back;
	}
	
	/**
	 * registration message:
	 * 			ID REG TRUCK FCL:x,y MCL:x,y BCL:x,y
	 */
	@Override
	protected boolean sendRegistration(DataInputStream is, DataOutputStream os) {
		try {
			os.writeUTF(super.getCarId() + " REG TRUCK" + " FCL:" + front.getKey() + "," + front.getValue() + " MCL:" +  middle.getKey() + "," + middle.getValue() + " BCL:" + back.getKey() + "," + back.getValue());
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
	
}
