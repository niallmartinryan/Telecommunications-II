

import processing.core.PApplet;
import java.util.Random;
import java.util.*;
import processing.core.PApplet;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;											//trying this for the moment
import java.util.ArrayList;
import tcdIO.*;

// might move this to TcomsMain......!!!!!!---

public class TelecomsAssignmentII extends PApplet {
	

	public static int SIZE_X = 1200;
	public static int SIZE_Y = 700;
	public static Network network;
	
	public static void main(String args[]){
		PApplet.main(new String[] { "TelecomsAssignmentII" });
		network = new Network();
		TcomsMain.main(args, network);
	}
	
	public void settings() {
		size(SIZE_X,SIZE_Y);
	}
	
	public void draw() {
		background(255);
		fill(0);
		network.drawNetwork(this);
	}
	
	//Might change the Network.. so that it adds to it instead of just taking in specific values..
}
