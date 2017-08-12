package pgot;

import java.awt.*;

import javax.swing.JFrame;


public class MapFrame extends JFrame{
	
	public static void main(String[] args) {
        new MapFrame().setVisible(true);
    }
 	
	public MapFrame(){
          super("Карта высот.");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(new MainPanel());
        pack();
	}
  
}


