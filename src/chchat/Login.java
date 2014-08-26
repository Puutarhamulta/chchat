package chchat;


import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Login extends JFrame {

	/**
	 * Create the frame.
	 */
	public Login() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName() );
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		setResizable(false);
		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 220, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtName = new JTextField();
		txtName.setText("egu");
		txtName.setBounds(32, 39, 140, 25);
		contentPane.add(txtName);
		txtName.setColumns(10);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setHorizontalAlignment(SwingConstants.CENTER);
		lblName.setBounds(7, 12, 190, 15);
		contentPane.add(lblName);
		
		txtIpAddress = new JTextField();
		txtIpAddress.setText("localhost");
		txtIpAddress.setColumns(10);
		txtIpAddress.setBounds(32, 103, 140, 25);
		contentPane.add(txtIpAddress);
		
		lblIpAddress = new JLabel("IP Address:");
		lblIpAddress.setBounds(45, 76, 114, 15);
		contentPane.add(lblIpAddress);
		
		txtPort = new JTextField();
		txtPort.setText("8192");
		txtPort.setColumns(10);
		txtPort.setBounds(32, 167, 140, 25);
		contentPane.add(txtPort);
		
		lblPort = new JLabel("Port:");
		lblPort.setBounds(45, 140, 114, 15);
		contentPane.add(lblPort);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addKeyListener(new KeyAdapter() {
			
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER){
					String name = txtName.getText();
					String address = txtIpAddress.getText();
					int port = Integer.parseInt(txtPort.getText());
					login(name, address, port);
				}
			}
		});
		btnLogin.addActionListener(new ActionListener() {
			
				public void actionPerformed(ActionEvent e) {
					String name = txtName.getText();
					String address = txtIpAddress.getText();
					int port = Integer.parseInt(txtPort.getText());
					login(name, address, port);
				}

		});
		btnLogin.setBounds(44, 236, 117, 25);
		contentPane.add(btnLogin);
		btnLogin.requestFocusInWindow();
	}
	
	private void login(String name, String address, int port) {
		dispose();
		//System.out.println("Name: "+ name + " Address: "+address+" Port: "+port);
		//Client client = new Client(name, address, port);
		new ClientWindow(name, address, port);
		
	}
	private JPanel contentPane;
	private JTextField txtName;
	private JTextField txtIpAddress;
	private JLabel lblIpAddress;
	private JTextField txtPort;
	private JLabel lblPort;

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
}
