package com.extra.crazyguess;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.extra.crazyguess.Tools.JudgeAnswer;
import com.extra.crazyguess.Tools.MakeIntToString;
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
	
	private TextView questionView, scorestext; // ����״̬��Ϣ
	private Button queA, queB, queC, queD;//4�����
	private Button optA, optB, optC, optD;//4����ѡ
	private Button ansA, ansB, ansC, ansD;//4����
	private int qnumber = 1; // ��ǰ��Ŀ�����
	// ��mainMap���洢�����Ӧ����Ϣ
	private Map<String, String> mainMap = new HashMap<String, String>();
	private int flag = 0; //������������0�����0����1�����1����2�����2����3�����3����4�����4����
	private final static int CHANGE_QUESTION = 1; // �任��Ϸ������Ŀ�ı�ʶ��
	
	// ���̺߳�handler��������Ϣ
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg){
			switch (msg.what) {
			case CHANGE_QUESTION:
				mainMap = (Map<String, String>) msg.obj;
				questionView.setText("����������������Ķ�Ӧ��ϵ");
				queA.setText(mainMap.get("Qa"));
				queB.setText(mainMap.get("Qb"));
				queC.setText(mainMap.get("Qc"));
				queD.setText(mainMap.get("Qd"));
				optA.setText(mainMap.get("Oa"));
				optB.setText(mainMap.get("Ob"));
				optC.setText(mainMap.get("Oc"));
				optD.setText(mainMap.get("Od"));
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lines);
		scorestext = (TextView)this.findViewById(R.id.scorestext);
		questionView = (TextView) this.findViewById(R.id.questiontext);
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
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
