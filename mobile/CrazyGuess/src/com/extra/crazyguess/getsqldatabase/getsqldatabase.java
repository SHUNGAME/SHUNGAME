package com.extra.crazyguess.getsqldatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import com.extra.crazyguess.R;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

public class getsqldatabase {

	private Context context;
	private String sdpath = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/Myquestion";
	private String filename = "myquestion.db";
	
	private String udpath = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/Userinfo";
	private String ufilename = "userinfo.db";

	public getsqldatabase(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public SQLiteDatabase opensqlDatabase() {
		File dir = new File(sdpath);
		if (!dir.exists()) {
			dir.mkdir();
		}
		File filepath = new File(sdpath, filename);
//		if (!filepath.exists()) {
			try {
				InputStream inputStream = context.getResources()
						.openRawResource(R.raw.myquestion);
				FileOutputStream fileOutputStream = new FileOutputStream(
						filepath);
				byte[] buff = new byte[10240];
				int len = 0;
				while ((len = inputStream.read(buff)) != -1) {
					fileOutputStream.write(buff, 0, len);
				} 
				fileOutputStream.close();
				inputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
//		}
		SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(filepath,
				null);
		return database;
	}
	
	public SQLiteDatabase openuserDatabase(){
		File dir = new File(udpath);
		if (!dir.exists()) {
			dir.mkdir();
		}
		File filepath = new File(udpath, ufilename);
//		if (!filepath.exists()) {
			try {
				InputStream inputStream = context.getResources()
						.openRawResource(R.raw.userinfo);
				FileOutputStream fileOutputStream = new FileOutputStream(
						filepath);
				byte[] buff = new byte[10240];
				int len = 0;
				while ((len = inputStream.read(buff)) != -1) {
					fileOutputStream.write(buff, 0, len);
				} 
				fileOutputStream.close();
				inputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
//		}
		SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(filepath,
				null);
		return database;
	}
}
