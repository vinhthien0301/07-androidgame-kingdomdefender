package com.techstorm.androidgame.kingdomdefender;

import java.util.List;

public class Monster {

	public static final Integer DEAD = Integer.valueOf(0);
	public static final Integer LIVE = Integer.valueOf(1);
	
	public MatrixLocation2d putting;
	
	public MatrixSize2d spriteSize;
	
	public String name;
	
	// Hearth blood
	public int hp;
	
	// Damage -- (integer)
	public int attackDamage;

	// Hurt effect
	public List<Effect> hurtEffect;

	// Cost -- (double) -- number of coins to award money after killed monster.
	public double rewardCost;

	public Monster(MatrixLocation2d put, MatrixSize2d size) {
		this.putting = put;
		this.spriteSize = size;
	}
	
	
}
