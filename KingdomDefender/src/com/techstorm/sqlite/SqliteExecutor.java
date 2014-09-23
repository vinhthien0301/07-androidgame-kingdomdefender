package com.techstorm.sqlite;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class SqliteExecutor {

	public static SQLiteDatabase createDatabase(Activity activity, String dbName) {
		return activity.openOrCreateDatabase(dbName, Context.MODE_PRIVATE, null);
	}
	
	public static void createTable(SQLiteDatabase database, String tableName, String attributes) {
		database.execSQL("CREATE TABLE IF NOT EXISTS " + tableName +	" ("+attributes+");");
	}
	
	public static void queryStatement(SQLiteDatabase database, String tableName, String select) {
		database.rawQuery("SELECT "+select+" FROM " + tableName, null);
	}
	
	public static void insertStatement(SQLiteDatabase database, String tableName, String... values) {
		database.execSQL("INSERT INTO " + tableName + " Values ("+buildValues(values)+");");
	}
	
	public static void deleteStatement(SQLiteDatabase database, String tableName, String criterial) {
		database.execSQL("DELETE FROM " + tableName + " WHERE "+criterial+";");
	}
	
	public static void deleteStatement(SQLiteDatabase database, String tableName) {
		database.execSQL("DELETE FROM " + tableName + ";");
	}
	
	public static void updateStatement(SQLiteDatabase database, String tableName, String setting, String criterial) {
		database.execSQL("UPDATE " + tableName + " SET "+setting+" WHERE "+criterial+";");
	}
	
	public static void updateStatement(SQLiteDatabase database, String tableName, String setting) {
		database.execSQL("UPDATE " + tableName + " SET "+setting+";");
	}
	
	private static String buildValues(String[] values) {
		StringBuilder valuesBuilder = new StringBuilder();
		if (values != null && values.length > 0) {
			for (String value : values) {
				valuesBuilder.append("'");
				valuesBuilder.append(value);
				valuesBuilder.append("',");
			}
			valuesBuilder.deleteCharAt(valuesBuilder.length() - 1);
		}
		return valuesBuilder.toString();
	}
}
