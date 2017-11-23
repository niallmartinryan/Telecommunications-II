package cs.tcd.ie;

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;

import tcdIO.*;

public class StringContent implements PacketContent {

	String stringPayload;
	String stringHeader;   											
	public StringContent(DatagramPacket packet) {
		byte[] header;												
		byte[] payload;
		byte[] buffer;
		
		buffer = packet.getData();
		header = new byte [HEADERLENGTH];
		System.arraycopy(buffer, 0 , header, 0 , HEADERLENGTH);		// copying the Header into byte [] header
		payload = new byte[packet.getLength()-HEADERLENGTH];
		System.arraycopy(buffer, HEADERLENGTH, payload, 0, packet.getLength()-HEADERLENGTH);

		stringHeader = new String(header);
		stringPayload = new String(payload);
	}
	public StringContent(String string) {							
		this.stringPayload = string;
	}
	public StringContent(String string, String head){				
		this.stringPayload = string;								
		this.stringHeader = head;
	}	
	public String toString() {
		return stringPayload;
	}
	public String headerToString(){

		return stringHeader;
	}	

	public DatagramPacket toDatagramPacket() {
		DatagramPacket packet = null;
		byte[] buffer= null;
		byte[] payload= null;
		byte[] header= null;

		try {
			payload= stringPayload.getBytes();
			header = new byte[HEADERLENGTH];
			header= stringHeader.getBytes();
			buffer= new byte[header.length+payload.length];
			System.arraycopy(header, 0, buffer, 0, header.length);
			System.arraycopy(payload, 0, buffer, header.length, payload.length);
			packet= new DatagramPacket(buffer, buffer.length);
		}
		catch(Exception e) {e.printStackTrace();}
		return packet;
	}
}
