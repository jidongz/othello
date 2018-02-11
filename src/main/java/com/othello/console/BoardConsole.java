package com.othello.console;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.othello.component.GameBoard;
import com.othello.component.GameConstants;
import com.othello.component.Square;

public class BoardConsole {

	private static Scanner playerMove = new Scanner(System.in);
	private GameBoard board;
	private GameConstants.SquareColor currentPlayer;
	private GameConstants.GameStatus currentState;
	private ArrayList<Square> directions;
	private int globalCounter;
	private HashMap<Square, String> availableMoves;

	public BoardConsole(GameBoard board, GameConstants.SquareColor currentPlayer) {
		this.board = board;
		this.currentPlayer = currentPlayer;
	}

	private void initBoard() {
		board.boardSetup();
		board.printBoard();
		currentState = GameConstants.GameStatus.PLAYING;
		globalCounter = 0;
		setStaticDirections();
	}

	public void playGame(GameConstants.SquareColor playPiece) {
		initBoard();
		currentPlayer = playPiece;
		while (currentState == GameConstants.GameStatus.PLAYING) {
			availableMoves = this.getAvailableMoves(currentPlayer);
			if (availableMoves.size() != 0) {
				System.out.println("Current Player - " + (currentPlayer == GameConstants.SquareColor.BLACK ? "X" : "O")
						+ ", Please make a move:");
				System.out.println("");
				int[] inputs = consoleInputParser(playerMove);
				if (inputs == null) {
					System.out.println("invalid input, please retry with [1-8]{1}[a-h]{1}");
					continue;
				}
				if (setMove(inputs[0], inputs[1], currentPlayer)) {
					currentPlayer = GameConstants.getOppositePlayer(currentPlayer);
					board.printBoard();
				} else {
					continue;
				}
			}
			if (isGameOver(currentPlayer)) {
				currentState = GameConstants.GameStatus.OVER;
				break;
			}
		}
	}

	public int[] consoleInputParser(Scanner in) {
		int[] inputs = new int[2];
		if (in.hasNext()) {
			String[] temp = inputCheck(in.next());
			if (temp == null) {
				return null;
			}
			inputs[0] = Integer.parseInt(temp[0]) - 1;
			inputs[1] = (int) (temp[1].toCharArray())[0] - 97;
		}
		return inputs;
	}

	private String[] inputCheck(String next) {
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

	private ArrayList<Square> setStaticDirections() {
		directions = new ArrayList<Square>(8);
		directions.add(new Square(1, 1));
		directions.add(new Square(1, 0));
		directions.add(new Square(1, -1));
		directions.add(new Square(0, -1));
		directions.add(new Square(-1, -1));
		directions.add(new Square(-1, 0));
		directions.add(new Square(-1, 1));
		directions.add(new Square(0, 1));
		return directions;
	}

	private HashMap<Square, String> getAvailableMoves(GameConstants.SquareColor playPiece) {
		HashMap<Square, String> availableMoves = new HashMap<Square, String>();
		int maxRow = board.getRows() - 1;
		int maxCol = board.getColumns() - 1;

		for (int row = 0; row <= maxRow; row++) {
			for (int col = 0; col <= maxCol; col++) {
				if (board.getBoardSquares()[row][col].getSquareColor() == playPiece) {
					for (int dir = 0; dir < 8; dir++) {
						globalCounter = 1;
						Square direction = directions.get(dir);
						int boundRow = row - direction.getX();
						int boundCol = col - direction.getY();
						if (!((boundRow < 0 || boundRow > maxRow) || (boundCol < 0 || boundCol > maxCol))) {
							if (isValid(boundRow, boundCol, dir, playPiece)) {
								availableMoves.put(new Square(row - globalCounter * direction.getX(),
										col - globalCounter * direction.getY(), dir), null);
							}
						}
					}
				}
			}
		}
		return availableMoves;
	}

	private boolean isValid(int row, int col, int dir, GameConstants.SquareColor playPiece) {
		int maxRow = board.getRows() - 1;
		int maxCol = board.getColumns() - 1;

		globalCounter++;
		if (board.getBoardSquares()[row][col].getSquareColor() == GameConstants.SquareColor.EMPTY) {
			return false;
		}
		if (board.getBoardSquares()[row][col].getSquareColor() == playPiece) {
			return false;
		} else {
			Square direction = directions.get(dir);
			int boundRow = row - direction.getX();
			int boundCol = col - direction.getY();
			if (!((boundRow < 0 || boundRow > maxRow) || (boundCol < 0 || boundCol > maxCol))) {
				if (board.getBoardSquares()[boundRow][boundCol].getSquareColor() == GameConstants.SquareColor.EMPTY) {
					return true;
				} else {
					return isValid(boundRow, boundCol, dir, playPiece);
				}
			} else {
				return false;
			}
		}
	}

	private boolean setMove(int row, int col, GameConstants.SquareColor playPiece) {
		int maxRow = board.getRows() - 1;
		int maxCol = board.getColumns() - 1;

		if ((row < 0 || row > maxRow) || (col < 0 || col > maxCol)) {
			return false;
		}

		if (validMove(row, col, availableMoves)) {
			board.getBoardSquares()[row][col].setFieldStatus(playPiece);
			flipPiece(row, col, playPiece, availableMoves);
			return true;
		} else {
			System.out.println("Invalid move. Please try again.");
			return false;
		}
	}

	private boolean validMove(int row, int col, HashMap<Square, String> availableMoves) {
		if (availableMoves.size() == 0) {
			return false;
		}

		for (int dir = 0; dir < 8; dir++) {
			if (availableMoves.containsKey(new Square(row, col, dir))) {
				return true;
			}
		}
		return false;
	}

	private void flipPiece(int row, int col, GameConstants.SquareColor playPiece,
			HashMap<Square, String> availableMoves) {
		for (int dir = 0; dir < 8; dir++) {
			int changedRow = row;
			int changedCol = col;
			Square direction = directions.get(dir);
			int x = direction.getX();
			int y = direction.getY();
			if (availableMoves.containsKey(new Square(changedRow, changedCol, dir))) {
				while (board.getBoardSquares()[changedRow + x][changedCol + y].getSquareColor() != playPiece) {
					board.getBoardSquares()[changedRow + x][changedCol + y].setFieldStatus(playPiece);
					changedRow += x;
					changedCol += y;
				}
			}
		}
	}

	private boolean isGameOver(GameConstants.SquareColor playPiece) {
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
			System.out.println("Player " + GameConstants.getOppositePlayer(playPiece) + " wins"
					+ (playPiece == GameConstants.SquareColor.BLACK ? "( " + blackCount + " vs " + whiteCount + " )"
							: "( " + whiteCount + " vs " + blackCount + " )"));
			return true;
		}
		return false;
	}
}
