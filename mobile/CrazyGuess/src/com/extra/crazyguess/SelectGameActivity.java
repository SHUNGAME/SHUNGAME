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
	
//	private int sqnumber = 1; // ��ǰ��Ŀ�����
	private boolean flag = false; // �����Ƿ���
	private final static int CHANGE_QUESTION = 1; // �任��Ϸ������Ŀ�ı�ʶ��
	private int finalResult = 0;
	
	private String userid = "";
	private String username = "";
	private int squesNum = 1;
	private String lquesNum = "1";
	private String module = "line";
	private String bquesNum = "1"; //��������
	private int totalscores = 0;//��ǰ�ܵ÷�
	private String challengelevel = "1";
	
	private int HELP = 0;
	
	// ��mainMap���洢�����Ӧ����Ϣ
	private Map<String, String> mainMap = new HashMap<String, String>();
	
	// ���̺߳�handler��������Ϣ
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
		//��ȡ�豸id
		TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		userid = tm.getDeviceId(); //�豸id
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
		//���activity��list
		ActivitiesCollection.getInstance().addActivity(this);
		new Thread(new StartGame()).start();
	}
	
	class StartGame implements Runnable{
		@Override
		public void run() {
			new dataDevice().dealscores();
			getselectquestion getsq = new getselectquestion(SelectGameActivity.this);
			Map<String, String> map = new HashMap<String, String>();
			// ��MakeIntToString��������ת���ַ�����ѡ���Ӧ��Ŀ
			String str = MakeIntToString.getString(squesNum);
			String[] strs = new String[] {str};
			map = getsq.getselectquestionMap(strs);
			// ��message�������̴߳�����Ϣ������
			Message message = Message.obtain();
			message.obj = map; // ��map��Ϣ����message��
			message.what = CHANGE_QUESTION; // �趨message�ı�ʾ��
			handler.sendMessage(message); // �����߳��е�handler������Ϣ
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
			// ���ص�ǰ�Ƿ���
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
			totalscores = totalscores + finalResult;//��������
			squesNum++;//�������
			new dataDevice().updatescores();//���ݿ����
			new ShowResult().showdialog();
			break;
		case R.id.SoptB:
			// ���ص�ǰ�Ƿ���
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
			totalscores = totalscores + finalResult;//��������
			squesNum++;//�������
			new dataDevice().updatescores();//���ݿ����
			new ShowResult().showdialog();
			break;
		case R.id.SoptC:
			// ���ص�ǰ�Ƿ���
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
			totalscores = totalscores + finalResult;//��������
			squesNum++;//�������
			new dataDevice().updatescores();//���ݿ����
			new ShowResult().showdialog();
			break;
		case R.id.SoptD:
			// ���ص�ǰ�Ƿ���
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
			totalscores = totalscores + finalResult;//��������
			squesNum++;//�������
			new dataDevice().updatescores();//���ݿ����
			new ShowResult().showdialog();
			break;
		}
	}
	
	//����һ����󣬵����Ի�����ʾ����������÷����������ѡ�������һ��
	public class ShowResult{
		
		public ShowResult(){
			
		}
		
		public void showdialog(){
			
			AlertDialog.Builder builder = new AlertDialog.Builder(SelectGameActivity.this);
			builder.setTitle("");
			if(flag){
				if(HELP == 0){
					builder.setMessage("�ش���ȷ�����"+finalResult+"���֣�");
				}else {
					builder.setMessage("�ش���ȷ��ʹ�ð���"+HELP+"�������"+finalResult+"���֣�");
				}
			}else{
				builder.setMessage("�ش����</br>���"+finalResult+"���֣�");
			}
			builder.setNegativeButton("����", 
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Intent intent1 = new Intent(SelectGameActivity.this, ChooseModeActivity.class);
							startActivity(intent1);
						}
					});
			builder.setPositiveButton("��һ��", 
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
			//����DatabaseHelper����  
			DatabaseHelper dbHelper = new DatabaseHelper(SelectGameActivity.this,"userAndQuestion",1);
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
			DatabaseHelper dbHelper = new DatabaseHelper(SelectGameActivity.this,"userAndQuestion",1);
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
			DatabaseHelper dbHelper = new DatabaseHelper(SelectGameActivity.this,"userAndQuestion",1);
			// �õ�һ����д��SQLiteDatabase����  
			SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();
			// ����һ��ContentValues����  
			ContentValues values = new ContentValues();
			values.put("scores", score);
			values.put("selquenum", bqn);
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
			DatabaseHelper dbHelper = new DatabaseHelper(SelectGameActivity.this,"userAndQuestion",1);
			// �õ�һ����д��SQLiteDatabase����  
			SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();  
			// ����insert�������Ϳ��Խ����ݲ��뵽���ݿ⵱��  
			sqliteDatabase.insert("userscore", null, values);
		}
		
		public void initQuery(){
			Map<String,String> miMap = queryLatestData();
			if(miMap.size() == 0){
				totalscores = 0;
				squesNum = 1;
			}else{
				//����ѡ���������Ϣ
				String ssss = miMap.get("scores");
				totalscores = Integer.parseInt(ssss);
				String sqn = miMap.get("selquenum");
				squesNum = Integer.parseInt(sqn);
				//����������Ϣ
				username = miMap.get("username");
				bquesNum = miMap.get("blaquenum");
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
				//��������������Ϣ
				String ssss = scoresmap.get("scores");
				totalscores = Integer.parseInt(ssss);
				String qqqq = scoresmap.get("selquenum");
				squesNum = Integer.parseInt(qqqq);
				//����������Ϣ
				username = scoresmap.get("username");
				bquesNum = scoresmap.get("selquenum");
				lquesNum = scoresmap.get("linequenum");
				challengelevel = scoresmap.get("challengelevel");
//				module = scoresmap.get("module");
			}
		}
		
		@SuppressLint("SimpleDateFormat")
		public void updatescores(){
			//��ȡ��ǰϵͳʱ��
			SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String currentdate = sDateFormat.format(new java.util.Date());
			//
			getscoresinfo getsi = new getscoresinfo(SelectGameActivity.this);
			//��ȡ���շ��������
			String newscores = totalscores + "";
			String newbqn = squesNum + "";
			String [] args = new String[]{userid, currentdate};
			Boolean isExist = getsi.getScoreForCurrent(args);//�жϸ��û�����ļ�¼�Ƿ���ڣ����ھ͸��£������ھͲ���
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
		Toast.makeText(this, "��ѡ�����ؼ�", Toast.LENGTH_LONG).show();
	}
}