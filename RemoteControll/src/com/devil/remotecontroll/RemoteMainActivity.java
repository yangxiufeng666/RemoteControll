package com.devil.remotecontroll;

import java.util.List;

import com.devil.remotecontroll.adapter.DiskAdapter;
import com.devil.remotecontroll.bean.Disk;
import com.devil.remotecontroll.fragment.CenterFragment;
import com.devil.remotecontroll.manager.RemoteActivityManager;
import com.devil.remotecontroll.net.SocketClientManager;
import com.devil.remotecontroll.slidingmenu.SlidingMenu;
import com.devil.remotecontroll.util.Util;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class RemoteMainActivity extends BaseFragmetActivity {
	private SlidingMenu sm;
	private DiskAdapter diskAdapter;
	private ListView slidingItemList;
	private List<Disk> diskList;
	private onKeyDownListener keyDownListener;

	@Override
	public void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		RemoteActivityManager.getInstance().addActivity(this);
		setContentView(R.layout.main_layout);

		initSlidingMenu();
		initListener();
		loadData();
	}

	private void selectItem(int position) {
		Fragment fragment = new CenterFragment();
		Bundle args = new Bundle();

		args.putString(CenterFragment.DISK_PATH,
				diskAdapter.getList().get(position).getPath());
		fragment.setArguments(args);
		FragmentManager fragmentManager = getSupportFragmentManager();

		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();
	}

	private void initSlidingMenu() {
		setBehindContentView(R.layout.behind_sliding_menu);
		sm = getSlidingMenu();
		sm.setShadowWidthRes(R.dimen.slidind_shadow_width);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		sm.setBehindWidth(dm.widthPixels * 4 / 5);
		sm.setFadeEnabled(true);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		sm.setShadowDrawable(R.drawable.slidingmenu_shadow);
		sm.setBehindScrollScale(0);
		diskAdapter = new DiskAdapter(this);
		slidingItemList = (ListView) findViewById(R.id.behind_silding_list);
		slidingItemList.setAdapter(diskAdapter);
	}

	private void initListener() {
		slidingItemList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				selectItem(position);
				sm.toggle();
			}
		});
	}

	private void loadData() {
		task.execute();
	}

	long time = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && keyDownListener != null) {
			boolean flag = keyDownListener.fragmentKeyDown(keyCode, event);

			if (!flag) {
				AlertDialog.Builder builder = new Builder(this);
				final Dialog dialog = builder
						.setTitle(R.string.exit_dialog_title)
						.setMessage(R.string.exit_dialog_content)
						.setPositiveButton(R.string.exit_dialog_btn1,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								})
						.setNegativeButton(R.string.exit_dialog_btn2,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
										finish();
									}

								}).create();
				dialog.show();
			}
			return flag;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onAttachFragment(Fragment fragment) {
		super.onAttachFragment(fragment);
		keyDownListener = (onKeyDownListener) fragment;

	}

	private AsyncTask<Void, Void, Boolean> task = new AsyncTask<Void, Void, Boolean>() {

		@Override
		protected Boolean doInBackground(Void... params) {
			diskList = SocketClientManager.getDiskInfo(application
					.getIpAdress());
			Log.d("fuck", diskList.size() + "");
			if (diskList.size() > 0) {
				return true;
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result) {
				diskAdapter.setList(diskList);
				diskAdapter.notifyDataSetChanged();
				selectItem(0);
			}

		}
	};

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.actions, menu);
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
			sm.showMenu();
			break;
		case R.id.action_mouse:
			intent = new Intent(this, MouseControllActivity.class);
			startActivity(intent);
			break;
		case R.id.action_shutdown:
			intent = new Intent(this, ShutDownActivity.class);
			startActivity(intent);
			break;
		case R.id.action_scan_file:
			if (!Util.isTopActiviy(".RemoteMainActivity", this)) {
				intent = new Intent(this, RemoteMainActivity.class);
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

	/**
	 * fragment keyDown回调
	 * 
	 * 
	 */
	public interface onKeyDownListener {
		public boolean fragmentKeyDown(int keyCode, KeyEvent event);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		RemoteActivityManager.getInstance().finishActivity();
	}
}
