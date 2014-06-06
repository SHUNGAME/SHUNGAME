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
	private TextView linequestiontext, scorestext; // ����״̬��Ϣ
	private TextView queA, queB, queC, queD;//4�����
	private TextView optA, optB, optC, optD;//4����ѡ
//	private int sqnumber = 1;//��ʼ���
	// ��mainMap���洢�����Ӧ����Ϣ
	private Map<String, String> mainMap = new HashMap<String, String>();
	private MyApplication myApplication;
	private ActivitiesCollection ac = ActivitiesCollection.getInstance();
	int finalResult = 0;//���յ÷�
	private final static int CHANGE_QUESTION = 1; // �任��Ϸ������Ŀ�ı�ʶ��
	private final static int WAITING_TRIGGER = 2; //
	
	private String userid = "";
	private String username = "";
	private String squesNum = "1";
	private int lquesNum = 1;
	private String module = "blank";
	private String bquesNum = "1"; //��������
	private int totalscores = 0;//��ǰ�ܵ÷�
	private String challengelevel = "1";
	
	// ���̺߳�handler��������Ϣ
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(android.os.Message msg){
			switch (msg.what) {
			case CHANGE_QUESTION:
				mainMap = (Map<String, String>) msg.obj;
				linequestiontext.setText("����������������Ķ�Ӧ��ϵ");
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
		//��ȡ�豸id
		TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		userid = tm.getDeviceId(); //�豸id
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
		//���activity��list
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
		totalscores = totalscores + singlescore;//��������
		lquesNum++;//�������
		new dataDevice().updatescores();//���ݿ����
		new ShowResult().showdialog();
	}
	
	public class StartGame implements Runnable{
		@Override
		public void run() {
			new dataDevice().dealscores();
			getlinequestion getlq = new getlinequestion(LinesGame2Activity.this);
			Map<String, String> map = new HashMap<String, String>();
			// ��MakeIntToString��������ת���ַ�����ѡ���Ӧ��Ŀ
			String str = MakeIntToString.getString(lquesNum);
			String[] strs = new String[] {str};
			map = getlq.getlinequestionMap(strs);
			// ��message�������̴߳�����Ϣ������
			Message message = Message.obtain();
			message.obj = map; // ��map��Ϣ����message��
			message.what = CHANGE_QUESTION; // �趨message�ı�ʾ��
			handler.sendMessage(message); // �����߳��е�handler������Ϣ
		}
	}
	
	
	@Override
	public void onBackPressed(){
//		super.onBackPressed();
		Toast.makeText(this, "������Ĭ�Ϸ��ط���", Toast.LENGTH_LONG).show();
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
	
	
	//����һ����󣬵����Ի�����ʾ����������÷����������ѡ�������һ��
	public class ShowResult{
		
		public ShowResult(){
			
		}
		
		public void showdialog(){
			int singlescore = MakeIntToString.tranceNum2Score(finalResult);
			AlertDialog.Builder builder = new AlertDialog.Builder(LinesGame2Activity.this);
			builder.setTitle("");
			builder.setMessage("���"+finalResult+"�������"+singlescore+"���֣�");
			builder.setNegativeButton("����", 
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Intent intent1 = new Intent(LinesGame2Activity.this, ChooseModeActivity.class);
							startActivity(intent1);
						}
					});
			builder.setPositiveButton("��һ��", 
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
			//��ȡ��ǰϵͳʱ��
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
				//��������������Ϣ
				String ssss = scoresmap.get("scores");
				totalscores = Integer.parseInt(ssss);
				String qqqq = scoresmap.get("linequenum");
				lquesNum = Integer.parseInt(qqqq);
				//����������Ϣ
				username = scoresmap.get("username");
				squesNum = scoresmap.get("selquenum");
				bquesNum = scoresmap.get("linequenum");
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
			getscoresinfo getsi = new getscoresinfo(LinesGame2Activity.this);
			//��ȡ���շ��������
			String newscores = totalscores + "";
			String newbqn = lquesNum + "";
			String [] args = new String[]{userid, currentdate};
			Boolean isExist = getsi.getScoreForCurrent(args);//�жϸ��û�����ļ�¼�Ƿ���ڣ����ھ͸��£������ھͲ���
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
