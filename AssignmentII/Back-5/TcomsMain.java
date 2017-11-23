
//package cs.tcd.ie;
import processing.core.PApplet;
import java.util.*;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;											//trying this for the moment

import tcdIO.*;



public class TcomsMain{

	Terminal terminal;
	static final Integer PORT_A = 20000;
	static final Integer PORT_B = 20001;
	static final Integer PORT_C = 20002;
	static final Integer PORT_D = 20003;
	static final Integer PORT_E = 20004;
	static final Integer PORT_F = 20005;
	

//	static final int DEFAULT_SRC_PORT = 50000;

	static final String DEFAULT_DST_NODE = "localhost";

	static final String PORTA = "A";
	static final String PORTB = "B";
	static final String PORTC = "C";
	static final String PORTD = "D";
	static final String PORTE = "E";
	static final String PORTF = "F";


	static final int NET_18 = 30000;
	static final int NET_15 = 30001;
	static final int NET_10 = 30002;
	static final int NET_28 = 30003;
	static final int NET_7 = 30004;
	static final int NET_2 = 30005;
	static final int NET_21 = 30006;
	static final int NET_11 = 30007;
	static final int NET_5 = 30008;

	static final int SIZE_OF_DESTS = 9;
	
	static Network network;
	

/*
	public synchronized void onReceipt(DatagramPacket packet) {
		try{


		}
		catch(Exception e) {e.printStackTrace();}
	}

*/
	public static void main(String []args, Network network){
		// will have to change these names to suit each Router/Smartphone
		
		try{
			
			

			int [] dests = {
				NET_18,NET_15,NET_10,NET_28,
				NET_7,NET_2,NET_21,NET_11,NET_5
			};
			Integer[] routersA = {PORT_B, PORT_C};
			Integer[] routersB = {PORT_A, PORT_D};
			Integer[] routersC = {PORT_A, PORT_D, PORT_E};
			Integer[] routersD = {PORT_B, PORT_C};
			Integer[] routersE = {PORT_C, PORT_F};
			Integer[] routersF = {PORT_E};

			//CreatingRoutingTables Intial ones
	//			int[] smartPhones = {}
			// Terminal test = new Terminal("smartPhone");
			// SmartPhone phone = new SmartPhone(test, DEFAULT_DST_NODE, DEFAULT_DST_PORT, DEFAULT_SRC_PORT, NET_18 );
			Integer[] smartPhonesA = {NET_18};
			Integer[] smartPhonesB = {NET_15,NET_28};
			Integer[] smartPhonesC = {NET_10};
			Integer[] smartPhonesD = {NET_7};
			Integer[] smartPhonesE = {NET_2, NET_21};
			Integer[] smartPhonesF = {NET_11,NET_5};
			//intialise Laptops
			//  THIS COULD BE A ARRAY OF LAPTOPS NIALL.. YOU SPA...
			Laptop [] laptops = {
				new Laptop(new Terminal("Router" + PORTA), PORT_A,routersA ,smartPhonesA,DEFAULT_DST_NODE, network ),
				new Laptop(new Terminal("Router" + PORTB), PORT_B,routersB ,smartPhonesB,DEFAULT_DST_NODE, network ),
				new Laptop(new Terminal("Router" + PORTC), PORT_C,routersC ,smartPhonesC,DEFAULT_DST_NODE, network ),
				new Laptop(new Terminal("Router" + PORTD), PORT_D,routersD ,smartPhonesD,DEFAULT_DST_NODE, network ),
				new Laptop(new Terminal("Router" + PORTE), PORT_E,routersE ,smartPhonesE,DEFAULT_DST_NODE, network ),
				new Laptop(new Terminal("Router" + PORTF), PORT_F,routersF ,smartPhonesF,DEFAULT_DST_NODE, network )
			};

			int [][] portsForSP = {{PORT_A},{PORT_B},{PORT_B},{PORT_C},
				{PORT_D},{PORT_E},{PORT_E},{PORT_E},{PORT_F},{PORT_F}};
			// NEED TO CHANGE END OF THESE INTIALISATIONS.. AS PORT_A ETC.. NEEDS TO IN AN ARRAY OR SOME SHIT..
			SmartPhone [] smartPhones = new SmartPhone[SIZE_OF_DESTS];
			for(int i=0; i< smartPhones.length;i++){
				smartPhones[i] = new SmartPhone(new Terminal("Smartphone" + dests[i]),DEFAULT_DST_NODE,dests[i], dests, portsForSP[i], network);
			}
			// this doesnt work.. because it doesnt make the rest of the laptops.. just gets stuck in start().... hmmmmmmm...
			// FML!!!!

			
			// here We need to initialise all the edges..
			for(int k=0; k< network.laptopCount;k++){
				for(int i=0; i<network.laptops[k].routers.length;i++){
					Edge toAdd = new Edge(network.getLaptop(network.laptops[k].routers[i]),network.laptops[k] );

						if(network.checkEdges(toAdd)){
							network.add(toAdd);
						}
				}
			}
			// same for smartphoneConnections
			for(int k=0 ; k < network.smartPhoneCount;k++){
				for(int i=0 ; i< network.smartPhones[k].availRouters.length;i++){
					Edge toAdd = new Edge(network.getLaptop(network.smartPhones[k].availRouters[i]),network.smartPhones[k] );
					
					if(network.checkEdges(toAdd)){
						network.add(toAdd);
					}
				}
			}

			// starts bullshit needs to be in separate method...

			boolean topologyComplete = false;
			while(!topologyComplete){
				for(int i=0; i<laptops.length;i++){
					
					laptops[i].start();
				}
				System.out.println(topologyComplete = topologyCheck(laptops,SIZE_OF_DESTS));
			}

			network.printEdges();
			
			
			// Once this is complete, We can begin sending random messages from 
			// random smartphones to other random smartphones..
			System.out.println("WHAT the actual fuck is this...--++++++++++");
			// smartPhone stuff
			Random rand = new Random();
			int randomDoub =(int) rand.nextDouble()*dests.length;
//			for(int i=0; i< smartPhones.length; i++){
				
			smartPhones[0].sendMessage(dests[randomDoub],smartPhones[0].getMessage() );
//			}
			
			
			
			
			
			
			
			// smartPhone stuff
			
			
			//Creating a test method for update table to check if it works
			// RoutingTable testUpdate = new RoutingTable(smartPhonesA, a.id);
			// test.println(testUpdate.printTable());
			// test.println("hi\n");
			// testUpdate = a.updateTable(b.routTable);
			// test.println(a.routTable.printTable());

			// ArrayList<Integer> ints = new ArrayList<Integer>();
			// for(int i=0;i<10;i++){		
			// 	ints.add(i+300000);
			// }
			// printArrayList(ints,test);
			// byte [] stuff = Laptop.arrayListToByte(ints);
			// ArrayList<Integer> otherStuff = Laptop.byteArrayToArrayList(stuff);
			// printArrayList(otherStuff,test);

		}catch(java.lang.Exception e) {e.printStackTrace();}
	}
	

	public static void printArrayList(ArrayList<Integer> array, Terminal test){
		test.println("ArrayList - \n");
		for(int i=0;i< array.size();i++){

			test.println("" +array.get(i));
		}
		test.println("\n");
	}

	// checks if the topology is complete.
	public static boolean topologyCheck( Laptop [] laptops, int size){
		boolean complete = true;
		for(int i=0; i< laptops.length;i++){
			if(laptops[i].routTable.dest.size() != size){
				return false;
			}
		}
		return complete; // or true in this case
	}


}