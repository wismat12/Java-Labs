package server_client;

import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		
		try {
			Server server_1 = new Server();
			
			Client user_1 = new Client("user_1",1,4,"Czeœæ Serwerze, co s³ychaæ?","Mogê od Ciebie dostaæ Obiekt Grafu? Chcê sprawdziæ dystans miedzy 1 - 4");
			Client user_2 = new Client("user_2",5,6,"Witaj Serwerze","Przeœlij mi Graf, proszê. Chcê sprawdziæ dystans miedzy 5 - 6");
			Client user_3 = new Client("user_3",3,5,"Dzieñ Dobry Serwerze","Mam proœbê,", "Potrzebujê Twojego Grafu. Chcê sprawdziæ dystans miedzy 3 - 5");
			
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
		
		System.out.println("***koniec watku g³ównego!!!***");
	}

}
