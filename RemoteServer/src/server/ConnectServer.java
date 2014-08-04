package server;

import java.awt.Label;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import manager.ThreadManager;
import model.FileLog;

import thread.DownThread;
import thread.KeypadThread;
import thread.MouseThread;
import thread.ServerThread;
import thread.UpThread;

/**
 * 与客户端连接的查询server
 * 
 * @author 20082755
 * 
 */
public class ConnectServer {
	/**
	 * 服务端口
	 */
	private static ServerSocket serverSocket;
	/**
	 * 下载端口
	 */
	private static ServerSocket downSocket;
	/**
	 * 上传端口
	 */
	private static ServerSocket upSocket;
	/**
	 * 鼠标端口
	 */
	private static ServerSocket mouseSocket;
	/**
	 * 键盘socket
	 */
	private static ServerSocket kepadSocket;
	/**
	 * 服务端口
	 */
	private static final int port = 8624;
	/**
	 * 下载端口
	 */
	private static final int down_port = 8625;
	/**
	 * 上传端口
	 */
	private static final int up_port = 8626;
	/**
	 * 鼠标端口
	 */
	public final static int PC_MOUSE_CON = 8627;
	/**
	 * 键盘端口
	 */
	public final static int PC_KEYPAD_CON = 8628;

	private Label label;

	// 存放断点数据，最好改为数据库存放
	private Map<Long, FileLog> datas = new HashMap<Long, FileLog>();
	/**
	 * 连接服务器的线程
	 */
	private Runnable connectRunnable = new Runnable() {

		@Override
		public void run() {
			while (true) {
				System.out.println("查询服务器正在等待连接...");
				Socket socket;
				try {
					socket = serverSocket.accept();// 一直等待连接，没有连接，线程会处于阻塞状态
					label.setText("The user connection is successful.");
					// 对应每一个请求，开启另外的线程去处理
					ThreadManager.getInstance().getExecutorService()
							.execute(new ServerThread(socket));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	};
	// 开启一个下载线程等待连接，即从电脑下载东西到手机
	private Runnable downloadRunnable = new Runnable() {

		@Override
		public void run() {
			while (true) {
				System.out.println("下载服务器正在等待连接...");
				Socket socket;
				try {
					socket = downSocket.accept();
					// 对应每一个请求，开启另外的线程去处理
					ThreadManager.getInstance().getExecutorService()
							.execute(new DownThread(socket));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	};
	// 开启一个上传线程等待连接，从手机端上传数据到电脑
	private Runnable uploadRunnable = new Runnable() {

		@Override
		public void run() {
			while (true) {
				System.out.println("上传服务器正在等待连接...");
				Socket socket;
				try {
					socket = upSocket.accept();
					// 对应每一个请求，开启另外的线程去处理
					ThreadManager.getInstance().getExecutorService()
							.execute(new UpThread(socket, datas));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	};
	private Runnable mouseControllRunnable = new Runnable() {

		@Override
		public void run() {
			while (true) {
				System.out.println("鼠标控制服务器正在等待连接...");
				Socket socket;
				try {
					socket = mouseSocket.accept();
					// 对应每一个请求，开启另外的线程去处理
					ThreadManager.getInstance().getExecutorService()
							.execute(new MouseThread(socket));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	};
	private Runnable keypadRunnable = new Runnable() {

		@Override
		public void run() {
			while (true) {
				System.out.println("键盘控制器正在等待连接。。。。。");
				try {
					Socket socket = kepadSocket.accept();
					// 对应每一个请求，开启另外的线程去处理
					ThreadManager.getInstance().getExecutorService()
							.execute(new KeypadThread(socket));
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
	};

	public ConnectServer() throws IOException {
		serverSocket = new ServerSocket(port);
		downSocket = new ServerSocket(down_port);
		upSocket = new ServerSocket(up_port);
		mouseSocket = new ServerSocket(PC_MOUSE_CON);
		kepadSocket = new ServerSocket(PC_KEYPAD_CON);
		datas = new HashMap<Long, FileLog>();
		System.out.println("服务器连接启动.");
	}

	public void setLabel(Label label) {
		this.label = label;
	}

	/**
	 * 开始服务
	 * 
	 * @throws IOException
	 */
	public void service() throws IOException {

		new Thread(connectRunnable).start();
		new Thread(downloadRunnable).start();
		new Thread(uploadRunnable).start();
		new Thread(mouseControllRunnable).start();
		new Thread(keypadRunnable).start();
	}

}
