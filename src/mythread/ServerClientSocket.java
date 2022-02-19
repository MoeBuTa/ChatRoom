package mythread;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.acl.Group;

import javax.swing.JOptionPane;

import entity.Message;
import util.BackInterface;
import util.ServerManager;
import view.ServerFrame;

public class ServerClientSocket implements Runnable {

	private Socket mySocket = null;
	private ObjectInputStream in = null;
	private ObjectOutputStream out = null;
	private boolean isRunnable = true;
	private BackInterface back;
	private String myIP;

	public ServerClientSocket(Socket mySocket, BackInterface back) {
		this.mySocket = mySocket;
		this.back = back;
		myIP = mySocket.getLocalAddress().getHostAddress() + ":" + mySocket.getLocalPort();
	}

	@Override
	public void run() {
		try {
			out = new ObjectOutputStream(mySocket.getOutputStream());
			in = new ObjectInputStream(mySocket.getInputStream());
			while (isRunnable) {				
				Message msg = (Message) in.readObject();
				for (ServerClientSocket clientSocket : ServerManager.CLIENTS.values()) {
					clientSocket.sendMsg(msg);
				}
				back.msgBack(msg);
			}
		} catch (Exception e) {			
			e.printStackTrace();
		}finally{
			String targetIP = mySocket.getInetAddress().getHostAddress()+":"+mySocket.getPort();
			ServerManager.CLIENTS.remove(targetIP);	
			back.clientLeave(targetIP);
		}
	}

	public void sendMsg(Message msg) {

		try {					
			out.writeObject(msg);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "服务器已关闭！", null, JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		}
	}

	public void start() {
		isRunnable = true;
		new Thread(this).start();
	}

	public void stop() {
		isRunnable = false;
		try {
			if (out != null)
				out.close();
			if (in != null)
				in.close();
			if (mySocket != null)
				mySocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getIP() {
		return myIP;
	}

	public Socket getTargetSocket() {
		return mySocket;
	}

}
