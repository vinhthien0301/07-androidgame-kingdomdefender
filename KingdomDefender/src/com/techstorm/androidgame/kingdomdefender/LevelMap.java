package com.techstorm.androidgame.kingdomdefender;

import java.util.ArrayList;
import java.util.List;

public class LevelMap {

	
	// begin matrix with index 0
	public int money;
	public int matrixWidth;
	public int matrixHeight;
	public int level;
	
	public List<Tower> towers;
	public MatrixLocation2d[] monsterPath;
	public int waveSize;
	public List<Wave> waves;
	public int wavesIndex;

	public LevelMap() {
		matrixWidth = (int)LayerConvertor.CAMERA_WIDTH / LayerConvertor.CONVERTOR_WIDTH_OF_SQUARE - 1;
		matrixHeight = (int)LayerConvertor.CAMERA_HEIGHT / LayerConvertor.CONVERTOR_HEIGHT_OF_SQUARE - 1;
		towers = new ArrayList<Tower>();
		waves = new ArrayList<Wave>();
		wavesIndex = 0;
		money = 0;
	}
	
	public void addTower(Tower tower) {
		this.towers.add(tower);
	}
	
	public void addWave(Wave wave) {
		this.waves.add(wave);
	} 
	
	public void setMonsterPath(MatrixLocation2d[] path) {
		this.monsterPath = path;
	}
	
	// Get monster of current wave
	public List<Monster> getCurrentMonsters() {
		return this.waves.get(wavesIndex).monsters;
	}
	
	// Get tower of current wave
	public List<Tower> getCurrentTowers() {
		return this.towers;
	}
}
