package com.devil.remotecontroll.adapter;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devil.remotecontroll.R;
import com.devil.remotecontroll.util.FileUtil;

public class PhoneFileAdapter extends BaseAdapter{

	private List<File> files;

	private Context context;

	public PhoneFileAdapter(List<File> files, Context context) {
		super();
		this.files = files;
		this.context = context;
	}

	@Override
	public int getCount() {
		if (files == null) {
			return 0;
		}
		return files.size();
	}

	@Override
	public Object getItem(int position) {
		return files.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.local_file_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.textView = (TextView) convertView
					.findViewById(R.id.local_file_text);
			viewHolder.textSize = (TextView) convertView
					.findViewById(R.id.local_file_text_size);
			viewHolder.fileIcon = (ImageView) convertView
					.findViewById(R.id.local_file_icon);
			viewHolder.fileImage = (ImageView) convertView
					.findViewById(R.id.local_file_image);
			viewHolder.linearLayout = (LinearLayout) convertView
					.findViewById(R.id.local_file_lin);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.textView.setText(files.get(position).getName());
		viewHolder.path = files.get(position).getAbsolutePath();
		viewHolder.parentPath = files.get(position).getParent();
		if (files.get(position).isDirectory()) {
			viewHolder.fileIcon.setImageResource(R.drawable.folder);
			viewHolder.fileImage.setImageResource(R.drawable.file_folder);
			viewHolder.textSize.setText("");
			viewHolder.isFolder = true;
		} else {
			viewHolder.fileImage.setImageResource(R.drawable.file_upload);
			FileUtil.setImage(context, files.get(position).getName(),
					viewHolder.fileIcon);
			viewHolder.textSize.setText(FileUtil.formatFileSize(files.get(
					position).length()));
			viewHolder.isFolder = false;
		}
		
		return convertView;
	}

	public class ViewHolder {
		/**
		 * 名称
		 */
		public TextView textView;
		/**
		 * 大小
		 */
		public TextView textSize;
		/**
		 * 文件标识图标
		 */
		ImageView fileIcon;
		/**
		 * 文件的话就是上传图标，文件夹是下拉图标
		 */
		ImageView fileImage;
		LinearLayout linearLayout;
		/**
		 * 是否是文件夹
		 */
		boolean isFolder;
		/**
		 * 路径
		 */
		String path;
		String parentPath;
		public boolean isFolder() {
			return isFolder;
		}
		public String getPath() {
			return path;
		}
		public String getParentPath() {
			return parentPath;
		}
		
	}
	public List<File> getFiles() {
		return files;
	}

	public void setFiles(List<File> files) {
		this.files = files;
	}
}
