package com.extra.crazyguess.sqlitehelper;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseOperate {
	
	@SuppressWarnings("unused")
	private Context context;

	public DatabaseOperate(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	
	public void createDatabase(Activity at){
		// 创建了一个DatabaseHelper对象，只执行这句话是不会创建或打开连接的  
		DatabaseHelper dbHelper = new DatabaseHelper(at, "userinfodb"); 
		//只有调用了DatabaseHelper的getWritableDatabase()方法或者getReadableDatabase()方法之后，才会创建或打开一个连接  
		SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();
		//创建表
		dbHelper.onCreate(sqliteDatabase);
	}
	
	public Map<String,String> queryUserTable(String table, Activity at, String[] args){
		Map<String, String> map = new HashMap<String, String>();
		//
		DatabaseHelper dbHelper = new DatabaseHelper(at, "userinfodb");
		SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();
		Cursor cursor = sqliteDatabase.rawQuery(
				"select * from table userinfo where userid=? and username=? and datetime=?", 
				args);
		int colums = cursor.getColumnCount();
		while (cursor.moveToNext()) {
			for (int i = 0; i < colums; i++) {
				String columname = cursor.getColumnName(i);
				String columvalue = cursor.getString(cursor
						.getColumnIndex(columname));
				if (columvalue == null) {
					columvalue = "";
				}
				map.put(columname, columvalue);
			}
		}
		if (sqliteDatabase != null) {
			sqliteDatabase.close();
		}
		return map;
	}
	
	//插入数据
	public void insertTableData(String tablename, Activity at, String[] args){
		// 创建ContentValues对象  
		ContentValues values = new ContentValues();  
		if("userinfo".equals(tablename)){
			// 向该对象中插入键值对，其中键是列名，值是希望插入到这一列的值，值必须和数据库当中的数据类型一致
			values.put("userid", args[0]);
			values.put("username", args[1]);
			values.put("datetime", args[2]);
			values.put("scores", args[3]);
			values.put("otherinfo", args[4]);
		}else if("questionstate".endsWith(tablename)){
			values.put("userid", args[0]);
			values.put("module", args[1]);
			values.put("selquenum", args[2]);
			values.put("blaquenum", args[3]);
			values.put("linequenum", args[4]);
			values.put("challengelevel", args[5]);
		}
		//
		DatabaseHelper dbHelper = new DatabaseHelper(at, "userinfodb");
		//
		SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();
		//
		sqliteDatabase.insert(tablename, null, values);
	}
	
	//更新用户分数
	public void updateTableForScores(String tablename, String args[], Activity at){
		// 创建ContentValues对象  
		ContentValues values = new ContentValues();
		values.put("scores", args[0]);
		//组织whereArgs数字
		String [] whereArgs = new String[]{args[1], args[2]}; 
		//
		DatabaseHelper dbHelper = new DatabaseHelper(at, "userinfodb");
		SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();
		sqliteDatabase.update(tablename, values, "userid=? and username=?", whereArgs);
	}
	
	//更新题目状态
	public void updateTableForQuestionStates(String table, String args[], Activity at){
		// 创建ContentValues对象  
		ContentValues values = new ContentValues();
		values.put("module", args[0]);
		values.put("selquenum", args[1]);
		values.put("blaquenum", args[2]);
		values.put("linequenum", args[3]);
		values.put("challengelevel", args[4]);
		//组织whereArgs数字
		String [] whereArgs = new String[]{args[5], args[6]};
		//
		DatabaseHelper dbHelper = new DatabaseHelper(at, "userinfodb");
		SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();
		sqliteDatabase.update(table, values, "userid=? and username=?", whereArgs);
	}
	
	//
	public void deleteTableData(String table, String args[], Activity at){
		//
		DatabaseHelper dbHelper = new DatabaseHelper(at, "userinfodb");
		SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();
		sqliteDatabase.delete(table, "userid=? and username=?", args);
	}
}
