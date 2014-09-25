package com.techstorm.androidgame.kingdomdefender;

import java.util.ArrayList;
import java.util.List;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.PathModifier;
import org.andengine.entity.modifier.PathModifier.IPathModifierListener;
import org.andengine.entity.modifier.PathModifier.Path;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.RepeatingSpriteBackground;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.source.AssetBitmapTextureAtlasSource;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;

public class MainActivity extends SimpleBaseGameActivity implements IOnSceneTouchListener {

	// ===========================================================
	// Constants
	// ===========================================================

	

	// ===========================================================
	// Fields
	// ===========================================================

	private KingDefGame game;
	private List<AnimatedSprite> monsters;
	private List<AnimatedSprite> towers;
	
	private RepeatingSpriteBackground mGrassBackground;

	private BitmapTextureAtlas mBitmapTextureAtlas;
	private TiledTextureRegion mPlayerTextureRegion;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public EngineOptions onCreateEngineOptions() {
		final Camera camera = new Camera(0, 0, LayerConvertor.CAMERA_WIDTH, LayerConvertor.CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED,
				new RatioResolutionPolicy(LayerConvertor.CAMERA_WIDTH, LayerConvertor.CAMERA_HEIGHT), camera);
	}

	@Override
	public void onCreateResources() {
		this.game = new KingDefGame(this);
		this.monsters = new ArrayList<AnimatedSprite>();
		this.towers = new ArrayList<AnimatedSprite>();
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		this.mBitmapTextureAtlas = new BitmapTextureAtlas(
				this.getTextureManager(), 128, 128);
		this.mPlayerTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.mBitmapTextureAtlas, this,
						"player.png", 0, 0, 3, 4);
		this.mGrassBackground = new RepeatingSpriteBackground(LayerConvertor.CAMERA_WIDTH,
				LayerConvertor.CAMERA_HEIGHT, this.getTextureManager(),
				AssetBitmapTextureAtlasSource.create(this.getAssets(),
						"gfx/background_grass.png"),
				this.getVertexBufferObjectManager());
		this.mBitmapTextureAtlas.load();
	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());
		final Scene scene = new Scene();
		scene.setBackground(this.mGrassBackground);
		scene.setOnSceneTouchListener(this);

		/*
		 * Calculate the coordinates for the face, so its centered on the
		 * camera.
		 */
		game.loadLevelData();
		/* Create the sprite and add it to the scene. */
		createMonster(scene);
		createTower(scene);

		return scene;
	}
	
	@Override
	public boolean onSceneTouchEvent(Scene pScene, final TouchEvent pSceneTouchEvent)
	{
	    if (pSceneTouchEvent.isActionDown())
	    {
	        //execute action.
	    	createShooter(pScene, towers.get(0), pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
	    }
	    return false;
	}

	// ===========================================================
	// Methods
	// ===========================================================
	private void createShooter(final Scene scene, AnimatedSprite tower, float x, float y) {
		final AnimatedSprite sprite = new AnimatedSprite(tower.getX(), tower.getY(), tower.getWidth(),
				tower.getHeight(), this.mPlayerTextureRegion,
				this.getVertexBufferObjectManager());
		MatrixLocation2d[] locs = new MatrixLocation2d[1];
		locs[0] = new MatrixLocation2d(3, 4);
		final Path path = new Path(2);
		for (MatrixLocation2d point : locs) {
			Location2d locat2d = LayerConvertor.maxtrixToGraphicLocation2d(point);
			path.to(locat2d.px, locat2d.py);
		}
		path.to(x, y);
		scene.attachChild(sprite);
		
		sprite.registerEntityModifier(new PathModifier(
				0.5f, path, null, new IPathModifierListener() {
					@Override
					public void onPathStarted(final PathModifier pPathModifier,
							final IEntity pEntity) {
						
					}

					@Override
					public void onPathWaypointStarted(
							final PathModifier pPathModifier,
							final IEntity pEntity, final int pWaypointIndex) {

					}

					@Override
					public void onPathWaypointFinished(
							final PathModifier pPathModifier,
							final IEntity pEntity, final int pWaypointIndex) {

					}

					@Override
					public void onPathFinished(
							final PathModifier pPathModifier,
							final IEntity pEntity) {
						scene.detachChild(sprite);
					}
				}));
		
		
	}
	
	private void createMonster(Scene scene) {
		for (Monster monster : game.getCurrentMonsters()) {
			final AnimatedSprite sprite = new AnimatedSprite(monster.putting.columnIndex, monster.putting.rowIndex, monster.spriteSize.width,
					monster.spriteSize.height, this.mPlayerTextureRegion,
					this.getVertexBufferObjectManager());
			
			MatrixLocation2d[] locs = game.getCurrentMonsterPath();
			final Path path = new Path(locs.length);
			for (MatrixLocation2d point : locs) {
				Location2d locat2d = LayerConvertor.maxtrixToGraphicLocation2d(point);
				path.to(locat2d.px, locat2d.py);
			}

			sprite.registerEntityModifier(new LoopEntityModifier(new PathModifier(
					30, path, null, new IPathModifierListener() {
						@Override
						public void onPathStarted(final PathModifier pPathModifier,
								final IEntity pEntity) {
							
						}

						@Override
						public void onPathWaypointStarted(
								final PathModifier pPathModifier,
								final IEntity pEntity, final int pWaypointIndex) {
							switch (pWaypointIndex) {
							case 0:
								sprite.animate(new long[] { 200, 200, 200 }, 6, 8,
										true);
								break;
							case 1:
								sprite.animate(new long[] { 200, 200, 200 }, 3, 5,
										true);
								break;
							case 2:
								sprite.animate(new long[] { 200, 200, 200 }, 0, 2,
										true);
								break;
							case 3:
								sprite.animate(new long[] { 200, 200, 200 }, 9, 11,
										true);
								break;
							}
						}

						@Override
						public void onPathWaypointFinished(
								final PathModifier pPathModifier,
								final IEntity pEntity, final int pWaypointIndex) {

						}

						@Override
						public void onPathFinished(
								final PathModifier pPathModifier,
								final IEntity pEntity) {

						}
					})));
			scene.attachChild(sprite);
			monsters.add(sprite);
		}
	}
	
	private void createTower(Scene scene) {
		for (Tower tower : game.getCurrentTowers()) {
			Location2d loca = LayerConvertor.maxtrixToGraphicLocation2d(tower.putting);
			final AnimatedSprite sprite = new AnimatedSprite(loca.px, loca.py, tower.spriteSize.width,
					tower.spriteSize.height, this.mPlayerTextureRegion,
					this.getVertexBufferObjectManager());
			
			scene.attachChild(sprite);
			towers.add(sprite);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
