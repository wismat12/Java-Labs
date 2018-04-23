package chat_server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;



public class Server_worker_th implements Runnable{
	
	String works_with;
	Server_worker_th talks_with = null;
	Socket socket_reference;
	//Socket socket_reference_conv;
	Thread receiver;
	Thread_queue_send sender;
	//Thread_queue_receive help2;
	//SynchronousQueue<message> queue_main = new SynchronousQueue<message>();
	LinkedBlockingQueue<message> queue1 = new LinkedBlockingQueue<message>();
	//LinkedBlockingQueue<message> queue2 = new LinkedBlockingQueue<message>();
	ObjectOutputStream os;
	ObjectInputStream is;
	
	Server_worker_th(Socket socket){
		
		this.receiver = new Thread(this);
		this.socket_reference = socket;
		this.sender = new Thread_queue_send();
		//this.help2 = new Thread_queue_receive();
		this.receiver.start();	
		
	}

	@Override
	public void run() {
		try {
			is = new ObjectInputStream(Server_worker_th.this.socket_reference.getInputStream());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			
			Server_worker_th.this.queue1.put(new message("SERVER","INFO","Po³¹czono z Serwerem"));
			Server.clients_amount++;
			Server.workers_list.add(Server_worker_th.this);		
			
			message name;
			try {
				name = (message) is.readObject();
				String client_name = (String) name.uknown_content;
	
				this.works_with = client_name;
				Server.clients_list.add(client_name);
				Server_worker_th.this.queue1.put(new message("SERVER","INFO","Server zaakceptowa³ Twój nick: "+client_name));
				
				synchronized(Server.dialog.textArea){
					Server.dialog.textArea.setText("");
					for(Server_worker_th t: Server.workers_list){
						Server.dialog.textArea.append("User: "+ t.works_with + "\n"+"Socket["+ t.works_with+"]: "+ t.socket_reference + "\n");
					}
				}
				synchronized(Server.dialog.label){
					Server.dialog.label.setText(Integer.toString(Server.clients_amount));
				}					
				if(Server.clients_amount > 1){
					Queue<String> tmp2 = new ConcurrentLinkedQueue<String>(Server.clients_list);
				
					for(Server_worker_th t: Server.workers_list){
						Queue<String> tmp3 = new ConcurrentLinkedQueue<String>(tmp2);
						tmp3.remove(t.works_with);
						tmp3.remove(t.talks_with);
						if(tmp3.size() == 0){
							
							t.queue1.put(new message("SERVER","INFO","Brak Dostêpnych Userów"));
							t.queue1.put(new message("SERVER","EMPTYLIST"));
							
						}else{
							
							String[] arr1 = tmp3.toArray(new String[tmp3.size()]);
							t.queue1.put(new message("SERVER","LIST",arr1));
							t.queue1.put(new message("SERVER","INFO","Server przesy³a listê userów"));
						}		
					}	
				}	
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}

			while(!this.receiver.isInterrupted())
		    {		
				message msg;
				try {
					//synchronized(is){
						msg = (message) is.readObject();
									
						if(msg.id.equals("LIST")){	
							
							Queue<String> tmp2 = new ConcurrentLinkedQueue<String>(Server.clients_list);
							
							tmp2.remove(Server_worker_th.this.works_with);
							tmp2.remove(Server_worker_th.this.talks_with);
							if(tmp2.size() == 0){
								
								Server_worker_th.this.queue1.put(new message("SERVER","INFO","Brak Dostêpnych Userów"));
								Server_worker_th.this.queue1.put(new message("SERVER","EMPTYLIST"));
								
							}else{
								
								String[] arr1 = tmp2.toArray(new String[tmp2.size()]);
								Server_worker_th.this.queue1.put(new message("SERVER","LIST",arr1));
								Server_worker_th.this.queue1.put(new message("SERVER","INFO","Server przesy³a listê userów"));
							}													
						
						}else if(msg.id.equals("CHOSEN")){
							
							/*if(Server_worker_th.this.talks_with!=null ){
								Server_worker_th.this.talks_with.queue1.put(new message("CLIENT","DISCONNECTED"));
							}*/	
							String talks_to = (String) msg.uknown_content;
							
							for(Server_worker_th t: Server.workers_list){
								if(talks_to.equals(t.works_with)){
							
									this.talks_with = t;
									//t.talks_with = this;
									//System.out.println("User " + this.works_with + socket_reference +" z " + t.works_with + t.socket_reference);
								}
							}
							//Server_worker_th.this.queue1.put(new message("CLIENT","CONNECTED", Server_worker_th.this.talks_with.works_with));
							//Server_worker_th.this.talks_with.queue1.put(new message("CLIENT","CONNECTED", Server_worker_th.this.works_with));
							
						}else if(msg.direction.equals("SERVER")&&(msg.id.equals("END"))){
							Server.clients_list.remove(Server_worker_th.this.works_with);
							Server.workers_list.remove(Server_worker_th.this);
							Server.clients_amount--;
						
							for(Server_worker_th t: Server.workers_list){
								
								Queue<String> tmp3 = new ConcurrentLinkedQueue<String>(Server.clients_list);
								tmp3.remove(t.works_with);
								System.out.println(tmp3);
								System.out.println(tmp3.size());
								if(tmp3.size() == 0){
									
									t.queue1.put(new message("SERVER","INFO","Brak Dostêpnych Userów"));
									t.queue1.put(new message("SERVER","EMPTYLIST"));
									
								}else{
									
									String[] arr1 = tmp3.toArray(new String[tmp3.size()]);
									t.queue1.put(new message("SERVER","LIST",arr1));
									t.queue1.put(new message("SERVER","INFO","Server przesy³a listê userów"));
								}		
							}
							synchronized(Server.dialog.textArea){
								Server.dialog.textArea.setText("");
								for(Server_worker_th t: Server.workers_list){
									Server.dialog.textArea.append("User: "+ t.works_with + "\n"+"Socket["+ t.works_with+"]: "+ t.socket_reference + "\n");
								}
							}
							synchronized(Server.dialog.label){
								Server.dialog.label.setText(Integer.toString(Server.clients_amount));
							}
							
							try {
								Server_worker_th.this.socket_reference.close();
							} catch (IOException e1) {
								
							}
							Server_worker_th.this.sender.thread.interrupt();
							Server_worker_th.this.receiver.interrupt();		
							//Server_worker_th.this.help2.thread.interrupt();
							
						}else if(msg.direction.equals("CLIENT")&&(msg.id.equals("MESSAGE"))){
							
							System.out.println("Redirecting message");
							Server_worker_th.this.talks_with.queue1.put(msg);
						}
					//}
				} catch (ClassNotFoundException | IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}	
		    }
		
		}catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}
	private class Thread_queue_send implements Runnable{
		
		Thread thread;

		Thread_queue_send(){
			this.thread = new Thread(this);
			this.thread.start();	
		}
		@Override
		public void run() {
			try {
				os = new ObjectOutputStream(Server_worker_th.this.socket_reference.getOutputStream());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			while(!this.thread.isInterrupted())
		    {
				try {
					message msg = Server_worker_th.this.queue1.take();
					
						//synchronized(os){
							os.writeObject(msg);
							System.out.println(msg.id+"  "+msg.uknown_content+" "+Server_worker_th.this.works_with);
						//}				
					
				} catch (IOException | InterruptedException e) {
					//e.printStackTrace();
				}
		    }
		}
	}
	/*private class Thread_queue_receive implements Runnable{
		
		Thread thread;

		Thread_queue_receive(){
			this.thread = new Thread(this);
			this.thread.start();	
		}
		@Override
		public void run() {
			
			while(!this.thread.isInterrupted())
		    {
				
		    }
		}
	}*/
}