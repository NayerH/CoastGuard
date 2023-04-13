package code;

import java.util.HashMap;

public class Node {
	public String curLocation;
	
	//String for location, Integer for damage points
	public HashMap<String, Integer> wrecks;
	
	//String for location, Integer for passenger count
	public HashMap<String, Integer> ships;
	
	public int passengerCount;
	public int totalPassengersSaved;
	public int deaths;
	public int retrieved;
	
	public int depth;
	public int pathCost;
	public double heuristicRes;
	public String action;
	public Node parent;
	
	public Node(String curLocation, HashMap<String, Integer> ships){
		this.curLocation = curLocation;
		this.ships = new HashMap<String, Integer>();
		this.ships.putAll(ships);
		
		this.wrecks = new HashMap<String, Integer>();
		this.passengerCount = 0;
		this.totalPassengersSaved = 0;
		this.deaths = 0;
		this.retrieved = 0;
		this.depth = 0;
		this.pathCost = 0;
		this.heuristicRes = 0;
		this.action = null;
		this.parent = null;
	}
	
	//making a child of a node
	public Node(Node n){
		this.curLocation = n.curLocation;
		
		this.ships = new HashMap<String, Integer>();
		this.ships.putAll(n.ships);
		
		this.wrecks = new HashMap<String, Integer>();
		this.wrecks.putAll(n.wrecks);
		
		this.passengerCount = n.passengerCount;
		this.totalPassengersSaved = n.totalPassengersSaved;
		this.deaths = n.deaths;
		this.retrieved = n.retrieved;
		this.depth = n.depth + 1;
		this.pathCost = n.pathCost;
		this.heuristicRes = 0;
		this.action = n.action;
		this.parent = n;
	}
	
	public boolean isGoalNode(){
		return (ships.isEmpty() && wrecks.isEmpty() && passengerCount == 0);
	}
	
	//TEST THIS METHOD TO MAKE SURE NO NEED TO INCREASE CONDITIONS
	public boolean isSameNode(Node n){
		return (
				n.curLocation.equals(this.curLocation) && 
				this.passengerCount == n.passengerCount && 
				this.retrieved == n.retrieved
		);
	}
	
	public int cityBlockDistance(String loc){
		int thisX = Integer.parseInt(this.curLocation.split(",")[0]);
		int thisY = Integer.parseInt(this.curLocation.split(",")[1]);
		
		int nX = Integer.parseInt(loc.split(",")[0]);
		int nY = Integer.parseInt(loc.split(",")[1]);
		
		return (Math.abs(thisX - nX) + Math.abs(thisY - nY));
	}
	
	public int cityBlockDistance(String loc1, String loc2){
		int thisX = Integer.parseInt(loc1.split(",")[0]);
		int thisY = Integer.parseInt(loc1.split(",")[1]);
		
		int nX = Integer.parseInt(loc2.split(",")[0]);
		int nY = Integer.parseInt(loc2.split(",")[1]);
		
		return (Math.abs(thisX - nX) + Math.abs(thisY - nY));
	}
	
}
