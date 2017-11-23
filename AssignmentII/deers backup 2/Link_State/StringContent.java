
import java.net.DatagramPacket;
import java.net.InetSocketAddress;

public class StringContent implements PacketContent {
	String string;
	InetSocketAddress dstAddress;
	
	public StringContent(DatagramPacket packet) {
		byte[] payload;
		byte[] buffer;
		
		buffer= packet.getData();
		payload= new byte[packet.getLength()];
		System.arraycopy(buffer, 0, payload, 0, packet.getLength());
		
		string = new String(payload);
	}
	
	public StringContent(String string) {
		this.string = string;
	}
	
	public String toString() {
		return string;
	}

	public DatagramPacket toDatagramPacket() {
		DatagramPacket packet= null;
		byte[] buffer= null;
		byte[] payload= null;
		byte[] header= null;

		try {
			payload= string.getBytes();
			header= new byte[HEADERLENGTH];
			buffer= new byte[header.length+payload.length];
			packet= new DatagramPacket(payload, buffer.length);
		}
		catch(Exception e) {e.printStackTrace();}
		return packet;
	}
}
