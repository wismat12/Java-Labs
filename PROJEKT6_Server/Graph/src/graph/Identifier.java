package graph;
import java.util.List;
import java.io.*;

//klasa reprezentujaca pojedyncza referencje do node'a zawierajacego wspolrzedne geograficzne
public class Identifier implements Serializable {
	
	public String id;
	public double longitude;
	public double latitude;
	public String roadname = null;
	
	public Identifier(String id){
		this.id = id;
		this.longitude = 0;
		this.latitude = 0;
	}
	public Identifier(String id,  String roadname){
		this.id = id;
		this.longitude = 0;
		this.latitude = 0;
		this.roadname = roadname;
	}
	public Identifier(String id, double lon, double lat){
		this.id = id;
		this.longitude = lon;
		this.latitude = lat;
	}
	public Identifier(String id, double lon, double lat, String roadname){
		this.id = id;
		this.longitude = lon;
		this.latitude = lat;
		this.roadname = roadname;
	}

	public String GetIdDetails(){
		return "ID: " + this.id + " NameofRoad: " + this.roadname + " | lon: " + this.longitude + " | lat: " + this.latitude; 
	}
	public void SetCoordinates(List<Identifier> allnodes){
		
		for(Identifier a: allnodes)
			if(this.id.equals(a.id)){
				this.longitude = a.longitude;
				this.latitude = a.latitude;
			}
	}
}