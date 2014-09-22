package com.techstorm.androidgame.kingdomdefender;

import java.util.List;

public class Monster {

	public Location2d putting;
	
	public Size2d spriteSize;
	
	// Damage -- (integer)
	public int attackDamage;

	// Hurt effect
	public List<Effect> hurtEffect;

	// Cost -- (double) -- number of coins to award money after killed monster.
	public double rewardCost;

	public Monster(Location2d put, Size2d size) {
		this.putting = put;
		this.spriteSize = size;
	}
	
	
}
