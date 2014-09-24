package com.techstorm.androidgame.kingdomdefender.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.techstorm.sqlite.SqliteExecutor;

public class DbCreator {

	public final static String DB_NAME = "KINGDOM_DEFENDER";
	
	// Create database skeleton
	public static SQLiteDatabase createSkeleton(Context context) {
		SQLiteDatabase database = SqliteExecutor.createDatabaseNotExisting(context, DB_NAME);
		SqliteExecutor.createTable(database, "K_MAP", SqliteExecutor.createColumnDefinitions(
				new SqliteExecutor.ColumnDefinition("K_PK", SqliteExecutor.COLUMN_STRING_DEF),
				new SqliteExecutor.ColumnDefinition("K_VERSION", SqliteExecutor.COLUMN_STRING_DEF),
				new SqliteExecutor.ColumnDefinition("K_LEVEL", SqliteExecutor.COLUMN_STRING_DEF)));
		SqliteExecutor.createTable(database, "K_WAVE", SqliteExecutor.createColumnDefinitions(
				new SqliteExecutor.ColumnDefinition("K_PK", SqliteExecutor.COLUMN_STRING_DEF),
				new SqliteExecutor.ColumnDefinition("K_VERSION", SqliteExecutor.COLUMN_STRING_DEF),
				new SqliteExecutor.ColumnDefinition("K_LEVEL", SqliteExecutor.COLUMN_STRING_DEF)));
		SqliteExecutor.createTable(database, "K_MONSTER", SqliteExecutor.createColumnDefinitions(
				new SqliteExecutor.ColumnDefinition("K_PK", SqliteExecutor.COLUMN_STRING_DEF),
				new SqliteExecutor.ColumnDefinition("K_VERSION", SqliteExecutor.COLUMN_STRING_DEF),
				new SqliteExecutor.ColumnDefinition("K_LEVEL", SqliteExecutor.COLUMN_STRING_DEF)));
		return database;
	}
	
	
}
