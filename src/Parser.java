import java.io.FileOutputStream;
import java.util.Collections;
import java.util.Comparator;
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

        LinkedList<Token> block = new LinkedList<Token>();
        for(Token token : tokens){
            if (token.getType().equals(token_type.new_line)){
                program.add(new ParsedTree(block));
                block = new LinkedList<Token>();
            }
            else{
                block.add(token);
            }
        }
        int k = 4;
        return program;
    }
}

class ParsedTree{
    Token token = null;
    ParsedTree left;
    ParsedTree right;
    public ParsedTree(LinkedList<Token> tokens){
        int i = 0;
        if (tokens.size() == 1){
            this.token = tokens.get(0);
        }
        else {
            if (tokens.get(0).getType() == token_type.parenthesis){
                int start = 0;
                int end = -100;
                int balance = 1;
                i = 1;
                while (i < tokens.size() && balance != 0) {
                    if (tokens.get(i).getType() == token_type.parenthesis) {
                        if (tokens.get(i).getValue().equals("(")){
                            balance = 1;
                        }
                        while (i < tokens.size() && balance != 0){
                            if (tokens.get(i).getValue().equals("(")) {
                                balance = balance + 1;
                            }
                            if (tokens.get(i).getValue().equals(")")){
                                balance = balance - 1;
                            }
                            if (balance == 0) {
                                end = i;

                            }
                            else {
                                i++;
                            }
                        }

                    }
                    i++;
                }
                if (balance == 0){
                    if(start + 1 == end){
                        // something
                    }
                    else
                    {
                        if (start == 0 && end == (tokens.size() - 1)){
                            tokens.remove(tokens.size() - 1);
                            tokens.remove(0);
                        }
                        else {
                            left = new ParsedTree(new LinkedList<Token>(tokens.subList(start + 1, end)));
                            right = new ParsedTree(new LinkedList<Token>(tokens.subList(end + 2, tokens.size())));
                            token = tokens.get(end + 1);
                        }
                    }
                }
                else{
                    //Throw error
                }
            }

            if (token == null) {
                i = 0;
                while (i < tokens.size()) {
                    if (tokens.get(i).getType() == token_type.arithmetic) {
                        if (tokens.get(i).getValue().equals("+") || tokens.get(i).getValue().equals("-")) {
                            if (i != 0)
                                left = new ParsedTree(new LinkedList<Token>(tokens.subList(0, i)));
                            right = new ParsedTree(new LinkedList<Token>(tokens.subList(i + 1, tokens.size())));
                            token = tokens.get(i);
                            break;
                        }
                    }
                    i++;
                }
            }
            if (token == null) {
                i = 0;
                while (i < tokens.size()) {
                    if (tokens.get(i).getType() == token_type.arithmetic) {
                        if (tokens.get(i).getValue().equals("*") || tokens.get(i).getValue().equals("/")) {
                            left = new ParsedTree(new LinkedList<Token>(tokens.subList(0, i)));
                            right = new ParsedTree(new LinkedList<Token>(tokens.subList(i + 1, tokens.size())));
                            token = tokens.get(i);
                            break;
                        }
                    }
                    i++;
                }
            }
        }

    }

    public ParsedTree(Token t){
        this.token = t;
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
