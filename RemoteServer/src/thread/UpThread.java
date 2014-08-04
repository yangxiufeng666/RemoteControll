package thread;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.Map;
import java.util.Properties;

import model.FileLog;

/**
 * 上传线程
 * @author 20082755
 *
 */
public class UpThread implements Runnable {

	private Socket socket;

	private Map<Long, FileLog> datas;

	public final static int BUFFER = 1024;

	public UpThread(Socket socket, Map<Long, FileLog> datas) {
		this.socket = socket;
		this.datas = datas;
	}

	/**
	 * 上传方法
	 */
	public void upLoad() {
		try {

			System.out.println("开始上传");

			DataInputStream inStream = new DataInputStream(
					socket.getInputStream());

			// 获取从客户端传来的参数
			String params = inStream.readUTF();

			System.out.println("params:    " + params);
			if (params == null || "".equals(params)) {
				return;
			}
			// 下面从协议数据中读取各种参数值【length=2815143;filename=IMG20140425085826.jpg;sourceid=;filePath=E:\】
			String[] items = params.split(";");
			// 要上传文件的长度
			String filelength = items[0].substring(items[0].indexOf("=") + 1);
			// 文件名称(带后缀名)
			String filename = items[1].substring(items[1].indexOf("=") + 1);
			// 上传过的记录的唯一标示符
			String sourceid = items[2].substring(items[2].indexOf("=") + 1);
			// 要上传到电脑的目录路径
			String filepath = items[3].substring(items[3].indexOf("=") + 1);

			// 临时文件名称
			String tempname = filename.substring(0, filename.indexOf("."))
					+ ".tmp";

			// 每个上传记录的唯一标示符
			Long id = System.currentTimeMillis();

			// 下载的记录
			FileLog log = null;

			// 如果手机端有存储上传记录的标示符，那就去查找本地是否有保存上传的这个记录
			if (null != sourceid && !"".equals(sourceid)) {
				id = Long.valueOf(sourceid);
				// 查找上传的文件是否存在上传记录
				log = find(id);
			}
			// 下载的文件
			File file = null;
			// 临时的文件
			File tempfile = null;
			//已经下载的位置，没有则为0
			long position = 0;
			// 如果不存在上传记录 为文件添加记录
			if (log == null) {
				// 目录
				File dir = new File(filepath);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				// 文件
				file = new File(dir, filename);

				tempfile = new File(dir, tempname);
				// 如果文件存在 改名
				if (file.exists()) {
					filename = filename.substring(0, filename.indexOf(".") - 1)
							+ dir.listFiles().length
							+ filename.substring(filename.indexOf("."));

					tempname = filename.substring(0, filename.indexOf("."))
							+ ".tmp";

					file = new File(dir, filename);

					tempfile = new File(dir, tempname);

				}
				save(id, file, tempfile);
			}
			// 如果存在记录,读取上次的断点
			else {
				file = new File(log.getPath());
				tempfile = new File(log.getTempPath());
				if (tempfile.exists()) {
					File logFile = new File(file.getParentFile(),
							file.getName() + ".log");
					if (logFile.exists()) {
						Properties properties = new Properties();
						properties.load(new FileInputStream(logFile));
						// 读取断点的位置
						position = Long.valueOf(properties
								.getProperty("length"));
						System.out.println("position: properties " + position);
					}
				}

			}

			// 向客户端发送sourceid和断点position
			DataOutputStream dataOutputStream = new DataOutputStream(
					socket.getOutputStream());

			String response = "sourceid=" + id + ";position=" + position;

			System.out.println("position:       " + position);

			// sourceid由服务生成，唯一标识上传的文件，position指示客户端从文件的什么位置开始上传
			dataOutputStream.writeUTF(response);

			dataOutputStream.flush();

			inStream = new DataInputStream(socket.getInputStream());

			// 向硬盘写文件
			RandomAccessFile fileOutStream = new RandomAccessFile(tempfile,
					"rwd");

			if (position == 0) {
				fileOutStream.setLength(Long.valueOf(filelength));
			}

			// 移动到文件指定位置开始写数据
			fileOutStream.seek(position);

			byte[] buffer = new byte[BUFFER];

			int len = -1;

			long length = position;

			while ((len = inStream.read(buffer)) != -1) {// read也会阻塞
				fileOutStream.write(buffer, 0, len);
				length += len;
				Properties properties = new Properties();
				properties.put("length", String.valueOf(length));
				FileOutputStream logFile = new FileOutputStream(new File(
						file.getParentFile(), file.getName() + ".log"));
				// 实时记录文件的最后保存位置
				properties.store(logFile, null);
				logFile.close();
			}

			if (length == Long.valueOf(filelength)) {
				fileOutStream.close();
				if (tempfile.renameTo(file)) {
					File logFile = new File(file.getParentFile(),
							file.getName() + ".log");
					if (logFile.exists()) {
						logFile.delete();
					}
					delete(id);
					System.out.println("上传成功");
				}
			} else {
				System.out.println("上传中断");
				fileOutStream.close();
			}
			inStream.close();
			dataOutputStream.close();
			file = null;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public FileLog find(Long sourceid) {
		return datas.get(sourceid);
	}

	// 当文件上传完毕，删除记录
	public void delete(long sourceid) {
		if (datas.containsKey(sourceid))
			datas.remove(sourceid);
	}

	// 保存上传记录
	public void save(Long id, File saveFile, File tempFile) {
		// 日后可以改成通过数据库存放
		FileLog fileLog = new FileLog();
		fileLog.setId(id);
		fileLog.setPath(saveFile.getAbsolutePath());
		fileLog.setTempPath(tempFile.getAbsolutePath());
		datas.put(id, fileLog);
	}

	@Override
	public void run() {
		upLoad();
//		super.run();
	}
}
