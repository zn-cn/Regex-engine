package regex_engine.parse.astnode;

public class ESCNode implements ASTNode{
    // ^ $  \\w  \\s \\d

    private int function;
    public ESCNode(int function){
        this.function = function;
    }

    @Override
    public String toString() {
        if (function == 1)
            return "ESC: ^";
        else if (function == 2)
            return "ESC: $";
        else if (function == 3)
            return "ESC: \\w";
        else if (function == 4)
            return "ESC: \\s";
        else if (function == 5)
            return "ESC: \\d";
        else if (function == 6)
            return "ESC: .";
        else
            return super.toString();
    }

    public int getFunction(){
        return this.function;
    }

    public void setFunction(int function){
        this.function = function;
    }
}
