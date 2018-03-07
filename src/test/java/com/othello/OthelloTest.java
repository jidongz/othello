package com.othello;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

import org.junit.Test;

import com.othello.component.Coordinate;
import com.othello.component.GameBoard;
import com.othello.component.GameConstants;
import com.othello.console.BoardConsole;

import junit.framework.Assert;

public class OthelloTest {

	private GameBoard board;
	private BoardConsole boardConsole;

	public void init() {
		this.board = new GameBoard(8, 8);
		boardConsole = new BoardConsole(board);
	}

	@Test
	public void testGetAvailableMoves() throws Exception {
		init();
		HashSet<Coordinate> availableMoves = boardConsole.getAvailableMoves(GameConstants.SquareColor.BLACK);
		Assert.assertNotNull(availableMoves);
		Assert.assertEquals(4, availableMoves.size());
	}

	@Test
	public void testConsoleInputParser() throws Exception {
		init();
		// test move within range
		ByteArrayInputStream in = new ByteArrayInputStream("e6".getBytes());
		int[] inputs = boardConsole.consoleInputParser(new Scanner(in));
		Assert.assertNotNull(inputs);
		Assert.assertEquals(2, inputs.length);
		Assert.assertEquals(5, inputs[0]);
		Assert.assertEquals(4, inputs[1]);
		
		in = new ByteArrayInputStream("6e".getBytes());
		inputs = boardConsole.consoleInputParser(new Scanner(in));
		Assert.assertNotNull(inputs);
		Assert.assertEquals(2, inputs.length);
		Assert.assertEquals(5, inputs[0]);
		Assert.assertEquals(4, inputs[1]);

		// test move without range
		in = new ByteArrayInputStream("k7".getBytes());
		inputs = boardConsole.consoleInputParser(new Scanner(in));
		Assert.assertNull(inputs);

		// test invalid input
		in = new ByteArrayInputStream("fd453".getBytes());
		inputs = boardConsole.consoleInputParser(new Scanner(in));
		Assert.assertNull(inputs);
		
		//test undo
		in = new ByteArrayInputStream("u".getBytes());
		inputs = boardConsole.consoleInputParser(new Scanner(in));
		Assert.assertNotNull(inputs);
		Assert.assertEquals(1, inputs.length);
		Assert.assertEquals(-1, inputs[0]);
		
	}

	@Test
	public void testSetMove() throws Exception {
		init();
		ArrayList<GameBoard> undoBoards = new ArrayList<GameBoard>();
		HashSet<Coordinate> availableMoves = boardConsole.getAvailableMoves(GameConstants.SquareColor.BLACK);
		boolean isMove = boardConsole.setMove(5, 4, GameConstants.SquareColor.BLACK, availableMoves,undoBoards);
		Assert.assertEquals(true, isMove);

		// test invalid move
		isMove = boardConsole.setMove(5, 3, GameConstants.SquareColor.BLACK, availableMoves,undoBoards);
		Assert.assertEquals(false, isMove);
		
		//test move out of range
		isMove = boardConsole.setMove(100, 50, GameConstants.SquareColor.BLACK, availableMoves,undoBoards);
		Assert.assertEquals(false, isMove);
	}

	// 1/0 - - - - - - - -
	// 2/1 - - - - - - - -
	// 3/2 - - - - - - - -
	// 4/3 - - - O X - - -
	// 5/4 - - - X O - - -
	// 6/5 - - - - - - - -
	// 7/6 - - - - - - - -
	// 8/7 - - - - - - - -
	// a b c d e f g h
	// 0 1 2 3 4 5 6 7

	@Test
	public void testIsGameOver() throws Exception {
		init();
		ArrayList<GameBoard> undoBoards = new ArrayList<GameBoard>();
		boardConsole.setMove(4, 5, GameConstants.SquareColor.BLACK, boardConsole.getAvailableMoves(GameConstants.SquareColor.BLACK),undoBoards);
		boardConsole.setMove(5, 3, GameConstants.SquareColor.WHITE, boardConsole.getAvailableMoves(GameConstants.SquareColor.WHITE),undoBoards);
		boardConsole.setMove(4, 2, GameConstants.SquareColor.BLACK, boardConsole.getAvailableMoves(GameConstants.SquareColor.BLACK),undoBoards);
		boardConsole.setMove(3, 5, GameConstants.SquareColor.WHITE, boardConsole.getAvailableMoves(GameConstants.SquareColor.WHITE),undoBoards);
		
		boardConsole.setMove(2, 5, GameConstants.SquareColor.BLACK, boardConsole.getAvailableMoves(GameConstants.SquareColor.BLACK),undoBoards);
		boardConsole.setMove(4, 1, GameConstants.SquareColor.WHITE, boardConsole.getAvailableMoves(GameConstants.SquareColor.WHITE),undoBoards);
		boardConsole.setMove(6, 2, GameConstants.SquareColor.BLACK, boardConsole.getAvailableMoves(GameConstants.SquareColor.BLACK),undoBoards);
		boardConsole.setMove(3, 6, GameConstants.SquareColor.WHITE, boardConsole.getAvailableMoves(GameConstants.SquareColor.WHITE),undoBoards);
		
		boardConsole.setMove(4, 0, GameConstants.SquareColor.BLACK, boardConsole.getAvailableMoves(GameConstants.SquareColor.BLACK),undoBoards);
		boardConsole.setMove(7, 1, GameConstants.SquareColor.WHITE, boardConsole.getAvailableMoves(GameConstants.SquareColor.WHITE),undoBoards);
		boardConsole.setMove(2, 3, GameConstants.SquareColor.BLACK, boardConsole.getAvailableMoves(GameConstants.SquareColor.BLACK),undoBoards);
		boardConsole.setMove(1, 3, GameConstants.SquareColor.WHITE, boardConsole.getAvailableMoves(GameConstants.SquareColor.WHITE),undoBoards);
		
		boardConsole.setMove(2, 4, GameConstants.SquareColor.BLACK, boardConsole.getAvailableMoves(GameConstants.SquareColor.BLACK),undoBoards);
		boardConsole.setMove(3, 2, GameConstants.SquareColor.WHITE, boardConsole.getAvailableMoves(GameConstants.SquareColor.WHITE),undoBoards);
		boardConsole.setMove(5, 4, GameConstants.SquareColor.BLACK, boardConsole.getAvailableMoves(GameConstants.SquareColor.BLACK),undoBoards);
		boardConsole.setMove(6, 3, GameConstants.SquareColor.WHITE, boardConsole.getAvailableMoves(GameConstants.SquareColor.WHITE),undoBoards);
		
		boardConsole.setMove(6, 4, GameConstants.SquareColor.BLACK, boardConsole.getAvailableMoves(GameConstants.SquareColor.BLACK),undoBoards);
		boardConsole.setMove(1, 6, GameConstants.SquareColor.WHITE, boardConsole.getAvailableMoves(GameConstants.SquareColor.WHITE),undoBoards);
		boardConsole.setMove(0, 3, GameConstants.SquareColor.BLACK, boardConsole.getAvailableMoves(GameConstants.SquareColor.BLACK),undoBoards);
		boardConsole.setMove(6, 5, GameConstants.SquareColor.WHITE, boardConsole.getAvailableMoves(GameConstants.SquareColor.WHITE),undoBoards);
		
		boardConsole.setMove(3, 7, GameConstants.SquareColor.BLACK, boardConsole.getAvailableMoves(GameConstants.SquareColor.BLACK),undoBoards);
		boardConsole.setMove(2, 6, GameConstants.SquareColor.WHITE, boardConsole.getAvailableMoves(GameConstants.SquareColor.WHITE),undoBoards);
		boardConsole.setMove(7, 3, GameConstants.SquareColor.BLACK, boardConsole.getAvailableMoves(GameConstants.SquareColor.BLACK),undoBoards);
		boardConsole.setMove(3, 1, GameConstants.SquareColor.WHITE, boardConsole.getAvailableMoves(GameConstants.SquareColor.WHITE),undoBoards);
		
		boardConsole.setMove(3, 0, GameConstants.SquareColor.BLACK, boardConsole.getAvailableMoves(GameConstants.SquareColor.BLACK),undoBoards);
		boardConsole.setMove(2, 0, GameConstants.SquareColor.WHITE, boardConsole.getAvailableMoves(GameConstants.SquareColor.WHITE),undoBoards);
		boardConsole.setMove(1, 0, GameConstants.SquareColor.BLACK, boardConsole.getAvailableMoves(GameConstants.SquareColor.BLACK),undoBoards);
		boardConsole.setMove(5, 2, GameConstants.SquareColor.WHITE, boardConsole.getAvailableMoves(GameConstants.SquareColor.WHITE),undoBoards);

		boardConsole.setMove(7, 5, GameConstants.SquareColor.BLACK, boardConsole.getAvailableMoves(GameConstants.SquareColor.BLACK),undoBoards);
		boardConsole.setMove(2, 1, GameConstants.SquareColor.WHITE, boardConsole.getAvailableMoves(GameConstants.SquareColor.WHITE),undoBoards);
		boardConsole.setMove(7, 6, GameConstants.SquareColor.BLACK, boardConsole.getAvailableMoves(GameConstants.SquareColor.BLACK),undoBoards);
		boardConsole.setMove(5, 5, GameConstants.SquareColor.WHITE, boardConsole.getAvailableMoves(GameConstants.SquareColor.WHITE),undoBoards);
		
		boardConsole.setMove(7, 2, GameConstants.SquareColor.BLACK, boardConsole.getAvailableMoves(GameConstants.SquareColor.BLACK),undoBoards);
		boardConsole.setMove(5, 1, GameConstants.SquareColor.WHITE, boardConsole.getAvailableMoves(GameConstants.SquareColor.WHITE),undoBoards);
		boardConsole.setMove(4, 6, GameConstants.SquareColor.BLACK, boardConsole.getAvailableMoves(GameConstants.SquareColor.BLACK),undoBoards);
		boardConsole.setMove(5, 6, GameConstants.SquareColor.WHITE, boardConsole.getAvailableMoves(GameConstants.SquareColor.WHITE),undoBoards);
		
		boardConsole.setMove(4, 7, GameConstants.SquareColor.BLACK, boardConsole.getAvailableMoves(GameConstants.SquareColor.BLACK),undoBoards);
		boardConsole.setMove(5, 7, GameConstants.SquareColor.WHITE, boardConsole.getAvailableMoves(GameConstants.SquareColor.WHITE),undoBoards);
		boardConsole.setMove(6, 7, GameConstants.SquareColor.BLACK, boardConsole.getAvailableMoves(GameConstants.SquareColor.BLACK),undoBoards);
		boardConsole.setMove(2, 2, GameConstants.SquareColor.WHITE, boardConsole.getAvailableMoves(GameConstants.SquareColor.WHITE),undoBoards);
		
		boardConsole.setMove(0, 6, GameConstants.SquareColor.BLACK, boardConsole.getAvailableMoves(GameConstants.SquareColor.BLACK),undoBoards);
		boardConsole.setMove(2, 7, GameConstants.SquareColor.WHITE, boardConsole.getAvailableMoves(GameConstants.SquareColor.WHITE),undoBoards);
		boardConsole.setMove(1, 7, GameConstants.SquareColor.BLACK, boardConsole.getAvailableMoves(GameConstants.SquareColor.BLACK),undoBoards);
		boardConsole.setMove(0, 4, GameConstants.SquareColor.WHITE, boardConsole.getAvailableMoves(GameConstants.SquareColor.WHITE),undoBoards);
		
		boardConsole.setMove(1, 2, GameConstants.SquareColor.BLACK, boardConsole.getAvailableMoves(GameConstants.SquareColor.BLACK),undoBoards);
		boardConsole.setMove(1, 1, GameConstants.SquareColor.WHITE, boardConsole.getAvailableMoves(GameConstants.SquareColor.WHITE),undoBoards);
		boardConsole.setMove(0, 2, GameConstants.SquareColor.BLACK, boardConsole.getAvailableMoves(GameConstants.SquareColor.BLACK),undoBoards);
		boardConsole.setMove(1, 5, GameConstants.SquareColor.WHITE, boardConsole.getAvailableMoves(GameConstants.SquareColor.WHITE),undoBoards);
		
		Assert.assertEquals(false, boardConsole.isGameOver(GameConstants.SquareColor.BLACK));
		
		boardConsole.setMove(6, 0, GameConstants.SquareColor.BLACK, boardConsole.getAvailableMoves(GameConstants.SquareColor.BLACK),undoBoards);
		boardConsole.setMove(7, 4, GameConstants.SquareColor.WHITE, boardConsole.getAvailableMoves(GameConstants.SquareColor.WHITE),undoBoards);
		boardConsole.setMove(7, 0, GameConstants.SquareColor.BLACK, boardConsole.getAvailableMoves(GameConstants.SquareColor.BLACK),undoBoards);
		boardConsole.setMove(6, 1, GameConstants.SquareColor.WHITE, boardConsole.getAvailableMoves(GameConstants.SquareColor.WHITE),undoBoards);
		
		boardConsole.setMove(0, 5, GameConstants.SquareColor.BLACK, boardConsole.getAvailableMoves(GameConstants.SquareColor.BLACK),undoBoards);;
		boardConsole.setMove(0, 1, GameConstants.SquareColor.WHITE, boardConsole.getAvailableMoves(GameConstants.SquareColor.WHITE),undoBoards);
		boardConsole.setMove(1, 4, GameConstants.SquareColor.BLACK, boardConsole.getAvailableMoves(GameConstants.SquareColor.BLACK),undoBoards);
		boardConsole.setMove(0, 7, GameConstants.SquareColor.WHITE, boardConsole.getAvailableMoves(GameConstants.SquareColor.WHITE),undoBoards);
		
		boardConsole.setMove(0, 0, GameConstants.SquareColor.BLACK, boardConsole.getAvailableMoves(GameConstants.SquareColor.BLACK),undoBoards);
		boardConsole.setMove(7, 7, GameConstants.SquareColor.WHITE, boardConsole.getAvailableMoves(GameConstants.SquareColor.WHITE),undoBoards);
		boardConsole.setMove(6, 6, GameConstants.SquareColor.BLACK, boardConsole.getAvailableMoves(GameConstants.SquareColor.BLACK),undoBoards);
		
		Assert.assertEquals(true, boardConsole.isGameOver(GameConstants.SquareColor.WHITE));
	}
	
	@Test
	public void testUndo() throws Exception {
		init();
		ArrayList<GameBoard> undoBoards = new ArrayList<GameBoard>();
		boardConsole.setMove(4, 5, GameConstants.SquareColor.BLACK, boardConsole.getAvailableMoves(GameConstants.SquareColor.BLACK),undoBoards);
		boardConsole.setMove(5, 3, GameConstants.SquareColor.WHITE, boardConsole.getAvailableMoves(GameConstants.SquareColor.WHITE),undoBoards);
		boardConsole.setMove(4, 2, GameConstants.SquareColor.BLACK, boardConsole.getAvailableMoves(GameConstants.SquareColor.BLACK),undoBoards);
		boardConsole.setMove(3, 5, GameConstants.SquareColor.WHITE, boardConsole.getAvailableMoves(GameConstants.SquareColor.WHITE),undoBoards);
		
		Assert.assertEquals(true, boardConsole.undoMove(undoBoards));
		Assert.assertEquals(true, boardConsole.undoMove(undoBoards));
		
		boardConsole.setMove(6, 2, GameConstants.SquareColor.BLACK, boardConsole.getAvailableMoves(GameConstants.SquareColor.BLACK),undoBoards);
		boardConsole.setMove(6, 3, GameConstants.SquareColor.WHITE, boardConsole.getAvailableMoves(GameConstants.SquareColor.WHITE),undoBoards);
		
		Assert.assertEquals(true, boardConsole.undoMove(undoBoards));
		Assert.assertEquals(true, boardConsole.undoMove(undoBoards));
		Assert.assertEquals(true, boardConsole.undoMove(undoBoards));
		Assert.assertEquals(true, boardConsole.undoMove(undoBoards));
		Assert.assertEquals(false, boardConsole.undoMove(undoBoards));
	}
}
