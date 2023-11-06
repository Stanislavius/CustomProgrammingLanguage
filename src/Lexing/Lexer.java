package Lexing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    final static Pattern arithmeticPattern = Pattern.compile("[\\+\\-\\/\\*]");
    final static Pattern intPattern = Pattern.compile("\\b(?<!\\.)[0-9]+(?!\\.)\\b");
    final static Pattern floatPattern = Pattern.compile("[0-9]+\\.[0-9]+");
    final static Pattern parenthesisPattern = Pattern.compile("[\\(\\)]{1}");
    final static Pattern assignmentPattern = Pattern.compile("=");
    //final static Pattern function_pattern = Pattern.compile("[a-zA-Z]+\\(.*\\)");
    final static Pattern functionPattern = Pattern.compile("[a-zA-Z]+\\(");
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
            tokens.addAll(this.readLine(st, line_num));
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
        for(int lineNum = 0; lineNum < code.length; ++lineNum){
            tokens.addAll(this.readLine(code[lineNum], lineNum));
        }
        return sort(tokens);
    }

    public LinkedList<Token> read(String code){
        String[] splitCode = code.split(System.lineSeparator());
        return this.read(splitCode);
    }

    public LinkedList<Token> readLine(String st, int lineNum){
        LinkedList<Token> tokens = new LinkedList<Token>();
        Matcher arithmeticMatcher = arithmeticPattern.matcher(st);
        Matcher intMatcher = intPattern.matcher(st);
        Matcher floatMatcher = floatPattern.matcher(st);
        Matcher paranthesisMatcher = parenthesisPattern.matcher(st);
        Matcher functionMatcher = functionPattern.matcher(st);
        Matcher assignmentMatcher = assignmentPattern.matcher(st);
        Matcher variableMatcher = variablePattern.matcher(st);
        Matcher separatorMatcher = separatorPattern.matcher(st);
        while (intMatcher.find()){
            int start = intMatcher.start();
            tokens.add(new Token(TokenTypes.INT, intMatcher.group(), lineNum, start));
        }

        while (floatMatcher.find()){
            int start = floatMatcher.start();
            tokens.add(new Token(TokenTypes.FLOAT, floatMatcher.group(), lineNum, start));
        }

        while (arithmeticMatcher.find()){
            int start = arithmeticMatcher.start();
            tokens.add(new Token(TokenTypes.ARITHMETIC,
                    arithmeticMatcher.group(), lineNum, start));
        }

        while (paranthesisMatcher.find()){
            int start = paranthesisMatcher.start();
            tokens.add(new Token(TokenTypes.PARENTHESIS,
                    paranthesisMatcher.group(), lineNum, start));
        }

        while (functionMatcher.find()){
            int start = functionMatcher.start();
            String function_name = functionMatcher.group();
            function_name = function_name.substring(0, function_name.indexOf("("));
            tokens.add(new Token(TokenTypes.FUNCTION,
                    function_name, lineNum, start));
        }

        while (assignmentMatcher.find()){
            int start = assignmentMatcher.start();
            tokens.add(new Token(TokenTypes.ASSIGNMENT, " = ", lineNum, start));
        }

        while (variableMatcher.find()){
            int start = variableMatcher.start();
            tokens.add(new Token(TokenTypes.VARIABLE, variableMatcher.group(), lineNum, start));
        }

        while (separatorMatcher.find()){
            int start = separatorMatcher.start();
            tokens.add(new Token(TokenTypes.SEPARATOR, separatorMatcher.group(), lineNum, start));
        }

        tokens.add(new Token(TokenTypes.NEWLINE, "", lineNum, st.length() - 1));
        return tokens;
    }
}
