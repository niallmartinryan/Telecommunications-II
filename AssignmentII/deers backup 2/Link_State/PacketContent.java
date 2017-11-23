
import java.net.DatagramPacket;

public interface PacketContent {
	
	public static byte PACKETLENGTH = 80;
	public static byte HEADERLENGTH = 16;
	public static byte ID_LENGTH = 5;
	public static byte MES_TYPE = 3;
	public static byte ARRAY_LENGTH = 1;	//Length of the length of the arraylist to go into packet. DO NOT DELETE
	
	public static String MES = "MES";
	public static String LSA = "LSA";

	public String toString();
	public DatagramPacket toDatagramPacket();
}
