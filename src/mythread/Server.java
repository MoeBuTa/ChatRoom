package mythread;

import java.net.ServerSocket;
import java.net.Socket;

import util.BackInterface;
import util.ServerManager;

public class Server implements Runnable {
	private ServerSocket server = null;
	private boolean isRunnable = false;
	private BackInterface back;

	public Server(BackInterface back) {
		try {
			server = new ServerSocket(6666);
			this.back = back;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			while (isRunnable) {
				// 1.不停的等待连接，有客户连接进来就会创建一个连接的管道对象
				Socket clientSocket = server.accept();
				
				// 2.通过管道对象就可以获取到客户端的IP+端口信息
				
				String IP = clientSocket.getInetAddress().getHostAddress();
				int port = clientSocket.getPort();
				// 3.启动另外一个线程不停地接受该客户端的消息
				ServerClientSocket socket = new ServerClientSocket(clientSocket, back);
				socket.start();
				// 4.将添加进来的客户信息加入到Map集合中
				ServerManager.CLIENTS.put(IP + ":" + port, socket);
				// 5.刷新界面
				back.clientCome(IP + ":" + port);
				
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void startServer() {
		isRunnable = true;
		new Thread(this).start();
	}

	public void stopServer() {
		isRunnable = false;
		try {			
			for (ServerClientSocket sockets : ServerManager.CLIENTS.values()) {				
				sockets.stop();
			}						
			server.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
