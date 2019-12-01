package server;

import util.Constants;
import util.E_Direction;

public class Human extends Entity implements Runnable {

	/**
	 * helper variable
	 */
	private static volatile Character humanCounter = 'A';

	/**
	 * human's name
	 */
	private Character name;
	
	/**
	 * location in 1 of 4 humans' areas - @see Constants#HUMAN_
	 */
	private int quarter;

	/**
	 * human's first (front) cell
	 */
	private RoadPart currCell;

	public Human(RoadPart curr, int quar) {
		this.name = humanCounter++;
		this.currCell = curr;
		this.quarter = quar;
		setDirection(getHumanDirection());
		setIsFinished(false);
		currCell.setCurrentEntity(this);
	}

	/**
	 * @return human's direction according to the relevant road part
	 */
	private E_Direction getHumanDirection() {
		E_Direction toReturn = null;
		if (currCell.getRow() == Constants.HUMAN_TO_RIGHT[quarter] && currCell.getCol() != Constants.HUMAN_TO_UP[quarter])
			toReturn = E_Direction.RIGHT;
		if (currCell.getRow() == Constants.HUMAN_TO_LEFT[quarter] && currCell.getCol() != Constants.HUMAN_TO_DOWN[quarter])
			toReturn = E_Direction.LEFT;
		if (currCell.getCol() == Constants.HUMAN_TO_UP[quarter] && currCell.getRow() != Constants.HUMAN_TO_LEFT[quarter])
			toReturn = E_Direction.UP;
		if (currCell.getCol() == Constants.HUMAN_TO_DOWN[quarter] && currCell.getRow() != Constants.HUMAN_TO_RIGHT[quarter])
			toReturn = E_Direction.DOWN;
		return toReturn;
	}

	public void run() {
		while (!isFinished()) {
			moveHuman();
		}
	}

	/**
	 * after 1 second: accordingly to current direction, move car using move_ methods
	 * @see Entity#moveDown()
	 */
	private void moveHuman() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		switch (getDirection()) {
		case DOWN:
			moveDown();
			break;
		case LEFT:
			moveLeft();
			break;
		case RIGHT:
			moveRight();
			break;
		case UP:
			moveUp();
			break;
		}
	}

	@Override
	public String getEntityName() {
		return name + "";
	}

	@Override
	protected RoadPart getFrontCell() {
		return currCell;
	}

	@Override
	protected void moveToFrontCell(RoadPart toMove) {
		setIsAfterMove(false);
		toMove.setCurrentEntity(this);
		if (isAfterMove()) {
			currCell.removeCurrentEntity();
			currCell = toMove;
		}
	}

	@Override
	protected void removeEntityFromRoad() {
		currCell.removeCurrentEntity();
		setDirection(null);
		setIsFinished(true);
	}

	@Override
	protected boolean isAtEndOfRoad() {
		if (getDirection() == E_Direction.RIGHT && currCell.getCol() == Constants.HUMAN_TO_UP[quarter])
			return true;
		if (getDirection() == E_Direction.DOWN && currCell.getRow() == Constants.HUMAN_TO_RIGHT[quarter])
			return true;
		if (getDirection() == E_Direction.LEFT && currCell.getCol() == Constants.HUMAN_TO_DOWN[quarter])
			return true;
		if (getDirection() == E_Direction.UP && currCell.getRow() == Constants.HUMAN_TO_LEFT[quarter])
			return true;
		return false;
	}

}
