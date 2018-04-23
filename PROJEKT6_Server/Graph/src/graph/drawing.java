package graph;
import xmlhandler.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class drawing extends JComponent{

private static class Line{
    final int x1; 
    final int y1;
    final int x2;
    final int y2;   
    final Color color;

    public Line(int x1, int y1, int x2, int y2, Color color) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;
    }               
}

private final LinkedList<Line> lines = new LinkedList<Line>();

public void addLine(int x1, int x2, int x3, int x4) {
    addLine(x1, x2, x3, x4, Color.black);
}

public void addLine(int x1, int x2, int x3, int x4, Color color) {
    lines.add(new Line(x1,x2,x3,x4, color));        
    repaint();
}

public void clearLines() {
    lines.clear();
    repaint();
}

@Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    for (Line line : lines) {
        g.setColor(line.color);
        g.drawLine(line.x1, line.y1, line.x2, line.y2);
    }
}

public void draw(DOMHandler doc){
    JFrame testFrame = new JFrame();
    testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    final drawing comp = new drawing();
    comp.setPreferredSize(new Dimension(1920, 1080));
    testFrame.getContentPane().add(comp, BorderLayout.CENTER);
    JPanel buttonsPanel = new JPanel();
    JButton newLineButton = new JButton("Draw Graph");
    JButton clearButton = new JButton("Clear");
    buttonsPanel.add(newLineButton);
    buttonsPanel.add(clearButton);
    testFrame.getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
    
    newLineButton.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
        	for(Junction jun: doc.AllJunctions){
        		for(Junction tojun: jun.connections){
        			
        			double x11 =( jun.id.latitude - 50.0)*10000.0;
        			double x12 =( jun.id.latitude - 50.0)*10000.0;
        			double y11 =( jun.id.longitude - 19.9)*10000.0;
        			double y12 =( jun.id.longitude - 19.9)*10000.0;
        			
        			int x1 = (int) 61;
                    int x2 = (int) x12;
                    int y1 = (int) 30;
                    int y2 = (int) y12;
                    System.out.println(x2+"  "+y2);
        			/*int x1 = (int) (Math.random()*320);
                    int x2 = (int) (Math.random()*320);
                    int y1 = (int) (Math.random()*200);
                    int y2 = (int) (Math.random()*200);*/
                    comp.addLine(x1, y1, x2, y2);
        			
        			
        		}
        		
        		
        	}
            
            //Color randomColor = new Color((float)Math.random(), (float)Math.random(), (float)Math.random());
            
        }
    });
    clearButton.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            comp.clearLines();
        }
    });
    testFrame.pack();
    testFrame.setVisible(true);
}

}
