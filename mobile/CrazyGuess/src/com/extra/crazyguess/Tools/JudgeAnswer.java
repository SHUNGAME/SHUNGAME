package com.extra.crazyguess.Tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.extra.crazyguess.hop.Hop;

import android.content.Context;
import android.util.Log;

public class JudgeAnswer {

	@SuppressWarnings("unused")
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
					System.out.print("$$$$$$$$$$$$$答案传递有误！");
				}
			}
		}
		return result;
	}

	public List<Map<String,String>> judgeitBlankAns(List<Map<String, String>> miList,
			Map<String, String> mainMap){
		int result = 0;
		String answer = "";
		String choice = "";
		List<Map<String,String>> returnList = new ArrayList<Map<String,String>>(); 
		for(int mi = 0; mi < miList.size(); mi++){
			Map<String,String> map = miList.get(mi);
			for(String key : map.keySet()){
				Map<String,String> node = new HashMap<String,String>();
				if("blank1".equals(key)){
					answer = mainMap.get("ansA");
					choice = map.get(key);
					if(answer.equals(choice)){
						result++;
					}else{
						node.put("blank1", answer);
						returnList.add(node);
					}
				}else if("blank2".equals(key)){
					answer = mainMap.get("ansB");
					choice = map.get(key);
					if(answer.equals(choice)){
						result++;
					}else{
						node.put("blank2", answer);
						returnList.add(node);
					}
				}else if("blank3".equals(key)){
					answer = mainMap.get("ansC");
					choice = map.get(key);
					if(answer.equals(choice)){
						result++;
					}else{
						node.put("blank3", answer);
						returnList.add(node);
					}
				}else if("blank4".equals(key)){
					answer = mainMap.get("ansD");
					choice = map.get(key);
					if(answer.equals(choice)){
						result++;
					}else{
						node.put("blank4", answer);
						returnList.add(node);
					}
				}else{
					Log.e("judgeBlankAnswer", "this key is undefined in blanks");
				}
			}
		}
		Map<String,String> remap = new HashMap<String,String>();
		remap.put("resultforstring", result+"");
		returnList.add(remap);
		return returnList;
	}
	
	public List<Map<String,String>> judgeAnswers (Map<String,String> answers, 
			Map<String,Hop> choicess){
		int result = 0;
		String answer = "";
		String choice = "";
		List<Map<String,String>> reList = new ArrayList<Map<String,String>>();
		ArrayList<String> hopKeys = new ArrayList<String>(choicess.keySet());
		for(String s : hopKeys){
//		for(String s : choicess){
			Map<String,String> node = new HashMap<String,String>();
			String [] ids = s.split("_");
			String oid = ids[0];
			if("2131361810".equals(oid) || "2131361831".equals(oid)){
				answer = changeString(answers.get("Aa"));
				choice = ids[1];
				if(answer.equals(choice)){
					result++;
				}else{
					node.put("Oa", answer);
					reList.add(node);
				}
			}else if("2131361808".equals(oid) || "2131361830".equals(oid)){
				answer = changeString(answers.get("Ab"));
				choice = ids[1];
				if(answer.equals(choice)){
					result++;
				}else{
					node.put("Ob", answer);
					reList.add(node);
				}
			}else if("2131361807".equals(oid) || "2131361829".equals(oid)){
				answer = changeString(answers.get("Ac"));
				choice = ids[1];
				if(answer.equals(choice)){
					result++;
				}else{
					node.put("Oc", answer);
					reList.add(node);
				}
			}else if("2131361813".equals(oid) || "2131361832".equals(oid)){
				answer = changeString(answers.get("Ad"));
				choice = ids[1];
				if(answer.equals(choice)){
					result++;
				}else{
					node.put("Od", answer);
					reList.add(node);
				}
			}else{
 				Log.e("JudgeAnser", "出现不明题干！");
				result = 0;
			}
		}
		Map<String,String> mimap = new HashMap<String,String>();
		mimap.put("resultstr", result+"");
		reList.add(mimap);
		return reList;
	}
	
	public String changeString(String s){
		String returnans = "";
		if("a".equals(s)){
			returnans = "2131361836";
//					"2131361809";
		}else if("b".equals(s)){
			returnans = "2131361834";
//					"2131361811";
		}else if("c".equals(s)){
			returnans = "2131361833";
//					"2131361814";
		}else if("d".equals(s)){
			returnans = "2131361835";
//					"2131361812";
		}else{
			Log.e("JudgeAnser", "出现不明答案！");
			returnans = "";
		}
		return returnans;
	}
}
