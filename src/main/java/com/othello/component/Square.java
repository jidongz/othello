package com.othello.component;

import com.othello.interfaces.Coordinate;

public class Square implements Coordinate {

	private int x;
	private int y;
	private int dir;

	public Square(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Square(int x, int y, int dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public void setX(int x) {
		this.x = x;
	}

	@Override
	public void setY(int y) {
		this.y = y;
	}

	public int getDir() {
		return dir;
	}

	public void setDir(int dir) {
		this.dir = dir;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		result = prime * result + dir;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}
		if (this.x == ((Square) obj).getX() && this.y == ((Square) obj).getY() && this.dir == ((Square) obj).getDir()) {
			return true;
		}
		return false;
	}
}
