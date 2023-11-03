import java.io.FileOutputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;

public class Parser {
    public LinkedList<ParsedTree> parse(LinkedList<Token> tokens){
        LinkedList<ParsedTree> program= new LinkedList<ParsedTree>();
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
            LinkedList<ParsedTree> block = new LinkedList<ParsedTree>();
            for (Token token : tokens) {
                if (token.getType().equals(token_type.new_line)) {
                    program.add(new ParsedTree(block));
                    block = new LinkedList<ParsedTree>();
                } else {
                    block.add(new ParsedTree(token));
                }
            }
        }
        catch (ParsingException e){
            System.out.println(e);
        }
        return program;
    }
}

class ParsedTree{
    final static String first_priority = "*/";
    final static String second_priority = "+-";
    Token token = null;
    ParsedTree left;
    ParsedTree right;
    public ParsedTree(LinkedList<ParsedTree> tokens) throws ParsingException {
        if (tokens.size() == 1) {
            if (tokens.get(0).is_single()) this.token = tokens.get(0).token;
            else{
                this.left = tokens.get(0).left;
                this.right = tokens.get(0).right;
                this.token = tokens.get(0).token;
            }
        }
        else {
            int i = 0;
            LinkedList<ParsedTree> operands = new LinkedList<ParsedTree>();
            while (i < tokens.size()) {
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
                        operands.add(new ParsedTree(new LinkedList<ParsedTree>(tokens.subList(start + 1, end+1))));
                    } else {
                        throw new ParenthesesException(tokens.get(start).token);
                    }
                } else {
                    operands.add(tokens.get(i));
                }
                i++;
            }
            Iterator iter = operands.iterator();
            while (iter.hasNext()){
                ParsedTree pt = (ParsedTree) iter.next();
                if (pt.getType().equals(token_type.parenthesis)) iter.remove();
            }
            //now we should have operands without any parentheses
            i = 0;
            if (token == null) {
                i = 0;
                while (i < operands.size()) {
                    if (operands.get(i).is_single() && operands.get(i).getType() == token_type.arithmetic) {
                        if (operands.get(i).getValue().equals("+") || operands.get(i).getValue().equals("-")) {
                            if (i != 0)
                                left = new ParsedTree(new LinkedList<ParsedTree>(operands.subList(0, i)));
                            right = new ParsedTree(new LinkedList<ParsedTree>(operands.subList(i + 1, operands.size())));
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
                            left = new ParsedTree(new LinkedList<ParsedTree>(operands.subList(0, i)));
                            right = new ParsedTree(new LinkedList<ParsedTree>(operands.subList(i + 1, operands.size())));
                            token = operands.get(i).token;
                            break;
                        }
                    }
                    i++;
                }
            }

        }
    }

    public boolean is_single(){
        if (this.left == null && this.right == null) return true;
        return false;
    }
    public ParsedTree(Object l, Object r, Token t){
        if (l.getClass().equals(Token.class))
            this.left = new ParsedTree((Token) l);
        else
            this.left = (ParsedTree) l;
        if (r.getClass().equals(Token.class))
            this.right = new ParsedTree((Token) r);
        else
            this.right = (ParsedTree) r;
        this.token = t;

    }

    public int getLine(){
        return this.token.getLine();
    }

    public int getPos(){
        return this.token.getPos();
    }

    public String getValue(){
        return this.token.getValue();
    }

    public void setLeft(ParsedTree left){
        this.left = left;
    }

    public void setRight(ParsedTree right){
        this.right = right;
    }

    public void setLeftAndRight(ParsedTree left, ParsedTree right){
        this.left = left;
        this.right = right;
    }

    public ParsedTree(Token t){
        this.token = t;
    }

    public ParsedTree(ParsedTree left, ParsedTree right, Token t){
        this.left = left;
        this.right = right;
        this.token = t;
    }

    public token_type getType(){
        return this.token.getType();
    }

    public int execute(){
        if (left == null && right == null){
            return Integer.parseInt(this.token.getValue());
        }
        else{
            switch (this.token.getValue()) {
                case "+":
                    if (left == null){
                        return right.execute();
                    }
                    else{
                        return left.execute() + right.execute();
                    }
                case "-":
                    if (left == null){
                        return -right.execute();
                    }
                    else{
                        return left.execute() - right.execute();
                    }
                case "*":
                        return left.execute() * right.execute();
                case "/":
                    return left.execute() / right.execute();
            }
        }
        return 0;

    }

    public String toString(){
        String l = "";
        if (left != null)
            l = "("+left.toString();
        String r = "";
        if (right != null){
            r = right.toString() + ")";
        }
        return l + this.token.getValue() + r;
    }
}

class Operand{
    int priority;
    Token token;
    String return_type;
    public Operand(){

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
