package com.devil.remotecontroll.thread;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.devil.remotecontroll.BaseApplication;
import com.devil.remotecontroll.bean.Upload;
import com.devil.remotecontroll.manager.DBManager;
import com.devil.remotecontroll.net.SocketClientManager;
import com.devil.remotecontroll.util.Constant;

/**
 * 
 * @author 20082755
 *
 */
public class UploadThread implements Runnable{
	
	// 上传失败状态
	public final static int UPLOAD_FAILL = -1;
	// 上传成功状态
	public final static int UPLOAD_SUCCESS = 1;
	// 上传更新状态
	public final static int UPLOAD_UPDATE = 2;

	private Socket socket;
	private File file;

	public final static int BUFFER = 1024;
	
	private Handler handler;

	private String savePath;
	
	private DBManager dbManager;
	
	private boolean isPause;
	
	public UploadThread(File file, Handler handler, String savePath,DBManager dbManager) {
		this.file = file;
		this.handler = handler;
		this.savePath = savePath;
		this.dbManager = dbManager;
	}
	/**
	 * @throws IOException 
	 * 向服务器请求上传
	 * 向服务器写文件的长度，文件名，文件路径，保存浏览
	 * 格式：params=lenght=1024;filename=哈哈.jpg;sourceid=DCMI/哈哈.jpg;savePath=...
	 * 服务器反馈：String response = "sourceid=" + id + ";position=" + position;
	 * sourceid(上传文件对应的唯一编码，时间毫秒)和断点position
	 */
	private String request(String sourceid) throws IOException{
		//得到输出流
		DataOutputStream out = new DataOutputStream(socket.getOutputStream());
		
		String params = "length=" + file.length() + ";filename="
				+ file.getName() + ";sourceid=" + sourceid + ";filePath="
				+ savePath;
		out.writeUTF(params);
		out.flush();
		DataInputStream in = new DataInputStream(socket.getInputStream());
		return in.readUTF();
	}
	@Override
	public void run() {
		try {
			socket = SocketClientManager.getSocket(BaseApplication
					.getInstance().getIpAdress(), Constant.up_port);
			String sourceid = dbManager.getResouceId(file.getAbsolutePath());
			String responsse = request(sourceid);//file.getAbsolutePath()
			String[] items = responsse.split(";");
			// 服务端断点记录标示符
			String responseid = items[0].substring(items[0].indexOf("=") + 1);

			// 服务端断点位置
			String position = items[1].substring(items[1].indexOf("=") + 1);
			// 没上传过此文件，添加到数据库
			if (TextUtils.isEmpty(sourceid)) {

				Upload upload = new Upload();

				upload.setSourceid(responseid);

				upload.setUploadfilepath(file.getAbsolutePath());

				dbManager.saveUpload(upload);
			}
			RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
			//得到输出流
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			//移动到断点处继续上传
			randomAccessFile.seek(Long.valueOf(position));
			byte[] buffer = new byte[BUFFER];

			int len = -1;

			long length = Long.valueOf(position);
			
			while (!isPause&&(len = randomAccessFile.read(buffer)) != -1) {
				out.write(buffer, 0, len);
				out.flush();
				length += len;
				Message msg = new Message();
				msg.what = UPLOAD_UPDATE;
				msg.obj = length;
				handler.sendMessage(msg);
			}
			randomAccessFile.close();
			out.close();
			if (file.length() == length) {
				dbManager.delUpload(file.getAbsolutePath());
				Log.v("Upload", "上传完毕");
				handler.sendEmptyMessage(UPLOAD_SUCCESS);
			}
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			
		}
	}
	public void setPause(boolean isPause) {
		this.isPause = isPause;
	}
	
}
