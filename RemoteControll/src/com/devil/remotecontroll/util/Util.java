package com.devil.remotecontroll.util;

import java.util.List;
import java.util.Properties;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;
import android.util.Property;

public class Util {
	//IP地址是否合法
	private static final String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
	            + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
	            + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
	            + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
	public static boolean isTopActiviy(String className, Context context)
    {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(Integer.MAX_VALUE);
        String cmpNameTemp = null;
        if (null != runningTaskInfos)
        {
            ComponentName componentName =  runningTaskInfos.get(0).topActivity;
            cmpNameTemp = componentName.getShortClassName();
        }
        if (null == cmpNameTemp)
        {
            return false;
        }
        return cmpNameTemp.equals(className);
    }
	public static boolean checkIp(String ip){
        if (ip.matches(regex)) {
            // 返回判断信息
            return true;
        } else {
            // 返回判断信息
            return false;
        }
	}
}
