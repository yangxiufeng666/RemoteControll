package com.devil.remotecontroll;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.devil.remotecontroll.manager.RemoteActivityManager;
import com.devil.remotecontroll.thread.DownLoadThread;
import com.devil.remotecontroll.util.FileUtil;

public class DownLoadActivty extends BaseActivity {
	public final static String FILE_NAME = "fileName";

	public final static String FILE_PATH = "filePath";

	// 文件重命名出错
	public final static int DOWNLOAD_RENAME = -2;
	// 下载失败状态
	public final static int DOWNLOAD_FAILL = -1;
	// 下载成功状态
	public final static int DOWNLOAD_SUCCESS = 1;
	// 下载更新状态
	public final static int DOWNLOAD_UPDATE = 2;
	// 已有文件
	public final static int DOWNLOAD_EXITS = 3;

	private ProgressBar progressBar;

	private TextView fileNameText, progressText;

	private String filePath;

	private String fileName;

	private DownLoadThread downLoadThread;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case DOWNLOAD_FAILL:
				Toast.makeText(DownLoadActivty.this, R.string.download_error, Toast.LENGTH_SHORT).show();
				break;
			case DOWNLOAD_RENAME:
				Toast.makeText(DownLoadActivty.this, R.string.download_rename_error, Toast.LENGTH_SHORT).show();
				break;
			case DOWNLOAD_SUCCESS:
				String localFilePath = (String) msg.obj;

				progressText.setText(R.string.download_success);

				// 打开文件
				File file = new File(localFilePath);

				 FileUtil.openFile(DownLoadActivty.this, fileName, file);
				break;
			case DOWNLOAD_UPDATE:
				Map<String, Object> map = (HashMap<String, Object>) msg.obj;

				progressBar.setProgress((Integer) map.get("progress"));

				progressText.setText(map.get("currentSize") + "/"
						+ map.get("fileSize"));
				break;
			case DOWNLOAD_EXITS:
				String localFilePath1 = (String) msg.obj;

				progressText.setText(R.string.download_exits);

				showAlertDialog(filePath, localFilePath1, fileName);
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.download_activity);

		fileName = getIntent().getExtras().getString(FILE_NAME);

		filePath = getIntent().getExtras().getString(FILE_PATH);

		initView();

		downLoad();
	}

	/**
	 * 下载方法
	 */
	private void downLoad() {
		downLoadThread = new DownLoadThread(filePath, fileName, handler);
		BaseApplication.getInstance().getExecutor().execute(downLoadThread);
	}

	/**
	 * 初始化视图
	 */
	private void initView() {

		progressBar = (ProgressBar) findViewById(R.id.download_progress);

		fileNameText = (TextView) findViewById(R.id.download_filename);

		progressText = (TextView) findViewById(R.id.download_progress_text);

		fileNameText.setText(fileName);

	}

	/**
	 * 显示是否重新下载的dialog
	 * 
	 * @param handler
	 * @param application
	 */
	public void showAlertDialog(final String filePath,
			final String localFilePath, final String fileName) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("提示");
		builder.setMessage("该文件已下载，是否重新下载?");
		builder.setPositiveButton("确定", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 删除文件
				File file = new File(localFilePath);
				file.delete();
				downLoad();

			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 打开文件
				File file = new File(localFilePath);

				 FileUtil.openFile(DownLoadActivty.this, fileName, file);

				dialog.dismiss();
				finish();

			}
		});
		builder.create().show();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		RemoteActivityManager.getInstance().finishActivity();
	}
}
