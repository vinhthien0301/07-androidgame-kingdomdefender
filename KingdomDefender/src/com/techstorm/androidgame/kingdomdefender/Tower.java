package com.techstorm.androidgame.kingdomdefender;

import java.util.List;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.vbo.ITiledSpriteVertexBufferObject;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

public class Tower extends AnimatedSprite {

	public Tower(float pX, float pY, float pWidth, float pHeight,
			ITiledTextureRegion pTiledTextureRegion,
			ITiledSpriteVertexBufferObject pTiledSpriteVertexBufferObject) {
		super(pX, pY, pWidth, pHeight, pTiledTextureRegion,
				pTiledSpriteVertexBufferObject);
		// TODO Auto-generated constructor stub
	}

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
