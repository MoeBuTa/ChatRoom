package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.ServerSocket;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import entity.Message;
import mythread.Server;
import mythread.ServerClientSocket;
import util.BackInterface;
import util.ServerManager;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JLabel;

public class ServerFrame extends JFrame implements ActionListener, BackInterface {

	private JPanel contentPane;
	private JButton btn_start;
	private JButton btn_stop;
	private JTextArea text_recv;
	private JTextArea text_send;
	private JList list_client;
	private JButton btn_sendIndividual;
	private JButton btn_sendAll;
	private JLabel label_client;
	private JTextArea text_info;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerFrame frame = new ServerFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ServerFrame() {
		setTitle("服务器");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 558, 352);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		btn_start = new JButton("开启服务");
		btn_start.setBounds(149, 10, 93, 23);
		contentPane.add(btn_start);

		btn_stop = new JButton("关闭服务");
		btn_stop.setEnabled(false);
		btn_stop.setBounds(306, 10, 93, 23);
		contentPane.add(btn_stop);

		btn_sendIndividual = new JButton("发送个人");
		btn_sendIndividual.setEnabled(false);
		btn_sendIndividual.setBounds(47, 281, 93, 23);
		contentPane.add(btn_sendIndividual);

		btn_sendAll = new JButton("发送全部");
		btn_sendAll.setEnabled(false);
		btn_sendAll.setBounds(240, 281, 93, 23);
		contentPane.add(btn_sendAll);

		text_recv = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(text_recv);
		scrollPane.setBounds(20, 43, 338, 143);
		contentPane.add(scrollPane);

		text_send = new JTextArea();
		JScrollPane scrollPane2 = new JScrollPane(text_send);
		scrollPane2.setBounds(20, 196, 338, 69);
		contentPane.add(scrollPane2);

		list_client = new JList();
		JScrollPane scrollPane3 = new JScrollPane(list_client);
		scrollPane3.setBounds(368, 43, 164, 120);
		contentPane.add(scrollPane3);

		label_client = new JLabel("          未选择");
		label_client.setBounds(368, 172, 164, 15);
		contentPane.add(label_client);
		
		text_info = new JTextArea();
		JScrollPane scrollPane_1 = new JScrollPane(text_info);
		scrollPane_1.setBounds(368, 197, 164, 108);
		contentPane.add(scrollPane_1);
		
		
		btn_start.addActionListener(this);
		btn_stop.addActionListener(this);
		btn_sendIndividual.addActionListener(this);
		btn_sendAll.addActionListener(this);

		list_client.addListSelectionListener(new ListSelectionListener() {		
			@Override
			public void valueChanged(ListSelectionEvent e) {				
				label_client.setText((String)list_client.getSelectedValue());
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
		DefaultListModel<String> model = new DefaultListModel<String>();
		for (String s : ServerManager.CLIENTS.keySet()) {
			model.addElement(s);
		}

		list_client.setModel(model);
		text_info.append(ip+"上线了！\r\n");
		
	}
	
	@Override
	public void clientLeave(String ip) {
		DefaultListModel<String> model = new DefaultListModel<String>();
		for (String s : ServerManager.CLIENTS.keySet()) {
			model.addElement(s);
		}
		list_client.setModel(model);
		label_client.setText("          未选择");
		text_info.append(ip+"已退出！\r\n");
		
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		JButton btn = (JButton) e.getSource();
		if (btn.equals(btn_start)) {
			start();

		} else if (btn.equals(btn_stop)) {
			stop();
		} else if (btn.equals(btn_sendIndividual)) {
			sendIndividual();
		} else if (btn.equals(btn_sendAll)) {
			sendAll();
		}
	}

	private void start() {
		ServerManager.SERVER = new Server(ServerFrame.this);
		ServerManager.SERVER.startServer();
		for (ServerClientSocket sockets : ServerManager.CLIENTS.values()) {
			sockets.start();
		}
		btn_sendAll.setEnabled(true);
		btn_sendIndividual.setEnabled(true);
		btn_stop.setEnabled(true);
		btn_start.setEnabled(false);

	}

	private void stop() {
		if (ServerManager.SERVER != null) {
			ServerManager.SERVER.stopServer();
			
		}

		btn_sendAll.setEnabled(false);
		btn_sendIndividual.setEnabled(false);
		btn_stop.setEnabled(false);
		btn_start.setEnabled(true);

	}

	private void sendIndividual() {
		if(list_client.isSelectionEmpty()){
			return;
		}
		String recvIP = label_client.getText();		
		Message msg = new Message(text_send.getText(), new Date(),"服务器", recvIP);
		if(text_send.getText().trim().equals("")){return;}
		ServerManager.CLIENTS.get(recvIP).sendMsg(msg);
		msgBack(msg);
		text_send.setText("");
	}

	private void sendAll() {
		Message msg = new Message(text_send.getText(), new Date(), "服务器", "");
		if(text_send.getText().trim().equals("")){return;}
		for (ServerClientSocket sockets : ServerManager.CLIENTS.values()) {
			sockets.sendMsg(msg);
		}
		msgBack(msg);
		text_send.setText("");
	}


}
