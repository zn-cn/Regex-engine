package regex_engine.parse.astnode;

import java.util.ArrayList;

public class ConcatNode implements ASTNode {

	private ArrayList<ASTNode> segments;

	public ConcatNode(ArrayList<ASTNode> s) {
		segments = s;
	}

    @Override
	public String toString() {
		return "Concat" + segments;
	}

	public ArrayList<ASTNode> getSegments() {
		return segments;
	}

	public void setSegments(ArrayList<ASTNode> s){
	    segments = s;
    }

}
