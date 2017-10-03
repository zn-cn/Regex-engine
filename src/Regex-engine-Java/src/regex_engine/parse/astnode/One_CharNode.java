package regex_engine.parse.astnode;

import java.util.ArrayList;

// []中的 a-z等
public class One_CharNode {
    private ArrayList<ASTNode> options;

    public One_CharNode(ArrayList<ASTNode> opts) {
        options = opts;
    }

    @Override
    public String toString() {
        return "One_Char" + options;
    }

    public ArrayList<ASTNode> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<ASTNode> op){
        options = op;
    }
}
