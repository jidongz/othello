package com.othello.console;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.othello.component.Coordinate;
import com.othello.component.GameBoard;
import com.othello.component.GameConstants;

public class BoardConsole {
	private GameBoard board;
	private GameConstants.GameStatus currentState;
	private ArrayList<Coordinate> directions;

	public BoardConsole(GameBoard board) {
		this.board = board;
		initBoard();
	}

	private void initBoard() {
		board.boardSetup();
		board.printBoard();
		currentState = GameConstants.GameStatus.PLAYING;
		initStaticDirections();
	}

	public void playGame(GameConstants.SquareColor playPiece) {
		ArrayList<GameBoard> undoBoards = new ArrayList<GameBoard>();
		HashSet<Coordinate> availableMoves;
		while (currentState == GameConstants.GameStatus.PLAYING) {
			availableMoves = getAvailableMoves(playPiece);
			if (availableMoves.size() != 0) {
				System.out.println("Current Player - " + (playPiece == GameConstants.SquareColor.BLACK ? "X" : "O")
						+ ", Please make a move:");
				System.out.println("");
				int[] inputs = consoleInputParser(new Scanner(System.in));
				if (inputs == null) {
					continue;
				}
				if (inputs.length == 1) {
					if (undoMove(undoBoards)) {
						playPiece = GameConstants.getOppositePlayer(playPiece);
					}
					continue;
				}
				if (setMove(inputs[0], inputs[1], playPiece, availableMoves, undoBoards)) {
					playPiece = GameConstants.getOppositePlayer(playPiece);
				} else {
					continue;
				}
			}
			if (isGameOver(playPiece)) {
				currentState = GameConstants.GameStatus.OVER;
				break;
			}
		}
	}

	public int[] consoleInputParser(Scanner in) {
		int[] inputs = null;
		if (in.hasNext()) {
			String[] temp = inputCheck(in.next());
			if (temp == null) {
				System.out.println("invalid input, please retry with [1-8]{1}[a-h]{1}");
				return null;
			}
			if (temp.length == 1) {
				inputs = new int[1];
				inputs[0] = -1;
			} else {
				inputs = new int[2];
				inputs[0] = Integer.parseInt(temp[0]) - 1;
				inputs[1] = (int) (temp[1].toCharArray())[0] - 97;
			}
		}
		return inputs;
	}

	private String[] inputCheck(String next) {
		if (next.length() == 1) {
			String input = next.toString();
			if (input.equals("u")) {
				String[] inputs = new String[1];
				inputs[0] = input;
				return inputs;
			}
		}

		if (next.length() != 2) {
			return null;
		}
		String[] inputs = new String[2];
		inputs[0] = next.substring(0, 1);
		inputs[1] = next.substring(1, 2);

		Pattern pattern_1 = Pattern.compile("[1-8]{1}");
		Pattern pattern_2 = Pattern.compile("[a-h]{1}");

		Matcher matcher_1 = pattern_1.matcher(inputs[0]);
		if (matcher_1.find()) {
			Matcher matcher_2 = pattern_2.matcher(inputs[1]);
			if (matcher_2.find()) {
				return inputs;
			}
		} else {
			matcher_1 = pattern_1.matcher(inputs[1]);
			if (matcher_1.find()) {
				Matcher matcher_2 = pattern_2.matcher(inputs[0]);
				if (matcher_2.find()) {
					String tempStr = inputs[1];
					inputs[1] = inputs[0];
					inputs[0] = tempStr;
					return inputs;
				}
			}
		}
		return null;
	}

	private ArrayList<Coordinate> initStaticDirections() {
		directions = new ArrayList<Coordinate>(8);
		directions.add(new Coordinate(1, 1));
		directions.add(new Coordinate(1, 0));
		directions.add(new Coordinate(1, -1));
		directions.add(new Coordinate(0, -1));
		directions.add(new Coordinate(-1, -1));
		directions.add(new Coordinate(-1, 0));
		directions.add(new Coordinate(-1, 1));
		directions.add(new Coordinate(0, 1));
		return directions;
	}

	public HashSet<Coordinate> getAvailableMoves(GameConstants.SquareColor playPiece) {
		HashSet<Coordinate> availableMoves = new HashSet<Coordinate>();
		int maxRow = board.getRows() - 1;
		int maxCol = board.getColumns() - 1;

		for (int row = 0; row <= maxRow; row++) {
			for (int col = 0; col <= maxCol; col++) {
				if (board.getBoardSquares()[row][col].getSquareColor() == playPiece) {
					for (int dir = 0; dir < 8; dir++) {
						int count = 1;
						Coordinate direction = directions.get(dir);
						int boundRow = row - direction.getX();
						int boundCol = col - direction.getY();
						if (!((boundRow < 0 || boundRow > maxRow) || (boundCol < 0 || boundCol > maxCol))) {
							count = isValid(boundRow, boundCol, dir, playPiece, count);
							if (count != -1) {
								availableMoves.add(new Coordinate(row - count * direction.getX(),
										col - count * direction.getY(), dir));
							}
						}
					}
				}
			}
		}
		return availableMoves;
	}

	private int isValid(int row, int col, int dir, GameConstants.SquareColor playPiece, int count) {
		int maxRow = board.getRows() - 1;
		int maxCol = board.getColumns() - 1;

		count++;
		if (board.getBoardSquares()[row][col].getSquareColor() == GameConstants.SquareColor.EMPTY) {
			return -1;
		}
		if (board.getBoardSquares()[row][col].getSquareColor() == playPiece) {
			return -1;
		} else {
			Coordinate direction = directions.get(dir);
			int boundRow = row - direction.getX();
			int boundCol = col - direction.getY();
			if (!((boundRow < 0 || boundRow > maxRow) || (boundCol < 0 || boundCol > maxCol))) {
				if (board.getBoardSquares()[boundRow][boundCol].getSquareColor() == GameConstants.SquareColor.EMPTY) {
					return count;
				} else {
					return isValid(boundRow, boundCol, dir, playPiece, count);
				}
			} else {
				return -1;
			}
		}
	}

	public boolean setMove(int row, int col, GameConstants.SquareColor playPiece, HashSet<Coordinate> availableMoves,
			ArrayList<GameBoard> undoBoards) {
		int maxRow = board.getRows() - 1;
		int maxCol = board.getColumns() - 1;

		if ((row < 0 || row > maxRow) || (col < 0 || col > maxCol)) {
			return false;
		}

		if (validMove(row, col, availableMoves)) {
			GameBoard tempBoard = board.deepCopy();
			board.getBoardSquares()[row][col].setFieldStatus(playPiece);
			flipPiece(row, col, playPiece, availableMoves);
			undoBoards.add(tempBoard);
			board.printBoard();
			return true;
		} else {
			System.out.println("Invalid move. Please try again.");
			return false;
		}
	}

	private boolean validMove(int row, int col, HashSet<Coordinate> availableMoves) {
		if (availableMoves.size() == 0) {
			return false;
		}

		for (int dir = 0; dir < 8; dir++) {
			if (availableMoves.contains(new Coordinate(row, col, dir))) {
				return true;
			}
		}
		return false;
	}

	private void flipPiece(int row, int col, GameConstants.SquareColor playPiece, HashSet<Coordinate> availableMoves) {
		for (int dir = 0; dir < 8; dir++) {
			int changedRow = row;
			int changedCol = col;
			Coordinate direction = directions.get(dir);
			int x = direction.getX();
			int y = direction.getY();
			if (availableMoves.contains(new Coordinate(changedRow, changedCol, dir))) {
				while (board.getBoardSquares()[changedRow + x][changedCol + y].getSquareColor() != playPiece) {
					board.getBoardSquares()[changedRow + x][changedCol + y].setFieldStatus(playPiece);
					changedRow += x;
					changedCol += y;
				}
			}
		}
	}

	public boolean isGameOver(GameConstants.SquareColor playPiece) {
		if (getAvailableMoves(playPiece).size() == 0) {
			int maxRow = board.getRows() - 1;
			int maxCol = board.getColumns() - 1;
			int blackCount = 0;
			int whiteCount = 0;

			for (int row = 0; row <= maxRow; row++) {
				for (int col = 0; col <= maxCol; col++) {
					if (board.getBoardSquares()[row][col].getSquareColor() == GameConstants.SquareColor.BLACK) {
						blackCount++;
					}
					if (board.getBoardSquares()[row][col].getSquareColor() == GameConstants.SquareColor.WHITE) {
						whiteCount++;
					}
				}
			}
			System.out.println("Player "
					+ (GameConstants.getOppositePlayer(playPiece) == GameConstants.SquareColor.BLACK ? " X wins "
							: " O wins ")
					+ (playPiece == GameConstants.SquareColor.BLACK
							? "( X: " + blackCount + " vs O: " + whiteCount + " )"
							: "( O: " + whiteCount + " vs X: " + blackCount + " )"));
			return true;
		}
		return false;
	}

	public boolean undoMove(ArrayList<GameBoard> undoBoards) {
		GameBoard tempBoard = popUndoBoard(undoBoards);
		if (tempBoard == null) {
			System.out.println("There is nothing to undo!");
			return false;
		}
		board = tempBoard.deepCopy();
		board.printBoard();
		return true;
	}

	public GameBoard popUndoBoard(ArrayList<GameBoard> undoBoards) {
		if (undoBoards == null || undoBoards.size() == 0) {
			return null;
		}
		GameBoard board = undoBoards.get(undoBoards.size() - 1);
		undoBoards.remove(undoBoards.size() - 1);
		return board;
	}
}
