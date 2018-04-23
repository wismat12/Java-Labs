package graph;
import java.util.*;

public class Way {
	public static int WayCounter = 0; 
	public String name;
	public List<Identifier> refs = null;
	
	public Way(){
		this.name = "none";
		this.refs = new ArrayList<Identifier>();
	}
	
	public void showrefs(){
		System.out.println("Name: " +this.name);
		for (Identifier a : this.refs) {
			System.out.println(a.GetIdDetails());
		}
		
	}
/*	public void SetrefsCoordinate(){
		for(Identifier a: this.refs)
			a.SetCoordinates(allnodes);
	}
	*/
	/*
	private String name;
	private int id;*/
/*
public void Setname(String name){
	this.name = name;
}
public void Setid(int id){
	this.id = id;
}
public String Getname(){
	return this.name;
}
public int Getint(){
	return this.id;
}*/
/*
@Override
public String toString() {
    return "Way:: Name="+this.name+" Id=" + this.id;
}*/
}