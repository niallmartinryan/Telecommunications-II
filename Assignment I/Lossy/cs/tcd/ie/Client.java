/**
 * 
 */
package cs.tcd.ie;
import java.util.*;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;											//trying this for the moment

import tcdIO.*;

/**
 *
 * Client class
 * 
 * An instance accepts user input 
 *										FIX THIS AREA OF CODE WITH CONSTANTS/PRIVATES..
 */
public class Client extends Node {
	static final int DEFAULT_SRC_PORT = 50000;
	static final int DEFAULT_DST_PORT = 50001;
	static final String DEFAULT_DST_NODE = "localhost";	
	static final int  MAX_BYTE_LENGTH = 64;														//no idea what to put this as..
	int windowStart = 0;
	int windowEnd = 4;									// due to the way the if statement works
	int windowSize = 4;
	int frameSize = 16;
	String payloadString;
	String originalString;
	boolean frameSent =true;
	int deleteCounter;
	int frameNumber;																			// change this later
	char frameArray[];
	boolean gotResponse;																		// use this with socket exceptions
	boolean shift = false;
	byte[] frameCode = null;
	boolean taskSet;
	Terminal terminal;
	InetSocketAddress dstAddress;
	int timeOut = 10000;
	Timer timer = new Timer();
	Timer receive = new Timer();
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
		StringContent content= new StringContent(packet);
		String payload =  content.toString();
		String testString = "ACK" + (frameNumber + 1);
		int ack = Character.getNumericValue(payload.charAt(3));
		terminal.println("testString :  " + testString + "\n");
		terminal.println("Payload : " + payload + "\n");								// checking if the receipt matches the next packet code
		if(ack>= windowStart+1 && ack <windowEnd){
			terminal.println("GOT THE RIGHT ACK\n");
			windowEnd += (ack -windowStart);
			windowStart += (ack -windowStart);			

			terminal.println("ack : " + ack +" - windowStart : " + windowStart +"=" +windowEnd);
			timer.cancel();
			timer = new Timer();
			taskSet = true;
		}
		else{												// figure out what to do here...
															// cancel timer blah blah blah
			terminal.println("ResendPacket");				// will auto because of time outs

		}
		
		terminal.println(payload);

																						// this will contain analysis of the packet
		this.notify();
																						// and also to check with frameCode to send back
	}
	/*
	*	resend packet
	*
	*/
	public synchronized void quickWaitToReceive() throws Exception {							// dont need for go back n
		for(int j =0 ; j<1000000; j++){}	
		this.notify();
	}
	/**
	 * Sender Method  
	 */
	public synchronized void start() throws Exception {
		taskSet = true;
		deleteCounter = 0;						//
		DatagramPacket packet= null;
		frameArray = new char [frameSize];														// dont know if i need this.
//		framePayloads = new byte[MAX_BYTE_LENGTH][15];									// the firstvalue should be Payload.length, but cant intialise as that
		frameNumber = 0;
		byte[] payload= null;															// because we dont know that value and its constantly changing??
		byte[] header= null;
		byte[] buffer= null;
		
		while(true){
//			terminal.println("start\n");
//			terminal.println("windowEnd : " +windowEnd + "\n");
//			terminal.println("windowStart : " +windowStart + "\n");
			if(frameSent){
				payloadString = terminal.readString("String to send: ");
				char currentChar;
				if(frameNumber ==0){													// error where i get random char at start of string for first string
					payloadString = payloadString.substring(0, frameSize);						// need to change this
				}
				for(int i=0; i< payloadString.length(); i++){
					currentChar = payloadString.charAt(i);
//					terminal.println("currentChar : " + currentChar +"\n");
					frameArray[i] = currentChar;
				}
				if(payloadString.length()<frameSize){
					for(int i = payloadString.length(); i<frameSize; i++){
						frameArray[i] = '\0';
					}
				}
			}
			if(frameNumber < windowEnd){
				terminal.println("windowEnd : " + windowEnd + "\n");
				frameCode = (""+ frameNumber).getBytes();
				// payload= (terminal.readString("String to send: ")).getBytes();
				payload = Character.toString(frameArray[frameNumber]).getBytes();
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

				// if(bufferString.toString().equals("/fake")){
				// 	terminal.println("sending wrong sequence number e.g. skipped packet");
				// 	frameCode = (""+ "50").getBytes();
		
				// 	header= new byte[PacketContent.HEADERLENGTH];

				// 	System.arraycopy(frameCode, 0, header, 0, frameCode.length);
				// 	System.arraycopy(header, 0, buffer, 0, header.length);
				// 	System.arraycopy(payload, 0, buffer, header.length, payload.length);

				// 	terminal.println("Sending packet...");
				// 	packet= new DatagramPacket(buffer, buffer.length, dstAddress);
				// 	bufferString = new StringContent(packet);
				// 	terminal.println("header : "+ bufferString.headerToString()  + "\n");
				// 	terminal.println("payload : " + bufferString.toString() + "\n");
				// 	currentPacket = packet;
				// }
	//			terminal.println("taskSet : " +taskSet + "\n");
				terminal.println("FRAME NUMBER : " + frameNumber);
				if( taskSet == true){
					taskSet =false;
					timer.schedule(new Task(), timeOut);	
				}
				for(int j =0 ; j<1000000000; j++){}								// sort this out later, just to add delay
				
				socket.send(currentPacket);
				receive.schedule(new receiveAck(), 1000);												// receive is a timer
				this.wait();
				frameNumber++;
				terminal.println("Packet sent");
				frameSent =false;
			}
			if(frameNumber == windowEnd){
				terminal.println("waiting");
				this.wait();
			}					// if(allPackets in the current Window have been sent) then wait	// at the moment this is fine.. because its stop and wait..
																				// later this wont work as it will be go back n
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
	class Task extends TimerTask{
		public void run(){
			try{
			//			terminal = new Terminal();
						// need to keep on resending the packet..
						terminal.println("TIMER!!!");
						taskSet = true;
						frameNumber = windowStart;
						notifyStart();
						terminal.println("\n");											// dont think this will work.
						//System.exit(0);
				}catch(Exception e) {e.printStackTrace();}

		}
		
	}
		class receiveAck extends TimerTask{
		public void run(){
			try{
				quickWaitToReceive();


				}catch(Exception e) {e.printStackTrace();}

		}
		
	}		
	public synchronized void notifyStart() throws Exception {
			terminal.println("notifyStart");
			this.notify();
		}		
}


	