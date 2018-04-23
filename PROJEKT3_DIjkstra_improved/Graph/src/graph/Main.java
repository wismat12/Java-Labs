package graph;

import java.io.IOException;

import xmlhandler.*;
import dijkstra.*;
import astar.Astar;

public class Main {

	public static void main(String[] args) {
/*		
		//Wywolywany przez konstruktor handler tworzy liste odpowiednich identyfikatorow i poprawnych uliczek!
		DOMHandler document = new DOMHandler();
		
		System.out.println("Ways from size: " +document.CorrectWays.size());
		
		//Metoda za duzo wypisujaca!
		//document.ShowEveryNodes();
		
		document.SetWaysRefCoordinate();
		
		//System.out.println("Koordynaty ustawione!");
		
		document.ShowCorrectWays(); 
		
		//Kazda droga posiada referencje ktore oznaczaja skrzyzowania, metoda tworzy nowe skrzyzowania dla nowo napotkanej referencji!
		//W tej metodzie wywoluje takze aranzacje polaczen do kazdego skrzyzowania (Wa¿na!-z klasy Junction)!!!
		document.PrepareJunctions();
	
		document.ShowEveryJunctions(); 
		
		document.prepareGPSmatrix();
		
		//Metoda za duzo wypisujaca!
	//	document.printGPSmatirx(); 
		
	    try {
			document.savetofile();
		} catch (IOException e) {

			e.printStackTrace();
		}
	    System.out.println("Done! "+document.AllJunctions.size()+" Nodes/Junctions were created!");
	    
	    WriteXMLFile Output = new WriteXMLFile(document);
*/
		
		HeuristicMatrixGraph readedXstream;
		readedXstream = Xstream.XStreamRead();
	    Dijkstra testXstream = new Dijkstra(readedXstream,5,4);
		
		/*
	    HeuristicMatrixGraph data = new HeuristicMatrixGraph();
	    HeuristicMatrixGraph readedObjectStream;
	    HeuristicMatrixGraph readedXstream;
	    
	    System.out.println("***Zapisanie Danych XStream!!!***");
	    Xstream.XStreamSave(data);
	    System.out.println("***Wczytanie Danych XStream!!!***");
	    readedXstream = Xstream.XStreamRead();
	    Dijkstra testXstream = new Dijkstra(readedXstream,5,4);
	    
	    System.out.println("***Zapisanie Danych Binarnych ObjectStream!!!***");
		 try {
			data.SaveSerializable();
			System.out.println("***Wczytanie Danych Binarnych ObjectStream!!!***");
			readedObjectStream = HeuristicMatrixGraph.ReadSerializable();
			Dijkstra testObjectStream = new Dijkstra(readedObjectStream,1,4);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	    
		 
		 */
	    /* drawing a = new drawing();
	    a.draw(document);*/	
	}
}