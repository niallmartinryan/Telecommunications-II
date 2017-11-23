package cs.tcd.ie;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Timer;
import tcdIO.Terminal;
import java.lang.Math;
import java.util.ArrayList;

public class Laptop extends Node {
	// for the smartPhone intialisation
	static final int DEFAULT_SRC_PORT = 50000;
	static final int DEFAULT_DST_PORT = 50001;
	static final String DEFAULT_DST_NODE = "localhost";	

	static final Integer DEFAULT_PORT = 50001;

	int id;

	Terminal terminal;
	RoutingTable routTable;
	Integer[] routers;
	Integer[] smartPhoneDests;
	String exitCodeFrame = "1111111111";
	/*
	 * 
	 */
	Laptop(Terminal terminal, int port, Integer[] availRouters, Integer[] smartPhoneDests) {
		try {
			this.terminal= terminal;
			socket= new DatagramSocket(port);
			this.id = port;
			routTable = new RoutingTable(smartPhoneDests);
			routers = availRouters;		// Think this method is efficient
			this.smartPhoneDests = smartPhoneDests;	// bear in mind these are pointers..

			listener.go();
		}
		catch(java.lang.Exception e) {e.printStackTrace();}
	}
	/*
	 * Assume that incoming packets contain a String and print the string.
	 */
	public synchronized void onReceipt(DatagramPacket packet) {
		try{
			DatagramPacket currentPacket = packet;
			/*
			check packet header for rip..
			convert byte array to header and packet information..

			if rip.. call update table
			- passing in routingTable








			*/

		}
		catch(Exception e) {e.printStackTrace();}
	}

	public synchronized void start() throws Exception {
		terminal.println("Waiting for contact");



		this.wait();
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
	


	public synchronized void updateTable(RoutingTable table){
		
		
		
			



	}
	// definitely need to work on this.
	public synchronized void sendOutRip(){




	}
	
	public synchronized void sendRips(RoutingTable table){
		DatagramPacket packet = null;
		//converting ArrayLists
		// array of byte arrays? xD
		byte [] data = null;
		ArrayList<byte [] > bytes = new ArrayList<byte []>();
		ArrayList [] columns = {table.dest, table.cost, table.nextNode};
		for(int i= 0; i< columns.length; i++){
			// dont know what order this is supposed to be in ---- 2nd array or 1st..
			bytes.add(arrayListToByte(columns[i]));
		}
		//turn the table from 3 arrayLists into byte arrays.. and back again..
		


		for(int i=0; i< routers.length; i++){
			packet= new DatagramPacket(data ,data.length,routers[i]);
			
		}
		
		/*
		for(int i=0;i<availRouters.length;i++){

			availRouters[i]
			// send packets to surrounding routers..

		}
*/
	}
	// will be called from on receipt.. dont know if it has to take in the RoutingTable;
	public synchronized void receivedRip(RoutingTable table){







	}


		// DYLAN SAYS---->>>> PUT THESE METHODS IN ROUTINGTABLE AS PRIVATE METHODS.. true...
		// DOES THIS METHOD EVEN FUCKING WORK???!!!!!!!!___-
	public static byte [] arrayListToByte(ArrayList list){
		// not correct syntax
		byte [] byteArray = new byte[list.size()];
		byte currentByte =0;
		for(int i =0; i< list.size();i++){
			currentByte = (byte) list.get(i);
			byteArray[i] = currentByte;
		}
		return byteArray; 
	}
	public static RoutingTable byteArrayToRoutingTable(byte[][] bytes){
		RoutingTable table;
		// could be 0,1,2 to find the correct length
		
		table = new RoutingTable(byteArrayToArrayList(bytes[0]),
			byteArrayToArrayList(bytes[1]),byteArrayToArrayList(bytes[2]));

		return table;
	}
	public static ArrayList byteArrayToArrayList(byte[] bytes){
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int i=0; i< bytes.length; i++){
			byte currentByte = bytes[i];
			int meh = currentByte;
			list.add(meh);
		}
		return list;
	}

	public static void main(String[] args) {
	// this should be in the try catch....!!!
	//	(new Laptop(terminal, DEFAULT_PORT)).start();
		try {					
			Terminal terminal= new Terminal("Server");
			// create all the routing tables for each server what they can see etc.

			//Intialise values for SurroundRouters
			//Move this topology to a Main or just somewhere else..
			// MOVE THIS SHIT SOMEWHERE ELSE..
			

			
			terminal.println("Program completed");
		} catch(java.lang.Exception e) {e.printStackTrace();}
	}
}
