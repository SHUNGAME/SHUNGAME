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
		// TODO ��ȡ���û����һ��ļ�¼
		Map<String, String> map = new HashMap<String, String>();
		getsqldatabase getsql = new getsqldatabase(context);
		SQLiteDatabase database = getsql.openuserDatabase();
		Cursor cursor = database.rawQuery(
				//��ѯ��ʱ����������һ����¼
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
		//TODO �ж��Ƿ��е���ļ�¼
		getsqldatabase getsql = new getsqldatabase(context);
		SQLiteDatabase database = getsql.openuserDatabase();
		Cursor cursor = database.rawQuery(
				"select * from userscore where userid=? and datetime=? ", args);
		//�жϸ��û������Ƿ���ڼ�¼
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
		//TODO ����userscore���е÷֡�����userscore������һ��״̬������userscore������������
		getsqldatabase getsql = new getsqldatabase(context);
		SQLiteDatabase database = getsql.openuserDatabase();
		//��֯���µļ�ֵ�ԣ�����Ϊ������Ҫ���µ�����Ϊֵ
		ContentValues values = new ContentValues();
		values.put("scores", args[0]);
		values.put("blaquenum", args[1]);
		values.put("module", args[2]);
		//��֯where�Ĳ���
		String [] whereArgs = new String[]{args[3], args[4]};
		//���±�
		database.update("userscore", values, "userid=? and datetime=?", whereArgs);
		//�ر����ݿ�
		if (database != null) {
			database.close();
		}
	}
	
	public void updateScoreInfoForSelectquestion(String [] args){
		//TODO ����userscore���е÷֡�����userscore������һ��״̬������userscore����ѡ�������
		getsqldatabase getsql = new getsqldatabase(context);
		SQLiteDatabase database = getsql.openuserDatabase();
		//��֯���µļ�ֵ�ԣ�����Ϊ������Ҫ���µ�����Ϊֵ
		ContentValues values = new ContentValues();
		values.put("scores", args[0]);
		values.put("selquenum", args[1]);
		values.put("module", args[2]);
		//��֯where�Ĳ���
		String [] whereArgs = new String[]{args[3], args[4]};
		//���±�
		database.update("userscore", values, "userid=? and datetime=?", whereArgs);
		//�ر����ݿ�
		if (database != null) {
			database.close();
		}
	}
	
	public void updateScoreInfoForLinequestion(String [] args){
		//TODO ����userscore���е÷֡�����userscore������һ��״̬������userscore�������������
		getsqldatabase getsql = new getsqldatabase(context);
		SQLiteDatabase database = getsql.openuserDatabase();
		//��֯���µļ�ֵ�ԣ�����Ϊ������Ҫ���µ�����Ϊֵ
		ContentValues values = new ContentValues();
		values.put("scores", args[0]);
		values.put("linequenum", args[1]);
		values.put("module", args[2]);
		//��֯where�Ĳ���
		String [] whereArgs = new String[]{args[3], args[4]};
		//���±�
		database.update("userscore", values, "userid=? and datetime=?", whereArgs);
		//�ر����ݿ�
		if (database != null) {
			database.close();
		}
	}
	
	public void updateScoreInfoForChallenge(String [] args){
		//TODO ����userscore���е÷֡�����userscore������һ��״̬������userscore������սģʽ�ؿ���
		getsqldatabase getsql = new getsqldatabase(context);
		SQLiteDatabase database = getsql.openuserDatabase();
		//��֯���µļ�ֵ�ԣ�����Ϊ������Ҫ���µ�����Ϊֵ
		ContentValues values = new ContentValues();
		values.put("scores", args[0]);
		values.put("challengelevel", args[1]);
		values.put("module", args[2]);
		//��֯where�Ĳ���
		String [] whereArgs = new String[]{args[3], args[4]};
		//���±�
		database.update("userscore", values, "userid=? and datetime=?", whereArgs);
		//�ر����ݿ�
		if (database != null) {
			database.close();
		}
	}
	
	public void insertScoreInfo(String[] args){
		//TODO ���뵱��ļ�¼
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
