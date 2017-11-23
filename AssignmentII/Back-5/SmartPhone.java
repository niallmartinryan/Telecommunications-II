
import java.util.Random;
import java.util.*;
import processing.core.PApplet;
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
	
//	//processing Stuff..
	static final int SMARTPHONE_WIDTH = 25;
	static final int SMARTPHONE_HEIGHT = 25;
	
	SmartPhonePro smartPhoneGraphics;
	
	//

	
	// config stuff
	
	String configMES = "MES";
	String configRIP = "RIP";
	
	//

	static final String DEFAULT_DST_NODE = "localhost";	
	static final int  MAX_PACKET_LENGTH = 64;														
	static final int noise = 10;						// used
	Terminal terminal;
	InetSocketAddress dstAddress;
	static final int timeOut = 20000;
	Timer timer = new Timer();
	Timer receive = new Timer();
	DatagramPacket currentPacket;
	int id;
	int [] availRouters;
	int [] dests;

	/*
	 * Constructor
	 * 	 
	 * Attempts to create socket at given port and create an InetSocketAddress for the destinations
	 */
	SmartPhone(Terminal terminal, String dstHost, int srcPort, int [] dests, int [] availRouters, Network network) {
		try {
			this.terminal= terminal;
				// dont need sockets between smartphones... need between avail routers!!!!
//			dstAddress= new InetSocketAddress(dstHost, dstPort);

			this.id= srcPort;
			this.dests = dests;
			this.availRouters = availRouters;
			
			// processing stuff
			
			// need to deal with collisions
			
			Random rand = new Random();
			
			double randomX = 0;
			double randomY = 0;
			boolean found= false;
			while(!found){
				//MAKE SURE TO ADJUST BECAUSE OF ITS TOP LEFT CORNER OF THE OBJECT...
				randomX =  rand.nextDouble()*(TelecomsAssignmentII.SIZE_X-200);
				randomY =  rand.nextDouble()*(TelecomsAssignmentII.SIZE_Y-200);
				if(network.checkCollisions((int)randomX,(int) randomY, SMARTPHONE_WIDTH, SMARTPHONE_HEIGHT)){
					found =true;
				}
			}
			
			smartPhoneGraphics = new SmartPhonePro((int)randomX,(int)randomY,
					SMARTPHONE_WIDTH, SMARTPHONE_HEIGHT,id);
			network.smartPhoneGraphicsLength++;
			network.add(this);
			
			//Add EDGES ---
			
			
			
			socket= new DatagramSocket(srcPort);
			listener.go();
		}
		catch(java.lang.Exception e) {e.printStackTrace();}
	}

	
	/**
	 * Assume that incoming packets contain a String and print the string.
	 */
	public synchronized void onReceipt(DatagramPacket packet) {		// FIX INPUT
		this.smartPhoneGraphics.receiving = true;
		this.smartPhoneGraphics.active = true;
		
		terminal.println("Got some shit bruv...");

		DatagramPacket currentPacket = packet;
		StringContent content = new StringContent(currentPacket);
		byte[] buffer = new byte[1024];
		byte[] header = new byte[PacketContent.HEADERLENGTH];
		byte[] config = new byte[PacketContent.CONFIGLENGTH];
		
		byte[] payload = new byte[PacketContent.ACKPACKET];
		String configString;
		
		buffer = packet.getData();
		
		System.arraycopy(buffer, 0, header, 0, header.length);
		System.arraycopy(header, 0, config, 0, config.length);
		
		configString = new String(config, 0, config.length);
		if(configString.equalsIgnoreCase(configMES)){
			// need to just parse the message now perhaps send a response..
		}

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
		// convert to int ^
		


	}
	public synchronized String getMessage(){
 
		return (terminal.readString("Message : "));
		
	}
	public synchronized void sendMessage(int dest, String message){
		
		try{
			System.out.println("message - "+ message);
			// add something that requests a response from the dest address...
			byte [] buffer = new byte[PacketContent.ACKPACKET];
			byte [] header = new byte[PacketContent.HEADERLENGTH];
			byte [] config = new byte[PacketContent.CONFIGLENGTH];
			byte [] length = new byte[PacketContent.ARRAYLENGTH];
			byte [] destination = new byte[PacketContent.DESTINATIONLENGTH];
			byte [] messageInBytes = message.getBytes();
			config = "MES".getBytes();
			int payloadLength = buffer.length-header.length;
			System.arraycopy(messageInBytes, 0, buffer, header.length, messageInBytes.length);
			
			System.out.println("Message in buffer =" + new String(buffer, header.length, payloadLength));
			
			config = configMES.getBytes();
			
			// generate a number at random to select the dest
			
			destination = Integer.toString(dest).getBytes();	// why do I have to do this..
			System.arraycopy(destination, 0, header, config.length+length.length, destination.length);
			System.arraycopy(config, 0, header, 0, config.length);
			System.arraycopy(header, 0, buffer, 0, header.length);
			System.out.println("DATA TO SEND FROM SMARTPHONE - buffer" + new String(buffer, 0, buffer.length));
			// CAN SMARTPHONES SEE THE TOPOLOGY.... might not pick correct router or something..
			
			
			
			
			
			
			
		}catch(java.lang.Exception e) {e.printStackTrace();}
			
	}

	public synchronized void sendMessagesAtRandom(){



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
	
	
	
	
	
	
	//processing stuff---
	public class SmartPhonePro{
		
		boolean active;
		boolean receiving;
		Color receive = new Color(252,4,4);	// red
		Color send = new Color(4,255,4);
		Color deactiveColor = new Color(114,73,1);	// brown
		Color currentColor; 
		
		int height;
		int width;
		int xpos;
		int ypos;
		
		
		
		SmartPhonePro(int xpos, int ypos, int width,int height, int id){
			active = false;
			this.xpos = xpos;
			this.ypos = ypos;
			this.width = width;
			this.height = height;
			
		}
		
		public void drawSmartPhones(PApplet applet){
			currentColor = (active)? (receiving)? receive : send : deactiveColor;	// looks sleightly complicated..
			applet.fill(currentColor.red,currentColor.green, currentColor.blue);
			applet.ellipse(xpos, ypos, height, width);
		}
		
		
		
		
	}
	
	
	
}


	