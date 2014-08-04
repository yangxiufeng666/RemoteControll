package com.devil.remotecontroll;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;

import com.devil.remotecontroll.manager.RemoteActivityManager;
import com.devil.remotecontroll.thread.MouseThread;
import com.devil.remotecontroll.util.Constant;
import com.devil.remotecontroll.view.PointerView;

public class MouseControllActivity extends BaseActivity implements OnTouchListener{

	private TextView mouseControl;

	// 初始值的x，y坐标
	private float initX, initY;

	// 移动后的x，y坐标
	private float disX, disY;

	// 抬起的x，y坐标
	private float upX, upY;
	
	private PointerView pointerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.control_mouse_activity);
		initView();
	}

	/**
	 * 初始化视图
	 */
	private void initView() {

		mouseControl = (TextView) findViewById(R.id.mouse_control);

		mouseControl.setOnTouchListener(this);
		pointerView = (PointerView)findViewById(R.id.ball);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		RemoteActivityManager.getInstance().finishActivity();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		// 按下动作
		case MotionEvent.ACTION_DOWN:
			initX = event.getX();
			initY = event.getY();
			pointerView.setCurX(event.getX());
			pointerView.setCurY(event.getY());
			pointerView.invalidate();
			pointerView.setVisibility(View.VISIBLE);
			// 抬起记录按下的x,y
			upX = event.getX();
			upY = event.getY();
			break;
		// 移动动作
		case MotionEvent.ACTION_MOVE:
			disX = event.getX() - initX;
			disY = event.getY() - initY;
			pointerView.setCurX(event.getX());
			pointerView.setCurY(event.getY());
			pointerView.invalidate();
			// 如果移动了
			if (disX != 0 || disY != 0) {
				String msg = "<" + disX + "," + disY + ">";

				BaseApplication
						.getInstance()
						.getExecutor()
						.execute(
								new MouseThread(BaseApplication.getInstance(),
										msg));
			}
			initX = event.getX();
			initY = event.getY();
			break;
		// 抬起动作
		case MotionEvent.ACTION_UP:
			// 如果没有移动过
			if ((event.getX() - upX) == 0 && (event.getY() - upY) == 0) {
				if(event.getX()-mouseControl.getWidth()/2<0.01){
					BaseApplication
					.getInstance()
					.getExecutor()
					.execute(
							new MouseThread(BaseApplication.getInstance(),
									Constant.MOUSE_LEFT));
				}else{
					BaseApplication
					.getInstance()
					.getExecutor()
					.execute(
							new MouseThread(BaseApplication.getInstance(),
									Constant.MOUSE_RIGHT));
				}
				
			}
			pointerView.setVisibility(View.GONE);
			break;
		}
		return true;
	}
}
