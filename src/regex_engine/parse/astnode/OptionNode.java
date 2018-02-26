package regex_engine.parse.astnode;

import java.util.ArrayList;
// (|)
public class OptionNode implements ASTNode {

	private ArrayList<ASTNode> options;

	public OptionNode(ArrayList<ASTNode> options) {
		this.options = options;
	}

	@Override
	public String toString() {
		return "Options" + options;
	}

	public ArrayList<ASTNode> getOptions() {
		return options;
	}

	public void setOptions(ArrayList<ASTNode> options){
		this.options = options;
	}

}
