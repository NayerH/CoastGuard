package code;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Stack;

//TO DO:
//1 - BFS, DFS, IDS - DONE
//2 - METHODS TO DETERMINE IF ACTION IS POSSIBLE OR NOT - DONE
//2 - ACTIONS MODELLING with updating time steps (wrecks and ships) - DONE
//3 - RETURN STRING IF GOAL IS FOUND - DONE

//CHECK: NEED FOR REMOVING REDUCTANT STATES
public class SearchProblem {
	//STATIONS are non changing so they are stored in the here
	public HashMap<String, Boolean> stations;
	public int capacity;
	public int width;
	public int height;
	public boolean visualize;
	public int nodesExpanded;
	public int heuristic;
	
	//DETECT IF EXPANDED BEFORE
	HashSet<String> expandedBeforeSet;
	
	public SearchProblem(boolean visualize){
		this.visualize = visualize;
	}
	
	public String generalSearch(String problem, String strategy){
		Node rootNode = makeNode(problem);
		
		if(strategy.equals("BF")){
			this.heuristic = 0;
			return BFS(rootNode);
		} else if(strategy.equals("DF")){
			this.heuristic = 0;
			return DFS(rootNode);
		} else if(strategy.equals("ID")){
			this.heuristic = 0;
			return IDS(rootNode);
		} else if(strategy.substring(2).equals("GR")){
			this.heuristic = Integer.parseInt(strategy.charAt(2) + "");
			return GR(rootNode, Integer.parseInt(strategy.charAt(2) + ""));
		}
		this.heuristic = Integer.parseInt(strategy.charAt(2) + "");
		return AS(rootNode, Integer.parseInt(strategy.charAt(2) + ""));
	}
	
	private String AS(Node rootNode, int heuristic) {
		PriorityQueue<Node> nodes = new PriorityQueue<Node>(new NodeASComparator());
		nodes.add(rootNode);
		while(!nodes.isEmpty()){
			Node head = nodes.poll();
			if(head.isGoalNode()){
				//System.out.println(head.pathCost);
				return resultString(head);
			}
			LinkedList<Node> expandedNodes = expandNode(head);
			nodes.addAll(expandedNodes);
		}
		return "FAILURE";
	}

	private String GR(Node rootNode, int heuristic) {
		PriorityQueue<Node> nodes = new PriorityQueue<Node>(new NodeGRComparator());
		nodes.add(rootNode);
		while(!nodes.isEmpty()){
			Node head = nodes.poll();
			if(head.isGoalNode()){
				return resultString(head);
			}
			LinkedList<Node> expandedNodes = expandNode(head);
			nodes.addAll(expandedNodes);
		}
		return "FAILURE";
	}

	private String IDS(Node rootNode) {
		int maxDepth = 0;
		while(true){
			Stack<Node> nodes = new Stack<Node>();
			nodes.push(rootNode);
			expandedBeforeSet.clear();
			while(!nodes.isEmpty()){
				Node head = nodes.pop();
				if(head.isGoalNode()){
					return resultString(head);
				}
				if(head.depth + 1 <= maxDepth){
					LinkedList<Node> expandedNodes = expandNode(head);
					nodes.addAll(expandedNodes);
				}
			}
			maxDepth++;
		}
	}

	private String DFS(Node rootNode) {
		Stack<Node> nodes = new Stack<Node>();
		nodes.push(rootNode);
		while(!nodes.isEmpty()){
			Node head = nodes.pop();
			if(head.isGoalNode()){
				return resultString(head);
			}
			LinkedList<Node> expandedNodes = expandNode(head);
			nodes.addAll(expandedNodes);
		}
		return "FAILURE";
	}

	private String BFS(Node rootNode) {
		LinkedList<Node> nodes = new LinkedList<Node>();
		nodes.add(rootNode);
		while(!nodes.isEmpty()){
			Node head = nodes.removeFirst();
			if(head.isGoalNode()){
				return resultString(head);
			}
			LinkedList<Node> expandedNodes = expandNode(head);
			nodes.addAll(expandedNodes);
		}
		return "FAILURE";
	}
	
	public boolean isActionPossible(Node n, String action){
		if(action.equals("up")){
			return !(n.curLocation.split(",")[0].equals("0"));
		} else if(action.equals("down")){
			return !(n.curLocation.split(",")[0].equals((height - 1) + ""));
		} else if(action.equals("left")){
			return !(n.curLocation.split(",")[1].equals("0"));
		} else if(action.equals("right")){
			return !(n.curLocation.split(",")[1].equals((width - 1) + ""));
		} else if(action.equals("pickup")){
			if(n.ships.containsKey((n.curLocation))){
				if(n.ships.get(n.curLocation) > 0){
					return n.passengerCount < capacity;
				}
			}
			return false;
		} else if(action.equals("drop")){
			if(stations.containsKey((n.curLocation))){
				if(stations.get(n.curLocation)){
					return n.passengerCount > 0;
				}
			}
			return false;
		} else if(action.equals("retrieve")){
			if(n.wrecks.containsKey((n.curLocation))){
				if(n.wrecks.get(n.curLocation) < 20){
					return true;
				}
			}
			return false;
		}
		return false;
	}
	
	public LinkedList<Node> expandNode(Node n){
		LinkedList<Node> res = new LinkedList<Node>();
		String expandedBeforeStr = n.curLocation + "," + n.passengerCount + "," + n.totalPassengersSaved + "," + n.retrieved;
//		System.out.println(expandedBeforeStr);
		if(expandedBeforeSet.contains(expandedBeforeStr)){
			return res;
		}
		expandedBeforeSet.add(expandedBeforeStr);
		String[] actionsArr = {"up", "down", "left", "right","pickup", "drop", "retrieve"};
		nodesExpanded++;
		for(String action : actionsArr){
			if(isActionPossible(n, action)){
				Node child = new Node(n);
				child.action = action;
				
				switch(action){
				case "up":
					String[] position = child.curLocation.split(",");
					child.curLocation = (Integer.parseInt(position[0]) - 1) + "," + position[1];
					break;
					
				case "down":
					position = child.curLocation.split(",");
					child.curLocation = (Integer.parseInt(position[0]) + 1) + "," + position[1];
					break;
					
				case "left":
					position = child.curLocation.split(",");
					child.curLocation = position[0] + "," + (Integer.parseInt(position[1]) - 1);
					break;
					
				case "right":
					position = child.curLocation.split(",");
					child.curLocation = position[0] + "," + (Integer.parseInt(position[1]) + 1);
					break;
					
				case "pickup":
					int passengersOnShip = child.ships.get(child.curLocation);
					int extraCapacity = this.capacity - child.passengerCount;
					int passengersToBeBoarded = Math.min(extraCapacity, passengersOnShip);
					child.passengerCount += passengersToBeBoarded;
					child.totalPassengersSaved += passengersToBeBoarded;
					
					if(passengersOnShip == passengersToBeBoarded){
						child.ships.remove(child.curLocation);
						child.wrecks.put(child.curLocation, 0);
					} else {
						child.ships.replace(child.curLocation, passengersOnShip - passengersToBeBoarded);
					}
					break;
					
				case "drop":
					child.passengerCount = 0;
					break;
					
				case "retrieve":
					child.retrieved++;
					child.wrecks.remove(child.curLocation);
					break;
					
				default: break;
				}
				int newDeaths = 0;
				ArrayList<String> toBeRemoved = new ArrayList<String>();
				for(String key : child.ships.keySet()){
					int passengersOnShip = child.ships.get(key);
					passengersOnShip--;
					newDeaths++;
					if(passengersOnShip > 0){
						child.ships.replace(key, passengersOnShip);
					} else {
						//child.ships.remove(key);
						toBeRemoved.add(key);
						child.wrecks.put(key, 0);
					}
				}
				for(String key : toBeRemoved){
					child.ships.remove(key);
				}
				child.deaths += newDeaths;
				child.pathCost += newDeaths;
				toBeRemoved = new ArrayList<String>();
				for(String key : child.wrecks.keySet()){
					int damagePoints = child.wrecks.get(key);
					damagePoints++;
					if(damagePoints < 20){
						child.wrecks.replace(key, damagePoints);
					} else {
						toBeRemoved.add(key);
						//child.wrecks.remove(key);
					}
				}
				for(String key : toBeRemoved){
					child.wrecks.remove(key);
				}
				child.pathCost += toBeRemoved.size();
				
				if(this.heuristic == 1){
					child.heuristicRes = heuristic1(child);
				} else if(this.heuristic == 2){
					child.heuristicRes = heuristic2(child);
				}
				
				if(!expandedBefore(child)){
					res.add(child);
				}
					
			}
		}
		return res;
	}
	
	public String resultString(Node n){
		String res = "";
		Stack<Node> parents = new Stack<Node>();
		if(n.isGoalNode()){
			Node parent = n;
			parents.push(parent);
			while(parent.parent != null){
				res = "," + parent.action + res;
				parent = parent.parent;
				parents.push(parent);
			}
			res = res.substring(1);
			res += ";";
			res += n.deaths + ";";
			res += n.retrieved + ";";
			res += nodesExpanded;
			if(visualize)
				printResult(parents, res);
		}
		return res;
	}
	
	public void printResult(Stack<Node> nodes, String res) {
		String[] actions = res.split(";")[0].split(",");
		int i = 0;
		String resOut = printHelper(nodes.pop());
		while(!nodes.isEmpty()){
			resOut += '\n' + System.getProperty("line.separator") + actions[i++];
			resOut += printHelper(nodes.pop());
		}
		System.out.print(resOut);
	}

	private String printHelper(Node node) {
		String res = "";
		for(int i = 0; i < height; i++){
			res += '\n' + "|";
			for(int j = 0; j < width; j++){
				String resStr = "";
				if(node.curLocation.equals(i + "," + j)){
					resStr += "C";
				}
				if(node.ships.containsKey(i + "," + j)){
					resStr += "SH";
				}
				if(node.wrecks.containsKey(i + "," + j)){
					resStr += "W";
				}
				if(stations.containsKey(i + "," + j)){
					resStr += "ST";
				}
				if(resStr.length() == 0){
					resStr = "   ";
				} else if(resStr.length() == 1){
					resStr = " " + resStr + " ";
				} else if(resStr.length() == 2){
					resStr += " ";
				}
				res += resStr + "|";
			}
		}
		return res;
	}

	public boolean expandedBefore(Node n){
		return (
				expandedBeforeSet.contains(n.curLocation + "," + n.passengerCount + "," + n.totalPassengersSaved + "," + n.retrieved) == true
		);
	}
	
	public double heuristic1(Node n){
		if(n.isGoalNode()){
			return 0;
		}
		int minDistance = Integer.MAX_VALUE;
		for(String key : n.ships.keySet()){
			int distance = n.cityBlockDistance(key);
			if(distance < minDistance){
				minDistance = distance;
			}
		}
		int extraPassengers = 0;
		int numberOfShips = 0;
		for(String key : n.ships.keySet()){
			int passengersToDie = n.ships.get(key);
			if(passengersToDie < minDistance){
				extraPassengers += passengersToDie;
			} else {
				numberOfShips++;
			}
		}
		return (minDistance * numberOfShips) + extraPassengers;
	}
	
	public double heuristic2(Node n){
		if(n.isGoalNode()){
			return 0;
		}
		int wrecksToBeDestroyed = 0;
		for(String key : n.wrecks.keySet()){
			int distance = n.cityBlockDistance(key);
			if(distance > (20 - n.wrecks.get(key))){
				wrecksToBeDestroyed++;
			}
		}
		return wrecksToBeDestroyed;
	}

	public Node makeNode(String grid){
		String[] gridDecoded = grid.split(";");
		width = Integer.parseInt(gridDecoded[0].split(",")[0]);
		height = Integer.parseInt(gridDecoded[0].split(",")[1]);
		capacity = Integer.parseInt(gridDecoded[1]);
		
		String curLocation = gridDecoded[2];
		
		String[] stationArr =  gridDecoded[3].split(",");
		stations = new HashMap<String, Boolean>();
		for(int i = 0; i < stationArr.length; i += 2){
			stations.put(stationArr[i] + "," + stationArr[i+1], true);
		}
		
		HashMap<String, Integer> ships = new HashMap<String, Integer>();
		String[] shipArr =  gridDecoded[4].split(",");
		for(int i = 0; i < shipArr.length; i += 3){
			ships.put(
					shipArr[i] + "," + shipArr[i+1], 
					Integer.parseInt(shipArr[i+2])
			);
		}
		
		expandedBeforeSet = new HashSet<String>();
		
		Node treeRoot = new Node(curLocation, ships);
		return treeRoot;
	}
	
//	public String stringifyShips(HashMap<String, Integer> ships){
//		String res = "";
//		PriorityQueue<String> pq = new PriorityQueue<String>();
//		for(String shipIndex : ships.keySet()){
//			pq.add(shipIndex + "," + ships.get(shipIndex));
//		}
//		while(!pq.isEmpty()){
//			res += pq.poll() + ",";
//		}
//		System.out.println(res);
//		if(res.length() > 0){
//			return res.substring(0, res.length() - 1);
//		}
//		return res;
//	}
}


class NodeGRComparator implements Comparator<Node> {
    
    public int compare(Node n1, Node n2) {
        if (n1.heuristicRes > n2.heuristicRes)
            return 1;
        else if (n1.heuristicRes < n2.heuristicRes)
            return -1;
        return 0;
    }
}

class NodeASComparator implements Comparator<Node> {
    
	public int compare(Node n1, Node n2) {
        if ((n1.heuristicRes + n1.pathCost) > (n2.heuristicRes + n2.pathCost))
            return 1;
        else if ((n1.heuristicRes + n1.pathCost) < (n2.heuristicRes + n2.pathCost))
            return -1;
        return 0;
    }
}