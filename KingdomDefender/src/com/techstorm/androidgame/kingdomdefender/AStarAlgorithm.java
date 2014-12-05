package com.techstorm.androidgame.kingdomdefender;

import java.util.ArrayList;
import java.util.List;

public class AStarAlgorithm {

	private KingDefGame game;
	
	public AStarAlgorithm(KingDefGame game) {
		this.game = game;
	}
	
	public List<MatrixLocation2d> calcNextMoveMonster(int monsterNumber) {
		List<MatrixLocation2d> locs = new ArrayList<MatrixLocation2d>();
		Monster monster = game.getMonster(monsterNumber);
		locs.add(monster.matrixLocation);
		locs.add(new MatrixLocation2d(monster.matrixLocation.columnIndex + 25, monster.matrixLocation.rowIndex));
		return locs;
	}
	
}
