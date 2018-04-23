package dijkstra;
import graph.*;
import java.util.*;

//http://eduinf.waw.pl/inf/alg/001_search/0138.php
public class Dijkstra {
	
	HeuristicMatrixGraph data;
	double[] mindist;
	int[] prev;
	PriorityQueue<qvertex> mindistPQ;
	Comparator<Object> comparator;
	
	class qvertex{
		int tab_mindist_index;
		Junction jun;
		qvertex(int index){
			System.out.println("Dodano Nowy Vertex do PQ nr - " + index);
			this.tab_mindist_index = index;
			this.jun = data.junctions.get(index);
		}
	}
	class vertex_comparator implements Comparator<Object>{

		public int compare(Object arg0, Object arg1) {
			qvertex v1 = (qvertex) arg0;
			qvertex v2 = (qvertex) arg1;
			return (int)(Dijkstra.this.mindist[v1.tab_mindist_index] - Dijkstra.this.mindist[v2.tab_mindist_index]);
		}	
	}
	public Dijkstra(HeuristicMatrixGraph data, int StartPoint, int targetindex){
		
		this.data =data;
		System.out.println("Dijkstra is Searching... now!  Starting from " + (StartPoint) + " index and aiming into " + (targetindex));
		//INIT
		long startTime = System.nanoTime();    
		
		this.comparator = new vertex_comparator();
		this.mindistPQ = new PriorityQueue<qvertex>(this.comparator);		
		this.mindist = new double[data.NodesAmount];
		this.prev = new int[data.NodesAmount];
		qvertex first = new qvertex(StartPoint); // Dodanie pierwszego vertexa do PQ z pocz¹tkowym numerem skrzyzowania
		this.mindistPQ.add(first);
		
		for(int i = 0; i < data.NodesAmount; i++){
			
			this.mindist[i] = Double.MAX_VALUE; 
			this.prev[i] = -1;
			
		}
		this.mindist[StartPoint] = 0;
		
		qvertex tmp;
		while((tmp = this.mindistPQ.poll()) != null){
			System.out.println("usuniêto z PQ Vertex nr - " + tmp.tab_mindist_index);
			Junction newintoS = tmp.jun;
			for(Junction tmpneighbour: newintoS.connections){
				if(this.mindist[tmpneighbour.number] > (this.mindist[newintoS.number] + data.matrix[newintoS.number][tmpneighbour.number])){
					
					this.mindist[tmpneighbour.number] = this.mindist[newintoS.number] + data.matrix[newintoS.number][tmpneighbour.number];
					//nowy dyst^^ i dodanie poprzednika
					this.prev[tmpneighbour.number] = newintoS.number;
					qvertex another = new qvertex(tmpneighbour.number);
					this.mindistPQ.add(another);
				}		
			}
		}	
		long estimatedTime = (System.nanoTime() - startTime)/1000;
		if(this.mindist[targetindex] != Integer.MAX_VALUE){
			
			System.out.println("The shortest distance: " + this.mindist[targetindex] + " km");		
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
	   // System.out.println("Estimated time of Dijkstra activity[uS]: " + estimatedTime);   
		System.out.println("*********************");
	}
}