package client;

import static java.lang.Math.toIntExact;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javafx.util.Pair;

public class Client {

	/**
	 * list of all connected clients
	 */
	private static ArrayList<CarClient> clients;
	
	/**
	 * get Road instance
	 * init cars, and then start them.
	 * start humans instances
	 * print road every second, till there no more entities on the road
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			System.out.println("###########################");
			System.out.println("#### Client Side is up ####");
			System.out.println("###########################");
			initCars();
			for (CarClient vehicle : clients)
			{
				Thread t = new Thread(vehicle);
				t.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * initializes cars. reads data from cars json file.
	 * per each type, create client instance, add it to the list, and then register it
	 * @see CarClient#register()
	 * @see TruckClient#register()
	 */
	@SuppressWarnings("unchecked")
	public static void initCars() {
		clients = new ArrayList<>();
		try (InputStream is = Client.class.getResourceAsStream("/cars.json");
				BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
			Iterator<JSONObject> outerIterator = ((JSONArray) new JSONParser().parse(reader)).iterator();
			while (outerIterator.hasNext()) {
				JSONObject obj = (JSONObject) outerIterator.next();
				Object[] f, m, b;
				Pair<Integer, Integer> front, middle, back;
				switch (((String) obj.get("type"))) {
				case "Car":
					f = ((JSONArray) obj.get("front")).toArray();
					front = new Pair<Integer, Integer>(toIntExact((long) f[0]), toIntExact((long) f[1]));
					b = ((JSONArray) obj.get("back")).toArray();
					back = new Pair<Integer, Integer>(toIntExact((long) b[0]), toIntExact((long) b[1]));
					CarClient c = new CarClient(front, back);
					clients.add(c);
					break;
				case "Truck":
					f = ((JSONArray) obj.get("front")).toArray();
					front = new Pair<Integer, Integer>(toIntExact((long) f[0]), toIntExact((long) f[1]));
					m = ((JSONArray) obj.get("middle")).toArray();
					middle = new Pair<Integer, Integer>(toIntExact((long) m[0]), toIntExact((long) m[1]));
					b = ((JSONArray) obj.get("back")).toArray();
					back = new Pair<Integer, Integer>(toIntExact((long) b[0]), toIntExact((long) b[1]));
					TruckClient t = new TruckClient(front, middle, back);
					clients.add(t);
					break;
				}
				
			}
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		System.out.println(LocalTime.now() + " all data fetched from file:\n\n\n"); 
	}
}
