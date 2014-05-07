package com.example.pingping_game1.Tools;

import java.util.Map;

import android.R.integer;
import android.content.Context;

public class JudgeAnswer {

	private Context context;
	public JudgeAnswer(Context context) {
		// TODO Auto-generated constructor stub
	}


	public boolean judgeit(String answer,Map<String, String> map){
		boolean flag=false;
		String Trueanswer=map.get("answer");
		if(answer.equals(Trueanswer)){
			flag=true;
		}
		return flag;
	}
}
