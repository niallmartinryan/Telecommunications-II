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
public class Client extends Node { // ALOT OF THESE SHOULD BE CONTANTS STATIC FINALS..
	static final int DEFAULT_SRC_PORT = 50000;
	static final int DEFAULT_DST_PORT = 50001;
	static final String DEFAULT_DST_NODE = "localhost";	
	static final int  MAX_BYTE_LENGTH = 64;														
	static final int noise = 10;						// used
	int windowStart = 0;
	int windowEnd = 4;									
	int windowSize = 4;
	static final int frameSize = 16;
	String payloadString;
	boolean frameSent =true;
	int frameNumber;																			// change this later
	char frameArray[];
	boolean shift = false;
	byte[] frameCode = null;
	boolean taskSet;
	Terminal terminal;
	InetSocketAddress dstAddress;
	static final int timeOut = 20000;
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
	public synchronized void onReceipt(DatagramPacket packet) {		// FIX INPUT

		StringContent content= new StringContent(packet);
		String payload =  content.toString();
		String payloadSubstring = payload.substring(3);
		String testString = ""+payload.charAt(0)+payload.charAt(1)+payload.charAt(2);
		int ack=0;
		if(payload.length()>4){
			if(payload.charAt(4)>='0' & payload.charAt(4)<='9'){

				 ack = (Integer.parseInt(payloadSubstring));
			}
		}
		else{
				 ack = Character.getNumericValue(payload.charAt(3));
		}
		
		if(testString.equals("NAK")){					// nak received..
			terminal.println("Nak received \n");
			frameNumber = windowStart;
		}
		else{
			terminal.println("Payload : " + payload + "\n");								// checking if the receipt matches the next packet code
			if(ack == frameSize){
				frameSent =true;
			}
			if(ack>= windowStart+1 && ack <=windowEnd){
				terminal.println("Ack Confirmed\n");
				windowEnd += (ack -windowStart);
				windowStart += (ack -windowStart);			

				timer.cancel();
				timer = new Timer();
				taskSet = true;
			}
			else{												// figure out what to do here...
																// cancel timer blah blah blah
				terminal.println("ResendPacket");				// will auto because of time outs

			}
		}
		terminal.println("Payload received : "+payload)	;							
		this.notify();								
	}
	/*
	*	resend packet
	*
	*/
	public synchronized void quickWaitToReceive() throws Exception {				// just to slow down the process and see results			
		for(int j =0 ; j<1000000; j++){}				
		this.notify();
	}
	/**
	 * Sender Method  
	 */
	public synchronized void start() throws Exception {
		taskSet = true;
		DatagramPacket packet= null;
		frameArray = new char [frameSize];																							
		frameNumber = 0;
		byte[] payload= null;														
		byte[] header= null;
		byte[] buffer= null;
		
		while(true){
			if(frameNumber==frameSize){
				// might try get rid of frameSent and replace with this.wait()
					this.wait();
				//	frameSent=true;
					frameNumber=0;
					windowStart=0;
					windowEnd = 4;
					windowSize= 4;
				}
			for(int j =0 ; j<1000000000; j++){}	

			if(frameSent){
				
				payloadString = terminal.readString("Enter String to send(16 chars per packet):  ");
				char currentChar;
				if(frameNumber ==0){
					if(payloadString.length()>=frameSize){
						payloadString = payloadString.substring(0, frameSize);
					}												// error where i get random char at start of string for first string
					else{
						payloadString = payloadString.substring(0, payloadString.length());						// need to change this
					}
				}
				for(int i=0; i< payloadString.length(); i++){
					currentChar = payloadString.charAt(i);
					frameArray[i] = currentChar;
				}
				if(payloadString.length()<frameSize){
					for(int i=payloadString.length()+1;i<frameSize;i++){
						frameArray[i] = '0';											//padding if the payload string is less than 16 char
					}
				}
				if(payloadString.length()<frameSize){
					for(int i = payloadString.length(); i<frameSize; i++){
						frameArray[i] = '\0';
					}
				}
			}
			if(frameNumber < windowEnd){
				frameCode = (""+ frameNumber).getBytes();
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
				for(int j =0 ; j<1000000000; j++){}								// just to add delay
				terminal.println("frame number : " + frameNumber + "\n");
				if( taskSet == true){
					taskSet =false;
					timer.schedule(new Task(), timeOut);	
				}
				
				if ((Math.random()*100) > noise) {  // the packet will be send depending on a random number between 0 and 100
					socket.send(currentPacket);
				}
				else {
					terminal.println("Packet dropped \n");
				}

				receive.schedule(new receiveAck(), 1000);				// just to show in sync
				frameNumber++;											// receive is a timer
				this.wait();
				terminal.println("Packet sent");
				frameSent =false;
			}
			if(frameNumber == windowEnd){
				terminal.println("waiting");
				this.wait();
			}					
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
						// need to keep on resending the packet..
						terminal.println("TIMER!!!\n");
						taskSet = true;
						frameNumber = windowStart;						// reset back to windowStart
						notifyStart();										
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


	