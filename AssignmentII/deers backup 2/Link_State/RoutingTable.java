
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.util.*;

public class RoutingTable
{
	public int nodeID;
	public boolean isRouter;		//Dunno about this one Boss
	public int[] destinations;	//put the destination in the correct index to reflect the shortest path from this router. There are only two dests
	public int[] costs;
	public int[] nextHops;

	public RoutingTable(int[] destinations, int[] costs, int[] nextHops, int nodeID)
	{
		this.destinations = new int[destinations.length];
		this.costs = new int[costs.length];
		this.nextHops = new int[nextHops.length];
		this.nodeID = nodeID;

		for(int i = 0; i < nextHops.length; i++)
		{
			//this.destinations[i] = 0;	//We'll see about changing this later
			this.costs[i] = costs[i];
			this.nextHops[i] = nextHops[i];
		}		
	}

	public RoutingTable(int[] costs, int[] nextHops, int nodeID)
	{
		this.costs = new int[costs.length];
		this.nextHops = new int[nextHops.length];
		this.nodeID = nodeID;		

		for(int i = 0; i < nextHops.length; i++)
		{
			this.costs[i] = costs[i];
			this.nextHops[i] = nextHops[i];
		}
	}
}