package cs.tcd.ie;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Timer;
import tcdIO.Terminal;

public class Server extends Node {
	static final int DEFAULT_PORT = 50001;

	Terminal terminal;
	int noise = 40;
	
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
			char []inputSequenceNumber = new char[PacketContent.HEADERLENGTH];							// this might be better off as an arrayList
			StringContent content= new StringContent(packet);
			byte[] buffer = content.toString().getBytes();
			byte[] headerBytes = new byte[PacketContent.HEADERLENGTH];
			String headerString = content.headerToString();
			for(int i =0 ; i < headerString.length(); i++){
				if(headerString.charAt(i) != '\0' ){
					inputSequenceNumber[i] = headerString.charAt(i);
				}
				inputSequenceNumber[i] = '/';
			}
			String currentString =null;
			terminal.println("Header : "+ headerString + "\n");
			if(headerString.charAt(1) != '\0'){
				currentString = ("" +headerString.charAt(0) + headerString.charAt(1));
			}
			else{
				currentString = ("" +headerString.charAt(0));
			}
			int sequenceNumber = Integer.parseInt(currentString);
			if(sequenceNumber == 1){
				sequenceNumber = 0;
			}
			else{
				sequenceNumber = 1;
			}
			terminal.println( "sequenceNumber as int : " + sequenceNumber + "\n" );
			terminal.println("payload: "+content.toString() + "\n");

			DatagramPacket response;
			if(headerString.charAt(1) != '\0'){
				response= (new StringContent("ACK" + sequenceNumber, headerString).toDatagramPacket());
			}
			else{
				response= (new StringContent("ACK" + sequenceNumber, headerString).toDatagramPacket());
			}
			response.setSocketAddress(packet.getSocketAddress());
			if ((Math.random()*100) > noise) {  // the packet will be send depending on a random number between 0 and 100
				socket.send(response);
			}
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
