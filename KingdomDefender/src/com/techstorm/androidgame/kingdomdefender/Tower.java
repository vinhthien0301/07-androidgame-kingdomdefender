package com.techstorm.androidgame.kingdomdefender;

import java.util.List;

public class Tower {

	public int number;
	
	public Monster target;
	// Time to track the wait time of tower attack (if attackTimeWait > attackSpeed then shoot)
	public float attackTimeWait;
	
	// DB-
	public MatrixLocation2d putting;
	public MatrixSize2d spriteSize;
	public String fileName;
	public int imageWidth;
	public int imageHeight;
	public int pTileColumn;
	public int pTileRow;
	// DB-Damage -- (integer)
	public int damage;
	// DB-Range -- (integer) -- Attack Range that tower can shoot
	public int range;
	// DB-Multi-target -- (integer) -- Radius of bullet (0 = single target)
	public int areaOfEffect;
	// DB-Pierce -- (integer)
	// pierce > armor => dmg 100%
	// pierce < armor => dmg = dmg - [(armor - pierce)*dmg / 100]
	public int armorPiercing;

	// DB-Attack effect
	public List<Effect> attackEffect;
	// DB-Speed -- (double) -- number of bullet / 1 second
	public float attackSpeed;

	// DB-BUY Cost -- (integer) -- number of coins to build tower in map
	public int buyCost;

	// DB-SELL Cost -- (integer) -- number of coins to sell tower in map
	public int sellCost;
	
	public boolean isInShootRange(Monster targetMonster) {
		if (targetMonster == null) {
			return false;
		}
		if (Math.abs(targetMonster.putting.columnIndex - this.putting.columnIndex) <= this.range
				&& Math.abs(targetMonster.putting.rowIndex - this.putting.rowIndex) <= this.range) {
			return true;
		}
		return false;
	}
	
	public float distance(Monster targetMonster) {
		float columnDistance = targetMonster.putting.columnIndex - this.putting.columnIndex;
		float rowDistance = targetMonster.putting.rowIndex - this.putting.rowIndex;
		return (float)Math.sqrt((columnDistance * columnDistance) + (rowDistance * rowDistance));
	}
	
}
