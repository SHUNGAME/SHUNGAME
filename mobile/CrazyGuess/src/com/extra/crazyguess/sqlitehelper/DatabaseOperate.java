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
		// ������һ��DatabaseHelper����ִֻ����仰�ǲ��ᴴ��������ӵ�  
		DatabaseHelper dbHelper = new DatabaseHelper(at, "userinfodb"); 
		//ֻ�е�����DatabaseHelper��getWritableDatabase()��������getReadableDatabase()����֮�󣬲Żᴴ�����һ������  
		SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();
		//������
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
	
	//��������
	public void insertTableData(String tablename, Activity at, String[] args){
		// ����ContentValues����  
		ContentValues values = new ContentValues();  
		if("userinfo".equals(tablename)){
			// ��ö����в����ֵ�ԣ����м���������ֵ��ϣ�����뵽��һ�е�ֵ��ֵ��������ݿ⵱�е���������һ��
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
	
	//�����û�����
	public void updateTableForScores(String tablename, String args[], Activity at){
		// ����ContentValues����  
		ContentValues values = new ContentValues();
		values.put("scores", args[0]);
		//��֯whereArgs����
		String [] whereArgs = new String[]{args[1], args[2]}; 
		//
		DatabaseHelper dbHelper = new DatabaseHelper(at, "userinfodb");
		SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();
		sqliteDatabase.update(tablename, values, "userid=? and username=?", whereArgs);
	}
	
	//������Ŀ״̬
	public void updateTableForQuestionStates(String table, String args[], Activity at){
		// ����ContentValues����  
		ContentValues values = new ContentValues();
		values.put("module", args[0]);
		values.put("selquenum", args[1]);
		values.put("blaquenum", args[2]);
		values.put("linequenum", args[3]);
		values.put("challengelevel", args[4]);
		//��֯whereArgs����
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
