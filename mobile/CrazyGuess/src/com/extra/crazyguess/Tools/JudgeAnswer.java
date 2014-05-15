package com.extra.crazyguess.Tools;

import java.util.List;
import java.util.Map;

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

	public int judgeitFourAns(List<Map<String, String>> miList,
			Map<String, String> mainMap) {
		// TODO Auto-generated method stub
		int result = 0;
		String answer = "";
		String choice = "";
		for(int mi = 0; mi < miList.size(); mi++){
			Map<String,String> map = miList.get(mi);
			for(String key : map.keySet()){
				if("a".equals(key)){
					answer = mainMap.get("Aa");
					choice = map.get(key);
					if(answer.equals(choice)){
						result++;
					}
				}else if("b".equals(key)){
					answer = mainMap.get("Ab");
					choice = map.get(key);
					if(answer.equals(choice)){
						result++;
					}
				}else if("c".equals(key)){
					answer = mainMap.get("Ac");
					choice = map.get(key);
					if(answer.equals(choice)){
						result++;
					}
				}else if("d".equals(key)){
					answer = mainMap.get("Ad");
					choice = map.get(key);
					if(answer.equals(choice)){
						result++;
					}
				}else{
					System.out.print("$$$$$$$$$$$$$´ð°¸´«µÝÓÐÎó£¡");
				}
			}
		}
		return result;
	}
}
