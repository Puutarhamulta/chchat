package server;

import java.net.DatagramSocket;
import java.net.SocketException;

public class Server implements Runnable{
	
	private int port;
	private DatagramSocket socket;
	private boolean running = false;
	private Thread run, manage, send, receive;
	
	public Server(int port){
		this.port = port;
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		run = new Thread(this, "Server");
	}
	
	public void run(){
		running = true;
		manageClients();
		receive();
	}
	
	public void manageClients(){
		manage = new Thread("Manage"){
			public void run(){
				while (running){
					//Managing
				}
			}
		};
		manage.start();
	}
	
	public void receive(){
		receive = new Thread("Receive"){
			public void run(){
				while (running){
					//Receiving
				}
			}
		};
	}
}


