package regex_engine.parse.astnode;

import java.util.ArrayList;
// (|)
public class OptionNode implements ASTNode {

	private ArrayList<ASTNode> options;

	public OptionNode(ArrayList<ASTNode> opts) {
		options = opts;
	}

	@Override
	public String toString() {
		return "Opt" + options;
	}

	public ArrayList<ASTNode> getOptions() {
		return options;
	}

	public void setOptions(ArrayList<ASTNode> op){
		options = op;
	}

}
