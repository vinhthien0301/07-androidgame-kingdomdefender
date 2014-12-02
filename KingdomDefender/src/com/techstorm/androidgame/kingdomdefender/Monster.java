package com.techstorm.androidgame.kingdomdefender;

import java.util.List;

public class Monster {

	public static final Integer DEAD = Integer.valueOf(0);
	public static final Integer LIVE = Integer.valueOf(1);
	
	public int number;
	
	public Integer life;
	
	public String fileName;
	
	public Integer moveSpeed;
	
	public MatrixLocation2d putting;
	
	public Size2d spriteSize;
	
	public MatrixSize2d matrixSize;
	
	public String name;
	
	// Hearth blood
	public int hp;
	
	// Damage -- (integer) -- if any
	public int attackDamage;

	// Hurt effect
	public List<Effect> hurtEffect;

	// Cost -- (double) -- number of coins to award money after killed monster.
	public double rewardCost;

	public Monster() {
		life = LIVE;
	}
	
	public Monster(MatrixLocation2d put, Size2d size) {
		this.putting = put;
		this.spriteSize = size;
		life = LIVE;
	}
	
	
}
