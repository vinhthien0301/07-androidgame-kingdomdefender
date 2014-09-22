package com.techstorm.androidgame.kingdomdefender;

import java.util.ArrayList;
import java.util.List;

public class KingDefGame {

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;
	
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
		final float centerX = (CAMERA_WIDTH - 24) / 2;
		final float centerY = (CAMERA_HEIGHT - 32) / 2;
		Monster monster = new Monster(new Location2d(centerX, centerY), new Size2d(48, 64));
		wave.addMonster(monster);
		map.addWave(wave);
		
		Location2d[] locs = new Location2d[5];
		locs[0] = new Location2d(10, 10);
		locs[1] = new Location2d(10, CAMERA_HEIGHT - 74);
		locs[2] = new Location2d(CAMERA_WIDTH - 58, CAMERA_HEIGHT - 74);
		locs[3] = new Location2d(CAMERA_WIDTH - 58, 10);
		locs[4] = new Location2d(10, 10);
		map.setMonsterPath(locs);
	}
	
	// get monster list of current level map
	public List<Monster> getCurrentMonsters() {
		return levelMaps.get(levelMapIndex).getCurrentMonsters();
	}
	
	// get monster path of current level map
	public Location2d[] getCurrentMonsterPath() {
		return levelMaps.get(levelMapIndex).monsterPath;
	}

}
