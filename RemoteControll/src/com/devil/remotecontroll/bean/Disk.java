package com.devil.remotecontroll.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * pc硬盘信息
 * 
 * @author 80074242
 * 
 */
public class Disk extends Base {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8900218717415895357L;

	private String fileName;

	private String totalSpace;

	private String freeSpace;

	private String path;

	/**
	 * 解析json字符串
	 * 
	 * @param json
	 * @return
	 */
	public static List<Disk> parse(String json) {
		Log.d("fuck", "parse"+json);
		List<Disk> list = new ArrayList<Disk>();

		try {

			JSONArray jsonArray = new JSONArray(json);

			for (int i = 0; i < jsonArray.length(); i++) {
				Disk disk = new Disk();

				JSONObject jsonObject = jsonArray.getJSONObject(i);

				disk.setFileName(jsonObject.getString("fileName"));

				disk.setTotalSpace(jsonObject.getString("totalSpace"));

				disk.setFreeSpace(jsonObject.getString("freeSpace"));

				disk.setPath(jsonObject.getString("path"));

				list.add(disk);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getTotalSpace() {
		return totalSpace;
	}

	public void setTotalSpace(String totalSpace) {
		this.totalSpace = totalSpace;
	}

	public String getFreeSpace() {
		return freeSpace;
	}

	public void setFreeSpace(String freeSpace) {
		this.freeSpace = freeSpace;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
