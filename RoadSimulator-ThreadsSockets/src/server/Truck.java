package server;

public class Truck extends Car {
	
	/**
	 * current cell of middle part
	 */
	private RoadPart middleCell;
	
	public Truck(int id, RoadPart front, RoadPart middle, RoadPart back) {
		super(id, front, back);
		this.middleCell = middle;
		middle.setCurrentEntity(this);
	}
	
	@Override
	protected void removeEntityFromRoad() {
		getFrontCell().removeCurrentEntity();
		setFrontCell(null);
		middleCell.removeCurrentEntity();
		middleCell = null;
		getBackCell().removeCurrentEntity();
		setBackCell(null);
		setDirection(null);
		setIsFinished(true);
	}
	
	@Override
	protected void moveToFrontCell(RoadPart toMove) {
		setIsAfterMove(false);
		toMove.setCurrentEntity(this);
		if (isAfterMove()) {
			getBackCell().removeCurrentEntity();;
			setBackCell(middleCell);
			middleCell = getFrontCell();
			setFrontCell(toMove);
		}
	}

	public RoadPart getMiddleCell() {
		return middleCell;
	}
	
}
