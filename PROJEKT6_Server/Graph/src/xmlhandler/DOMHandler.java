package xmlhandler;

import graph.*;

import java.util.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

class pair{
	Identifier first;
	Identifier second;
	pair(Identifier first, Identifier second){
		this.first = first;
		this.second = second;
	}
	
}

public class DOMHandler{
	
	File xml;
	public List<Way> CorrectWays;
	public List<Identifier> AllNodes;
	public List<Junction> AllJunctions;
	public List<pair> Cuts;
	public double[][] matrix;
	
	public DOMHandler(){
		
		try {
			//this.xml = new File("giant");
			this.xml = new File("mapka");
			//this.xml = new File("test.txt");
			//this.xml = new File("map2");
			CorrectWays = new ArrayList<Way>();
			AllNodes = new ArrayList<Identifier>();
			AllJunctions = new ArrayList<Junction>();
			Cuts = new ArrayList<pair>();
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xml);
			boolean addedWay = false;
	
			//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();

			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

			NodeList AllWayList = doc.getElementsByTagName("way");
			NodeList AllNodeList = doc.getElementsByTagName("node");

			System.out.println("----------------------------");
			
			//Iteracja po Liscie Node'ow + tworzenie listy ze wszystkimi node'ami o poprawnych danych...
			for (int Nodepos = 0; Nodepos < AllNodeList.getLength(); Nodepos++) {
				
				Element presentNode =(Element) AllNodeList.item(Nodepos);
				//System.out.println(Nodepos);
				
				String ID =  presentNode.getAttribute("id");
				if(ID.length() > 9)
					break;
				//System.out.println(ID);
				double lat = Double.parseDouble(presentNode.getAttribute("lat"));
				double lon = Double.parseDouble(presentNode.getAttribute("lon"));
				Identifier newId = new Identifier(ID,lon,lat);
				this.AllNodes.add(newId);	
				/*if(ID.length() == 9){					
					double lat = Double.parseDouble(presentNode.getAttribute("lat"));
					double lon = Double.parseDouble(presentNode.getAttribute("lon"));
					Identifier newId = new Identifier(ID,lon,lat);
					this.AllNodes.add(newId);
				}*/
			}
			System.out.println("Nodes Loaded!");
			//iteracja po liscie Way'ow
			for (int pos = 0; pos < AllWayList.getLength(); pos++) {

				Node presentWayNode = AllWayList.item(pos);

				if (presentWayNode.getNodeType() == Node.ELEMENT_NODE) {

					Element presentWayElem = (Element) presentWayNode;
					//System.out.println("Staff id : " + eElement.getAttribute("id"));
					NodeList TagWayChldList = presentWayElem.getElementsByTagName("tag");
					//iteracja po dzieciach 'tag'
					for (int pos2 = 0; pos2 < TagWayChldList.getLength(); pos2++) {
						
						Element TagChildren = (Element) TagWayChldList.item(pos2);
						String a = TagChildren.getAttribute("k");
						String b = TagChildren.getAttribute("v");
						//System.out.println(a + b );
						//Jeœli jest ulica...
						if((a.equals("highway"))&&(b.equals("residential"))){
						//	String c = eeElement.getAttribute("k");
							Way NewWay = new Way();
							NodeList RefWayChildList = presentWayElem.getElementsByTagName("nd");
							Way.WayCounter++;
						
							for (int j = 0; j < RefWayChildList.getLength(); j++) {
								
								Element RefTmpElement = (Element) RefWayChildList.item(j);
								
								String ref = RefTmpElement.getAttribute("ref");
								
								if(ref.length() == 9){
									Identifier NewId = new Identifier(ref);
									NewWay.refs.add(NewId);
								}	
							}
							this.CorrectWays.add(NewWay);	
							addedWay = true;
						}
						if((a.equals("name")&&addedWay)){
							
							 this.CorrectWays.get(CorrectWays.size() - 1).name = b;	
							 break;
						}
					}
				}
				addedWay = false;
			}
		
		   }catch (Exception e) {
			   e.printStackTrace();
		   	}
	}
	public void SetWaysRefCoordinate(){
		for(Way way: CorrectWays)
			for(Identifier a: way.refs){
				a.SetCoordinates(AllNodes);
				a.roadname = way.name;
			}
	}
	
	public void ShowCorrectWays(){
		for (Way a : this.CorrectWays) {
			a.showrefs();
		}
	}
	public void ShowEveryNodes(){
		for (Identifier a : this.AllNodes) {
			System.out.println(a.GetIdDetails());
		}
	}
	public void ShowEveryJunctions(){
		for(Junction tmpj: AllJunctions){
			tmpj.info();
		}
	}
	public void PrepareJunctions(){
		boolean junjustcreated = false;
		int counter = -1;
		for(Way way: CorrectWays){
			
			for(int i = 0; i < way.refs.size() - 1; i++){
				
				pair cut = new pair(way.refs.get(i), way.refs.get(i+1));
				this.Cuts.add(cut);
				
			}
			for(Identifier a: way.refs){
				
				if(this.AllJunctions.size() == 0){
					Junction NewJun = new Junction(a,++counter);
					AllJunctions.add(NewJun);	
				}else{
					for(Junction juntmp: AllJunctions){
						//if(juntmp.checkIfcreated(a)){
						//Odwolanie sie do stringa reprezentujacego kod referencyjny danego skrzyzowania - sprawdzenie czy dane skrzyzowanie juz istnieje!
						if(juntmp.id.id.equals(a.id)){
							//System.out.println("Number " + a.GetIdDetails());	
							junjustcreated = true;
							break;
						}
					}
					if(junjustcreated==false){
						Junction NewJun = new Junction(a,++counter);
						AllJunctions.add(NewJun);
					}
					junjustcreated = false;	
				}
			}
		}
		System.out.println(this.Cuts.size());
	
		for(Junction juntmp: AllJunctions){	
			for(pair cut: this.Cuts){
				if(juntmp.id.id.equals(cut.first.id)){
					Identifier search = cut.second;
					for(Junction junjuntmp: AllJunctions){	
						if(junjuntmp.id.id.equals(search.id)){
							juntmp.connections.add(junjuntmp);
						}
					
					}
				}else if(juntmp.id.id.equals(cut.second.id)){
					Identifier search = cut.first;
					for(Junction junjuntmp: AllJunctions){	
						if(junjuntmp.id.id.equals(search.id)){
							juntmp.connections.add(junjuntmp);
						}
					
					}
				}	
			}
		}
		System.out.println("Number of properly created junctions: " + this.AllJunctions.size());	
	}
	
	public void prepareGPSmatrix(){
		int size = this.AllJunctions.size();
		matrix = new double[size][size];
		//Zalatwione przez inicjalizacje domyslna!
		/*for(int i = 0; i < size; i++)
			for(int j = 0; j < size; j++)
				matrix[i][j] = 0.0;*/	
		for(Junction tmpj: AllJunctions){
			for(Junction tmpjconnection: tmpj.connections){
				matrix[tmpj.number][tmpjconnection.number] = GPS.distance(tmpj.id.latitude, tmpj.id.longitude, tmpjconnection.id.latitude, tmpjconnection.id.longitude);
			}
		}
	}
	public void printGPSmatirx(){
		//System.out.printf("%-25s : %25s%n", "left justified", "right justified");
		System.out.println("Distance presented in Kilometers");
		System.out.print("GPS");
		for(int i = 0; i < this.AllJunctions.size(); i++){
			System.out.print("|");
			System.out.printf("%6d",(i+1));
			System.out.print("|");
			
		}
		System.out.println("");
		for(int i = 0; i < this.AllJunctions.size(); i++){
			System.out.printf("%3d",(i+1));
			for(int j = 0; j < this.AllJunctions.size(); j++){
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
	}
	public void savetofile()throws IOException{
		
		PrintWriter out = new PrintWriter(new FileWriter("Out_Graph.txt",false));
		
		out.printf("%s"+"%n","Distance presented in Kilometers: "+this.AllJunctions.size()+" Junctions were created!");
		out.printf("GPS");
		for(int i = 0; i < this.AllJunctions.size(); i++){
			out.printf("|");
			out.printf("%6d",(i+1));
			out.printf("|");
		}
		out.printf("%n","");
		
		for(int i = 0; i < this.AllJunctions.size(); i++){
			out.printf("%3d",(i+1));
			for(int j = 0; j < this.AllJunctions.size(); j++){
				if(this.matrix[i][j]!=0.0){
					out.printf("|");
					out.printf("%6.3f",this.matrix[i][j]);
					out.printf("|");
				}else{
					out.printf("|");
					out.printf("%6s","-");
					out.printf("|");
				}	
			}	
			out.printf("%n","");
		}
		out.close();		
	}
}