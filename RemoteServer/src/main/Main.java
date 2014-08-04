package main;

import java.awt.Color;

public class Main {

	public static void main(String[] args) {

		ServerWindow window = new ServerWindow("远程遥控器服务端");
		window.setSize(300, 100);
		window.setBackground(Color.white);
		window.setVisible(true);
	}
}
