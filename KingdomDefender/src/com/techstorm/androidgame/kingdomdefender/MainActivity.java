package com.techstorm.androidgame.kingdomdefender;

import java.util.ArrayList;
import java.util.List;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
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
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.StrokeFont;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.source.AssetBitmapTextureAtlasSource;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import android.graphics.Color;
import android.graphics.Typeface;

public class MainActivity extends SimpleBaseGameActivity implements
		IOnSceneTouchListener {

	// ===========================================================
	// Constants
	// ===========================================================

	private static final int FONT_SIZE = 48;
	
	// ===========================================================
	// Fields
	// ===========================================================

	private StrokeFont mStrokeFont;
	
	private KingDefGame game;
	private List<AnimatedSprite> monsters;
	private List<AnimatedSprite> towers;
	private List<AnimatedSprite> shopItems;
	private AnimatedSprite shopItemDragging;
	private Text textStroke;

	private Scene scene;
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
		final Camera camera = new Camera(0, 0, LayerConvertor.CAMERA_WIDTH,
				LayerConvertor.CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED,
				new RatioResolutionPolicy(LayerConvertor.CAMERA_WIDTH,
						LayerConvertor.CAMERA_HEIGHT), camera);
	}

	@Override
	public void onCreateResources() {
		this.game = new KingDefGame(this);
		this.monsters = new ArrayList<AnimatedSprite>();
		this.towers = new ArrayList<AnimatedSprite>();
		this.shopItems = new ArrayList<AnimatedSprite>();

		final ITexture strokeFontTexture = new BitmapTextureAtlas(this.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
		this.mStrokeFont = new StrokeFont(this.getFontManager(), strokeFontTexture, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), FONT_SIZE, true, Color.BLACK, 2, Color.WHITE);
		this.mStrokeFont.load();
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		this.mBitmapTextureAtlas = new BitmapTextureAtlas(
				this.getTextureManager(), 128, 128);
		this.mPlayerTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.mBitmapTextureAtlas, this,
						"player.png", 0, 0, 3, 4);
		this.mGrassBackground = new RepeatingSpriteBackground(
				LayerConvertor.CAMERA_WIDTH, LayerConvertor.CAMERA_HEIGHT,
				this.getTextureManager(), AssetBitmapTextureAtlasSource.create(
						this.getAssets(), "gfx/background_grass.png"),
				this.getVertexBufferObjectManager());
		this.mBitmapTextureAtlas.load();
	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());
		final Scene scene = new Scene();
		scene.setBackground(this.mGrassBackground);
		scene.setOnSceneTouchListener(this);
		scene.registerUpdateHandler(loop);
		this.scene = scene;
		/*
		 * Calculate the coordinates for the face, so its centered on the
		 * camera.
		 */
		game.loadLevelData();
		/* Create the sprite and add it to the scene. */
		createMonster(scene);
		createTower(scene);
		createShop(scene);
		createInformationBar(scene);

		return scene;
	}

	IUpdateHandler loop = new IUpdateHandler() {
		@Override
		public void reset() {
		}

		@Override
		public void onUpdate(float pSecondsElapsed) {
			// Tower shoot automatically in range
			for (AnimatedSprite tower : towers) {
				MatrixLocation2d monsterPutting = 
						LayerConvertor.graphicLocationToMaxtrix2d(new Location2d(monsters.get(0).getX(), monsters.get(0).getY()));
				for (AnimatedSprite monster : monsters) {
					if (game.canShoot(tower.getTag(), monster.getTag(), monsterPutting)) {
						createShooter(scene, tower.getTag(), monster.getTag(), tower, monster
								.getX(), monster.getY());
					}
				}
			}

		}

	};

	@Override
	public boolean onSceneTouchEvent(Scene pScene,
			final TouchEvent pSceneTouchEvent) {
		if (shopItemDragging != null) {
			shopItemDragging.setPosition(pSceneTouchEvent.getX()
					- shopItemDragging.getWidth() / 2, pSceneTouchEvent.getY()
					- shopItemDragging.getHeight() / 2);
		}
		if (pSceneTouchEvent.isActionUp()) {
			// execute action.
			if (shopItemDragging != null) {
				createTowerBought(shopItemDragging.getTag(), 
						shopItemDragging.getX(), shopItemDragging.getY(), 
						shopItemDragging.getWidth(), shopItemDragging.getHeight());
				shopItemDragging = null;
				
				textStroke.setText(String.valueOf(game.getCurrentMoney()));
			}
		}
		return true;
	}

	// ===========================================================
	// Methods
	// ===========================================================
	public void createShooter(final Scene scene, final int towerIndex, final int monsterIndex, 
			AnimatedSprite tower, float x, float y) {
		
		final AnimatedSprite sprite = new AnimatedSprite(tower.getX(),
				tower.getY(), tower.getWidth(), tower.getHeight(),
				this.mPlayerTextureRegion, this.getVertexBufferObjectManager());
		final Path path = new Path(2);
		path.to(tower.getX(), tower.getY());
		path.to(x, y);

		sprite.registerEntityModifier(new PathModifier(0.5f, path, null,
				new IPathModifierListener() {
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
						mEngine.runOnUpdateThread(new Runnable() {
							@Override
							public void run() {
								int monsterStatus = game.shoot(towerIndex, monsterIndex);
//								if (monsterStatus == Monster.DEAD) {
//									AnimatedSprite monster =  getGraphicMonster(monsterIndex);
//									monsters.remove(monster);
//									scene.detachChild(monster);
//								}
								scene.detachChild(sprite);
							}
						});

					}
				}));
		scene.attachChild(sprite);
	}

	private void createMonster(Scene scene) {
		for (int index = 0; index < game.getCurrentMonsters().size(); index++) {
			Monster monster = game.getCurrentMonsters().get(index);
		
			final AnimatedSprite sprite = new AnimatedSprite(
					monster.putting.columnIndex, monster.putting.rowIndex,
					monster.spriteSize.width, monster.spriteSize.height,
					this.mPlayerTextureRegion,
					this.getVertexBufferObjectManager());

			MatrixLocation2d[] locs = game.getCurrentMonsterPath();
			final Path path = new Path(locs.length);
			for (MatrixLocation2d point : locs) {
				Location2d locat2d = LayerConvertor
						.maxtrixToGraphicLocation2d(point);
				path.to(locat2d.px, locat2d.py);
			}

			sprite.registerEntityModifier(new LoopEntityModifier(
					new PathModifier(30, path, null,
							new IPathModifierListener() {
								@Override
								public void onPathStarted(
										final PathModifier pPathModifier,
										final IEntity pEntity) {

								}

								@Override
								public void onPathWaypointStarted(
										final PathModifier pPathModifier,
										final IEntity pEntity,
										final int pWaypointIndex) {
									switch (pWaypointIndex) {
									case 0:
										sprite.animate(new long[] { 200, 200,
												200 }, 6, 8, true);
										break;
									case 1:
										sprite.animate(new long[] { 200, 200,
												200 }, 3, 5, true);
										break;
									case 2:
										sprite.animate(new long[] { 200, 200,
												200 }, 0, 2, true);
										break;
									case 3:
										sprite.animate(new long[] { 200, 200,
												200 }, 9, 11, true);
										break;
									}
								}

								@Override
								public void onPathWaypointFinished(
										final PathModifier pPathModifier,
										final IEntity pEntity,
										final int pWaypointIndex) {

								}

								@Override
								public void onPathFinished(
										final PathModifier pPathModifier,
										final IEntity pEntity) {

								}
							})));
			scene.attachChild(sprite);
			monsters.add(sprite);
			sprite.setTag(index);
		}
	}

	private void createInformationBar(final Scene scene) {
		final VertexBufferObjectManager vertexBufferObjectManager = this.getVertexBufferObjectManager();
		int textLength = 10;
		textStroke = new Text(300, 200, this.mStrokeFont, String.valueOf(game.getCurrentMoney()), textLength, vertexBufferObjectManager);
		scene.attachChild(textStroke);
	}
	
	private void createShop(final Scene scene) {
		float distance = 50f;
		if (game.getCurrentShop() != null && !game.getCurrentShop().isEmpty()) {
			for (int index = 0; index < game.getCurrentShop().size(); index++) {
				final AnimatedSprite sprite = new AnimatedSprite(
						LayerConvertor.CAMERA_WIDTH / 2 + distance,
						LayerConvertor.CAMERA_HEIGHT
								- LayerConvertor.CONVERTOR_HEIGHT_OF_SQUARE
								- 100,
						LayerConvertor.CONVERTOR_WIDTH_OF_SQUARE + 50,
						LayerConvertor.CONVERTOR_HEIGHT_OF_SQUARE + 50,
						this.mPlayerTextureRegion,
						this.getVertexBufferObjectManager()) {
					@Override
					public boolean onAreaTouched(
							final TouchEvent pSceneTouchEvent,
							final float pTouchAreaLocalX,
							final float pTouchAreaLocalY) {
						if (pSceneTouchEvent.isActionDown()) {
							if (game.canBuy(this.getTag())) {
								AnimatedSprite dragShopItem = createShopItemDragging(this.getTag(), 
										this.getX(), this.getY(), 
										this.getWidth(), this.getHeight());
								shopItemDragging = dragShopItem;
								scene.attachChild(dragShopItem);
							}
						}
						onSceneTouchEvent(scene, pSceneTouchEvent);
						return true;
					}

				};
				sprite.setTag(index);
				distance += 30;
				
				scene.registerTouchArea(sprite);
				scene.setTouchAreaBindingOnActionDownEnabled(true);
				scene.attachChild(sprite);
				shopItems.add(sprite);
			}
		}
	}

	private AnimatedSprite createShopItemDragging(int shopItemIndex, float graphicX, float graphicY, float width, float height) {
		final AnimatedSprite shopItem = new AnimatedSprite(
				graphicX, graphicY, width, height, mPlayerTextureRegion,
				this.getVertexBufferObjectManager());
		shopItem.setTag(shopItemIndex);
		return shopItem;
	}
	
	private AnimatedSprite createTowerBought(int shopItemIndex, float graphicX, float graphicY, float width, float height) {
		Location2d location2d = new Location2d(graphicX, graphicY); 
		game.createTower(shopItemIndex, LayerConvertor.graphicLocationToMaxtrix2d(location2d), width, height);
		final AnimatedSprite tower = new AnimatedSprite(
				graphicX, graphicY, width, height, mPlayerTextureRegion,
				this.getVertexBufferObjectManager());
		tower.setTag(towers.size());
		towers.add(tower);
		return tower;
	}
	
	private void createTower(Scene scene) {
		for (int index = 0; index < game.getCurrentTowers().size(); index++) {
			Tower tower = game.getCurrentTowers().get(index);
			Location2d loca = LayerConvertor
					.maxtrixToGraphicLocation2d(tower.putting);
			final AnimatedSprite sprite = new AnimatedSprite(loca.px, loca.py,
					tower.spriteSize.width, tower.spriteSize.height,
					this.mPlayerTextureRegion,
					this.getVertexBufferObjectManager());
			sprite.setTag(index);
			scene.attachChild(sprite);
			towers.add(sprite);
		}
	}

	private AnimatedSprite getGraphicMonster(int tag) {
		for (AnimatedSprite monster : monsters) {
			if (monster.getTag() == tag) {
				return monster;
			}
		}
		return null;
	}
	
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
