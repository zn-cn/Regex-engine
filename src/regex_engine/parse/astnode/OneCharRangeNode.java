package regex_engine.parse.astnode;

import java.util.ArrayList;

// []
public class OneCharRangeNode implements ASTNode{
    private ArrayList<ASTNode> options;

    public OneCharRangeNode(ArrayList<ASTNode> options) {
        this.options = options;
    }

    @Override
    public String toString() {
        return "[]" + options;
    }

    public ArrayList<ASTNode> getOptions() {
        return this.options;
    }

    public void setOptions(ArrayList<ASTNode> options){
        this.options = options;
    }
}
