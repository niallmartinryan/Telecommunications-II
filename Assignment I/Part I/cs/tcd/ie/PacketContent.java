package cs.tcd.ie;

import java.net.DatagramPacket;						//look up class for this.

public interface PacketContent {
	
	public static byte HEADERLENGTH = 10;
	
	public String toString();
	public DatagramPacket toDatagramPacket();
}
