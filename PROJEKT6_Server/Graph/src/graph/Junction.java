package graph;
import java.util.*;
import java.io.*;

public class Junction implements Serializable {
	public int number;
	public Identifier id;
	public List<Junction> connections;
	
	public Junction(Identifier id, int num){
		this.number = num;
		this.id = id;
		this.connections = new ArrayList<Junction>();
	}
	
/*	public void arrangeConnections(List<Way> allways, List<Junction> alljunctions){
		boolean tmpcontained = false;
		
		for(Way tmpway: allways){
			for(Identifier tmpreff: tmpway.refs){	
				//Skrzyzowanie this na drodze! Jesli id skrzyzowania jest rowne  id referencyjnemu z napotkanych w danej drodze to trzeba inne skrzyzowania z tej drogi dolaczyc! akcja w kolejnym if!
				if(tmpreff.id.equals(this.id.id))
					tmpcontained = true;
			}
			//if(tmpway.refs.contains(this.id))//Dodawanie skrzyzowan wystepujacych na drodze z skrzyzowaniem reprz. przez this
			if(tmpcontained)
				for(Identifier tmpref: tmpway.refs){				
					if(!(tmpref.id.equals(this.id.id))){
						for(Junction tmpjunction: alljunctions)
						{
							if(tmpjunction.id.id.equals(tmpref.id))
								this.connections.add(tmpjunction);
						}
					}	
				}
			tmpcontained = false;
		}
	}*/
	public void info(){	
		System.out.println("*************************");
		System.out.println("Number:"+(this.number+1)+" | "+this.id.GetIdDetails());
		System.out.println("Assigned to");
		for(Junction tmp: connections){	
			System.out.println("Number:"+(tmp.number+1)+" | "+tmp.id.GetIdDetails());
		}
		System.out.println("*************************");
	}
	
	//public boolean checkIfcreated(Identifier id){
	//	return this.id.compare(id);
	//}

}
