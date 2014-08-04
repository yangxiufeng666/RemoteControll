package com.devil.remotecontroll.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devil.remotecontroll.R;
import com.devil.remotecontroll.bean.Disk;

/**
 * 盘符的adapter
 * @author 20082755
 *
 */
public class DiskAdapter extends BaseAdapter {

	private List<Disk> list;

	private Context context;

	private int selectedPosition = -1;

	public DiskAdapter(Context context) {
		this.context = context;
	}

	public DiskAdapter(Context context, List<Disk> list) {
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
					R.layout.disk_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.textView = (TextView) convertView
					.findViewById(R.id.disk_text);
			viewHolder.textSize = (TextView) convertView
					.findViewById(R.id.disk_text_size);
			viewHolder.linearLayout = (LinearLayout) convertView
					.findViewById(R.id.disk_lin);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (selectedPosition == position) {
			viewHolder.textView.setSelected(true);
			viewHolder.linearLayout.setBackgroundColor(context.getResources()
					.getColor(R.color.skyblue));
		} else {
			viewHolder.textView.setSelected(false);
			viewHolder.linearLayout.setBackgroundColor(Color.TRANSPARENT);
		}
		viewHolder.textView.setText(list.get(position).getFileName());
		viewHolder.textSize.setText("剩余:" + list.get(position).getFreeSpace());
		return convertView;
	}

	class ViewHolder {
		TextView textView;
		TextView textSize;
		LinearLayout linearLayout;
	}

	public int getSelectedPosition() {
		return selectedPosition;
	}

	public void setSelectedPosition(int selectedPosition) {
		this.selectedPosition = selectedPosition;
	}

	public List<Disk> getList() {
		return list;
	}

	public void setList(List<Disk> list) {
		this.list = list;
	}

}
