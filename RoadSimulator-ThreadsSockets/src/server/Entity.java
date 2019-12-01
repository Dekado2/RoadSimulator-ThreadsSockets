package server;

import util.E_Direction;

public abstract class Entity {

	/**
	 * entity direction
	 */
	private E_Direction direction;
	
	/**
	* car status
	*/
	private boolean isFinished;
	
	/**
	 * If entity succeeded to move - true, otherwise - false
	 */
	private boolean isAfterMove;

	public E_Direction getDirection() {
		return direction;
	}

	protected void setDirection(E_Direction direction) {
		this.direction = direction;
	}

	public boolean isFinished() {
		return isFinished;
	}

	protected void setIsFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}
	
	public boolean isAfterMove() {
		return isAfterMove;
	}
	
	protected void setIsAfterMove(boolean toSet) {
		this.isAfterMove = toSet;
	}

	public abstract String getEntityName();

	protected abstract RoadPart getFrontCell();

	/**
	 * Moves the entity to given RoadPart (next cell in entity's direction) 
	 * @param toMove - the RoadPart to move in
	 */
	protected abstract void moveToFrontCell(RoadPart toMove);

	/**
	 * When the entity arrives to the end of the road - remove it from road
	 */
	protected abstract void removeEntityFromRoad();

	/**
	 * @return if front cell of entity arrived to the last cell of its road
	 */
	protected abstract boolean isAtEndOfRoad();

	/**
	 * if entity arrived to end of road - remove it,
	 * else - get potential front cell and move there
	 */
	protected void moveDown() {
		if (isAtEndOfRoad()) {
			removeEntityFromRoad();
		} else {
			RoadPart potentialFrontCell = Road.getInstance().road[getFrontCell().getRow() + 1][getFrontCell().getCol()];
			moveToFrontCell(potentialFrontCell);
		}
	}

	/**
	 * if entity arrived to end of road - remove it,
	 * else - get potential front cell and move there
	 */
	protected void moveLeft() {
		if (isAtEndOfRoad()) {
			removeEntityFromRoad();
		} else {
			RoadPart potentialFrontCell = Road.getInstance().road[getFrontCell().getRow()][getFrontCell().getCol() - 1];
			moveToFrontCell(potentialFrontCell);
		}
	}

	/**
	 * if entity arrived to end of road - remove it,
	 * else - get potential front cell and move there
	 */
	protected void moveRight() {
		if (isAtEndOfRoad()) {
			removeEntityFromRoad();
		} else {
			RoadPart potentialFrontCell = Road.getInstance().road[getFrontCell().getRow()][getFrontCell().getCol() + 1];
			moveToFrontCell(potentialFrontCell);
		}
	}

	/**
	 * if entity arrived to end of road - remove it,
	 * else - get potential front cell and move there
	 */
	protected void moveUp() {
		if (isAtEndOfRoad()) {
			removeEntityFromRoad();
		} else {
			RoadPart potentialFrontCell = Road.getInstance().road[getFrontCell().getRow() - 1][getFrontCell().getCol()];
			moveToFrontCell(potentialFrontCell);
		}
	}

}
