package xmlhandler;
//http://www.mkyong.com/java/how-to-create-xml-file-in-java-dom/

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import graph.Junction;

public class WriteXMLFile {
	
	StreamResult result;
	
	public WriteXMLFile(DOMHandler doc){
		
		try {

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements - General Graph
			Document GraphXML = docBuilder.newDocument();
			Element rootElement = GraphXML.createElement("Graph");
			Attr tmp_attribute = GraphXML.createAttribute("nodes_amount");
			tmp_attribute.setValue(Integer.toString(doc.AllJunctions.size()));
			rootElement.setAttributeNode(tmp_attribute);
			GraphXML.appendChild(rootElement);
			// shorten way rootElement.setAttribute("id", "1");
			// Creating childs of rootElement
			for(Junction junjun: doc.AllJunctions){
				
				Element Junction = GraphXML.createElement("junction");
				Junction.setAttribute("id",Integer.toString(junjun.number));
				Junction.setAttribute("roadname",junjun.id.roadname);
				Junction.setAttribute("longitude", Double.toString(junjun.id.longitude));
				Junction.setAttribute("latitude", Double.toString(junjun.id.latitude));
				rootElement.appendChild(Junction);		
			}
			///////Now Connections with Junctions
			for(Junction junjun: doc.AllJunctions){
				Element Connection = GraphXML.createElement("connections_of");
				rootElement.appendChild(Connection);
				Connection.setAttribute("junction_id", Integer.toString(junjun.number));
				
				for(Junction concon: junjun.connections){
					Element tmpjunction = GraphXML.createElement("junction");
					tmpjunction.setAttribute("id", Integer.toString(concon.number));
					tmpjunction.setAttribute("distance", Double.toString(doc.matrix[junjun.number][concon.number]));
					Connection.appendChild(tmpjunction);
				}
			}
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(GraphXML);
			this.result = new StreamResult(new File("MyGraph.xml"));

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);
			transformer.transform(source, result);		
			System.out.println("New XML File saved!");

		  } catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		  } catch (TransformerException tfe) {
			tfe.printStackTrace();
		  }
		}
}