package com.extra.crazyguess;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import com.extra.crazyguess.Tools.JudgeAnswer;
import com.extra.crazyguess.Tools.MakeIntToString;
import com.extra.crazyguess.getsqldatabase.getscoresinfo;
import com.extra.crazyguess.getsqldatabase.getselectquestion;
import com.extra.crazyguess.single.ActivitiesCollection;
import com.extra.crazyguess.sqlitehelper.DatabaseHelper;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SelectGameActivity extends Activity implements OnClickListener{

	public SelectGameActivity(){
		// TODO Auto-generated method stub
	}
	
	private TextView scors, selectquestiontext, helptext1, helptext2;
	private Button back, help1, help2, Sopt1, Sopt2, Sopt3, Sopt4;
	
//	private int sqnumber = 1; // 当前题目的题号
	private boolean flag = false; // 此题是否答对
	private final static int CHANGE_QUESTION = 1; // 变换游戏界面题目的标识符
	private int finalResult = 0;
	
	private String userid = "";
	private String username = "";
	private int squesNum = 1;
	private String lquesNum = "1";
	private String module = "line";
	private String bquesNum = "1"; //填空题题号
	private int totalscores = 0;//当前总得分
	private String challengelevel = "1";
	
	private int HELP = 0;
	
	// 用mainMap来存储该题对应的信息
	private Map<String, String> mainMap = new HashMap<String, String>();
	
	// 用线程和handler来处理消息
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CHANGE_QUESTION:
				mainMap = (Map<String, String>) msg.obj;
				selectquestiontext.setText(mainMap.get("questions"));
				Sopt1.setText("A."+mainMap.get("optionA"));
				Sopt2.setText("B."+mainMap.get("optionB"));
				Sopt3.setText("C."+mainMap.get("optionC"));
				Sopt4.setText("D."+mainMap.get("optionD"));
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
		setContentView(R.layout.selectquestion);
		//
		scors = (TextView)this.findViewById(R.id.scorestext);
		selectquestiontext = (TextView)this.findViewById(R.id.selectquestiontext);
		helptext1 = (TextView)this.findViewById(R.id.helptext1);
		helptext2 = (TextView)this.findViewById(R.id.helptext2);
		back = (Button)this.findViewById(R.id.back);
		back.setOnClickListener(this);
		help1 = (Button)this.findViewById(R.id.help1);
		help1.setOnClickListener(this);
		help2 = (Button)this.findViewById(R.id.help2);
		help2.setOnClickListener(this);
		Sopt1 = (Button)this.findViewById(R.id.SoptA);
		Sopt1.setOnClickListener(this);
		Sopt2 = (Button)this.findViewById(R.id.SoptB);
		Sopt2.setOnClickListener(this);
		Sopt3 = (Button)this.findViewById(R.id.SoptC);
		Sopt3.setOnClickListener(this);
		Sopt4 = (Button)this.findViewById(R.id.SoptD);
		Sopt4.setOnClickListener(this);
		//添加activity到list
		ActivitiesCollection.getInstance().addActivity(this);
		new Thread(new StartGame()).start();
	}
	
	class StartGame implements Runnable{
		@Override
		public void run() {
			new dataDevice().dealscores();
			getselectquestion getsq = new getselectquestion(SelectGameActivity.this);
			Map<String, String> map = new HashMap<String, String>();
			// 用MakeIntToString工具类来转换字符，并选择对应题目
			String str = MakeIntToString.getString(squesNum);
			String[] strs = new String[] {str};
			map = getsq.getselectquestionMap(strs);
			// 用message来向主线程传递信息并处理
			Message message = Message.obtain();
			message.obj = map; // 将map信息放入message中
			message.what = CHANGE_QUESTION; // 设定message的标示符
			handler.sendMessage(message); // 向主线程中的handler发送信息
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back:
			Intent intent = new Intent(SelectGameActivity.this, ChooseModeActivity.class);
			startActivity(intent);
			break;
		case R.id.help1:
			if(HELP == 0){
				helptext1.setText(mainMap.get("help1"));
				HELP = 1;
			}
			break;
		case R.id.help2:
			if(HELP == 1){
				helptext2.setText(mainMap.get("help2"));
				HELP = 2;
			}
			break;
		case R.id.SoptA:
			// 返回当前是否答对
			flag = new JudgeAnswer(SelectGameActivity.this).judgeit("a", mainMap);
			if(flag){
				if(HELP == 0){
					finalResult = 5;
				}else if(HELP == 1){
					finalResult = 3;
				}else if(HELP == 2){
					finalResult = 1;
				}
			}
			totalscores = totalscores + finalResult;//分数增加
			squesNum++;//题号自增
			new dataDevice().updatescores();//数据库更新
			new ShowResult().showdialog();
			break;
		case R.id.SoptB:
			// 返回当前是否答对
			flag = new JudgeAnswer(SelectGameActivity.this).judgeit("b", mainMap);
			if(flag){
				if(HELP == 0){
					finalResult = 5;
				}else if(HELP == 1){
					finalResult = 3;
				}else if(HELP == 2){
					finalResult = 1;
				}
			}
			totalscores = totalscores + finalResult;//分数增加
			squesNum++;//题号自增
			new dataDevice().updatescores();//数据库更新
			new ShowResult().showdialog();
			break;
		case R.id.SoptC:
			// 返回当前是否答对
			flag = new JudgeAnswer(SelectGameActivity.this).judgeit("c", mainMap);
			if(flag){
				if(HELP == 0){
					finalResult = 5;
				}else if(HELP == 1){
					finalResult = 3;
				}else if(HELP == 2){
					finalResult = 1;
				}
			}
			totalscores = totalscores + finalResult;//分数增加
			squesNum++;//题号自增
			new dataDevice().updatescores();//数据库更新
			new ShowResult().showdialog();
			break;
		case R.id.SoptD:
			// 返回当前是否答对
			flag = new JudgeAnswer(SelectGameActivity.this).judgeit("d", mainMap);
			if(flag){
				if(HELP == 0){
					finalResult = 5;
				}else if(HELP == 1){
					finalResult = 3;
				}else if(HELP == 2){
					finalResult = 1;
				}
			}
			totalscores = totalscores + finalResult;//分数增加
			squesNum++;//题号自增
			new dataDevice().updatescores();//数据库更新
			new ShowResult().showdialog();
			break;
		}
	}
	
	//答完一道题后，弹出对话框，显示答题情况及得分情况，并供选择进行下一题
	public class ShowResult{
		
		public ShowResult(){
			
		}
		
		public void showdialog(){
			
			AlertDialog.Builder builder = new AlertDialog.Builder(SelectGameActivity.this);
			builder.setTitle("");
			if(flag){
				if(HELP == 0){
					builder.setMessage("回答正确，获得"+finalResult+"积分！");
				}else {
					builder.setMessage("回答正确，使用帮助"+HELP+"个，获得"+finalResult+"积分！");
				}
			}else{
				builder.setMessage("回答错误</br>获得"+finalResult+"积分！");
			}
			builder.setNegativeButton("返回", 
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Intent intent1 = new Intent(SelectGameActivity.this, ChooseModeActivity.class);
							startActivity(intent1);
						}
					});
			builder.setPositiveButton("下一题", 
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Intent intent1 = new Intent(SelectGameActivity.this, LinesGame2Activity.class);
							startActivity(intent1);
						}
					});
			builder.setCancelable(false);
			Dialog dialog = builder.create();
			dialog.show();
		}
	}
	
	public class userScoreDevice{
		public userScoreDevice(){
			
		}
		
		public Map<String,String> queryLatestData(){
			Map<String,String> map = new HashMap<String,String>();
			//创建DatabaseHelper对象  
			DatabaseHelper dbHelper = new DatabaseHelper(SelectGameActivity.this,"userAndQuestion",1);
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
			DatabaseHelper dbHelper = new DatabaseHelper(SelectGameActivity.this,"userAndQuestion",1);
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
			DatabaseHelper dbHelper = new DatabaseHelper(SelectGameActivity.this,"userAndQuestion",1);
			// 得到一个可写的SQLiteDatabase对象  
			SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();
			// 创建一个ContentValues对象  
			ContentValues values = new ContentValues();
			values.put("scores", score);
			values.put("selquenum", bqn);
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
			DatabaseHelper dbHelper = new DatabaseHelper(SelectGameActivity.this,"userAndQuestion",1);
			// 得到一个可写的SQLiteDatabase对象  
			SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();  
			// 调用insert方法，就可以将数据插入到数据库当中  
			sqliteDatabase.insert("userscore", null, values);
		}
		
		public void initQuery(){
			Map<String,String> miMap = queryLatestData();
			if(miMap.size() == 0){
				totalscores = 0;
				squesNum = 1;
			}else{
				//设置选择题相关信息
				String ssss = miMap.get("scores");
				totalscores = Integer.parseInt(ssss);
				String sqn = miMap.get("selquenum");
				squesNum = Integer.parseInt(sqn);
				//设置其他信息
				username = miMap.get("username");
				bquesNum = miMap.get("blaquenum");
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
			String newsqn = squesNum + "";
			
		}
	}
	
	
	public class dataDevice{
		
		public dataDevice(){
			
		}
		
		public void dealscores (){
			//
			String [] cstrs = new String[] {userid};
			//
			getscoresinfo getsi = new getscoresinfo(SelectGameActivity.this);
			Map<String, String> scoresmap = getsi.getscoresinfoMap(cstrs);
			if(scoresmap.size() == 0){
				totalscores = 0;
				squesNum = 1;
			}else{
				//设置填空题相关信息
				String ssss = scoresmap.get("scores");
				totalscores = Integer.parseInt(ssss);
				String qqqq = scoresmap.get("selquenum");
				squesNum = Integer.parseInt(qqqq);
				//设置其他信息
				username = scoresmap.get("username");
				bquesNum = scoresmap.get("selquenum");
				lquesNum = scoresmap.get("linequenum");
				challengelevel = scoresmap.get("challengelevel");
//				module = scoresmap.get("module");
			}
		}
		
		@SuppressLint("SimpleDateFormat")
		public void updatescores(){
			//获取当前系统时间
			SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String currentdate = sDateFormat.format(new java.util.Date());
			//
			getscoresinfo getsi = new getscoresinfo(SelectGameActivity.this);
			//获取最终分数跟题号
			String newscores = totalscores + "";
			String newbqn = squesNum + "";
			String [] args = new String[]{userid, currentdate};
			Boolean isExist = getsi.getScoreForCurrent(args);//判断该用户当天的记录是否存在，存在就更新，不存在就插入
			if(isExist){
				args = new String[]{newscores, newbqn, module, userid, currentdate};
				getsi.updateScoreInfoForSelectquestion(args);
			}else{
				args = new String[]{userid,username,currentdate,newscores,newbqn,bquesNum,lquesNum,
						module,challengelevel};
				getsi.insertScoreInfo(args);
			}
		}
	}
	
	@Override
	public void onBackPressed(){
		Toast.makeText(this, "单选按返回键", Toast.LENGTH_LONG).show();
	}
}