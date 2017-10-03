package regex_engine.parse.astnode;

import java.util.ArrayList;
// (pattern)  含(|)
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

}
