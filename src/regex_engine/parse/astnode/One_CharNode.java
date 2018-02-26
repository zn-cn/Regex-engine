package regex_engine.parse.astnode;

// []中的 a-z等
public class One_CharNode implements ASTNode{
    private char upper_bound;
    private char lower_bound;
    public One_CharNode(char lower_bound, char upper_bound) {
        this.lower_bound = lower_bound;
        this.upper_bound = upper_bound;
    }

    public String toString() {
        return String.valueOf(String.valueOf(lower_bound) + "-" + String.valueOf(upper_bound));
    }

    public char getLower_bound() {
        return this.lower_bound;
    }

    public char getUpper_bound(){
        return this.upper_bound;
    }

    public void setUpper_bound(char upper_bound){
        this.upper_bound = upper_bound;
    }

    public void setLower_bound(char lower_bound){
        this.lower_bound = lower_bound;
    }
}
