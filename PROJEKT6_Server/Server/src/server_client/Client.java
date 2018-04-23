package server_client;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import dijkstra.Dijkstra;
import dijkstra.HeuristicMatrixGraph;

public class Client {
	
	Socket socket;
	String id;
	
	Client(String id,int from, int to, String ...args) throws UnknownHostException, IOException{
		this.socket = new Socket("localhost", 4445);
		this.id = id;
		PrintWriter pw = new PrintWriter(this.socket.getOutputStream(), true);
		//BufferedReader buff = new BufferedReader(new InputStreamReader(socket_reference.getInputStream()));
		for(String  tmp: args){
			pw.println(this.id + " " + tmp);
		}
		
		class client_th implements Runnable{	
			
			Thread t;
			ObjectInputStream is;
			int from;
			int to;
			client_th(int from, int to){
				this.t = new Thread(this);
				this.t.start();
				this.from = from;
				this.to= to;
			}										
			@Override
			public void run() {
								
				//new ObjectInputStream(new BufferedInputStream(new FileInputStream("Data/savedbin.ser")));
				try {
					is = new ObjectInputStream(socket.getInputStream());
					HeuristicMatrixGraph graph = (HeuristicMatrixGraph) is.readObject();
					System.out.println("<CLIENT-" + t + "> Rozpoczyna obliczanie œcie¿ki");
					Dijkstra testObjectStream = new Dijkstra(graph,from,to);
					
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("<CLIENT-" + t + "> Koñczy obliczanie œcie¿ki");
				try {
					Client.this.socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		try {
			
			new client_th(from, to).t.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
	
		

	
	
	}

}
