package cs.tcd.ie;

import java.util.*;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;											//trying this for the moment

import java.util.ArrayList;

import tcdIO.*;



public class TcomsMain{

	Terminal terminal;
	static final Integer PORT_A = 20000;
	static final Integer PORT_B = 20001;
	static final Integer PORT_C = 20002;
	static final Integer PORT_D = 20003;
	static final Integer PORT_E = 20004;
	static final Integer PORT_F = 20005;
	

	static final int DEFAULT_SRC_PORT = 50000;
	static final int DEFAULT_DST_PORT = 50001;

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
	TcomsMain(){


	}


/*
	public synchronized void onReceipt(DatagramPacket packet) {
		try{


		}
		catch(Exception e) {e.printStackTrace();}
	}

*/
	public static void main(String []args){
		// will have to change these names to suit each Router/Smartphone
		Terminal terminal = new Terminal("Main");

		Integer[] routersA = {PORT_B, PORT_C};
		Integer[] routersB = {PORT_A, PORT_D};
		Integer[] routersC = {PORT_A, PORT_D, PORT_E};
		Integer[] routersD = {PORT_B, PORT_C};
		Integer[] routersE = {PORT_C, PORT_F};
		Integer[] routersF = {PORT_E};

		//CreatingRoutingTables Intial ones
//			int[] smartPhones = {}
		Terminal test = new Terminal("smartPhone");
		SmartPhone phone = new SmartPhone(test, DEFAULT_DST_NODE, DEFAULT_DST_PORT, DEFAULT_SRC_PORT, NET_18);
		Integer[] smartPhonesA = {NET_18};
		Integer[] smartPhonesB = {NET_15,NET_28};
		Integer[] smartPhonesC = {NET_10};
		Integer[] smartPhonesD = {NET_7};
		Integer[] smartPhonesE = {NET_2, NET_21};
		Integer[] smartPhonesF = {NET_11,NET_5};
		//intialise Laptops
		new Laptop(terminal, PORT_A,routersA ,smartPhonesA, DEFAULT_DST_NODE );
		new Laptop(terminal, PORT_B,routersB ,smartPhonesB,DEFAULT_DST_NODE );
		new Laptop(terminal, PORT_C,routersC ,smartPhonesC,DEFAULT_DST_NODE  );
		new Laptop(terminal, PORT_D,routersD ,smartPhonesD,DEFAULT_DST_NODE  );
		new Laptop(terminal, PORT_E,routersE ,smartPhonesE,DEFAULT_DST_NODE  );
		new Laptop(terminal, PORT_F,routersF ,smartPhonesF,DEFAULT_DST_NODE  );


	}



}