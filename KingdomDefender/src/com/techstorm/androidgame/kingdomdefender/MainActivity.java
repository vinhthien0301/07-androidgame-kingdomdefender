package com.techstorm.androidgame.kingdomdefender;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.PathModifier;
import org.andengine.entity.modifier.PathModifier.IPathModifierListener;
import org.andengine.entity.modifier.PathModifier.Path;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.RepeatingSpriteBackground;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.StrokeFont;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.source.AssetBitmapTextureAtlasSource;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.color.Color;

import android.R.integer;
import android.graphics.Typeface;

public class MainActivity extends SimpleBaseGameActivity implements
		IOnSceneTouchListener {

	// ===========================================================
	// Constants
	// ===========================================================

	private static final int FONT_SIZE = 48;
	private static final int MAX_TIME = 60;
	private static final Color RED_COLOR = new Color(1f, 0, 0, 70);
	private static final Color GREEN_COLOR = new Color(0, 1f, 0, 70);

	// ===========================================================
	// Fields
	// ===========================================================

	private boolean canBuyShop = true;
	private StrokeFont mStrokeFont;

	private KingDefGame game;
	private AStarAlgorithm pathAlgorithm;
	private Tower tow;
	private List<AnimatedSprite> monsters;
	private List<AnimatedSprite> towers;
	private List<AnimatedSprite> shopItems;
	private AnimatedSprite shopItemDragging;
	private Text textStroke;
	private Rectangle itemCover;
	private Scene scene;
	private RepeatingSpriteBackground mGrassBackground;
	private Map<AnimatedSprite, Rectangle> healthBarMap;
	private Map<Integer, AnimatedSprite> rangeSpriteMap;
	private Map<String, TiledTextureRegion> textureRegionMap;
	private BitmapTextureAtlas mBitmapTextureAtlas;
	private TiledTextureRegion mPlayerTextureRegion;
	private BitmapTextureAtlas mHpTextureAtlas;
	private BitmapTextureAtlas mCircleTextureAtlas;
	private TiledTextureRegion mCircleTextureRegion;
	private BitmapTextureAtlas mBoundTextureAtlas;
	private TiledTextureRegion mBoundTextureRegion;
	private AnimatedSprite spriteCircleMain;
	private float a;
	private BitmapTextureAtlas mMoneyTextureAtlas;
	private TiledTextureRegion mMoneyTextureRegion;
	private float b,c;
	
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
		this.pathAlgorithm = new AStarAlgorithm(this.game);
		this.monsters = new ArrayList<AnimatedSprite>();
		this.towers = new ArrayList<AnimatedSprite>();
		this.shopItems = new ArrayList<AnimatedSprite>();

		this.healthBarMap = new HashMap<AnimatedSprite, Rectangle>();
		this.rangeSpriteMap = new HashMap<Integer, AnimatedSprite>();
		
		final ITexture strokeFontTexture = new BitmapTextureAtlas(
				this.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
		this.mStrokeFont = new StrokeFont(this.getFontManager(),
				strokeFontTexture, Typeface.create(Typeface.DEFAULT,
						Typeface.BOLD), FONT_SIZE, true, Color.BLACK, 2,
				Color.WHITE);
		this.mStrokeFont.load();

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		this.mBitmapTextureAtlas = new BitmapTextureAtlas(
				this.getTextureManager(), 128, 128);

		this.mPlayerTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.mBitmapTextureAtlas, this,
						"player.png", 0, 0, 3, 4);

		this.mHpTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(),
				200, 50);

		this.mHpTextureAtlas.load();
		
		this.mBoundTextureAtlas = new BitmapTextureAtlas(
				this.getTextureManager(), 64, 64);
		this.mBoundTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.mBoundTextureAtlas, this,
						"box.png", 0, 0, 1, 1);
		this.mBoundTextureAtlas.load();
		
		this.mCircleTextureAtlas = new BitmapTextureAtlas(
				this.getTextureManager(), 64, 64);

		this.mCircleTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.mCircleTextureAtlas, this,
						"circle.png", 0, 0, 1, 1);
		this.mCircleTextureAtlas.load();
		this.mMoneyTextureAtlas = new BitmapTextureAtlas(
				this.getTextureManager(), 64, 64);

		this.mMoneyTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.mMoneyTextureAtlas, this,
						"money.png", 0, 0, 1, 1);
		this.mMoneyTextureAtlas.load();

		this.textureRegionMap = new HashMap<String, TiledTextureRegion>();
		for (Tower tower : game.shopItems) {
			BitmapTextureAtlas nBitmapTextureAtlas = new BitmapTextureAtlas(
					this.getTextureManager(), tower.imageWidth,
					tower.imageHeight);

			TiledTextureRegion tiledTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createTiledFromAsset(nBitmapTextureAtlas, this,
							tower.fileName, 0, 0, tower.pTileColumn,
							tower.pTileRow);
			this.textureRegionMap.put(createTextureRegionKeyMap(tower, null),
					tiledTextureRegion);
			nBitmapTextureAtlas.load();
		}
		this.mGrassBackground = new RepeatingSpriteBackground(
				LayerConvertor.CAMERA_WIDTH, LayerConvertor.CAMERA_HEIGHT,
				this.getTextureManager(), AssetBitmapTextureAtlasSource.create(
						this.getAssets(), "gfx/background_grass.png"),
				this.getVertexBufferObjectManager());
		this.mBitmapTextureAtlas.load();

	}

	private String createTextureRegionKeyMap(Tower tower, Monster monster) {
		StringBuilder key = new StringBuilder();
		if (tower != null) {
			key.append("shop");
			key.append("-");
			key.append(tower.number);
		}
		if (monster != null) {
			key.append("monster");
			key.append("-");
			key.append(monster.number);
		}
		return key.toString();
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
		/* Create the sprite and add it to the scene. */
		createSquare(scene);
		createBounds(scene);
		initMonsters(scene);
		createTower(scene);
		createShop(scene);
		createInformationBar(scene);
		createMoneyIcon(scene);

		
		return scene;
	}
	private void createMoneyIcon(Scene scene)
	{
		final AnimatedSprite spriteMoney = new AnimatedSprite(LayerConvertor.CAMERA_WIDTH
				/ LayerConvertor.CONVERTOR_WIDTH_OF_SQUARE * 4,
				0, LayerConvertor.CONVERTOR_WIDTH_OF_SQUARE*2, 
				LayerConvertor.CONVERTOR_HEIGHT_OF_SQUARE *2,
				mMoneyTextureRegion,
				this.getVertexBufferObjectManager());
		scene.attachChild(spriteMoney);
		
	}
	private void createBounds(Scene scene) {
		int numberOfColumnLines = LayerConvertor.CAMERA_WIDTH
				/ LayerConvertor.CONVERTOR_WIDTH_OF_SQUARE;
		int numberOfRowLines = LayerConvertor.CAMERA_HEIGHT
				/ LayerConvertor.CONVERTOR_HEIGHT_OF_SQUARE;
		
		float bottomBound = (numberOfRowLines - 1) * LayerConvertor.CONVERTOR_HEIGHT_OF_SQUARE;
		for (int columnIndex = 0; columnIndex < numberOfColumnLines; columnIndex++) {
			float columnPosition = columnIndex
					* LayerConvertor.CONVERTOR_WIDTH_OF_SQUARE;
			AnimatedSprite bound = new AnimatedSprite(columnPosition,
					0, LayerConvertor.CONVERTOR_WIDTH_OF_SQUARE, 
					LayerConvertor.CONVERTOR_HEIGHT_OF_SQUARE,
					mBoundTextureRegion,
					this.getVertexBufferObjectManager());
			scene.attachChild(bound);
			
			bound = new AnimatedSprite(columnPosition,
					bottomBound, LayerConvertor.CONVERTOR_WIDTH_OF_SQUARE, 
					LayerConvertor.CONVERTOR_HEIGHT_OF_SQUARE,
					mBoundTextureRegion,
					this.getVertexBufferObjectManager());
			scene.attachChild(bound);
		}
		
		float rightBound = (numberOfColumnLines) * LayerConvertor.CONVERTOR_WIDTH_OF_SQUARE;
		for (int rowIndex = 0; rowIndex < numberOfRowLines; rowIndex++) {
			float rowPosition = rowIndex
					* LayerConvertor.CONVERTOR_HEIGHT_OF_SQUARE;
			AnimatedSprite bound = new AnimatedSprite(0,
					rowPosition, LayerConvertor.CONVERTOR_WIDTH_OF_SQUARE, 
					LayerConvertor.CONVERTOR_HEIGHT_OF_SQUARE,
					mBoundTextureRegion,
					this.getVertexBufferObjectManager());
			scene.attachChild(bound);
			
			bound = new AnimatedSprite(rightBound,
					rowPosition, LayerConvertor.CONVERTOR_WIDTH_OF_SQUARE, 
					LayerConvertor.CONVERTOR_HEIGHT_OF_SQUARE,
					mBoundTextureRegion,
					this.getVertexBufferObjectManager());
			scene.attachChild(bound);
		}
	}

	private void createSquare(Scene scene) {
		// final Line line = new Line(x1, y1, x2, y2, lineWidth);
		int numberOfColumnLines = LayerConvertor.CAMERA_WIDTH
				/ LayerConvertor.CONVERTOR_WIDTH_OF_SQUARE + 1;
		int numberOfRowLines = LayerConvertor.CAMERA_HEIGHT
				/ LayerConvertor.CONVERTOR_HEIGHT_OF_SQUARE;
		for (int columnIndex = 0; columnIndex < numberOfColumnLines; columnIndex++) {
			float columnPosition = columnIndex
					* LayerConvertor.CONVERTOR_WIDTH_OF_SQUARE;
			final Line line = new Line(columnPosition, 0, columnPosition,
					LayerConvertor.CAMERA_HEIGHT,
					this.getVertexBufferObjectManager());
			scene.attachChild(line);
		}
		for (int rowIndex = 0; rowIndex < numberOfRowLines; rowIndex++) {
			float rowPosition = rowIndex
					* LayerConvertor.CONVERTOR_HEIGHT_OF_SQUARE;
			final Line line = new Line(0, rowPosition,
					LayerConvertor.CAMERA_WIDTH, rowPosition,
					this.getVertexBufferObjectManager());
			scene.attachChild(line);
		}

	}

	IUpdateHandler loop = new IUpdateHandler() {
		@Override
		public void reset() {
		}

		@Override
		public void onUpdate(float pSecondsElapsed) {
			// Check buying tower item.
			checkConfliction();

			// Update position for monsters.
			updateMonstersMoving();

			// Tower shoot automatically in range
			for (int index = 0; index < towers.size(); index++) {
				AnimatedSprite tower = towers.get(index);
				Tower gameTower = game.getTower(tower.getTag());
				if (gameTower == null) {
					continue;
				}

				// Find a nearest target.
				if (gameTower.target == null) {
					Monster nearestMonster = game.getNearestMonster(gameTower);
					if (nearestMonster == null) {
						continue;
					}
					gameTower.target = nearestMonster;
				}

				Monster monster = gameTower.target;
				// Shooting target.
				if (game.canShoot(pSecondsElapsed, gameTower, monster)) {
					createShooter(
							scene,
							gameTower.number,
							monster.number,
							tower,
							LayerConvertor
									.maxtrixToGraphicLocation2d(monster.matrixLocation));
				} else if (game.isInMonsterList(monster)
						|| !gameTower.isInShootRange(monster)) {
					gameTower.target = null;
				}
			}

		}

	};

	public void updateMonstersMoving() {
		if (monsters != null && !monsters.isEmpty()) {
			for (AnimatedSprite monster : monsters) {
				updateMonsterMoving(monster.getX(), monster.getY(), monster.getTag());
			}
		}
	}

	private void updateMonsterMoving(float locationX, float locationY, int monsterNumber) {
		MatrixLocation2d newMatrixLoc = new MatrixLocation2d(
				(int) locationX
						/ LayerConvertor.CONVERTOR_WIDTH_OF_SQUARE,
				(int) locationY
						/ LayerConvertor.CONVERTOR_HEIGHT_OF_SQUARE);
		game.updateMonsterMoving(monsterNumber, newMatrixLoc);
	}
	
	@Override
	public boolean onSceneTouchEvent(Scene pScene,
			final TouchEvent pSceneTouchEvent) {
		
		if (shopItemDragging != null) {

			float currentX = pSceneTouchEvent.getX()
					- shopItemDragging.getWidth() / 2;
			float currentY = pSceneTouchEvent.getY()
					- shopItemDragging.getHeight() / 2;
			MatrixLocation2d locat = new MatrixLocation2d((int) currentX
					/ LayerConvertor.CONVERTOR_WIDTH_OF_SQUARE, (int) currentY
					/ LayerConvertor.CONVERTOR_HEIGHT_OF_SQUARE);
			Location2d graLocat = LayerConvertor
					.maxtrixToGraphicLocation2d(locat);
			itemCover.setPosition(graLocat.px, graLocat.py);
			shopItemDragging.setPosition(graLocat.px, graLocat.py);
			// spriteCircleMain.setPosition(graLocat.px, graLocat.py);
			spriteCircleMain
					.setPosition(
							graLocat.px
									- (a
											* (LayerConvertor.CONVERTOR_WIDTH_OF_SQUARE) - (shopItemDragging
											.getWidth() / 2)),
							graLocat.py
									- (a
											* (LayerConvertor.CONVERTOR_HEIGHT_OF_SQUARE) - (shopItemDragging
											.getHeight() / 2)));
			
		} else {
			
			// int monsterCharacterIndex = 0;
			// Monster monster = game.createMonster(monsterCharacterIndex,
			// LayerConvertor.graphicLocationToMaxtrix2d(
			// new Location2d(pSceneTouchEvent.getX(),
			// pSceneTouchEvent.getY())),
			// new Size2d(48, 64));
			// createMonster(scene, monster, monster.number);
		}
		
		if (pSceneTouchEvent.isActionUp()) {
			// execute action.

			if (shopItemDragging != null) {
				if (canBuyShop) {
					createTowerBought(shopItemDragging.getTiledTextureRegion(),
							shopItemDragging.getTag(), shopItemDragging.getX(),
							shopItemDragging.getY(),
							shopItemDragging.getWidth(),
							shopItemDragging.getHeight());
					
					shopItemDragging = null;
					textStroke.setText(String.valueOf(game.getCurrentMoney()));
				} else {
					scene.detachChild(shopItemDragging);
				}
				scene.detachChild(itemCover);
				scene.detachChild(spriteCircleMain);

			}
			
		}
		
		return true;
	}

	private void checkConfliction() {
		if (shopItemDragging != null) {
			Location2d location = new Location2d(shopItemDragging.getX(),
					shopItemDragging.getY());
			MatrixLocation2d locat = LayerConvertor
					.graphicLocationToMaxtrix2d(location);
			Tower towerInfo = game.getShopItem(shopItemDragging.getTag());
			boolean conflicted = game.isTowerConflicted(locat, towerInfo.matrixSize) 
					|| game.isTowerConflicted(locat, towerInfo.matrixSize);
			
			if (conflicted) {
				itemCover.setColor(RED_COLOR);
			} else {
				itemCover.setColor(GREEN_COLOR);
			}

			canBuyShop = !conflicted;
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================
	public void createShooter(final Scene scene, final int towerIndex,
			final int monsterIndex, AnimatedSprite tower, Location2d location2d) {

		final AnimatedSprite sprite = new AnimatedSprite(tower.getX(),
				tower.getY(), tower.getWidth(), tower.getHeight(),
				tower.getTiledTextureRegion(),
				this.getVertexBufferObjectManager());
		final Path path = new Path(2);
		path.to(tower.getX(), tower.getY());
		path.to(location2d.px, location2d.py);

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
								int monsterStatus = game.shoot(towerIndex,
										monsterIndex);

								AnimatedSprite monster = null;
								monster = getGraphicMonster(monsterIndex);

								if (monsterStatus == Monster.DEAD) {
									if (monsters != null && !monsters.isEmpty()
											&& monster != null) {
										monsters.remove(monster);
										scene.detachChild(monster);
										scene.detachChild(healthBarMap
												.get(monster));
										healthBarMap.remove(monster);
										int i=Integer.parseInt(String.valueOf(game.getCurrentMoney()).replaceAll("[\\D]", ""));
										i+=1;
										textStroke.setText(String.valueOf(i));
									}
								} else {
									if (monster != null) {
										Rectangle healthBar = healthBarMap
												.get(monster);
										Monster monsterInfo = game
												.getMonster(monsterIndex);
										healthBar.setWidth(monster.getWidth()
												* monsterInfo
														.getLifePercentage());
										healthBar
												.setColor(parseColor(monsterInfo
														.getLifePeriod()));
									}
								}
								scene.detachChild(sprite);
							}
						});

					}
				}));
		scene.attachChild(sprite);
	}

	private Color parseColor(int colorLife) {
		if (Monster.GREEN_LIFE == colorLife) {
			return Color.GREEN;
		} else if (Monster.YELLOW_LIFE == colorLife) {
			return Color.YELLOW;
		}
		return Color.RED;
	}

	private AnimatedSprite createMonsterAutoGoing(final Scene scene, final Monster monster,
			final int tagIndex) {
		final AnimatedSprite sprite = new AnimatedSprite(
				monster.matrixLocation.columnIndex,
				monster.matrixLocation.rowIndex,
				LayerConvertor.CONVERTOR_WIDTH_OF_SQUARE
						* monster.matrixSize.width,
				LayerConvertor.CONVERTOR_HEIGHT_OF_SQUARE
						* monster.matrixSize.height, this.mPlayerTextureRegion,
				this.getVertexBufferObjectManager());

		final Rectangle healthBar = new Rectangle(0, 0,
				LayerConvertor.CONVERTOR_WIDTH_OF_SQUARE
						* monster.matrixSize.width, 5,
				this.getVertexBufferObjectManager());
		healthBar.setColor(Color.GREEN);
		
		
		IPathModifierListener pathListener = new IPathModifierListener() {
			@Override
			public void onPathStarted(final PathModifier pPathModifier,
					final IEntity pEntity)
			{

			}

			@Override
			public void onPathWaypointStarted(final PathModifier pPathModifier,
					final IEntity pEntity, final int pWaypointIndex) {

				switch (pWaypointIndex) {
				case 0:
					// move down
					sprite.animate(new long[] { 200, 200, 200 }, 6, 8, true);

					break;
				case 1:
					// move right
					sprite.animate(new long[] { 200, 200, 200 }, 3, 5, true);
					break;
				case 2:
					// move up
					sprite.animate(new long[] { 200, 200, 200 }, 0, 2, true);
					break;
				case 3:
					// move left
					sprite.animate(new long[] { 200, 200, 200 }, 9, 11, true);
					break;
				}
			}

			@Override
			public void onPathWaypointFinished(
					final PathModifier pPathModifier, final IEntity pEntity,
					final int pWaypointIndex) {
				
			}

			@Override
			public void onPathFinished(final PathModifier pPathModifier,
					final IEntity pEntity) {
				
				Path tempPath = pPathModifier.getPath();
				float endX = tempPath.getCoordinatesX()[tempPath.getSize() - 1];
				float endY = tempPath.getCoordinatesY()[tempPath.getSize() - 1];
				updateMonsterMoving(endX, endY, monster.number);
				
				List<MatrixLocation2d> locs = pathAlgorithm.calcNextMoveMonster(tagIndex);
				if (locs != null && locs.size() > 1) {
					Path path = LayerConvertor.matrixToPath(locs);
					sprite.registerEntityModifier(new PathModifier(MAX_TIME
							/ monster.moveSpeed, path, null, this));
					healthBar.registerEntityModifier(new PathModifier(MAX_TIME
							/ monster.moveSpeed, path));
				}
			}
		};
		
		List<MatrixLocation2d> locs = pathAlgorithm.calcNextMoveMonster(tagIndex);
		if (locs != null && locs.size() > 1) {
			Path path = LayerConvertor.matrixToPath(locs);
			sprite.registerEntityModifier(new PathModifier(MAX_TIME
					/ monster.moveSpeed, path, null, pathListener));
			healthBar.registerEntityModifier(new PathModifier(MAX_TIME
					/ monster.moveSpeed, path));
		}
		
		scene.attachChild(healthBar);
		scene.attachChild(sprite);
		healthBarMap.put(sprite, healthBar);
		monsters.add(sprite);
		sprite.setTag(tagIndex);
		return sprite;

	}
	
	private AnimatedSprite createMonsterWithPath(final Scene scene, Monster monster,
			int tagIndex) {
		b = LayerConvertor.CONVERTOR_WIDTH_OF_SQUARE
				* monster.matrixSize.width;
		c = LayerConvertor.CONVERTOR_HEIGHT_OF_SQUARE
				* monster.matrixSize.height;
		final AnimatedSprite sprite = new AnimatedSprite(
				monster.matrixLocation.columnIndex,
				monster.matrixLocation.rowIndex,
				LayerConvertor.CONVERTOR_WIDTH_OF_SQUARE
						* monster.matrixSize.width,
				LayerConvertor.CONVERTOR_HEIGHT_OF_SQUARE
						* monster.matrixSize.height, this.mPlayerTextureRegion,
				this.getVertexBufferObjectManager());

		MatrixLocation2d[] locs = game.getCurrentMonsterPath();
		final Path path = new Path(locs.length);
		for (MatrixLocation2d point : locs) {
			Location2d locat2d = LayerConvertor
					.maxtrixToGraphicLocation2d(point);
			path.to(locat2d.px, locat2d.py);
		}

		final Path path1 = new Path(locs.length);
		for (MatrixLocation2d point : locs) {
			Location2d locat2d = LayerConvertor
					.maxtrixToGraphicLocation2d(point);
			path1.to(locat2d.px, locat2d.py);
		}

		sprite.registerEntityModifier(new PathModifier(MAX_TIME
				/ monster.moveSpeed, path, null, new IPathModifierListener() {
			@Override
			public void onPathStarted(final PathModifier pPathModifier,
					final IEntity pEntity)

			{

			}

			@Override
			public void onPathWaypointStarted(final PathModifier pPathModifier,
					final IEntity pEntity, final int pWaypointIndex) {

				switch (pWaypointIndex) {
				case 0:
					// move down
					sprite.animate(new long[] { 200, 200, 200 }, 6, 8, true);

					break;
				case 1:
					// move right
					sprite.animate(new long[] { 200, 200, 200 }, 3, 5, true);
					break;
				case 2:
					// move up
					sprite.animate(new long[] { 200, 200, 200 }, 0, 2, true);
					break;
				case 3:
					// move left
					sprite.animate(new long[] { 200, 200, 200 }, 9, 11, true);
					break;
				}
			}

			@Override
			public void onPathWaypointFinished(
					final PathModifier pPathModifier, final IEntity pEntity,
					final int pWaypointIndex) {

			}

			@Override
			public void onPathFinished(final PathModifier pPathModifier,
					final IEntity pEntity) {
				mEngine.runOnUpdateThread(new Runnable() {
					@Override
					public void run() {
						game.removeMonster(sprite.getTag());
						monsters.remove(sprite);
						scene.detachChild(sprite);
					}
				});
			}
		}));

		final Rectangle healthBar = new Rectangle(0, 0,
				LayerConvertor.CONVERTOR_WIDTH_OF_SQUARE
						* monster.matrixSize.width, 5,
				this.getVertexBufferObjectManager());
		healthBar.setColor(Color.GREEN);
		healthBar.registerEntityModifier(new PathModifier(MAX_TIME
				/ monster.moveSpeed, path1, null, new IPathModifierListener() {
			@Override
			public void onPathStarted(final PathModifier pPathModifier,
					final IEntity pEntity)

			{

			}

			@Override
			public void onPathWaypointStarted(final PathModifier pPathModifier,
					final IEntity pEntity, final int pWaypointIndex) {

			}

			@Override
			public void onPathWaypointFinished(
					final PathModifier pPathModifier, final IEntity pEntity,
					final int pWaypointIndex) {

			}

			@Override
			public void onPathFinished(final PathModifier pPathModifier,
					final IEntity pEntity) {
				mEngine.runOnUpdateThread(new Runnable() {
					@Override
					public void run() {
						// game.removeMonster(healthBar.getTag());
						// monsters.remove(healthBar);
						scene.detachChild(healthBar);
					}
				});
			}
		}));
		scene.attachChild(healthBar);
		scene.attachChild(sprite);
		healthBarMap.put(sprite, healthBar);
		monsters.add(sprite);
		sprite.setTag(tagIndex);
		return sprite;

	}

	private AnimatedSprite createTowerBought(
			ITiledTextureRegion tiledTextureRegion, int shopItemIndex,
			float graphicX, float graphicY, float width, float height) {
		Location2d location2d = new Location2d(graphicX, graphicY);
		Tower gameTower = game.createTower(shopItemIndex,
				LayerConvertor.graphicLocationToMaxtrix2d(location2d), width,
				height);
		final AnimatedSprite tower = new AnimatedSprite(graphicX, graphicY,
				width, height, tiledTextureRegion,
				this.getVertexBufferObjectManager());
		rangeSpriteMap.put(gameTower.number, spriteCircleMain);
		tower.setTag(gameTower.number);
		towers.add(tower);
		return tower;
	}

	private void initMonsters(Scene scene) {
		for (int index = 0; index < game.getCurrentMonsters().size(); index++) {
			Monster monster = game.getCurrentMonsters().get(index);

			createMonsterAutoGoing(scene, monster, monster.number);
		}
	}

	private void createInformationBar(final Scene scene) {
		final VertexBufferObjectManager vertexBufferObjectManager = this
				.getVertexBufferObjectManager();
		int textLength = 10;
		textStroke = new Text(0, 0, this.mStrokeFont, String.valueOf(game
				.getCurrentMoney()), textLength, vertexBufferObjectManager);
		scene.attachChild(textStroke);
	}

	private void createShop(final Scene scene) {
		float distance = 50f;
		if (game.getCurrentShop() != null && !game.getCurrentShop().isEmpty()) {
			for (int index = 0; index < game.getCurrentShop().size(); index++) {
				final Tower tower = game.getShopItem(index);
				if (tower == null) {
					continue;
				}
				
				final AnimatedSprite sprite = new AnimatedSprite(
						LayerConvertor.CAMERA_WIDTH / 2 + distance,
						LayerConvertor.CAMERA_HEIGHT
								- LayerConvertor.CONVERTOR_HEIGHT_OF_SQUARE
								- 100, LayerConvertor.CONVERTOR_WIDTH_OF_SQUARE
								* tower.matrixSize.width,
						LayerConvertor.CONVERTOR_HEIGHT_OF_SQUARE
								* tower.matrixSize.height,
						this.textureRegionMap.get(createTextureRegionKeyMap(
								tower, null)),
						this.getVertexBufferObjectManager()) {
					@Override
					public boolean onAreaTouched(
							final TouchEvent pSceneTouchEvent,
							final float pTouchAreaLocalX,
							final float pTouchAreaLocalY) {
						if (pSceneTouchEvent.isActionDown()) {
							if (game.canBuy(this.getTag())) {
								spriteCircleMain = new AnimatedSprite(
										this.getX(),
										this.getY(),
										(float) tower.range
												* LayerConvertor.CONVERTOR_WIDTH_OF_SQUARE
												* 2,
										(float) tower.range
												* LayerConvertor.CONVERTOR_HEIGHT_OF_SQUARE
												* 2, mCircleTextureRegion,
										this.getVertexBufferObjectManager());
								rangeSpriteMap.put(-1, spriteCircleMain);
								
								AnimatedSprite dragShopItem = createShopItemDragging(
										this.getTiledTextureRegion(),
										this.getTag(), this.getX(),
										this.getY(), this.getWidth(),
										this.getHeight());
								
								itemCover = new Rectangle(
										pSceneTouchEvent.getX(),
										pSceneTouchEvent.getY(),
										this.getWidth(), this.getHeight(),
										this.getVertexBufferObjectManager());

								
								
								
								a = (float) tower.range;
								
								 scene.registerTouchArea(spriteCircleMain);

								scene.attachChild(spriteCircleMain);
								scene.attachChild(itemCover);
								shopItemDragging = dragShopItem;
								scene.attachChild(dragShopItem);
								
							}
						}
						onSceneTouchEvent(scene, pSceneTouchEvent);
						return true;
					}

				};
				sprite.setTag(index);
				distance += 100;
				scene.registerTouchArea(sprite);
				scene.setTouchAreaBindingOnActionDownEnabled(true);
				scene.attachChild(sprite);
				shopItems.add(sprite);
			}
		}
	}

	private AnimatedSprite createShopItemDragging(
			ITiledTextureRegion tiledTextureRegion, final int shopItemIndex,
			float graphicX, float graphicY, float width, float height) {
		final AnimatedSprite shopItem = new AnimatedSprite(graphicX, graphicY,
				width, height, tiledTextureRegion,
				this.getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(
					final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX,
					final float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionDown()) {
					AnimatedSprite circle = rangeSpriteMap.get(shopItemIndex);
					if (circle == null) {
						circle = rangeSpriteMap.get(-1);
					}
					if (!circle.hasParent()) {
						scene.attachChild(circle);
					} else {
						scene.detachChild(circle);
					}
				}
				onSceneTouchEvent(scene, pSceneTouchEvent);
				return true;
			}

		
		};
		scene.registerTouchArea(shopItem);
		scene.setTouchAreaBindingOnActionDownEnabled(true);
		shopItem.setTag(shopItemIndex);
		return shopItem;
	}

	public void onAreaTouched(final TouchEvent pSceneTouchEvent,
			final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
	}

	private void createTower(Scene scene) {
		for (int index = 0; index < game.getCurrentTowers().size(); index++) {
			Tower tower = game.getCurrentTowers().get(index);
			Location2d loca = LayerConvertor
					.maxtrixToGraphicLocation2d(tower.matrixLocation);
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
		if (monsters != null && !monsters.isEmpty()) {
			for (AnimatedSprite monster : monsters) {
				if (monster.getTag() == tag) {
					return monster;
				}
			}
		}
		return null;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
