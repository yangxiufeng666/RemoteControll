package com.devil.remotecontroll.thread;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.devil.remotecontroll.BaseApplication;
import com.devil.remotecontroll.net.SocketClientManager;
import com.devil.remotecontroll.util.Constant;

public class KeyPadThread implements Runnable{
	private Socket socket;

	private DataOutputStream out;
	
	private int keyCode;
	public KeyPadThread( int keyCode) {
		super();
		this.keyCode = keyCode;
	}

	@Override
	public void run() {
		try {
			socket = SocketClientManager.getSocket(BaseApplication.getInstance().getIpAdress(), Constant.PC_KEYPAD_CON);
			out = new DataOutputStream(socket.getOutputStream());
			out.writeInt(keyCode);
		} catch (Exception e) {
			try {
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (socket != null) {
					socket.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

}
