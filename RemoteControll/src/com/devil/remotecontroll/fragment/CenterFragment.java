package com.devil.remotecontroll.fragment;

import java.util.List;
import java.util.Stack;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.devil.remotecontroll.DownLoadActivty;
import com.devil.remotecontroll.PhoneFileDialogActivity;
import com.devil.remotecontroll.R;
import com.devil.remotecontroll.RemoteMainActivity;
import com.devil.remotecontroll.RemoteMainActivity.onKeyDownListener;
import com.devil.remotecontroll.adapter.FileAdapter;
import com.devil.remotecontroll.bean.PcFile;
import com.devil.remotecontroll.net.SocketClientManager;

/**
 * 中间的布局
 * 
 * @author 赵庆洋
 * 
 */
public class CenterFragment extends BaseFragment implements OnClickListener,
		onKeyDownListener {

	public static final String DISK_PATH = "disk_path";

	private ListView listView;

	private ImageButton returnBtn, upBtn;

	private TextView titleView;

	private FileAdapter fileAdapter;

	private List<PcFile> fileList;

	private String diskPath;


	// path的堆栈
	private static Stack<String> pathStack;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		diskPath = getArguments().getString(DISK_PATH);
		addPath(diskPath);
	}

	/**
	 * 添加路径到堆栈
	 * 
	 * @param path
	 */
	public void addPath(String path) {

		if (pathStack == null) {
			pathStack = new Stack<String>();
		}

		pathStack.add(path);
	}

	/**
	 * 获取堆栈最上层的路径
	 * 
	 * @return
	 */
	public String getLastPath() {
		return pathStack.lastElement();
	}

	/**
	 * 移除堆栈最上层路径
	 */
	public void removeLastPath() {
		pathStack.remove(getLastPath());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.center_fragment, container,
				false);

		listView = (ListView) rootView.findViewById(R.id.file_list);

		fileAdapter = new FileAdapter(getActivity());

		listView.setAdapter(fileAdapter);

		listView.setOnItemClickListener(new DrawerItemClickListener());

		returnBtn = (ImageButton) rootView.findViewById(R.id.center_return_btn);

		upBtn = (ImageButton) rootView.findViewById(R.id.center_up_btn);

		returnBtn.setOnClickListener(this);

		upBtn.setOnClickListener(this);

		titleView = (TextView) rootView.findViewById(R.id.center_title);

		titleView.setText(diskPath);

		searchViewData(diskPath);

		return rootView;
	}

	/**
	 * 查询调用方法
	 */
	public void searchData(String path) {
		searchViewData(path);
		titleView.setText(path);
	}

	/**
	 * 查询view的数据
	 */
	public void searchViewData(final String path) {

		AsyncTask<Void, Void, Boolean> task = new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {

				fileList = SocketClientManager.getFileInfo(getMyApplication().getIpAdress(), path);
				if(fileList.size()>=0){
					return true;
				}
				return false;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);

				if (result) {

					fileAdapter.setList(fileList);

					fileAdapter.notifyDataSetChanged();

				} else {
					
				}
			}
		};
		task.execute();
	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	private void selectItem(int position) {

		String filePath = fileAdapter.getList().get(position).getFilePath();

		String fileName = fileAdapter.getList().get(position).getFileName();

		if (fileAdapter.getList().get(position).isDirectory()) {
			searchData(filePath);
			addPath(filePath);
		} else if (fileAdapter.getList().get(position).isFile()) {
			//是文件那就下载
			Intent intent = new Intent(getActivity(), DownLoadActivty.class);
			intent.putExtra(DownLoadActivty.FILE_NAME, fileName);
			intent.putExtra(DownLoadActivty.FILE_PATH, filePath);
			startActivity(intent);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.center_return_btn:
			if (getLastPath().equals(diskPath)) {
				return;
			}
			removeLastPath();

			searchData(getLastPath());

			break;

		case R.id.center_up_btn:
			Intent intent = new Intent(getActivity(),PhoneFileDialogActivity.class);
			intent.putExtra("upLoadPath", getLastPath());
			startActivity(intent);
			
			break;
		}
	}

	@Override
	public boolean fragmentKeyDown(int keyCode, KeyEvent event) {
		if(getLastPath().equals(diskPath)){
			return false;
		}
		removeLastPath();

		searchData(getLastPath());
		return true;
	}

}
