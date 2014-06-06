package com.extra.crazyguess;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.extra.crazyguess.Tools.JudgeAnswer;
import com.extra.crazyguess.Tools.MakeIntToString;
import com.extra.crazyguess.exchange.HttpTest;
import com.extra.crazyguess.getsqldatabase.getblankquestion;
import com.extra.crazyguess.single.ActivitiesCollection;
import com.extra.crazyguess.sqlitehelper.DatabaseHelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class BlankGameActivity extends Activity implements OnClickListener{
	
	public BlankGameActivity(){
		// TODO Auto-generated method stub
	}
	
	private TextView scors,movietitle,questionhead,questiontail,
						question1,question2,question3,question4;
	private Button back,blank1,blank2,blank3,blank4,
					option1,option2,option3,option4;
//	private int sqnumber = 1;//��ʼ���
	private final static int CHANGE_QUESTION = 1; // �任��Ϸ������Ŀ�ı�ʶ��
	private final static int LASTQUESTION = 2; // ���������
	
	int finalResult = 0;
	private String selectA = null;
	private String selectB = null;
	private String selectC = null;
	private String selectD = null;
	
	private String userid = "";
	private String username = "";
	private String squesNum = "1";
	private String lquesNum = "1";
	private String module = "select";
	private int bquesNum = 1; //��������
	private int totalscores = 0;//��ǰ�ܵ÷�
	private String challengelevel = "1";
	
	
	// ��mainMap���洢�����Ӧ����Ϣ
	private Map<String, String> mainMap = new HashMap<String, String>();
	private List<Map<String,String>> selectList = new ArrayList<Map<String,String>>();
	private List<Map<String,String>> selectresult = new ArrayList<Map<String,String>>();
	
	// ���̺߳�handler��������Ϣ
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CHANGE_QUESTION:
				mainMap = (Map<String, String>) msg.obj;
				movietitle.setText(mainMap.get("moviename"));
				questionhead.setText(mainMap.get("head"));
				question1.setText(mainMap.get("roleA"));
				question2.setText(mainMap.get("roleB"));
				question3.setText(mainMap.get("roleC"));
				question4.setText(mainMap.get("roleD"));
				questiontail.setText(mainMap.get("tail"));
				option1.setText(mainMap.get("optionA"));
				option2.setText(mainMap.get("optionB"));
				option3.setText(mainMap.get("optionC"));
				option4.setText(mainMap.get("optionD"));
				scors.setText(totalscores+"");
				break;
			}
		};
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//��ȡ�豸id
		TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		userid = tm.getDeviceId(); //�豸id
//		new dataDevice().dealscores();
		new userScoreDevice().initQuery();
		ActivitiesCollection.getInstance().addActivity(this);
		if(bquesNum > LASTQUESTION){//��Ŀ�Ѿ�����
			setContentView(R.layout.completed);
			back = (Button)this.findViewById(R.id.back);
			back.setOnClickListener(this);
		}else{
			//���ò���
			setContentView(R.layout.blankquestion);
			//��ȡ�����ϵĿؼ�
			scors = (TextView)this.findViewById(R.id.scorestextB);
			movietitle = (TextView)this.findViewById(R.id.movietitle);
			questionhead = (TextView)this.findViewById(R.id.questionhead);
			question1 = (TextView)this.findViewById(R.id.role1);
			question2 = (TextView)this.findViewById(R.id.role2);
			question3 = (TextView)this.findViewById(R.id.role3);
			question4 = (TextView)this.findViewById(R.id.role4);
			questiontail = (TextView)this.findViewById(R.id.questiontail);
			back = (Button)this.findViewById(R.id.back);
			back.setOnClickListener(this);
			option1 = (Button)this.findViewById(R.id.BoptionA);
			option1.setOnClickListener(this);
			option2 = (Button)this.findViewById(R.id.BoptionB);
			option2.setOnClickListener(this);
			option3 = (Button)this.findViewById(R.id.BoptionC);
			option3.setOnClickListener(this);
			option4 = (Button)this.findViewById(R.id.BoptionD);
			option4.setOnClickListener(this);
			blank1 = (Button)this.findViewById(R.id.blank1);
			blank1.setOnClickListener(this);
			blank2 = (Button)this.findViewById(R.id.blank2);
			blank2.setOnClickListener(this);
			blank3 = (Button)this.findViewById(R.id.blank3);
			blank3.setOnClickListener(this);
			blank4 = (Button)this.findViewById(R.id.blank4);
			blank4.setOnClickListener(this);
			
			//������Ϸ
			new Thread(new StartGame()).start();
		}
	}
	
	public class StartGame implements Runnable{
		@SuppressLint("SimpleDateFormat")
		@Override
		public void run() {
			HttpTest ht = new HttpTest();
			ht.sendData();
			getblankquestion getlq = new getblankquestion(BlankGameActivity.this);
			Map<String, String> map = new HashMap<String, String>();
			// ��MakeIntToString��������ת���ַ�����ѡ���Ӧ��Ŀ
			String str = MakeIntToString.getString(bquesNum);
			String[] strs = new String[] {str};
			map = getlq.getblankquestionMap(strs);
			// ��message�������̴߳�����Ϣ������
			Message message = Message.obtain();
			message.obj = map; // ��map��Ϣ����message��
			message.what = CHANGE_QUESTION; // �趨message�ı�ʾ��
			handler.sendMessage(message); // �����߳��е�handler������Ϣ
		}
	}
	
	public class userScoreDevice{
		public userScoreDevice(){
			
		}
		
		public Map<String,String> queryLatestData(){
			Map<String,String> map = new HashMap<String,String>();
			//����DatabaseHelper����  
			DatabaseHelper dbHelper = new DatabaseHelper(BlankGameActivity.this,"userAndQuestion",1);
			// �õ�һ��ֻ����SQLiteDatabase����  
			SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();
			Cursor cursor = sqliteDatabase.rawQuery(
					"select * from userscore where userid=? order by datetime desc limit 0,1",
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
		
		@SuppressLint("SimpleDateFormat")
		public Boolean queryCurrentScore(){
			//��ȡ��ǰϵͳʱ��
			SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String currentdate = sDateFormat.format(new java.util.Date());
			//����DatabaseHelper����  
			DatabaseHelper dbHelper = new DatabaseHelper(BlankGameActivity.this,"userAndQuestion",1);
			// �õ�һ��ֻ����SQLiteDatabase����  
			SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();
			Cursor cursor = sqliteDatabase.rawQuery(
					"select * from userscore where userid=? and datetime=?", 
					new String[] {userid, currentdate});
			if(cursor.getCount() == 0){
				return false;
			}else{
				return true;
			}
		}
		
		public void update(String score,String bqn, String datetime){
			// ����һ��DatabaseHelper����  
			DatabaseHelper dbHelper = new DatabaseHelper(BlankGameActivity.this,"userAndQuestion",1);
			// �õ�һ����д��SQLiteDatabase����  
			SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();
			// ����һ��ContentValues����  
			ContentValues values = new ContentValues();
			values.put("scores", score);
			values.put("blaquenum", bqn);
			// ����update����  
			sqliteDatabase.update("userscore", values, "userid=? and datetime=?", 
			new String[] {userid, datetime});
		}
		
		public void insert(String id, String date, String score, String squesNum, String bquesNum,
							String lquesNum, String challengelevel){
			// ����ContentValues����  
			ContentValues values = new ContentValues();
			values.put("id", 1);
			values.put("userid", userid);
			values.put("username", "");
			values.put("datetime", date);
			values.put("scores", score);
			values.put("selquenum", squesNum);
			values.put("blaquenum", bquesNum);
			values.put("linequenum", lquesNum);
			values.put("module", module);
			values.put("challengelevel", challengelevel);
        	// ����DatabaseHelper����  
        	DatabaseHelper dbHelper = new DatabaseHelper(BlankGameActivity.this,"userAndQuestion",1);  
        	// �õ�һ����д��SQLiteDatabase����  
        	SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();  
        	// ����insert�������Ϳ��Խ����ݲ��뵽���ݿ⵱��  
        	sqliteDatabase.insert("userscore", null, values);
		}
		
		
		public void initQuery(){
			Map<String,String> miMap = queryLatestData();
			if(miMap.size() == 0){
				totalscores = 0;
				bquesNum = 1;
			}else{
				//��������������Ϣ
				String ssss = miMap.get("scores");
				totalscores = Integer.parseInt(ssss);
				String bqn = miMap.get("blaquenum");
				bquesNum = Integer.parseInt(bqn);
				//����������Ϣ
				username = miMap.get("username");
				squesNum = miMap.get("selquenum");
				lquesNum = miMap.get("linequenum");
				challengelevel = miMap.get("challengelevel");
			}
		}
		
		@SuppressLint("SimpleDateFormat")
		public void stateUpdate(){
			//��ȡ��ǰϵͳʱ��
			SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String currentdate = sDateFormat.format(new java.util.Date());
			//��ȡ���շ��������
			String newscores = totalscores + "";
			String newbqn = bquesNum + "";
			Boolean isExist = queryCurrentScore();
			if(isExist){
				update(newscores, newbqn, currentdate);
			}else{
				insert("1", currentdate, newscores, squesNum, newbqn, lquesNum, challengelevel);
			}
		}
	}
	
	public class dataDevice{
		
		public dataDevice(){
			
		}
		
		public void dealscores (){
			//
//			getscoresinfo getsi = new getscoresinfo(BlankGameActivity.this);
			Map<String, String> scoresmap = queryForUser();
//					getsi.getscoresinfoMap(cstrs);
			Map<String,String> statesmap = queryForStates();
			if(scoresmap.size() == 0){
				totalscores = 0;
			}else{
				//��������������Ϣ
				String ssss = scoresmap.get("scores");
				totalscores = Integer.parseInt(ssss);
//				module = scoresmap.get("module");
			}
			if(statesmap.size() == 0){
				bquesNum = 1;
			}else{
				String bqn = statesmap.get("blaquenum");
				bquesNum = Integer.parseInt(bqn);
				//����������Ϣ
				username = statesmap.get("username");
				squesNum = statesmap.get("selquenum");
				lquesNum = statesmap.get("linequenum");
				challengelevel = scoresmap.get("challengelevel");
			}
		}
		
		@SuppressLint("SimpleDateFormat")
		public Map<String, String> queryForUser(){
			Map<String, String> map = new HashMap<String, String>();
			//����DatabaseHelper����  
			DatabaseHelper dbHelper = new DatabaseHelper(BlankGameActivity.this,"userAndQuestion",1);
			// �õ�һ��ֻ����SQLiteDatabase����  
			SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();
			Cursor cursor = sqliteDatabase.rawQuery(
					"select * from userinfo where userid=? order by datetime desc limit 0,1",
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
		
		@SuppressLint("SimpleDateFormat")
		public Boolean getCurrentScore(){
			//��ȡ��ǰϵͳʱ��
			SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String currentdate = sDateFormat.format(new java.util.Date());
			//����DatabaseHelper����  
			DatabaseHelper dbHelper = new DatabaseHelper(BlankGameActivity.this,"userAndQuestion",1);
			// �õ�һ��ֻ����SQLiteDatabase����  
			SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();
			Cursor cursor = sqliteDatabase.rawQuery(
					"select * from userinfo where userid=? and datetime=?", 
					new String[] {userid, currentdate});
			if(cursor.getCount() == 0){
				return false;
			}else{
				return true;
			}
		}
		
		public Map<String, String> queryForStates(){
			Map<String, String> map = new HashMap<String, String>();
			//����DatabaseHelper����  
			DatabaseHelper dbHelper = new DatabaseHelper(BlankGameActivity.this,"userAndQuestion",1);
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
		
		public void updateUser(String score, String userid, String datetime){
	    	// ����һ��DatabaseHelper����  
	        DatabaseHelper dbHelper = new DatabaseHelper(BlankGameActivity.this,"userAndQuestion",1);  
	        // �õ�һ����д��SQLiteDatabase����  
	        SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();  
	        // ����һ��ContentValues����  
	        ContentValues values = new ContentValues();  
	        values.put("scores", score);  
	        // ����update����  
	        sqliteDatabase.update("userinfo", values, "userid=? and datetime=?", 
	        		new String[] {userid, datetime});
	    }
		
		public void updateState(int bnum){
			// ����һ��DatabaseHelper����  
	        DatabaseHelper dbHelper = new DatabaseHelper(BlankGameActivity.this,"userAndQuestion",1);  
	        // �õ�һ����д��SQLiteDatabase����  
	        SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();  
	        // ����һ��ContentValues����  
	        ContentValues values = new ContentValues();  
	        values.put("blaquenum", bnum);  
	        // ����update����  
	        sqliteDatabase.update("questionstate", values, "userid=?", 
	        		new String[] {userid});
		}
		
		public void insertUser(String date, String score){
	    	// ����ContentValues����  
	        ContentValues values = new ContentValues();
	        values.put("id", 1);
	        values.put("userid", userid);
        	values.put("username", "");
        	values.put("datetime", date);
        	values.put("scores", score);
        	values.put("otherinfo", "");
        	// ����DatabaseHelper����  
        	DatabaseHelper dbHelper = new DatabaseHelper(BlankGameActivity.this,"userAndQuestion",1);  
        	// �õ�һ����д��SQLiteDatabase����  
        	SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();  
        	// ����insert�������Ϳ��Խ����ݲ��뵽���ݿ⵱��  
        	sqliteDatabase.insert("userinfo", null, values);  
	    }
		
		@SuppressLint("SimpleDateFormat")
		public void updatescores(){
			//��ȡ��ǰϵͳʱ��
			SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String currentdate = sDateFormat.format(new java.util.Date());
			//��ȡ���շ��������
			String newscores = totalscores + "";
//			String newbqn = bquesNum + "";
			Boolean isExist = getCurrentScore();
			if(isExist){
				updateUser(newscores, userid, currentdate);
			}else{
				insertUser(currentdate, newscores);
			}
			updateState(bquesNum);
//			getscoresinfo getsi = new getscoresinfo(BlankGameActivity.this);
//			String [] args = new String[]{userid, currentdate};
//			Boolean isExist = getsi.getScoreForCurrent(args);//�жϸ��û�����ļ�¼�Ƿ���ڣ����ھ͸��£������ھͲ���
//			if(isExist){
//				args = new String[]{newscores, newbqn, module, userid, currentdate};
//				getsi.updateScoreInfoForBlankquestion(args);
//			}else{
//				args = new String[]{userid,username,currentdate,newscores,
//						squesNum,newbqn,lquesNum,module,challengelevel};
//				getsi.insertScoreInfo(args);
//			}
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Map<String,String> node = new HashMap<String,String>();
		if(blank1 == null){
			Intent intent = new Intent(BlankGameActivity.this, ChooseModeActivity.class);
			startActivity(intent);
			return;
		}
		String blankValue1 = (String) blank1.getText();
		String blankValue2 = (String) blank2.getText();
		String blankValue3 = (String) blank3.getText();
		String blankValue4 = (String) blank4.getText();
		String option = "";
		switch (v.getId()) {
		case R.id.back:
			Intent intent = new Intent(BlankGameActivity.this, ChooseModeActivity.class);
			startActivity(intent);
			break;
		case R.id.BoptionA:
			option = (String) option1.getText();
			option1.setVisibility(View.GONE);
			if("".equals(blankValue1) || blankValue1 == null){
				blank1.setText(option);
				selectA = "a";
				node.put("blank1", "a");
			}else if("".equals(blankValue2) || blankValue2 == null){
				blank2.setText(option);
				selectB = "a";
				node.put("blank2", "a");
			}else if("".equals(blankValue3) || blankValue3 == null){
				blank3.setText(option);
				selectC = "a";
				node.put("blank3", "a");
			}else if("".equals(blankValue4) || blankValue4 == null){
				blank4.setText(option);
				selectD = "a";
				node.put("blank4", "a");
			}else{
				//�ĸ��ն��ǿգ�˵���������ˣ����д𰸼�飬����ʵ��������ǲ����ڵ�
				Log.e("blankOnclick", "four blanks have been filled,but this still have option1");
			}
			selectList.add(node);
			if(selectList.size() == 4){
				//�������ĸ��ո񣬽��д𰸼��
				selectresult = new JudgeAnswer(BlankGameActivity.this).
						judgeitBlankAns(selectList, mainMap);
				finalResult = followAction(selectresult);
				int singlescore = MakeIntToString.tranceNum2Score(finalResult);
				totalscores = totalscores + singlescore;//��������
				bquesNum++;//�������
//				new dataDevice().updatescores();//���ݿ����
				new userScoreDevice().stateUpdate();
				Log.e("blankOnclick", "totalscores="+totalscores);
				new ShowResult().showdialog();
			}
			break;
		case R.id.BoptionB:
			option = (String)option2.getText();
			option2.setVisibility(View.GONE);
			if("".equals(blankValue1) || blankValue1 == null){
				blank1.setText(option);
				selectA = "b";
				node.put("blank1", "b");
			}else if("".equals(blankValue2) || blankValue2 == null){
				blank2.setText(option);
				selectB = "b";
				node.put("blank2", "b");
			}else if("".equals(blankValue3) || blankValue3 == null){
				blank3.setText(option);
				selectC = "b";
				node.put("blank3", "b");
			}else if("".equals(blankValue4) || blankValue4 == null){
				blank4.setText(option);
				selectD = "b";
				node.put("blank4", "b");
			}else{
				//�ĸ��ն��ǿգ�˵���������ˣ����д𰸼�飬����ʵ��������ǲ����ڵ�
				Log.e("blankOnclick", "four blanks have been filled,but this still have option2");
			}
			selectList.add(node);
			if(selectList.size() == 4){
				//�������ĸ��ո񣬽��д𰸼��
				selectresult = new JudgeAnswer(BlankGameActivity.this).
						judgeitBlankAns(selectList, mainMap);
				finalResult = followAction(selectresult);
				int singlescore = MakeIntToString.tranceNum2Score(finalResult);
				totalscores = totalscores + singlescore;//��������
				bquesNum++;//�������
//				new dataDevice().updatescores();//���ݿ����
				new userScoreDevice().stateUpdate();
				Log.e("blankOnclick", "totalscores="+totalscores);
				new ShowResult().showdialog();
			}
			break;
		case R.id.BoptionC:
			option = (String)option3.getText();
			option3.setVisibility(View.GONE);
			if("".equals(blankValue1) || blankValue1 == null){
				blank1.setText(option);
				selectA = "c";
				node.put("blank1", "c");
			}else if("".equals(blankValue2) || blankValue2 == null){
				blank2.setText(option);
				selectB = "c";
				node.put("blank2", "c");
			}else if("".equals(blankValue3) || blankValue3 == null){
				blank3.setText(option);
				selectC = "c";
				node.put("blank3", "c");
			}else if("".equals(blankValue4) || blankValue4 == null){
				blank4.setText(option);
				selectD = "c";
				node.put("blank4", "c");
			}else{
				//�ĸ��ն��ǿգ�˵���������ˣ����д𰸼�飬����ʵ��������ǲ����ڵ�
				Log.e("blankOnclick", "four blanks have been filled,but this still have option3");
			}
			selectList.add(node);
			if(selectList.size() == 4){
				//�������ĸ��ո񣬽��д𰸼��
				selectresult = new JudgeAnswer(BlankGameActivity.this).
						judgeitBlankAns(selectList, mainMap);
				finalResult = followAction(selectresult);
				int singlescore = MakeIntToString.tranceNum2Score(finalResult);
				totalscores = totalscores + singlescore;//��������
				bquesNum++;//�������
//				new dataDevice().updatescores();//���ݿ����
				new userScoreDevice().stateUpdate();
				Log.e("blankOnclick", "totalscores="+totalscores);
				new ShowResult().showdialog();
			}
			break;
		case R.id.BoptionD:
			option = (String)option4.getText();
			option4.setVisibility(View.GONE);
			if("".equals(blankValue1) || blankValue1 == null){
				blank1.setText(option);
				selectA = "d";
				node.put("blank1", "d");
			}else if("".equals(blankValue2) || blankValue2 == null){
				blank2.setText(option);
				selectB = "d";
				node.put("blank2", "d");
			}else if("".equals(blankValue3) || blankValue3 == null){
				blank3.setText(option);
				selectC = "d";
				node.put("blank3", "d");
			}else if("".equals(blankValue4) || blankValue4 == null){
				blank4.setText(option);
				selectD = "d";
				node.put("blank4", "d");
			}else{
				//�ĸ��ն��ǿգ�˵���������ˣ����д𰸼�飬����ʵ��������ǲ����ڵ�
				Log.e("blankOnclick", "four blanks have been filled,but this still have option4");
			}
			selectList.add(node);
			if(selectList.size() == 4){
				//�������ĸ��ո񣬽��д𰸼��
				selectresult = new JudgeAnswer(BlankGameActivity.this).
						judgeitBlankAns(selectList, mainMap);
				finalResult = followAction(selectresult);
				int singlescore = MakeIntToString.tranceNum2Score(finalResult);
				totalscores = totalscores + singlescore;//��������
				bquesNum++;//�������
//				new dataDevice().updatescores();//���ݿ����
				new userScoreDevice().stateUpdate();
				Log.e("blankOnclick", "totalscores="+totalscores);
				new ShowResult().showdialog();
//				Dialog dialog = new Dialog(BlankGameActivity.this, R.style.MyDialog);
//				dialog.setContentView(R.layout.changequestiondialog);
//				dialog.setCancelable(false);//���õ���Ի��������ط������岻�ر�
//				dialog.show();
			}
			break;
		case R.id.blank1:
			blank1.setText("");
			if("a".equals(selectA)){
				option1.setVisibility(View.VISIBLE);
				node.put("blank1", "a");
				selectList.remove(node);
			}else if("b".equals(selectA)){
				option2.setVisibility(View.VISIBLE);
				node.put("blank1", "b");
				selectList.remove(node);
			}else if("c".equals(selectA)){
				option3.setVisibility(View.VISIBLE);
				node.put("blank1", "c");
				selectList.remove(node);
			}else if("d".equals(selectA)){
				option4.setVisibility(View.VISIBLE);
				node.put("blank1", "d");
				selectList.remove(node);
			}
			selectA = "";
			break;
		case R.id.blank2:
			blank2.setText("");
			if("a".equals(selectB)){
				option1.setVisibility(View.VISIBLE);
				node.put("blank2", "a");
				selectList.remove(node);
			}else if("b".equals(selectB)){
				option2.setVisibility(View.VISIBLE);
				node.put("blank2", "b");
				selectList.remove(node);
			}else if("c".equals(selectB)){
				option3.setVisibility(View.VISIBLE);
				node.put("blank2", "c");
				selectList.remove(node);
			}else if("d".equals(selectB)){
				option4.setVisibility(View.VISIBLE);
				node.put("blank2", "d");
				selectList.remove(node);
			}
			selectB = "";
			break;
		case R.id.blank3:
			blank3.setText("");
			if("a".equals(selectC)){
				option1.setVisibility(View.VISIBLE);
				node.put("blank3", "a");
				selectList.remove(node);
			}else if("b".equals(selectC)){
				option2.setVisibility(View.VISIBLE);
				node.put("blank3", "b");
				selectList.remove(node);
			}else if("c".equals(selectC)){
				option3.setVisibility(View.VISIBLE);
				node.put("blank3", "c");
				selectList.remove(node);
			}else if("d".equals(selectC)){
				option4.setVisibility(View.VISIBLE);
				node.put("blank3", "d");
				selectList.remove(node);
			}
			selectC = "";
			break;
		case R.id.blank4:
			blank4.setText("");
			if("a".equals(selectD)){
				option1.setVisibility(View.VISIBLE);
				node.put("blank4", "a");
				selectList.remove(node);
			}else if("b".equals(selectD)){
				option2.setVisibility(View.VISIBLE);
				node.put("blank4", "b");
				selectList.remove(node);
			}else if("c".equals(selectD)){
				option3.setVisibility(View.VISIBLE);
				node.put("blank4", "c");
				selectList.remove(node);
			}else if("d".equals(selectD)){
				option4.setVisibility(View.VISIBLE);
				node.put("blank4", "d");
				selectList.remove(node);
			}
			selectD = "";
			break;
		}
	}
	
	@Override
	public void onBackPressed(){
//		super.onBackPressed();
		Toast.makeText(this, "�����Ĭ�Ϸ��ط���", Toast.LENGTH_LONG).show();
	}
	
	private int followAction(List<Map<String,String>> list){
		int resultforint = 0;
		for(int l = 0; l < list.size(); l++){
			Map<String,String> faMap = list.get(l);
			for(String key : faMap.keySet()){
				if("blank1".equals(key)){
					blank1.setBackgroundColor(Color.RED);
				}else if("blank2".equals(key)){
					blank2.setBackgroundColor(Color.RED);
				}else if("blank3".equals(key)){
					blank3.setBackgroundColor(Color.RED);
				}else if("blank4".equals(key)){
					blank4.setBackgroundColor(Color.RED);
				}else if("resultforstring".equals(key)){
					String mi = faMap.get(key);
					resultforint = Integer.parseInt(mi);
				}
			}
		}
		return resultforint;
	}
	
	//����һ����󣬵����Ի�����ʾ����������÷����������ѡ�������һ��
	public class ShowResult{
		
		public ShowResult(){
			
		}
		
		public void showdialog(){
			int singlescore = MakeIntToString.tranceNum2Score(finalResult);
			AlertDialog.Builder builder = new AlertDialog.Builder(BlankGameActivity.this);
			builder.setTitle("");
			builder.setMessage("���"+finalResult+"�������"+singlescore+"���֣�");
			builder.setNegativeButton("����", 
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Intent intent1 = new Intent(BlankGameActivity.this, ChooseModeActivity.class);
							startActivity(intent1);
						}
					});
			builder.setPositiveButton("��һ��", 
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Intent intent1 = new Intent(BlankGameActivity.this, SelectGameActivity.class);
							startActivity(intent1);
						}
					});
			builder.setCancelable(false);
			Dialog dialog = builder.create();
			dialog.show();
		}
	}

}
