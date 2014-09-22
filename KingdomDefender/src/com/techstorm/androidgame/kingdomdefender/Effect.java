package com.techstorm.androidgame.kingdomdefender;

public class Effect {
	// Effect (integer):
	// -- Slow: (theo % và thời gian slow), 2 Options:
	// --- % slow speed (double)
	// --- time in second (double)
	// -- Poison (máu / giây || % máu / giây), 2 Options:
	// --- máu / giây (double)
	// --- % máu / giây (double)
	// -- Stun (số giây)
	// ------- NOTE: (Slow 5s + Stun 2s) = Stun 2s + Slow 3s.

	public static final Integer NO_EFFECT = Integer.valueOf(0);
	public static final Integer SLOW = Integer.valueOf(1);
	public static final Integer POISON = Integer.valueOf(2);
	public static final Integer STUN = Integer.valueOf(3);

	// Constant for SLOW
	public static final Integer SLOW_TIME_AMOUNT = Integer.valueOf(0);
	public static final Integer SLOW_TIME_PERCENTAGE = Integer.valueOf(1);

	// Constant for POISON
	public static final Integer POISON_DAMAGE_AMOUNT = Integer.valueOf(0);
	public static final Integer POISON_DAMAGE_PERCENTAGE = Integer.valueOf(1);

	public Integer targetEffect;

	// Property for SLOW effect
	public Integer slowTimeType;
	public double slowTime;
	public double slowPercentage;

	// Property for POISON effect
	public double poisonEffectTime;
	public Integer poisonDamageType;
	public double poisonHearthAmountDamageOnSecond;
	public double poisonHearthPercentageDamageOnSecond;

	// Property for STUN effect
	public double stunEffectTime;

	public Effect(Integer effect) {
		targetEffect = effect;

		if (NO_EFFECT.equals(effect)) {
			// do nothing
		} else if (SLOW.equals(effect)) {
			applyEffectSlow(SLOW_TIME_AMOUNT, 3);
		} else if (POISON.equals(effect)) {
			applyEffectPoison(6, POISON_DAMAGE_AMOUNT, 10);
		} else if (STUN.equals(effect)) {
			applyEffectStun(10);
		}
	}

	// set slow effect information
	public void applyEffectSlow(Integer slowTimeType, double slowTimeValue) {
		this.targetEffect = SLOW;
		this.slowTimeType = slowTimeType;
		if (SLOW_TIME_AMOUNT.equals(slowTimeType)) {
			this.slowTime = slowTimeValue;
		} else if (SLOW_TIME_PERCENTAGE.equals(slowTimeType)) {
			this.slowPercentage = slowTimeValue;
		}
	}

	// set poison effect information
	public void applyEffectPoison(double effectTime, Integer damageType,
			double damageValue) {
		this.targetEffect = POISON;
		this.poisonEffectTime = effectTime;
		this.poisonDamageType = damageType;
		if (POISON_DAMAGE_AMOUNT.equals(damageType)) {
			this.poisonHearthAmountDamageOnSecond = damageValue;
		} else if (POISON_DAMAGE_PERCENTAGE.equals(damageType)) {
			this.poisonHearthPercentageDamageOnSecond = damageValue;
		}
	}

	// set stun effect information
	public void applyEffectStun(double effectTime) {
		this.targetEffect = STUN;
		this.stunEffectTime = effectTime;
	}
}
