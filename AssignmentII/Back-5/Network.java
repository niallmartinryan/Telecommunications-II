import processing.core.PApplet;
import java.util.Random;
import java.util.*;
import processing.core.PApplet;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;											//trying this for the moment
import java.util.ArrayList;
import tcdIO.*;

public class Network extends PApplet{
	
		//processing stuff
		public static int MIN_DISTANCE = 150;
		public static final int LAPTOP_MAX_SIZE = 40;
		public static final int SMARTPHONE_MAX_SIZE = 40;
		public static final int EDGE_MAX_SIZE = 100;
		
		
		
		public int laptopCount =0;
		public int smartPhoneCount =0;
		public int edgeCount =0;
		Laptop [] laptops;
		int laptopGraphicsLength = 0;
		int smartPhoneGraphicsLength =0;
		SmartPhone [] smartPhones;
		Edge [] edges;
		
		
		Network(){
			this.laptops = new Laptop[LAPTOP_MAX_SIZE];
			this.smartPhones = new SmartPhone[SMARTPHONE_MAX_SIZE];
			this.edges = new Edge[EDGE_MAX_SIZE];
		}
		// is the post-increment
		public void add(SmartPhone phone){
			smartPhones[smartPhoneCount++] = phone;
		}
		public void add(Laptop lap){
			laptops[laptopCount++] = lap;
		}
		public void add(Laptop [] laps){
			for(int i=0;i<laps.length;i++,laptopCount++){
				laptops[laptopCount] = laps[i];
			}
		}
		public void add(SmartPhone [] phones){
			for(int i=0;i<phones.length;i++,smartPhoneCount++){
				smartPhones[smartPhoneCount] = phones[i];
			}
		}
		public void add(Edge edge){
			edges[edgeCount++] = edge;
		}
		public void add(Edge [] edges){
			
			for(int i=0;i<edges.length;i++,edgeCount++){
				this.edges[edgeCount] = edges[i];
			}
		}
		
		public void drawNetwork(PApplet app){
			for(int i =0; i< laptopCount;i++){
				laptops[i].LaptopGraphics.drawLaptops(app);
			}
			for(int i =0; i< smartPhoneCount;i++){
				smartPhones[i].smartPhoneGraphics.drawSmartPhones(app);
			}
			for(int i=0; i< edgeCount;i++){
				edges[i].drawEdges(app);
			}
		}
		
		// change this method to include height and width? or just remove them
		public  boolean checkCollisions(int xpos, int ypos, int width, int height){
			
			for(int i=0; i< laptopGraphicsLength;i++){
				if(MIN_DISTANCE < Math.sqrt((xpos - laptops[i].LaptopGraphics.xpos)^2 +(ypos - laptops[i].LaptopGraphics.ypos)^2)){
					return false;
				}
			}
			for(int i=0; i< smartPhoneGraphicsLength;i++){
				if(MIN_DISTANCE < Math.sqrt((xpos - smartPhones[i].smartPhoneGraphics.xpos)^2 +(ypos - smartPhones[i].smartPhoneGraphics.ypos)^2)){
					return false;
				}
			}
			return true;
		}
		public Laptop getLaptop(int id){
			for(int i=0;i<laptopCount;i++){
				if(laptops[i].id == id){
					return laptops[i];
				}
			}
			return null;	// not found
		}
//		public SmartPhone getSmartPhone(){
//			
//		}
		// if returns true.. didnt find any conflicts
		public boolean checkEdges(Edge edge){
			boolean match = true;
			for(int i=0;i< edgeCount;i++){
				if(!edges[i].checkEdges(edge)){
					return false;
				}
			}
			return match;
		}
		public void printEdges(){
			System.out.println("---------EDGES--------\n");
			for(int i=0; i< edgeCount;i++){
				Edge currentEdge = edges[i];
				System.out.println("Edge - " +currentEdge.x1 +" -" + 
				currentEdge.x2 + " -" + currentEdge.y1 + " -" + currentEdge.y2 + "\n" );
			}
			System.out.println("=======================");
		}
	}
