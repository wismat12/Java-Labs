package dijkstra;
import java.util.*;


public class Node {
	
	public int number;
	public List<Connection> neighbours;
	
	public Node(int number, int maxrange, double[][] matrix){
		
		this.number = number;
		this.neighbours = new ArrayList<Connection>();
		
		for(int i = 0; i < maxrange; i++){
			
			if(matrix[number][i] != 0){
				Connection newconnection = new Connection(i,matrix[number][i]);
				neighbours.add(newconnection);
			}
				
		}
		
		
	}
	

}