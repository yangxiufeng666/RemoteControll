package com.devil.remotecontroll;

import com.devil.remotecontroll.manager.RemoteActivityManager;
import com.devil.remotecontroll.util.Util;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class BaseActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		RemoteActivityManager.getInstance().addActivity(this);
	}
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

//		getMenuInflater().inflate(R.menu.actions, menu);
		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		Intent intent;
		switch (id) {
		case android.R.id.home:
			finish();
			break;
		case R.id.action_mouse:
			if(!Util.isTopActiviy(".MouseControllActivity", this)){
				intent = new Intent(this, MouseControllActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.action_shutdown:
			if(!Util.isTopActiviy(".ShutDownActivity", this)){
				intent= new Intent(this, ShutDownActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.action_scan_file:
			if(!Util.isTopActiviy(".RemoteMainActivity", this)){
				intent= new Intent(this, RemoteMainActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.action_kepad:
			if(!Util.isTopActiviy(".KeyPadActivity", this)){
				intent= new Intent(this, KeyPadActivity.class);
				startActivity(intent);
			}
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
