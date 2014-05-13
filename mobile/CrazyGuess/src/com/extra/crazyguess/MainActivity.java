package com.extra.crazyguess;

import com.extra.crazyguess.MusicTools.Mediaplayer;
import com.extra.crazyguess.MusicTools.SoundPlayer;

import android.media.SoundPool;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener {

	private Button button;
	private Button button2;
	private Button button3;
	private Button button4;

	private CharSequence items[] = { "开始游戏", "音效设置", "结束游戏", "排行榜"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setTitle("欢迎您！");
		SoundPlayer.init(MainActivity.this);
		Mediaplayer.init(MainActivity.this);
		if (Mediaplayer.getplayflag()) {
			Mediaplayer.PlayBackgroundMusic();
		}
		button = (Button) this.findViewById(R.id.button1);
		button.setOnClickListener(this);
		button2 = (Button) this.findViewById(R.id.button2);
		button2.setOnClickListener(this);
		button3 = (Button) this.findViewById(R.id.button3);
		button3.setOnClickListener(this);
		button4 = (Button) this.findViewById(R.id.button4);
		button4.setOnClickListener(this);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		SoundPlayer.Releasesoundplayer();// 退出Activity时释放soundpool中的资源
		Mediaplayer.ReleaseMediaplayer();// 退出Activity时释放mediaplayer中的资源
		finish();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.action_settings1:
			onClick(button);
			break;
		case R.id.action_settings2:
			onClick(button2);
			break;
		case R.id.action_settings3:
			onClick(button3);
			break;
		case R.id.action_settings4:
			onClick(button4);
			break;
		}
		
		return super.onMenuItemSelected(featureId, item);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button1:
			// 播放音效
			SoundPlayer.playsound(R.raw.gang2);
			Intent intent1=new Intent(MainActivity.this,GameActivity1.class);
			startActivity(intent1);    //进入游戏界面
			// 设置Activity之间的切换效果
			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
			break;
		case R.id.button2:
			Intent intent2 = new Intent(MainActivity.this, VolumControl.class);
			startActivity(intent2);
			// 设置Activity之间的切换效果
			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
			break;
		case R.id.button3:
			//用对话框的形式提示确定要不要退出游戏
			AlertDialog.Builder builder=new AlertDialog.Builder(this);   
			builder.setTitle("提示");
			builder.setMessage("你确定要退出游戏么？");
			builder.setPositiveButton("确定", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					onBackPressed();// 调用按返回键时触发的事件处理
				}
			});
			builder.setNegativeButton("取消", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					return;
				}
			});
			builder.setCancelable(false);
			AlertDialog dialog=builder.create();
			dialog.show();
			break;
		case R.id.button4:
			Intent intent4 = new Intent(MainActivity.this, VolumControl.class);
			startActivity(intent4);
			// 设置Activity之间的切换效果
			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
			break;
		}
	}

}
