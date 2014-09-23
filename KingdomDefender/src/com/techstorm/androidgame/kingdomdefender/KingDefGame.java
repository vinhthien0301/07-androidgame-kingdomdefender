package com.techstorm.androidgame.kingdomdefender;

import java.util.ArrayList;
import java.util.List;

public class KingDefGame {
	public List<LevelMap> levelMaps;
	public int levelMapIndex;

	public KingDefGame() {
		levelMaps = new ArrayList<LevelMap>();
		levelMapIndex = 0;
	}

	// Check end game
	public Integer endgame() {
		return 0;
	}

	// Load level data
	public void loadLevelData() {
		levelMaps = new ArrayList<LevelMap>();
		LevelMap map = new LevelMap();
		levelMaps.add(map);
		
		Wave wave = new Wave();
		final int centerX = (int)(LayerConvertor.CAMERA_WIDTH - 24) / 2;
		final int centerY = (int)(LayerConvertor.CAMERA_HEIGHT - 32) / 2;
		Monster monster = new Monster(new MatrixLocation2d(centerX, centerY), new MatrixSize2d(48, 64));
		wave.addMonster(monster);
		map.addWave(wave);
		
		MatrixLocation2d[] locs = new MatrixLocation2d[5];
		locs[0] = new MatrixLocation2d(0, 0);
		locs[1] = new MatrixLocation2d(0, map.matrixHeight);
		locs[2] = new MatrixLocation2d(map.matrixWidth, map.matrixHeight);
		locs[3] = new MatrixLocation2d(map.matrixWidth, 0 );
		locs[4] = new MatrixLocation2d(0, 0);
		map.setMonsterPath(locs);
	}
	
	// get monster list of current level map
	public List<Monster> getCurrentMonsters() {
		return levelMaps.get(levelMapIndex).getCurrentMonsters();
	}
	
	// get monster path of current level map
	public MatrixLocation2d[] getCurrentMonsterPath() {
		return levelMaps.get(levelMapIndex).monsterPath;
	}

}
