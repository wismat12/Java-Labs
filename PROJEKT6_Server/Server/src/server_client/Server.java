package server_client;
import dijkstra.*;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;



public class Server implements Runnable{
	
	public static final int PORT = 4445;
	public final static String FILE_TO_SEND = "Data/savebin.ser";
	Thread main_server_thread;
	Thread clients_amount_listener;
	ServerSocket serversocket;
	Socket socket;
	int connections = 0;
	boolean start_activity = false;
	HeuristicMatrixGraph graph;
	
	ExecutorService exec = Executors.newFixedThreadPool(3);

	
	Server() throws IOException, InterruptedException{
		//G£ÓWNY W¹tek SERVERA
		main_server_thread = new Thread(this);
		main_server_thread.start();
		main_server_thread.setPriority(6);
		//Ladowanie pliku
		ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream("Data/savedbin.ser")));
		try {
			Server.this.graph = (HeuristicMatrixGraph) in.readObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		in.close();
	}
	
	public void runServer() throws IOException, InterruptedException{
		
		this.serversocket = new ServerSocket(PORT);
		System.out.println("<SERVER-" + this.main_server_thread + "> Serwer jest gotowy, czeka na po³¹czenia, PORT = " + PORT);

		
		//Accepting connections from multiple clients
		System.out.println("<EXECUTOR> Ready for execute Secondary Server Threads");
		do{
			try{
			//	System.out.println(this.connections);
				this.socket = serversocket.accept();
				ServerThread serverthread = new ServerThread(socket);
				this.connections++;			
			
				exec.execute(serverthread.thread);
				serverthread.thread.setPriority(4);
				
			}catch (SocketException e){
				//Closes this socket. Any thread currently blocked in accept() will throw a SocketException. 
				System.out.println("<SERVER-" + this.main_server_thread + "> Listening for connections to this.socket is closed. Any thread currently blocked in serversocket.accept() will throw a SocketException.");	
			}
			//serverthread.thread.join();		
		}while(this.connections != 0);
		System.out.println("<SERVER-" + this.main_server_thread + "> Brak aktywnych po³¹czeñ, Serwer zostaje wy³¹czony ");	
	}
	@Override
	public void run() {
		try {
			runServer();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private class ServerThread implements Runnable{
		Thread thread;
		Socket socket_reference;
		
		
		ServerThread(Socket socket){
			this.thread = new Thread(this);
			this.socket_reference = socket;
			
		}
		@Override
		public void run() {
			try {
				Server.this.start_activity = true;
				String message = null;
				BufferedReader buff = new BufferedReader(new InputStreamReader(socket_reference.getInputStream()));
				
				ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
				os.writeObject(graph);
			
				while(((message = buff.readLine()) != null)){
					System.out.println("<SERVER-" + this.thread + "> Nadchodz¹ca Wiadomoœæ: " + message);
				}
				
				
				System.out.println("<SERVER-" + this.thread + "> Koniec Pobocznego W¹tku Servera!");
				Server.this.connections--;
				//this.socket_reference.close();
				//System.out.println(Server.this.connections);
				
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
	}
}