package server;

import util.Constants;
import util.E_Direction;

public class Car extends Entity {
	
	/**
	 * car's id
	 */
	private int id;
	
	/**
	 * car's front (first) RoadPart
	 */
	private RoadPart frontCell;
	
	/**
	 * car's back RoadPart
	 */
	private RoadPart backCell;
	
	public Car(int id, RoadPart front, RoadPart back) {
		this.id = id;
		this.frontCell = front;
		this.backCell = back;
		setDirection(RoadPart.getDirectionBetweenTwoParts(front, back));
		setIsFinished(false);
		front.setCurrentEntity(this);
		back.setCurrentEntity(this);
	}

	protected void setFrontCell(RoadPart frontCell) {
		this.frontCell = frontCell;
	}

	public RoadPart getBackCell() {
		return backCell;
	}

	protected void setBackCell(RoadPart backCell) {
		this.backCell = backCell;
	}

	@Override
	public String getEntityName() {
		return id + "";
	}
	
	@Override
	protected RoadPart getFrontCell() {
		return frontCell;
	}
	
	/**
	 * after 1 second:
	 * accordingly to given direction, move car using move_ methods,
	 * then update current direction
	 * @see Entity#moveDown()
	 * @param direction
	 */
	protected void moveCar(E_Direction direction) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		switch (direction) {
		case DOWN:
			moveDown();
			setDirection(E_Direction.DOWN);
			break;
		case LEFT:
			moveLeft();
			setDirection(E_Direction.LEFT);
			break;
		case RIGHT:
			moveRight();
			setDirection(E_Direction.RIGHT);
			break;
		case UP:
			moveUp();
			setDirection(E_Direction.UP);
			break;
		}
	}
	
	@Override
	protected void removeEntityFromRoad() {
		frontCell.removeCurrentEntity();
		frontCell = null;
		backCell.removeCurrentEntity();
		backCell = null;
		setDirection(null);
		setIsFinished(true);
	}
	
	@Override
	protected void moveToFrontCell(RoadPart toMove) {
		setIsAfterMove(false);
		toMove.setCurrentEntity(this);
		if (isAfterMove()) {
			backCell.removeCurrentEntity();
			backCell = frontCell;
			frontCell = toMove;
		}
	}
	
	@Override
	protected boolean isAtEndOfRoad() {
		if (getDirection() == E_Direction.RIGHT && frontCell.getCol() == (Constants.ROAD_DIMENSION - 1))
			return true;
		if (getDirection() == E_Direction.DOWN && frontCell.getRow() == (Constants.ROAD_DIMENSION - 1))
			return true;
		if (getDirection() == E_Direction.LEFT && frontCell.getCol() == 0)
			return true;
		if (getDirection() == E_Direction.UP && frontCell.getRow() == 0)
			return true;
		return false;
	}
	
	@Override
	public String toString() {
		return "Car [ID=" + id + ", frontCell=" + frontCell + ", backCell=" + backCell + ", direction=" + getDirection()
				+ "]";
	}


}
