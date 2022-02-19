package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import entity.Message;
import mythread.ServerClientSocket;
import util.BackInterface;
import util.ServerManager;

import javax.swing.JTextArea;
import javax.sound.sampled.Port;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class ClientFrame extends JFrame implements BackInterface {

	private JPanel contentPane;
	private JTextArea text_send;
	private JTextArea text_recv;
	private JButton btn_send;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane2;
	private ServerClientSocket client;
	private JLabel label_ip;
	private String ip;
	private int portNum;
	private String name;
	private String pwd;

	/**
	 * @param ip
	 * @param portNum
	 * @param name
	 * @param pwd
	 */

	/**
	 * Launch the application.
	 */
	// public static void main(String[] args) {
	// EventQueue.invokeLater(new Runnable() {
	// public void run() {
	// try {
	// ClientFrame frame = new ClientFrame(ip,portNum, name,pwd);
	// frame.setVisible(true);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// });
	// }

	/**
	 * Create the frame.
	 */
	public ClientFrame(String ip, int portNum, String name, String pwd) throws Exception {
		this.ip = ip;
		this.portNum = portNum;
		this.name = name;
		this.pwd = pwd;

		setTitle("客户端");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 496, 396);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		text_recv = new JTextArea();
		scrollPane = new JScrollPane(text_recv);
		scrollPane.setBounds(10, 30, 456, 200);
		contentPane.add(scrollPane);

		text_send = new JTextArea();
		scrollPane2 = new JScrollPane(text_send);
		scrollPane2.setBounds(10, 256, 353, 92);
		contentPane.add(scrollPane2);

		btn_send = new JButton("发送");
		btn_send.setBounds(373, 292, 93, 23);
		contentPane.add(btn_send);

		label_ip = new JLabel("");
		label_ip.setBounds(95, 5, 245, 15);
		contentPane.add(label_ip);

		client = new ServerClientSocket(new Socket(ip, portNum), this);
		client.start();
		label_ip.setText(client.getIP() + " " + name);

		btn_send.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				Message msg = new Message(text_send.getText(), new Date(), name, "");
				if (text_send.getText().trim().equals("")) {
					return;
				}

				client.sendMsg(msg);
				text_send.setText("");
			}
		});
	}

	@Override
	public void msgBack(Message msg) {
		String str = msg.getTime().toLocaleString() + " " + msg.getSendIP() + ":\r\n" + msg.getContent();
		text_recv.append(str + "\r\n");
	}

	@Override
	public void clientCome(String ip) {

	}

	@Override
	public void clientLeave(String ip) {
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPortNum() {
		return portNum;
	}

	public void setPortNum(int portNum) {
		this.portNum = portNum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

}
