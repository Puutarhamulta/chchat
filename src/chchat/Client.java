package chchat;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

import java.awt.GridBagLayout;

import javax.swing.JTextArea;

import java.awt.GridBagConstraints;

import javax.swing.JButton;

import java.awt.Insets;

import javax.swing.JTextField;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;

	private String name, address;
	private int port;
	private JTextField txtMessage;
	private JTextArea history;
	private DefaultCaret caret;
	
	private DatagramSocket socket; //UDP socket
	private InetAddress ip;
	
	private Thread send;
	
	public Client(String name, String address, int port) {
		setTitle("ChChat");
		//Här sparas serverns address och port (inte klientens)
		this.name = name;
		this.address = address;
		this.port = port;
		
		boolean connect = openConnection(address);
		if (!connect){
			System.err.println("Connection failed");
			console("Connection failed");
		}
		
		createWindow();
		
		console("Attempting connection to: "+address+":"+port);
		/*
		 * Skickar första paket till servern.
		 * Strängen konverteras till byte och anropar send()
		 */
		String beep = (name + " connected from "+address+":"+port);
		send(beep.getBytes());
	}
	
	private boolean openConnection(String address){

		try {
			//Klienten kan inte använda samma port som servern, DatagramSocket() väljer en ledig port 
			socket = new DatagramSocket();
			//Obs! InetAddress (inte address)
			ip = InetAddress.getByName(address);
		} 
		catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
		
		
	}
	
	private String receive(){
		byte[] data = new byte[1024];
		DatagramPacket packet = new DatagramPacket(data, data.length);
		
		try {
			socket.receive(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String message = new String(packet.getData());
		return message;
		
	}
	
	private void createWindow(){	
		
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
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setSize(880, 550);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{20, 820, 30, 10};
		gbl_contentPane.rowHeights = new int[]{30, 480, 40};
		gbl_contentPane.columnWeights = new double[]{1.0, 1.0};
		gbl_contentPane.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		history = new JTextArea();
		history.setEditable(false);
		caret = (DefaultCaret) history.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		JScrollPane scroll = new JScrollPane(history);
		
		GridBagConstraints scrollConstraints = new GridBagConstraints();
		scrollConstraints.insets = new Insets(0, 0, 5, 5);
		scrollConstraints.fill = GridBagConstraints.BOTH;
		scrollConstraints.gridx = 0;
		scrollConstraints.gridy = 0;
		scrollConstraints.gridwidth = 3;
		scrollConstraints.gridheight = 2;
		contentPane.add(scroll, scrollConstraints);
		
		txtMessage = new JTextField();
		txtMessage.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER){
					send(txtMessage.getText());
				}
			}
		});
		
		GridBagConstraints gbc_txtMessage = new GridBagConstraints();
		gbc_txtMessage.insets = new Insets(0, 0, 0, 5);
		gbc_txtMessage.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtMessage.gridx = 1;
		gbc_txtMessage.gridy = 2;
		scrollConstraints.gridwidth = 3;
		scrollConstraints.gridheight = 2;
		contentPane.add(txtMessage, gbc_txtMessage);
		txtMessage.setColumns(10);
		
		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				send(txtMessage.getText());
			}
		});
		GridBagConstraints gbc_btnSend = new GridBagConstraints();
		gbc_btnSend.insets = new Insets(0, 0, 0, 5);
		gbc_btnSend.gridx = 2;
		gbc_btnSend.gridy = 2;
		contentPane.add(btnSend, gbc_btnSend);
		
		setVisible(true);
		txtMessage.requestFocusInWindow();
	}
	
	private void send(String message){
		txtMessage.requestFocusInWindow();
		if (message.equals("")) return;
		message = name +": "+ message;
		console(message);
		send(message.getBytes());
		txtMessage.setText("");
	}
	
	private void send(final byte[] data){
		send = new Thread("Send"){
			public void run(){
				System.out.println("sending packet");
				DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
				try {
					socket.send(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		//IGEN! Kom ihåg att starta threads
		send.start();
		
		
	}
	
	/*
	 * consoleskickar stringen till history/"chat rutan" och går till next line
	 * DOS & Windows: \r\n 0D0A (hex), 13,10 (decimal)
	   Unix & Mac OS X: \n, 0A, 10
	   Macintosh (OS 9): \r, 0D, 13
	 */
	public void console(String message){
		history.append(message + "\r\n");
	}
}
