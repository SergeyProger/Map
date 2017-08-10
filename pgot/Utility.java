package pgot;


import java.util.Random;

public class Utility {
	public static String toCoords(int x, int y){
		return "(" + x + ", " + y + ")";
	}
	
	public static String toCoords(int[] c){
		return "(" + c[0] + ", " + c[1] + ")";
	}
	
	public static int randInt(int min, int max) {
	    Random rand = new Random();
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    return randomNum;
	}
	
	public static void lines(int l){
		for(int i=0; i<l; i++)
			System.out.println();
	}
	
	public static String LineofLength(int length, char c){
		String line = "";
		for(int i=0; i<length; i++){
			line+=c;
		}
		return line;
	}
	
	public static String inBrackets(String s){
		return ("(" + s + ")");
	}
	
	
	public static String centreText(String s, int length, char c){
		String centred = "";
		int lengthtoadd = (int)((length-s.length())/2);
		
		centred+=Utility.LineofLength(lengthtoadd, c);
		centred+=s;
		centred+=Utility.LineofLength(lengthtoadd, c);
		
		return centred;
	}
	
	public static int maxOf(int a, int b){
		if(a>b)
			return a;
		return b;
	}
	
	public static boolean beginsWith(String text, String begin){
		for(int i=0; i<begin.length(); i++){
			if(text.charAt(i) != begin.charAt(i)){
				return false;
			}
		}
		return true;
	}
	
}
