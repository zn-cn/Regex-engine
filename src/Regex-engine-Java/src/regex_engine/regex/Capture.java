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
    private ArrayList<String> captures = new ArrayList<>();

    public Capture(String r) throws SyntaxError{
        regex = r;
        match = new StateMatch(Compile.compile(Parse.parse(regex)));
    }
    public ArrayList<String> matchAndCapture(String input) throws SyntaxError {
        boolean isfinalstate = true;
        for (int i = 0; i < input.length(); i++) {
            int judge = judgeFunction(0);
            if (judge == 0) {
                if (!match.accept(input.charAt(i))) {
                    isfinalstate = false;
                    break;
                }
            } else if (judge == 1) {
                if (!acceptZeroOrMore(input.substring(i)))
                    isfinalstate = false;
                break;
            } else if (judge == 2) {
                if (!acceptZeroOrOne(input.substring(i)))
                    isfinalstate = false;
                break;
            } else if (judge == 3) {
                if (!acceptOneOrMore(input.substring(i)))
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
                if (!acceptOneCharRange(input.substring(i)))
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

    private int judgeFunction(int f) throws SyntaxError{
        int min = (Integer) match.getCurrentStates().toArray()[0];
        HashMap<String, HashSet<Integer>> state = match.getStateChart().getConnections().get(min);
        if (f == 0){
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
        } else if (f == 1){
            if (state.containsKey("??"))
                return 2;
            else if (state.containsKey("++"))
                return 3;
            else if (state.containsKey("()"))
                return 4;
            else if (state.containsKey("{}"))
                return 5;
            else
                return 0;
        } else if (f == 2){
            if (state.containsKey("**"))
                return 1;
            else if (state.containsKey("++"))
                return 3;
            else if (state.containsKey("()"))
                return 4;
            else if (state.containsKey("{}"))
                return 5;
            else
                return 0;
        } else if (f == 3){
            if (state.containsKey("**"))
                return 1;
            else if (state.containsKey("??"))
                return 2;
            else if (state.containsKey("()"))
                return 4;
            else if (state.containsKey("{}"))
                return 5;
            else
                return 0;
        } else
            throw new SyntaxError("The engine has some ploblems.");

    }

    // 匹配 ？
    public boolean acceptZeroOrOne(String input) throws SyntaxError {
//        HashSet<Integer> currentStates2 = new HashSet<>(currentStates);

        int bound = (Integer) match.getStateChart().getConnections().get((Integer) match.getCurrentStates().toArray()[0]).get("??").toArray()[0];
        int time = 0;
        for (int i = 0; i < input.length(); i++) {
            int judge;
            if (i == 0)
                judge = judgeFunction(2);
            else
                judge = judgeFunction(0);

            if (judge == 0) {
                if (!match.accept(input.charAt(i))) {
                    if (time == 0){
                        match.getCurrentStates().clear();
                        match.getCurrentStates().add(bound);
                        i = -1;
                        time++;
                    } else
                        break;

                }
            } else if (judge == 1) {
                if (!acceptZeroOrMore(input.substring(i))) {
                    if (time == 0){
                        match.getCurrentStates().clear();
                        match.getCurrentStates().add(bound);
                        i = -1;
                        time++;
                    } else
                        break;
                }
            } else if (judge == 2) {
                if (!acceptZeroOrOne(input.substring(i))) {
                    if (time == 0){
                        match.getCurrentStates().clear();
                        match.getCurrentStates().add(bound);
                        i = -1;
                        time++;
                    } else
                        break;
                }
            } else if (judge == 3) {
                if (!acceptOneOrMore(input.substring(i))) {
                    if (time == 0){
                        match.getCurrentStates().clear();
                        match.getCurrentStates().add(bound);
                        i = -1;
                        time++;
                    } else
                        break;
                }
            } else if (judge == 4) {
                HashMap<Boolean, Integer> capture = match.captureString(input.substring(i));
                if (!capture.containsKey(true)) {
                    if (time == 0){
                        match.getCurrentStates().clear();
                        match.getCurrentStates().add(bound);
                        i = -1;
                        time++;
                    } else
                        break;
                } else {
                    captures.add(input.substring(i, i + capture.get(true)));
                    i = i + capture.get(true) - 1;
                }
            } else if (judge == 5) {
                if (!acceptOneCharRange(input.substring(i))) {
                    if (time == 0){
                        match.getCurrentStates().clear();
                        match.getCurrentStates().add(bound);
                        i = -1;
                        time++;
                    } else
                        break;
                }
            } else
                throw new SyntaxError("The engine has some problems.");

        }
        return match.isOnFinalState();
    }

    // TODO
    // 匹配{}
    public boolean acceptOneCharRange(String input) {
        return false;
    }

    // TODO
    // 匹配 +
    public boolean acceptOneOrMore(String input) {
        return true;
    }

    // TODO
    // 匹配 *
    public boolean acceptZeroOrMore(String input) {
        return true;
    }
}
