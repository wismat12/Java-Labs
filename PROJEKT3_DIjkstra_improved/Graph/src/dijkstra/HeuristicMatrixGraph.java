package dijkstra;
import java.util.*;
import java.io.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import graph.GPS;
import graph.Identifier;
import graph.Junction;

import java.util.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

public class HeuristicMatrixGraph implements Serializable {
	
	public File newxml;
	
	public List<Junction> junctions;
	//public int StartPoint;
	public int NodesAmount;
	public double[][] matrix;
	public double[][] heuristicCost;
	
	public HeuristicMatrixGraph(){
		
		System.out.println("Junctions are loading... Creating Connections to Neighbours and HeuristicCosts Matrixes");
		try {
			this.newxml = new File("MyGraph.xml");
			//this.StartPoint = StartPoint;
			junctions = new ArrayList<Junction>();
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(newxml);
			
			doc.getDocumentElement().normalize();

			System.out.println("Root element of loaded xml file :" + doc.getDocumentElement().getNodeName());
			
			this.NodesAmount = Integer.parseInt(doc.getDocumentElement().getAttribute("nodes_amount"));
			this.matrix = new double[NodesAmount][NodesAmount];
			this.heuristicCost = new double[NodesAmount][NodesAmount];
			
			NodeList AllNodeList = doc.getElementsByTagName("junction");
			NodeList AllConnectionsList = doc.getElementsByTagName("connections_of");
			
			for (int Nodepos = 0; Nodepos < this.NodesAmount; Nodepos++) {
				
				Element presentNode =(Element) AllNodeList.item(Nodepos);
				
				String ID =  presentNode.getAttribute("id");
				String name =  presentNode.getAttribute("roadname");
				double lat = Double.parseDouble(presentNode.getAttribute("latitude"));
				double lon = Double.parseDouble(presentNode.getAttribute("longitude"));
				Identifier newId = new Identifier(ID,lon,lat,name);
				//Identifier newId = new Identifier(ID,lon,lat);
				Junction newJun = new Junction(newId, Integer.parseInt(ID));
				this.junctions.add(newJun);	
			}
			//System.out.println("Nodes Loaded!");
			//Creating Connections
			for (int pos = 0; pos < AllConnectionsList.getLength(); pos++) {

				Node presentJunNode = AllConnectionsList.item(pos);

				if (presentJunNode.getNodeType() == Node.ELEMENT_NODE) {

					Element presentJunElem = (Element) presentJunNode;
					int parentnumber = Integer.parseInt(presentJunElem.getAttribute("junction_id"));
					
					NodeList TagJunChldList = presentJunElem.getElementsByTagName("junction");
					//iteracja po dzieciach 'junction'
					for (int pos2 = 0; pos2 < TagJunChldList.getLength(); pos2++) {
						
						Element TagChildren = (Element) TagJunChldList.item(pos2);
						int id = Integer.parseInt(TagChildren.getAttribute("id"));
						double distance = Double.parseDouble(TagChildren.getAttribute("distance"));
						
						this.junctions.get(parentnumber).connections.add(this.junctions.get(id));
						
						this.matrix[parentnumber][id] = distance;	
					}
				}
			}//Heuristic cost Matrix
		for (Junction tmpr: this.junctions) {	
			for (Junction tmpc: this.junctions){
				
				this.heuristicCost[tmpr.number][tmpc.number] = 	GPS.distance(tmpr.id.latitude, tmpr.id.longitude, tmpc.id.latitude, tmpc.id.longitude);
			}
		}
		
		System.out.println("Distance presented in Kilometers computed from newXML file");
		System.out.print("GPS");
		
		for(int i = 0; i < this.junctions.size(); i++){
			System.out.print("|");
			System.out.printf("%6d",(i+1));
			System.out.print("|");
			
		}
		System.out.println("");
		for(int i = 0; i < this.junctions.size(); i++){
			System.out.printf("%3d",(i+1));
			for(int j = 0; j < this.junctions.size(); j++){
				if(this.matrix[i][j]!=0.0){
					System.out.print("|");
					System.out.printf("%6.3f",this.matrix[i][j]);
					System.out.print("|");
				}else{
					System.out.print("|");
					System.out.printf("%6s","-");
					System.out.print("|");
				}
			}	
			System.out.println("");
		}	
		System.out.println("Heuristic Cost presented in Kilometers computed from newXML file");
		System.out.print("GPS");
		
		for(int i = 0; i < this.junctions.size(); i++){
			System.out.print("|");
			System.out.printf("%6d",(i+1));
			System.out.print("|");
			
		}
		System.out.println("");
		for(int i = 0; i < this.junctions.size(); i++){
			System.out.printf("%3d",(i+1));
			for(int j = 0; j < this.junctions.size(); j++){
				
					System.out.print("|");
					System.out.printf("%6.3f",this.heuristicCost[i][j]);
					System.out.print("|");
				
			
			}	
			System.out.println("");
		}	
		System.out.println("Ordinals presented from 0 index");			
		}catch (Exception e) {
			e.printStackTrace();
		 }
	}

	
	public void SaveSerializable() throws Exception {
		   
		ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("Data/savedbin.ser")));
		out.writeObject(this);
		out.close(); 
	} 
	public static HeuristicMatrixGraph ReadSerializable() throws Exception {
		       
		ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream("Data/savedbin.ser")));
		HeuristicMatrixGraph graph = (HeuristicMatrixGraph) in.readObject();
		in.close();
		return graph;

	} 
/*public double[][] heuristicCost = {{0,2,5,0,2,0},
										{0,0,1,0,0,0},
										{0,0,0,2,0,1},
										{0,2,0,0,0,0},
										{0,0,0,0,0,1},
										{4,0,0,1,0,0}};*/
	
	/*public double[][] matrix = {{0,1,0,1,0,0,0,0},
								{1,0,1,0,0,0,0,0},
								{0,1,0,0,0,0,0,0},
								{1,0,0,0,0,2,1,5},
								{0,0,0,0,0,0,0,0},
								{0,0,0,2,0,0,3,0},
								{0,0,0,1,0,5,0,1},
								{0,0,0,5,0,0,1,0}};*/
}