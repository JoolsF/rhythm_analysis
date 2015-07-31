package rhythm.analysis.view;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.gicentre.utils.multisketch.EmbeddedSketch;
import org.gicentre.utils.multisketch.PopupWindow;

import processing.core.PFont;

public class Text_viewer  extends EmbeddedSketch{

	private static final long serialVersionUID = 1L;
	private final int screenWidth = 500;
	private final int screenHeight = 500;
	private final int screenBorder = 50;
	
	private PFont f;
	private int fontSize = 15;
	private int lineHeight = fontSize;
	private int characterSpacing = 15;
	Random rand = new Random();
	
	private Map<Integer, Integer> colourMap = new HashMap<Integer, Integer>(); // Maps characters to colours.  
	private Arc_viewer arcViewerParent = null;
	 
	public Text_viewer(Arc_viewer arcViewerParent){
		this.arcViewerParent = arcViewerParent;	
	}
	
	
	public void setup() {
		size(screenWidth, screenHeight);	
		f = createFont("Georgia",fontSize,true);
	    textFont(f);
	    fill(0); //set colour black
	    colorMode(HSB);	
	    setColourMap();
	}
	
	public void draw(){
		background(128);
		smooth();
	
		int currentChar = 20;
		int currentline = lineHeight;
		char[] charArray = arcViewerParent.getStringAsArray();
		int from = arcViewerParent.getSlider1();
		int to = arcViewerParent.getSlider2();
		
		for(int i = from; i < to ;i ++){	
			if(colourMap.get(i) != null){
				pushStyle();
				fill(colourMap.get(i),255,255);
				text(charArray[i], currentChar, currentline);
				popStyle();
			} else {
				text(charArray[i], currentChar, currentline);	
			}
			
			currentChar += characterSpacing;
			if((i != 0) && (i % 30 == 0)) {
				currentline += lineHeight;
				currentChar = 20;
			}
		}
		
	}
	
	
	/**
	 * Takes a 2d array representing pairs of matching words
	 * Each element contains an array of length 3 with {a,b,l} integers
	 * a is position of character a
	 * b is position of character b
	 * l is length of words
	 * 
	 */
	private void setColourMap(){ //int[][] pairs arg
		int colour = getRandomNumber(255);
		
		//TEST DATA
		colourMap.put(3, colour);
		colourMap.put(4, colour);
		colourMap.put(5, colour);
		
		colourMap.put(8, colour);
		colourMap.put(9, colour);
		colourMap.put(10, colour);
		
		int colour2 = getRandomNumber(255);
		colourMap.put(20, colour2);
		colourMap.put(21, colour2);
		colourMap.put(22, colour2);
		colourMap.put(23, colour2);
		
		colourMap.put(34, colour2);
		colourMap.put(35, colour2);
		colourMap.put(36, colour2);
		colourMap.put(37, colour2);
		//TEST DATA END
		
	}

	
	private int getRandomNumber(int upperLimit){
		return rand.nextInt(upperLimit) + 1;
	}
	
}
