package com.othello.component;

public abstract class GameConstants {

	public enum GameStatus {
		PLAYING, OVER
	}

	public enum SquareColor {
		EMPTY, WHITE, BLACK
	}
	
	public static GameConstants.SquareColor getOppositePlayer(GameConstants.SquareColor currentPlayer) {
		return (currentPlayer == GameConstants.SquareColor.BLACK) ? GameConstants.SquareColor.WHITE
				: GameConstants.SquareColor.BLACK;
	}
}