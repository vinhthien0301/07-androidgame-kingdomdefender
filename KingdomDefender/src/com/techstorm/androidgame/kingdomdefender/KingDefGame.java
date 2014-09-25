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
		
		// add tower
		Tower tower = new Tower();
		tower.putting = new MatrixLocation2d(3, 5);
		tower.spriteSize = new MatrixSize2d(48, 48);
		map.towers.add(tower);
		
		levelMaps.add(map);
	}
	
	// get monster list of current level map
	public List<Monster> getCurrentMonsters() {
		return levelMaps.get(levelMapIndex).getCurrentMonsters();
	}
	
	// get tower list of current level map
	public List<Tower> getCurrentTowers() {
		return levelMaps.get(levelMapIndex).getCurrentTowers();
	}
	
	// get monster path of current level map
	public MatrixLocation2d[] getCurrentMonsterPath() {
		return levelMaps.get(levelMapIndex).monsterPath;
	}

}
