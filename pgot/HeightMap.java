package pgot;

import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import static pgot.PirateIsland.runFeatureGenerator;


public class HeightMap{
	
	float[][] map;
	float elmax;
	float waterheight = (float) 0.5;
	float beachthreshold = (float) 0.02;
	float mountainthreshold = (float) 0.7;
        //цвета поверхностей

    public static final int WATER = 0x0000ff;
    public static final int ROCK = 0xaabbcc;
    public static final int GRASS = 0x00ff00;
    public static final int SOIL = 0xffff00;
    public static final int TREE = 0x007700;
 
	int[] startloc;
	int[] treasureloc;
	int[] treeloc;
	ArrayList<int[]> path;
 	
	public HeightMap(float[][] world, float e, float wt, float bt, float mt, int[] start, int[] treasure, int[] tree, ArrayList<int[]> p){
		startloc = start;
		treasureloc = treasure;
		waterheight = wt;
		treeloc = tree;
		beachthreshold = bt;
		mountainthreshold = mt;
		map = world;
		elmax = e;
	        path = p;
               // System.out.println(""+p);
		JFrame map = new JFrame("Карта");
		map.setBounds(0,0, world.length, world[0].length);
		map.setLayout(new BorderLayout());
                   map.add(new MainPanel());
        map.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        map.setLocationRelativeTo(null);
        map.setVisible(true);
       
	}
        public class MainPanel extends JPanel {

    private final TestPane paintPanel;
    private final JSlider smoothSlider;
    
    public MainPanel() {
        setLayout(new BorderLayout());
        
        paintPanel = new TestPane();
        add(paintPanel, BorderLayout.CENTER);
        
        final JPanel configPanel = new JPanel();
        configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.LINE_AXIS));
        configPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

        configPanel.add(new JLabel("Шаг сетки"));
        
        smoothSlider = new JSlider();
        smoothSlider.setMinimum(100);
        smoothSlider.setMaximum(1000);
        smoothSlider.setValue(PirateIsland.run);
        smoothSlider.addChangeListener(smoothListener);
        configPanel.add(smoothSlider);
        
        final JButton generateButton = new JButton("Генерация");
        generateButton.addActionListener(generateListener);
        configPanel.add(generateButton);
        
        add(configPanel, BorderLayout.PAGE_START);
    }
    
    private final ChangeListener smoothListener = new ChangeListener() {

        @Override
        public void stateChanged(ChangeEvent e) {
         PirateIsland.run=(smoothSlider.getValue());
        }
    };
    
    private final ActionListener generateListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
             paintPanel.generateNew();
               smoothSlider.setValue( PirateIsland.run);
        }
    };
}
	
	public class TestPane extends JPanel {

        public TestPane() {
        }

        @Override
		public void paint(Graphics g) {
            super.paint(g);

            Graphics2D g2d = (Graphics2D)g;
            g2d.setBackground(Color.WHITE);

           // System.out.println("Drawing Heightmap!");
            for(int y = 0; y< map.length; y++){
	            for (int x = 0; x < map.length; x++) {
	           
	            	Color c = null;
                          if(map[x][y] <= waterheight){
	            		int col = Utility.randInt(100, 125);
	            		c = new Color(WATER);
	            		
	      
	            	}
	            	else if(map[x][y] > waterheight && map[x][y] < waterheight+beachthreshold){
	            		c = new Color(ROCK);
	            	}
	            	else if(map[x][y] > waterheight+beachthreshold && map[x][y] < mountainthreshold){
	            		c = new Color((int)(219/((map[x][y]+0.1)*1.5)), (int)(185/((map[x][y]+0.1)*1.5)), (int)(155/((map[x][y]+0.1)*1.5)));
	            	}
	            	else{
	            		
	            		c = new Color((int)(219/((map[x][y]+0.1)*1.8)), (int)(185/((map[x][y]+0.1)*1.8)), (int)(155/((map[x][y]+0.1)*1.8)));
	            	}
                
	            	g.setColor(c);
	                g2d.drawLine(x,y,x,y);
	            	
            }
      
        }
           
    }
	
	public  boolean containsNode(ArrayList<int[]> list, int x, int y){
		for(int i=0; i<list.size(); i++){
			if(list.get(i)[0] == x && list.get(i)[1] == y)
				return true;
		}
		return false;
	}

        private void generateNew() {
             PirateIsland.runFeatureGenerator();
          repaint();
         //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

       
        }
}


