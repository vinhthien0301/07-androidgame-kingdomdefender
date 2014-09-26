package com.techstorm.androidgame.kingdomdefender;

import java.util.List;

public class Tower {

	public MatrixLocation2d putting;
	public MatrixSize2d spriteSize;
	
	// Damage -- (integer)
	public int damage;
	// Range -- (integer) -- Attack Range that tower can shoot
	public int range;
	// Multi-target -- (integer) -- Radius of bullet (0 = single target)
	public int areaOfEffect;
	// Pierce -- (integer)
	// pierce > armor => dmg 100%
	// pierce < armor => dmg = dmg - [(armor - pierce)*dmg / 100]
	public int armorPiercing;

	// Attack effect
	public List<Effect> attackEffect;
	// Speed -- (double) -- number of bullet / 1 second
	public double attackSpeed;

	// BUY Cost -- (double) -- number of coins to build tower in map
	public double buyCost;

	// SELL Cost -- (double) -- number of coins to sell tower in map
	public double sellCost;
	
}
