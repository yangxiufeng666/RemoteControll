package com.devil.remotecontroll.thread;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.devil.remotecontroll.BaseApplication;
import com.devil.remotecontroll.DownLoadActivty;
import com.devil.remotecontroll.net.SocketClientManager;
import com.devil.remotecontroll.util.Constant;
import com.devil.remotecontroll.util.FileUtil;

public class DownLoadThread implements Runnable{
	
	private Socket socket;

	private String filePath;

	private String fileName;
	
	public final static int BUFFER = 8 * 1024;

	private Handler handler;

	private boolean start = true;
	
	public DownLoadThread(String filePath, String fileName, Handler handler) {
		super();
		this.filePath = filePath;
		this.fileName = fileName;
		this.handler = handler;
	}
	
	/*
	 * 服务器提出下载请求，返回文件长度
	 */
	private long request() throws IOException {


		DataOutputStream out = new DataOutputStream(socket.getOutputStream());
		
		String params = filePath + "@" + fileName;

		// 发出下载请求
		out.writeUTF(params);

		out.flush();
		DataInputStream in = new DataInputStream(socket.getInputStream());
		// 返回文件长度
		return in.readLong();

	}

	/**
	 * 接收保存文件
	 * 
	 * @param message
	 * 
	 * @param localFile
	 *            文件
	 * @param tempFile
	 *            临时文件
	 * @param fileLength
	 *            文件长度
	 * @param message
	 */
	private void receiveFile(File localFile, File tempFile, long fileLength)
			throws IOException {

		// 获取socket的输入流包装成bufferedInputStream
		BufferedInputStream in = new BufferedInputStream(
				socket.getInputStream());

		// 获取本地关联的临时文件流
		FileOutputStream out = new FileOutputStream(tempFile);

		byte[] buf = new byte[BUFFER];

		// 每次下载的长度
		int len;

		// 累计下载的长度
		int count = 0;

		while (start && ((len = in.read(buf)) >= 0)) {

			out.write(buf, 0, len);

			out.flush();

			count += len;

			Message message = new Message();

			message.what = DownLoadActivty.DOWNLOAD_UPDATE;

			// 当前进度值
			int progress = (int) (((float) count / fileLength) * 100);

			// 当先下载文件大小
			String currentSize = FileUtil.formatFileSize(count);

			Map<String, Object> map = new HashMap<String, Object>();

			map.put("progress", progress);

			map.put("currentSize", currentSize);

			map.put("fileSize", FileUtil.formatFileSize(fileLength));

			message.obj = map;

			handler.sendMessage(message);
		}

		out.close();

		in.close();

		// 如果下载完成
		if (count == fileLength) {
			// 临时文件重命名
			if (tempFile.renameTo(localFile)) {

				Message message = new Message();

				message.what = DownLoadActivty.DOWNLOAD_SUCCESS;

				message.obj = localFile.getAbsolutePath();

				handler.sendMessage(message);

				Log.i("DownLoad", "下载完毕");
			} else {
				handler.sendEmptyMessage(DownLoadActivty.DOWNLOAD_RENAME);
			}
		}

		// 如果暂停下载 删除已有的临时文件
		if (!start) {

			if (tempFile.exists()) {
				tempFile.delete();
			}
		}

	}

	// 从服务器下载文件
	private void download() {
		try {
			//获取下载目录
			String localFilePath = FileUtil.fileDirectory(Constant.DOWN_PATH,
					fileName);
			//临时文件
			String tempFilePath = FileUtil.fileDirectory(Constant.DOWN_PATH,
					fileName.substring(0, fileName.indexOf(".")) + ".tmp");

			File file = new File(localFilePath);

			if (file.exists()) {

				Message message = new Message();

				message.what = DownLoadActivty.DOWNLOAD_EXITS;

				message.obj = localFilePath;

				handler.sendMessage(message);

				return;
			}

			// 临时文件
			File tempFile = new File(tempFilePath);

			socket = SocketClientManager.getSocket(BaseApplication.getInstance().getIpAdress(), Constant.down_port);

			// 文件长度
			long fileLength = request();

			if (fileLength > 0) {
				Log.i("DownLoad",
						"文件大小" + FileUtil.formatFileSize(fileLength));
//				Log.v(AppException.LOG_TAG, "开始下载");
				Log.i("DownLoad", "开始下载");
				// 保存到本地
				receiveFile(file, tempFile, fileLength);

			} else {
				Log.e("DownLoad", "下载出错");
				handler.sendEmptyMessage(DownLoadActivty.DOWNLOAD_FAILL);
			}
		} catch (IOException e) {
			Log.e("DownLoad", "下载出错");
			e.printStackTrace();
			handler.sendEmptyMessage(DownLoadActivty.DOWNLOAD_FAILL);
		} finally {
			try {
				if (socket != null) {
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		download();
	}

}
