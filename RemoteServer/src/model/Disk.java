package model;

/**
 * 硬盘模块类
 * 
 * @author 20082755
 * 
 */
public class Disk {

	/**
	 * 磁盘名称
	 */
	private String fileName;
	/**
	 * 磁盘总大小
	 */
	private String totalSpace;
	/**
	 * 可以空间
	 */
	private String freeSpace;
	/**
	 * 路径
	 */
	private String path;

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
