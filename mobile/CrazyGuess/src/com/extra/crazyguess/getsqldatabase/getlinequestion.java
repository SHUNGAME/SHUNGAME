package com.extra.crazyguess.getsqldatabase;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class getlinequestion {
	private Context context;

	public getlinequestion(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	
	public Map<String, String> getlinequestionMap(String[] args) {
		Map<String, String> map = new HashMap<String, String>();
		getsqldatabase getsql = new getsqldatabase(context);
		SQLiteDatabase database = getsql.opensqlDatabase();
		Cursor cursor = database.rawQuery(
//				"select * from question where id=?", args);
				"select * from linequestion where id=?", args);
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
