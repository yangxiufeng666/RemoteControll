package com.devil.remotecontroll.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PcFile extends Base {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8396782383859284814L;

	// 文件名称
	private String fileName;

	// 是否是文件夹
	private boolean directory;

	// 是否是文件
	private boolean file;

	// 文件的路径
	private String filePath;

	// 总容量
	private String totalSpace;

	// 可用容量
	private String freeSpace;

	// 文件大小
	private long length;

	// 父节点
	private String parent;

	// 父节点路径
	private String parentPath;

	/**
	 * 解析json字符串
	 * 
	 * @param json
	 * @return
	 */
	public static List<PcFile> parse(String json) {

		List<PcFile> directoryList = new ArrayList<PcFile>();

		List<PcFile> fileList = new ArrayList<PcFile>();

		try {

			JSONArray jsonArray = new JSONArray(json);

			for (int i = 0; i < jsonArray.length(); i++) {

				PcFile file = new PcFile();

				JSONObject jsonObject = jsonArray.getJSONObject(i);

				file.setFileName(jsonObject.getString("fileName"));

				file.setTotalSpace(jsonObject.getString("totalSpace"));

				file.setFreeSpace(jsonObject.getString("freeSpace"));

				file.setFilePath(jsonObject.getString("filePath"));

				file.setDirectory(jsonObject.getBoolean("directory"));

				file.setFile(jsonObject.getBoolean("file"));

				file.setParent(jsonObject.getString("parent"));

				file.setParentPath(jsonObject.getString("parentPath"));

				file.setLength(jsonObject.getLong("length"));
				if (file.isDirectory()) {
					directoryList.add(file);
				} else {
					fileList.add(file);
				}
			}

			directoryList.addAll(fileList);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return directoryList;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public boolean isDirectory() {
		return directory;
	}

	public void setDirectory(boolean directory) {
		this.directory = directory;
	}

	public boolean isFile() {
		return file;
	}

	public void setFile(boolean file) {
		this.file = file;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
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

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getParentPath() {
		return parentPath;
	}

	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
	}
}
