package tcd.lossy;
public class DatagramSocket extends java.net.DatagramSocket {

public void send(DatagramPacket arg0) throws IOException {
if ((Math.random()*100) > noise) {
super.send(arg0);
}
else {
System.out.println("** Packet dropped");
}
}
}
