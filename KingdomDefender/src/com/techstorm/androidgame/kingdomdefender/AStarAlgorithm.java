package com.techstorm.androidgame.kingdomdefender;

import java.util.ArrayList;
import java.util.List;

public class AStarAlgorithm {

	public static final int[] COLUMN_MOVE = { -1, 1,  0, 0 };
	public static final int[] ROW_MOVE    = {  0, 0, -1, 1 };
	
	private KingDefGame game;
	
	public AStarAlgorithm(KingDefGame game) {
		this.game = game;
	}
	
	
	public List<MatrixLocation2d> calcNextMoveMonster(int monsterNumber) {
		List<MatrixLocation2d> locs = new ArrayList<MatrixLocation2d>();
		Monster monster = game.getMonster(monsterNumber);
		locs.add(monster.matrixLocation);
		
		
		List<PathData> bestMoves = new ArrayList<PathData>();
		PathData firstPathData = new PathData(monster.matrixLocation, game.getDestination(), null);
		bestMoves.add(firstPathData);
		
		boolean pathFound = false;
		PathData endlessPathData = null;
		while (!pathFound) {
			PathData nearestPathData = getBestPathData(bestMoves);
			nearestPathData.passed = true;
			for (int moveIndex = 0; moveIndex < COLUMN_MOVE.length; moveIndex++) {
				MatrixLocation2d nextLocation = new MatrixLocation2d(
						nearestPathData.position.columnIndex + COLUMN_MOVE[moveIndex], 
						nearestPathData.position.rowIndex + ROW_MOVE[moveIndex]);
				if (!game.isTowerConflicted(nextLocation, monster.matrixSize) 
						&& !containInMoveList(bestMoves, nextLocation)
						&& !passInMoveList(bestMoves, nextLocation)) {
					PathData nextPathData = new PathData(nextLocation, game.getDestination(), nearestPathData);
					bestMoves.add(nextPathData);
					if (game.getDestination().equals(nextPathData.position)) {
						pathFound = true;
						endlessPathData = nextPathData;
						break;
					}
				}
			}
		}
		if (!pathFound || endlessPathData == null || endlessPathData.previous == null) {
			return null;
		}
		
		while (endlessPathData.previous != firstPathData) {
			
			endlessPathData = endlessPathData.previous;
		}
		locs.add(endlessPathData.position);
		return locs;
	}
	
	private boolean passInMoveList(List<PathData> bestMoves,
			MatrixLocation2d location) {
		boolean passed = false;
		for (PathData pathData : bestMoves) {
			if (location.columnIndex == pathData.position.columnIndex
					&& location.rowIndex == pathData.position.rowIndex) {
				passed = pathData.passed;
			}
		}
		return passed;
	}


	private boolean containInMoveList(List<PathData> bestMoves, MatrixLocation2d location) {
		boolean contain = false;
		for (PathData pathData : bestMoves) {
			if (location.columnIndex == pathData.position.columnIndex
					&& location.rowIndex == pathData.position.rowIndex) {
				contain = true;
			}
		}
		return contain;
	}


	private PathData getBestPathData(List<PathData> bestMoves) {
		PathData nearestPathData = null;
		double nearestDistance = Double.MAX_VALUE;
		if (bestMoves != null && !bestMoves.isEmpty()) {
			for (int index = 0; index < bestMoves.size(); index++) {
				PathData pathData = bestMoves.get(index);
				if (nearestDistance > pathData.distance && !pathData.passed) {
					nearestPathData = pathData;
					nearestDistance = pathData.distance;
				}
			}
		}
		return nearestPathData;
	}
	
	private class PathData {
		public MatrixLocation2d position;
		public double distance;
		public PathData previous;
		public boolean passed;
		
		public PathData(MatrixLocation2d position, MatrixLocation2d destination, PathData previous) {
			this.position = position;
			double width = Math.abs(position.columnIndex - destination.columnIndex);
			double height = Math.abs(position.rowIndex - destination.rowIndex);
			this.distance = Math.sqrt(width*width + height*height);
			this.previous = previous;
			this.passed = false;
		}
	}
	
}
