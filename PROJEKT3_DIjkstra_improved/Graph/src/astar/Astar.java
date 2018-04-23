package astar;
import java.util.*;
import dijkstra.*;
import graph.Junction;

//https://en.wikipedia.org/wiki/A*_search_algorithm
public class Astar {
	
	List<Junction> Qcollection;
	List<Junction> closedSet;
	List<Junction> openSet;
	double[] gScore;
	double[] fScore;
	int[] cameFrom;

	public Astar(HeuristicMatrixGraph data, int start, int goal){
		
		System.out.println("A* Searching... now!  Starting from " + (start) + " index and aiming into " + (goal));
		long startTime = System.nanoTime();   
		/*
		 *  gScore - the cost of getting from the start node to that node
		 *  fScore = gScore + heuristicCost - 	partly known, partly heuristic
		 */
		this.Qcollection = new ArrayList<Junction>();
		this.closedSet = new ArrayList<Junction>();
		this.openSet = new ArrayList<Junction>();
		this.gScore = new double[data.NodesAmount];
		this.fScore = new double[data.NodesAmount];
		this.cameFrom = new int[data.NodesAmount];	
		//init	
		for(Junction tmp: data.junctions){
			
			Qcollection.add(tmp);
			
		}
		
		this.openSet.add(this.Qcollection.get(start));
		//System.out.println(this.openSet.size());
	    // The set of currently discovered nodes still to be evaluated.
	    // Initially, only the start node is known.
	    //!!!openSet := {start}
	    // For each node, which node it can most efficiently be reached from.
	    // If a node can be reached from many nodes, cameFrom will eventually contain the
	    // most efficient previous step.
	    //!!!cameFrom := the empty map
		for(int i = 0; i < data.NodesAmount; i++){
			this.gScore[i] = Integer.MAX_VALUE;
			this.fScore[i] = Integer.MAX_VALUE;
			this.cameFrom[i] = -1;
		}
		this.gScore[start] = 0;
		this.fScore[start] = data.heuristicCost[start][goal];
		
	    // For each node, the cost of getting from the start node to that node.
	    //!!!gScore := map with default value of Infinity
	    // The cost of going from start to start is zero.
	    //!!gScore[start] := 0 
	    // For each node, the total cost of getting from the start node to the goal
	    // by passing by that node. That value is partly known, partly heuristic.!!!!->WA¯NE!!!
	    //!!!fScore := map with default value of Infinity						--przybli¿one rozwi¹zanie problemu optymalizacyjnego.
	    // For the first node, that value is completely heuristic.
	    //!!!fScore[start] := heuristic_cost_estimate(start, goal)
		double lowest;
		int lowestindex;
		boolean neighbourInClosed;
		boolean neighbourInOpen;
		boolean failure = true;
		double tentative_gScore;

		int itercounter = 0;
	    while(!this.openSet.isEmpty()){
	    	itercounter++;
	    	System.out.println("Next MOVE!!!");
	    	//current := the node in openSet having the lowest fScore[] value
	    	lowest = this.fScore[this.openSet.get(0).number];
	    	lowestindex = 0;
	    	for(Junction i: this.openSet){
	    		if(lowest > this.fScore[i.number]){
	    			lowest = this.fScore[i.number];
	    			lowestindex =  this.openSet.indexOf(i);
	    		}
	    		//System.out.println(lowest);
	    	}
	    	Junction current = this.openSet.get(lowestindex);
	    	System.out.println("Current Node during Searching - "+current.number);
	
	    	if(current.number == goal){
	    		failure = false;
	    		System.out.println("Ended Successfully!!!!");
	    		break;
	    		//return reconstruct_path(cameFrom, current)
	    	}
	    	/*openSet.Remove(current)
	        closedSet.Add(current)*/
	    	//this.closedSet.add(this.openSet.remove(current.number));
	    	//System.out.println(current.number);
	    	this.closedSet.add(this.openSet.remove(this.openSet.indexOf(current)));
	    	
	    	 /* for each neighbor of current
            if neighbor in closedSet
                continue		// Ignore the neighbor which is already evaluated.*/
	    	
	    	for(Junction neighbour: current.connections){
	    		neighbourInClosed = false;
	    		for(Junction i: this.closedSet){
	    			if(i.number == neighbour.number){
	    				neighbourInClosed = true;
	    				break;
	    			}
	    			
	    		}
	    		
	    		if(neighbourInClosed){
	    			System.out.println("AlreadyClosed("+neighbour.number+")");
	    			continue; 
	    			
	    		}
	    		 /*// The distance from start to a neighbor
	            tentative_gScore := gScore[current] + dist_between(current, neighbor)
	            if neighbor not in openSet	// Discover a new node
	                openSet.Add(neighbor)
	            else if tentative_gScore >= gScore[neighbor]
	                continue		// This is not a better path.*/
	    		tentative_gScore = this.gScore[current.number] + data.matrix[current.number][neighbour.number];
	    		
	    		neighbourInOpen = false;
		    	for(Junction i: this.openSet){
		    		if(i.number == neighbour.number){
		    			neighbourInOpen = true;
		    			break;
		    		}
		    	}
	    		if(!neighbourInOpen){
	    			this.openSet.add(this.Qcollection.get(neighbour.number));
	    		}else if(tentative_gScore >= this.gScore[neighbour.number]){
	    			System.out.print(neighbour.number +" - omitted - There is no use to go ");
	    			continue;
	    			
	    		}
	    		/*This path is the best until now. Record it!
	             *cameFrom[neighbor] := current
	             *gScore[neighbor] := tentative_gScore
	             *fScore[neighbor] := gScore[neighbor] + heuristic_cost_estimate(neighbor, goal)
	             */
	    		this.cameFrom[neighbour.number] = current.number;
	    		this.gScore[neighbour.number] = tentative_gScore;
	    		this.fScore[neighbour.number] = this.gScore[neighbour.number] + data.heuristicCost[neighbour.number][goal];
	    		
	    		System.out.println("Neighbour:"+neighbour.number);
	    	}	
	    }
	    if(failure)
	    	System.out.println("ERROR!!!!");
	    long estimatedTime = (System.nanoTime() - startTime)/1000;
	    
	    System.out.println("Amount of iterations: " + itercounter);	
	    System.out.println("The shortest distance: " + gScore[goal] + " km");   
	   // System.out.println("Estimated time of Astar activity[uS]: " + estimatedTime);   
	    

	        
	      
	           

	         

/*	function reconstruct_path(cameFrom, current)
	    total_path := [current]
	    while current in cameFrom.Keys:
	        current := cameFrom[current]
	        total_path.append(current)
	    return total_path*/
		
		
	}
	
	

}
