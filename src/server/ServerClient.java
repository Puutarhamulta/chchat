package server;

import java.net.InetAddress;

public class ServerClient {
	
	public String name;
	public InetAddress address;
	public int port;
	//ID genereras automatiskt
	public final int ID;
	//Om servern inte får kontakt/svar från klienten, inkrementeras attempt 
	public int attempt = 0;
	
	public ServerClient(String name, InetAddress address, int port, final int ID){
		this.name = name;
		this.address = address;
		this.port = port;
		this.ID = ID;
	}
	
	public int getID(){
		return ID;
	}
	
}
