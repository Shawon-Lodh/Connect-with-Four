/**
 * A class that describes a specific example of game parameters
 * @author Dmitriy Stepanov
 */
public class GameParameters {
	public static int gameMode = Constants.PLAYER_VS_AI;
	public static int levelAI1 = 5;
	public static int levelAI2 = 5;
	public static int player1Color = Constants.RED;
	public static int player2Color = Constants.YELLOW;

	public static String getColorNameByNumber(int number) {
		switch (number) {
			case 2:
				return "Yellow";
			case 3:
				return "Black";
			case 4:
				return "Green";
			case 5:
				return "Orange";
			case 6:
				return "Purple";
			default:
				return "Red";
		}
	}
}
