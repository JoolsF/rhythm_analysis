package rhythm.analysis.view;

/*
Referenced
http://www.gicentre.net/utils/multiwindow/
http://www.sojamo.de/libraries/controlP5/
http://cs.smith.edu/dftwiki/index.php/Tutorial:_A_Model-View-Controller_in_Processing
http://forum.processing.org/two/discussion/1368/controlp5-buttons-controls-trigger-automatically-on-sketch-start
*/

//TO DO look into performance.  I.e when new string analysed other screen should close and their objects be destroyed
//TO DO look into slider graphics versus output when string length > 50.  Scale and output not matching
import java.io.File;

import processing.core.*;

import org.gicentre.utils.multisketch.*;
import controlP5.ControlP5;
import controlP5.Textarea;
import controlP5.Textfield;
import rhythm.analysis.control.RhythmController;

public class MainViewer extends PApplet implements Observer{
	private static final long serialVersionUID = 1L;
	
	private ControlP5 cp5;
	private Textarea myTextarea;
	
	private RhythmController controller;
	
	private PopupWindow arcViewWindow;
	private ArcViewer arcView;
	
	private Textfield textfield;
		
	private boolean pulsesSet;
	private boolean initialInputSet;
	private boolean toggleSimilarity;
	private boolean toggleArcFilter;
	
	/*-----------------------------------------------------------------------------------------
	 * Constructor
	 *----------------------------------------------------------------------------------------*/		
	public MainViewer(){
		this.pulsesSet = false;
		this.initialInputSet = false;
		this.toggleSimilarity = false; /* Must be set to false by default */
		this.toggleArcFilter = true;
		
		/*MVC structure started here */
		this.controller = new RhythmController(this);
		this.controller.attach(this);
		
		/*Start second windows*/
		this.arcView = new ArcViewer(controller);
		this.controller.attach(arcView);
		this.arcViewWindow = new PopupWindow(this, arcView);

	}	
	
	/*-----------------------------------------------------------------------------------------
	 * Processing setup and draw methods
	 *----------------------------------------------------------------------------------------*/	
	 /**
	  * Processing setup method run immediately after constructor.
	  * Initialises key screen elements such as size and background.
	  * Run once
	  */
	public void setup() {  
		size(280,610);	  
		frame.setTitle("Rhythmic Data Analyser");
		PFont font = createFont("arial",20);
		textFont(font);
		cp5 = new ControlP5(this);
		
		/*Input textfield 1 */
		textfield = cp5.addTextfield("input")
					   .setPosition(40,70)
		 	           .setSize(200,40)
		 	           .setFont(font)
		 	           .setFocus(true)
		 	           .setColor(color(255,0,0));
		textfield.setAutoClear(false);
		
		arcViewWindow.setVisible(true);
				
		/* Arc min slider */
		cp5.addSlider("arcMin")
			.setBroadcast(false)
			.setPosition(40,135)
			.setSize(200,20)
			.setRange(1,20)
			.setNumberOfTickMarks(20)
			.setValue(1)
			.setBroadcast(true);
		
		/* Similarity toggle */ 
		cp5.addToggle("similarity")
		 	.setPosition(40,185)
		 	.setSize(50,20)
		 	.setValue(false) /* set to false be default*/
		 	.setMode(ControlP5.SWITCH)
		     ;
		/* Arc filter toggle */ 
		cp5.addToggle("arcFilter")
	 	.setPosition(115,185)
	 	.setSize(50,20)
	 	.setValue(true) 
	 	.setMode(ControlP5.SWITCH)
	     ;
		
		/* Arc colour toggle */ 
		cp5.addToggle("arcColour")
	 	.setPosition(185,185)
	 	.setSize(50,20)
	 	.setValue(true)
	 	.setMode(ControlP5.SWITCH)
	     ;

		/*Load file button*/
		cp5.addButton("loadFile")
		.setBroadcast(false)	
		.setValue(100)
		.setPosition(40,235)
		.setSize(50,20)
		.setBroadcast(true);	
		  
		  
		/*Text area */
		myTextarea = cp5.addTextarea("txt")
	    .setPosition(40,285)
	    .setSize(200,290)
	    .setFont(createFont("arial",12))
	    .setLineHeight(14)
	    .setColor(color(128))
	    .setColorBackground(color(255,100))
	    .setColorForeground(color(255,100));
		myTextarea.setText("Enter number of pulses to start (between 2 and 32)");
	}
		
	 /**
	   * Processing draw method runs in a loop immediately after setup()
	   */
	public void draw() {
		background(0);
		textSize(17);
		text("Rhythmic Data Analyser",45,40);	
		fill(255);	 
	}
	
	/*-----------------------------------------------------------------------------------------
	 * Getters and setters
	 *----------------------------------------------------------------------------------------*/
	public boolean getArcFilterToggleValue(){
		return this.toggleArcFilter;
	}
	
	public boolean getSimilarityToggleValue(){
		return this.toggleSimilarity;
	}
	
	
	/*-----------------------------------------------------------------------------------------
	 * Data input and validation
	 *----------------------------------------------------------------------------------------*/
	
	
	/*
	 * Automatically receives results from controller input
	 */
	private void input(String theText) {
		if(! pulsesSet) {
			pulsesSetCheck(theText);
			return;
		}		
		if(inputValid(theText) && pulsesSet){
			this.controller.updateTree(theText);
			this.myTextarea.setText(controller.getTreeAsList().toString());
			textfield.clear();
			arcView.redraw();
		} 
	}
	
	/*
	 * Validates string input
	 */
	private boolean inputValid(String theText){	
		if(! this.initialInputSet){
				if(theText.length() > 2){
				this.initialInputSet = true;
				return true;
			} else {
				this.myTextarea.setText("Error. Initial input must be greater than 2 characters");
				textfield.clear();
				return false;
			}
		}
		return true;	
	}
	
	/*
	 * Checks if number of pulses set
	 */
	private boolean pulsesSetCheck(String theText){
		if(! pulsesSet){
			if(isInteger(theText)){
				Integer x = Integer.valueOf(theText);
				if(x >= 2 && x <= 32){
					this.controller.setNumPulses(x);
					pulsesSet = true;
					textfield.clear();
					myTextarea.setText("Pulses set to " + x + ". Program initialised.");
					return true;
				} else {
					myTextarea.setText("Error.  Number out of range. Please enter number of pulses to start (between 2 and 32)");
					textfield.clear();
					return false;
					
				}
			} else {
				myTextarea.setText("Error.  Input not integer value. Please enter number of pulses to start (between 2 and 32)");
				textfield.clear();
				return false;
			}
			
		}	
		return false;
	}
	
	/*
	 * Ref http://stackoverflow.com/questions/237159/whats-the-best-way-to-check-to-see-if-a-string-represents-an-integer-in-java
	 */
	private static boolean isInteger(String str) {
		if (str == null) {
			return false;
		}
		int length = str.length();
		if (length == 0) {
			return false;
		}
		int i = 0;
		if (str.charAt(0) == '-') {
			if (length == 1) {
				return false;
			}
			i = 1;
		}
		for (; i < length; i++) {
			char c = str.charAt(i);
			if (c <= '/' || c >= ':') {
				return false;
			}
		}
		return true;
	}
	

	/*-----------------------------------------------------------------------------------------
	 * Buttons
	 *----------------------------------------------------------------------------------------*/
	public void loadFile() {
		if(pulsesSet){
			selectInput("Select a file to process:", "fileSelected");
		}	else {
			myTextarea.setText("Error.  You must first enter number of pulses to start (between 2 and 32)");
		}
	}
	
	public void fileSelected(File selection) {
	  if (selection == null) {
	    println("Window was closed or the user hit cancel.");
	  } else {
		  this.initialInputSet = true;
		  String lines[] = loadStrings(selection.getAbsolutePath());
		  StringBuffer sb = new StringBuffer();
		  for (int i = 0 ; i < lines.length; i++) {
		    sb.append(lines[i]);
		  }
		  String s = sb.toString().replaceAll("\\s+",""); /*Removes all whitespace from input*/

		  //TO DO - Remove this limit once program memory footprint and speed has been improved
		  if(s.length() <= 4000){
			  input(s);  
		  } else {
			  myTextarea.setText("Error. File is " + s.length() + "characters long. 4000 character limit currently (excluding whitespace)");
		  }

		  
	  }
	}
		
	public void arcMin(int arcMinimum) {
			this.controller.setArcMin(arcMinimum);
			arcView.redraw();
			arcView.updateTextViewer();
	}
	
	public void similarity(boolean theFlag) {
		this.toggleSimilarity = theFlag;
		
	}
	
	public void arcFilter(boolean theFlag) {
		this.toggleArcFilter = theFlag;
		
	}
	
	public void arcColour(boolean theFlag) {
		this.arcView.setArcColour(theFlag);
	}
		
	/**
	 * Implementation of Observer interface's update methods
	 */
	@Override
	public void update() {
		this.redraw();
		
	}
	
}
	
