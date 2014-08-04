package com.devil.remotecontroll;

import java.util.Timer;
import java.util.TimerTask;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.devil.remotecontroll.manager.RemoteActivityManager;
import com.devil.remotecontroll.net.SocketClientManager;

/**
 * 关机
 * @author 20082755
 *
 */
public class ShutDownActivity extends BaseActivity {

	private TextView powerText;

	private ImageView powerImage;

	// 是否关机
	private boolean isPowerOff = true;

	// 倒计时的秒数
	private int recLen = 6;

	private Timer timer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pc_power_off_activity);
		initView();
	}

	/**
	 * 初始化视图
	 */
	private void initView() {

		powerText = (TextView) findViewById(R.id.power_text);

		powerImage = (ImageView) findViewById(R.id.power_image);


		powerImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isPowerOff) {
					powerImage.setImageResource(R.drawable.pc_power_off_canel);

					isPowerOff = false;

					timer = new Timer();

					timer.schedule(new TimerTask() {
						@Override
						public void run() {

							runOnUiThread(new Runnable() { // UI thread
								@Override
								public void run() {
									recLen--;
									powerText.setText(recLen
											+ "秒后将关闭电脑! 取消请再次点击按钮!");
									if (recLen == 0) {
										timer.cancel();

										timer = null;

										powerOffPc();
									}
								}
							});
						}
					}, 0, 1000);
				} else {
					powerImage.setImageResource(R.drawable.pc_power_off);

					isPowerOff = true;

					powerText.setText("");

					timer.cancel();

					timer = null;

					recLen = 6;
				}
			}
		});

	}

	/**
	 * 关闭计算机
	 */
	public void powerOffPc() {
		new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				Boolean isClose = false;
//				try {
					isClose = SocketClientManager.PowerOffPc(BaseApplication.getInstance().getIpAdress());

//				} catch (AppException e) {
//					return false;
//				}

				return isClose;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);

				if (result) {
					System.exit(0);
				}
			}
		}.execute();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		RemoteActivityManager.getInstance().finishActivity();
	}
}
