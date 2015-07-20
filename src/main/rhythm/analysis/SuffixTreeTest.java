package analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import analysis.model.InnerNode;
import analysis.model.Node;
import analysis.model.NodeLeaf;
import analysis.model.NodeNonLeaf;
import analysis.model.NodeRoot;
import analysis.model.Rhythm_model;



public class SuffixTreeTest {

	public static void main(String[] args){
		// CHECK MORE DEPTH 2 TREES THEN TRY DEPTH 3. 
		
		// WORKING UP TO AB11AA
		// WORKING 112233
		//
		// WORKING AB11001
		// NOT WORKING AB110011AB
		// NOT WORKING WORKING 11001111
		// NOT WORKING 110011110 
		
		Rhythm_model test = new Rhythm_model("AB11001"); //trace construction debug output carefully
		test.getTree().printTree();
		
		
//		Map<String, List<Integer>> nodeMap = test.getTree().nodesToMap(new TreeMap<String, List<Integer>>());
//		for(Entry<String, List<Integer>> value: nodeMap.entrySet()){	
//    		System.out.println(value.getKey() + ": " + value.getValue());
//    	}
		
		
	
		
//		Node root = new NodeRoot();
//		List<InnerNode> children = new ArrayList<InnerNode>();
//		InnerNode nonLeaf = new NodeNonLeaf("a", 1, root, children);
//		
//		
//		InnerNode leafOther = new NodeLeaf("001111",0,nonLeaf);
//		InnerNode leafOther2 = new NodeLeaf("001111",0,nonLeaf);
//		InnerNode leaf$ = new NodeLeaf("$",6,nonLeaf);
//		children.add(leafOther);
//		//children.add(leafOther2);
//		children.add(leaf$);
//		
//		
//		System.out.println(leafOther.okToSplitNode('0'));
		
		
		
		
	}

}