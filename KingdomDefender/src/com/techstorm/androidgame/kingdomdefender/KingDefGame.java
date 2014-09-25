package com.techstorm.androidgame.kingdomdefender;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.techstorm.androidgame.kingdomdefender.data.DatabaseCreator;

public class KingDefGame {
	public List<LevelMap> levelMaps;
	public int levelMapIndex;

	public KingDefGame(Context context) {
		DatabaseCreator.openDatabase(context);
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
		DatabaseCreator.getMap(map);
		DatabaseCreator.getMonster(map);
		DatabaseCreator.getMapPath(map);
		
//		Wave wave = new Wave();
//		final int centerX = (int)(LayerConvertor.CAMERA_WIDTH - 24) / 2;
//		final int centerY = (int)(LayerConvertor.CAMERA_HEIGHT - 32) / 2;
//		Monster monster = new Monster(new MatrixLocation2d(centerX, centerY), new MatrixSize2d(48, 64));
//		wave.addMonster(monster);
//		map.addWave(wave);
		
		levelMaps.add(map);
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
