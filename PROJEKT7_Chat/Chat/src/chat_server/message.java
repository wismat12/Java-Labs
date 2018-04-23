package chat_server;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Queue;

import javax.swing.JTextPane;

public class message implements Serializable{
	
	String direction;
	String id;
	String from;
	Object uknown_content;
	
	message(){
		
	}
	message(String direction, String id){  //komunikat dla serwera
		
		this.direction = direction;
		this.id = id;
		
		
	}
	message(String direction, String id, String phrase){  //komunikat dla lkienta
		
		this.direction = direction;
		this.id = id;
		this.uknown_content = phrase;
		
		
	}

	message(String direction, String id, String[] list){  //list
		
		this.direction = direction;
		this.id = id;
		this.uknown_content = list;
		
		
	}
	message(String direction, String id, JTextPane ms){  //wiadomoœæ
		
		this.direction = direction;
		this.id = id;
		this.uknown_content = ms;
		
		
	}
	message(String direction, String id, JTextPane ms, String aim){  //wiadomoœæ kierunkowa
		
		this.direction = direction;
		this.id = id;
		this.from = aim;
		this.uknown_content = ms;
		
		
	}


}
