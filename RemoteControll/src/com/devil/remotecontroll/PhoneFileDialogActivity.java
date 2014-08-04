package com.devil.remotecontroll;

import java.io.File;
import java.util.List;
import java.util.Stack;

import com.devil.remotecontroll.adapter.PhoneFileAdapter;
import com.devil.remotecontroll.adapter.PhoneFileAdapter.ViewHolder;
import com.devil.remotecontroll.manager.RemoteActivityManager;
import com.devil.remotecontroll.util.FileUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class PhoneFileDialogActivity extends Activity{
	private TextView localFilePath;
	private ImageButton returnBtn;
	private ListView fileListView;
	private File root;
	private List<File> fileList;
	private PhoneFileAdapter phoneFileAdapter;
	private String upLoadPath;
	// path的堆栈
	private static Stack<String> pathStack;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		RemoteActivityManager.getInstance().addActivity(this);
		setTitle("从SD卡中选择上传的文件");
		setContentView(R.layout.local_file_dialog);
		String storageState = Environment.getExternalStorageState();
		upLoadPath = getIntent().getExtras().getString("upLoadPath");
		if (storageState.equals(Environment.MEDIA_MOUNTED)) {
			root = new File(Environment.getExternalStorageDirectory()
					.getAbsolutePath());
		} else {
			finish();
		}
		findView();
		addToStack(root.getPath());
		searchData(root.getPath());
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		RemoteActivityManager.getInstance().finishActivity();
	}
	private void findView(){
		localFilePath = (TextView)findViewById(R.id.local_File_title);
		returnBtn = (ImageButton)findViewById(R.id.local_File_return_btn);
		fileListView = (ListView)findViewById(R.id.local_file_list);
		localFilePath.setText(root.getPath());
		phoneFileAdapter = new PhoneFileAdapter(fileList, this);
		fileListView.setAdapter(phoneFileAdapter);
		fileListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				PhoneFileAdapter.ViewHolder holder = (ViewHolder) view.getTag();
				if(holder.isFolder()){
					searchData(holder.getPath());
					addToStack(holder.getPath());
					setRootPath(holder.getPath());
				}else{
					//下载
					Intent intent = new Intent(PhoneFileDialogActivity.this, UploadActivity.class);
					intent.putExtra("uploadFileName", holder.textView.getText().toString());
					intent.putExtra("uploadFilePath", holder.getPath());
					intent.putExtra("savePath", upLoadPath);
					startActivity(intent);
				}
			}
		});
		returnBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(root.getPath().equals(getLastPath())){
					return;
				}
				removeLastPath();
				String path = getLastPath();
				searchData(path);
				setRootPath(path);
			}
		});
	}
	private void searchData(String path){
		AsyncTask<String, Integer, List<File>> task = new AsyncTask<String, Integer, List<File>>(){

			@Override
			protected List<File> doInBackground(String... params) {
				return FileUtil.getFileListByPath(params[0]);
			}

			@Override
			protected void onPostExecute(List<File> result) {
				fileList = result;
				phoneFileAdapter.setFiles(fileList);
				phoneFileAdapter.notifyDataSetChanged();
			}

			@Override
			protected void onProgressUpdate(Integer... values) {
				Log.d("fuck", "a....="+values[0]);
			}
			
		};
		task.execute(path);
	}
	private void setRootPath(String path){
		localFilePath.setText(path);
	}
	private void addToStack(String path){
		if (pathStack == null) {
			pathStack = new Stack<String>();
		}
		pathStack.add(path);
	}
	private String getLastPath(){
		
		return pathStack.lastElement();
	}
	/**
	 * 移除堆栈最上层路径
	 */
	public void removeLastPath() {
		pathStack.remove(getLastPath());
	}
}
