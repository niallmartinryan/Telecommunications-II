package cs.tcd.ie;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Timer;
import tcdIO.Terminal;

public class Server extends Node {
	static final int DEFAULT_PORT = 50001;

	Terminal terminal;
	
	/*
	 * 
	 */
	Server(Terminal terminal, int port) {
		try {
			this.terminal= terminal;
			socket= new DatagramSocket(port);
			listener.go();
		}
		catch(java.lang.Exception e) {e.printStackTrace();}
	}

	/**
	 * Assume that incoming packets contain a String and print the string.
	 */
	public void onReceipt(DatagramPacket packet) {
		try {
			char []frameSequenceNumber = new char[PacketContent.HEADERLENGTH];							// this might be better off as an arrayList
			StringContent content= new StringContent(packet);
			byte[] buffer = content.toString().getBytes();
			byte[] headerBytes = new byte[PacketContent.HEADERLENGTH];
			String headerString = content.headerToString();
			for(int i =0 ; i < headerString.length(); i++){
				if(headerString.charAt(i) != '\0' ){
					terminal.println(" "+ i + " : "+ headerString.charAt(i)+ "\n");
					frameSequenceNumber[i] = headerString.charAt(i);
				}
				frameSequenceNumber[i] = '/';
			}
			String currentString =null;
			terminal.println("Header : "+ headerString + "\n");
			if(headerString.charAt(1) != '\0'){
				currentString = ("" +headerString.charAt(0) + headerString.charAt(1));
			}
			else{
				currentString = ("" +headerString.charAt(0));
			}
			int frameCode = Integer.parseInt(currentString);
			if(frameCode == 1){
				frameCode = 0;
			}
			else{
				frameCode = 1;
			}
			terminal.println( "frameCode as int : " + frameCode + "\n" );
//			frameCode++;
										// one way of going about it I guess.
//																							could also just grab the first byte...
//			int frameCode = Integer.parseInt(header);									// error here for numberformat exception
														// do i convert it to a integer or leave it as a string
																	// attempting to parse the header from payload
// 																								and then the frameSequenceNumber;
//			System.arraycopy();																// copy header contents of payload to header
//																							 might refer to it as FSN
			terminal.println("payload: "+content.toString() + "\n");

			DatagramPacket response;
			if(headerString.charAt(1) != '\0'){
				response= (new StringContent("ACK" + frameCode, headerString).toDatagramPacket());
			}
			else{
				response= (new StringContent("ACK" + frameCode, headerString).toDatagramPacket());
			}
			response.setSocketAddress(packet.getSocketAddress());
			socket.send(response);
		}
		catch(Exception e) {e.printStackTrace();}
	}

	
	public synchronized void start() throws Exception {
		terminal.println("Waiting for contact\n");
		this.wait();
	}
	
	/*
	 * 
	 */
	public static void main(String[] args) {
		try {					
			Terminal terminal= new Terminal("Server");
			(new Server(terminal, DEFAULT_PORT)).start();
			terminal.println("Program completed");
		} catch(java.lang.Exception e) {e.printStackTrace();}
	}
}
