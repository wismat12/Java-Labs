package xmlhandler;
import dijkstra.*;
//import graph.*;
import java.io.*;
import java.nio.charset.Charset;

import com.thoughtworks.xstream.*;

public class Xstream {
	
	public static void XStreamSave(HeuristicMatrixGraph graph){
		
		XStream xstream = new XStream();
		OutputStream file;
		Writer writer;
		try {
			file = new FileOutputStream("Data/savedXstream.xml");
			writer = new OutputStreamWriter(file, Charset.forName("UTF-8"));
			
			xstream.aliasField("SourceData", HeuristicMatrixGraph.class, "newxml");
			xstream.alias("Graph", HeuristicMatrixGraph.class);
			xstream.addImplicitCollection(HeuristicMatrixGraph.class, "junctions");
			xstream.alias("Junction", graph.Junction.class);
			xstream.useAttributeFor(graph.Identifier.class, "id");
            xstream.aliasField("details", graph.Junction.class, "id");
            xstream.aliasField("row", double[][].class, "double-array");
            
			xstream.toXML(graph, writer);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		//System.out.println(xml);
	}
	public static HeuristicMatrixGraph XStreamRead(){
		
		XStream xstream = new XStream();
		
		xstream.aliasField("SourceData", HeuristicMatrixGraph.class, "newxml");
		xstream.alias("Graph", HeuristicMatrixGraph.class);
		xstream.addImplicitCollection(HeuristicMatrixGraph.class, "junctions");
		xstream.alias("Junction", graph.Junction.class);
		xstream.useAttributeFor(graph.Identifier.class, "id");
        xstream.aliasField("details", graph.Junction.class, "id");
        xstream.aliasField("row", double[][].class, "double-array");
		
		File file = new File("Data/savedXstream.xml");
		
		HeuristicMatrixGraph graph = (HeuristicMatrixGraph) xstream.fromXML(file);
		
		//System.out.println(xstream);
		
		return graph;
	}

}
