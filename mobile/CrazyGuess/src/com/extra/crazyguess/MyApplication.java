package com.extra.crazyguess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.extra.crazyguess.hop.Hop;

import android.app.Application;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.view.View;

public class MyApplication extends Application {
	private Map<String, Hop> hops = new HashMap<String, Hop>();
	private View fv;
	private View tv;
	
	private Map<String, String> anwsers = new HashMap<String, String>();
	
	//获取设备唯一标识
	public String getDeviceId(){
		TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		String DEVICE_ID = tm.getDeviceId(); 
		return DEVICE_ID;
	}
	
	public Map<String, Hop> getHops() {
		return hops;
	}
	
	public void addHop(Hop h){
		hops.put(h.getId(), h);
	}
	public void addHop(View fv, View tv){
		if(fv !=null && tv!=null){
			Hop h = new Hop(fv, tv);
			hops.put(h.getId(), h);
			this.fv = null;
			this.tv = null;
		}
	}
	
	public void clearHop(View v){
		ArrayList<String> hopKeys = new ArrayList<String>(hops.keySet());
		for(String s : hopKeys){
			String [] ids = s.split("_");
			if((v.getId()+"").equals(ids[0]) || (v.getId()+"").equals(ids[1])){
				hops.remove(s);
			}
		}
	}

	public View getFromView() {
		return fv;
	}

	public void setFromView(View fv) {
		this.clearHop(fv);
		this.fv = fv;
		addHop(fv, tv);
	}

	public View getToView() {
		return tv;
	}

	public void setToView(View tv) {
		this.clearHop(tv);
		this.tv = tv;
		addHop(fv, tv);
	}

	public Map<String, String> getAnwsers() {
		return anwsers;
	}

	public void setAnwsers(Map<String, String> anwsers) {
		this.anwsers = anwsers;
	};
	
	public void initHops(){
		this.hops.clear();
		this.fv=null;
		this.tv=null;
		this.anwsers=null;
	}
	
}
