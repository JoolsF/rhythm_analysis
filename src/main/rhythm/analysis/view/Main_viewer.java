package rhythm.analysis.view;

/*
Referenced
http://www.gicentre.net/utils/multiwindow/
http://www.sojamo.de/libraries/controlP5/
http://cs.smith.edu/dftwiki/index.php/Tutorial:_A_Model-View-Controller_in_Processing
http://forum.processing.org/two/discussion/1368/controlp5-buttons-controls-trigger-automatically-on-sketch-start
*/

//TO DO look into performance.  I.e when new string analysed other screen should close and their objects be destroyed
//TO DO look into slider graphics versus output when string length > 50.  Scale and ouput not matching
import processing.core.*;

import org.gicentre.utils.multisketch.*;

import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.Textarea;
import controlP5.Textfield;
import rhythm.analysis.control.Rhythm_controller;

public class Main_viewer extends PApplet implements Observer{
	private static final long serialVersionUID = 1L;
	
	private ControlP5 cp5;
	private Textarea myTextarea;
	
	private Rhythm_controller controller;
	
	private PopupWindow arcViewWindow;
	private Arc_viewer arcView;
	
	private Textfield textfield;
	private Textfield textfield2;
	
	public boolean pulsesSet;
	public boolean initialInputSet;
		
	public Main_viewer(){
		//MVC system started here
		this.controller = new Rhythm_controller();
		this.controller.attach(this);
		
		//Start second windows
		this.arcView = new Arc_viewer(controller);
		this.controller.attach(arcView);
		this.arcViewWindow = new PopupWindow(this, arcView);

		this.pulsesSet = false;
		this.initialInputSet = false;
	}	
	
	/**
	 * setup() called immediately after constructor
	 */
	public void setup() {  
		size(300,650);	  
		
		//Fontsetup
		PFont font = createFont("arial",20);
		textFont(font);
		cp5 = new ControlP5(this);
		
		//Input textfield 1
		textfield = cp5.addTextfield("input")
					   .setPosition(40,50)
		 	           .setSize(200,40)
		 	           .setFont(font)
		 	           .setFocus(true)
		 	           .setColor(color(255,0,0));
		textfield.setAutoClear(false);
		
		arcViewWindow.setVisible(true);
		
		//Button
		cp5.addButton("clear_data")
		.setBroadcast(false)	
		.setValue(100)
		.setPosition(40,125)
		.setSize(200,19)
		.setBroadcast(true);
			
		// Slider
		cp5.addSlider("arcMax")
		.setBroadcast(false)
		.setPosition(40,225)
		.setSize(200,20)
		.setRange(1,10)
		.setNumberOfTickMarks(10)
		.setValue(1)
		.setBroadcast(true);
		
		cp5.addSlider("pulses")
		.setBroadcast(false)
		.setPosition(40,275)
		.setSize(200,20)
		.setRange(3,32)
		.setNumberOfTickMarks(10)
		.setValue(8)
		.setBroadcast(true);
		
		
		//Text area
		myTextarea = cp5.addTextarea("txt")
	    .setPosition(40,325)
	    .setSize(225,250)
	    .setFont(createFont("arial",12))
	    .setLineHeight(14)
	    .setColor(color(128))
	    .setColorBackground(color(255,100))
	    .setColorForeground(color(255,100));
		myTextarea.setText("Enter number of pulses to start (between 2 and 32");
	}
		

	public void draw() {
		background(0);
		fill(255);
	}
	
	//ControlP5 code starts here
	
	/**
	 * clear()
	 * @see processing.core.PApplet#clear()
	 */
	public void clear() {
	  cp5.get(Textfield.class,"textValue").clear();
	}
		
	/**
	 * 
	 * @param theEvent
	 */
	public void controlEvent(ControlEvent theEvent) {
		if(theEvent.isAssignableFrom(Textfield.class)) {
//			println("controlEvent: accessing a string from controller '"
//					+theEvent.getName()+"': "
//					+theEvent.getStringValue()
//					);
			}
		}

	/**
	 * Automatically receives results from controller input
	 * @param theText
	 */
	public void input(String theText) {
		
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
	/**
	 * 
	 * @param theText
	 * @return
	 */
	private boolean inputValid(String theText){	
		if(! this.initialInputSet){
				if(theText.length() > 2){
				this.initialInputSet = true;
				return true;
			} else {
				this.myTextarea.setText("Error, initial input must greater than 2 characters");
				textfield.clear();
				return false;
			}
		}
		return true;	
	}
	
	public boolean pulsesSetCheck(String theText){
		if(! pulsesSet){
			if(isInteger(theText)){
				Integer x = Integer.valueOf(theText);
				if(x >= 2 && x <= 32){
					this.controller.setNumPulses(x);
					pulsesSet = true;
					textfield.clear();
					myTextarea.setText("Pulses set to " + x + ". Program initialised");
					return true;
				} else {
					myTextarea.setText("Error.  Number out of range. Please enter number of pulses to start (between 2 and 32");
					textfield.clear();
					return false;
					
				}
			} else {
				myTextarea.setText("Error.  Input not integer value. Please enter number of pulses to start (between 2 and 32");
				textfield.clear();
				return false;
			}
			
		}	
		return false;
	}
	/**
	 * Ref http://stackoverflow.com/questions/237159/whats-the-best-way-to-check-to-see-if-a-string-represents-an-integer-in-java
	 * @param str
	 * @return
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
	
	//BUTTONS
	public void clear_data(){
//TO DO needs debugging - ensure model reset correctly
//		this.myTextarea.setText("Data cleared");
//		this.controller.resetModel();
	}
	
	
		
	public void arcMax(int arcMinimum) {
			this.controller.setArcMin(arcMinimum);
			arcView.redraw();
	}
	
	//Slider inactive currently as causing ArrayIndexOutOfBoundsException when moved rapidly
	//Need to improve synchronisation with model.
	public void pulses(int numPulses) {
		//this.controller.setNumPulses(numPulses);
	}
	
	
	
	@Override
	public void update() {
		this.redraw();
		
	}
	
}
	
