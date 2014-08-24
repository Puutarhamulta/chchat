package server;

import java.io.IOException;
import java.net.DatagramPacket;
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
			return;
		}
		run = new Thread(this, "Server");
		run.start();
	}
	
	public void run(){
		running = true;
		System.out.println("Server started on port "+port);
		receive();
		manageClients();
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
					byte[] data = new byte[1024];
					DatagramPacket packet = new DatagramPacket(data, data.length);
					try {
						socket.receive(packet);
						System.out.println("Beep from "+ packet.getAddress());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						//return;
					}
					String string = new String(packet.getData());
					System.out.println(string);
					
				}
			}
		};
		//DU GLÖMDE START THREAD (noob dit for ~2 timmar)
		receive.start();
	}
}


