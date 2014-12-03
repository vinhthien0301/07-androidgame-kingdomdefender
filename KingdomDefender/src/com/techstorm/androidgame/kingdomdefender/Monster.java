package com.techstorm.androidgame.kingdomdefender;

import java.util.List;

public class Monster {

	public static final Integer DEAD = Integer.valueOf(0);
	public static final Integer LIVE = Integer.valueOf(1);

	public static final Integer GREEN_LIFE = Integer.valueOf(0);
	public static final Integer YELLOW_LIFE = Integer.valueOf(1);
	public static final Integer RED_LIFE = Integer.valueOf(2);

	public int number;

	public Integer life;

	public String fileName;

	public Integer moveSpeed;

	public MatrixLocation2d matrixLocation;

	public Size2d spriteSize;

	public MatrixSize2d matrixSize;

	public String name;

	// Hearth blood
	public int hp;
	public int hpMax;

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
		this.matrixLocation = put;
		this.spriteSize = size;
		life = LIVE;
	}

	public float getLifePercentage() {
		return (float) hp / hpMax;
	}

	public int getLifePeriod() {
		float lifePercentage = getLifePercentage();
		if (lifePercentage < 0.3) {
			return RED_LIFE;
		}
		if (lifePercentage < 0.7) {
			return YELLOW_LIFE;
		}
		return GREEN_LIFE;
	}

	public boolean isIntersection(MatrixLocation2d location, MatrixSize2d size) {
		return LayerConvertor.isIntersection(this.matrixLocation,
				this.matrixSize, location, size);
	}

	public boolean isIntersectionRelative(MatrixLocation2d location,
			MatrixSize2d size) {
		return LayerConvertor.isIntersection(this.matrixLocation,
				this.matrixSize, location, size)

				&& LayerConvertor.isIntersection(new MatrixLocation2d(
						matrixLocation.columnIndex + matrixSize.height - 1,
						matrixLocation.rowIndex),

				new MatrixSize2d(1, matrixSize.height), location, size)

				&& LayerConvertor.isIntersection(new MatrixLocation2d(
						matrixLocation.columnIndex, matrixLocation.rowIndex
								+ matrixSize.height - 1),

				new MatrixSize2d(matrixSize.width, 1), location, size);
	}
}
