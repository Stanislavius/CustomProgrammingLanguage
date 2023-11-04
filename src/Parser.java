import java.io.FileOutputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;

public class Parser {
    public LinkedList<ParsedTokens> parse(LinkedList<Token> tokens){
        LinkedList<ParsedTokens> program= new LinkedList<ParsedTokens>();
        Collections.sort(tokens, new Comparator<Token>() {
            public int compare(Token t1, Token t2) {
                // compare two instance of `Score` and return `int` as result.
                int result = 0;
                if (t1.getLine() > t2.getLine()){
                    result = 1;
                }
                else{
                    if (t1.getLine() < t2.getLine())
                        result = - 1;
                    else{
                        if (t1.getPos() > t2.getPos()){
                            result = 1;
                        }
                        else{
                            if (t1.getPos() < t2.getPos()){
                                result = -1;
                            }
                            else{
                                result = 0;
                            }
                        }
                    }
                }
                return result;
            }
        });
        try {
            LinkedList<ParsedTokens> block = new LinkedList<ParsedTokens>();
            for (Token token : tokens) {
                if (token.getType().equals(token_type.new_line)) {
                    program.add(new ParsedTokens(block));
                    block = new LinkedList<ParsedTokens>();
                } else {
                    block.add(new ParsedTokens(token));
                }
            }
        }
        catch (ParsingException e){
            System.out.println(e);
        }
        return program;
    }
}



class ParsedTokens{
    final static String first_priority = "*/";
    final static String second_priority = "+-";
    Token token = null;
    LinkedList<ParsedTokens> children = new LinkedList<ParsedTokens>();

    public ParsedTokens(Token t){
        this.token = t;
    }

    public ParsedTokens(ParsedTokens left, ParsedTokens right, Token t){
        children.add(left);
        children.add(right);
        this.token = t;
    }

    public ParsedTokens(LinkedList<ParsedTokens> tokens, Token token) throws ParsingException {
        this.token = token;
        ParsedTokens pt = new ParsedTokens(tokens);
        children.add(pt); //divide into args
    }

    public ParsedTokens(LinkedList<ParsedTokens> tokens) throws ParsingException {
        if (tokens.size() == 1) {
            if (tokens.get(0).is_single()) this.token = tokens.get(0).token;
            else{
                children = tokens.get(0).getChildren();
                this.token = tokens.get(0).getToken();
            }
        }
        else {
            int i = 0;
            LinkedList<ParsedTokens> operands = new LinkedList<ParsedTokens>();
            boolean function_is_expected = false;
            int function_inx = -1;
            while (i < tokens.size()) {
                if (tokens.get(i).getType() == token_type.function) {
                    if (tokens.get(i).OperandsCount() == 0) {
                        function_is_expected = true;
                        function_inx = i;
                    }
                    i++;
                }
                else {
                    if (tokens.get(i).getType() == token_type.parenthesis) {
                        int balance = 1;
                        int start = i;
                        while (i < tokens.size() - 1) {
                            i = i + 1;
                            if (tokens.get(i).getType() == token_type.parenthesis) {
                                if (tokens.get(i).getValue().equals("(")) {
                                    balance++;
                                } else {
                                    balance--;
                                }
                                if (balance == 0)
                                    break;
                            }
                        }
                        int end = i - 1;
                        if (balance == 0) {
                            if (function_is_expected){ //TODO THIS, FUNCTION CAN HAVE MANY ARGS
                                operands.add(new ParsedTokens(new LinkedList<ParsedTokens>(tokens.subList(start + 1, end + 1)),
                                        tokens.get(function_inx).getToken()));
                                //there we should divide into args of func, will be done later
                                function_is_expected = false;
                                function_inx = - 1;
                            }
                            else operands.add(new ParsedTokens(new LinkedList<ParsedTokens>(tokens.subList(start + 1, end + 1))));
                        } else {
                            throw new ParenthesesException(tokens.get(start).token);
                        }
                    } else {
                        if(function_is_expected) throw new FunctionStartException(tokens.get(function_inx).getToken());
                        else operands.add(tokens.get(i));
                    }
                    i++;
                }
            }
            Iterator iter = operands.iterator();
            while (iter.hasNext()) {
                ParsedTokens pt = (ParsedTokens) iter.next();
                if (pt.getType().equals(token_type.parenthesis)) iter.remove();
            }
            //now we should have operands without any parentheses
            if (operands.size() == 1) {
                ParsedTokens pt =  operands.get(0);
                this.token = pt.token;
                this.children = pt.getChildren();
            } else {
                i = 0;
                if (token == null) {
                    i = 0;
                    while (i < operands.size()) {
                        if (operands.get(i).is_single() && operands.get(i).getType() == token_type.arithmetic) {
                            if (operands.get(i).getValue().equals("+") || operands.get(i).getValue().equals("-")) {
                                setRight(new ParsedTokens(new LinkedList<ParsedTokens>(operands.subList(i + 1, operands.size()))));
                                if (i != 0)
                                    setLeft(new ParsedTokens(new LinkedList<ParsedTokens>(operands.subList(0, i))));
                                token = operands.get(i).token;
                                break;
                            }
                        }
                        i++;
                    }
                }
                if (token == null) {
                    i = 0;
                    while (i < operands.size()) {
                        if (operands.get(i).is_single() && operands.get(i).getType() == token_type.arithmetic) {
                            if (operands.get(i).getValue().equals("*") || operands.get(i).getValue().equals("/")) {
                                setRight(new ParsedTokens(new LinkedList<ParsedTokens>(operands.subList(i + 1, operands.size()))));
                                setLeft(new ParsedTokens(new LinkedList<ParsedTokens>(operands.subList(0, i))));
                                token = operands.get(i).token;
                                break;
                            }
                        }
                        i++;
                    }
                }

            }
        }
    }

    public LinkedList<ParsedTokens> getChildren() {
        return this.children;
    }

    public boolean is_single(){
        return (children.isEmpty());
    }

    public boolean hasLeft(){
        return (children.size() > 1);
    }

    public Token getToken(){
        return this.token;
    }

    public ParsedTokens getRight(){
        if (children.isEmpty()) return null;
        else return this.children.get(0);
    }

    public ParsedTokens getLeft(){
        if (children.size() < 2) return null;
        else return this.children.get(1);
    }

    public int getLine(){
        return this.token.getLine();
    }

    public int OperandsCount(){
        return children.size();
    }

    public int getPos(){
        return this.token.getPos();
    }

    public String getValue(){
        return this.token.getValue();
    }

    public void setLeft(ParsedTokens left){
        if (children.size() == 1) children.add(left);
        else children.set(1, left);
    }

    public void setRight(ParsedTokens right){
        if (children.isEmpty()) children.add(right);
        else children.set(0, right);
    }

    public void setLeftAndRight(ParsedTokens left, ParsedTokens right){
        this.setRight(right);
        this.setLeft(left);
    }

    public token_type getType(){
        return this.token.getType();
    }

    public String toString(){
        if (this.token.getType() == token_type.function){
            StringBuilder st = new StringBuilder();
            st.append(token.getValue());
            st.append("(");
            for (int i = 0; i < children.size(); ++i) {
                st.append(children.get(i).toString());
                st.append(" ");
            }
            return st.toString();
        }
        else {
            String l = "";
            if (getLeft() != null)
                l = "(" + getLeft().toString();
            String r = "";
            if (getRight() != null) {
                r = getRight().toString() + ")";
            }
            return l + this.token.getValue() + r;
        }
    }
}


class ParsingException extends Exception{
    protected Token error_token;

    public ParsingException(Token t){
        error_token = t;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Error in line ");
        sb.append(error_token.getLine());
        sb.append(", position is ");
        sb.append(error_token.getPos());
        return sb.toString();
    }

}

class ParenthesesException extends ParsingException{
    public ParenthesesException(Token t){
        super(t);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Parentheses don't match. Starts at ");
        sb.append(error_token.getLine());
        sb.append(" line, position is ");
        sb.append(error_token.getPos());
        return sb.toString();
    }

}

class FunctionStartException extends ParsingException{
    public FunctionStartException(Token t){
        super(t);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("At this position are expected parentheses: ");
        sb.append(error_token.getLine());
        sb.append(" line, position is ");
        sb.append(error_token.getPos());
        return sb.toString();
    }

}
