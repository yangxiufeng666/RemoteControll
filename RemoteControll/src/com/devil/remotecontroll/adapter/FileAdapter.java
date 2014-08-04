package com.devil.remotecontroll.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devil.remotecontroll.R;
import com.devil.remotecontroll.bean.PcFile;
import com.devil.remotecontroll.util.FileUtil;

/**
 * 文件和文件夹的adapter
 * @author 20082755
 *
 */
public class FileAdapter extends BaseAdapter {

	private List<PcFile> list;

	private Context context;

	public FileAdapter(Context context) {
		this.context = context;
	}

	public FileAdapter(Context context, List<PcFile> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		if (list == null) {
			return 0;
		}
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
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
					R.layout.file_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.textView = (TextView) convertView
					.findViewById(R.id.file_text);
			viewHolder.textSize = (TextView) convertView
					.findViewById(R.id.file_text_size);
			viewHolder.fileIcon = (ImageView) convertView
					.findViewById(R.id.file_icon);
			viewHolder.fileImage = (ImageView) convertView
					.findViewById(R.id.file_image);
			viewHolder.linearLayout = (LinearLayout) convertView
					.findViewById(R.id.file_lin);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.textView.setText(list.get(position).getFileName());

		if (list.get(position).isDirectory()) {
			viewHolder.fileIcon.setImageResource(R.drawable.folder);
			viewHolder.fileImage.setImageResource(R.drawable.file_folder);
			viewHolder.textSize.setText("");
		} else {
			viewHolder.fileImage.setImageResource(R.drawable.file_down);
			FileUtil.setImage(context, list.get(position).getFileName(),
					viewHolder.fileIcon);
			viewHolder.textSize.setText(FileUtil.formatFileSize(list.get(
					position).getLength()));
		}

		return convertView;
	}

	class ViewHolder {
		TextView textView;
		TextView textSize;
		ImageView fileIcon;
		ImageView fileImage;
		LinearLayout linearLayout;
	}
	
	public List<PcFile> getList() {
		return list;
	}

	public void setList(List<PcFile> list) {
		this.list = list;
	}

}
