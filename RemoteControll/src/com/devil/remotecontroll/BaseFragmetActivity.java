package com.devil.remotecontroll;

import android.os.Bundle;

import com.devil.remotecontroll.manager.RemoteActivityManager;
import com.devil.remotecontroll.slidingmenu.app.SlidingFragmentActivity;

public class BaseFragmetActivity extends SlidingFragmentActivity{
	protected BaseApplication application;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		application = (BaseApplication)getApplication();
	}
}
