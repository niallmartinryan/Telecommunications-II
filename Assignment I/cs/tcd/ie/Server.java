package cs.tcd.ie;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import tcdIO.Terminal;

import tcdIO.*;
import javax.swing.Timer;
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
			StringContent content= new StringContent(packet);
			String stringPacket = (content.toString());
			
			char transmission = stringPacket.charAt(stringPacket.length()-1);			// could also use startsWith()
			if(transmission == '1'){
				transmission ='0';
			}
			else{
				transmission ='1';
			}
			stringPacket = stringPacket.substring(0, stringPacket.length()-1);
			terminal.println(stringPacket.toString());
																// convert the new String back into the String content/packet
																// might not have to recreate the packet with the new alter string.
	//		content = stringPacket 
			terminal.print("Transmission: ");
			terminal.println(transmission + " ");
			
			terminal.println("Packet Data:");													//parse that shit first
			terminal.println(stringPacket);		// dont want to print out the starting 1/0
			DatagramPacket response;
			response= (new StringContent("ACK" + transmission)).toDatagramPacket();		// change to ACK1 .. parse in on receipt in Client ..use notify to send on next packet Something will fuck up/wont be called
			response.setSocketAddress(packet.getSocketAddress());
			socket.send(response);
		}
		catch(Exception e) {e.printStackTrace();}
	}

	
	public synchronized void start() throws Exception {
		terminal.println("Waiting for contact");
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
