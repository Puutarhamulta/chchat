package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Server implements Runnable{
	
	private List<ServerClient> clients = new ArrayList<ServerClient>();
	
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
	
	//"hey man", vanligt meddelande
	//"/c/42352523", connection packet med client ID
	
	public void receive(){
		receive = new Thread("Receive"){
			public void run(){
				while (running){
					byte[] data = new byte[1024];
					DatagramPacket packet = new DatagramPacket(data, data.length);
					try {
						socket.receive(packet);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						//return;
					}
					process(packet);
					
					//clients.add(new ServerClient("egu", packet.getAddress(), packet.getPort(), 50));
					//System.out.println(clients.get(0).address.toString()+":"+ clients.get(0).port);
					
				}
			}
		};
		//DU GLÖMDE START THREAD (noob dit for ~2 timmar)
		receive.start();
	}
	
	private void sendToAll(String message){
		for (int i = 0; i < clients.size(); i++){
			//System.out.println("sending message to all: "+i);
			ServerClient client = clients.get(i);
			send(message.getBytes(), client.address, client.port);
		}
	}
	
	private void send(String message, InetAddress address, int port){
		message += "/e/"; 
		send(message.getBytes(), address, port);
	}
	
	private void send(final byte[] data, final InetAddress address, final int port){
		send = new Thread("Send"){
			public void run(){
				DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
				try {
					socket.send(packet);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		send.start();
	}
	
	/*
	 * Här processeras datan som kommit i paketet. /c/ för ny användare, /m/ för chat meddelande, /d/ disconnect
	 *  "/c/egu", med subtring som börjar på tredje tecknet till slutet av stringen finns namnet på användaren
	 */
	private void process(DatagramPacket packet){
		String string = new String(packet.getData());
		if (string.startsWith("/c/")){
			
			//int radom = new SecureRandom().nextInt(); 
			//UUID id = UUID.randomUUID();
			int id = UniqueIdentifier.getIdentifier();
			// "/c/egu"
			String name = string.substring(3, string.length());
			//System.out.print(name);
			System.out.println(" has connected to the server with ID: " + id);
			clients.add(new ServerClient(
					string.substring(3, string.length()), packet.getAddress(), packet.getPort(), id));
			String ID = "/c/"+id;
			send(ID,packet.getAddress(),packet.getPort());
			
		} else if (string.startsWith("/m/")){
			//String message = string.substring(3, string.length());
			System.out.println("m paket: "+string);
			sendToAll(string);
		} else if (string.startsWith("/d/")){
			//String name = string.substring(3, string.length());
			String id = string.split("/d/|/e/")[1];
			System.out.println(id+" har loggat ut.");
			disconnect(Integer.parseInt(id), true);
		}
		
		else{
			System.out.println("annat paket");
		}
		
	}
	private void disconnect(int id, boolean status){
		ServerClient c = null;
		
		for (int i = 0; i < clients.size(); i++){
			System.out.println("hepp");
			if (clients.get(i).getID() == id) {
				c = clients.get(i);
				clients.remove(i);
				break;
			}
		}
		String message = "";
		if (status){
			message = "Client " + c.name + " (" + c.getID() + ") @ " + c.address.toString() + ":" + c.port + " disconnected.";
		}
		else {
			message = "Client " + c.name + " (" + c.getID() + ") @ " + c.address.toString() + ":" + c.port + " timed out.";
		}
		System.out.println(message);
	}
}


