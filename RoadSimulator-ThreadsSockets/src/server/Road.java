package server;

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

import util.Constants;

public class Road {

	/**
	 * 2d road
	 * @see Constants#ROAD_DIMENSION
	 */
	protected RoadPart[][] road;

	/**
	 * list of all humans
	 */
	private ArrayList<Human> humans;

	/**
	 * hashMap of all cars key = ID, value = car
	 */
	private ArrayList<Car> cars;

	/**
	 * singleton instance
	 */
	private static Road instance;

	private Road() {
		humans = new ArrayList<>();
		cars = new ArrayList<>();
		initRoad();
		initHumans();
	}

	public static Road getInstance() {
		if (instance == null)
			instance = new Road();
		return instance;
	}

	/**
	 * initialise only usable cells, all the rest - null
	 */
	private void initRoad() {
		road = new RoadPart[Constants.ROAD_DIMENSION][Constants.ROAD_DIMENSION];
		for (int row = 0; row < Constants.ROAD_DIMENSION; row++)
			for (int col = 0; col < Constants.ROAD_DIMENSION; col++)
				if (isPartOfRoad(row, col) || isHumanPart(row, col))
					road[row][col] = new RoadPart(row, col);
	}

	/**
	 * initializes humans. reads data from humans json file.
	 */
	@SuppressWarnings("unchecked")
	private void initHumans() {
		humans = new ArrayList<>();
		try (InputStream is = Server.class.getResourceAsStream("/humans.json");
				BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
			Iterator<JSONObject> outerIterator = ((JSONArray) new JSONParser().parse(reader)).iterator();
			while (outerIterator.hasNext()) {
				JSONObject obj = (JSONObject) outerIterator.next();
				Object[] f = ((JSONArray) obj.get("front")).toArray();
				RoadPart front = road[toIntExact((long) f[0])][toIntExact((long) f[1])];
				int quarter = ((Long)obj.get("quarter")).intValue();
				humans.add(new Human(front, quarter));
			}
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		System.out.println(LocalTime.now() + " all data fetched from file:\n\n\n");
	}

	/**
	 * print the road accordingly to each cell status - must be printed every second
	 */
	public void printRoad(){
		System.out.println("\nPrinting road\n");
		for (RoadPart[] row : road) {
			for (RoadPart cell : row) {
				if (cell == null)
					System.out.print(" ");
				else if (cell.getCurrentEntity() == null) {
					if (isPartOfRoad(cell.getRow(), cell.getCol()))
						System.out.print("0");
					else if (isHumanPart(cell.getRow(), cell.getCol()))
						System.out.print("O");
				} else
					System.out.print(cell.getCurrentEntity().getEntityName());
			}
			System.out.println();
		}
	}

	/**
	 * @see Constants#HUMAN_TO_DOWN and others
	 * @param row
	 * @param col
	 * @return if given cell belongs to humans road
	 */
	private boolean isHumanPart(int row, int col) {
		for (int i = 0; i < 4; i++) {
			if (row == Constants.HUMAN_TO_LEFT[i] || row == Constants.HUMAN_TO_RIGHT[i])
				if (Constants.HUMAN_TO_DOWN[i] <= col && col <= Constants.HUMAN_TO_UP[i])
					return true;
			if (col == Constants.HUMAN_TO_DOWN[i] || Constants.HUMAN_TO_UP[i] == col)
				if (Constants.HUMAN_TO_LEFT[i] <= row && row <= Constants.HUMAN_TO_RIGHT[i])
					return true;
		}
		return false;
	}

	/**
	 * @see Constants#DOWN_TO_UP and others
	 * @param row
	 * @param col
	 * @return if given cell belongs to vehicles road
	 */
	private boolean isPartOfRoad(int row, int col) {
		if (row == Constants.LEFT_TO_RIGHT[0] || row == Constants.RIGHT_TO_LEFT[0] || row == Constants.LEFT_TO_RIGHT[1]
				|| row == Constants.RIGHT_TO_LEFT[1])
			return true;
		if (col == Constants.UP_TO_DOWN[0] || col == Constants.DOWN_TO_UP[0] || col == Constants.UP_TO_DOWN[1]
				|| col == Constants.DOWN_TO_UP[1])
			return true;
		return false;
	}

	/**
	 * @return true if there is at least one entity on the road
	 */
	public boolean isEntityOnRoad() {
		for (Human h : humans)
			if (!h.isFinished())
				return true;
		for (Car c : cars) {
			if (!c.isFinished())
				return true;
		}
		return false;
	}
	
	/**
	 * starts all humans entities
	 */
	public void startHumans() {
		Thread[] threads = new Thread[humans.size()];
		int i = 0;
		for (Human h : humans)
			threads[i++] = new Thread(h);

		for (Thread t : threads)
			t.start();
	}
	
	/**
	 * add given car to cars
	 * @param toAdd
	 */
	protected void addCar(Car toAdd) {
		cars.add(toAdd);
	}

}
