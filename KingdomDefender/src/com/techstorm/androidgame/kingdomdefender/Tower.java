package com.techstorm.androidgame.kingdomdefender;

import java.util.List;

public class Tower {

	public MatrixLocation2d putting;
	public MatrixSize2d spriteSize;
	
	// Damage -- (integer)
	public int attackDamage;
	// Range -- (integer) -- Range that tower can shoot
	public int attackRange;
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

	// Cost -- (double) -- number of coins to build tower in map
	public double buyCost;

}
