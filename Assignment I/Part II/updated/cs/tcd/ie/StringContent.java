package cs.tcd.ie;

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;

import tcdIO.*;

public class StringContent implements PacketContent {
	Terminal terminal;

	String stringPayload;
	String stringHeader;   											// this might be a good implementation
	public StringContent(DatagramPacket packet) {
		terminal = new Terminal("StringContent");
		byte[] header;												// will have to parse the header from the packet data i.e.Buffer
		byte[] payload;
		byte[] buffer;
		
		buffer = packet.getData();
		header = new byte [HEADERLENGTH];
		System.arraycopy(buffer, 0 , header, 0 , HEADERLENGTH);		// trying to parse the data from the buffer, copying the Header into byte [] header
		// terminal.println("headerCheck : ");
		
		// stringHeader = new String(header);
		// for(int i =0; i< header.length; i++){
		// 	terminal.println(Character.toString(stringHeader.charAt(i)));
		// }
		// terminal.println("HeaderCheck as string : ");				// test printing out stuff.. PLZ REMOVE BEFORE SUBMISSION
		// terminal.println(stringHeader);
		payload = new byte[packet.getLength()-HEADERLENGTH];
		System.arraycopy(buffer, HEADERLENGTH, payload, 0, packet.getLength()-HEADERLENGTH);

		terminal.println("String header: " +( stringHeader = new String(header))+ "\n");
		terminal.println("String payload: " +( stringPayload = new String(payload))+ "\n");
	}
	public StringContent(String string) {							// need to add another toString method to accomodate the stringHeader
		this.stringPayload = string;
	}
	public StringContent(String string, String head){				// trying out new methods for new String content class..
		this.stringPayload = string;								// i.e. See what happens dog..
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
															// need to change this is  = stringHeader.getBytes();
			buffer= new byte[header.length+payload.length];
			System.arraycopy(header, 0, buffer, 0, header.length);
			System.arraycopy(payload, 0, buffer, header.length, payload.length);
			packet= new DatagramPacket(buffer, buffer.length);
		}
		catch(Exception e) {e.printStackTrace();}
		return packet;
	}
}
