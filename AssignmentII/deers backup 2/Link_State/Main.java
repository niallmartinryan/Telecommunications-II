import java.util.*;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;											

import java.util.ArrayList;

import tcdIO.*;

public class Main
{
	static final String DEFAULT_DST_NODE = "localhost";
	static final int ENDPOINT1 = 50000;
	static final int ENDPOINT2 = 50001;
	static final int ROUTER_A = 40000;
	static final int ROUTER_B = 40001;
	static final int ROUTER_C = 40002;
	static final int ROUTER_D = 40003;
	static final int ROUTER_E = 40004;

	int[] routerIDs = {ROUTER_A, ROUTER_B, ROUTER_C, ROUTER_D, ROUTER_E};
	static int[] endpointIDs = {ENDPOINT1, ENDPOINT2};

	public static void main(String[] args)
	{
		try
		{
			int[] connected_to_a = {ENDPOINT1, ROUTER_B, ROUTER_C};
			int[] costs_for_a = {1, 1, 1};

			int[] connected_to_b = {ROUTER_D, ROUTER_A};
			int[] costs_for_b = {1, 1};

			int[] connected_to_c = {ROUTER_D, ROUTER_A};
			int[] costs_for_c = {1, 1};

			int[] connected_to_d = {ENDPOINT2, ROUTER_B, ROUTER_C};
			int[] costs_for_d = {1, 1, 1};

			int[] connected_to_e = {ROUTER_A, ROUTER_B, ROUTER_C, ROUTER_D};
			int[] costs_for_e = {1, 1, 1, 1};

			Router[] routers = {
				new Router(new Terminal("ROUTER_A"), ROUTER_A, costs_for_a, connected_to_a, endpointIDs),
				new Router(new Terminal("ROUTER_B"), ROUTER_B, costs_for_b, connected_to_b, endpointIDs),
				new Router(new Terminal("ROUTER_C"), ROUTER_C, costs_for_c, connected_to_c, endpointIDs),
				new Router(new Terminal("ROUTER_D"), ROUTER_D, costs_for_d, connected_to_d, endpointIDs),
				new Router(new Terminal("ROUTER_E"), ROUTER_E, costs_for_e, connected_to_e, endpointIDs)
			};

			routers[4].start();
			// for(int i = 0; i < routers.length; i++)
			// {
			// 	routers[i].start();
			// }
		}
		catch(java.lang.Exception e) { e.printStackTrace(); }
	}
}