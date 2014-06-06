package com.extra.crazyguess.getsqldatabase;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class getblankquestion {
	
	private Context context;

	public getblankquestion(Context context){
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	
	public Map<String, String> getblankquestionMap(String[] args){
		Map<String, String> map = new HashMap<String, String>();
		getsqldatabase getsql = new getsqldatabase(context);
		SQLiteDatabase database = getsql.opensqlDatabase();
		Cursor cursor = database.rawQuery(
				"select * from blankquestion where id=?", args);
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
	
	
}
