package com.extra.crazyguess.single;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;

public class ActivitiesCollection {
	private static List<Activity> activityList = new LinkedList<Activity>();
	private static ActivitiesCollection ac = new ActivitiesCollection();

	private ActivitiesCollection() {
	}

	public static ActivitiesCollection getInstance() {
		return ac;
	}

	// ���Activity��������
	public void addActivity(Activity activity) {
		if(!activityList.contains(activity)){
			activityList.add(activity);
		}
	}

	// ��������Activity��finish
	public void exit() {
		for (Activity activity : activityList) {
			if(activity == null){break;}
			activity.finish();
		}
		System.exit(0);
	}

}
