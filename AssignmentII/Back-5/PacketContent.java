
//package cs.tcd.ie;
import processing.core.PApplet;
import java.net.DatagramPacket;						//look up class for this.

public interface PacketContent {

	// change ackPacket to something more informative
	public static byte ACKPACKET = 80;
	public static byte HEADERLENGTH = 11;
	public static byte CONFIGLENGTH = 3;
	public static byte ARRAYLENGTH = 3;
	public static byte DESTINATIONLENGTH = 4;
	
	public String toString();
	public DatagramPacket toDatagramPacket();
}
