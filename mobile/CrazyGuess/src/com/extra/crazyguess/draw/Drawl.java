package com.extra.crazyguess.draw;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.extra.crazyguess.LinesGame2Activity;
import com.extra.crazyguess.MyApplication;
import com.extra.crazyguess.hop.Hop;
import com.extra.crazyguess.Tools.JudgeAnswer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class Drawl extends View{
	private MyApplication myApplication;
	
	public Drawl(Context context, AttributeSet attrs) {  
		super(context, attrs);
		// TODO Auto-generated constructor stub
		paint=new Paint(Paint.DITHER_FLAG);//创建一个画笔
		bitmap = Bitmap.createBitmap(800, 1024, Bitmap.Config.ARGB_8888); //设置位图的宽高
		canvas=new Canvas();
		canvas.setBitmap(bitmap);

		paint.setStyle(Style.STROKE);//设置非填充
		paint.setStrokeWidth(5);//笔宽5像素
		paint.setColor(Color.BLUE);//设置为蓝笔
		paint.setAntiAlias(true);//锯齿不显示
	}
	
	private Paint paint;//声明画笔
	private Canvas canvas;//画布
	private Bitmap bitmap;//位图

	private LinesGame2Activity linesGame2Activity;

	public Drawl(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		paint=new Paint(Paint.DITHER_FLAG);//创建一个画笔
		bitmap = Bitmap.createBitmap(720, 1280, Bitmap.Config.ARGB_8888); //设置位图的宽高
		canvas=new Canvas();
		canvas.setBitmap(bitmap);

		paint.setStyle(Style.STROKE);//设置非填充
		paint.setStrokeWidth(5);//笔宽5像素
		paint.setColor(Color.RED);//设置为红笔
		paint.setAntiAlias(true);//锯齿不显示
	}
	
	//画位图
	@Override
	protected void onDraw(Canvas canvas) {
		//super.onDraw(canvas);
		canvas.drawBitmap(bitmap,0,0,null);
	}
	
	//触摸事件
	public boolean onTouchEvent(MotionEvent event){
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			canvas.drawColor(Color.RED, android.graphics.PorterDuff.Mode.CLEAR);
			for (Hop hop : myApplication.getHops().values()) {
				int[] h = hop.getLine();
				canvas.drawLine(h[0], h[1], h[2], h[3], paint);// 画线
			}
			invalidate();
			
			@SuppressWarnings("unused")
			ArrayList<String> hopKeys = new ArrayList<String>(myApplication.getHops().keySet()); 
			
			if(myApplication.getHops().keySet().size()==4){
				canvas.drawColor(Color.RED, android.graphics.PorterDuff.Mode.CLEAR);
				//check answer
				Map<String,String> answers = myApplication.getAnwsers();
				Map<String, Hop> choicess = myApplication.getHops();
//				ArrayList<String> choicess = new ArrayList<String>(myApplication.getHops().keySet());
				List<Map<String,String>> judgeList = new ArrayList<Map<String,String>>();
				judgeList = new JudgeAnswer(myApplication).judgeAnswers(answers, choicess);
				//下一题
				linesGame2Activity.startNextGame(judgeList);
				//清除屏幕画线
				myApplication.initHops();
				canvas.drawColor(Color.RED, android.graphics.PorterDuff.Mode.CLEAR);
				invalidate();
			}
		}
		
		return true;
	}
	
	
	public void setApplication(MyApplication myApp) {
		this.myApplication = myApp;
	}

	public void setApplication(MyApplication myApp,
			LinesGame2Activity at) {
		this.myApplication = myApp;
		this.linesGame2Activity = at;
	}
}


