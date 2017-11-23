
import java.net.DatagramSocket;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.util.*;

import tcdIO.*;


public class Endpoint extends Node {
	static final int ENDPOINT_1 = 50000;
	static final int ENDPOINT_2 = 50001;
	static final String DEFAULT_DST_NODE = "localhost";	
	
	Terminal terminal;
	InetSocketAddress dstAddress;
	RoutingTable table;
	int connectedRouter;
	int[] connectedNodes;		//Only use this if there is more than one router connected to the smartphone for some reason
	int id;

	
	Endpoint(Terminal terminal, String dstHost, int id, int connectedRouter) {
		try {
			this.terminal= terminal;
			dstAddress= new InetSocketAddress(dstHost, connectedRouter);
			this.id = id;
			this.connectedRouter = connectedRouter;
			terminal.println("id: " + id);
			socket= new DatagramSocket(id);
			listener.go();
		}
		catch(java.lang.Exception e) {e.printStackTrace();}
	}

	
	/**
	 * Assume that incoming packets contain a String and print the string.
	 */
	public synchronized void onReceipt(DatagramPacket packet) {
		byte[] data = packet.getData();
		StringContent content= new StringContent(packet);
		terminal.println("Message Received: " + content.toString());
	}
	
	/**
	 * Sender Method
	 * 
	 */
	public synchronized void start() throws Exception {	

		DatagramPacket packet= null;
		
		//This is the order of the packet structure!

		byte[] mes_type = null;
		byte[] source = null; 			//Who originally sent the message
		byte[] destination = null;		//Where the message is going
		byte[] payload = null;
		byte[] buffer = null;			//Final byte array that's put into the packet
	
		payload= (terminal.readString("Message: ")).getBytes();		//Reads in the payload from the user

		mes_type = new byte[PacketContent.MES_TYPE];
		destination = new byte[PacketContent.ID_LENGTH];
		source = new byte[PacketContent.ID_LENGTH];

		String dest = Integer.toString(connectedRouter);
		terminal.println("DEST: " + dest);
		destination = dest.getBytes();
		// terminal.println("Size of destination[]: " + destination.length);
		source = Integer.toString(id).getBytes();
		mes_type = PacketContent.MES.getBytes();
		// terminal.println("Size of mes_type[]: " + mes_type.length);

		//Need to clean up this and put the source and destination in the mes_type
		buffer= new byte[mes_type.length + source.length + destination.length + payload.length];
		System.arraycopy(mes_type, 0, buffer, 0, mes_type.length);
		System.arraycopy(source, 0, buffer, mes_type.length, source.length);
		System.arraycopy(destination, 0, buffer, (source.length + mes_type.length), destination.length);
		System.arraycopy(payload, 0, buffer, (source.length + mes_type.length + destination.length), payload.length);
		
		//terminal.println("Sending packet...");
		packet= new DatagramPacket(buffer, buffer.length, dstAddress);

		socket.send(packet);
		
		//terminal.println("Packet sent");
	}
}
