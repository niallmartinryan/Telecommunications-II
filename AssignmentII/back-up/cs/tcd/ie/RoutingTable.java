package cs.tcd.ie;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Timer;
import tcdIO.Terminal;
import java.lang.Math;
import java.util.ArrayList;
public class RoutingTable{
	public ArrayList<Integer> dest;
	public ArrayList<Integer> cost;
	// next node.. might have to be an INTEGER!!!!!!!!
	public ArrayList<Integer> nextNode;
	public int columnLength = 3;
	public int rowLength = 0;
	// only need to take in dests.. because cost will be 0
	RoutingTable(Integer[] dests){

		dest = new ArrayList<Integer>();
		cost = new ArrayList<Integer>();
		nextNode = new ArrayList<Integer>();
		// should probably check instead.. dests[0] == null or something like that..
		if(dests == null){
			
		}
		else{
			for(int i=0; i<dests.length; i++){
				this.dest.add(dests[i]);
				this.cost.add(1);
				this.nextNode.add(0);
				rowLength++;
			}
		}
	}
	RoutingTable(ArrayList<Integer> dest, ArrayList<Integer> cost, ArrayList<Integer> nextNode){
		this.dest = new ArrayList<Integer>();
		this.cost = new ArrayList<Integer>();
		this.nextNode = new ArrayList<Integer>();
		// need and if block inscase the input happens to be null
		// perhaps check that each of the input arrays has the same 
		// Not sure this will work correctly.. will find out.
		for(int i=0; i< dest.size(); i++){
			this.dest.add(dest.get(i));
			this.cost.add(cost.get(i));
			this.nextNode.add(nextNode.get(i));
		}

	}
	public void printTable(){
		System.out.print("destinations	-	cost	-	nextNode\n");
		for(int i =0; i < dest.size(); i++){
			System.out.print(dest.get(i)+ "		"+ cost.get(i) + "		" + nextNode.get(i)+"\n");
		}
		//Dont think i have to return..
		return;
	}

}