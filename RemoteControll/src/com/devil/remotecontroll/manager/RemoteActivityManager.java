package com.devil.remotecontroll.manager;

import java.util.Stack;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;

public class RemoteActivityManager {
	// activity的堆栈
	private static Stack<Activity> activityStack;
	private static RemoteActivityManager instance;
	private RemoteActivityManager(){
		
	}
	public static RemoteActivityManager getInstance() {
		if(instance==null){
			synchronized (RemoteActivityManager.class) {
				instance = new RemoteActivityManager();
				activityStack = new Stack<Activity>();
			}
		}
		return instance;
	}
	/**
	 * 添加activity到堆栈
	 * 
	 * @param activity
	 */
	public void addActivity(Activity activity) {

		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);
	}

	/**
	 * 获取当前activity(堆栈中最后一个压入的activity)
	 * 
	 * @return
	 */
	public Activity currentActivity() {
		Activity activity = activityStack.lastElement();
		return activity;
	}

	/**
	 * 结束当前的activity(堆栈中最后一个压入的activity)
	 */
	public void finishActivity() {
		Activity activity = activityStack.lastElement();

		finishActivity(activity);
	}

	/**
	 * 结束指定的activity
	 */
	public void finishActivity(Activity activity) {

		if (activity != null) {
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	/**
	 * 结束所有的activity
	 */
	public void finishAllActivity() {

		for (int i = 0, size = activityStack.size(); i < size; i++) {
			if (activityStack.get(i) != null) {
				activityStack.get(i).finish();
			}
		}

		activityStack.clear();

	}

	/**
	 * 退出应用程序
	 * 
	 * @param context
	 */
	public void AppExit(Context context) {

		finishAllActivity();

		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);

		// // 2.2之前的rom就用restartPackage之后的就用killBackgroundProcesses
		if (Build.VERSION.SDK_INT>Build.VERSION_CODES.FROYO) {
			activityManager.killBackgroundProcesses(context.getPackageName());
		} else {
			activityManager.restartPackage(context.getPackageName());
		}

		System.exit(0);
	}
}
