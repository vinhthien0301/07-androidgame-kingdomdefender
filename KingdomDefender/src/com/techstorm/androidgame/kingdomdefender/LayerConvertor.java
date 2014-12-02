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
	
	public static boolean isIntersection(MatrixLocation2d location1, MatrixSize2d size1, MatrixLocation2d location2, MatrixSize2d size2) {
		float columnMin = location1.columnIndex;
		float columnMax = location1.columnIndex + size1.width - 1;
		float rowMin = location1.rowIndex;
		float rowMax = location1.rowIndex + size1.height - 1;
		
		float outColumnMin = location2.columnIndex;
		float outColumnMax = location2.columnIndex + size2.width - 1;
		float outRowMin = location2.rowIndex;
		float outRowMax = location2.rowIndex + size2.height - 1;
		return (((columnMin <= outColumnMin && outColumnMin <= columnMax) || (columnMin <= outColumnMax && outColumnMax <= columnMax))
				&&
				((rowMin <= outRowMin && outRowMin <= rowMax) || (rowMin <= outRowMax && outRowMax <= rowMax)));
	}

}
