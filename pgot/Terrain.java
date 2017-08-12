/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pgot;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;

/**
 *
 * @author Сергей
 */
public class Terrain {
    //Terrain Variables--------------------------------------------------------
   

    private final int WATER = 0x0000ff;
    private static final int ROCK = 0xaabbcc;

        float waterheight = (float) 0.5;
	float beachthreshold = (float) 0.02;
	float mountainthreshold = (float) 0.7;
        
	static float[][] terrain = null;
	private int wsizex = 700;
	private int wsizey = 700;
        private int run = 300;
	//static float[] terrainthresholds = {0.8f, 0.02f, 1.0f}; //Water level, beach range, mountain start range
	private static long seed;
        
        
      
         public   void generate(long seed) {
           this.seed = seed;
          generate();
       }
        
        public void generate(){
		 
		int seed = randInt(0, 40000);
	
	    SimplexNoise simplexNoise=new SimplexNoise(getRun(),0.5,1);
	  //  SimplexNoise simplexNoise2=new SimplexNoise(10,0.5,seed+1);

	    double xStart=0;
	    double XEnd=1000;
	    double yStart=0;
	    double yEnd=1000;

	    int xResolution=wsizex;
	    int yResolution=wsizey;

	    double[][] result=new double[xResolution][yResolution];
	    double[][] result2=new double[xResolution][yResolution];
	    

	    for(int i=0;i<xResolution;i++){
	        for(int j=0;j<yResolution;j++){
	            int x=(int)(xStart+i*((XEnd-xStart)/xResolution));
	            int y=(int)(yStart+j*((yEnd-yStart)/yResolution));
	            result[i][j]=0.5*(1+simplexNoise.getNoise(x,y)); 
                 }
	    }	
            
	    for(int i=0;i<xResolution;i++){
	        for(int j=0;j<yResolution;j++){
	            int x=(int)(xStart+i*((XEnd-xStart)/xResolution));
	            int y=(int)(yStart+j*((yEnd-yStart)/yResolution));
                    result2[i][j]=0.5*(1+simplexNoise.getNoise(x,y));
	        }
	    }		
	 
		float[][] result_fl = new float[result.length][result[0].length];
		double res2mult = 0.3;
		for(int i=0; i<result.length; i++){
			for(int j=0; j<result[0].length; j++){
				result_fl[i][j] = (float)(result[i][j]+res2mult*result2[i][j]);
				
                        }
		}
		
		terrain = result_fl;
                System.out.print("Карта пересчитана \n"+ getRun());
        }
        
         public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
            g2d.setBackground(Color.WHITE);

  
            for(int y = 0; y< terrain.length; y++){
	            for (int x = 0; x < terrain.length; x++) {
	            	Color c = null;
                          if(terrain[x][y] <= waterheight){
	          	        c = new Color(WATER);
	                  }
	            	else if(terrain[x][y] > waterheight && terrain[x][y] < waterheight+beachthreshold){
	            		c = new Color(ROCK);
	            	}
	            	else if(terrain[x][y] > waterheight+beachthreshold && terrain[x][y] < mountainthreshold){
	            		c = new Color((int)(219/((terrain[x][y]+0.1)*1.5)), (int)(185/((terrain[x][y]+0.1)*1.5)), (int)(155/((terrain[x][y]+0.1)*1.5)));
	            	}
	            	else{
	            		c = new Color((int)(219/((terrain[x][y]+0.1)*1.8)), (int)(185/((terrain[x][y]+0.1)*1.8)), (int)(155/((terrain[x][y]+0.1)*1.8)));
	            	}
                
	            	g.setColor(c);
	                g2d.drawLine(x,y,x,y);
	            	
            }
        }
        
    
        }
	
        public  int randInt(int min, int max) {
	    Random rand = new Random();
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    return randomNum;
	}

    /**
     * @return the run
     */
    public int getRun() {
        return run;
    }

    /**
     * @param run the run to set
     */
    public void setRun(int run) {
        this.run = run;
        generate();
    }
    
}
