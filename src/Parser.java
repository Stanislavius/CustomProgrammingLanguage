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
        if (tokens.size() == 1){
            this.token = tokens.get(0);
        }
        else {
            int i = 0;
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
}
