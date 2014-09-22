package com.techstorm.androidgame.kingdomdefender;

import java.util.ArrayList;
import java.util.List;

public class LevelMap {

	public int width;
	public int height;
	
	public List<Tower> towers;
	public Location2d[] monsterPath;
	public List<Wave> waves;
	public int wavesIndex;

	public LevelMap() {
		towers = new ArrayList<Tower>();
		waves = new ArrayList<Wave>();
		wavesIndex = 0;
	}
	
	public void addTower(Tower tower) {
		this.towers.add(tower);
	}
	
	public void addWave(Wave wave) {
		this.waves.add(wave);
	} 
	
	public void setMonsterPath(Location2d[] path) {
		this.monsterPath = path;
	}
	
	// Get monster of current wave
	public List<Monster> getCurrentMonsters() {
		return this.waves.get(wavesIndex).monsters;
	}
}
