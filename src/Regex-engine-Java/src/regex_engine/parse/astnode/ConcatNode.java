package regex_engine.parse.astnode;

import java.util.ArrayList;

// ()或者一个区块
public class ConcatNode implements ASTNode {

	private ArrayList<ASTNode> segments;

	public ConcatNode(ArrayList<ASTNode> segments) {
		this.segments = segments;
	}

    @Override
	public String toString() {
		return "Concat" + this.segments;
	}

	public ArrayList<ASTNode> getSegments() {
		return this.segments;
	}

	public void setSegments(ArrayList<ASTNode> segments){
	    this.segments = segments;
    }

}
