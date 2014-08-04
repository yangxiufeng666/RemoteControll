package thread;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

/**
 * 下载线程
 * @author 20082755
 *
 */
public class DownThread implements Runnable{

	private Socket socket;

	public final static int BUFFER = 8 * 1024;

	public DownThread(Socket socket) {
		this.socket = socket;
	}

	/**
	 * 获取文件的长度
	 * 
	 * @param path
	 * @param fileName
	 * @return
	 */
	private long getFileLength(String path) {

		File file = new File(path);

		// 如果文件不存在 返回-1
		if (!file.exists()) {
			return -1;
		}

		return file.length();
	}

	/**
	 * 用指定的流发送文件
	 * 
	 * @param outputStream
	 * @param path
	 * @param fileName
	 */
	private boolean sendFile(OutputStream out, String path) throws IOException {

		FileInputStream in = new FileInputStream(path);

		System.out.println("DownServer sendFile" + path);

		byte[] buf = new byte[BUFFER];

		int len;

		while ((len = in.read(buf)) >= 0) {

			try {
				out.write(buf, 0, len);

				out.flush();

			} catch (SocketException e) {

				out.close();

				in.close();

				return false;
			}

		}

		out.close();

		in.close();

		return true;
	}

	public void download() {

		System.out.println("启动下载");

//		System.out
//				.println("DownServer currentThread" + "id:"
//						+ currentThread().getId() + "name:"
//						+ currentThread().getName());

		try {

			// 获取socket的输入流包装成dataInputStream
			DataInputStream in = new DataInputStream(socket.getInputStream());

			// 获取socket的输出流包装成dataOutputStream
			DataOutputStream out = new DataOutputStream(
					socket.getOutputStream());

			String parameterStr = in.readUTF();

			// 分隔成string数组
			String[] parameter = parameterStr.split("@");

			String filePath = parameter[0];

			String fileName = parameter[1];

			long len = getFileLength(filePath);

			out.writeLong(len);

			// 强制清除缓存
			out.flush();
			
			if (len > 0) {
				System.out.println("正在下载文件:" + fileName);

				if (sendFile(out, filePath)) {
					System.out.println(fileName + ": " + "下载完毕 ");
				} else {
					System.out.println(fileName + ": " + "下载出错");
				}

			} else {
				System.out.println("下载文件不存在!!!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

	}

	@Override
	public void run() {
		download();
	}
}
