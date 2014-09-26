package com.techstorm.androidgame.kingdomdefender.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.techstorm.androidgame.kingdomdefender.LayerConvertor;
import com.techstorm.androidgame.kingdomdefender.LevelMap;
import com.techstorm.androidgame.kingdomdefender.MatrixLocation2d;
import com.techstorm.androidgame.kingdomdefender.MatrixSize2d;
import com.techstorm.androidgame.kingdomdefender.Monster;
import com.techstorm.androidgame.kingdomdefender.Tower;
import com.techstorm.androidgame.kingdomdefender.Wave;
import com.techstorm.sqlite.SqliteExecutor;

public class DatabaseCreator {

	public static SQLiteDatabase database;
	
	public static SQLiteDatabase openDatabase(Context context) {
		DatabaseHelper myDbHelper = new DatabaseHelper(context);

		try {
			myDbHelper.createDataBase();

		} catch (IOException ioe) {
			throw new Error("Unable to create database");
		}

		try {
			database = myDbHelper.openDatabase();

		} catch (SQLException sqle) {
			throw sqle;
		}
		
		return database;
	}

	public static void getMap(LevelMap map) {
		Cursor c = SqliteExecutor.queryStatement(database, "K_MAP", "*");
		//If Cursor is valid
    	if (c != null ) {
    		//Move cursor to first row
    		if  (c.moveToFirst()) {
    			do {
    				map.matrixHeight = c.getInt(c.getColumnIndex("K_HEIGHT"));
    				map.matrixWidth = c.getInt(c.getColumnIndex("K_WIDTH"));
    				map.level = c.getInt(c.getColumnIndex("K_LEVEL"));
    				map.waveSize = c.getInt(c.getColumnIndex("K_WAVE_NUM"));
    				for (int index = 0; index < map.waveSize; index++) {
    					Wave wave = new Wave();
						map.waves.add(wave);
					}
    			}while (c.moveToNext()); //Move to next row
    		} 
    	}
	}
	
	public static void getMapPath(LevelMap map) {
		List<MatrixLocation2d> matrixLoc2ds = new ArrayList<MatrixLocation2d>();
		Cursor c = SqliteExecutor.queryStatement(database, "K_PATH", "*", "K_MAP_LEVEL = "+String.valueOf(map.level));
		//If Cursor is valid
    	if (c != null ) {
    		//Move cursor to first row
    		if  (c.moveToFirst()) {
    			do {
    				int columnIndex = c.getInt(c.getColumnIndex("K_COLUMN"));
    				int rowIndex = c.getInt(c.getColumnIndex("K_ROW"));
    				MatrixLocation2d loc = new MatrixLocation2d(columnIndex, rowIndex);
    				matrixLoc2ds.add(loc);

    			}while (c.moveToNext()); //Move to next row
    		}
    	}
    	
    	MatrixLocation2d[] locs = new MatrixLocation2d[matrixLoc2ds.size()];
    	map.setMonsterPath(matrixLoc2ds.toArray(locs));
	}
	
	public static void getMonster(LevelMap map) {
		int characterId = 0;
		Cursor c = SqliteExecutor.queryStatement(database, "K_MONSTER", "*", "K_MAP_LEVEL = "+String.valueOf(map.level));
		//If Cursor is valid
    	if (c != null ) {
    		//Move cursor to first row
    		if  (c.moveToFirst()) {
    			do {
    				final int centerX = (int)(LayerConvertor.CAMERA_WIDTH - 24) / 2;
    				final int centerY = (int)(LayerConvertor.CAMERA_HEIGHT - 32) / 2;
    				Monster monster = new Monster(new MatrixLocation2d(centerX, centerY), new MatrixSize2d(48, 64));
    				characterId = c.getInt(c.getColumnIndex("K_CHARACTER"));
    				int waveIndex = c.getInt(c.getColumnIndex("K_WAVE"));
    				Wave wave = map.waves.get(waveIndex - 1);
    				wave.addMonster(monster);
    				
    				Cursor cur = SqliteExecutor.queryStatement(database, "K_MONSTER_CHARACTER", "*", "K_PK = "+String.valueOf(characterId));
    				//If Cursor is valid
    		    	if (cur != null ) {
    		    		//Move cursor to first row
    		    		if  (cur.moveToFirst()) {
    		    			do {
    		    				monster.attackDamage = cur.getInt(cur.getColumnIndex("K_DAMAGE"));
    		    				monster.rewardCost = cur.getInt(cur.getColumnIndex("K_REWARD"));
    		    				monster.name = cur.getString(cur.getColumnIndex("K_NAME"));

    		    			}while (cur.moveToNext()); //Move to next row
    		    		}
    		    	}
    				
    			}while (c.moveToNext()); //Move to next row
    		}
    	}
    	
    	
	}
	
	public static List<Tower> getShopItems() {
		// new shop item list
		List<Tower> shopItems = new ArrayList<Tower>();
		
		Cursor cur = SqliteExecutor.queryStatement(database, "K_TOWER_CHARACTER", "*");
		//If Cursor is valid
    	if (cur != null ) {
    		//Move cursor to first row
    		if  (cur.moveToFirst()) {
    			do {
    				Tower tower = new Tower();
    				tower.damage = cur.getInt(cur.getColumnIndex("K_DAMAGE"));
    				tower.armorPiercing = cur.getInt(cur.getColumnIndex("K_ARMOR_PIERCING"));
    				tower.areaOfEffect = cur.getInt(cur.getColumnIndex("K_AREA_OF_EFFECT"));
//    				tower.attackEffect = cur.getInt(cur.getColumnIndex("K_AREA_OF_EFFECT"));
    				tower.range = cur.getInt(cur.getColumnIndex("K_RANGE"));
    				tower.attackSpeed = cur.getDouble(cur.getColumnIndex("K_ATTACK_SPEED"));
    				tower.buyCost = cur.getInt(cur.getColumnIndex("K_BUY_COST"));
    				tower.sellCost = cur.getInt(cur.getColumnIndex("K_SELL_COST"));
    				shopItems.add(tower);
    			}while (cur.moveToNext()); //Move to next row
    		}
    	}
    	return shopItems;
	}
	
}