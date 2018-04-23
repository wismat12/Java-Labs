package chat_server;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.rtf.RTFEditorKit;
import java.io.File;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.SystemColor;



public class Client_GUI extends JFrame {

	private int frame_width;
	private int frame_height;
	private JTextPane conversation_window;
	private JTextPane edytor;
	private Client client;
	String Client_id;
	JLabel label_client_id ;
	JTextPane text_SERVER;
	LinkedBlockingQueue<message> queue_main = new LinkedBlockingQueue<message>();
	JComboBox comboBox_users;
	String Chosen_id=null;
	String Connected_with;
	JLabel chat_info;
	private SimpleAttributeSet current_style;
	JButton btnRozlacz;
	JButton btnWylij;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Client_GUI window = new Client_GUI();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	/**
	 * Create the application.
	 */
	public Client_GUI() {
		getContentPane().setBackground(Color.LIGHT_GRAY);
		getContentPane().setForeground(Color.BLACK);
		
		JMenuBar menu = new JMenuBar();
		setJMenuBar(menu);
		
		JMenu menu_Plik = new JMenu("Plik");
		menu.add(menu_Plik);
		
		JLabel lblId = new JLabel("ID:");
		menu.add(lblId);
		
		this.label_client_id = new JLabel("");
		menu.add(label_client_id);
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {	
        
		/*GetID dialog = new GetID();
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setVisible(true);*/
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Komunikator");
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		initStyle();
		
		//frame_height = screenSize.height - 180;
		//frame_width =(int) (screenSize.width - screenSize.width/1.5);
		frame_height = 920;
		frame_width = 640;
		setSize(frame_width, frame_height);
		Dimension programmedsize = this.getSize();
		//this.setMaximumSize(programmedsize);
		//this.setMinimumSize(programmedsize);
		//this.setPreferredSize(programmedsize);
		
		this.setResizable(false);
		
		getContentPane().setLayout(new BorderLayout(0, 0));
		this.conversation_window = new JTextPane();
		conversation_window.setEditable(false);
		conversation_window.setForeground(Color.BLACK);
		JScrollPane conversation_window_scroll = new JScrollPane(this.conversation_window);
		//scroll.setBounds( (this.getWidth()/2) - (this.workspace_width/2), 0, frame_width, frame_height - 300 );
		//conversation_window_scroll.setSize(200, frame_height - 300);
		//Panel na conversation_window
		JPanel panel_conversation = new JPanel();
		conversation_window_scroll.setBounds(10, 30, frame_width-25, frame_height - 460 );
		panel_conversation.setLayout( null );
		panel_conversation.add( conversation_window_scroll );
		panel_conversation.setPreferredSize( new Dimension( frame_width, frame_height - 420 ) );
		panel_conversation.setBackground(Color.gray);
		
		//Tworzenie panelu zawartoœæ
		JPanel main_panel = new JPanel();
		main_panel.setBorder(new CompoundBorder());
		main_panel.setLayout(new BoxLayout(main_panel, BoxLayout.Y_AXIS));
		
		JToolBar toolBar = new JToolBar();
		toolBar.setAlignmentX(0);
		toolBar.setPreferredSize(new Dimension(frame_width,85));
		toolBar.setMaximumSize(new Dimension(frame_width,85));
		//System.out.println(frame_width);
		main_panel.add(new JSeparator());
		main_panel.add(toolBar);
		
		this.text_SERVER = new JTextPane();
		text_SERVER.setBackground(SystemColor.controlHighlight);
		text_SERVER.setEditable(false);
		//text_SERVER.setLineWrap(true);
		JScrollPane text_SERVER_scroll = new JScrollPane(this.text_SERVER);
		
		toolBar.add(text_SERVER_scroll);
		

		toolBar.addSeparator();
		JLabel lblDostpniUytkownicy = new JLabel("Dost\u0119pni U\u017Cytkownicy: ");
		toolBar.add(lblDostpniUytkownicy);
		toolBar.addSeparator();
		this.comboBox_users = new JComboBox();
		comboBox_users.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			/*	btnWylij.setEnabled(true);
				btnRozlacz.setEnabled(true);
				if(Client_GUI.this.Chosen_id!=null){
					save();
				}
				
				Client_GUI.this.Chosen_id = Client_GUI.this.comboBox_users.getSelectedItem().toString();
				message msg = new message("SERVER","CHOSEN",Client_GUI.this.Chosen_id);
				
				try {
					Client_GUI.this.queue_main.put(msg);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}*/
				Client_GUI.this.Chosen_id = Client_GUI.this.comboBox_users.getSelectedItem().toString();
				message msg = new message("SERVER","CHOSEN",Client_GUI.this.Chosen_id);
				try {
					Client_GUI.this.queue_main.put(msg);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				btnWylij.setEnabled(true);
				btnRozlacz.setEnabled(true);
				if(Client_GUI.this.Chosen_id!=null){
					save();
				}
				String text = Client_GUI.this.text_SERVER.getText();
				String nick = Client_GUI.this.Chosen_id;
				Client_GUI.this.text_SERVER.setText(text + "Server po³¹czy³ Ciê z " + nick +"\n");
				Client_GUI.this.text_SERVER.setCaretPosition(Client_GUI.this.text_SERVER.getDocument().getLength());
				Client_GUI.this.chat_info.setText(nick);
				Client_GUI.this.Connected_with = nick;
				Client_GUI.this.conversation_window.setText("");
				File file = new File("DATA/"+Client_GUI.this.Client_id+"/conversations/"+Client_GUI.this.Connected_with+".rtf");
		        if (file.exists()){
		        	
		        	 RTFEditorKit rtf = new RTFEditorKit();
					try {
						// load file into jTextPane
						FileInputStream fi = new FileInputStream("DATA/"+Client_GUI.this.Client_id+"/conversations/"+Client_GUI.this.Connected_with+".rtf");
						rtf.read(fi, Client_GUI.this.conversation_window.getDocument(), 0);
						System.out.println("");
						fi.close();
					} catch (Exception e) {
						System.out.println("err:" + e.toString());
					}
		       }
		       /* message msg1 = new message("SERVER","LIST");
				try {
					Client_GUI.this.queue_main.put(msg1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			*/
			}
		});
		comboBox_users.setModel(new DefaultComboBoxModel(new String[] {"-empty-"}));
		comboBox_users.setMaximumSize(new Dimension(150,25));
		comboBox_users.setMinimumSize(new Dimension(150,25));
		toolBar.add(comboBox_users);
		
		toolBar.addSeparator();
		/*JButton button_update_list = new JButton("Aktualizuj List\u0119");
		button_update_list.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				message msg = new message("SERVER","LIST");
				try {
					Client_GUI.this.queue_main.put(msg);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
		});
		toolBar.add(button_update_list);*/
		
		
		main_panel.add(new JSeparator());
		main_panel.add(panel_conversation);
		
		JLabel lbl_chat_info = new JLabel("Rozmowa z: ");
		lbl_chat_info.setForeground(Color.WHITE);
		lbl_chat_info.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lbl_chat_info.setBounds(220, 0, 118, 25);
		panel_conversation.add(lbl_chat_info);
		
		this.chat_info = new JLabel("Brak");
		chat_info.setForeground(Color.WHITE);
		chat_info.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		chat_info.setBounds(334, 5, 91, 14);
		panel_conversation.add(chat_info);
		main_panel.add(new JSeparator());
		
		//toolbar1 edytora
		JToolBar toolBar1_edytor = new JToolBar();
		toolBar1_edytor.setAlignmentX(0);
		toolBar1_edytor.setPreferredSize(new Dimension(frame_width,35));
		toolBar1_edytor.setMaximumSize(new Dimension(frame_width,35));
		main_panel.add(toolBar1_edytor);
		
		
		JLabel lblCzcionka = new JLabel();
		lblCzcionka.setText("Czcionka:");
		lblCzcionka.setToolTipText("Wybierz czcionk\u0119");
		lblCzcionka.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblCzcionka.setIcon(new ImageIcon(Client_GUI.class.getResource("/resources/toolbar_font.png")));
		toolBar1_edytor.add(lblCzcionka);
		toolBar1_edytor.addSeparator();
		
		JComboBox<String> box_font = new JComboBox<String>();
		box_font.setMaximumSize(new Dimension(150,25));
		prepareFonts(box_font);
		toolBar1_edytor.add(box_font);
		toolBar1_edytor.addSeparator();
		
		JLabel lblNewLabel = new JLabel("Rozmiar: ");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 11));
		toolBar1_edytor.add(lblNewLabel);
		toolBar1_edytor.addSeparator();
		
		JComboBox<String> box_size = new JComboBox<String>();
		box_size.setMaximumSize(new Dimension(70,25));
		box_size.setModel(new DefaultComboBoxModel<String>(new String[] {"9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "32", "34", "36", "38", "40"}));
		box_size.setSelectedIndex(8);
		toolBar1_edytor.add(box_size);
		toolBar1_edytor.addSeparator();
		JLabel lblKolor = new JLabel("Kolory:");
		toolBar1_edytor.add(lblKolor);
		toolBar1_edytor.addSeparator();
		JButton toolbar_fontcolor = new JButton("");
		
		toolbar_fontcolor.setIcon(new ImageIcon(Client_GUI.class.getResource("/resources/toolbar_colortext.png")));
		toolbar_fontcolor.setToolTipText("Kolor");
		toolBar1_edytor.add(toolbar_fontcolor);
		
		JToggleButton toolbar_bold = new JToggleButton();
		
		toolbar_bold.setIcon(new ImageIcon(Client_GUI.class.getResource("/resources/toolbar_bold.png")));
		toolBar1_edytor.add(toolbar_bold);
		
		JToggleButton toolbar_italic = new JToggleButton();
	
		toolbar_italic.setIcon(new ImageIcon(Client_GUI.class.getResource("/resources/toolbar_italic.png")));
		toolBar1_edytor.add(toolbar_italic);
		
		JToggleButton toolbar_underline = new JToggleButton("");
	
		toolbar_underline.setIcon(new ImageIcon(Client_GUI.class.getResource("/resources/toolbar_underline.png")));
		toolBar1_edytor.add(toolbar_underline);
		
		/*//toolbar2 edytora
		JToolBar toolBar2_edytor = new JToolBar();
		toolBar2_edytor.setAlignmentX(0);
		toolBar2_edytor.setPreferredSize(new Dimension(frame_width,35));
		toolBar2_edytor.setMaximumSize(new Dimension(frame_width,35));
		main_panel.add(toolBar2_edytor);*/
		
		//Panel na edytor
		this.edytor = new JTextPane();
		JScrollPane edytor_scroll = new JScrollPane(this.edytor);
		JPanel panel_edytor = new JPanel();
		edytor_scroll.setBounds(10, 10, frame_width-25, frame_height - 740 );
		panel_edytor.setLayout( null );
		panel_edytor.add( edytor_scroll );
		panel_edytor.setPreferredSize( new Dimension( frame_width, frame_height - 720 ) );
		panel_edytor.setBackground(Color.gray);
		
		main_panel.add(panel_edytor);
		
		//toolbar do wysy³ania
		JToolBar toolBar_send = new JToolBar();
		toolBar_send.setAlignmentX(0);
		toolBar_send.setPreferredSize(new Dimension(frame_width,45));
		toolBar_send.setMaximumSize(new Dimension(frame_width,45));
		main_panel.add(toolBar_send);
		
		btnWylij = new JButton("WY\u015ALIJ");
		btnWylij.setEnabled(false);
		btnWylij.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				append_to_conversation();
				save();
			}
		});
		toolBar_send.add(btnWylij);
		
		btnRozlacz = new JButton("WYLACZ KOMUNIKATOR");
		btnRozlacz.setEnabled(false);
		btnRozlacz.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save();
				the_end();
				System.exit(0);
				
			}
		});
		toolBar_send.add(btnRozlacz);
		
		
		getContentPane().add(main_panel, BorderLayout.NORTH); //Dodanie maina na szczyt
		
		
		
		//Anonimowa klasa wew. monitorujaca zmiany w przestrzeni roboczej
				//bez Invoke by³o Attempt to mutate in notification
				Client_GUI.this.edytor.getDocument().addDocumentListener(new DocumentListener(){
					private void setter() {
		                Runnable Change = new Runnable() {
		                    @Override
		                    public void run() {
		                    	
		                    	StyledDocument doc = Client_GUI.this.edytor.getStyledDocument();
		    					int cursorPosition = Client_GUI.this.edytor.getCaretPosition();
		    					doc.setCharacterAttributes(cursorPosition-1, 1,current_style, true);

		                    }
		                };
		                EventQueue.invokeLater(Change);
		            }
					  public void insertUpdate(DocumentEvent e){
							 setter();
					  }
					  public void removeUpdate(DocumentEvent e){}
					  public void changedUpdate(DocumentEvent e){}

				});
		
		//CZCIONKA
				box_font.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						StyleConstants.setFontFamily(current_style,box_font.getSelectedItem().toString());
						//StyleConstants.setFontSize(current_style, Integer.parseInt(box_size.getSelectedItem().toString()));

					}
				});
		//BOLD
		toolbar_bold.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StyleConstants.setBold(current_style, toolbar_bold.isSelected());
			}
		});
		//ITALIC
		toolbar_italic.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StyleConstants.setItalic(current_style, toolbar_italic.isSelected());
			}
		});
		//UNDERLINE
		toolbar_underline.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StyleConstants.setUnderline(current_style, toolbar_underline.isSelected());
			}
		});
		//KOLOR CZCIONKI
		toolbar_fontcolor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color selectedColor = null;
				// open color dialog and select Color
				selectedColor = JColorChooser.showDialog(Client_GUI.this, "Select color", Client_GUI.this.edytor.getForeground());
				StyleConstants.setForeground(current_style, selectedColor);
			}
		});
		//ROZMIAR
		box_size.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Select size of text
				String getSize = box_size.getSelectedItem().toString();
				StyleConstants.setFontSize(current_style, Integer.parseInt(getSize));
			}
		});
		
		this.addWindowListener( new WindowAdapter(){
			
			//@Override
			public void windowClosing(WindowEvent e){
				
					save();
					the_end();
					System.exit(0);
			}
		});
		
		try {
			this.client = new Client();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
		
		
		
	
	
	public class Client {
		
		Socket Client_socket;
		String Client_id;
		Thread_queue_send sender;
		Thread_queue_receive receiver;
		
		
		Client() throws UnknownHostException, IOException{
			this.Client_socket = new Socket("localhost", 4445);
			this.sender = new Thread_queue_send();
			this.receiver = new Thread_queue_receive();
			
			GeetID dialog = new GeetID();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);

			
		}
		private class Thread_queue_send implements Runnable{
			
			Thread thread;
			ObjectOutputStream os;

			Thread_queue_send(){
				this.thread = new Thread(this);
				this.thread.start();	
			}
			@Override
			public void run() {
				try {
					os = new ObjectOutputStream(Client.this.Client_socket.getOutputStream());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				while(!this.thread.isInterrupted())
			    {
					try {
						message msg = Client_GUI.this.queue_main.take();
						
						os.writeObject(msg);
						if(msg.id == "END"){
							
							try {
								Client.this.Client_socket.close();
								Client.this.receiver.thread.interrupt();
								Client.this.sender.thread.interrupt();
							} catch (IOException e1) {
								
							}
						}							
					} catch (IOException | InterruptedException e) {
						e.printStackTrace();
					}
			    }
			}
		}
		private class Thread_queue_receive implements Runnable{
			
			Thread thread;
			ObjectInputStream is;

			Thread_queue_receive(){
				this.thread = new Thread(this);
				this.thread.start();	
			}
			@Override
			public void run() {
				try {
					is = new ObjectInputStream(Client.this.Client_socket.getInputStream());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				while(!this.thread.isInterrupted())
			    {
					
						message msg;
						try {
							msg = (message) is.readObject();
							
							if((msg.direction.equals("SERVER"))&&(msg.id.equals("INFO"))){
								String text = Client_GUI.this.text_SERVER.getText();
								Client_GUI.this.text_SERVER.setText(text + (String)(msg.uknown_content) +"\n");
								Client_GUI.this.text_SERVER.setCaretPosition(Client_GUI.this.text_SERVER.getDocument().getLength());
								//Client.this.queue_main.put(msg);
							}else if((msg.direction.equals("SERVER"))&&(msg.id.equals("LIST"))){
								
								 String[] arr = (String[])msg.uknown_content;
								 //System.out.println(arr[0]);
								 Client_GUI.this.comboBox_users.setModel(new DefaultComboBoxModel<String>(arr));								
								
							}else if((msg.direction.equals("SERVER"))&&(msg.id.equals("EMPTYLIST"))){
								//System.out.println("testtttttt");
								 Client_GUI.this.comboBox_users.setModel(new DefaultComboBoxModel<String>(new String[]{"-empty-"}));								
								
							/*}else if(msg.direction.equals("CLIENT")&&(msg.id.equals("CONNECTED"))){
								//TO ZOSTA£O WPISANE W COMOBOX USERÓW I PRZY ODBIORZE WIADOMOŒÆI JEŒLI USER MA ROZMOWE Z INNYM NIZ WE WIADOMOSCI
								btnWylij.setEnabled(true);
								btnRozlacz.setEnabled(true);
								if(Client_GUI.this.Chosen_id!=null){
									save();
								}
								String text = Client_GUI.this.text_SERVER.getText();
								String nick = (String)(msg.uknown_content);
								Client_GUI.this.text_SERVER.setText(text + "Server po³¹czy³ Ciê z " + nick +"\n");
								Client_GUI.this.text_SERVER.setCaretPosition(Client_GUI.this.text_SERVER.getDocument().getLength());
								Client_GUI.this.chat_info.setText(nick);
								Client_GUI.this.Connected_with = nick;
								Client_GUI.this.conversation_window.setText("");
								File file = new File("DATA/"+Client_GUI.this.Client_id+"/conversations/"+Client_GUI.this.Connected_with+".rtf");
						        if (file.exists()){
						        	
						        	 RTFEditorKit rtf = new RTFEditorKit();
									try {
										// load file into jTextPane
										FileInputStream fi = new FileInputStream("DATA/"+Client_GUI.this.Client_id+"/conversations/"+Client_GUI.this.Connected_with+".rtf");
										rtf.read(fi, Client_GUI.this.conversation_window.getDocument(), 0);
										System.out.println("");
										fi.close();
									} catch (Exception e) {
										System.out.println("err:" + e.toString());
									}
						       }
						        message msg1 = new message("SERVER","LIST");
								try {
									Client_GUI.this.queue_main.put(msg1);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}       
						       
								*/
							}else if(msg.direction.equals("CLIENT")&&(msg.id.equals("INFO"))){
								
								String text = Client_GUI.this.text_SERVER.getText();
								Client_GUI.this.text_SERVER.setText(text + (String)(msg.uknown_content) +"\n");
								Client_GUI.this.text_SERVER.setCaretPosition(Client_GUI.this.text_SERVER.getDocument().getLength());
								
							}else if(msg.direction.equals("CLIENT")&&(msg.id.equals("DISCONNECTED"))){
								
								save();
								btnWylij.setEnabled(false);
								btnRozlacz.setEnabled(true);
								chat_info.setText("Brak");	
								
								//Client_GUI.this.conversation_window.setText("");
								
							}else if(msg.direction.equals("CLIENT")&&(msg.id.equals("MESSAGE"))){
								
								
								if(!msg.from.equals(Client_GUI.this.Connected_with)){
									message msg2 = new message("SERVER","CHOSEN",msg.from);
									try {
										Client_GUI.this.queue_main.put(msg2);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									
									btnWylij.setEnabled(true);
									btnRozlacz.setEnabled(true);
									if(Client_GUI.this.Chosen_id!=null){
										save();
									}
									String text = Client_GUI.this.text_SERVER.getText();
									String nick = msg.from;
									Client_GUI.this.text_SERVER.setText(text + "Server po³¹czy³ Ciê z " + nick +"\n");
									Client_GUI.this.text_SERVER.setCaretPosition(Client_GUI.this.text_SERVER.getDocument().getLength());
									Client_GUI.this.chat_info.setText(nick);
									Client_GUI.this.Connected_with = nick;
									Client_GUI.this.conversation_window.setText("");
									File file = new File("DATA/"+Client_GUI.this.Client_id+"/conversations/"+Client_GUI.this.Connected_with+".rtf");
							        if (file.exists()){
							        	
							        	 RTFEditorKit rtf = new RTFEditorKit();
										try {
											// load file into jTextPane
											FileInputStream fi = new FileInputStream("DATA/"+Client_GUI.this.Client_id+"/conversations/"+Client_GUI.this.Connected_with+".rtf");
											rtf.read(fi, Client_GUI.this.conversation_window.getDocument(), 0);
											System.out.println("");
											fi.close();
										} catch (Exception e) {
											System.out.println("err:" + e.toString());
										}
							       }
							        message msg1 = new message("SERVER","LIST");
									try {
										Client_GUI.this.queue_main.put(msg1);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									String text1 = Client_GUI.this.text_SERVER.getText();
									Client_GUI.this.text_SERVER.setText(text1 + "Server-<Masz Wiadomoœæ!> " + nick+"\n");
									
									
									Client_GUI.this.text_SERVER.setCaretPosition(Client_GUI.this.text_SERVER.getDocument().getLength());
									JTextPane tmp = (JTextPane) msg.uknown_content;
									StyledDocument doc = (StyledDocument) tmp.getDocument();
									
									RTFEditorKit kit = new RTFEditorKit();
									RTFEditorKit rtf = new RTFEditorKit();
									BufferedOutputStream out;
									try {
										out = new BufferedOutputStream(new FileOutputStream("DATA/"+Client_GUI.this.Client_id+"/received.rtf"));
										kit.write(out, doc, doc.getStartPosition().getOffset(), doc.getLength());
										out.flush();
										out.close();
										FileInputStream ms = new FileInputStream("DATA/"+Client_GUI.this.Client_id+"/received.rtf");
										rtf.read(ms, Client_GUI.this.conversation_window.getDocument(), Client_GUI.this.conversation_window.getStyledDocument().getLength());
										
									} catch (BadLocationException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}else{
									
									String text = Client_GUI.this.text_SERVER.getText();
									Client_GUI.this.text_SERVER.setText(text + "Server-<Masz Wiadomoœæ!>"  + Client_GUI.this.Connected_with +"\n");
									Client_GUI.this.text_SERVER.setCaretPosition(Client_GUI.this.text_SERVER.getDocument().getLength());
									JTextPane tmp = (JTextPane) msg.uknown_content;
									StyledDocument doc = (StyledDocument) tmp.getDocument();
									
									RTFEditorKit kit = new RTFEditorKit();
									RTFEditorKit rtf = new RTFEditorKit();
									BufferedOutputStream out;
									try {
										out = new BufferedOutputStream(new FileOutputStream("DATA/"+Client_GUI.this.Client_id+"/received.rtf"));
										kit.write(out, doc, doc.getStartPosition().getOffset(), doc.getLength());
										out.flush();
										out.close();
										FileInputStream ms = new FileInputStream("DATA/"+Client_GUI.this.Client_id+"/received.rtf");
										rtf.read(ms, Client_GUI.this.conversation_window.getDocument(), Client_GUI.this.conversation_window.getStyledDocument().getLength());
										Client_GUI.this.conversation_window.setCaretPosition(Client_GUI.this.conversation_window.getDocument().getLength());
										
									} catch (BadLocationException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
						        
								
								
								
							}
						} catch (ClassNotFoundException | IOException e) {
							// TODO Auto-generated catch block
							//e.printStackTrace();
						}
						
			    }
				
			}
		}
		private class GeetID extends JDialog {

			private final JPanel contentPanel = new JPanel();
			private JTextField user_id;
			/**
			 * Create the dialog.
			 */
			public GeetID() {
				super(Client_GUI.this, "Nawi¹zywaneie Po³¹czenia z Serverem!", true);
				setBounds(100, 100, 450, 300);
				getContentPane().setLayout(new BorderLayout());
				contentPanel.setLayout(new FlowLayout());
				contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
				getContentPane().add(contentPanel, BorderLayout.CENTER);
				{
					JLabel lblnazwa = new JLabel("Podaj nazw\u0119 u\u017Cytkownika:");
					contentPanel.add(lblnazwa);
				}
				{
					user_id = new JTextField();
					contentPanel.add(user_id);
					user_id.setColumns(10);
					
				}
				{
					JPanel buttonPane = new JPanel();
					buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
					getContentPane().add(buttonPane, BorderLayout.SOUTH);
					{
						JButton okButton = new JButton("OK");
						okButton.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
								
								Client_GUI.this.Client_id =	user_id.getText();
								Client_GUI.this.label_client_id.setText(user_id.getText());
								Client_GUI.this.setTitle("Komunikator - " + Client_GUI.this.Client_id);
								setVisible(false);
								message msg = new message("SERVER","NAME",Client_GUI.this.Client_id );
								try {
									Client_GUI.this.queue_main.put(msg);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								File file = new File("DATA/"+Client_GUI.this.Client_id);
						        if (!file.exists()){
						        	 file.mkdir();
						        }
						        File file2 = new File("DATA/"+Client_GUI.this.Client_id+"/conversations");
						        if (!file2.exists()){
						        	 file2.mkdir();
						        }
						        
						        
							}
						});
						okButton.setActionCommand("OK");
						buttonPane.add(okButton);
						getRootPane().setDefaultButton(okButton);
						
					}
					{
						JButton cancelButton = new JButton("Cancel");
						cancelButton.setActionCommand("Cancel");
						buttonPane.add(cancelButton);
					}
				}
			}

		}
		
	}
	private void prepareFonts(JComboBox<String> box_font){
		//£adowanie Czcionek
				GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
				// get all font name&amp;amp;amp;amp;amp;amp;amp;nbsp;
				String[] fontNames = gEnv.getAvailableFontFamilyNames();
				// load to combobox
				ComboBoxModel<String> model = new DefaultComboBoxModel<String>(fontNames);
				box_font.setModel(model);
		}
	private void initStyle(){
		current_style = new SimpleAttributeSet();
		StyleConstants.setFontFamily(current_style, "Academy Engraved LET");
		StyleConstants.setFontSize(current_style, new Integer(17));		
	}
	private void the_end(){
		
		message msg = new message("SERVER","END");
		try {
			Client_GUI.this.queue_main.put(msg);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
			
	}
	private void save(){
		
		StyledDocument doc = (StyledDocument) Client_GUI.this.conversation_window.getDocument();
		// convert to richtext format
		RTFEditorKit kit = new RTFEditorKit();
		BufferedOutputStream out;
		try {
			out = new BufferedOutputStream(new FileOutputStream("DATA/"+Client_GUI.this.Client_id+"/conversations/"+Client_GUI.this.Connected_with+".rtf"));
			// save content to file
			kit.write(out, doc, doc.getStartPosition().getOffset(), doc.getLength());
			out.flush();
			out.close();
		} catch (Exception e1) {
			System.out.println("Err:" + e1.toString());
		}
	}
	private void append_to_conversation(){
		

        
           
		DateFormat dateFormat = new SimpleDateFormat(" <-yyyy/MM/dd HH:mm:ss->");
		Date date = new Date();
		
		SimpleAttributeSet tmp = new SimpleAttributeSet();;
		StyleConstants.setItalic(tmp, true);
		StyleConstants.setUnderline(tmp, true);
		StyleConstants.setFontSize(tmp, 12);
		StyleConstants.setFontFamily(tmp, "Calibri");
		JTextPane info = new JTextPane();
		JTextPane message = new JTextPane();
		try {
			info.getDocument().insertString(0, "Wiadomoœæ nadchodz¹ca od " + Client_GUI.this.Client_id + dateFormat.format(date)+ "\n", tmp);
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
		// get meta of text
		StyledDocument doc = (StyledDocument) Client_GUI.this.edytor.getDocument();
		StyledDocument info_doc = (StyledDocument) info.getDocument();
		StyledDocument message_doc = (StyledDocument) message.getDocument();
		// convert to richtext format
		RTFEditorKit kit = new RTFEditorKit();
		RTFEditorKit rtf = new RTFEditorKit();
		BufferedOutputStream out;
		BufferedOutputStream info_out;
		BufferedOutputStream message_out;
		try {
			out = new BufferedOutputStream(new FileOutputStream("DATA/"+Client_GUI.this.Client_id+"/temp.rtf"));
			info_out = new BufferedOutputStream(new FileOutputStream("DATA/"+Client_GUI.this.Client_id+"/info.rtf"));
			message_out = new BufferedOutputStream(new FileOutputStream("DATA/"+Client_GUI.this.Client_id+"/message.rtf"));
			// save content to file
			kit.write(out, doc, doc.getStartPosition().getOffset(), doc.getLength());
			out.flush();
			out.close();
			kit.write(info_out, info_doc, info_doc.getStartPosition().getOffset(), info_doc.getLength());
			info_out.flush();
			info_out.close();
			
			// load file into jTextPane
			FileInputStream info_fi = new FileInputStream("DATA/"+Client_GUI.this.Client_id+"/info.rtf");
			rtf.read(info_fi, message.getDocument(), message.getStyledDocument().getLength());
			info_fi.close();
			FileInputStream fi = new FileInputStream("DATA/"+Client_GUI.this.Client_id+"/temp.rtf");
			rtf.read(fi, message.getDocument(), message.getStyledDocument().getLength());
			fi.close();	
			
			kit.write(message_out, message_doc, message_doc.getStartPosition().getOffset(), message_doc.getLength());
			message_out.flush();
			message_out.close();
			
			FileInputStream ms = new FileInputStream("DATA/"+Client_GUI.this.Client_id+"/message.rtf");
			rtf.read(ms, Client_GUI.this.conversation_window.getDocument(), Client_GUI.this.conversation_window.getStyledDocument().getLength());
			Client_GUI.this.conversation_window.setCaretPosition(Client_GUI.this.conversation_window.getDocument().getLength());
			
			
			message msg = new message("CLIENT","MESSAGE",message,Client_GUI.this.Client_id);
			//System.out.println(Client_GUI.this.Chosen_id);
			try {
				Client_GUI.this.queue_main.put(msg);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			ms.close();	
			
			
			
		} catch (Exception e) {
			System.out.println("Err:" + e.toString());
		}
	}
}
