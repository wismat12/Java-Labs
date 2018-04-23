package myTextEditor.views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.awt.event.InputEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.SoftBevelBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.text.Utilities;
import javax.swing.text.rtf.RTFEditorKit;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.JToggleButton;
import javax.swing.JColorChooser;
import java.awt.event.KeyAdapter;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;


public class GUIeditor extends JFrame {

	private JTextPane workspace;
	private boolean ifsaved = true;
	private SimpleAttributeSet current_style;
	private boolean style_change = false;
	private JLabel label_bool_modyfikacjastylu;
	private int workspace_width;
	private int frame_width;
	private int frame_height;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUIeditor frame = new GUIeditor();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	private void prepareFrame(){
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		this.setTitle("Edytor");
		
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		
		frame_height = screenSize.height - 180;
		frame_width =screenSize.width - screenSize.width/6;
		setSize(frame_width, frame_height);
		workspace_width = 600;
		//this.setLocationByPlatform(true);
		this.setLocation(screenSize.width/2 - frame_width/2, 100);
		setMinimumSize(new Dimension(1000, 400));
		
	}
	public GUIeditor() {
		prepareFrame();
		//MENU
		
		JMenuBar menu = new JMenuBar();
		setJMenuBar(menu);
		
		JMenu menu_plik = new JMenu("Plik");
		menu.add(menu_plik);
		
		JMenuItem mntmNowy = new JMenuItem("Nowy");
		
		mntmNowy.setIcon(new ImageIcon(GUIeditor.class.getResource("/myTextEditor/resources/menu_new.png")));
		mntmNowy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		menu_plik.add(mntmNowy);
		menu_plik.addSeparator();
		
		JMenuItem mntmOtwrz = new JMenuItem("Otw\u00F3rz");
		mntmOtwrz.setIcon(new ImageIcon(GUIeditor.class.getResource("/myTextEditor/resources/menu_open.png")));
		mntmOtwrz.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		menu_plik.add(mntmOtwrz);
		
		JMenuItem mntmZapisz = new JMenuItem("Zapisz");
		mntmZapisz.setIcon(new ImageIcon(GUIeditor.class.getResource("/myTextEditor/resources/menu_save.png")));
		mntmZapisz.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		menu_plik.add(mntmZapisz);
		
		JMenuItem mntmZapiszJako = new JMenuItem("Zapisz jako");
		mntmZapiszJako.setIcon(new ImageIcon(GUIeditor.class.getResource("/myTextEditor/resources/menu_saveas.png")));
		mntmZapiszJako.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		menu_plik.add(mntmZapiszJako);
		menu_plik.addSeparator();
		
		JMenuItem mntmDrukuj = new JMenuItem("Drukuj");
		mntmDrukuj.setEnabled(false);
		mntmDrukuj.setIcon(new ImageIcon(GUIeditor.class.getResource("/myTextEditor/resources/menu_print.png")));
		mntmDrukuj.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
		menu_plik.add(mntmDrukuj);
		menu_plik.addSeparator();
		
		JMenuItem mntmZakocz = new JMenuItem("Zako\u0144cz");
		mntmZakocz.setIcon(new ImageIcon(GUIeditor.class.getResource("/myTextEditor/resources/menu_turnoff.png")));
		
		mntmZakocz.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
		menu_plik.add(mntmZakocz);
		
		JMenu menu_opcje = new JMenu("Opcje");
		menu.add(menu_opcje);
		
		JMenu menu_pomoc = new JMenu("Pomoc");
		menu.add(menu_pomoc);
		
		JLabel menu_remembertoSave = new JLabel("");
		menu_remembertoSave.setFont(new Font("Tahoma", Font.PLAIN, 11));
		menu_remembertoSave.setText("<-Nowy Dokument->");
		menu.add(menu_remembertoSave);
		
		
		getContentPane().setLayout(new BorderLayout(0, 0));
		this.workspace = new JTextPane();
		//this.workspace.setEditable(false);
		//Inicjalizacja stylu
		initStyle();
		//Dodanie workspace
		JScrollPane scroll = new JScrollPane(workspace);
		//Panel na workspace
		JPanel p = new JPanel();
		scroll.setBounds( (this.getWidth()/2) - (this.workspace_width/2), 0, this.workspace_width, frame_height -170 );
	    p.setLayout( null );
	    p.add( scroll );
	    p.setPreferredSize( new Dimension( this.workspace_width, frame_height - 170 ) );
	    p.setBackground(Color.gray);
	    getContentPane().add( p, BorderLayout.CENTER );
	    
	    //TOOLBARY
		//Tworzenie panelu na dwa toolbary
		JPanel toolbar_panel = new JPanel();
		toolbar_panel.setBorder(new CompoundBorder());
		toolbar_panel.setLayout(new BoxLayout(toolbar_panel, BoxLayout.Y_AXIS));
		
		JToolBar toolBar1 = new JToolBar();
		JToolBar toolBar2 = new JToolBar();
		toolBar2.setAlignmentX(0);
		
		JButton toolbar_new = new JButton();
		toolbar_new.setFocusable(false);
		toolbar_new.setIcon(new ImageIcon(GUIeditor.class.getResource("/myTextEditor/resources/toolbar_new.png")));
		toolBar1.add(toolbar_new);
		
		JButton toolbar_open = new JButton();
		
		toolbar_open.setFocusable(false);
		toolbar_open.setIcon(new ImageIcon(GUIeditor.class.getResource("/myTextEditor/resources/toolbar_open.png")));
		toolBar1.add(toolbar_open);
		
		JButton toolbar_save = new JButton();
		
		toolbar_save.setIcon(new ImageIcon(GUIeditor.class.getResource("/myTextEditor/resources/toolbar_save.png")));
		toolbar_save.setToolTipText("Zapisz do pliku");
		toolBar1.add(toolbar_save);
		
		JButton toolbar_print = new JButton();
		toolbar_print.setEnabled(false);
		toolbar_print.setIcon(new ImageIcon(GUIeditor.class.getResource("/myTextEditor/resources/toolbar_print.png")));
		toolbar_print.setToolTipText("Kolor Czcionki");
		toolBar1.add(toolbar_print);
		toolBar1.addSeparator();
		
		JLabel lblCzcionka = new JLabel();
		lblCzcionka.setText("Czcionka:");
		lblCzcionka.setToolTipText("Wybierz czcionk\u0119");
		lblCzcionka.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblCzcionka.setIcon(new ImageIcon(GUIeditor.class.getResource("/myTextEditor/resources/toolbar_font.png")));
		toolBar2.add(lblCzcionka);
		toolBar2.addSeparator();
		
		JComboBox<String> box_font = new JComboBox<String>();
		box_font.setMaximumSize(new Dimension(150,25));
		prepareFonts(box_font);
		
		toolBar2.add(box_font);
		toolBar2.addSeparator();

		JLabel lblNewLabel = new JLabel("Rozmiar: ");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 11));
		toolBar2.add(lblNewLabel);
		toolBar2.addSeparator();
		
		
		JComboBox<String> box_size = new JComboBox<String>();
		box_size.setMaximumSize(new Dimension(70,25));
		box_size.setModel(new DefaultComboBoxModel<String>(new String[] {"9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "32", "34", "36", "38", "40"}));
		box_size.setSelectedIndex(8);
		toolBar2.add(box_size);
		
		toolBar1.setAlignmentX(0);
		toolBar1.setPreferredSize(new Dimension(this.getWidth(),50));
		toolBar1.setMaximumSize(new Dimension(this.getWidth(),50));
		toolBar2.setPreferredSize(new Dimension(this.getWidth(),40));
		toolBar2.setMaximumSize(new Dimension(this.getWidth(),40));
		toolbar_panel.add(toolBar1);
		
		JButton toolbar_cut = new JButton("");
		toolbar_cut.setIcon(new ImageIcon(GUIeditor.class.getResource("/myTextEditor/resources/toolbar_cut.png")));
		toolBar1.add(toolbar_cut);
		
		JButton toolbar_copy = new JButton("");
		toolbar_copy.setIcon(new ImageIcon(GUIeditor.class.getResource("/myTextEditor/resources/toolbar_paste.png")));
		toolBar1.add(toolbar_copy);
		
		JButton toolbar_paste = new JButton("");
		toolbar_paste.setIcon(new ImageIcon(GUIeditor.class.getResource("/myTextEditor/resources/toolbar_glue.jpg")));
		toolBar1.add(toolbar_paste);
		toolBar1.addSeparator();
		
		JLabel lblAkapityLewy = new JLabel("Akapity:");
		toolBar1.add(lblAkapityLewy);
		toolBar1.addSeparator();
		
		JLabel ltoolbar_leftindent = new JLabel("");
		ltoolbar_leftindent.setIcon(new ImageIcon(GUIeditor.class.getResource("/myTextEditor/resources/toolbar_rightindent.png")));
		toolBar1.add(ltoolbar_leftindent);
		
		toolBar1.addSeparator();
		JComboBox<String> toolbar_box_leftindent = new JComboBox<String>();
		toolbar_box_leftindent.setModel(new DefaultComboBoxModel<String>(new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25"}));
		toolbar_box_leftindent.setMaximumSize(new Dimension(40,35));
		toolBar1.add(toolbar_box_leftindent);
		toolBar1.addSeparator();
		
		JLabel toolbar_rightindent = new JLabel("");
		toolbar_rightindent.setIcon(new ImageIcon(GUIeditor.class.getResource("/myTextEditor/resources/toolbar_leftindent.png")));
		toolBar1.add(toolbar_rightindent);
		toolBar1.addSeparator();
		
		JComboBox<String> toolbar_box_rightindent = new JComboBox<String>();
		toolbar_box_rightindent.setModel(new DefaultComboBoxModel<String> (new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25"}));
		toolbar_box_rightindent.setMaximumSize(new Dimension(40,35));
		toolBar1.add(toolbar_box_rightindent);
		toolBar1.addSeparator();
		
		JLabel lblWyrwnanie = new JLabel("Wyr\u00F3wnanie: ");
		toolBar1.add(lblWyrwnanie);
		
		JToggleButton toolbar_leftalg = new JToggleButton("");
	
		toolbar_leftalg.setIcon(new ImageIcon(GUIeditor.class.getResource("/myTextEditor/resources/toolbar_alignleft.png")));
		toolBar1.add(toolbar_leftalg);
		
		JToggleButton toolbar_centeralg = new JToggleButton();
	
		toolbar_centeralg.setIcon(new ImageIcon(GUIeditor.class.getResource("/myTextEditor/resources/toolbar_aligncenter.png")));
		toolBar1.add(toolbar_centeralg);
		
		JToggleButton toolbar_rightalg = new JToggleButton("");
	
		toolbar_rightalg.setIcon(new ImageIcon(GUIeditor.class.getResource("/myTextEditor/resources/toolbar_alignright.png")));
		toolBar1.add(toolbar_rightalg);
		
		JToggleButton toolbar_justify = new JToggleButton("");
		toolbar_justify.setIcon(new ImageIcon(GUIeditor.class.getResource("/myTextEditor/resources/toolbar_justify.png")));
		toolBar1.add(toolbar_justify);
		
		JLabel lblOdstp = new JLabel(" Odst\u0119p: ");
		toolBar1.add(lblOdstp);
		
		JLabel toolbar_spacing = new JLabel("");
		toolbar_spacing.setIcon(new ImageIcon(GUIeditor.class.getResource("/myTextEditor/resources/toolbar_spacing.png")));
		toolBar1.add(toolbar_spacing);
		toolBar1.addSeparator();
		
		JComboBox<String> toolbar_box_spacing = new JComboBox<String>();
	
		toolbar_box_spacing.setModel(new DefaultComboBoxModel<String> (new String[] {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25"}));
		toolbar_box_spacing.setMaximumSize(new Dimension(40,35));
		toolBar1.add(toolbar_box_spacing);
		
		JLabel lblNastpiaModyfikacjaStylu = new JLabel("   Nast\u0105pi\u0142a Modyfikacja Stylu - ");
		toolBar1.add(lblNastpiaModyfikacjaStylu);
		
		label_bool_modyfikacjastylu = new JLabel("");
		label_bool_modyfikacjastylu.setFont(new Font("Tahoma", Font.BOLD, 11));
		label_bool_modyfikacjastylu.setText(" NIE");
		toolBar1.add(label_bool_modyfikacjastylu);
		
		toolBar1.addSeparator();
		
		JLabel lblNewLabel_2 = new JLabel("  Rozmiar Okna:  ");
		toolBar1.add(lblNewLabel_2);
		
		JLabel window_width = new JLabel("");
		toolBar1.add(window_width);
		
		JLabel lblNewLabel_3 = new JLabel("x");
		toolBar1.add(lblNewLabel_3);
		
		JLabel window_height = new JLabel("");
		toolBar1.add(window_height);
		
		JLabel lblAktywnyKlawisz = new JLabel("  Ostatni Znak:");
		toolBar1.add(lblAktywnyKlawisz);
		JLabel lblNewLabel_1 = new JLabel();
		lblNewLabel_1.setFont(new Font("Calibri", Font.PLAIN, 20));
		toolBar1.add(lblNewLabel_1);
		toolBar1.addSeparator();
		toolbar_panel.add(new JSeparator());
		toolbar_panel.add(toolBar2);
		toolBar2.addSeparator();
		
		JLabel lblKolor = new JLabel("Kolory:");
		toolBar2.add(lblKolor);
		toolBar2.addSeparator();
		JButton toolbar_fontcolor = new JButton("");
		
		toolbar_fontcolor.setIcon(new ImageIcon(GUIeditor.class.getResource("/myTextEditor/resources/toolbar_colortext.png")));
		toolbar_fontcolor.setToolTipText("Kolor");
		toolBar2.add(toolbar_fontcolor);
		
		JButton toolbar_bgcolor = new JButton("");
	
		toolbar_bgcolor.setIcon(new ImageIcon(GUIeditor.class.getResource("/myTextEditor/resources/toolbar_fill.png")));
		toolBar2.add(toolbar_bgcolor);
		toolBar2.addSeparator();
		
		JLabel lblStyl = new JLabel("Styl: ");
		toolBar2.add(lblStyl);
		
		JToggleButton toolbar_bold = new JToggleButton();
		
		toolbar_bold.setIcon(new ImageIcon(GUIeditor.class.getResource("/myTextEditor/resources/toolbar_bold.png")));
		toolBar2.add(toolbar_bold);
		
		JToggleButton toolbar_italic = new JToggleButton();
	
		toolbar_italic.setIcon(new ImageIcon(GUIeditor.class.getResource("/myTextEditor/resources/toolbar_italic.png")));
		toolBar2.add(toolbar_italic);
		
		JToggleButton toolbar_underline = new JToggleButton("");
	
		toolbar_underline.setIcon(new ImageIcon(GUIeditor.class.getResource("/myTextEditor/resources/toolbar_underline.png")));
		toolBar2.add(toolbar_underline);
		
		JToggleButton toolbar_strike = new JToggleButton("");
	
		toolbar_strike.setIcon(new ImageIcon(GUIeditor.class.getResource("/myTextEditor/resources/toolbar_strikethrough.png")));
		toolBar2.add(toolbar_strike);
		
		JToggleButton toolbar_subscript = new JToggleButton("");
	
		toolbar_subscript.setIcon(new ImageIcon(GUIeditor.class.getResource("/myTextEditor/resources/toolbar_subscript.png")));
		toolBar2.add(toolbar_subscript);
		
		JToggleButton toolbar_superscript = new JToggleButton("");
		
		toolbar_superscript.setIcon(new ImageIcon(GUIeditor.class.getResource("/myTextEditor/resources/toolbar_superscript.png")));
		toolBar2.add(toolbar_superscript);
		
		JButton toolbar_toUpper = new JButton("");
	
		toolbar_toUpper.setIcon(new ImageIcon(GUIeditor.class.getResource("/myTextEditor/resources/toolbar_toupper.png")));
		toolBar2.add(toolbar_toUpper);
		
		JButton toolbar_toLower = new JButton("");
	
		toolbar_toLower.setIcon(new ImageIcon(GUIeditor.class.getResource("/myTextEditor/resources/toolbar_tolower.png")));
		toolBar2.add(toolbar_toLower);
		toolBar2.addSeparator();
		
		JLabel toolbar_selectedtext = new JLabel("Zaznaczony Tekst:");
		toolBar2.add(toolbar_selectedtext);
		
		JTextArea toolbar_field_selectedtext = new JTextArea();
		toolbar_field_selectedtext.setForeground(Color.RED);
		toolbar_field_selectedtext.setBackground(Color.LIGHT_GRAY);
		JScrollPane toolbar_field_selectedtext_scrollPane = new JScrollPane(toolbar_field_selectedtext);
		toolBar2.add(toolbar_field_selectedtext_scrollPane);
		
		JPanel panel = new JPanel();
		toolbar_panel.add(panel);
		
		toolbar_panel.add(new JSeparator());
		getContentPane().add(toolbar_panel, BorderLayout.NORTH); //Dodanie toolbarów na szczyt
		
		JFrame frame = this;
		
		/*
		 * OBS£UGA ZDARZEÑ
		 */
		
		//Maksymalizowanie Okna
		this.addWindowStateListener( new WindowStateListener(){

			@Override
			public void windowStateChanged(WindowEvent arg0) {
				
				scroll.setBounds( (GUIeditor.this.getWidth()/2) - (workspace_width/2), 0, workspace_width, GUIeditor.this.getHeight() -170 );
				p.setPreferredSize( new Dimension( workspace_width, frame_height - 170 ) );
				toolBar1.setPreferredSize(new Dimension(GUIeditor.this.getWidth(),50));
				toolBar1.setMaximumSize(new Dimension(GUIeditor.this.getWidth(),50));
				toolBar2.setPreferredSize(new Dimension(GUIeditor.this.getWidth(),40));
				toolBar2.setMaximumSize(new Dimension(GUIeditor.this.getWidth(),40));	
			}
		});
		
		//INFO O KLAWISZU
		workspace.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent vk) {
				
				ifsaved = false;
				menu_remembertoSave.setText("<-Wprowadzono Zmiany! - Pamiêtaj by zapisaæ!->");
				style_change = true;
				lblNewLabel_1.setText(Character.toString(vk.getKeyChar()));
			}
		});
		//Anonimowa klasa wew. monitorujaca zmiany w przestrzeni roboczej
		//bez Invoke by³o Attempt to mutate in notification
		workspace.getDocument().addDocumentListener(new DocumentListener(){
			private void setter() {
                Runnable Change = new Runnable() {
                    @Override
                    public void run() {
                    	
                    	StyledDocument doc = workspace.getStyledDocument();
    					int cursorPosition = workspace.getCaretPosition();
    					doc.setCharacterAttributes(cursorPosition-1, 1,current_style, true);
    					
    					style_change = false;
    					label_bool_modyfikacjastylu.setText(" NIE");
                    }
                };
                EventQueue.invokeLater(Change);
            }
			  public void insertUpdate(DocumentEvent e){
				 if(style_change)
					 setter();
			  }
			  public void removeUpdate(DocumentEvent e){}
			  public void changedUpdate(DocumentEvent e){}
		});
		//ZAPIS DO RTF
		toolbar_save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SaveFile(workspace);
			}
		});
		//ROZMIAR
		box_size.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Select size of text
				String getSize = box_size.getSelectedItem().toString();
				StyleConstants.setFontSize(current_style, Integer.parseInt(getSize));
				style_change = true;
				if(label_bool_modyfikacjastylu!=null)
					label_bool_modyfikacjastylu.setText(" TAK");
			}
		});
		//CZCIONKA
		box_font.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				StyleConstants.setFontFamily(current_style,box_font.getSelectedItem().toString());
				StyleConstants.setFontSize(current_style, Integer.parseInt(box_size.getSelectedItem().toString()));
				style_change = true;
				if(label_bool_modyfikacjastylu!=null)
					label_bool_modyfikacjastylu.setText(" TAK");
			}
		});
		//LEWY MARGINES
		toolbar_box_leftindent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				StyleConstants.setLeftIndent(current_style, Integer.parseInt(toolbar_box_leftindent.getSelectedItem().toString()));
				int cursorPosition = workspace.getCaretPosition();
				workspace.getStyledDocument().setParagraphAttributes(cursorPosition, 1, current_style, true);
				style_change = true;
				if(label_bool_modyfikacjastylu!=null)
					label_bool_modyfikacjastylu.setText(" TAK");
			}
		});	
		//PRAWY MARGINES
		toolbar_box_rightindent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StyleConstants.setRightIndent(current_style, Integer.parseInt(toolbar_box_rightindent.getSelectedItem().toString()));
				int cursorPosition = workspace.getCaretPosition();
				workspace.getStyledDocument().setParagraphAttributes(cursorPosition, 1, current_style, true);
				style_change = true;
				if(label_bool_modyfikacjastylu!=null)
					label_bool_modyfikacjastylu.setText(" TAK");
			}
		});
		//WYROWNANIE DO LEWEJ
		toolbar_leftalg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toolbar_centeralg.setSelected(false);
				toolbar_rightalg.setSelected(false);
				toolbar_justify.setSelected(false);
				StyleConstants.setAlignment(current_style,(int)LEFT_ALIGNMENT);
				int cursorPosition = workspace.getCaretPosition();
				workspace.getStyledDocument().setParagraphAttributes(cursorPosition, 1, current_style, true);
				
				if(label_bool_modyfikacjastylu!=null)
					label_bool_modyfikacjastylu.setText(" TAK");
			}
		});
		//CENTROWANIE
		toolbar_centeralg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toolbar_leftalg.setSelected(false);
				toolbar_rightalg.setSelected(false);
				toolbar_justify.setSelected(false);
				StyleConstants.setAlignment(current_style,StyleConstants.ALIGN_CENTER);
				int cursorPosition = workspace.getCaretPosition();
				workspace.getStyledDocument().setParagraphAttributes(cursorPosition, 1, current_style, true);
				style_change = true;
				if(label_bool_modyfikacjastylu!=null)
					label_bool_modyfikacjastylu.setText(" TAK");
			}
		});
		//DO PRAWEJ
		toolbar_rightalg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toolbar_centeralg.setSelected(false);
				toolbar_leftalg.setSelected(false);
				toolbar_justify.setSelected(false);
				StyleConstants.setAlignment(current_style,StyleConstants.ALIGN_RIGHT);
				int cursorPosition = workspace.getCaretPosition();
				workspace.getStyledDocument().setParagraphAttributes(cursorPosition, 1, current_style, true);
				style_change = true;
				if(label_bool_modyfikacjastylu!=null)
					label_bool_modyfikacjastylu.setText(" TAK");
			}
		});
		//DO LEWEJ I PRAWEJ
		toolbar_justify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toolbar_centeralg.setSelected(false);
				toolbar_rightalg.setSelected(false);
				toolbar_leftalg.setSelected(false);
				StyleConstants.setAlignment(current_style,StyleConstants.ALIGN_JUSTIFIED);
				int cursorPosition = workspace.getCaretPosition();
				workspace.getStyledDocument().setParagraphAttributes(cursorPosition, 1, current_style, true);
				style_change = true;
				if(label_bool_modyfikacjastylu!=null)
					label_bool_modyfikacjastylu.setText(" TAK");
			}
		});
		//ODSTEPY MIEDZY LINIAMI
		toolbar_box_spacing.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StyleConstants.setLineSpacing(current_style, Integer.parseInt(toolbar_box_spacing.getSelectedItem().toString()));
				int cursorPosition = workspace.getCaretPosition();
				workspace.getStyledDocument().setParagraphAttributes(cursorPosition, 1, current_style, true);
				style_change = true;
				if(label_bool_modyfikacjastylu!=null)
					label_bool_modyfikacjastylu.setText(" TAK");
			}
		});
		//KOLOR CZCIONKI
		toolbar_fontcolor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color selectedColor = null;
				// open color dialog and select Color
				selectedColor = JColorChooser.showDialog(GUIeditor.this, "Select color", workspace.getForeground());
				
				if (selectedColor != null){
					StyleConstants.setForeground(current_style, selectedColor);
					style_change = true;
					if(label_bool_modyfikacjastylu!=null)
						label_bool_modyfikacjastylu.setText(" TAK");
				}
			}
		});
		//TLO CZCIONKI
		toolbar_bgcolor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color selectedColor = null;
				selectedColor = JColorChooser.showDialog(GUIeditor.this, "Select color", workspace.getForeground());
				if (selectedColor != null){
					StyleConstants.setBackground(current_style, selectedColor);
					style_change = true;
					if(label_bool_modyfikacjastylu!=null)
						label_bool_modyfikacjastylu.setText(" TAK");
				}
			}
		});
		//BOLD
		toolbar_bold.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StyleConstants.setBold(current_style, toolbar_bold.isSelected());
				style_change = true;
				if(label_bool_modyfikacjastylu!=null)
					label_bool_modyfikacjastylu.setText(" TAK");
			}
		});
		//ITALIC
		toolbar_italic.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StyleConstants.setItalic(current_style, toolbar_italic.isSelected());
				style_change = true;
				if(label_bool_modyfikacjastylu!=null)
					label_bool_modyfikacjastylu.setText(" TAK");
			}
		});
		//UNDERLINE
		toolbar_underline.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StyleConstants.setUnderline(current_style, toolbar_underline.isSelected());
				style_change = true;
				if(label_bool_modyfikacjastylu!=null)
					label_bool_modyfikacjastylu.setText(" TAK");
			}
		});
		//STRIKE
		toolbar_strike.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StyleConstants.setStrikeThrough(current_style, toolbar_strike.isSelected());
				style_change = true;
				if(label_bool_modyfikacjastylu!=null)
					label_bool_modyfikacjastylu.setText(" TAK");
			}
		});
		//SUBSCRIPT
		toolbar_subscript.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toolbar_superscript.setSelected(false);
				StyleConstants.setSubscript(current_style, toolbar_subscript.isSelected());
				StyleConstants.setSuperscript(current_style, toolbar_superscript.isSelected());
				style_change = true;
				if(label_bool_modyfikacjastylu!=null)
					label_bool_modyfikacjastylu.setText(" TAK");
				
			}
		});
		//SUPERSCRIPT
		toolbar_superscript.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toolbar_subscript.setSelected(false);
				StyleConstants.setSubscript(current_style, toolbar_subscript.isSelected());
				StyleConstants.setSuperscript(current_style, toolbar_superscript.isSelected());
				style_change = true;
				if(label_bool_modyfikacjastylu!=null)
					label_bool_modyfikacjastylu.setText(" TAK");
			}
		});
		//UPPER
		toolbar_toUpper.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String text = workspace.getSelectedText();
				
				if(text != null){
					text = text.toUpperCase();

					int start = workspace.getSelectionStart();
					int end = workspace.getSelectionEnd();
					toolbar_field_selectedtext.setText("Pozycja Zaznaczenia(start x end): " + start + "x" + end + "\n" + text);
			
					StringBuilder strBuilder = new StringBuilder(workspace.getText());
					strBuilder.replace(start, end, text);
					workspace.setText(strBuilder.toString());				
				}
			}
		});
		//LOWER
		toolbar_toLower.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String text = workspace.getSelectedText();
	
				if(text != null){
					text = text.toLowerCase();
				
					int start = workspace.getSelectionStart();
					int end = workspace.getSelectionEnd();
					toolbar_field_selectedtext.setText("Pozycja Zaznaczenia(start x end): " + start + "x" + end + "\n" + text);
					StringBuilder strBuilder = new StringBuilder(workspace.getText());
					strBuilder.replace(start, end, text);
					
					workspace.setText(strBuilder.toString());
				}
			}
		});
		//Okno dialogowe Ostrzezenia
		class WarningDialog extends JDialog{
			
			private static final long serialVersionUID = 1L;

			WarningDialog(JFrame owner)
			{
				super(owner, "Uwaga!", true);
				JPanel panel = new JPanel();
				JPanel lab_panel = new JPanel();
				
				JLabel labelka = new JLabel("Czy chcesz zamkn¹æ bez zapisania danych?");
				lab_panel.add(labelka);
				add(lab_panel, BorderLayout.CENTER);
				JButton save_close = new JButton("Zapisz");
				JButton close = new JButton("Zamknij");
				JButton ret = new JButton("Powróæ do edycji");
				save_close.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent arg0) {
						SaveFile(workspace);
						if(!ifsaved)
							setVisible(true);
						else{
							setVisible(false);
							System.exit(0);
						}
					}
				});
				close.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent arg0) {
						setVisible(false);
						System.exit(0);
					}
				});
				ret.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent arg0) {
						setVisible(false);
					}
				});
				panel.add(save_close);
				panel.add(close);
				panel.add(ret);
				add(panel, BorderLayout.SOUTH);
				setSize(500,120);
				
				Toolkit kit = Toolkit.getDefaultToolkit();
				Dimension screenSize = kit.getScreenSize();
				this.setLocation(screenSize.width/2 - 250, screenSize.height/2 - 250);
			}
		}
		//Okno dialogowe Nowego pliku
		class NewDialog extends JDialog{
			
			private static final long serialVersionUID = 1L;

			NewDialog(JFrame owner)
			{
				super(owner, "Uwaga!", true);
				JPanel panel = new JPanel();
				JPanel lab_panel = new JPanel();
				
				JLabel labelka = new JLabel("Czy chcesz rozpocz¹c nowy dokument bez zapisania poprzedniego?");
				lab_panel.add(labelka);
				add(lab_panel, BorderLayout.CENTER);
				JButton yes = new JButton("Zapisz");
				JButton no = new JButton("Otworz bez zapisywania");
		
				yes.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent arg0) {
						SaveFile(workspace);
						if(!ifsaved)
							setVisible(true);
						else
							setVisible(false);
					}
				});
				no.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent arg0) {
						setVisible(false);
						workspace.setText("");
						menu_remembertoSave.setText("<-Nowy Dokument->");
						ifsaved=true;
					}
				});
				panel.add(yes);
				panel.add(no);
			
				add(panel, BorderLayout.SOUTH);
				setSize(500,120);
				
				Toolkit kit = Toolkit.getDefaultToolkit();
				Dimension screenSize = kit.getScreenSize();
				this.setLocation(screenSize.width/2 - 250, screenSize.height/2 - 250);
			}
		}
		//Ostrzezenie przed zamkniêciem!!!
		this.addWindowListener( new WindowAdapter(){
			
			//@Override
			public void windowClosing(WindowEvent e){
				if(!ifsaved){
					JDialog dialog = new WarningDialog(GUIeditor.this);
					dialog.setVisible(true);
				}else
					System.exit(0);
			}
		});
		//Nowy Plik
		toolbar_new.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!ifsaved){
					JDialog dialog = new NewDialog(GUIeditor.this);
					dialog.setVisible(true);
				}
			}
		});
		//Otworz
		toolbar_open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!ifsaved){
					JDialog dialog = new NewDialog(GUIeditor.this);
					dialog.setVisible(true);
				}
				open();
			}
		});
		//MENU::zakoncz
		mntmZakocz.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!ifsaved){
					JDialog dialog = new WarningDialog(GUIeditor.this);
					dialog.setVisible(true);
				}else
					System.exit(0);
			}
		});
		//MENU::otworz
		mntmOtwrz.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!ifsaved){
					JDialog dialog = new NewDialog(GUIeditor.this);
					dialog.setVisible(true);
				}
				open();
			}
		});
		//MENU::zapisz
		mntmZapisz.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SaveFile(workspace);
			}
		});
		//MENU::zapiszjako
		mntmZapiszJako.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SaveFile(workspace);
			}
		});
		//MENU::nowy
		mntmNowy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!ifsaved){
					JDialog dialog = new NewDialog(GUIeditor.this);
					dialog.setVisible(true);
				}
			}
		});
		//SKALOWANIE
		addComponentListener(new ComponentListener() {
			 
			@Override
			public void componentHidden(ComponentEvent arg0) {}

			@Override
			public void componentMoved(ComponentEvent arg0) {}

			@Override
			public void componentResized(ComponentEvent arg0) {
				scroll.setBounds( (frame.getWidth()/2) - (workspace_width/2), 0, workspace_width, frame.getHeight() -170 );
				p.setPreferredSize( new Dimension( workspace_width, frame_height - 170 ) );
				toolBar1.setPreferredSize(new Dimension(frame.getWidth(),50));
				toolBar1.setMaximumSize(new Dimension(frame.getWidth(),50));
				toolBar2.setPreferredSize(new Dimension(frame.getWidth(),40));
				toolBar2.setMaximumSize(new Dimension(frame.getWidth(),40));	
				window_width.setText(Integer.toString(frame.getWidth()));
				window_height.setText(Integer.toString(frame.getHeight()));
				
			}

			@Override
			public void componentShown(ComponentEvent arg0) {}
		});
	}
	
	private void initStyle(){
			current_style = new SimpleAttributeSet();
			StyleConstants.setFontFamily(current_style, "Academy Engraved LET");
			StyleConstants.setFontSize(current_style, new Integer(17));		
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
/*rivate void appendToPane(JTextPane workspace, String msg) {
		
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, workspace.getForeground());

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, workspace.getFont());
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = workspace.getDocument().getLength();
        workspace.setCaretPosition(len);
        workspace.setCharacterAttributes(aset, false);
        workspace.replaceSelection(msg);
        
    }
*/
	private void SaveFile(JTextPane workspace){
		
		JFileChooser file = new JFileChooser();
		TextFilter filter = new TextFilter();
		file.setFileFilter(filter);
		String fileName = "";
		// show save file dialog
		if (file.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			// get full path of selected file
			fileName = file.getSelectedFile().getAbsolutePath();
			// get meta of text
			StyledDocument doc = (StyledDocument) workspace.getDocument();
			// convert to richtext format
			RTFEditorKit kit = new RTFEditorKit();
			BufferedOutputStream out;
			try {
				out = new BufferedOutputStream(new FileOutputStream(fileName));
				// save content to file
				kit.write(out, doc, doc.getStartPosition().getOffset(), doc.getLength());
				out.flush();
				out.close();
			} catch (Exception e) {
				System.out.println("Err:" + e.toString());
		}
		} else {
			return;
		}
		ifsaved = true;
	}
	private void open() {
		JFileChooser file = new JFileChooser();
		TextFilter filter = new TextFilter();
		file.setFileFilter(filter);
		String fileName = "";
		// show open file dialog
		if (file.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			fileName = file.getSelectedFile().getAbsolutePath();
		} else {
			return;
		}
		// using richtext format
		RTFEditorKit rtf = new RTFEditorKit();
		try {
			// load file into jTextPane
			FileInputStream fi = new FileInputStream(fileName);
			rtf.read(fi, workspace.getDocument(), 0);
			fi.close();
		} catch (Exception e) {
			System.out.println("err:" + e.toString());
		}
	}
}