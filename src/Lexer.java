import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    final String[] arithmetic_op = {"+", "-", "*", "/"};
    Pattern arithmetic_pattern = Pattern.compile("[+-/*]{1}");
    Pattern int_pattern = Pattern.compile("[0-9]+");
    final private String filename;

    public Lexer(String filename) {
        this.filename = filename;
    }

    public LinkedList<ExpressionToken> read() throws Exception {
        LinkedList<ExpressionToken> tokens = new LinkedList<ExpressionToken>();
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String st = null;
        while ((st = br.readLine()) != null) {
            Matcher arithmetic_matcher = arithmetic_pattern.matcher(st);
            Matcher int_matcher = int_pattern.matcher(st);
            if (arithmetic_matcher.find()){
                String op = arithmetic_matcher.group();
                if (arithmetic_matcher.find()){
                    //System.out.println("Error");
                    //throw new Exception("Can't process expression.");
                }
                if (int_matcher.find()){
                    int arg1 = Integer.parseInt(int_matcher.group());
                    int_matcher.find();
                    int arg2 = Integer.parseInt(int_matcher.group());
                    ExpressionToken new_token = new ExpressionToken(op, arg1, arg2);
                    tokens.add(new_token);
                }
            }
        }
        return tokens;
    }
}

class ExpressionToken{
    final private String op;
    final private int arg1;
    final private int arg2;
    public ExpressionToken(String op, int arg1, int arg2){
        this.op = op;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public String getOp(){
        return this.op;
    }

    public int getArg1(){
        return this.arg1;
    }

    public int getArg2(){
        return this.arg2;
    }

    public String toString(){
        return this.arg1 + this.op + this.arg2;
    }
}
