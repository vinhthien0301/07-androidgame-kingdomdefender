package com.techstorm.androidgame.kingdomdefender;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.techstorm.androidgame.kingdomdefender.data.DatabaseCreator;

public class KingDefGame {
	public List<LevelMap> levelMaps;
	public int levelMapIndex;
	public List<Tower> shopItems;
	public List<Monster> monsterCharacter;
	public Counter monsterNumber; // total of monsters include all maps
	public Counter towerNumber; // total of towers include all maps, except shop items.

	public KingDefGame(Context context) {
		DatabaseCreator.openDatabase(context);
		levelMaps = new ArrayList<LevelMap>();
		levelMapIndex = 0;
		monsterNumber = new Counter();
		towerNumber = new Counter();
		loadLevelData();
	}

	public void resetMonsterPaths() {
		
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
		monsterCharacter = DatabaseCreator.getMonsterCharacter();
		DatabaseCreator.getMonster(map, monsterNumber);
		DatabaseCreator.getMapPath(map);
		shopItems = DatabaseCreator.getShopItems(towerNumber);
		
		levelMaps.add(map);
		// init money for testing
		levelMaps.get(levelMapIndex).setMoney(100);
	}
	
	public int getMonsterNumber() {
		return monsterNumber.getNumber();
	}
	
	public int getTowerNumber() {
		return towerNumber.getNumber();
	}
	
	public Tower createTower(int shopItemIndex, MatrixLocation2d matrixLoc2d, float width, float height) {
		Tower tower = cloneTower(shopItems.get(shopItemIndex));
		tower.number = towerNumber.getNumber();
		tower.matrixLocation = matrixLoc2d;
		tower.spriteSize = new Size2d(width, height);
		levelMaps.get(levelMapIndex).getCurrentTowers().add(tower);
		levelMaps.get(levelMapIndex).subsMoney(tower.buyCost);
		towerNumber.increase(1);
		return tower;
	}
	
	private Tower cloneTower(Tower tower) {
		Tower cloneTower = new Tower();
		cloneTower.areaOfEffect = tower.areaOfEffect;
		cloneTower.armorPiercing = tower.armorPiercing;
		cloneTower.attackEffect = tower.attackEffect;
		cloneTower.attackSpeed = tower.attackSpeed;
		cloneTower.buyCost = tower.buyCost;
		cloneTower.damage = tower.damage;
		cloneTower.matrixLocation = tower.matrixLocation;
		cloneTower.range = tower.range;
		cloneTower.sellCost = tower.sellCost;
		cloneTower.spriteSize = tower.spriteSize;
		cloneTower.matrixSize = tower.matrixSize;
		cloneTower.fileName = tower.fileName;
		cloneTower.imageWidth = tower.imageWidth;
		cloneTower.imageHeight = tower.imageHeight;
		cloneTower.pTileColumn = tower.pTileColumn;
		cloneTower.pTileRow = tower.pTileRow;
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
		if (getCurrentMonsters() == null || getCurrentMonsters().isEmpty()) {
			return Monster.DEAD;
		}
		Monster monster = getMonster(monsterIndex);
		if (monster == null) {
			return Monster.DEAD;
		}
		monster.hp -= calcHpDamaged(towerIndex, monsterIndex);
		if (monster.hp <= 0) {
			levelMaps.get(levelMapIndex).getCurrentMonsters().remove(monster);
			Tower tower = getTower(towerIndex);
			if (tower != null) {
				tower.target = null;
			}
			return Monster.DEAD;
		}
		return Monster.LIVE;
	}
	
	public void removeMonster(int monsterIndex) {
		Monster monster = getMonster(monsterIndex);
		levelMaps.get(levelMapIndex).getCurrentMonsters().remove(monster);
	}
	
	public int calcHpDamaged(int towerIndex, int monsterIndex) {
		Tower tower = getTower(towerIndex);
		return tower.damage;
	}
	
	public boolean isInMonsterList(Monster monster) {
		return getMonster(monster.number) != null;
	}
	
	public Monster getMonster(int monsterNumber) {
		Monster monster = null;
		if (getCurrentMonsters() != null && !getCurrentMonsters().isEmpty()) {
			for (Monster monsterItem : getCurrentMonsters()) {
				if (monsterItem.number == monsterNumber) {
					monster = monsterItem;
					break;
				}
			}
		}
		return monster;
	}
	
	public Tower getShopItem(int towerNumber) {
		Tower tower = null;
		if (getCurrentShop() != null && !getCurrentShop().isEmpty()) {
			for (Tower towerItem : getCurrentShop()) {
				if (towerItem.number == towerNumber) {
					tower = towerItem;
					break;
				}
			}
		}
		return tower;
	}
	
	public Tower getTower(int towerNumber) {
		Tower tower = null;
		if (getCurrentTowers() != null && !getCurrentTowers().isEmpty()) {
			for (Tower towerItem : getCurrentTowers()) {
				if (towerItem.number == towerNumber) {
					tower = towerItem;
					break;
				}
			}
		}
		return tower;
	}
	
	public boolean canShoot(float pSecondsElapsed, int towerNumber, int monsterNumber) {
		Tower tower = this.getTower(towerNumber);
		Monster monster = this.getMonster(monsterNumber);
		return canShoot(pSecondsElapsed, tower, monster);
	}

	public boolean canShoot(float pSecondsElapsed, Tower tower, Monster monster) {
		if (tower == null) {
			return false;
		}
		if (monster == null) {
			return false;
		}
		tower.attackTimeWait += pSecondsElapsed;
		if (tower.attackTimeWait < tower.attackSpeed) {
			return false;
		}
		tower.attackTimeWait = 0;
		
		if (Math.abs(monster.matrixLocation.columnIndex - tower.matrixLocation.columnIndex) <= tower.range
				&& Math.abs(monster.matrixLocation.rowIndex - tower.matrixLocation.rowIndex) <= tower.range) {
			return true;
		}
		return false;
	}
	
	public boolean canBuy(int shopItemIndex) {
		Tower shopItem = shopItems.get(shopItemIndex);
		if (levelMaps.get(levelMapIndex).getMoney() >= shopItem.buyCost) {
			return true;
		}
		return false;
	}
	
	public Monster createMonster(int monsterCharacterIndex, MatrixLocation2d matrixLoc2d, Size2d size2d) {
		if (monsterCharacter == null || monsterCharacter.isEmpty()) {
			return null;
		}
		Monster monster = cloneMonster(monsterCharacter.get(monsterCharacterIndex));
		monster.number = monsterNumber.getNumber();
		monster.matrixLocation = matrixLoc2d;
		monster.spriteSize = size2d;
		levelMaps.get(levelMapIndex).getCurrentMonsters().add(monster);
		monsterNumber.increase(1);
		return monster;
	}
	
	private Monster cloneMonster(Monster monster) {
		Monster cloneMonster = new Monster();
		cloneMonster.attackDamage = monster.attackDamage;
		cloneMonster.hp = monster.hp;
		cloneMonster.hpMax = monster.hpMax;
		cloneMonster.hurtEffect = monster.hurtEffect;
		cloneMonster.name = monster.name;
		cloneMonster.matrixLocation = monster.matrixLocation;
		cloneMonster.rewardCost = monster.rewardCost;
		cloneMonster.spriteSize = monster.spriteSize;
		cloneMonster.matrixSize = monster.matrixSize;
		return cloneMonster;
	}

	public Monster getNearestMonster(Tower tower) {
		float nearestDistance = Float.MAX_VALUE;
		Monster nearestMonster = null;
		for (Monster monster : getCurrentMonsters()) {
			if (tower.isInShootRange(monster)) {
				float distance = tower.distance(monster);
				if (distance < nearestDistance) {
					nearestDistance = distance;
					nearestMonster = monster;
				}
			}
		}
		return nearestMonster;
	}

	public void updateMonsterMoving(int monsterNumber, MatrixLocation2d newPosition) {
		Monster monster = getMonster(monsterNumber);
		if (monster == null) {
			return;
		}
		monster.matrixLocation = newPosition;
	}
	
}
