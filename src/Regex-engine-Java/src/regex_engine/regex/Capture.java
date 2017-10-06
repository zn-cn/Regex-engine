package regex_engine.regex;

import regex_engine.compile.Compile;
import regex_engine.parse.Parse;
import regex_engine.parse.SyntaxError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Capture {
    private String regex;
    private StateMatch match;

    public Capture(String r) throws SyntaxError{
        regex = r;
        match = new StateMatch(Compile.compile(Parse.parse(r)));
    }
    public ArrayList<String> matchAndCapture(String input) throws SyntaxError {
        boolean isfinalstate = true;
        ArrayList<String> captures = new ArrayList<>();
        for (int i = 0; i < input.length(); i++) {
            int judge = judgeFunction();
            if (judge == 0) {
                if (!match.accept(input.charAt(i))) {
                    isfinalstate = false;
                    break;
                }
            } else if (judge == 1) {
                if (!match.acceptZeroOrMore(input.substring(i)))
                    isfinalstate = false;
                break;
            } else if (judge == 2) {
                if (!match.acceptZeroOrOne(input.substring(i)))
                    isfinalstate = false;
                break;
            } else if (judge == 3) {
                if (!match.acceptOneOrMore(input.substring(i)))
                    isfinalstate = false;
                break;
            } else if (judge == 4) {
                HashMap<Boolean, Integer> capture = match.captureString(input.substring(i));

                if (capture.containsKey(true)) {
                    captures.add(input.substring(i, i + capture.get(true)));
                    i = i + capture.get(true) - 1;

                } else{
                    isfinalstate = false;
                    break;
                }
            }else if (judge == 5) {
                if (!match.acceptOneCharRange(input.substring(i)))
                    isfinalstate = false;
                break;
            } else
                throw new SyntaxError("The engine has some problems.");
        }

        if (isfinalstate && match.isOnFinalState())
            isfinalstate = true;
//        boolean result = match.isOnFinalState();
        match.reset();
        if (isfinalstate){
            return captures;
        } else {
            throw new SyntaxError("The regex doesn't match your string.");
        }
    }

    private int judgeFunction() {
        int min = (Integer)match.getCurrentStates().toArray()[0];

        HashMap<String, HashSet<Integer>> state = match.getStateChart().getConnections().get(min);
        if (state.containsKey("**"))
            return 1;
        else if (state.containsKey("??"))
            return 2;
        else if (state.containsKey("++"))
            return 3;
        else if (state.containsKey("()"))
            return 4;
        else if (state.containsKey("{}"))
            return 5;
        else
            return 0;
    }
}
