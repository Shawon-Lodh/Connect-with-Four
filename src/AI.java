import java.util.ArrayList;
import java.util.Random;

/**
 * A class that describes the way artificial intelligence works
 * @author Dmitriy Stepanov
 */
public class AI {
	private int levelAI;
	private int aiPlayer;

	/**
	 * Constructor - creating a new player-computer
	 * @see AI#AI()
	 */
	public AI() {
		levelAI = 4;
		aiPlayer = Constants.PLAYER2;
	}

	/**
	 * Constructor - creating a new player-computer
	 * @param levelAI - the level of intelligence
	 * @param aiLetter - symbol
	 * @see AI#AI(int,int)
	 */
	public AI(int levelAI, int aiLetter) {
		this.levelAI = levelAI;
		this.aiPlayer = aiLetter;
	}
		
	public int getLevelAI() {
			return levelAI;
		}
	public void setLevelAI(int levelAI) {
			this.levelAI = levelAI;
		}
	public int getAiPlayer() {
			return aiPlayer;
		}
	public void setAiPlayer(int aiPlayer) {
			this.aiPlayer = aiPlayer;
		}

	public Move minimax(Board board) {
		if (aiPlayer == Constants.PLAYER1) {
			return max(new Board(board), 0);
		} else {
			return min(new Board(board), 0);
		}
	}
	
	public Move max(Board board, int depth) {
		Random r = new Random();
		if((board.checkForGameOver()) || (depth == levelAI)) {
			return new Move(board.getLastMove().getRow(),
					board.getLastMove().getColumn(), board.evaluate());
		}

		ArrayList<Board> children = new ArrayList<>(board.getChildren(Constants.PLAYER1));
		Move maxMove = new Move(Integer.MIN_VALUE);

		for (Board child : children) {
			Move move = min(child, depth + 1);
			if (move.getValue() >= maxMove.getValue()) {
				if ((move.getValue() == maxMove.getValue())) {
					if (r.nextInt(2) == 0) {
						maxMove.setRow(child.getLastMove().getRow());
						maxMove.setColumn(child.getLastMove().getColumn());
						maxMove.setValue(move.getValue());
					}
				} else {
					maxMove.setRow(child.getLastMove().getRow());
					maxMove.setColumn(child.getLastMove().getColumn());
					maxMove.setValue(move.getValue());
				}
			}
		}
		return maxMove;
	}

	public Move min(Board board, int depth) {
		Random r = new Random();
		if((board.checkForGameOver()) || (depth == levelAI)) {
			return new Move(board.getLastMove().getRow(),
					board.getLastMove().getColumn(), board.evaluate());
		}

		ArrayList<Board> children = new ArrayList<>(board.getChildren(Constants.PLAYER2));
		Move minMove = new Move(Integer.MAX_VALUE);

		for (Board child : children) {
			Move move = max(child, depth + 1);
			if(move.getValue() <= minMove.getValue()) {
				if ((move.getValue() == minMove.getValue())) {
					if (r.nextInt(2) == 0) {
						minMove.setRow(child.getLastMove().getRow());
						minMove.setColumn(child.getLastMove().getColumn());
						minMove.setValue(move.getValue());
					}
				} else {
					minMove.setRow(child.getLastMove().getRow());
					minMove.setColumn(child.getLastMove().getColumn());
					minMove.setValue(move.getValue());
				}
			}
		}
		return minMove;
	}

	public Move miniMaxAlphaBeta(Board board) {
		if (aiPlayer == Constants.PLAYER1) {
			return maxAlphaBeta(new Board(board), 0, Double.MAX_VALUE, Double.MIN_VALUE);
		} else {
			return minAlphaBeta(new Board(board), 0, Double.MIN_VALUE, Double.MAX_VALUE);
		}
	}

	public Move maxAlphaBeta(Board board, int depth, double a, double b) {
		Random r = new Random();
		if((board.checkForGameOver()) || (depth == levelAI)) {
			return new Move(board.getLastMove().getRow(),
					board.getLastMove().getColumn(), board.evaluate());
		}

		ArrayList<Board> children = new ArrayList<>(board.getChildren(Constants.PLAYER1));
		Move maxMove = new Move(Integer.MIN_VALUE);

		for (Board child : children) {
			Move move = minAlphaBeta(child, depth + 1, a, b);
			if (move.getValue() >= maxMove.getValue()) {
				if ((move.getValue() == maxMove.getValue())) {
					if (r.nextInt(2) == 0) {
						maxMove.setRow(child.getLastMove().getRow());
						maxMove.setColumn(child.getLastMove().getColumn());
						maxMove.setValue(move.getValue());
					}
				} else {
					maxMove.setRow(child.getLastMove().getRow());
					maxMove.setColumn(child.getLastMove().getColumn());
					maxMove.setValue(move.getValue());
				}
			}
			if (maxMove.getValue() >= b) {
				return maxMove;
			}

			a = (a > maxMove.getValue()) ? a : maxMove.getValue();
		}
		return maxMove;
	}

	public Move minAlphaBeta(Board board, int depth, double a, double b) {
		Random r = new Random();
		if((board.checkForGameOver()) || (depth == levelAI)) {
			return new Move(board.getLastMove().getRow(),
					board.getLastMove().getColumn(), board.evaluate());
		}

		ArrayList<Board> children = new ArrayList<>(board.getChildren(Constants.PLAYER2));
		Move minMove = new Move(Integer.MAX_VALUE);

		for (Board child : children) {
			Move move = maxAlphaBeta(child, depth + 1, a, b);
			if(move.getValue() <= minMove.getValue()) {
				if ((move.getValue() == minMove.getValue())) {
					if (r.nextInt(2) == 0) {
						minMove.setRow(child.getLastMove().getRow());
						minMove.setColumn(child.getLastMove().getColumn());
						minMove.setValue(move.getValue());
					}
				} else {
					minMove.setRow(child.getLastMove().getRow());
					minMove.setColumn(child.getLastMove().getColumn());
					minMove.setValue(move.getValue());
				}
			}
			if (minMove.getValue() <= a) {
				return minMove;
			}

			b = (b < minMove.getValue()) ? b : minMove.getValue();
		}
		return minMove;
	}
}