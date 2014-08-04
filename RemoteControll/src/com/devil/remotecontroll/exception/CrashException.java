package com.devil.remotecontroll.exception;

import java.lang.Thread.UncaughtExceptionHandler;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.devil.remotecontroll.R;
import com.devil.remotecontroll.RemoteMainActivity;
import com.devil.remotecontroll.manager.RemoteActivityManager;

public class CrashException implements UncaughtExceptionHandler {
	// 系统默认的UncaughtExceptionHandler处理类
	private UncaughtExceptionHandler mDefaultHandler;
	private static Context mContext;
	private static CrashException instance = new CrashException();

	private CrashException() {
	}

	/**
	 * 获取APP异常崩溃处理对象
	 * 
	 * @param context
	 * @return
	 */
	public static CrashException getInstance() {
		return instance;
	}

	public void init() {
		// 获取系统默认的UncaughtException处理器   
		this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		// 设置该CrashHandler为程序的默认处理器   
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
//			android.os.Process.killProcess(android.os.Process.myPid());
//			System.exit(0);
		}
	}

	/**
	 * 自定义异常处理:收集错误信息并发送错误报告
	 * 
	 * @param ex
	 * @return 处理异常返回true否则返回false
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return false;
		}
		ex.printStackTrace();
		mContext = RemoteActivityManager.getInstance().currentActivity();
//		Log.e("RemoteCrash", "e = "+ex.getLocalizedMessage());
//		Log.e("RemoteCrash", "e = "+ex.getStackTrace());
		final String crashReport = getCrashReport(mContext, ex);
//		Log.e("RemoteCrash", "crashReport = "+crashReport);
		Log.e("RemoteCrash", "马上进入sendAppCrashReport = ");
		new Thread(new Runnable() {

			@Override
			public void run() {
				Looper.prepare();
				sendAppCrashReport(mContext, crashReport);
				Looper.loop();
			}
		}).start();
		Log.e("RemoteCrash", "后面进入sendAppCrashReport = ");
		return true;
	}

	/**
	 * 获取app崩溃异常报告
	 * 
	 * @param context
	 * @param ex
	 * @return
	 */
	private String getCrashReport(Context context, Throwable ex) {
		PackageInfo packageInfo;
		try {
			packageInfo = context.getApplicationContext().getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			StringBuffer stringBuffer = new StringBuffer();

			stringBuffer.append("Version: " + packageInfo.versionName + "("
					+ packageInfo.versionCode + ")\n");

			stringBuffer.append("Android: " + Build.VERSION.RELEASE + "("
					+ Build.MODEL + ")\n");

			stringBuffer.append("Exception: " + ex.getMessage() + "\n");

			// 异常元素集合
			StackTraceElement[] elements = ex.getStackTrace();

			for (int i = 0; i < elements.length; i++) {
				stringBuffer.append(elements[i].toString() + "\n");
				Log.e("fuck", "e = "+elements[i].toString());
			}
			return stringBuffer.toString();
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 发送App错误异常报告
	 * 
	 * @param context
	 * @param crashReport
	 */
	public static void sendAppCrashReport(final Context context,
			final String crashReport) {
		Log.e("RemoteCrash", "sendAppCrashReport = "+crashReport);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle(R.string.app_error);
		builder.setMessage(R.string.app_error_message);
		builder.setPositiveButton(R.string.submit_report,
				new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						// 发送异常报告
						Intent intent = new Intent(Intent.ACTION_SEND);
						// intent.setType("text/plain");// 模拟器
						intent.setType("message/rfc822");// 真机
						// 发送邮件 Intent.EXTRA_CC抄送 Intent.EXTRA_BCC密送者
						intent.putExtra(Intent.EXTRA_EMAIL,
								new String[] { "469863752@qq.com" });
						// 标题
						intent.putExtra(Intent.EXTRA_SUBJECT,
								"远程遥控android客户端-错误报告");
						// 内容
						intent.putExtra(Intent.EXTRA_TEXT, crashReport);

						context.startActivity(Intent.createChooser(intent,
								"发送错误报告"));
						System.exit(0);

					}
				});

		builder.setNegativeButton(R.string.sure, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				System.exit(0);
			}
		});

		AlertDialog alertDialog = builder.create();
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				System.exit(0);
			}
		});
		alertDialog.show();

	}
}
