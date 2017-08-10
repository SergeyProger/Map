package pgot;

import java.util.ArrayList;

//-------------------------------------------------
//Daily Programmer x-post /r/ProduceduralGeneration
//           Monthly #1 - December 2015
//                 /u/Epthelyn
//-------------------------------------------------


public class PirateIsland {
	
	//Terrain Variables--------------------------------------------------------
	static float[][] terrain = null;
	static int wsizex = 500;
	static int wsizey = 500;
        static int run = 1000;
	static float[] terrainthresholds = {0.8f, 0.02f, 1.0f}; //Water level, beach range, mountain start range
	//Terrain notes:
	//Terrain range ~0.15-~0.85
	//If WL+BR = MSR, then desert
	//WL higher than 0.65 gives small islands
	//WL lower than ~0.2 gives no oceans
	//MSR higher than ~0.85 gives no mountains
	//--------------------------------------------------------------------------
	
	//Feature variables---------------------------------------------------------
	static int[] start; //"Landing" location
	static int[] treasure; //Treasure location
	static int[] tree;
	

	public static void main(String[] args){
            generate();
        }
        public static void generate(){
		int seed = intSeedFromText("Hello World");
		seed = Utility.randInt(0, 40000);
		System.out.println("Seed: " + seed);
	    SimplexNoise simplexNoise=new SimplexNoise(run,0.5,seed);
	    SimplexNoise simplexNoise2=new SimplexNoise(10,0.5,seed+1);

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
		
		float maxel = 0;
		float minel = 1;
		
		float[][] result_fl = new float[result.length][result[0].length];
		double res2mult = 0.3;
		for(int i=0; i<result.length; i++){
			for(int j=0; j<result[0].length; j++){
				result_fl[i][j] = (float)(result[i][j]+res2mult*result2[i][j]);
				if(result_fl[i][j] > maxel)
					maxel = (float) (result_fl[i][j]+res2mult*result2[i][j]);
				if(result_fl[i][j] < minel)
					minel = (float) (result_fl[i][j]+res2mult*result2[i][j]);
			}
		}
		System.out.println("Max Elevation: " + maxel);
		System.out.println("Min Elevation: " + minel);
		
		terrain = result_fl;
		runFeatureGenerator();
		
		System.out.println("Start: " + Utility.toCoords(start));
		System.out.println("Treasure: " + Utility.toCoords(treasure));
		
		//Calculate a path from start to treasure using the "interesting features" on the map
		//If no path exists between two points, the feature is on another island, in which case
		//path to the nearest shore (North/East/South/West) from each end point and
		//then link the path ends together via water only
		
		ArrayList<ArrayList<int[]>> paths = new ArrayList<ArrayList<int[]>>();
		ArrayList<int[]> path1 = findPath(start, tree, false);
		
		ArrayList<int[]> path2 = findPath(tree, treasure, false);
		ArrayList<int[]> pathc = new ArrayList<int[]>();
		pathc.addAll(path1);
		pathc.addAll(path2);
		
		HeightMap hm = new HeightMap(result_fl, maxel, terrainthresholds[0], terrainthresholds[1],
                        terrainthresholds[2], start, treasure, tree, pathc);
	}
	
	public static boolean inTRange(int[] p, float min, float max){
		if(terrain[p[0]][p[1]] > min && terrain[p[0]][p[1]] < max){
			return true;
		}
		return false;
	}
	
	public static void runFeatureGenerator(){
		//Place the starting location on a random beach (i.e. water level + (0~beachthreshold))
		boolean startplaced = false;
		while(!startplaced){
			int placex = Utility.randInt(0, wsizex-1);
			int placey = Utility.randInt(0, wsizey-1);
			if(terrain[placex][placey] > terrainthresholds[0] && terrain[placex][placey] < (terrainthresholds[0] + terrainthresholds[1])){
				startplaced = true;
				start = new int[] {placex, placey};
			}
		}
		
		//Place a tree somewhere on not-mountains and not-beach
		boolean treeplaced = false;
		while(!treeplaced){
			int placex = Utility.randInt(0, wsizex-1);
			int placey = Utility.randInt(0, wsizey-1);
			if(terrain[placex][placey] > terrainthresholds[0]+terrainthresholds[1] && terrain[placex][placey] < terrainthresholds[2]){
				treeplaced = true;
				tree = new int[] {placex, placey};
			}
		}
		
		
		//Place the treasure somewhere NOT on a beach (i.e. terrain > (waterlevel + beachthreshold)
		boolean treasureplaced = false;
		while(!treasureplaced){
			int placex = Utility.randInt(0, wsizex-1);
			int placey = Utility.randInt(0, wsizey-1);
			if(terrain[placex][placey] > terrainthresholds[0]+terrainthresholds[1]){
				treasureplaced = true;
				treasure = new int[] {placex, placey};
			}
		}
		
	}
	
	public static int intSeedFromText(String text){
		String stringseed = "";
		for(int i=0; i<text.length(); i++){
			try{
				Integer.parseInt(""+text.charAt(i));
				stringseed+=text.charAt(i);
			}
			catch(NumberFormatException nfe){
				stringseed+=""+((int)text.charAt(i));
			}

		}
		
		return Integer.parseInt(stringseed.substring(0, 8));
	}
	
	public static ArrayList<int[]> findPath(int[] a, int[] b, boolean crosswater){
		//Lee Algorithm for path a -> b
		ArrayList<int[]> path = new ArrayList<int[]>();
		
		ArrayList<int[]> wave = new ArrayList<int[]>();
		int[][] pathgrid = new int[wsizex][wsizey];
		for(int i=0; i<pathgrid.length; i++){
			for(int j=0; j<pathgrid[0].length; j++){
				pathgrid[i][j] = -1;
			}
		}
		pathgrid[a[0]][a[1]] = 0;
		int[] startnode = new int[] {a[0], a[1], 0};
		wave.add(startnode);
		
		boolean greached = false;
		boolean noplacement = false;
		boolean searching = true;
		
		while(!greached && searching){
			if(noplacement){
				searching = false;
				System.out.println("NO PATH FOUND");
				break;
			}
			noplacement = true;
			//Check all 4 horizontal/vertical positions and if valid, add to path
			for(int i=0; i<wave.size(); i++){
				int[] n = wave.get(i);
				double mmh = 0.01; //Maximum movement height between terrain blocks
				if(onGrid(n[0]-1, n[1]) && /*terrain[n[0]-1][n[1]] > terrainthresholds[0] &&*/ terrain[n[0]-1][n[1]] < terrainthresholds[2] && pathgrid[n[0]-1][n[1]] == -1 && (Math.abs(terrain[n[0]-1][n[1]]-terrain[n[0]][n[1]]) <= mmh | terrain[n[0]-1][n[1]] < terrainthresholds[0])){
					pathgrid[n[0]-1][n[1]] = n[2]+1;
					wave.add(new int[] {n[0]-1, n[1], n[2]+1});
					noplacement = false;
				}
				if(onGrid(n[0]+1, n[1]) && /*terrain[n[0]+1][n[1]] > terrainthresholds[0] &&*/ terrain[n[0]+1][n[1]] < terrainthresholds[2] && pathgrid[n[0]+1][n[1]] == -1 && (Math.abs(terrain[n[0]+1][n[1]]-terrain[n[0]][n[1]]) <= mmh) | terrain[n[0]+1][n[1]] < terrainthresholds[0]){
					pathgrid[n[0]+1][n[1]] = n[2]+1;
					wave.add(new int[] {n[0]+1, n[1], n[2]+1});
					noplacement = false;
				}
				if(onGrid(n[0], n[1]-1) && /*terrain[n[0]][n[1]-1] > terrainthresholds[0] &&*/ terrain[n[0]][n[1]-1] < terrainthresholds[2] && pathgrid[n[0]][n[1]-1] == -1 && (Math.abs(terrain[n[0]][n[1]-1]-terrain[n[0]][n[1]]) <= mmh) | terrain[n[0]][n[1]-1] < terrainthresholds[0]){
					pathgrid[n[0]][n[1]-1] = n[2]+1;
					wave.add(new int[] {n[0], n[1]-1, n[2]+1});
					noplacement = false;
				}
				if(onGrid(n[0], n[1]+1) && /*terrain[n[0]][n[1]+1] > terrainthresholds[0] &&*/ terrain[n[0]][n[1]+1] < terrainthresholds[2] && pathgrid[n[0]][n[1]+1] == -1 && (Math.abs(terrain[n[0]][n[1]+1]-terrain[n[0]][n[1]]) <= mmh) | terrain[n[0]][n[1]+1] < terrainthresholds[0]){
					pathgrid[n[0]][n[1]+1] = n[2]+1;
					wave.add(new int[] {n[0], n[1]+1, n[2]+1});
					noplacement = false;
				}
			}
			
			if(containsNode(wave, b[0], b[1])){
				greached = true;
				searching = false;
				System.out.println("PATH FOUND");
			}
	
		}
		
		if(greached){
			System.out.println("Starting backtrack procedure");
			path.add(b);
			
			int cwavei = nodeIndex(wave, b[0], b[1]);
			
			//while(wave.get(cwavei)[0] != a[0] && wave.get(cwavei)[1] != a[1]){
			while(!atPos(wave.get(cwavei)[0], wave.get(cwavei)[1], a)){
				cwavei = findPrevious(wave, cwavei, true);
				path.add(new int[] {wave.get(cwavei)[0], wave.get(cwavei)[1]});
			}
		}
		
		System.out.println("Returning path of length " + path.size());
		return path;
	}
	
	public static boolean onGrid(int x, int y){
		if(x>=0 && y>=0 && x<wsizex && y<wsizex)
			return true;
		return false;
	}
	
	public static boolean atPos(int x, int y, int[] g){
		if(g[0] == x && g[1] == y)
			return true;
		return false;
	}
	
	public static boolean containsNode(ArrayList<int[]> list, int x, int y){
		for(int i=0; i<list.size(); i++){
			if(list.get(i)[0] == x && list.get(i)[1] == y)
				return true;
		}
		return false;
	}
	
	public static int nodeIndex(ArrayList<int[]> list, int x, int y){
		for(int i=0; i<list.size(); i++){
			if(list.get(i)[0] == x && list.get(i)[1] == y)
				return i;
		}
		return -1;
	}
	
	public static int findPrevious(ArrayList<int[]> wave, int index, boolean contour){
		for(int i=0; i<wave.size(); i++){
			if(adjacent(new int[] {wave.get(i)[0], wave.get(i)[1]}, new int[] {wave.get(index)[0], wave.get(index)[1]}) && wave.get(i)[2] == (wave.get(index)[2]-1) && terrain[wave.get(i)[0]][wave.get(i)[1]] == terrain[wave.get(index)[0]][wave.get(index)[1]]){
				return i;
			}
			else if(adjacent(new int[] {wave.get(i)[0], wave.get(i)[1]}, new int[] {wave.get(index)[0], wave.get(index)[1]}) && wave.get(i)[2] == (wave.get(index)[2]) && terrain[wave.get(i)[0]][wave.get(i)[1]] == terrain[wave.get(index)[0]][wave.get(index)[1]]){
				return i;
			}
			else if(adjacent(new int[] {wave.get(i)[0], wave.get(i)[1]}, new int[] {wave.get(index)[0], wave.get(index)[1]}) && wave.get(i)[2] == (wave.get(index)[2]-1)){
				return i;
			}
		}
		return -1;
		
	}
	
	public static boolean adjacent(int[] a, int[] b){
		if(Math.abs(a[0]-b[0]) <= 1 && Math.abs(a[1]-b[1]) <= 1 && (a[0] == b[0]) | (a[1] == b[1])){
			return true;
		}
		return false;
	}
	
	
}
