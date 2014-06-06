package com.extra.crazyguess.Tools;

public class MakeIntToString {

	public MakeIntToString() {
		// TODO Auto-generated constructor stub
	}
	
	public static String getString(int number){
		int sum=number;
		int len=0;
		while(sum!=0){
			sum/=10;
			len++;
		}
		if(len==1){
			return new String("000"+number);
		}else if(len==2){
			return new String("00"+number);
		}else if(len==3){
			return new String("0"+number);
		}else{
			return new String(""+number);
		}
	}
	
	public static int tranceNum2Score(int number){
		int singlescore = 0;
		if(number == 1){
			singlescore = 1;
		}else if(number == 2){
			singlescore = 3;
		}else if(number == 4){
			singlescore = 5;
		}
		return singlescore;
	}

}
