package server;

import java.util.HashSet;

import util.Constants;
import util.E_Direction;

public class RoadPart {

	/**
	 * the cell's row number
	 */
	private int row;

	/**
	 * the cell's column number
	 */
	private int col;

	/**
	 * the current entity in this cell
	 */
	private volatile Entity currentEntity;

	/**
	 * set with all possible directions (For cars and trucks)
	 */
	private HashSet<E_Direction> possibleDirectionsForCars;

	private Object lock = new Object();

	public RoadPart(int row, int col) {
		this.row = row;
		this.col = col;
		initDirections();
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public Entity getCurrentEntity() {
		return currentEntity;
	}

	public HashSet<E_Direction> getPossibleDirections() {
		return this.possibleDirectionsForCars;
	}

	private void initDirections() {
		possibleDirectionsForCars = new HashSet<>();
		for (int i = 0; i < 2; i++) {
			if (row == Constants.LEFT_TO_RIGHT[i])
				possibleDirectionsForCars.add(E_Direction.RIGHT);
			if (row == Constants.RIGHT_TO_LEFT[i])
				possibleDirectionsForCars.add(E_Direction.LEFT);
			if (col == Constants.DOWN_TO_UP[i])
				possibleDirectionsForCars.add(E_Direction.UP);
			if (col == Constants.UP_TO_DOWN[i])
				possibleDirectionsForCars.add(E_Direction.DOWN);
		}
	}

	/**
	 * update currentEntity only when it's became a null
	 * @param toInsert
	 */
	protected synchronized void setCurrentEntity(Entity toInsert) {
		if (currentEntity==null)
		{
			currentEntity=toInsert;
			currentEntity.setIsAfterMove(true);
		}
	}

	/**
	 * update currentEntity to be null
	 */
	protected synchronized void removeCurrentEntity() {
		currentEntity=null;
	}

	/**
	 * calculate the right direction between two cell
	 * @param front
	 * @param back
	 * @return the entity's direction
	 */
	public static E_Direction getDirectionBetweenTwoParts(RoadPart front, RoadPart back) {
		E_Direction toReturn = null;
		if (front.getCol() > back.getCol() && front.getRow() == back.getRow())
			toReturn = E_Direction.RIGHT;
		if (front.getCol() < back.getCol() && front.getRow() == back.getRow())
			toReturn = E_Direction.LEFT;
		if (front.getCol() == back.getCol() && front.getRow() > back.getRow())
			toReturn = E_Direction.DOWN;
		if (front.getCol() == back.getCol() && front.getRow() < back.getRow())
			toReturn = E_Direction.UP;
		return toReturn;
	}

}
