package model;

/**
 * 文件的模板类
 * 
 * @author 80074242
 * 
 */
public class PcFile {

	/**
	 * 文件名称
	 */
	private String fileName;

	/**
	 * 是否是目录
	 */
	private boolean directory;

	/**
	 * 是否是一个文件
	 * */
	private boolean file;

	/**
	 * 路径
	 */
	private String filePath;

	/**
	 * 总大小
	 */
	private String totalSpace;

	/**
	 * 可用大小
	 */
	private String freeSpace;

	/**
	 * 大小
	 */
	private long length;

	/**
	 * 上一级
	 */
	private String parent;

	/**
	 * 上一级目录
	 */
	private String parentPath;

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

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

	public String getFreeSpace() {
		return freeSpace;
	}

	public void setFreeSpace(String freeSpace) {
		this.freeSpace = freeSpace;
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
