package regex_engine.parse.astnode;

// {lower_bound, upper_bound}
public class MatchTimesNode implements ASTNode{
    private int upper_bound;
    private int lower_bound;

    public MatchTimesNode(String lower_bound, String upper_bound) {
        this.lower_bound = Integer.parseInt(lower_bound);
        this.upper_bound = Integer.parseInt(upper_bound);
    }

    public MatchTimesNode(String upper_bound){
        this.upper_bound = this.lower_bound = Integer.parseInt(upper_bound);
    }

    public MatchTimesNode(String lower_bound, int up){
        this.lower_bound = Integer.parseInt(lower_bound);
        this.upper_bound = up;
    }
    public String toString() {
        return String.valueOf("{" + String.valueOf(upper_bound) + ", " + String.valueOf(lower_bound) + "}");
    }

    public int getLowwer_bound() {
        return this.lower_bound;
    }

    public int getUpper_bound(){
        return this.upper_bound;
    }

}
