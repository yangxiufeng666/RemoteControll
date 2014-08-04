package thread;

import java.awt.AWTException;
import java.awt.Event;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class KeypadThread implements Runnable{

	private Socket socket;
	
	
	public KeypadThread(Socket socket) {
		super();
		this.socket = socket;
	}
	@Override
	public void run() {
		try {
			DataInputStream in = new DataInputStream(socket.getInputStream());
			//从客户端读取输入的字符值
			int keycode =  in.readInt();
			System.out.println("输入的值为："+keycode);
			Robot robot = new Robot();
			robot.keyPress(keycode);
			robot.keyRelease(keycode);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
