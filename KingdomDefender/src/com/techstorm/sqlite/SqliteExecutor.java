package com.techstorm.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SqliteExecutor {

	public static final Integer COLUMN_STRING_DEF = Integer.valueOf(0);
	public static final Integer COLUMN_BIT_DEF = Integer.valueOf(1);
	public static final Integer COLUMN_INTEGER_DEF = Integer.valueOf(2);
	
	public static SQLiteDatabase createDatabaseNotExisting(Context context, String dbName) {
		return context.openOrCreateDatabase(dbName, Context.MODE_PRIVATE, null);
	}
	
	public static String createColumnDefinitions(ColumnDefinition... columnDefinitions) {
		StringBuilder builder = new StringBuilder();
		if (columnDefinitions != null && columnDefinitions.length > 0) {
			for (ColumnDefinition columnDef : columnDefinitions) {
				builder.append(columnDef.name);
				builder.append(" ");
				if (COLUMN_STRING_DEF.equals(columnDef.definition)) {
					builder.append("VARCHAR");
				} else if (COLUMN_BIT_DEF.equals(columnDef.definition)) {
					builder.append("TINYINT");
				} else if (COLUMN_INTEGER_DEF.equals(columnDef.definition)) {
					builder.append("VARCHAR");
				}
				builder.append(",");
			}
			builder.deleteCharAt(builder.length() - 1);
		}
		return builder.toString();
	}
	
	public static void createTable(SQLiteDatabase database, String tableName, String columns) {
		database.execSQL("CREATE TABLE IF NOT EXISTS " + tableName +	" ("+columns+");");
	}
	
	public static Cursor queryStatement(SQLiteDatabase database, String tableName, String select) {
		return database.rawQuery("SELECT "+select+" FROM " + tableName, null);
	}
	
	public static Cursor queryStatement(SQLiteDatabase database, String tableName, String select, String where) {
		return database.rawQuery("SELECT "+select+" FROM " + tableName + " WHERE " + where + ";", null);
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
		StringBuilder builder = new StringBuilder();
		if (values != null && values.length > 0) {
			for (String value : values) {
				builder.append("'");
				builder.append(value);
				builder.append("',");
			}
			builder.deleteCharAt(builder.length() - 1);
		}
		return builder.toString();
	}
	
	public static class ColumnDefinition {
		public Integer definition;
		public String name;
		
		public ColumnDefinition(String name, Integer definition) {
			this.name = name;
			this.definition = definition;
		}
	}
}
