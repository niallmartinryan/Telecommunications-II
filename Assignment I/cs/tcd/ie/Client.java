/**
 * 
 */
package cs.tcd.ie;

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
// my imports
import tcdIO.*;
import javax.swing.Timer;
/**
 *
 * Client class
 * 
 * An instance accepts user input 
 *
 */
public class Client extends Node {
	static final int DEFAULT_SRC_PORT = 50000;
	static final int DEFAULT_DST_PORT = 50001;
	static final String DEFAULT_DST_NODE = "localhost";	
	
	boolean  shift = false;					//need to implement the 3 seconds**
	Terminal terminal;
	InetSocketAddress dstAddress;
	
	/**
	 * Constructor
	 * 	 
	 * Attempts to create socket at given port and create an InetSocketAddress for the destinations
	 */
	Client(Terminal terminal, String dstHost, int dstPort, int srcPort) {
		try {
			this.terminal= terminal;
			dstAddress= new InetSocketAddress(dstHost, dstPort);
			socket= new DatagramSocket(srcPort);
			listener.go();
		}
		catch(java.lang.Exception e) {e.printStackTrace();}
	}
			//java timer class
			// If the timer expires and a acknowledgement hasnt been received that matches.. then
			// an exception should be thrown
	//
	
	/**
	 * Assume that incoming packets contain a String and print the string.
	 */
	public synchronized void onReceipt(DatagramPacket packet) {
		StringContent content= new StringContent(packet);
		if(shift == true){
			shift = false;
		}
		else{
			shift = true;
		}	
		this.notify();
		terminal.println(content.toString());
	}

	
	/**
	 * Sender Method
	 * 
	 */
	public synchronized void start() throws Exception {
		byte[] data= null;
		DatagramPacket packet= null;
		StringContent cotent;
		char transmission;
		
		while(true){
			if(shift == false){
				transmission = '0';
			} //"0" + 
			else{
				transmission = '1';
			}
			data= (terminal.readString("String to send: ")).getBytes();
			String dataString = data.toString();
			terminal.println(dataString);
			dataString = dataString + transmission ;
			terminal.println(dataString);

			// byte[] newData = dataString.getBytes();
			// DatagramPacket newPacket = new DatagramPacket(newData, data.length, dstAddress);
			// socket.send(newPacket);
			data = dataString.getBytes();

			terminal.println("Sending packet...");
			packet= new DatagramPacket(data, data.length, dstAddress);
			socket.send(packet);
			terminal.println("Packet sent");
			this.wait();
		}	
	}


	/**
	 * Test method
	 * 
	 * Sends a packet to a given address
	 */
	public static void main(String[] args) {
		try {					
			Terminal terminal= new Terminal("Client");		
			(new Client(terminal, DEFAULT_DST_NODE, DEFAULT_DST_PORT, DEFAULT_SRC_PORT)).start();
			terminal.println("Program completed");
		} catch(java.lang.Exception e) {e.printStackTrace();}
	}
}
