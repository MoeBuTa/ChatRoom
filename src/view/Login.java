package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import dao.UserDao;
import entity.User;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JPasswordField;

public class Login extends JFrame {

	private JPanel contentPane;
	private JTextField text_ip;
	private JTextField text_id;
	private JLabel label;
	private JButton btn_login;
	private JButton btn_register;
	private UserDao udao = new UserDao();
	private JLabel label_1;
	private JTextField text_portNum;
	private ClientFrame clients;
	private JPasswordField text_pwd;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
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
	public Login() {
		setTitle("登录聊天室");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		text_ip = new JTextField("192.168.7.187");
		text_ip.setBounds(85, 119, 129, 21);
		contentPane.add(text_ip);
		text_ip.setColumns(10);

		text_portNum = new JTextField("6666");
		text_portNum.setColumns(10);
		text_portNum.setBounds(295, 119, 54, 21);
		contentPane.add(text_portNum);

		text_id = new JTextField();
		text_id.setColumns(10);
		text_id.setBounds(159, 150, 129, 21);
		contentPane.add(text_id);

		JLabel lblIp = new JLabel("IP地址：");
		lblIp.setBounds(30, 122, 54, 15);
		contentPane.add(lblIp);

		JLabel label_pwd = new JLabel("密  码：");
		label_pwd.setBounds(95, 184, 54, 15);
		contentPane.add(label_pwd);

		label = new JLabel("用户名：");
		label.setBounds(95, 153, 54, 15);
		contentPane.add(label);

		btn_login = new JButton("登录");
		btn_login.setBounds(72, 229, 93, 23);
		contentPane.add(btn_login);

		btn_register = new JButton("注册");
		btn_register.setBounds(237, 229, 93, 23);
		contentPane.add(btn_register);

		label_1 = new JLabel("端口号：");
		label_1.setBounds(240, 122, 54, 15);
		contentPane.add(label_1);
		
		text_pwd = new JPasswordField();
		text_pwd.setBounds(159, 181, 129, 21);
		contentPane.add(text_pwd);

		btn_login.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});
		btn_register.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				register();
			}
		});
	}

	private void login() {

		try {
			String ip = text_ip.getText().trim();
			int portNum = Integer.parseInt(text_portNum.getText().trim());
			String name = text_id.getText().trim();
			String pwd = text_pwd.getText().trim();
			if (!udao.query(name, pwd).isEmpty() && !name.equals("")) {
				clients = new ClientFrame(ip, portNum, name, pwd);
				clients.setVisible(true);
			} else {
				JOptionPane.showMessageDialog(null, "用户名不存在或密码错误！", null, JOptionPane.WARNING_MESSAGE);
				return;
			}
			Login.this.dispose();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "找不到服务器！", null, JOptionPane.WARNING_MESSAGE);
			// e.printStackTrace();
		}

	}

	private void register() {
		String name = text_id.getText().trim();
		String pwd = text_pwd.getText().trim();
		if (udao.query(name, null).isEmpty()||name.equals("")) {
			if (!name.equals("")) {
				if (!pwd.equals("")) {
					User u = new User(name, pwd);
					udao.insert(u);
					JOptionPane.showMessageDialog(null, "注册成功！", null, JOptionPane.WARNING_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "密码不能为空！", null, JOptionPane.WARNING_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(null, "用户名不能为空！", null, JOptionPane.WARNING_MESSAGE);

			}

		} else {
			JOptionPane.showMessageDialog(null, "用户已存在！", null, JOptionPane.WARNING_MESSAGE);
		}
	}
}
