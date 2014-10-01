package com.techstorm.androidgame.kingdomdefender;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.techstorm.androidgame.kingdomdefender.data.DatabaseCreator;

public class KingDefGame {
	public List<LevelMap> levelMaps;
	public int levelMapIndex;
	public List<Tower> shopItems;

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
		shopItems = DatabaseCreator.getShopItems();
		
		// add tower
		Tower tower = new Tower();
		tower.putting = new MatrixLocation2d(3, 5);
		tower.spriteSize = new MatrixSize2d(48, 48);
		tower.range = 5;
		tower.buyCost = 3;
		tower.damage = 10;
		map.towers.add(tower);
		
		levelMaps.add(map);
	}
	
	public void createTower(int shopItemIndex, MatrixLocation2d matrixLoc2d, float width, float height) {
		Tower tower = cloneTower(shopItems.get(shopItemIndex));
		tower.putting = matrixLoc2d;
		tower.spriteSize = new MatrixSize2d(width, height);
		levelMaps.get(levelMapIndex).getCurrentTowers().add(tower);
		levelMaps.get(levelMapIndex).subsMoney(tower.buyCost);
	}
	
	private Tower cloneTower(Tower tower) {
		Tower cloneTower = new Tower();
		cloneTower.areaOfEffect = tower.areaOfEffect;
		cloneTower.armorPiercing = tower.armorPiercing;
		cloneTower.attackEffect = tower.attackEffect;
		cloneTower.attackSpeed = tower.attackSpeed;
		cloneTower.buyCost = tower.buyCost;
		cloneTower.damage = tower.damage;
		cloneTower.putting = tower.putting;
		cloneTower.range = tower.range;
		cloneTower.sellCost = tower.sellCost;
		cloneTower.spriteSize = tower.spriteSize;
		return cloneTower;
	}
	
	
	public void addTower(Tower tower) {
		levelMaps.get(levelMapIndex).getCurrentTowers().add(tower);
	}
	
	public int getCurrentMoney() {
		return levelMaps.get(levelMapIndex).getMoney();
	}
	
	// get monster list of current level map
	public List<Monster> getCurrentMonsters() {
		return levelMaps.get(levelMapIndex).getCurrentMonsters();
	}
	
	// get tower list of current level map
	public List<Tower> getCurrentTowers() {
		return levelMaps.get(levelMapIndex).getCurrentTowers();
	}
	
	// get shop item list of current level map
	public List<Tower> getCurrentShop() {
		return shopItems;
	}
	
	// get monster path of current level map
	public MatrixLocation2d[] getCurrentMonsterPath() {
		return levelMaps.get(levelMapIndex).monsterPath;
	}

	public int shoot(int towerIndex, int monsterIndex) {
		Monster monster = getCurrentMonsters().get(monsterIndex);
		monster.hp -= calcHpDamaged(towerIndex, monsterIndex);
		if (monster.hp <= 0) {
			levelMaps.get(levelMapIndex).getCurrentMonsters().remove(monster);
			return Monster.DEAD;
		}
		return Monster.LIVE;
	}
	
	public int calcHpDamaged(int towerIndex, int monsterIndex) {
		Tower tower = getCurrentTowers().get(towerIndex);
		return tower.damage;
	}
	
	public boolean canShoot(int towerIndex, int monsterIndex, MatrixLocation2d monsterPutting) {
		Tower tower = getCurrentTowers().get(towerIndex);
		Monster monster = getCurrentMonsters().get(monsterIndex);
		monster.putting = monsterPutting;
		if (Math.abs(monster.putting.columnIndex - tower.putting.columnIndex) <= tower.range
				&& Math.abs(monster.putting.rowIndex - tower.putting.rowIndex) <= tower.range) {
			return true;
		}
		return false;
	}
	
}
