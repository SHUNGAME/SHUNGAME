package com.extra.crazyguess;

import java.util.HashMap;
import java.util.Map;

import com.extra.crazyguess.MusicTools.Mediaplayer;
import com.extra.crazyguess.MusicTools.SoundPlayer;
import com.extra.crazyguess.single.ActivitiesCollection;
import com.extra.crazyguess.sqlitehelper.DatabaseHelper;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener {

	private Button button;
	private Button button2;
	private Button button3;
	private String userid = "";
//	private CharSequence items[] = { "��ʼ��Ϸ", "��Ч����", "������Ϸ" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//��ȡ�豸id
		TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		userid = tm.getDeviceId(); //�豸id
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setTitle("��ӭ���뺣�����Ϸ��԰");
		SoundPlayer.init(MainActivity.this);
		Mediaplayer.init(MainActivity.this);
		if (Mediaplayer.getplayflag()) {
			Mediaplayer.PlayBackgroundMusic();
		}
		button = (Button) this.findViewById(R.id.button1);
		button.setOnClickListener(this);
		button2 = (Button) this.findViewById(R.id.button2);
		button2.setOnClickListener(this);
		button3 = (Button) this.findViewById(R.id.button3);
		button3.setOnClickListener(this);
		ActivitiesCollection.getInstance().addActivity(this);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
//		super.onBackPressed();
		SoundPlayer.Releasesoundplayer();// �˳�Activityʱ�ͷ�soundpool�е���Դ
		Mediaplayer.ReleaseMediaplayer();// �˳�Activityʱ�ͷ�mediaplayer�е���Դ
		finish();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.action_settings1:
			onClick(button);
			break;
		case R.id.action_settings2:
			onClick(button2);
			break;
		case R.id.action_settings3:
			onClick(button3);
			break;
		}
		
		return super.onMenuItemSelected(featureId, item);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button1:
			//�������ݿ�  
			creatDatabase();
//			Map<String,String> miMap = queryForState();
//			if(miMap.size() == 0){
//				insertStates();
//			}
			// ������Ч
			SoundPlayer.playsound(R.raw.gang2);
			Intent intent1=new Intent(MainActivity.this,ChooseModeActivity.class);
//			Intent intent1=new Intent(MainActivity.this,SQLiteActivity.class);
//			Intent intent1=new Intent(MainActivity.this,SelectGameActivity.class);
//			Intent intent1=new Intent(MainActivity.this,BlankGameActivity.class);
//			Intent intent1=new Intent(MainActivity.this,LinesGame2Activity.class);
//			Intent intent1=new Intent(MainActivity.this,LinesGameActivity.class);
//			Intent intent1=new Intent(MainActivity.this,GameActivity1.class);
			startActivity(intent1);    //������Ϸ����
			// ����Activity֮����л�Ч��
			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
			break;
		case R.id.button2:
			Intent intent2 = new Intent(MainActivity.this, VolumControl.class);
			startActivity(intent2);
			// ����Activity֮����л�Ч��
			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
			break;
		case R.id.button3:
			//�öԻ������ʽ��ʾȷ��Ҫ��Ҫ�˳���Ϸ
			AlertDialog.Builder builder=new AlertDialog.Builder(this);   
			builder.setTitle("��ʾ");
			builder.setMessage("��ȷ��Ҫ�˳���Ϸô��");
			builder.setPositiveButton("ȷ��", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
//					onBackPressed();// ���ð����ؼ�ʱ�������¼�����
					//�������˳�activity
					ActivitiesCollection.getInstance().exit();
				}
			});
			builder.setNegativeButton("ȡ��", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					return;
				}
			});
			builder.setCancelable(false);
			AlertDialog dialog=builder.create();
			dialog.show();
			break;

		}
	}
	
	public void creatDatabase(){
    	DatabaseHelper dbHelper = new DatabaseHelper(MainActivity.this, "userAndQuestion", 1);
    	SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();
    }
	
	public void insertStates(){
    	// ����ContentValues����  
        ContentValues values = new ContentValues();
    	values.put("userid", userid);
    	values.put("module", "");
    	values.put("selquenum", 1);
    	values.put("blaquenum", 1);
    	values.put("linequenum", 1);
    	values.put("challengelevel", 1);
    	// ����DatabaseHelper����  
    	DatabaseHelper dbHelper = new DatabaseHelper(MainActivity.this, "userAndQuestion", 1);  
    	// �õ�һ����д��SQLiteDatabase����  
    	SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();  
    	// ����insert�������Ϳ��Խ����ݲ��뵽���ݿ⵱��  
    	sqliteDatabase.insert("questionstate", null, values);  
    }
	
	public Map<String, String> queryForState(){
		Map<String, String> map = new HashMap<String, String>();
		//����DatabaseHelper����  
		DatabaseHelper dbHelper = new DatabaseHelper(MainActivity.this, "userAndQuestion", 1);  
		// �õ�һ��ֻ����SQLiteDatabase����  
		SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor cursor = sqliteDatabase.rawQuery(
				"select * from questionstate where userid=?", 
				new String[] {userid});
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
		return map;
	}
}
