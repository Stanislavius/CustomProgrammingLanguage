import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    final static Pattern arithmetic_pattern = Pattern.compile("[\\+\\-\\/\\*]");
    final static Pattern int_pattern = Pattern.compile("\\b(?<!\\.)[0-9]+(?!\\.)\\b");
    final static Pattern float_pattern = Pattern.compile("[0-9]+\\.[0-9]+");
    final static Pattern parenthesis_pattern = Pattern.compile("[\\(\\)]{1}");
    final static Pattern assignment_pattern = Pattern.compile("=");
    //final static Pattern function_pattern = Pattern.compile("[a-zA-Z]+\\(.*\\)");
    final static Pattern function_pattern = Pattern.compile("[a-zA-Z]+\\(");
    final static Pattern variablePattern = Pattern.compile("[a-zA-Z]+(?!\\()\\b");
    final static Pattern separatorPattern = Pattern.compile(",");
    final private String filename;

    public Lexer(String filename) {
        this.filename = filename;
    }

    public Lexer(){this.filename = null;}

    public LinkedList<Token> read() throws Exception {
        LinkedList<Token> tokens = new LinkedList<Token>();
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String st = null;
        int line_num = 0;
        while ((st = br.readLine()) != null) {
            tokens.addAll(this.readline(st, line_num));
            line_num++;
        }
        return sort(tokens);
    }

    public static LinkedList<Token> sort(LinkedList<Token> tokens){
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
        return tokens;
    }
    public LinkedList<Token> read(String[] code){
        LinkedList<Token> tokens = new LinkedList<Token>();
        for(int line_num = 0; line_num < code.length; ++line_num){
            tokens.addAll(this.readline(code[line_num], line_num));
        }
        return sort(tokens);
    }

    public LinkedList<Token> readline(String st, int line_num){
        LinkedList<Token> tokens = new LinkedList<Token>();
        Matcher arithmetic_matcher = arithmetic_pattern.matcher(st);
        Matcher int_matcher = int_pattern.matcher(st);
        Matcher float_matcher = float_pattern.matcher(st);
        Matcher paranthesis_matcher = parenthesis_pattern.matcher(st);
        Matcher function_matcher = function_pattern.matcher(st);
        Matcher assignment_matcher = assignment_pattern.matcher(st);
        Matcher variableMatcher = variablePattern.matcher(st);
        Matcher separatorMatcher = separatorPattern.matcher(st);
        while (int_matcher.find()){
            int start = int_matcher.start();
            tokens.add(new Token(token_type.INT, int_matcher.group(), line_num, start));
        }

        while (float_matcher.find()){
            int start = float_matcher.start();
            tokens.add(new Token(token_type.FLOAT, float_matcher.group(), line_num, start));
        }

        while (arithmetic_matcher.find()){
            int start = arithmetic_matcher.start();
            tokens.add(new Token(token_type.arithmetic,
                    arithmetic_matcher.group(), line_num, start));
        }
        while (paranthesis_matcher.find()){
            int start = paranthesis_matcher.start();
            tokens.add(new Token(token_type.parenthesis,
                    paranthesis_matcher.group(), line_num, start));
        }
        while (function_matcher.find()){
            int start = function_matcher.start();
            String function_name = function_matcher.group();
            function_name = function_name.substring(0, function_name.indexOf("("));
            tokens.add(new Token(token_type.function,
                    function_name, line_num, start));
        }

        while (assignment_matcher.find()){
            int start = assignment_matcher.start();
            tokens.add(new Token(token_type.assignment, " = ", line_num, start));
        }

        while (variableMatcher.find()){
            int start = variableMatcher.start();
            tokens.add(new Token(token_type.variable, variableMatcher.group(), line_num, start));
        }

        while (separatorMatcher.find()){
            int start = separatorMatcher.start();
            tokens.add(new Token(token_type.separator, separatorMatcher.group(), line_num, start));
        }

        tokens.add(new Token(token_type.new_line, "", line_num, st.length() - 1));
        return tokens;
    }
}


enum token_type{
    INT,
    FLOAT,
    arithmetic,
    new_line,
    parenthesis,
    function,
    assignment,
    variable,
    separator
}


class Token{
    final private token_type type;
    final private String value;
    final private int line;
    final private int start_pos;
    public Token(token_type type, String value, int line, int start_pos){
        this.type = type;
        this.value = value;
        this.line = line;
        this.start_pos = start_pos;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(type);
        sb.append(" ");
        sb.append(value);
        sb.append(" ");
        sb.append(line);
        sb.append(" ");
        sb.append(start_pos);
        return sb.toString();
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
