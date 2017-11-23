/**
 * 
 */
package cs.tcd.ie;
import java.util.*;
import java.net.DatagramSocket;
//import tcd.lossy.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;											
import tcdIO.*;

/**
 *
 * Client class
 * 
 * An instance accepts user input 
 * FRAME NUMBER SHOULD BE SEQUENCE NUMBER
 */
public class Client extends Node {
	static final int DEFAULT_SRC_PORT = 50000;
	static final int DEFAULT_DST_PORT = 50001;
	static final String DEFAULT_DST_NODE = "localhost";	
	static final int  MAX_BYTE_LENGTH = 64;	
	int noise = 40;												
	boolean notResending;
	int frameNumber;																			
	boolean shift = false;
	byte[] frameCode = null;
	Terminal terminal;
	InetSocketAddress dstAddress;
	int timeOut = 5000;
	Timer timer = new Timer();
	DatagramPacket currentPacket;
	/*
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

	
	/**
	 * Assume that incoming packets contain a String and print the string.
	 */
	public synchronized void onReceipt(DatagramPacket packet) {
		terminal.println("IncomingPacket\n");
		StringContent content= new StringContent(packet);
		String payload =  content.toString();
		int ack = Character.getNumericValue(payload.charAt(3));
		int check = frameNumber;
		if(check == 1){
			check =0;
		}
		else{
			check =1;
		}
		terminal.println("Payload : " + payload + "\n");							
		if(ack == check ){
			timer.cancel();
			timer = new Timer();
		}
		else{												
			terminal.println("ResendPacket\n");				

		}
		if(frameNumber == 1){
			frameNumber =0;
		}
		else{
			frameNumber =1;
		}
		this.notify();
	}
	/*
	*	resend packet
	*
	*/
	public synchronized void resend() throws Exception {
		terminal.println("resending packet\n");
		socket.send(currentPacket);
		timer.schedule(new Task(), timeOut);
	}
	/**
	 * Sender Method  
	 */
	public synchronized void start() throws Exception {
		DatagramPacket packet= null;
		frameNumber = 0;
		byte[] payload= null;															
		byte[] header= null;
		byte[] buffer= null;
		
		while(true){																
				payload= (terminal.readString("String to send: ")).getBytes();
				frameCode = (""+ frameNumber).getBytes();
				header= new byte[PacketContent.HEADERLENGTH];

				System.arraycopy(frameCode, 0, header, 0, frameCode.length);
				
				buffer= new byte[header.length + payload.length];
				System.arraycopy(header, 0, buffer, 0, header.length);
				System.arraycopy(payload, 0, buffer, header.length, payload.length);

				terminal.println("Sending packet...\n");
				packet= new DatagramPacket(buffer, buffer.length, dstAddress);
				StringContent bufferString = new StringContent(packet);
				terminal.println("header : "+ bufferString.headerToString()  + "\n");
				terminal.println("payload : " + bufferString.toString() + "\n");
				currentPacket = packet;
				if ((Math.random()*100) > noise) {  // the packet will be send depending on a random number between 0 and 100
					socket.send(currentPacket);
				}
				// spliting up packets.
				terminal.println("----------------------------------------------\n");
				timer.schedule(new Task(), timeOut);									
	//			frameNumber++; 	
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
	//	Terminal terminal;

	class Task extends TimerTask{
		public void run (){
						try{
						// need to keep on resending the packet..
						terminal.println("Resending Packet due to timeout");
						resend();
						}catch(Exception e) {e.printStackTrace();}

					}
		
	}
}


	