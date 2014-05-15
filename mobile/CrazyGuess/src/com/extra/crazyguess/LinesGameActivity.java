package com.extra.crazyguess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.extra.crazyguess.Tools.JudgeAnswer;
import com.extra.crazyguess.Tools.MakeIntToString;
import com.extra.crazyguess.draw.Drawl;
import com.extra.crazyguess.getsqldatabase.getlinequestion;
import com.extra.crazyguess.getsqldatabase.getquestion;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class LinesGameActivity extends Activity implements OnClickListener {
	public LinesGameActivity() {
		// TODO Auto-generated constructor stub
	}
	
	private TextView linequestiontext, scorestext; // ����״̬��Ϣ
	private Button queA, queB, queC, queD;//4�����
	private Button optA, optB, optC, optD;//4����ѡ
	private String ansA, ansB, ansC, ansD;//4����
	private int sqnumber = 1;//��ʼ���
	private int qnumber = 1; // ��ǰ��Ŀ�����
	// ��mainMap���洢�����Ӧ����Ϣ
	private Map<String, String> mainMap = new HashMap<String, String>();
	//��miList���洢�û����ߵ����
	private List<Map<String, String>> miList = new ArrayList<Map<String, String>>();
	private int flag = 0; //������������0�����0����1�����1����2�����2����3�����3����4�����4����
	private int questionflag = 0;//�������ı�־
	private final static int CHANGE_QUESTION = 1; // �任��Ϸ������Ŀ�ı�ʶ��
	private final static int WAITING_TRIGGER = 2; //
	
	private Drawl bDrawl;
	
	// ���̺߳�handler��������Ϣ
	private Handler handler = new Handler() {
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
				break;
			case WAITING_TRIGGER:
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lines);
		scorestext = (TextView)this.findViewById(R.id.scorestext);
		linequestiontext = (TextView) this.findViewById(R.id.linequestiontext);
		queA = (Button)this.findViewById(R.id.queA);
		queA.setAlpha((float) 0.5);
		queA.setOnClickListener(this);
		queB = (Button)this.findViewById(R.id.queB);
		queB.setAlpha((float) 0.5);
		queB.setOnClickListener(this);
		queC = (Button)this.findViewById(R.id.queC);
		queC.setAlpha((float) 0.5);
		queC.setOnClickListener(this);
		queD = (Button)this.findViewById(R.id.queD);
		queD.setAlpha((float) 0.5);
		queD.setOnClickListener(this);
		optA = (Button)this.findViewById(R.id.optA);
		optA.setAlpha((float) 0.5);
		optA.setOnClickListener(this);
		optB = (Button)this.findViewById(R.id.optB);
		optB.setAlpha((float) 0.5);
		optB.setOnClickListener(this);
		optC = (Button)this.findViewById(R.id.optC);
		optC.setAlpha((float) 0.5);
		optC.setOnClickListener(this);
		optD = (Button)this.findViewById(R.id.optD);
		optD.setAlpha((float) 0.5);
		optD.setOnClickListener(this);
		new Thread(new StartGame()).start();
	}
	
	public class StartGame implements Runnable{
		@Override
		public void run() {
			//TODO Auto-generated method stub
			getlinequestion getlq = new getlinequestion(LinesGameActivity.this);
			Map<String, String> map = new HashMap<String, String>();
			// ��MakeIntToString��������ת���ַ�����ѡ���Ӧ��Ŀ
			String str = MakeIntToString.getString(sqnumber);
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
	public void onClick(View v) {
		
		Map<String,String> node= new LinkedHashMap<String, String>();
		// TODO Auto-generated method stub
		switch (v.getId()){
		case R.id.queA:
			//
			questionflag = 1;
			break;
		case R.id.queB:
			//
			questionflag = 2;
			break;
		case R.id.queC:
			//
			questionflag = 3;
			break;
		case R.id.queD:
			//
			questionflag = 4;
			break;
		case R.id.optA:
			//
			if(questionflag == 1){
				node.put("a", "a");
			}else if(questionflag == 2){
				node.put("b", "a");
			}else if(questionflag == 3){
				node.put("c", "a");
			}else if(questionflag == 4){
				node.put("d", "a");
			}
			miList.add(node);
			questionflag = 0;
			if(miList.size() == 4){
				flag = new JudgeAnswer(LinesGameActivity.this).judgeitFourAns(miList, mainMap);
			}
			break;
		case R.id.optB:
			//
			if(questionflag == 1){
				node.put("a", "b");
			}else if(questionflag == 2){
				node.put("b", "b");
			}else if(questionflag == 3){
				node.put("c", "b");
			}else if(questionflag == 4){
				node.put("d", "b");
			}
			miList.add(node);
			questionflag = 0;
			if(miList.size() == 4){
				flag = new JudgeAnswer(LinesGameActivity.this).judgeitFourAns(miList, mainMap);
			}
			break;
		case R.id.optC:
			//
			if(questionflag == 1){
				node.put("a", "c");
			}else if(questionflag == 2){
				node.put("b", "c");
			}else if(questionflag == 3){
				node.put("c", "c");
			}else if(questionflag == 4){
				node.put("d", "c");
			}
			miList.add(node);
			questionflag = 0;
			if(miList.size() == 4){
				flag = new JudgeAnswer(LinesGameActivity.this).judgeitFourAns(miList, mainMap);
			}
			break;
		case R.id.optD:
			//
			if(questionflag == 1){
				node.put("a", "d");
			}else if(questionflag == 2){
				node.put("b", "d");
			}else if(questionflag == 3){
				node.put("c", "d");
			}else if(questionflag == 4){
				node.put("d", "d");
			}
			miList.add(node);
			questionflag = 0;
			if(miList.size() == 4){
				flag = new JudgeAnswer(LinesGameActivity.this).judgeitFourAns(miList, mainMap);
			}
			break;
		}
	}
}
