
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

public class Edge{
	
	boolean active;
	Color currentColor;
	Color activeColor = new Color(4,227,252);
	Color deactiveColor = new Color(0,0,0);

	int length;
	int height;

	int x1, x2, y1, y2;
	

	Edge( int x1, int x2,int y1,int y2, int height){
		active = false;
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
		this.height = height;
		length = (int) Math.sqrt((x1-x2)^2+(x2 -y2)^2);
	}
	Edge(Laptop lap, SmartPhone phone){
		x1 = lap.LaptopGraphics.xpos;
		y1 = lap.LaptopGraphics.ypos;
		x2 = phone.smartPhoneGraphics.xpos;
		y2 = phone.smartPhoneGraphics.ypos;
	}
	Edge(Laptop lap1, Laptop lap2){
		x1 = lap1.LaptopGraphics.xpos;
		y1 = lap1.LaptopGraphics.ypos;
		x2 = lap2.LaptopGraphics.xpos;
		y2 = lap2.LaptopGraphics.ypos;
	}
	Edge(SmartPhone phone1, SmartPhone phone2){
		x1 = phone1.smartPhoneGraphics.xpos;
		y1 = phone1.smartPhoneGraphics.ypos;
		x2 = phone2.smartPhoneGraphics.xpos;
		y2 = phone2.smartPhoneGraphics.ypos;
	}
	public void drawEdges(PApplet applet){
		currentColor = (active) ? activeColor : deactiveColor;
		applet.fill(currentColor.red,currentColor.green, currentColor.blue);
		applet.line(x1,y1,x2,y2);
	}
	// check if this works..
	public  boolean checkEdges(Edge edge){
		
		if((x1==edge.x1&&x2==edge.x2&&y1==edge.y1&&y2==edge.y2)|| (x1==edge.y1&&x2==edge.y2&&y1==edge.x1&&y2==edge.x2)){
			return false;
		}
		return true;
		
	}
	

}