
import java.util.Random;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Timer;
import tcdIO.Terminal;
import java.lang.Math;
import java.util.ArrayList;

import java.nio.*;
import java.net.*;
import processing.core.PApplet;


public class Laptop extends Node {
	// for the smartPhone intialisation
	static final int DEFAULT_SRC_PORT = 50000;
	static final int DEFAULT_DST_PORT = 50001;
	static final String DEFAULT_DST_NODE = "localhost";
	
	
	//processing stuff
	static final int LAPTOP_WIDTH = 25;
	static final int LAPTOP_HEIGHT = 50;
	LaptopPro LaptopGraphics;

	
	
	
	// Not sure i have to do this..
	InetSocketAddress tempAddress;
	InetSocketAddress[] smartPhoneAdrs;
	InetSocketAddress[] routerAdrs;
	ArrayList<DatagramSocket> sockets;
	static final Integer DEFAULT_PORT = 50001;
	int id;

	// config stuff
	String configRIP = "RIP";
	String configMES = "MES";
	
	Terminal terminal;
	RoutingTable routTable;
	
	//May have to change these to Laptops... will think about it..
	Integer[] routers;
	Integer[] smartPhoneDests;
	String exitCodeFrame = "1111111111";
	/*
	 * 
	 */
	Laptop(Terminal terminal, int port, Integer[] availRouters, Integer[] smartPhoneDests, String dstHost, Network network) {
		try {
			this.terminal= terminal;
			// SHOULDNT BE MAKING SOCKETS HERE I THINK!!!!!!!!!
//			socket= new DatagramSocket(port);
			sockets = new ArrayList<DatagramSocket>();
			this.id = port;
			routTable = new RoutingTable(smartPhoneDests, port);
			routers = availRouters;		// Think this method is efficient
			this.smartPhoneDests = smartPhoneDests;	// bear in mind these are pointers..
//			sockets.add(new DatagramSocket(this.id));
			
			
			// processing intialisation
			Random rand = new Random();
			
			// need something for collisions/spacing
			double randomX= 0;
			double randomY= 0;
			// This will sort out collisions			
			boolean found = false;
			while(!found){
				randomX = rand.nextDouble()*(TelecomsAssignmentII.SIZE_X-200);
				randomY = rand.nextDouble()*(TelecomsAssignmentII.SIZE_Y-200);
				if(network.checkCollisions((int)randomX,(int) randomY, LAPTOP_WIDTH, LAPTOP_HEIGHT)){
					found = true;
				}
			}
			LaptopGraphics = new LaptopPro((int)randomX,
					(int)randomY, LAPTOP_WIDTH, LAPTOP_HEIGHT,id);
			network.laptopGraphicsLength++;
			network.add(this);
			// ADD EDGES ----		THINK HARD ABOUT HOW THE EDGES WILL BE INTIALISED WITHOUT LAPTOPS BEING INTIALISED..first
			
			
			routerAdrs = new InetSocketAddress[availRouters.length];
			smartPhoneAdrs = new InetSocketAddress[smartPhoneDests.length];

			
			System.out.println("WHY HELLO THERE -----"+availRouters.length);
			// InetAddresses
			for(int i=0;i<availRouters.length;i++){
				System.out.println("JESUS FUCKING CHRIST");
				tempAddress = new InetSocketAddress(dstHost,(int)availRouters[i]);
				routerAdrs[i] = tempAddress;
			}
			for(int i=0;i< smartPhoneDests.length;i++){
				tempAddress = new InetSocketAddress(dstHost,(int)smartPhoneDests[i]);
				smartPhoneAdrs [i] =  tempAddress;
			}
			
			
			
			socket = new DatagramSocket(this.id);
/*			
			// sockets
			for(int i=0; i< routers.length; i++){
				// was  - sockets.add(new DatagramSocket(routers[i]));
				sockets.add(new DatagramSocket(this.id));
			}
			for(int i =0; i<smartPhoneDests.length;i++){
				// was - sockets.add(new DatagramSocket(smartPhoneDests[i]));
				sockets.add(new DatagramSocket(this.id));
			}
*/

			

			listener.go();
		}
		catch(java.lang.Exception e) {e.printStackTrace();}
	}
	/*
	 * Assume that incoming packets contain a String and print the string.
	 */
	public synchronized void onReceipt(DatagramPacket packet) {
		try{

			this.LaptopGraphics.receive = true;
			this.LaptopGraphics.active = true;
			
			terminal.println("Received some shit BRUV!!!");
			System.out.println("=============------------------RECEIVING--------------=========");
			DatagramPacket currentPacket = packet;
			StringContent content = new StringContent(currentPacket);
			byte[] buffer = new byte[1024];
			byte[] header = new byte[PacketContent.HEADERLENGTH];
			byte[] config = new byte[PacketContent.CONFIGLENGTH];
			byte[] arrayLength = new byte[3];
			byte[] arrayLengthTemp;
			byte[] payload = new byte[PacketContent.ACKPACKET];
			String configString;
			String arrayLengthString;
			int integerValueArrayLength;
			buffer = packet.getData();
			
			System.arraycopy(buffer, 0, header, 0, header.length);
			System.arraycopy(header, 0, config, 0, config.length);
			
			configString = new String(config, 0, config.length);
			
			if(configString.equalsIgnoreCase( configRIP)){
				// if statement block should be "around" here to differentiate what config it is// mesage
	//			terminal.println("Received : " + new String(buffer, 0, PacketContent.ACKPACKET));
				System.out.println( this.id + " - Received : " + new String(buffer, 0, PacketContent.ACKPACKET));
				
				
	
				//PARSING ERROR HERE... NOT GETTING 2ND DIGIT.. WE ARE ONLY GETTING 1 INSTEAD OF 12
	
				System.arraycopy(header, config.length, arrayLength, 0, arrayLength.length);
				int sizeOffset = arrayLength.length;
				for(int i=0;i< arrayLength.length; i++){
					if(arrayLength[i]== 0){
						sizeOffset--;
					}
				}
				arrayLengthTemp = new byte[sizeOffset];
				for(int i=0;i< arrayLengthTemp.length;i++){
					arrayLengthTemp[i] = arrayLength[i];
				}
				arrayLengthString = new String(arrayLengthTemp);
				integerValueArrayLength = Integer.parseInt(arrayLengthString);
	
				configString = new String(config, 0, config.length);
		//		System.out.println("config- " + configString);
	
	
				System.arraycopy(buffer, PacketContent.HEADERLENGTH, payload, 0, PacketContent.ACKPACKET);
	
				byte [][] arrays = new byte[3][integerValueArrayLength];
	// need to parse the length of the arrays from the header....
	//	this will be parsed later
	//			byte [] temp = new byte[];
				int offset =PacketContent.HEADERLENGTH;
				for(int i=0; i< arrays.length;i++){
					System.arraycopy(buffer, offset, arrays[i], 0 , integerValueArrayLength);
					//watch for overflow
					offset +=  integerValueArrayLength;
				}
				System.out.println("\n");
				ArrayList[] lists = new ArrayList[arrays[1].length];
				for(int i=0; i<arrays.length;i++){
					lists [i] = byteArrayToArrayList(arrays[i]);
				}
				
	
				// maybe I should make a method that takes in bytes arrays instead..
	
				 
				RoutingTable incomingTable = new RoutingTable(lists[0],lists[1], lists[2], id);
				updateTable(incomingTable);
			}
			// if its a message
			else if(configString.equalsIgnoreCase(configMES)){
				System.out.println("TIS A FUCKING MESSAGE BOIS...");
				
				
				
				
				
			}
			// hardcoded.. so change later..
			try {
			    Thread.sleep(2000);                 //1000 milliseconds is one second.
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
			this.LaptopGraphics.receive = false;
			this.LaptopGraphics.active = false;
//			terminal.println("String : " + packet.getData());
			/*
			check packet header for rip..
			convert byte array to header and packet information..

			if rip.. call update table
			- passing in routingTable

								2bytes			1 byte 				1byte
			PACKET--- Header - LENGTH of data - TYPE (RIP, GEN, ) - distance.. 
				  --- DATA





			*/

		}
		catch(Exception e) {e.printStackTrace();}
	}

	public synchronized void start() throws Exception {
		terminal.println("Waiting for contact");
		sendRips(routTable);
		terminal.println(routTable.printTable());
		//this.wait();
	}
	// should this return a routing table?


	/*
	 * 
	 */

	/*
	byte[] result = new byte[list.size()];
	for(int i = 0; i < list.size(); i++) {
    	result[i] = list.get(i).byteValue();
	}


	*/
	// This function should return a new routing table instead of updating perhaps.
	// dont think i need this synchronised..
	


	public synchronized RoutingTable updateTable(RoutingTable table){
		//Iterate through table and use indexOf to find the correct dest in this.routTable
		//then use that to update the current table
		for(int i = 0; i < table.dest.size(); i++){
			int newDest = table.dest.get(i);
			int newDestCost = table.cost.get(i);
			int newNextNode = table.nextNode.get(i);


			if(!this.routTable.dest.contains(newDest)){
				this.routTable.dest.add(newDest);
				this.routTable.cost.add(newDestCost + 1);				// HARDCODED>>>>>>>
				this.routTable.nextNode.add(table.routerID);
				// this.routTable.nextNode.add(currentNextNode);
				this.routTable.columnLength++;
			}
			else{
				int destIndex = this.routTable.dest.indexOf(newDest);
//				int costIndex = this.routTable.cost.indexOf(newDestCost);
				int thisDest = this.routTable.dest.get(destIndex);
				int thisCost = this.routTable.cost.get(destIndex);

				if(newDestCost < thisCost)
				{
					// this doesnt look correct..OISIN!!!!!! fixed ^^
					int alteredCost = 1 + newDestCost;
					this.routTable.cost.set(destIndex, alteredCost);
				}
			}
		}
		this.routTable.printTable();
		return this.routTable;
	}
	// definitely need to work on this.
	public synchronized void sendOutRip(){




	}
	
	public synchronized void sendRips(RoutingTable table){
		try{
			//processing stuff
			this.LaptopGraphics.receive = false;
			this.LaptopGraphics.active = true;
			
			//
			DatagramPacket packet = null;
			//converting ArrayLists
			// array of byte arrays? xD
			ArrayList [] columns = {table.dest, table.cost, table.nextNode};
			int arrayLength = arrayListToByte(columns[0]).length;
			byte [] arrays = new byte[arrayLength*3];
			int packetSize = PacketContent.HEADERLENGTH+ (arrayLength*3);
			byte [] data = new byte[packetSize];
			// get rid of hardcodedNess--
			byte [] header = new byte[PacketContent.HEADERLENGTH];
			// setup everything that is supposed to go in the header



			ArrayList<byte [] > bytes = new ArrayList<byte []>();
			int dataOffset = header.length;
			for(int i= 0; i< columns.length; i++){
				// dont know what order this is supposed to be in ---- 2nd array or 1st..
				System.arraycopy(arrayListToByte(columns[i]), 0, data, dataOffset, arrayListToByte(columns[i]).length);
// GET RID OF		data =(arrayListToByte(columns[i]));
				// for(int j=0; j< routers.length; j++){
				// 	packet= new DatagramPacket(data ,data.length,routerAdrs[j]);
				// 	socket.send(packet);
				// }


				dataOffset+= arrayLength;
			}
			for(int j=0; j< routers.length; j++){

				byte [] config = "RIP".getBytes();
				System.arraycopy(config, 0, header, 0 , config.length);
				// might have to change the start of where the array is copied
				byte [] sizeofLength = Integer.toString(arrayLength).getBytes();
				System.arraycopy(sizeofLength, 0, header,config.length, sizeofLength.length );
				System.arraycopy(header, 0, data, 0, header.length);
				System.out.println("Data  : " + new String(data, 0, data.length));

				if( routerAdrs==null){
					System.out.println("WTF IS THIS SHIT");
				}
				
				packet= new DatagramPacket(data ,data.length,routerAdrs[j]);
				socket.send(packet);
			}
			
			// processing stuff -- also hardcoded so change this..
			try {
			    Thread.sleep(2000);                 //1000 milliseconds is one second.
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
			this.LaptopGraphics.receive = true;
			this.LaptopGraphics.active = false;
			//turn the table from 3 arrayLists into byte arrays.. and back again..

			/*
			for(int i=0;i<availRouters.length;i++){

				availRouters[i]
				// send packets to surrounding routers..

			}
	*/}
			catch(java.lang.Exception e) {e.printStackTrace();}
		
	}
	// will be called from on receipt.. dont know if it has to take in the RoutingTable;
	public synchronized void receivedRip(RoutingTable table){






	}


		// DYLAN SAYS---->>>> PUT THESE METHODS IN ROUTINGTABLE AS PRIVATE METHODS.. true...
		// DOES THIS METHOD EVEN FUCKING WORK???!!!!!!!!___-
	public static byte [] arrayListToByte(ArrayList list){

		int[] listData = buildIntArray(list);

		ByteBuffer byteBuffer = ByteBuffer.allocate(listData.length * 4);        
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        intBuffer.put(listData);

       byte[] array = byteBuffer.array();
        // for(int i=0;i<array.length;i++){
        // 	System.out.print(array[i]);
        // }

		// not correct syntax
// 		byte [] byteArray = new byte[list.size()];
// 		byte [] currentByte =null;
// 		for(int i =0; i< list.size();i++){
// 			currentByte = (byte[]) list.get(i);			// cannot cast from integer to byte..
// //			byteArray[i] = currentByte;
// 		}
		return array; 
	}
	public static RoutingTable byteArrayToRoutingTable(byte[][] bytes, int id){

		RoutingTable table;
		// could be 0,1,2 to find the correct length
		table = new RoutingTable(byteArrayToArrayList(bytes[0]),byteArrayToArrayList(bytes[1]),byteArrayToArrayList(bytes[2]), id);

		return table;
	}
	public static ArrayList byteArrayToArrayList(byte[] bytes){
		boolean CLZ = true;
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int i=0; i< bytes.length; i+=4){
			// WILL HAVE TO CHANGE THIS LATER I THINK...
			byte []fourBytes = {bytes[i], bytes[i+1], bytes[i+2], bytes[i+3]};
			list.add(toInt(fourBytes,0));
		}
		return list;
	}
	// edit this LATER 			Dont think i even use this..
	public static int toInt(byte[] bytes, int offset) {
	  int ret = 0;
	  for (int i=0; i<4 && i+offset<bytes.length; i++) {
	    ret <<= 8;
	    ret |= (int)bytes[i] & 0xFF;
	  }
	  return ret;
	}

	private static int[] buildIntArray(ArrayList<Integer> list) {
    int[] ints = new int[list.size()];
    int i = 0;
    for (Integer n : list) {
        ints[i++] = n;
    }
    return ints;
	}
	
	
	// just for processing stuff
	public class LaptopPro {
		boolean active;
		boolean receive;
		Color sending = new Color(6,253,14);			//green
		Color receiving = new Color(255,4,4);				//red
		Color deactiveColor = new Color(50,50,50);			//grey
		Color currentColor;			
		  
		int height;
		int width;
		int xpos;
		int ypos;


		// might change these to doubles.	dont know if I have to take in id as param
		LaptopPro(int xpos, int ypos, int height,int width, int id){
			active = false;
			receive = false;
			this.xpos =xpos;
			this.ypos =ypos;
			this.width =width;
			this.height = height;
		}
		public void drawLaptops(PApplet applet){
			currentColor= (active)? (receive)? receiving : sending :deactiveColor;
			applet.fill(currentColor.red,currentColor.green,currentColor.blue);	// nice little statement
			applet.rect(xpos, ypos, height,width);
		}
		
	}

}







