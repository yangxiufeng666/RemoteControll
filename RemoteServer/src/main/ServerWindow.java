package main;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import server.ConnectServer;

/**
 * 显示窗口
 * 
 * @author 赵庆洋
 * 
 */
public class ServerWindow extends Frame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Label label;

	private Label ip;

	private ConnectServer connectServer;

	public ServerWindow(String title) {
		super(title);

		label = new Label("Waiting for the user to connect.....");

		try {

			java.net.InetAddress test = java.net.InetAddress
					.getByName("localhost");

			ip = new Label("Please enter the IP on your phone"
					+ test.getLocalHost().getHostAddress());

		} catch (Exception e) {
			ip = new Label("Search ip wrong");
		}

		add(ip, BorderLayout.PAGE_START);

		add(label, BorderLayout.PAGE_END);

		this.addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent e) {
				try {
					connectServer = new ConnectServer();

					connectServer.setLabel(label);

					connectServer.service();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

			@Override
			public void windowIconified(WindowEvent e) {
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
			}

			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}

			@Override
			public void windowClosed(WindowEvent e) {
			}

			@Override
			public void windowActivated(WindowEvent e) {
			}
		});
	}
}
