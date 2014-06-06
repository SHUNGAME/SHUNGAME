package com.extra.crazyguess;

import com.extra.crazyguess.single.ActivitiesCollection;
import com.extra.crazyguess.sqlitehelper.DatabaseOperate;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class ChooseModeActivity extends Activity implements OnClickListener{
	
	public ChooseModeActivity(){
		// TODO Auto-generated method stub
	}
	
	private Button back, selectnormal, selectchallenge;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choosemode);
		//创建表格，创建数据库
		DatabaseOperate dbo = new DatabaseOperate(ChooseModeActivity.this);
		dbo.createDatabase(ChooseModeActivity.this);
		//
		back = (Button)this.findViewById(R.id.back);
		back.setOnClickListener(this);
		selectnormal = (Button)this.findViewById(R.id.selectnormal);
		selectnormal.setOnClickListener(this);
		selectchallenge = (Button)this.findViewById(R.id.selectchallenge);
		selectchallenge.setOnClickListener(this);
		ActivitiesCollection.getInstance().addActivity(this);
	}
	
	@SuppressLint("CommitPrefEdits")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back:
			Intent intent3 = new Intent(ChooseModeActivity.this, MainActivity.class);
			startActivity(intent3);
			break;
		case R.id.selectnormal:
			//activity跳转
			Intent intent1 = new Intent(ChooseModeActivity.this, BlankGameActivity.class);
			startActivity(intent1);
			break;
		case R.id.selectchallenge:
			Intent intent2 = new Intent(ChooseModeActivity.this, GameActivity1.class);
			startActivity(intent2);
			break;
		}
	}
	
	@Override
	public void onBackPressed(){
//		super.onBackPressed();
		Toast.makeText(this, "模式选择按返回键", Toast.LENGTH_LONG).show();
//		Intent intent = new Intent(ChooseModeActivity.this, MainActivity.class);
//		startActivity(intent);
	}

}
