
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import tcdIO.Terminal;
import java.lang.Math;
import java.util.ArrayList;

import java.nio.*;
import java.net.*;

public class Router extends Node {

	static final int ENDPOINT_1 = 50000;
	static final int ENDPOINT_2 = 50001;
	static final String HOST = "localhost";

	Terminal terminal;
	RoutingTable table;
	ArrayList<RoutingTable> routingTables;	//Contains information about the entire network!
	InetSocketAddress tempAddress;
	int id;

	/*
	 * 
	 */
	Router(Terminal terminal, int id, int[] costs, int[] nextHops, int[] destination) {
		try {
			this.terminal= terminal;
			this.id = id;
			table = new RoutingTable(destination, costs, nextHops, id);
			routingTables = new ArrayList<RoutingTable>();
			routingTables.add(table);
			terminal.println("id: " + this.id);
			socket= new DatagramSocket(id);
			listener.go();
		}
		catch(java.lang.Exception e) {e.printStackTrace();}
	}

	/**
	 * Assume that incoming packets contain a String and print the string.
	 */
	public synchronized void onReceipt(DatagramPacket packet) {
		try {

			System.out.println("ROUTER_ID: " + this.id);
			//if statement to decide on LSA or MES
			byte[] data = packet.getData();
			byte[] mes_type = new byte[PacketContent.MES_TYPE];
			byte[] source = new byte[PacketContent.ID_LENGTH];
			byte[] destination = new byte[PacketContent.ID_LENGTH];
			byte[] payload = new byte[data.length - PacketContent.HEADERLENGTH];

			System.out.println("DATA: " + new String(data, 0 , PacketContent.PACKETLENGTH));
			System.arraycopy(data, 0, mes_type, 0, PacketContent.MES_TYPE);	//COPYING OUT MESSAGE TYPE
			System.arraycopy(data, PacketContent.MES_TYPE + PacketContent.ID_LENGTH, destination, 0, PacketContent.ID_LENGTH); //COPYING OUT DESTINATION
			System.arraycopy(data, PacketContent.HEADERLENGTH, payload, 0, data.length - PacketContent.HEADERLENGTH);	//Copying out the payload

			String mesType = new String(mes_type);

			if(mesType.equals(PacketContent.MES))
			{
				//MESSAGE
			}
			else if(mesType.equals(PacketContent.LSA))
			{
				System.arraycopy(data, PacketContent.MES_TYPE, source, 0, PacketContent.ID_LENGTH);	//COPYING OUT THE SOURCE ROUTER
				String sourceID = new String(source);
				int senderID = Integer.parseInt(sourceID);
				//Have to copy over the routing table of the LSA received.
				RoutingTable temp_RT = reconstruct_RoutingTable(data);
				routingTables.add(temp_RT);
				System.out.println("Added a routing table!");

				System.out.println("ID: " + this.id);
				System.out.println("Routing table of: " + temp_RT.nodeID);
				for(int i = 0; i < temp_RT.nextHops.length; i++)
				{
					System.out.print("Address: " + temp_RT.nextHops[i] + " ");
				}
				System.out.println();

				for(int i = 0; i < table.nextHops.length; i++)
				{
					int tmpPort = table.nextHops[i];
					if(tmpPort != senderID)
					{
						System.out.println("Forwarding");
						tempAddress = new InetSocketAddress(HOST, tmpPort);
						DatagramPacket forwarded = new DatagramPacket(data, PacketContent.PACKETLENGTH, tempAddress);
						//socket.send(forwarded);
					}
					else
					{
						System.out.println("That'd be back to the sender");
					}
				}
			}
		}
		catch(Exception e) {e.printStackTrace();}
	}

	public synchronized void sendLSA()
	{
		try
		{
			DatagramPacket packet = null;

			byte[] buffer = new byte[PacketContent.PACKETLENGTH];	
			byte[] type = new byte[PacketContent.MES_TYPE];
			byte[] source = new byte[PacketContent.ID_LENGTH];
			byte[] rout_array_length = (this.table.nextHops.length + "").getBytes();	//Tells me how long the arrays in the routing table are, e.g. length of costs
			byte[] payload = create_LSA_payload();

			type = (PacketContent.LSA).getBytes();
			source = (this.id + "").getBytes();

			//Copying in header info
			System.arraycopy(type, 0, buffer, 0, type.length);
			System.arraycopy(source, 0, buffer, type.length, source.length);
			System.arraycopy(rout_array_length, 0, buffer, (type.length + source.length), rout_array_length.length);
			System.arraycopy(payload, 0, buffer, (type.length + source.length + rout_array_length.length), payload.length);

			System.out.println("Sending router's LSA to neighbours");

			for(int i = 0; i < table.nextHops.length; i++)
			{
				int tmpPort = table.nextHops[i];
				if(tmpPort == ENDPOINT_1 || tmpPort == ENDPOINT_2)
				{
					System.out.println("Stopping LSA to endpoint");
				}
				else
				{
					tempAddress = new InetSocketAddress(HOST, tmpPort);
					DatagramPacket advertisement = new DatagramPacket(buffer, buffer.length, tempAddress);
					socket.send(advertisement);
				}
			}
		}
		catch(Exception e) { e.printStackTrace(); }
	}

	//This method creates a byte array payload containing the 
	public byte[] create_LSA_payload()
	{
		//Nexthops then costs is the order

		byte[] rout_table_info = new byte[(this.table.costs.length) + (this.table.nextHops.length * PacketContent.ID_LENGTH)];
		System.out.println("ROUT: " + rout_table_info.length);
		int payload_counter = 0;

		int temp_int_val;
		String temp_string_val;
		byte[] temp_byte_val;

		for(int i = 0; i < this.table.nextHops.length; i++)
		{
			temp_int_val = this.table.nextHops[i];
			temp_string_val = temp_int_val + "";
			temp_byte_val = temp_string_val.getBytes();

			for(int j = 0; j < temp_byte_val.length; j++)
			{
				rout_table_info[payload_counter] = temp_byte_val[j];
				payload_counter++;
			}
		}

		//has to be a way to include these two for loops together
		for(int i = 0; i < this.table.costs.length; i++)
		{
			temp_int_val = this.table.costs[i];
			temp_string_val = temp_int_val + "";
			temp_byte_val = temp_string_val.getBytes();

			for(int j = 0; j < temp_byte_val.length; j++)
			{
				rout_table_info[payload_counter] = temp_byte_val[j];
				payload_counter++;
			}
		}
		return rout_table_info;
	}

	public RoutingTable reconstruct_RoutingTable(byte[] buffer)
	{
		RoutingTable newRT;

		byte[] source = new byte[PacketContent.ID_LENGTH];
		byte[] arrayLength = new byte[PacketContent.ARRAY_LENGTH];
		byte[] payload = new byte[buffer.length - (PacketContent.MES_TYPE + PacketContent.ID_LENGTH + PacketContent.ARRAY_LENGTH)];

		System.arraycopy(buffer, PacketContent.MES_TYPE, source, 0, source.length);	//Copying out the sender ID
		System.arraycopy(buffer, PacketContent.MES_TYPE + PacketContent.ID_LENGTH, arrayLength, 0, arrayLength.length); //copying out the length of the arrays
		System.arraycopy(buffer, PacketContent.MES_TYPE + PacketContent.ID_LENGTH + PacketContent.ARRAY_LENGTH, payload, 0, payload.length);	//copying out the payload

		int length = Integer.parseInt(new String(arrayLength));

		int[] nextHops = new int[length];
		int[] costs = new int[length];

		int payload_counter = 0;
		int temp_int_val;
		String temp_string_val;
		byte[] temp_byte_val = new byte[PacketContent.ID_LENGTH];

		System.out.println("Length of payload: " + payload.length);
		System.out.println("Length of nextHops: " + nextHops.length);
		System.out.println("Length of costs: " + costs.length);

		//Building the next hops array
		for(int i = 0; i < nextHops.length; i++)
		{
			for(int j = 0; j < temp_byte_val.length; j++)
			{
				temp_byte_val[j] = payload[payload_counter];
				payload_counter++;
			}
			temp_string_val = new String(temp_byte_val);
			temp_int_val = Integer.parseInt(temp_string_val);

			nextHops[i] = temp_int_val;
		}

		temp_byte_val = new byte[PacketContent.ARRAY_LENGTH];
		//Building the costs array
		for(int i = 0; i < costs.length; i++)
		{
			for(int j = 0; j < temp_byte_val.length; j++)
			{
				temp_byte_val[j] = payload[payload_counter];
				payload_counter++;
			}
			temp_string_val = new String(temp_byte_val);
			temp_int_val = Integer.parseInt(temp_string_val);

			costs[i] = temp_int_val;
		}

		String senderID_string = new String(source);
		int senderID = Integer.parseInt(senderID_string);

		newRT = new RoutingTable(costs, nextHops, senderID);
		return newRT;
	}

	public synchronized void shortestPath(int destination)
	{
		ArrayList completedNodes = new ArrayList();
		ArrayList uncompletedNodes = new ArrayList();		//uncompletedNodes & tentative dists will match up with each other
		ArrayList tentativeDists = new ArrayList();

		//Populated the uncompleted nodes arrayList;
		int currentNode = this.id;
		int dist_Current = 0;	//Distance from the this.id to the current node, 0 as the distance from this node to itself is 0

		for(int i = 0; i < routingTables.size(); i++)
		{
			RoutingTable temp = routingTables.get(i);
			int tempNode = temp.nodeID;
			uncompletedNodes.add(tempNode);
			if(tempNode != currentNode)
			{
				tentativeDists.add(Integer.MAX_VALUE);		//Setting every node to be "infinite" distance at the start
			}
			else
			{
				tentativeDists.add(dist_Current);	//Need the distance and node arrays to match up so add in the distance of 0 for the current node
			}
		}

		while(currentNode != destination)		//While the destination node hasn't been reached
		{
			RoutingTable temp;
			for(int i = 0; i < routingTables.size(); i++)
			{
				temp = routingTables.get(i);
				if(temp.nodeID == currentNode)
				{
					for(int j = 0; j < temp.nextHops.length; j++)	//Look at current node's neighbours
					{
						int currentNeighbour = temp.nextHops[j];
						if(uncompletedNodes.contains(currentNeighbour))
						{	
							int dist_to_neighbour = temp.costs[j];
							int neighbourIndex = uncompletedNodes.indexOf(currentNeighbour);
							int tentative_distance = dist_Current + dist_to_neighbour;

							if(tentative_distance < (int) tentativeDists.get(neighbourIndex))
							{
								tentativeDists.set(neighbourIndex, tentative_distance);
								System.out.println("Distance replaced");
							}
						}
					}	//Done considering all the neighbours

					int finishedNodeIdx = uncompletedNodes.indexOf(currentNode);		//Need the index to remove the appropriate distance from the distance ArrayList
					uncompletedNodes.remove(finishedNodeIdx);
					tentativeDists.remove(finishedNodeIdx);
					completedNodes.add(currentNode);
				}
			}
		}
	}
	
	public synchronized void start() throws Exception {
			terminal.println("Waiting for contact");
			sendLSA();
	}
}
