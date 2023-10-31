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

    public LinkedList<Token> read() throws Exception {
        LinkedList<Token> tokens = new LinkedList<Token>();
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String st = null;
        int line_num = 0;
        while ((st = br.readLine()) != null) {
            Matcher arithmetic_matcher = arithmetic_pattern.matcher(st);
            Matcher int_matcher = int_pattern.matcher(st);
            while (int_matcher.find()){
                int start = int_matcher.start();
                tokens.add(new Token(token_type.INT, int_matcher.group(), line_num, start));
            }
            while (arithmetic_matcher.find()){
                int start = arithmetic_matcher.start();
                tokens.add(new Token(token_type.arithmetic,
                        arithmetic_matcher.group(), line_num, start));
            }
            tokens.add(new Token(token_type.new_line, "", line_num, st.length() - 1));

            line_num++;
        }
        return tokens;
    }
}


enum token_type{
    INT,
    arithmetic,
    new_line
}


class Token{
    private token_type type;
    private String value;
    private int line;
    private int start_pos;
    public Token(token_type type, String value, int line, int start_pos){
        this.type = type;
        this.value = value;
        this.line = line;
        this.start_pos = start_pos;
    }

    public String toString(){
        return type + " " + value + " " + line + " " + start_pos;
    }

    public token_type getType(){
        return this.type;
    }

    public String getValue(){
        return this.value;
    }

    public int getLine(){
        return this.line;
    }

    public int getPos(){
        return this.start_pos;
    }
}
