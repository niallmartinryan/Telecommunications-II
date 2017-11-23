/**
 * 
 */
package cs.tcd.ie;
import java.util.*;
import java.net.DatagramSocket;
//import tcd.lossy.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;											//trying this for the moment

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
	static final int  MAX_BYTE_LENGTH = 64;														//no idea what to put this as..
	int frameNumber;																			// change this later
	byte framePayloads[][];
	int frameArray[];
	boolean gotResponse;																		// use this with socket exceptions
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
		terminal.println("Payload : " + payload + "\n");								// checking if the receipt matches the next packet code
		if(ack == check ){
			timer.cancel();
			timer = new Timer();
		}
		else{												// figure out what to do here...
															// cancel timer blah blah blah
			terminal.println("ResendPacket\n");				// will auto because of time outs

		}
		if(frameNumber == 1){
			frameNumber =0;
		}
		else{
			frameNumber =1;
		}

																						// this will contain analysis of the packet
		this.notify();
																						// and also to check with frameCode to send back
	}
	/*
	*	resend packet
	*
	*/
	public synchronized void resend() throws Exception {
		terminal.println("resending packet\n");
		socket.send(currentPacket);
	}
	/**
	 * Sender Method  
	 */
	public synchronized void start() throws Exception {
		DatagramPacket packet= null;
		frameArray = new int [15];														// dont know if i need this.
		framePayloads = new byte[MAX_BYTE_LENGTH][15];									// the firstvalue should be Payload.length, but cant intialise as that
		frameNumber = 0;
		byte[] payload= null;															// because we dont know that value and its constantly changing??
		byte[] header= null;
		byte[] buffer= null;
		
		while(true){																	// need to fix how headers are used.. packet 
			payload= (terminal.readString("String to send: ")).getBytes();
			framePayloads[frameNumber] = payload;
			frameCode = (""+ frameNumber).getBytes();
			header= new byte[PacketContent.HEADERLENGTH];

			System.arraycopy(frameCode, 0, header, 0, frameCode.length);
			
			buffer= new byte[header.length + payload.length];
			System.arraycopy(header, 0, buffer, 0, header.length);
			System.arraycopy(payload, 0, buffer, header.length, payload.length);

			terminal.println("Sending packet...");
			packet= new DatagramPacket(buffer, buffer.length, dstAddress);
			StringContent bufferString = new StringContent(packet);
			terminal.println("header : "+ bufferString.headerToString()  + "\n");
			terminal.println("payload : " + bufferString.toString() + "\n");
			currentPacket = packet;
			terminal.println("----------------------------------------------\n");
			timer.schedule(new TimerTask(){
				@Override
				public void run (){
					try{
		//			terminal = new Terminal();
					// need to keep on resending the packet..
					socket.send(currentPacket);
					terminal.println("\n");
					//System.exit(0);
					}catch(Exception e) {e.printStackTrace();}

				}

			},0, timeOut);									// sort this out later
			
//			frameNumber++;
			terminal.println("Packet sent");																	// at the moment this is fine.. because its stop and wait..
			this.wait();																	// later this wont work as it will be go back n
																							// might have to decrease frameNumber if transmission fails
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

		
	
}


	