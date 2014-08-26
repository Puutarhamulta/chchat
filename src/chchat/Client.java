package chchat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client{

	private static final long serialVersionUID = 1L;
	private String name, address;
	private int port;
	private DatagramSocket socket; //UDP socket
	private InetAddress ip;
	private Thread send;
	
	public Client(String name, String address, int port){
		this.name = name;
		this.address = address;
		this.port = port;
	}
	
	public String getName(){
		return name;
	}
	
	public String getAddress(){
		return address;
	}
	
	public int getPort(){
		return port;
	}
	
	boolean openConnection(String address){
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
	
	void send(final byte[] data){
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
	//Originella sättet
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
		//console(message);
		return message;
	}
	
	/* wooooah, de fungera inte så bra
	private void receive(){
		receive = new Thread("Receive"){
			public void run(){
				byte[] data = new byte[1024];
				DatagramPacket packet = new DatagramPacket(data, data.length);
				
				try {
					socket.receive(packet);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String message = new String(packet.getData());
				console(message);
				
			}
		};
		receive.start();
	}
	*/
	
}
