package regex_engine.parse.astnode;

import java.util.ArrayList;

// []
public class OneCharRangeNode implements ASTNode{
    private ArrayList<ASTNode> options;

    public OneCharRangeNode(ArrayList<ASTNode> opts) {
        options = opts;
    }

    @Override
    public String toString() {
        return "[]" + options;
    }

    public ArrayList<ASTNode> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<ASTNode> op){
        options = op;
    }
}
