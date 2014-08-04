package com.devil.remotecontroll;

import com.devil.remotecontroll.manager.RemoteActivityManager;
import com.devil.remotecontroll.util.Util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class IPsetActivity extends Activity {
	public final static String IS_STRAT_MAIN = "isStartMain";

	private EditText edit1, edit2, edit3, edit4;

	private Button submitButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		RemoteActivityManager.getInstance().addActivity(this);
		setContentView(R.layout.ip_set_layout);
		initView();
	}

	private void initView() {

		edit1 = (EditText) findViewById(R.id.local_gw_edit_1);

		edit2 = (EditText) findViewById(R.id.local_gw_edit_2);

		edit3 = (EditText) findViewById(R.id.local_gw_edit_3);

		edit4 = (EditText) findViewById(R.id.local_gw_edit_4);

		submitButton = (Button) findViewById(R.id.ipset_submit);

		//split(".")不能处理.，所有要换成,。.是java的对象操作符
		String[] ip = BaseApplication.getInstance().getIpAdress().replace(".", ",")
				.split(",");
		Log.i("fuck", "ip = "+ip.length);

		edit1.setText(ip[0]);

		edit2.setText(ip[1]);

		edit3.setText(ip[2]);

		edit4.setText(ip[3]);

		submitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String str = null;
				if (TextUtils.isEmpty(edit1.getText().toString())
						|| TextUtils.isEmpty(edit2.getText().toString())
						|| TextUtils.isEmpty(edit3.getText().toString())
						|| TextUtils.isEmpty(edit4.getText().toString())) {
					Toast.makeText(IPsetActivity.this,
							R.string.ipset_activity_setting_error,
							Toast.LENGTH_SHORT).show();
					return;
				}

				String ipadress = edit1.getText().toString() + "."
						+ edit2.getText().toString() + "."
						+ edit3.getText().toString() + "."
						+ edit4.getText().toString();
				if(!Util.checkIp(ipadress)){
					Toast.makeText(IPsetActivity.this,
							R.string.ipset_activity_setting_error,
							Toast.LENGTH_SHORT).show();
					return;
				}
				BaseApplication.getInstance().setIpAddress(ipadress);
				Intent intent = new Intent(IPsetActivity.this, RemoteMainActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
//		RemoteActivityManager.getInstance().finishActivity();
	}
}
