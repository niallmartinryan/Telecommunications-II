/**
 * 
 */
package cs.tcd.ie;
import java.util.*;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;											//trying this for the moment
import java.util.ArrayList;

import tcdIO.*;

/**
 *
 * Client class
 * 
 * An instance accepts user input 
 *										
 */
public class SmartPhone extends Node { // ALOT OF THESE SHOULD BE CONTANTS STATIC FINALS..
	static final int DEFAULT_SRC_PORT = 50000;
	static final int DEFAULT_DST_PORT = 50001;



	static final String DEFAULT_DST_NODE = "localhost";	
	static final int  MAX_PACKET_LENGTH = 64;														
	static final int noise = 10;						// used
	Terminal terminal;
	InetSocketAddress dstAddress;
	static final int timeOut = 20000;
	Timer timer = new Timer();
	Timer receive = new Timer();
	DatagramPacket currentPacket;
	static int id;
	ArrayList<String> availRouters;

	/*
	 * Constructor
	 * 	 
	 * Attempts to create socket at given port and create an InetSocketAddress for the destinations
	 */
	SmartPhone(Terminal terminal, String dstHost, int dstPort, int srcPort, int id) {
		try {
			this.terminal= terminal;
			dstAddress= new InetSocketAddress(dstHost, dstPort);
			this.id= id;
			socket= new DatagramSocket(srcPort);
			listener.go();
		}
		catch(java.lang.Exception e) {e.printStackTrace();}
	}

	
	/**
	 * Assume that incoming packets contain a String and print the string.
	 */
	public synchronized void onReceipt(DatagramPacket packet) {		// FIX INPUT
		





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
		DatagramPacket packet= null;
		// going to make a var to hold the String for the header.. 
		// aswell as for the byte array for the header.. perhaps conversion..
		String headerString ;
		// Header is going to contain sourceAddress/destinationAddress/lengthOfMessage
		String dataDest= (terminal.readString("Send to : "));
		byte []byteDest = dataDest.getBytes();
		String dataMess = (terminal.readString("Message : "));
		byte []byteMess = dataMess.getBytes();
		sendMessage(byteDest, byteMess);

	}
	public synchronized void sendMessage(byte[] dest, byte[] data){





	}


	/**
	 * Test method
	 * 
	 * Sends a packet to a given address
	 */
	public static void main(String[] args) {
		try {					
			Terminal terminal= new Terminal("Client");







			// shouldnt be commented
		//	(new SmartPhone(terminal, DEFAULT_DST_NODE, DEFAULT_DST_PORT, DEFAULT_SRC_PORT)).start();

			// create datagram sockets between all the servers and what they can see..






			terminal.println("Program completed");
		} catch(java.lang.Exception e) {e.printStackTrace();}
	}
	//	Terminal terminal;
	class Task extends TimerTask{
		public void run(){
			try{
						// need to keep on resending the packet..
						terminal.println("TIMER!!!\n");

					// reset back to windowStart
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


	