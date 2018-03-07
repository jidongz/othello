package com.othello.launch;

import com.othello.component.GameBoard;
import com.othello.component.GameConstants;
import com.othello.console.BoardConsole;

public class Launch {

	public static void main(String[] args) {
		GameBoard board = new GameBoard(8, 8);

		BoardConsole boardConsole = new BoardConsole(board);
		boardConsole.playGame(GameConstants.SquareColor.BLACK);

	}

}
