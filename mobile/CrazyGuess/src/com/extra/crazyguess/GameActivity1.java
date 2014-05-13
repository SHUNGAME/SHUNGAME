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

public class GameActivity1 extends Activity implements OnClickListener {

	public GameActivity1() {
		// TODO Auto-generated constructor stub
	}

	private TextView stateView, stateprogressView, questionView; // 各种状态信息
	private Button aswA, aswB, aswC, aswD; // 4个答案选项按钮
	private ProgressBar timeprogress; // 时间进度条
	private int wr = 0; // 答错的题数
	private int tr = 0; // 答对的题数
	private int qnumber = 1; // 当前题目的题号
	private int statenum = 1; // 当前关数
	private final static int sum = 5; // 总共需要答对的题数
	private final static int wrsum = 3; // 总共可答错的次数
	private final static int LASTSTATE = 2; // 最终关数
	private final static int CHANGE_QUESTION = 1; // 变换游戏界面题目的标识符
	private final static int SETPROGRESS = 2; // 表示设置时间进度条的标识符
	private final static int RESTARTGAME = 3; // 重新开始游戏的标识符
	private static boolean OVERTIME = false; // 是否已经超时标识符
	// 用mainMap来存储该题对应的信息
	private Map<String, String> mainMap = new HashMap<String, String>();
	private boolean flag = false; // 此题是否答对
	private int progressBarValue = 0; // 表示时间进度条的进度
	private final static int TOTALPROGRESS = 30; // 设置时间进度条的最大值
	private Timer timer; // 设置一个定时器
	private Random random = new Random(); // 设置一个随机数来随机抽取题目
	private int[] QuestionNum = new int[8]; // 每一关题目的序列号

	// 用线程和handler来处理消息
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CHANGE_QUESTION:
				mainMap = (Map<String, String>) msg.obj;
				stateView.setText("第" + statenum + "关");
				stateprogressView.setText(tr + "/" + sum + "\n" + wr + "/"
						+ wrsum);
				questionView.setText(qnumber + ":" + mainMap.get("questions"));
				aswA.setText("A." + mainMap.get("a"));
				aswB.setText("B." + mainMap.get("b"));
				aswC.setText("C." + mainMap.get("c"));
				aswD.setText("D." + mainMap.get("d"));
				break;
			case SETPROGRESS:
				int progress = (Integer) msg.obj;
				timeprogress.setProgress(progress);
				break;
			case RESTARTGAME:
				timer.cancel();
				OVERTIME = true; // 设置为超时
				new ShowTimeOverDialog().showdialog();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ggg);
		stateView = (TextView) this.findViewById(R.id.statetext);
		stateprogressView = (TextView) this.findViewById(R.id.stateprogress);
		questionView = (TextView) this.findViewById(R.id.questiontext);
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
		InitialQNum(); // 初始化题号序列数组
		new Thread(new StartGame()).start();
		timer = new Timer(true);
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (progressBarValue == TOTALPROGRESS) {
					// 超出游戏时间，弹出对话框提示玩家
//					handler.sendEmptyMessage(RESTARTGAME);
				} else {
					// 将信息传送给handler来更新进度条
					Message message = Message.obtain();
					message.obj = progressBarValue;
					message.what = SETPROGRESS;
					handler.sendMessage(message);
					// 时间进度自增
					progressBarValue++;
				}
			}
		}, 0, 1000);
	}

	// 初始化QuestionNum数组,随机抽取
	private void InitialQNum() {
		int count = 0;
		while (count < 8) {
			boolean flag1 = true; // 标志是否重复
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
			// 用MakeIntToString工具类来转换字符，并选择对应题目
			String str = MakeIntToString.getString(QuestionNum[qnumber - 1]
					+ (statenum - 1) * 8);
			String str1 = String.valueOf(statenum);
			String[] strs = new String[] { str, str1 };
			map = getq.getquestionMap(strs);
			// 用message来向主线程传递信息并处理
			Message message = Message.obtain();
			message.obj = map; // 将map信息放入message中
			message.what = CHANGE_QUESTION; // 设定message的标示符
			handler.sendMessage(message); // 向主线程中的handler发送信息
		}

	}

	// 游戏进入下一关
	private void GoToNextState() {
		if (OVERTIME) {
			return;
		}
		timer.cancel(); // 关闭时钟
		statenum++; // 关数自增
		qnumber = 1; // 题号重置为1
		wr = 0; // 答错重置
		tr = 0; // 答对重置
		InitialQNum(); // 重新抽取随机数组为题目序列
		progressBarValue = 0; // 将时间进度重置为0
		Toast.makeText(GameActivity1.this, "恭喜你进入第" + statenum + "关！", 0)
				.show();
		new Thread(new StartGame()).start();
		timer = null;
		timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (progressBarValue == TOTALPROGRESS) {
					// 超出游戏时间，弹出对话框提示玩家
//					handler.sendEmptyMessage(RESTARTGAME);
				} else {
					// 将信息传送给handler来更新进度条
					Message message = Message.obtain();
					message.obj = progressBarValue;
					message.what = SETPROGRESS;
					handler.sendMessage(message);
					// 时间进度自增
					progressBarValue++;
				}
			}
		}, 0, 1000);
	}

	// 重新开始游戏
	private class RestartGame {
		public RestartGame() {

		}

		public void restart() {
			statenum = 1;
			qnumber = 1; // 重置题号为1
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
						// 超出游戏时间，弹出对话框提示玩家
//						handler.sendEmptyMessage(RESTARTGAME);
					} else {
						// 将信息传送给handler来更新进度条
						Message message = Message.obtain();
						message.obj = progressBarValue;
						message.what = SETPROGRESS;
						handler.sendMessage(message);
						// 时间进度自增
						progressBarValue++;
					}
				}
			}, 0, 1000);
			new Thread(new StartGame()).start();
		}
	}

	// 游戏超时弹出对话框
	public class ShowTimeOverDialog {
		public ShowTimeOverDialog() {

		}

		public void showdialog() {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					GameActivity1.this);
			builder.setTitle("提示");
			builder.setMessage("对不起，你的智商太低，没有在规定时间内完成答题！");
			builder.setPositiveButton("重新开始",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							OVERTIME = false; // 设置为不超时
							new RestartGame().restart();
						}
					});
			builder.setNegativeButton("主界面",
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

	// 游戏失败时弹出的对话框
	public class ShowGameOverDialog {

		public ShowGameOverDialog() {

		}

		public void showdialog() {
			timer.cancel();
			AlertDialog.Builder builder = new AlertDialog.Builder(
					GameActivity1.this);
			builder.setTitle("提示");
			builder.setMessage("对不起，愚蠢的人类，你闯关失败了！");
			builder.setPositiveButton("重新闯关",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							new RestartGame().restart();
						}
					});
			builder.setNegativeButton("主界面",
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
		builder.setTitle("提示");
		builder.setMessage("恭喜您通关！！~您的智商真是高!");
		builder.setPositiveButton("谦让谦让",
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
	public void onBackPressed() { // 按返回键时触发事件
		// TODO Auto-generated method stub
		super.onBackPressed();
		timer.cancel(); // 将时钟取消并置空
		timer = null;
		GameActivity1.this.finish();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.aswA:
			// 返回当前是否答对
			flag = new JudgeAnswer(GameActivity1.this).judgeit("a", mainMap);
			if (flag) { // 如果答对，对应参数进行改变
				tr++;
				qnumber++;
				if (tr == sum) {
					if (statenum == LASTSTATE) {
						GoOverGame();
					} else {
						GoToNextState();
					}
				} else {
					new Thread(new StartGame()).start();
				}
			} else {
				wr++;
				qnumber++;
				if (wr == wrsum) { // 当错误题量达到上限，弹出游戏结束对话框
					new ShowGameOverDialog().showdialog();
				} else { // 否则更换题目
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
					}
				} else {
					new Thread(new StartGame()).start();
				}
			} else {
				wr++;
				qnumber++;
				if (wr == wrsum) {
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
					}
				} else {
					new Thread(new StartGame()).start();
				}
			} else {
				wr++;
				qnumber++;
				if (wr == wrsum) {
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
					}
				} else {
					new Thread(new StartGame()).start();
				}
			} else {
				wr++;
				qnumber++;
				if (wr == wrsum) {
					new ShowGameOverDialog().showdialog();
				} else {
					new Thread(new StartGame()).start();
				}
			}
			break;
		}
	}
}
