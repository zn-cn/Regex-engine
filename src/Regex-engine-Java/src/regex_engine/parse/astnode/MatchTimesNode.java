package regex_engine.parse.astnode;

// {lower_bound, upper_bound}
public class MatchTimesNode implements ASTNode{
    private int upper_bound;
    private int lower_bound;
    private ASTNode node;   // {}之前的ASTNode

    public MatchTimesNode(String lower_bound, String upper_bound, ASTNode node) {
        this.lower_bound = Integer.parseInt(lower_bound);
        this.upper_bound = Integer.parseInt(upper_bound);
        this.node = node;
    }

    public MatchTimesNode(String upper_bound, ASTNode node){
        this.upper_bound = this.lower_bound = Integer.parseInt(upper_bound);
        this.node = node;
    }

    public MatchTimesNode(String lower_bound, int up, ASTNode node){
        this.lower_bound = Integer.parseInt(lower_bound);
        this.upper_bound = up;
        this.node = node;
    }
    public String toString() {
        return String.valueOf("{" + String.valueOf(upper_bound) + ", " + String.valueOf(lower_bound) + "}");
    }

    public String getLowwer_bound() {
        return String.valueOf(this.lower_bound);
    }

    public String getUpper_bound(){
        return String.valueOf(this.upper_bound);
    }

    public  ASTNode getNode(){
        return this.node;
    }
}
