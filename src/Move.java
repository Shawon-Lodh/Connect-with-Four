/**
 * A class that describes the movement of chips on the board
 * @author Dmitriy Stepanov
 */
public class Move {
	private int row;
	private int column;
	private int value;

	/**
	 * Constructor - create a new move on the board
	 * @see Move#Move()
	 */
	public Move() {}

	/**
	 * Constructor - create a new move on the board
	 * @param row - row of the board
	 * @param col - column of the board
	 * @see Move#Move(int,int)
	 */
	public Move(int row, int col) {
		this.row = row;
		this.column = col;
	}

	/**
	 * Constructor - create a new move on the board
	 * @param value - the value of the element
	 * @see Move#Move(int)
	 */
	public Move(int value) {
		this.value = value;
	}

	/**
	 * Constructor - create a new move on the board
	 * @param row - row of the board
	 * @param col - column of the board
	 * @param value - the value of the element
	 * @see Move#Move(int,int,int)
	 */
	public Move(int row, int col, int value) {
		this.row = row;
		this.column = col;
		this.value = value;
	}
	
	public int getRow() {
		return row;
	}
	public int getColumn() {
		return column;
	}
	public int getValue() {
		return value;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public void setColumn(int col) {
		this.column = col;
	}
	public void setValue(int value) {
		this.value = value;
	}
}
