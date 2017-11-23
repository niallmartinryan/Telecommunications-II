package cs.tcd.ie;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Timer;
import tcdIO.Terminal;
import java.lang.Math;

public class Server extends Node {
	static final int DEFAULT_PORT = 50001;
	static final int noise = 10;
	Terminal terminal;
	String exitCodeFrame = "1111111111";
	int currentSequenceNumber =0;
	char frame [] = new char[16];
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
	/*
	 * Assume that incoming packets contain a String and print the string.
	 */
	public void onReceipt(DatagramPacket packet) {
		try {																	//
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
				frameSequenceNumber[i] = '\0';
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
			if(headerString.equals(exitCodeFrame)){					// dont know ^^


			}
			if(frameCode != currentSequenceNumber){
																				// wait for timeout atm.. but need to send a NAK
				DatagramPacket response;
				terminal.println("frameCode : " + frameCode + "\n");
				terminal.println("currentSequenceNumber : " + currentSequenceNumber + "\n");
				terminal.println("Incorrect sequence number\n");
				if(headerString.charAt(1) != '\0'){
					response= (new StringContent("NAK" + currentSequenceNumber, headerString).toDatagramPacket());
				}
				else{
					response= (new StringContent("NAK" + currentSequenceNumber, headerString).toDatagramPacket());
				}
				response.setSocketAddress(packet.getSocketAddress());
				if ((Math.random()*100) > noise) {
					socket.send(response);
				}
				else{
					terminal.println("NAK dropped\n");
				}
			}
			else{
				terminal.println( "frameCode as int : " + frameCode + "\n" );
				frameCode++;
				terminal.println("payload: '"+content.toString() + "'\n");
				frame[frameCode-1] = content.toString().charAt(0);
				terminal.println("char passed :" + frame[frameCode-1] );

				DatagramPacket response;
				if(headerString.charAt(1) != '\0'){
					response= (new StringContent("ACK" + frameCode, headerString).toDatagramPacket());
				}
				else{
					response= (new StringContent("ACK" + frameCode, headerString).toDatagramPacket());
				}
				response.setSocketAddress(packet.getSocketAddress());
				currentSequenceNumber++;
				for(int j =0 ; j<1000000000; j++){}
				if ((Math.random()*100) > noise) {	
					socket.send(response);
				}
				else{
					terminal.println("Ack dropped\n");
				}
			}
			if(frameCode==16){
				currentSequenceNumber=0;
			}
		
		}
		catch(Exception e) {e.printStackTrace();}
	}

	public synchronized void start() throws Exception {
		terminal.println("Waiting for contact");
		currentSequenceNumber = 0;
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
