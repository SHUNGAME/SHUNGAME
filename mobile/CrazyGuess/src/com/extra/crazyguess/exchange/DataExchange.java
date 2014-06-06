package com.extra.crazyguess.exchange;

import org.json.JSONException;
import org.json.JSONObject;

import com.extra.crazyguess.getsqldatabase.getsqldatabase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataExchange {
	private Context context;

	public DataExchange(Context context){
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	
	public void sendData(String userid){
//		Map<String, String> map = new HashMap<String, String>();
		String [] args = new String[]{userid};
		getsqldatabase getsql = new getsqldatabase(context);
		SQLiteDatabase database = getsql.opensqlDatabase();
		Cursor cursor = database.rawQuery(
			//查询按时间排序的最后一条记录
			"select * from userscore where userid=? and sendstates='0' order by datetime desc", args);
		JSONObject userDatas = new JSONObject();
		int colums = cursor.getColumnCount();
		while (cursor.moveToNext()) {
			JSONObject temp = new JSONObject();
			for (int i = 0; i < colums; i++) {
				String columname = cursor.getColumnName(i);
				String columvalue = cursor.getString(i);
				if (columvalue == null) {
					columvalue = "";
				}
				try {
					temp.put(columname, columvalue);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}
			try {
				userDatas.put(temp.getString("datetime"), temp);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

}
