package substringAlgorithms;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Julian Fenner
 * Root can have zero, one or more children.
 *
 */

/* NODE SPLIT LOGIC
 *checking if the substring is a prefix of the child's substring e.g child's substring = 11 and substring = 1.
If yes then SPLIT	  
In other word the substring to add ends part of the way through the child's substring value.
 in this case the node must be split
 two cases one for lead and one for non leaf

CASE FOR LEAF NODE
checking if the substring is a prefix of the child's substring e.g child's substring = 11 and substring = 1 	
 This case turns a LEAF node where the substring to add is a prefix of this node's substring field value.  
 * i.e
 * 		   \
 * 			\
 * 			11 (2)  <- adding 1 to leaf
 * 
 * 			\
 *           \
 *            1
 * 			 / \
 * 			/   \
 * 		null(3)  1(2)
 * 		
 * 
 * 
 * This requires 
 * 1) var for matching prefix 
 * 2) var for rest of string i.e suffix
 * 3) adding a child leaf node which has the suffix as its value along with the index of the current node
 * 4) current node 'converted' into non-leaf node by deleting its index (not applicable now) 
 * 5) and setting its substring field to prefix
 * 6) finally we need to give the other index of the prefix 
 * 
 * 
 **/


public class NodeImpl implements Node{
	private List<Node> children;
	private String subStringField;
	private Integer subStringIndex;
	
	//CONSTRUCTOR
	public NodeImpl(String subStringToAdd, int subStringIndex){
		this.subStringField = subStringToAdd;
		this.subStringIndex = subStringIndex;
		children = new ArrayList<Node>();
	}
	
	//GETTERS
	@Override
	public String getSubString() {
		return this.subStringField;
	}
	
	@Override
	public List<Node> getChildren() {
		return this.children;
	}

	
	@Override
	public String getChildStrings() {
		String childStr = "";
		for(Node next: children){
			childStr += next.getSubString() + " " + next.getSubStringIndex() +"\n";
		}
		return childStr;
	}
	
	@Override
	public int getSubStringIndex() {
		return this.subStringIndex;
	}
	
	
	//SETTERS
	@Override
	public void setSubString(String subString, int subStringIndex) {
		
//		System.out.println("Setting substring " + subString + " " + subStringIndex);
//		System.out.println("This subString: " + this.subStringField + " " + this.subStringIndex);
//		System.out.println("child subString: " + this.getChildStrings());
//		System.out.println();
		
		this.subStringField = subString;
		this.subStringIndex = subStringIndex;	
	}
		
	@Override
	public void updateSubString(String subString, int subStringIndex) {
		System.out.println("updateSubString " + subString + " " + subStringIndex);
		System.out.println();
		
		if(this.subStringField == "$") {
			this.subStringField = subString;
			this.subStringIndex = subStringIndex;
		} else {
		this.subStringField += subString;
		this.subStringIndex = subStringIndex;
		}
	
	}

	@Override
	/**
	 * Whenever child the new subString is checked to see if it a terminating value $, if it is placed at the end
	 */
	public void addChild(Node node) {
//		System.out.println("add child " + node.getSubString() + " " + node.getSubStringIndex());
//		System.out.println("Adding child");
//		System.out.println("This subString: " + this.subStringField + " " + this.subStringIndex);
//		System.out.println("child subString: " + this.getChildStrings());
//		System.out.println();
		
		if(node.getSubString() != "$"){
			this.children.add(0,node);	
		} else {
			this.children.add(this.children.size(),node);
		}
	}
	/**
	 * Method for adding new subString to tree
	 * @param subStringToAdd
	 * @param subStringIndex
	 */
	@Override
	public void addSubString(String subStringToAdd, int subStringIndex){
	for(Node child: children) {
//		System.out.println("----> SS TO ADD " + subStringToAdd + " " + subStringIndex);
//		System.out.println("----> Child SS " + child.getSubString());
		
		/* 
		 * TO DO - Refactor next block as too complicated and potentially buggy.
		 * Consider moving to method so it can be tested properly 
		 * To block deal with the case where the subString to add is an exact match of the node's subString
 		* Cases
  		* 1. The node has no children 
		*  ??? CHECK THIS FULLY
		* 2. The node has children
		* 	a. is has a child with a terminating "$" value
		* 		update this index value of this node to the new index
		*   b. there is no child with terminating "$" value
		*   	crate a new child node with "$" value and the new index
		This needs refactoring
		*/
		// TRY COMMENTING THIS BLOCK AND OBSERVE WHAT HAPPENS WHEN YOU ADD "aaa"
		if(child.getSubString().equals(subStringToAdd) && ! (subStringToAdd.equals("$"))){
				System.out.println("IN PROBLEM AREA------>");
				if(! (child.getChildren().isEmpty())){
					for(Node next: child.getChildren()){
						if(next.getSubString().equals("$")){
							next.updateSubString("$", subStringIndex);
							return;
						}
					}
					//if previous symbol has matched and there are no terminating symbols
					child.addChild(new NodeImpl("$", subStringIndex));
					return;
				}
		}else if (child.getSubString().equals("$")){ 
			//	BASE CASE 
			//i.e. this is a leaf node without any value then you are at correct place and just need to update the field
			//children will be sorted so that "$" for any given list of children will always be at the end.
			//TO DO - UPDATE INDEX TOO FOR SAFETY?
			child.setSubString(subStringToAdd, subStringIndex);
			return;	
		} else if(child.thisHasAPrefixOf(subStringToAdd)) {
			//TWO CASES HERE
			// 1 LEAF NODE
			// 2 NON LEAF NODE
			
			String prefix =  child.getSubString().substring(0, subStringToAdd.length());
			String suffix = child.getSubString().substring(subStringToAdd.length(), child.getSubString().length());
			child.addChild(new NodeImpl(suffix, child.getSubStringIndex()));
			child.setSubString(prefix, -1);
			child.addChild(new NodeImpl("$", subStringIndex)); 
			//	
			return;	
			}else if(child.thisIsAPrefixOf(subStringToAdd)){
				if(child.getChildren().isEmpty()) {
					//BASE CASE. child is a prefix of substring to add and there's no more children to traverse
					child.setSubString(subStringToAdd, subStringIndex);
					return;
				} else {	
					//RECURSIVE CASE.  child is a prefix of substring but there are more children to traverse
						child.addSubString(child.removePrefix(subStringToAdd), subStringIndex);		
						return;
					}	
			} 
		} 
		//FINAL CASE - Outside of loop 
		//If no cases have been matched the method (ie. there will be no return) then by definition we must create a new child node		
		addChild(new NodeImpl(subStringToAdd, subStringIndex));
	}
	
	
	/**
	 * throws exception if arg is shorter than subStringfield
	 * @param subStringArg
	 * @return
	 */
	@Override
	public String removePrefix(String subStringArg){
		return subStringArg.substring(this.subStringField.length());
	}
	

	@Override
	/**checks if this object's subString field is a prefix of param string.
	 * checks length as well to make sure they're not exact match
	 */
	public boolean thisIsAPrefixOf(String string) {
		if(string.startsWith(this.subStringField) && string.length() > this.subStringField.length()){
			return true;
	    } else {
	    	return false;
	    }
	}
	
	/**
	 * Check if argument is a prefix of object's subString field 
	 * @param string
	 * @return
	 */
	@Override
	public boolean thisHasAPrefixOf(String string) {
		if(this.subStringField.startsWith(string) && this.subStringField.length() >  string.length()){
			return true;
	    } else {
	    	return false;
	    }
	}
	
	@Override
	public void printTree(){
		for(Node child: children){
			if(child.getChildren().isEmpty()){
				System.out.println(child.getSubString() + " " + child.getSubStringIndex());
			} else {
				child.printTree();
			}		
		}
	}	
}
