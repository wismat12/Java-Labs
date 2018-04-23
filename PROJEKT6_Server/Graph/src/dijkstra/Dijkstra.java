package dijkstra;
import graph.*;
import java.util.*;

//http://eduinf.waw.pl/inf/alg/001_search/0138.php
public class Dijkstra {
	
	List<Junction> Qcollection;
	List<Junction> Scollection;
	double[] mindist;
	//PriorityQueue<Double> mindistSet;
	int[] prev;
	List<Junction> SameCostList;   // Przypadek gdy z jednego node'a mamy kilka polaczen o takiej samej dlugosci
	
	public Dijkstra(HeuristicMatrixGraph data, int StartPoint, int targetindex){
		//--StartPoint;
		//--targetindex;
		System.out.println("Dijkstra is Searching... now!  Starting from " + (StartPoint) + " index and aiming into " + (targetindex));
		//INIT
		long startTime = System.nanoTime();    
		
		this.Qcollection = new ArrayList<Junction>();
		this.Scollection = new ArrayList<Junction>();
		this.SameCostList = new ArrayList<Junction>();
		//this.mindistSet = new PriorityQueue<Double>();
		
		for(Junction tmp: data.junctions){
			
			Qcollection.add(tmp);
			
		}
		this.mindist = new double[data.NodesAmount];
		this.prev = new int[data.NodesAmount];
		

	
		//this.mindist.add(0.0);
		
		
		for(int i = 0; i < data.NodesAmount; i++){
			
			this.mindist[i] = Double.MAX_VALUE; 
			this.prev[i] = -1;
			
		}
		this.mindist[StartPoint] = 0;
		double closest;
		int itercounter = 0;
		
		
		while(!this.Qcollection.isEmpty()){
			itercounter++;
			this.SameCostList.clear();
			
			//int first = this.Qcollection.get(0).number;
			//pos = 0;
			closest = mindist[this.Qcollection.get(0).number];
			
			for(Junction tmp: Qcollection){
				//szukanie najblizszego noda w zbiorze Q
				if(closest > mindist[tmp.number]){
					closest = mindist[tmp.number];
				//	pos = tmp.number;					
				}
				
			}
			//Junction current = this.Qcollection.get(0); do priorityqueue
			//closest = this.mindist.peek();
			//Sprawdzanie czy sa nody oddalone o taka sama d³ugoœæ - wtedy losowanie
			//for(Junction tmp: current.connections){
			for(Junction tmp: this.Qcollection){
				
				if(closest == this.mindist[tmp.number]){
					this.SameCostList.add(tmp);				
				}
				
			}
			for(Junction SameCostNode: this.SameCostList){
				//bierzemy noda albo nody (gdy kilka takich samych odleglosci), przenosimy do S i sprawdzamy sasiadow
				this.Scollection.add(this.Qcollection.remove(this.Qcollection.indexOf(SameCostNode)));
				//teraz sasiedzi z tego nowego, z S
				
				Junction newintoS =  this.Scollection.get(this.Scollection.size() - 1);
				for(Junction tmpneighbour: newintoS.connections){
					if(this.mindist[tmpneighbour.number] > (this.mindist[newintoS.number] + data.matrix[newintoS.number][tmpneighbour.number])){
						
						this.mindist[tmpneighbour.number] = this.mindist[newintoS.number] + data.matrix[newintoS.number][tmpneighbour.number];
						//nowy dyst^^ i dodanie poprzednika
						this.prev[tmpneighbour.number] = newintoS.number;
					}
				}
			}
		}	
		long estimatedTime = (System.nanoTime() - startTime)/1000;
		if(this.mindist[targetindex] != Integer.MAX_VALUE){
			
			System.out.println("The shortest distance: " + this.mindist[targetindex] + " km");	
			System.out.println("Amount of iterations: " + itercounter);	
			int counter = targetindex;
		
			Vector<Integer> path = new Vector<Integer>();
			
			
			while(this.prev[counter]!=-1){
				
			
				path.add(0,counter);
			
				
				counter = this.prev[counter];
			}
			path.add(0,counter);
			
			System.out.println(StartPoint + " - First Point");
			
			for(int i = 0; i < path.size(); i++){
				if(i != path.size() - 1)
					System.out.print(path.get(i)+" - (" + data.junctions.get(path.get(i)).id.roadname + ") > ");
					//System.out.print(path.get(i)+" -> ");
				else
					System.out.print(path.get(i));
			}
		}else{
			System.out.println("There is no access to ->"+targetindex+"<- node!!!");
		}
		System.out.println();
	  //  System.out.println("Estimated time of Dijkstra activity[uS]: " + estimatedTime);   
		System.out.println("*********************");

	}
/*	public void prepareNodes(double matrix[][], int maxrange){
		
		for(int i = 0; i < maxrange; i++){
				
			Node newnode = new Node(i, maxrange, matrix);
			
			Scollection.add(newnode);
			
		}
		
	}*/

}
