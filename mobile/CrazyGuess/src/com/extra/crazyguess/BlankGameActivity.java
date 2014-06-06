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
//	private int sqnumber = 1;//初始题号
	private final static int CHANGE_QUESTION = 1; // 变换游戏界面题目的标识符
	private final static int LASTQUESTION = 2; // 填空总题数
	
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
	private int bquesNum = 1; //填空题题号
	private int totalscores = 0;//当前总得分
	private String challengelevel = "1";
	
	
	// 用mainMap来存储该题对应的信息
	private Map<String, String> mainMap = new HashMap<String, String>();
	private List<Map<String,String>> selectList = new ArrayList<Map<String,String>>();
	private List<Map<String,String>> selectresult = new ArrayList<Map<String,String>>();
	
	// 用线程和handler来处理消息
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
		//获取设备id
		TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		userid = tm.getDeviceId(); //设备id
//		new dataDevice().dealscores();
		new userScoreDevice().initQuery();
		ActivitiesCollection.getInstance().addActivity(this);
		if(bquesNum > LASTQUESTION){//题目已经答完
			setContentView(R.layout.completed);
			back = (Button)this.findViewById(R.id.back);
			back.setOnClickListener(this);
		}else{
			//设置布局
			setContentView(R.layout.blankquestion);
			//获取布局上的控件
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
			
			//开启游戏
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
			// 用MakeIntToString工具类来转换字符，并选择对应题目
			String str = MakeIntToString.getString(bquesNum);
			String[] strs = new String[] {str};
			map = getlq.getblankquestionMap(strs);
			// 用message来向主线程传递信息并处理
			Message message = Message.obtain();
			message.obj = map; // 将map信息放入message中
			message.what = CHANGE_QUESTION; // 设定message的标示符
			handler.sendMessage(message); // 向主线程中的handler发送信息
		}
	}
	
	public class userScoreDevice{
		public userScoreDevice(){
			
		}
		
		public Map<String,String> queryLatestData(){
			Map<String,String> map = new HashMap<String,String>();
			//创建DatabaseHelper对象  
			DatabaseHelper dbHelper = new DatabaseHelper(BlankGameActivity.this,"userAndQuestion",1);
			// 得到一个只读的SQLiteDatabase对象  
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
			//获取当前系统时间
			SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String currentdate = sDateFormat.format(new java.util.Date());
			//创建DatabaseHelper对象  
			DatabaseHelper dbHelper = new DatabaseHelper(BlankGameActivity.this,"userAndQuestion",1);
			// 得到一个只读的SQLiteDatabase对象  
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
			// 创建一个DatabaseHelper对象  
			DatabaseHelper dbHelper = new DatabaseHelper(BlankGameActivity.this,"userAndQuestion",1);
			// 得到一个可写的SQLiteDatabase对象  
			SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();
			// 创建一个ContentValues对象  
			ContentValues values = new ContentValues();
			values.put("scores", score);
			values.put("blaquenum", bqn);
			// 调用update方法  
			sqliteDatabase.update("userscore", values, "userid=? and datetime=?", 
			new String[] {userid, datetime});
		}
		
		public void insert(String id, String date, String score, String squesNum, String bquesNum,
							String lquesNum, String challengelevel){
			// 创建ContentValues对象  
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
        	// 创建DatabaseHelper对象  
        	DatabaseHelper dbHelper = new DatabaseHelper(BlankGameActivity.this,"userAndQuestion",1);  
        	// 得到一个可写的SQLiteDatabase对象  
        	SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();  
        	// 调用insert方法，就可以将数据插入到数据库当中  
        	sqliteDatabase.insert("userscore", null, values);
		}
		
		
		public void initQuery(){
			Map<String,String> miMap = queryLatestData();
			if(miMap.size() == 0){
				totalscores = 0;
				bquesNum = 1;
			}else{
				//设置填空题相关信息
				String ssss = miMap.get("scores");
				totalscores = Integer.parseInt(ssss);
				String bqn = miMap.get("blaquenum");
				bquesNum = Integer.parseInt(bqn);
				//设置其他信息
				username = miMap.get("username");
				squesNum = miMap.get("selquenum");
				lquesNum = miMap.get("linequenum");
				challengelevel = miMap.get("challengelevel");
			}
		}
		
		@SuppressLint("SimpleDateFormat")
		public void stateUpdate(){
			//获取当前系统时间
			SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String currentdate = sDateFormat.format(new java.util.Date());
			//获取最终分数跟题号
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
				//设置填空题相关信息
				String ssss = scoresmap.get("scores");
				totalscores = Integer.parseInt(ssss);
//				module = scoresmap.get("module");
			}
			if(statesmap.size() == 0){
				bquesNum = 1;
			}else{
				String bqn = statesmap.get("blaquenum");
				bquesNum = Integer.parseInt(bqn);
				//设置其他信息
				username = statesmap.get("username");
				squesNum = statesmap.get("selquenum");
				lquesNum = statesmap.get("linequenum");
				challengelevel = scoresmap.get("challengelevel");
			}
		}
		
		@SuppressLint("SimpleDateFormat")
		public Map<String, String> queryForUser(){
			Map<String, String> map = new HashMap<String, String>();
			//创建DatabaseHelper对象  
			DatabaseHelper dbHelper = new DatabaseHelper(BlankGameActivity.this,"userAndQuestion",1);
			// 得到一个只读的SQLiteDatabase对象  
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
			//获取当前系统时间
			SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String currentdate = sDateFormat.format(new java.util.Date());
			//创建DatabaseHelper对象  
			DatabaseHelper dbHelper = new DatabaseHelper(BlankGameActivity.this,"userAndQuestion",1);
			// 得到一个只读的SQLiteDatabase对象  
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
			//创建DatabaseHelper对象  
			DatabaseHelper dbHelper = new DatabaseHelper(BlankGameActivity.this,"userAndQuestion",1);
			// 得到一个只读的SQLiteDatabase对象  
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
	    	// 创建一个DatabaseHelper对象  
	        DatabaseHelper dbHelper = new DatabaseHelper(BlankGameActivity.this,"userAndQuestion",1);  
	        // 得到一个可写的SQLiteDatabase对象  
	        SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();  
	        // 创建一个ContentValues对象  
	        ContentValues values = new ContentValues();  
	        values.put("scores", score);  
	        // 调用update方法  
	        sqliteDatabase.update("userinfo", values, "userid=? and datetime=?", 
	        		new String[] {userid, datetime});
	    }
		
		public void updateState(int bnum){
			// 创建一个DatabaseHelper对象  
	        DatabaseHelper dbHelper = new DatabaseHelper(BlankGameActivity.this,"userAndQuestion",1);  
	        // 得到一个可写的SQLiteDatabase对象  
	        SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();  
	        // 创建一个ContentValues对象  
	        ContentValues values = new ContentValues();  
	        values.put("blaquenum", bnum);  
	        // 调用update方法  
	        sqliteDatabase.update("questionstate", values, "userid=?", 
	        		new String[] {userid});
		}
		
		public void insertUser(String date, String score){
	    	// 创建ContentValues对象  
	        ContentValues values = new ContentValues();
	        values.put("id", 1);
	        values.put("userid", userid);
        	values.put("username", "");
        	values.put("datetime", date);
        	values.put("scores", score);
        	values.put("otherinfo", "");
        	// 创建DatabaseHelper对象  
        	DatabaseHelper dbHelper = new DatabaseHelper(BlankGameActivity.this,"userAndQuestion",1);  
        	// 得到一个可写的SQLiteDatabase对象  
        	SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();  
        	// 调用insert方法，就可以将数据插入到数据库当中  
        	sqliteDatabase.insert("userinfo", null, values);  
	    }
		
		@SuppressLint("SimpleDateFormat")
		public void updatescores(){
			//获取当前系统时间
			SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String currentdate = sDateFormat.format(new java.util.Date());
			//获取最终分数跟题号
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
//			Boolean isExist = getsi.getScoreForCurrent(args);//判断该用户当天的记录是否存在，存在就更新，不存在就插入
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
				//四个空都非空，说明都填满了，进行答案检查，但其实这种情况是不存在的
				Log.e("blankOnclick", "four blanks have been filled,but this still have option1");
			}
			selectList.add(node);
			if(selectList.size() == 4){
				//填满了四个空格，进行答案检查
				selectresult = new JudgeAnswer(BlankGameActivity.this).
						judgeitBlankAns(selectList, mainMap);
				finalResult = followAction(selectresult);
				int singlescore = MakeIntToString.tranceNum2Score(finalResult);
				totalscores = totalscores + singlescore;//分数增加
				bquesNum++;//题号自增
//				new dataDevice().updatescores();//数据库更新
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
				//四个空都非空，说明都填满了，进行答案检查，但其实这种情况是不存在的
				Log.e("blankOnclick", "four blanks have been filled,but this still have option2");
			}
			selectList.add(node);
			if(selectList.size() == 4){
				//填满了四个空格，进行答案检查
				selectresult = new JudgeAnswer(BlankGameActivity.this).
						judgeitBlankAns(selectList, mainMap);
				finalResult = followAction(selectresult);
				int singlescore = MakeIntToString.tranceNum2Score(finalResult);
				totalscores = totalscores + singlescore;//分数增加
				bquesNum++;//题号自增
//				new dataDevice().updatescores();//数据库更新
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
				//四个空都非空，说明都填满了，进行答案检查，但其实这种情况是不存在的
				Log.e("blankOnclick", "four blanks have been filled,but this still have option3");
			}
			selectList.add(node);
			if(selectList.size() == 4){
				//填满了四个空格，进行答案检查
				selectresult = new JudgeAnswer(BlankGameActivity.this).
						judgeitBlankAns(selectList, mainMap);
				finalResult = followAction(selectresult);
				int singlescore = MakeIntToString.tranceNum2Score(finalResult);
				totalscores = totalscores + singlescore;//分数增加
				bquesNum++;//题号自增
//				new dataDevice().updatescores();//数据库更新
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
				//四个空都非空，说明都填满了，进行答案检查，但其实这种情况是不存在的
				Log.e("blankOnclick", "four blanks have been filled,but this still have option4");
			}
			selectList.add(node);
			if(selectList.size() == 4){
				//填满了四个空格，进行答案检查
				selectresult = new JudgeAnswer(BlankGameActivity.this).
						judgeitBlankAns(selectList, mainMap);
				finalResult = followAction(selectresult);
				int singlescore = MakeIntToString.tranceNum2Score(finalResult);
				totalscores = totalscores + singlescore;//分数增加
				bquesNum++;//题号自增
//				new dataDevice().updatescores();//数据库更新
				new userScoreDevice().stateUpdate();
				Log.e("blankOnclick", "totalscores="+totalscores);
				new ShowResult().showdialog();
//				Dialog dialog = new Dialog(BlankGameActivity.this, R.style.MyDialog);
//				dialog.setContentView(R.layout.changequestiondialog);
//				dialog.setCancelable(false);//设置点击对话框其他地方，窗体不关闭
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
		Toast.makeText(this, "填空题默认返回方法", Toast.LENGTH_LONG).show();
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
	
	//答完一道题后，弹出对话框，显示答题情况及得分情况，并供选择进行下一题
	public class ShowResult{
		
		public ShowResult(){
			
		}
		
		public void showdialog(){
			int singlescore = MakeIntToString.tranceNum2Score(finalResult);
			AlertDialog.Builder builder = new AlertDialog.Builder(BlankGameActivity.this);
			builder.setTitle("");
			builder.setMessage("答对"+finalResult+"个，获得"+singlescore+"积分！");
			builder.setNegativeButton("返回", 
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Intent intent1 = new Intent(BlankGameActivity.this, ChooseModeActivity.class);
							startActivity(intent1);
						}
					});
			builder.setPositiveButton("下一题", 
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
