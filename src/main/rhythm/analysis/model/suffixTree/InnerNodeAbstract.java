package rhythm.analysis.model.suffixTree;

import java.util.List;

public abstract class InnerNodeAbstract implements InnerNode {
	
	/*-----------------------------------------------------------------------------------------
	 * InnerNode check methods
	 *----------------------------------------------------------------------------------------*/
	
	public boolean nodeIsAPrefixOf(String string) {
		if(string.startsWith(this.getString()) && string.length() > this.getString().length()){
			return true;
	    } else {
	    	return false;
	    }
	}
	
	public boolean nodeHasAPrefixOf(String string) {
		if(this.getString().startsWith(string) && this.getString().length() >  string.length()){
			return true;
	    } else {
	    	return false;
	    }
	}
	
	/**
	 * 
	 * @return true if has "$" sibling and total number of siblings are greater than 2
	 */
	public boolean needToSplitNode(){
		//TO DO - Rethink right side of OR below.  This deals with case where node's parent is root Could use instanceOf but needs proper OO solution	
		if ((getLastSiblingValue().equals("$") && this.getSiblings().size() > 2) || 
			  this.getParent() instanceof NodeRoot){			
			return true;
		}else {
			return false;
		}				
	}

	
	/*-----------------------------------------------------------------------------------------
	 * Default - InnerNode creational / modification methods
	 *----------------------------------------------------------------------------------------*/
	
	
	//i.e if getString() returns a and arg is abab the return is bab
	public String removeNodeFromArg(String string){
		return string.substring(getString().length());
	}
	
	
	public String removeArgFromNode(String string){
		return this.getString().substring(string.length());
	}
	
	
	
	public String getCommonPrefix(String string){
		if(this.getString().length() > string.length()){
			return this.getString().substring(0, string.length());
		} else {
			return string.substring(0, this.getString().length());
		}
	}	
	
	public void movePrefixUp(String str){	
		//TO DO - fix this, have to cast.  What about if parent is Root etc
		//class cast exception causing bug here
		InnerNode parent = (InnerNode) this.getParent();
		parent.setString(parent.getString() + this.getCommonPrefix(str));
		this.setSubString(str.length());	
	}
	
	
	
	
	
	/*-----------------------------------------------------------------------------------------
	 * Default - Child methods
	 *----------------------------------------------------------------------------------------*/
	
	public boolean hasChildWithSameFirstLetter(String str){
		char charToCheck[] = str.toCharArray();
		for(InnerNode next: this.getChildren()){
			if(next.getString().toCharArray()[0] == charToCheck[0]
					&& charToCheck[0] != charToCheck[1]
							){
				return true;
			}
		}
		return false;	
	}
	
	public String getChildValues(){
		String childValues = "";
		//guard condition needed for LeafNode
		if(this.getChildren() != null) {
			for(InnerNode next: this.getChildren()){
				childValues += next.getString() + "(" +next.getStringIndex()+")  - ";
				}
		}
		return childValues;
	}
	
	public InnerNode getLastChild(){
		return this.getChildren().get(this.getChildren().size()-1);
	}
	
	public String getLastSiblingValue(){
		return this.getParent().getChildren().get(this.getParent().getChildren().size()-1).getString();
	}
	
	public InnerNode getLastSibling(){
		return this.getParent().getChildren().get(this.getParent().getChildren().size()-1);
	}
	
	
	public List<InnerNode> getSiblings(){
		return this.getParent().getChildren();
	}
	
	

	/**
	 * Removes any % children from parent (if parent not root)
	 */
	public void remove$Children(){
		if(this.getLastSiblingValue().equals("$")){
			this.getParent().removeChild(getLastSibling());
			}
		}
	
	
	
	
	
	/*-----------------------------------------------------------------------------------------
	 * Default - Debug method
	 *----------------------------------------------------------------------------------------*/
	public void debugTrace(String location, String str, int index){		
		System.out.println("	*******************");
		System.out.println("	Location: " + location + " " + this.getString() + "(" +this.getStringIndex() + ")");
		System.out.println("	Child values: " + getChildValues() );
		System.out.println("	Node type: " + this.getClass());
		System.out.println("	String to add: " + str + "(" + index +")");
		System.out.println();
		System.out.println();
	}

}
