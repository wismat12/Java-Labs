package chat_server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;


public class Server implements Runnable{
	
	public static volatile int clients_amount = 0;
	static Queue<String> clients_list = new ConcurrentLinkedQueue<String>();
	static Queue<Server_worker_th> workers_list = new ConcurrentLinkedQueue<Server_worker_th>();
	public static final int PORT = 4445;
	Thread main_server_thread;
	ServerSocket serversocket;
	Socket socket;
	static Server_dialogg dialog;

	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					dialog = new Server_dialogg();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
					dialog.textArea.setText("Server is listening for connections...");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}
	Server() throws IOException, InterruptedException{	
		main_server_thread = new Thread(this);
		main_server_thread.start();	
	}
	
	public void runServer() throws IOException, InterruptedException{
			
		this.serversocket = new ServerSocket(PORT);

		do{
			try{
				this.socket = serversocket.accept();
				Server_worker_th serverthread = new Server_worker_th(socket);

			}catch (SocketException e){
			}
		}while(true);
	}

	@Override
	public void run() {
		try {
			runServer();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
	class Server_dialogg extends JDialog {

		private static final long serialVersionUID = 1L;
		private final JPanel contentPanel = new JPanel();
		JButton cancelButton;
		JButton okButton;
		Server server;
		JTextArea textArea;
		JLabel label;
	
		public Server_dialogg() {
			setBounds(100, 100, 450, 300);
			getContentPane().setLayout(new BorderLayout());
			contentPanel.setLayout(new BorderLayout());
			contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
			getContentPane().add(contentPanel, BorderLayout.CENTER);
			{
				JToolBar toolBar = new JToolBar();
				contentPanel.add(toolBar, BorderLayout.NORTH);
				{
					JLabel lblLiczbaUserw = new JLabel("Users Amount: ");
					toolBar.add(lblLiczbaUserw);
				}
				{
					label = new JLabel("0");
					toolBar.add(label);
				}
			}
			{
				textArea = new JTextArea();
				//textArea.setEnabled(false);
				textArea.setEditable(false);
				textArea.setForeground(Color.black);
				contentPanel.add(textArea, BorderLayout.CENTER);
			}
			{
				JPanel buttonPane = new JPanel();
				buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
				getContentPane().add(buttonPane, BorderLayout.SOUTH);
				{
					JButton okButton = new JButton("Start Server");
					okButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							
							try {
								Server_dialogg.this.server = new Server();
							} catch (IOException | InterruptedException e1) {
								e1.printStackTrace();
							}
							okButton.setEnabled(false);
							cancelButton.setEnabled(true);
						}
					});
					okButton.setActionCommand("OK");
					buttonPane.add(okButton);
					getRootPane().setDefaultButton(okButton);
				}
				{
					cancelButton = new JButton("Turn Off Server");
					cancelButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							
							try {
								Server_dialogg.this.server.serversocket.close();
							} catch (IOException e1) {
								
							}
							System.exit(0);
						}
					});
					cancelButton.setEnabled(false);
					cancelButton.setActionCommand("Cancel");
					buttonPane.add(cancelButton);
				}
				this.addWindowListener( new WindowAdapter(){
					
					//@Override
					public void windowClosing(WindowEvent e){
						try {
							Server_dialogg.this.server.serversocket.close();
						} catch (IOException e1) {
							
						}
						System.exit(0);
						
					}
				});
			}
		}
}