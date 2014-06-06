package com.extra.crazyguess;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;
import android.widget.Toast;

import com.extra.crazyguess.Tools.MakeIntToString;
import com.extra.crazyguess.draw.Drawl;
import com.extra.crazyguess.getsqldatabase.getlinequestion;
import com.extra.crazyguess.getsqldatabase.getscoresinfo;
import com.extra.crazyguess.single.ActivitiesCollection;

public class LinesGame2Activity extends Activity implements OnTouchListener{
	public LinesGame2Activity(){
		// TODO Auto-generated method stub
	}
	private TextView linequestiontext, scorestext; // 各种状态信息
	private TextView queA, queB, queC, queD;//4个题干
	private TextView optA, optB, optC, optD;//4个备选
//	private int sqnumber = 1;//初始题号
	// 用mainMap来存储该题对应的信息
	private Map<String, String> mainMap = new HashMap<String, String>();
	private MyApplication myApplication;
	private ActivitiesCollection ac = ActivitiesCollection.getInstance();
	int finalResult = 0;//最终得分
	private final static int CHANGE_QUESTION = 1; // 变换游戏界面题目的标识符
	private final static int WAITING_TRIGGER = 2; //
	
	private String userid = "";
	private String username = "";
	private String squesNum = "1";
	private int lquesNum = 1;
	private String module = "blank";
	private String bquesNum = "1"; //填空题题号
	private int totalscores = 0;//当前总得分
	private String challengelevel = "1";
	
	// 用线程和handler来处理消息
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(android.os.Message msg){
			switch (msg.what) {
			case CHANGE_QUESTION:
				mainMap = (Map<String, String>) msg.obj;
				linequestiontext.setText("请连接相左右两侧的对应关系");
				queA.setText(mainMap.get("Qa"));
				queB.setText(mainMap.get("Qb"));
				queC.setText(mainMap.get("Qc"));
				queD.setText(mainMap.get("Qd"));
				optA.setText(mainMap.get("Oa"));
				optB.setText(mainMap.get("Ob"));
				optC.setText(mainMap.get("Oc"));
				optD.setText(mainMap.get("Od"));
				scorestext.setText(totalscores+"");
				myApplication.setAnwsers(mainMap);
				break;
			case WAITING_TRIGGER:
				break;
			}
		}
	};
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myApplication = (MyApplication) getApplication();
		setContentView(R.layout.linesquestion);
		//获取设备id
		TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		userid = tm.getDeviceId(); //设备id
		//
		Drawl dw = (Drawl)this.findViewById(R.id.drawline);
		dw.setApplication(myApplication, this);
		//
		scorestext = (TextView)this.findViewById(R.id.scorestext);
		linequestiontext = (TextView) this.findViewById(R.id.linequestiontext);
		queA = (TextView)this.findViewById(R.id.queA);
//		queA.setAlpha((float) 0.5);
		queA.setOnTouchListener(this);
		queB = (TextView)this.findViewById(R.id.queB);
//		queB.setAlpha((float) 0.5);
		queB.setOnTouchListener(this);
		queC = (TextView)this.findViewById(R.id.queC);
//		queC.setAlpha((float) 0.5);
		queC.setOnTouchListener(this);
		queD = (TextView)this.findViewById(R.id.queD);
//		queD.setAlpha((float) 0.5);
		queD.setOnTouchListener(this);
		optA = (TextView)this.findViewById(R.id.optA);
//		optA.setAlpha((float) 0.5);
		optA.setOnTouchListener(this);
		optB = (TextView)this.findViewById(R.id.optB);
//		optB.setAlpha((float) 0.5);
		optB.setOnTouchListener(this);
		optC = (TextView)this.findViewById(R.id.optC);
//		optC.setAlpha((float) 0.5);
		optC.setOnTouchListener(this);
		optD = (TextView)this.findViewById(R.id.optD);
//		optD.setAlpha((float) 0.5);
		optD.setOnTouchListener(this);
		//添加activity到list
		ac.addActivity(this);
		new Thread(new StartGame()).start();
	}
	
	public void startNextGame(List<Map<String,String>> list){
		//
//		new Thread(new StartGame()).start();
		for(int l = 0; l < list.size(); l++){
			Map<String,String> faMap = list.get(l);
			for(String key : faMap.keySet()){
				if("Oa".equals(key)){
					optA.setBackgroundColor(Color.RED);
				}else if("Ob".equals(key)){
					optB.setBackgroundColor(Color.RED);
				}else if("Oc".equals(key)){
					optC.setBackgroundColor(Color.RED);
				}else if("Od".equals(key)){
					optD.setBackgroundColor(Color.RED);
				}else if("resultstr".equals(key)){
					String mi = faMap.get(key);
					finalResult = Integer.parseInt(mi);
				}
			}
		}
		int singlescore = MakeIntToString.tranceNum2Score(finalResult);
		totalscores = totalscores + singlescore;//分数增加
		lquesNum++;//题号自增
		new dataDevice().updatescores();//数据库更新
		new ShowResult().showdialog();
	}
	
	public class StartGame implements Runnable{
		@Override
		public void run() {
			new dataDevice().dealscores();
			getlinequestion getlq = new getlinequestion(LinesGame2Activity.this);
			Map<String, String> map = new HashMap<String, String>();
			// 用MakeIntToString工具类来转换字符，并选择对应题目
			String str = MakeIntToString.getString(lquesNum);
			String[] strs = new String[] {str};
			map = getlq.getlinequestionMap(strs);
			// 用message来向主线程传递信息并处理
			Message message = Message.obtain();
			message.obj = map; // 将map信息放入message中
			message.what = CHANGE_QUESTION; // 设定message的标示符
			handler.sendMessage(message); // 向主线程中的handler发送信息
		}
	}
	
	
	@Override
	public void onBackPressed(){
//		super.onBackPressed();
		Toast.makeText(this, "连线题默认返回方法", Toast.LENGTH_LONG).show();
	}
	
	
	@Override
	public boolean onTouch(View v, MotionEvent arg1) {
		// TODO Auto-generated method stub
		switch (v.getId()){
		case R.id.queA:
			//
			myApplication.setFromView(v);
			break;
		case R.id.queB:
			//
			myApplication.setFromView(v);
			break;
		case R.id.queC:
			//
			myApplication.setFromView(v);
			break;
		case R.id.queD:
			//
			myApplication.setFromView(v);
			break;
		case R.id.optA:
			//
			myApplication.setToView(v);
			break;
		case R.id.optB:
			//
			myApplication.setToView(v);
			break;
		case R.id.optC:
			//
			myApplication.setToView(v);
			break;
		case R.id.optD:
			//
			myApplication.setToView(v);
			break;
		}
		return false;
	}
	
	
	//答完一道题后，弹出对话框，显示答题情况及得分情况，并供选择进行下一题
	public class ShowResult{
		
		public ShowResult(){
			
		}
		
		public void showdialog(){
			int singlescore = MakeIntToString.tranceNum2Score(finalResult);
			AlertDialog.Builder builder = new AlertDialog.Builder(LinesGame2Activity.this);
			builder.setTitle("");
			builder.setMessage("答对"+finalResult+"个，获得"+singlescore+"积分！");
			builder.setNegativeButton("返回", 
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Intent intent1 = new Intent(LinesGame2Activity.this, ChooseModeActivity.class);
							startActivity(intent1);
						}
					});
			builder.setPositiveButton("下一题", 
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Intent intent1 = new Intent(LinesGame2Activity.this, BlankGameActivity.class);
							startActivity(intent1);
						}
					});
			builder.setCancelable(false);
			Dialog dialog = builder.create();
			dialog.show();
		}
	}
	
	
	public class dataDevice{
		
		public dataDevice(){
			
		}
		
		public void dealscores (){
			//获取当前系统时间
//			SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//			String currentdate = sDateFormat.format(new java.util.Date());
			//
			String [] cstrs = new String[] {userid};
			//
			getscoresinfo getsi = new getscoresinfo(LinesGame2Activity.this);
			Map<String, String> scoresmap = getsi.getscoresinfoMap(cstrs);
			if(scoresmap.size() == 0){
				totalscores = 0;
				lquesNum = 1;
			}else{
				//设置填空题相关信息
				String ssss = scoresmap.get("scores");
				totalscores = Integer.parseInt(ssss);
				String qqqq = scoresmap.get("linequenum");
				lquesNum = Integer.parseInt(qqqq);
				//设置其他信息
				username = scoresmap.get("username");
				squesNum = scoresmap.get("selquenum");
				bquesNum = scoresmap.get("linequenum");
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
			getscoresinfo getsi = new getscoresinfo(LinesGame2Activity.this);
			//获取最终分数跟题号
			String newscores = totalscores + "";
			String newbqn = lquesNum + "";
			String [] args = new String[]{userid, currentdate};
			Boolean isExist = getsi.getScoreForCurrent(args);//判断该用户当天的记录是否存在，存在就更新，不存在就插入
			if(isExist){
				args = new String[]{newscores, newbqn, module, userid, currentdate};
				getsi.updateScoreInfoForLinequestion(args);
			}else{
				args = new String[]{userid,username,currentdate,newscores,squesNum,bquesNum,newbqn,
						module,challengelevel};
				getsi.insertScoreInfo(args);
			}
		}
	}
}
