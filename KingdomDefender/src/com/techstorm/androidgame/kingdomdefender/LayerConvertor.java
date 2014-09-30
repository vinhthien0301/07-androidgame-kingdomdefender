package com.techstorm.androidgame.kingdomdefender;

public class LayerConvertor {

	public static final int CAMERA_WIDTH = 720;
	public static final int CAMERA_HEIGHT = 480;
	public static final int CONVERTOR_WIDTH_OF_SQUARE = 32;
	public static final int CONVERTOR_HEIGHT_OF_SQUARE = 32;
	
	public static Location2d maxtrixToGraphicLocation2d(
			MatrixLocation2d matrixLoc2d) {
		return new Location2d(matrixLoc2d.columnIndex * CONVERTOR_WIDTH_OF_SQUARE,
				matrixLoc2d.rowIndex * CONVERTOR_HEIGHT_OF_SQUARE);
	}
	
	public static MatrixLocation2d graphicLocationToMaxtrix2d(
			Location2d location2d) {
		return new MatrixLocation2d((int)location2d.px / CONVERTOR_WIDTH_OF_SQUARE,
				(int)location2d.py / CONVERTOR_HEIGHT_OF_SQUARE);
	}

}
