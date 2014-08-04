package model;

/**
 * 文件上传记录
 * 
 * @author 80074242
 * 
 */
public class FileLog {
	
	/**
	 * 上传文件对应的唯一编码
	 */
	private Long id;
	/**
	 * 真正写入得路径
	 */
	private String path;
	/**
	 * temp路径，副本，在没有传输完成的时候，文件名是这个副本，传输完成后，文件名改为上面的名称
	 */
	private String tempPath;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getTempPath() {
		return tempPath;
	}

	public void setTempPath(String tempPath) {
		this.tempPath = tempPath;
	}
}
