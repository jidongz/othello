package com.othello.component;

public class GameBoard {
	private int rows;
	private int columns;
	private BoardSquare[][] boardSquares;

	public static class BoardSquare {
		private GameConstants.SquareColor squareColor;
		private int x;
		private int y;

		public BoardSquare(int row, int col, GameConstants.SquareColor squareColor) {
			this.x = row;
			this.y = col;
			this.squareColor = squareColor;
		}

		public void clear() {
			setFieldStatus(GameConstants.SquareColor.EMPTY);
		}

		public GameConstants.SquareColor getSquareColor() {
			return squareColor;
		}

		public void setFieldStatus(GameConstants.SquareColor squareColor) {
			this.squareColor = squareColor;
		}

		public int getX() {
			return x;
		}

		public void setX(int x) {
			this.x = x;
		}

		public int getY() {
			return y;
		}

		public void setY(int y) {
			this.y = y;
		}
	}

	public GameBoard(int maxRow, int maxCol) {
		this.rows = maxRow;
		this.columns = maxCol;
	}

	public void boardSetup() {
		if (rows <= 0 || columns <= 0) {
			return;
		}

		boardSquares = new BoardSquare[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				boardSquares[i][j] = new BoardSquare(i, j, GameConstants.SquareColor.EMPTY);
			}
		}
		boardSquares[rows / 2 - 1][columns / 2 - 1].setFieldStatus(GameConstants.SquareColor.WHITE);
		boardSquares[rows / 2 - 1][columns / 2].setFieldStatus(GameConstants.SquareColor.BLACK);
		boardSquares[rows / 2][columns / 2 - 1].setFieldStatus(GameConstants.SquareColor.BLACK);
		boardSquares[rows / 2][columns / 2].setFieldStatus(GameConstants.SquareColor.WHITE);
	}

	public void printBoard() {
		if (boardSquares == null) {
			return;
		}
		StringBuffer bottomLine = new StringBuffer("");
		for (int i = 0; i < rows; i++) {
			System.out.print((i + 1) + " ");
			for (int j = 0; j < columns; j++) {
				System.out.print(printSquare(boardSquares[i][j]));
				if (i == rows - 1) {
					bottomLine.append(" ").append((char) (j + 97)).append(" ");
				}
			}
			System.out.println("");
			if (i == rows - 1) {
				System.out.print("  " + bottomLine.toString());
			}
		}
		System.out.println("");
	}

	public String printSquare(BoardSquare square) {
		switch (square.getSquareColor()) {
		case WHITE:
			return " O ";
		case BLACK:
			return " X ";
		case EMPTY:
			return " - ";
		}
		return null;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}

	public BoardSquare[][] board() {
		return boardSquares;
	}

	public BoardSquare[][] getBoardSquares() {
		return boardSquares;
	}

	public void setBoardSquares(BoardSquare[][] boardSquares) {
		this.boardSquares = boardSquares;
	}

	public GameBoard deepCopy() {
		BoardSquare[][] copy = new BoardSquare[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				copy[i][j] = new BoardSquare(i, j, boardSquares[i][j].getSquareColor());
			}
		}
		GameBoard board = new GameBoard(rows, columns);
		board.setBoardSquares(copy);
		return board;
	}
}
