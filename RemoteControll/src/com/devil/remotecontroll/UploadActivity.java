package com.devil.remotecontroll;

import java.io.File;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.devil.remotecontroll.manager.DBManager;
import com.devil.remotecontroll.manager.RemoteActivityManager;
import com.devil.remotecontroll.thread.UploadThread;
import com.devil.remotecontroll.util.FileUtil;
/**
 * 
 * @author 20082755
 *
 */
public class UploadActivity extends BaseActivity implements OnClickListener{
	
	private TextView fileName;
	private TextView progressTxt;
	private ProgressBar progressBar;
	private Button uploadBtn;
	private String savePath;
	private String filePath;
	private File file;
	private UploadThread uploadThread;
	private DBManager dbManager;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case UploadThread.UPLOAD_FAILL:
				
				break;
			case UploadThread.UPLOAD_SUCCESS:
				uploadBtn.setText(R.string.upload_success);
				break;
			case UploadThread.UPLOAD_UPDATE:
				long length = (Long) msg.obj;

				// 当前进度值
				int progress = (int) (((float) length / file.length()) * 100);

				progressBar.setProgress(progress);

				progressTxt.setText(FileUtil.formatFileSize(length) + "/"
						+ FileUtil.formatFileSize(file.length()));
				break;
			default:
				break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upload_activity);
		findView();
		dbManager = new DBManager(this);
	}
	private void findView(){
		String filename = getIntent().getExtras().getString("uploadFileName", "");
		savePath = getIntent().getExtras().getString("savePath");
		filePath = getIntent().getExtras().getString("uploadFilePath");
		fileName = (TextView)findViewById(R.id.upload_filename);
		progressTxt = (TextView)findViewById(R.id.upload_progress_text);
		progressBar = (ProgressBar)findViewById(R.id.upload_progress);
		uploadBtn = (Button)findViewById(R.id.upload_btn);
		fileName.setText(filename);
		uploadBtn.setOnClickListener(this);
	}
	
	private void startUpload(){
		file = new File(filePath);
		uploadThread = new UploadThread(file, handler, savePath,dbManager);
		BaseApplication.getInstance().getExecutor()
				.execute(uploadThread);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.upload_btn:
			if(uploadBtn.getText().equals(getString(R.string.upload_success))){
				finish();
			}else if(uploadBtn.getText().equals(getString(R.string.upload_start))){
				startUpload();
				uploadBtn.setText(R.string.upload_pause);
			}else if(uploadBtn.getText().equals(getString(R.string.upload_pause))){
				//stop load
				uploadThread.setPause(true);
				uploadBtn.setText(R.string.upload_resume);
			}else if(uploadBtn.getText().equals(getString(R.string.upload_resume))){
				startUpload();
				uploadBtn.setText(R.string.upload_pause);
			}
			break;

		default:
			break;
		}
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case android.R.id.home:
			if(uploadThread!=null){
				uploadThread.setPause(true);
			}
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		RemoteActivityManager.getInstance().finishActivity();
	}
}
