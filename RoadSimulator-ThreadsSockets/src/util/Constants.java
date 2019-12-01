package util;

public class Constants {

	public static final int ROAD_DIMENSION = 20;
	// Car area		->		from index 0 you can turn left, from index 1 you can turn right
	public static final int[] UP_TO_DOWN =    { ROAD_DIMENSION/2 - 6, ROAD_DIMENSION/2 + 4 };		//	4, 14
	public static final int[] RIGHT_TO_LEFT = 	UP_TO_DOWN;											//	4, 14
	public static final int[] DOWN_TO_UP = 	  { ROAD_DIMENSION/2 - 5 , ROAD_DIMENSION/2 + 5 };		//	5, 15
	public static final int[] LEFT_TO_RIGHT = 	DOWN_TO_UP;											//	5, 15
	// Human area
	// Left up, Right up, Left down, Right down - quarters[0,1,2,3]
	public static final int[] HUMAN_TO_LEFT = { RIGHT_TO_LEFT[0] - 2, RIGHT_TO_LEFT[0] - 2, RIGHT_TO_LEFT[1] - 2, RIGHT_TO_LEFT[1] - 2 };	//	2, 2, 12, 12
	public static final int[] HUMAN_TO_DOWN = { UP_TO_DOWN[0] - 2, UP_TO_DOWN[1] - 2, UP_TO_DOWN[0] - 2, UP_TO_DOWN[1] - 2 };				//	2, 12, 2, 12
	public static final int[] HUMAN_TO_RIGHT = { LEFT_TO_RIGHT[0] + 2, LEFT_TO_RIGHT[0] + 2, LEFT_TO_RIGHT[1] + 2, LEFT_TO_RIGHT[1] + 2 };	//	7, 7, 17, 17
	public static final int[] HUMAN_TO_UP = { DOWN_TO_UP[0] + 2, DOWN_TO_UP[1] + 2, DOWN_TO_UP[0] + 2, DOWN_TO_UP[1] + 2 };					//	7, 17, 7, 17
	
	//port RMI registry is listening on.
	public static final int PORT = 2018;
	
	//localhost IP.
	public static final String LOCALHOST = "127.0.0.1";
	
	public static final boolean DEBUG = false;
}
