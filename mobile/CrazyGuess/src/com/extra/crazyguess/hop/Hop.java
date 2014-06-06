package com.extra.crazyguess.hop;

import android.view.View;

public class Hop {
	private View fromView;
	private View toView;
	private String id;
	
	public Hop(View f, View t){
		this.fromView = f;
		this.toView = t;
		this.id = f.getId()+"_"+t.getId();
	};
	
	public int[] getLine(){
		int fx = fromView.getRight();
		int fy = (fromView.getTop()+fromView.getBottom())/2;
		int tx = toView.getLeft();
		int ty = (toView.getTop()+toView.getBottom())/2;
		int[] line = {fx, fy, tx, ty};
		return line; 
	}
	
	public String getId(){
		return this.id;
	}
}
