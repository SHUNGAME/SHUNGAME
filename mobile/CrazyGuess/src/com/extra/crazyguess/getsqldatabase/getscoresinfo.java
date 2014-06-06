package com.extra.crazyguess.getsqldatabase;

import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class getscoresinfo {
	
	private Context context;

	public getscoresinfo(Context context){
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	
	public Map<String, String> getscoresinfoMap(String[] args){
		// TODO 获取该用户最近一天的记录
		Map<String, String> map = new HashMap<String, String>();
		getsqldatabase getsql = new getsqldatabase(context);
		SQLiteDatabase database = getsql.openuserDatabase();
		Cursor cursor = database.rawQuery(
				//查询按时间排序的最后一条记录
				"select * from userscore where userid=? order by datetime desc limit 0,1", args);
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
		if (database != null) {
			database.close();
		}
		return map;
	}
	
	public Boolean getScoreForCurrent(String[] args){
		//TODO 判断是否有当天的记录
		getsqldatabase getsql = new getsqldatabase(context);
		SQLiteDatabase database = getsql.openuserDatabase();
		Cursor cursor = database.rawQuery(
				"select * from userscore where userid=? and datetime=? ", args);
		//判断该用户当天是否存在记录
		if(cursor.getCount() == 0){
			if (database != null) {
				database.close();
			}
			return false;
		}else {
			if (database != null) {
				database.close();
			}
			return true;
		}
		
	}
	
	public void updateScoreInfoForBlankquestion(String [] args){
		//TODO 更新userscore表中得分、更新userscore表中下一题状态、更新userscore表中填空题题号
		getsqldatabase getsql = new getsqldatabase(context);
		SQLiteDatabase database = getsql.openuserDatabase();
		//组织更新的键值对，列名为键，需要更新的数据为值
		ContentValues values = new ContentValues();
		values.put("scores", args[0]);
		values.put("blaquenum", args[1]);
		values.put("module", args[2]);
		//组织where的参数
		String [] whereArgs = new String[]{args[3], args[4]};
		//更新表
		database.update("userscore", values, "userid=? and datetime=?", whereArgs);
		//关闭数据库
		if (database != null) {
			database.close();
		}
	}
	
	public void updateScoreInfoForSelectquestion(String [] args){
		//TODO 更新userscore表中得分、更新userscore表中下一题状态、更新userscore表中选择题题号
		getsqldatabase getsql = new getsqldatabase(context);
		SQLiteDatabase database = getsql.openuserDatabase();
		//组织更新的键值对，列名为键，需要更新的数据为值
		ContentValues values = new ContentValues();
		values.put("scores", args[0]);
		values.put("selquenum", args[1]);
		values.put("module", args[2]);
		//组织where的参数
		String [] whereArgs = new String[]{args[3], args[4]};
		//更新表
		database.update("userscore", values, "userid=? and datetime=?", whereArgs);
		//关闭数据库
		if (database != null) {
			database.close();
		}
	}
	
	public void updateScoreInfoForLinequestion(String [] args){
		//TODO 更新userscore表中得分、更新userscore表中下一题状态、更新userscore表中连线题题号
		getsqldatabase getsql = new getsqldatabase(context);
		SQLiteDatabase database = getsql.openuserDatabase();
		//组织更新的键值对，列名为键，需要更新的数据为值
		ContentValues values = new ContentValues();
		values.put("scores", args[0]);
		values.put("linequenum", args[1]);
		values.put("module", args[2]);
		//组织where的参数
		String [] whereArgs = new String[]{args[3], args[4]};
		//更新表
		database.update("userscore", values, "userid=? and datetime=?", whereArgs);
		//关闭数据库
		if (database != null) {
			database.close();
		}
	}
	
	public void updateScoreInfoForChallenge(String [] args){
		//TODO 更新userscore表中得分、更新userscore表中下一题状态、更新userscore表中挑战模式关卡号
		getsqldatabase getsql = new getsqldatabase(context);
		SQLiteDatabase database = getsql.openuserDatabase();
		//组织更新的键值对，列名为键，需要更新的数据为值
		ContentValues values = new ContentValues();
		values.put("scores", args[0]);
		values.put("challengelevel", args[1]);
		values.put("module", args[2]);
		//组织where的参数
		String [] whereArgs = new String[]{args[3], args[4]};
		//更新表
		database.update("userscore", values, "userid=? and datetime=?", whereArgs);
		//关闭数据库
		if (database != null) {
			database.close();
		}
	}
	
	public void insertScoreInfo(String[] args){
		//TODO 插入当天的记录
		getsqldatabase getsql = new getsqldatabase(context);
		SQLiteDatabase database = getsql.openuserDatabase();
		try{
			database.execSQL("insert into userscore values('" + args[0] + "','" + args[1] +
					"','" + args[2] + "','" + args[3] + "','" + args[4] + "','" + args[5] +
					"','" + args[6] + "','" + args[7] + "','" + args[8] + "')");
		}catch(SQLException e){
			e.printStackTrace();
		}
		if (database != null) {
			database.close();
		}
	}

}
