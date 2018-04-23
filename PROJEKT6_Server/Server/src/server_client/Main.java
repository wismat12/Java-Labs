package server_client;

import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		
		try {
			Server server_1 = new Server();
			
			Client user_1 = new Client("user_1",1,4,"Cze�� Serwerze, co s�ycha�?","Mog� od Ciebie dosta� Obiekt Grafu? Chc� sprawdzi� dystans miedzy 1 - 4");
			Client user_2 = new Client("user_2",5,6,"Witaj Serwerze","Prze�lij mi Graf, prosz�. Chc� sprawdzi� dystans miedzy 5 - 6");
			Client user_3 = new Client("user_3",3,5,"Dzie� Dobry Serwerze","Mam pro�b�,", "Potrzebuj� Twojego Grafu. Chc� sprawdzi� dystans miedzy 3 - 5");
			
			server_1.exec.shutdown();
			while(true){
				if(server_1.exec.isTerminated()){
					System.out.println("<EXECUTOR> All tasks have completed following shut down");
					server_1.serversocket.close();
					server_1.main_server_thread.join();
					break;
				}
			}
		} catch (IOException | InterruptedException e) {
		
			e.printStackTrace();
		}
		
		System.out.println("***koniec watku g��wnego!!!***");
	}

}
