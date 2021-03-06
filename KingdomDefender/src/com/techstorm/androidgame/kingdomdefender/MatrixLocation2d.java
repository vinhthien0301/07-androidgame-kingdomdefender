package com.techstorm.androidgame.kingdomdefender;

public class MatrixLocation2d {
	public int columnIndex;
	public int rowIndex;
	
	public MatrixLocation2d() {
		this.columnIndex = 0;
		this.rowIndex = 0;
	}
	
	public MatrixLocation2d(int columnIndex, int rowIndex) {
		this.columnIndex = columnIndex;
		this.rowIndex = rowIndex;
	}
	
	public boolean equals(MatrixLocation2d mLocation2d) {
		return this.columnIndex == mLocation2d.columnIndex && this.rowIndex == mLocation2d.rowIndex;
	}
}
