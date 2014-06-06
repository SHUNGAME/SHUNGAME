package com.extra.crazyguess;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.extra.crazyguess.Tools.JudgeAnswer;
import com.extra.crazyguess.Tools.MakeIntToString;
import com.extra.crazyguess.exchange.HttpTest;
import com.extra.crazyguess.getsqldatabase.getquestion;
import com.extra.crazyguess.getsqldatabase.getscoresinfo;
import com.extra.crazyguess.single.ActivitiesCollection;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.ProgressBar;
import android.widget.TextView;

public class GameActivity1 extends Activity implements OnClickListener {

	public GameActivity1() {
		// TODO Auto-generated constructor stub
	}

	private TextView showscors,stateprogressView, questionView; // ����״̬��Ϣ
	private Button back, aswA, aswB, aswC, aswD; // 4����ѡ�ť
	private ProgressBar timeprogress; // ʱ�������
	private int wr = 0; // ��������
	private int tr = 0; // ��Ե�����
	private int qnumber = 1; // ��ǰ��Ŀ�����
	private int statenum = 1; // ��ǰ����
	private final static int sum = 5; // �ܹ���Ҫ��Ե�����
//	private final static int wrsum = 3; // �ܹ��ɴ��Ĵ���
	private final static int quesum = 8; // ÿ����ս�ܹ�ȡ������Ŀ��
	private final static int LASTSTATE = 2; // ���չ���+1
	private final static int CHANGE_QUESTION = 1; // �任��Ϸ������Ŀ�ı�ʶ��
	private final static int SETPROGRESS = 2; // ��ʾ����ʱ��������ı�ʶ��
	private final static int RESTARTGAME = 3; // ���¿�ʼ��Ϸ�ı�ʶ��
	private static boolean OVERTIME = false; // �Ƿ��Ѿ���ʱ��ʶ��
	// ��mainMap���洢�����Ӧ����Ϣ
	private Map<String, String> mainMap = new HashMap<String, String>();
	private boolean flag = false; // �����Ƿ���
	private int progressBarValue = 0; // ��ʾʱ��������Ľ���
	private final static int TOTALPROGRESS = 30; // ����ʱ������������ֵ
	private Timer timer; // ����һ����ʱ��
	private Random random = new Random(); // ����һ��������������ȡ��Ŀ
	private int[] QuestionNum = new int[8]; // ÿһ����Ŀ�����к�
	
	private String userid = "";
	private String username = "";
	private String squesNum = "1";
	private String lquesNum = "1";
	private String module = "blank";
	private String bquesNum = "1"; //��������
	private int totalscores = 0;//��ǰ�ܵ÷�
//	private int challengelevel = 1;//

	// ���̺߳�handler��������Ϣ
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CHANGE_QUESTION:
				mainMap = (Map<String, String>) msg.obj;
//				stateView.setText("��" + statenum + "��");
				stateprogressView.setText(tr + "/" + sum + "\n" + "�Ѵ��" + wr);
				questionView.setText(qnumber + ":" + mainMap.get("questions"));
				aswA.setText("A." + mainMap.get("a"));
				aswB.setText("B." + mainMap.get("b"));
				aswC.setText("C." + mainMap.get("c"));
				aswD.setText("D." + mainMap.get("d"));
				showscors.setText(totalscores+"");
				break;
			case SETPROGRESS:
				int progress = (Integer) msg.obj;
				timeprogress.setProgress(progress);
				break;
			case RESTARTGAME:
				timer.cancel();
				OVERTIME = true; // ����Ϊ��ʱ
				new ShowTimeOverDialog().showdialog();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//��ȡ�豸id
		TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		userid = tm.getDeviceId(); //�豸id
		new dataDevice().dealscores();
		if(statenum > LASTSTATE){
			setContentView(R.layout.completed);
			back = (Button)this.findViewById(R.id.back);
			back.setOnClickListener(this);
		}else{
			setContentView(R.layout.ggg);
			stateprogressView = (TextView) this.findViewById(R.id.stateprogress);
			questionView = (TextView) this.findViewById(R.id.questiontext);
			showscors = (TextView)this.findViewById(R.id.showscors);
			back = (Button)this.findViewById(R.id.back);
			back.setOnClickListener(this);
			aswA = (Button) this.findViewById(R.id.aswA);
			aswA.setAlpha((float) 0.5);
			aswA.setOnClickListener(this);
			aswB = (Button) this.findViewById(R.id.aswB);
			aswB.setAlpha((float) 0.5);
			aswB.setOnClickListener(this);
			aswC = (Button) this.findViewById(R.id.aswC);
			aswC.setAlpha((float) 0.5);
			aswC.setOnClickListener(this);
			aswD = (Button) this.findViewById(R.id.aswD);
			aswD.setAlpha((float) 0.5);
			aswD.setOnClickListener(this);
			timeprogress = (ProgressBar) this.findViewById(R.id.progressBar1);
			timeprogress.setMax(TOTALPROGRESS);
			InitialQNum(); // ��ʼ�������������
			
			new Thread(new StartGame()).start();
			timer = new Timer(true);
			timer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (progressBarValue == TOTALPROGRESS) {
						// ������Ϸʱ�䣬�����Ի�����ʾ���
						handler.sendEmptyMessage(RESTARTGAME);
					} else {
						// ����Ϣ���͸�handler�����½�����
						Message message = Message.obtain();
						message.obj = progressBarValue;
						message.what = SETPROGRESS;
						handler.sendMessage(message);
						// ʱ���������
						progressBarValue++;
					}
				}
			}, 0, 1000);
		}
		//��ӵ�ǰactivity������
		ActivitiesCollection.getInstance().addActivity(this);
	}

	// ��ʼ��QuestionNum����,�����ȡ
	private void InitialQNum() {
		int count = 0;
		while (count < 8) {
			boolean flag1 = true; // ��־�Ƿ��ظ�
			int cur = Math.abs(random.nextInt() % 8) + 1;
			for (int i = 0; i < count; i++) {
				if (cur == QuestionNum[i]) {
					flag1 = false;
					break;
				}
			}
			if (flag1) {
				QuestionNum[count] = cur;
				count++;
			}
		}
	}

	public class StartGame implements Runnable {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			getquestion getq = new getquestion(GameActivity1.this);
			Map<String, String> map = new HashMap<String, String>();
			// ��MakeIntToString��������ת���ַ�����ѡ���Ӧ��Ŀ
			String str = MakeIntToString.getString(QuestionNum[qnumber - 1]
					+ (statenum - 1) * 8);
			String str1 = String.valueOf(statenum);
			String[] strs = new String[] { str, str1 };
			map = getq.getquestionMap(strs);
			// ��message�������̴߳�����Ϣ������
			Message message = Message.obtain();
			message.obj = map; // ��map��Ϣ����message��
			message.what = CHANGE_QUESTION; // �趨message�ı�ʾ��
			handler.sendMessage(message); // �����߳��е�handler������Ϣ
		}

	}

	// ��Ϸ������һ��
	private void GoToNextState() {
		if (OVERTIME) {
			return;
		}
		timer.cancel(); // �ر�ʱ��
		statenum++; // ��������
		totalscores = totalscores + 50;//��ý�������
		qnumber = 1; // �������Ϊ1
		wr = 0; // �������
		tr = 0; // �������
		InitialQNum(); // ���³�ȡ�������Ϊ��Ŀ����
		progressBarValue = 0; // ��ʱ���������Ϊ0
//		Toast.makeText(GameActivity1.this, "��ϲ������" + statenum + "�أ�", 0).show();
		
		timer.cancel();
		AlertDialog.Builder builder = new AlertDialog.Builder(
				GameActivity1.this);
		builder.setTitle("��ʾ");
		builder.setMessage("��ս�ɹ�����û���50��");
		builder.setPositiveButton("����",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Intent intent1 = new Intent(GameActivity1.this, ChooseModeActivity.class);
						startActivity(intent1);
					}
				});
		builder.setNegativeButton("������ս",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						new Thread(new StartGame()).start();
						timer = null;
						timer = new Timer();
						timer.schedule(new TimerTask() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								if (progressBarValue == TOTALPROGRESS) {
									// ������Ϸʱ�䣬�����Ի�����ʾ���
									handler.sendEmptyMessage(RESTARTGAME);
								} else {
									// ����Ϣ���͸�handler�����½�����
									Message message = Message.obtain();
									message.obj = progressBarValue;
									message.what = SETPROGRESS;
									handler.sendMessage(message);
									// ʱ���������
									progressBarValue++;
								}
							}
						}, 0, 1000);
					}
				});
		builder.setCancelable(false);
		Dialog dialog = builder.create();
		dialog.show();
	}

	// ���¿�ʼ��Ϸ
	private class RestartGame {
		public RestartGame() {

		}

		public void restart() {
//			statenum = 1;
			qnumber = 1; // �������Ϊ1
			wr = 0;
			tr = 0;
			progressBarValue = 0;
			InitialQNum();
			timer = null;
			timer = new Timer(true);
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (progressBarValue == TOTALPROGRESS) {
						// ������Ϸʱ�䣬�����Ի�����ʾ���
						handler.sendEmptyMessage(RESTARTGAME);
					} else {
						// ����Ϣ���͸�handler�����½�����
						Message message = Message.obtain();
						message.obj = progressBarValue;
						message.what = SETPROGRESS;
						handler.sendMessage(message);
						// ʱ���������
						progressBarValue++;
					}
				}
			}, 0, 1000);
			new Thread(new StartGame()).start();
		}
	}

	// ��Ϸ��ʱ�����Ի���
	public class ShowTimeOverDialog {
		public ShowTimeOverDialog() {

		}

		public void showdialog() {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					GameActivity1.this);
			builder.setTitle("��ʾ");
			builder.setMessage("�Բ�����սʧ�ܣ�");
			builder.setPositiveButton("������ս",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							OVERTIME = false; // ����Ϊ����ʱ
							new RestartGame().restart();
						}
					});
			builder.setNegativeButton("����",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							GameActivity1.this.finish();
						}
					});
			builder.setCancelable(false);
			Dialog dialog = builder.create();
			dialog.show();

		}
	}

	// ��Ϸʧ��ʱ�����ĶԻ���
	public class ShowGameOverDialog {

		public ShowGameOverDialog() {

		}

		public void showdialog() {
			timer.cancel();
			AlertDialog.Builder builder = new AlertDialog.Builder(
					GameActivity1.this);
			builder.setTitle("��ʾ");
			builder.setMessage("�Բ�����սʧ���ˣ�");
			builder.setPositiveButton("���´���",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							new RestartGame().restart();
						}
					});
			builder.setNegativeButton("����",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							GameActivity1.this.finish();
						}
					});
			builder.setCancelable(false);
			Dialog dialog = builder.create();
			dialog.show();
		}
	}

	private void GoOverGame() {
		if (OVERTIME) {
			return;
		}
		timer.cancel();
		AlertDialog.Builder builder = new AlertDialog.Builder(
				GameActivity1.this);
		builder.setTitle("��ʾ");
		builder.setMessage("��ϲ�����ȫ����ս!");
		builder.setPositiveButton("����",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						GameActivity1.this.finish();
					}
				});
		builder.setCancelable(false);
		Dialog dialog = builder.create();
		dialog.show();
	}

	@Override
	public void onBackPressed() { // �����ؼ�ʱ�����¼�
		// TODO Auto-generated method stub
//		super.onBackPressed();
		timer.cancel(); // ��ʱ��ȡ�����ÿ�
		timer = null;
//		GameActivity1.this.finish();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back:
			Intent intent = new Intent(GameActivity1.this, ChooseModeActivity.class);
			startActivity(intent);
			break;
		case R.id.aswA:
			// ���ص�ǰ�Ƿ���
			flag = new JudgeAnswer(GameActivity1.this).judgeit("a", mainMap);
			if (flag) { // �����ԣ���Ӧ�������иı�
				tr++;
				qnumber++;
				if (tr == sum) {
					if (statenum == LASTSTATE) {
						GoOverGame();
					} else {
						GoToNextState();
						new dataDevice().updatescores();//���ݿ����
					}
				} else {
					new Thread(new StartGame()).start();
				}
			} else {
				wr++;
				qnumber++;
				if (qnumber == quesum) { // ��������ﵽÿ��ȡ���������ޣ��ж���սʧ��
					new ShowGameOverDialog().showdialog();
				} else { // ���������Ŀ
					new Thread(new StartGame()).start();
				}
			}
			break;
		case R.id.aswB:
			flag = new JudgeAnswer(GameActivity1.this).judgeit("b", mainMap);
			if (flag) {
				tr++;
				qnumber++;
				if (tr == sum) {
					if (statenum == LASTSTATE) {
						GoOverGame();
					} else {
						GoToNextState();
						new dataDevice().updatescores();//���ݿ����
					}
				} else {
					new Thread(new StartGame()).start();
				}
			} else {
				wr++;
				qnumber++;
				if (qnumber == quesum) {
					new ShowGameOverDialog().showdialog();
				} else {
					new Thread(new StartGame()).start();
				}
			}
			break;
		case R.id.aswC:
			flag = new JudgeAnswer(GameActivity1.this).judgeit("c", mainMap);
			if (flag) {
				tr++;
				qnumber++;
				if (tr == sum) {
					if (statenum == LASTSTATE) {
						GoOverGame();
					} else {
						GoToNextState();
						new dataDevice().updatescores();//���ݿ����
					}
				} else {
					new Thread(new StartGame()).start();
				}
			} else {
				wr++;
				qnumber++;
				if (qnumber == quesum) {
					new ShowGameOverDialog().showdialog();
				} else {
					new Thread(new StartGame()).start();
				}
			}
			break;
		case R.id.aswD:
			flag = new JudgeAnswer(GameActivity1.this).judgeit("d", mainMap);
			if (flag) {
				tr++;
				qnumber++;
				if (tr == sum) {
					if (statenum == LASTSTATE) {
						GoOverGame();
					} else {
						GoToNextState();
						new dataDevice().updatescores();//���ݿ����
					}
				} else {
					new Thread(new StartGame()).start();
				}
			} else {
				wr++;
				qnumber++;
				if (qnumber == quesum) {
					new ShowGameOverDialog().showdialog();
				} else {
					new Thread(new StartGame()).start();
				}
			}
			break;
		}
	}
	
	public class dataDevice{
		
		public dataDevice(){
			
		}
		
		public void dealscores (){
			String [] cstrs = new String[] {userid};
			//
			getscoresinfo getsi = new getscoresinfo(GameActivity1.this);
			Map<String, String> scoresmap = getsi.getscoresinfoMap(cstrs);
			if(scoresmap.size() == 0){
				
			}else{
				//��������������Ϣ
				String ssss = scoresmap.get("scores");
				totalscores = Integer.parseInt(ssss);
				String llll = scoresmap.get("challengelevel");
				statenum = Integer.parseInt(llll);
				//����������Ϣ
				bquesNum = scoresmap.get("blaquenum");
				username = scoresmap.get("username");
				squesNum = scoresmap.get("selquenum");
				lquesNum = scoresmap.get("linequenum");
//				module = scoresmap.get("module");
			}
		}
		
		@SuppressLint("SimpleDateFormat")
		public void updatescores(){
			//��ȡ��ǰϵͳʱ��
			SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String currentdate = sDateFormat.format(new java.util.Date());
			//
			getscoresinfo getsi = new getscoresinfo(GameActivity1.this);
			//��ȡ���շ��������
			String newscores = totalscores + "";
			String newbqn = statenum + "";
			String [] args = new String[]{userid, currentdate};
			Boolean isExist = getsi.getScoreForCurrent(args);//�жϸ��û�����ļ�¼�Ƿ���ڣ����ھ͸��£������ھͲ���
			if(isExist){
				args = new String[]{newscores, newbqn, module, userid, currentdate};
				getsi.updateScoreInfoForBlankquestion(args);
			}else{
				args = new String[]{userid,username,currentdate,newscores,squesNum,bquesNum,lquesNum,
						module,newbqn};
				getsi.insertScoreInfo(args);
			}
		}
	}
}
