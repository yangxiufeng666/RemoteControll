package com.devil.remotecontroll.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import com.devil.remotecontroll.BaseApplication;
import com.devil.remotecontroll.bean.Disk;
import com.devil.remotecontroll.bean.PcFile;
import com.devil.remotecontroll.util.Constant;

public class SocketClientManager {
	// 连接超时时间
	private static final int TIMEOUT_CONNECTION = 2000;

	// 重新操作次数
	private static final int RETRY_TIME = 3;

	/**
	 * 获取socket
	 * 
	 * @return
	 * @throws IOException
	 * @throws UnknownHostException
	 * @throws Exception
	 */
	public static Socket getSocket(String destinationIp,int port)
			throws UnknownHostException, IOException {
		Socket socket = new Socket(destinationIp, port);
		socket.setKeepAlive(true);
		socket.setSoTimeout(TIMEOUT_CONNECTION);
		return socket;
	}

	/**
	 * socket连接方法
	 * 
	 * @return
	 */
	private static String socketConnect(String destinationIp,
			String method,int port) {

		DataInputStream dis = null;

		DataOutputStream dos = null;

		Socket socket = null;

		String result = "";

		int time = 0;

		do {
			try {
				socket = getSocket(destinationIp,port);

				dos = new DataOutputStream(socket.getOutputStream());

				dos.writeUTF(method);

				dis = new DataInputStream(socket.getInputStream());

				result = dis.readUTF();
				break;

			} catch (IOException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);

						continue;
					} catch (InterruptedException e1) {
					}
				}
				e.printStackTrace();
			} finally {
				try {
					if (dis != null) {
						dis.close();
					}
					if (dos != null) {
						dos.close();
					}
					if (socket != null) {
						socket.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		} while (time < RETRY_TIME);

		return result;
	}

	// 获取盘符信息
	public static List<Disk> getDiskInfo(String destinationIp) {
		return Disk.parse(socketConnect(destinationIp, Constant.GET_DISK,Constant.port));
	}
	// 获取盘符下的文件信息
	public static List<PcFile> getFileInfo(String destinationIp,
			String diskPath){
		return PcFile.parse(socketConnect(destinationIp, diskPath,Constant.port));
	}
	//
	// 关闭PC计算机
	public static boolean PowerOffPc(String destinationIp) {
		return Boolean.valueOf(socketConnect(destinationIp, Constant.POWER_PC,Constant.port));
	}
}
