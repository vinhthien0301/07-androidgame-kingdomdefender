package com.techstorm.androidgame.kingdomdefender.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.techstorm.androidgame.kingdomdefender.LevelMap;
import com.techstorm.androidgame.kingdomdefender.MatrixLocation2d;
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
}
