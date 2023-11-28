package Lexing;

import Lexing.Exceptions.IndentationException;
import Lexing.Exceptions.LexingException;
import Lexing.Exceptions.MissingEndOfStringException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    final static Pattern operationPattern = Pattern.compile("[\\+\\-\\/\\*\\.]|[=]{2}|[<]|[>]");
    final static Pattern intPattern = Pattern.compile("\\b(?<!\\.)[0-9]+(?!\\.)\\b");
    final static Pattern floatPattern = Pattern.compile("[0-9]+\\.[0-9]*");
    final static Pattern parenthesisPattern = Pattern.compile("[\\(\\)]{1}");
    final static Pattern assignmentPattern = Pattern.compile("(?<!\\=)[=](?!\\=)");
    //final static Pattern function_pattern = Pattern.compile("[a-zA-Z]+\\(.*\\)");
    final static Pattern listPattern = Pattern.compile("[\\[\\]]");
    final static Pattern dictPattern = Pattern.compile("[\\{\\}]");
    final static Pattern variablePattern = Pattern.compile("[a-zA-Z_]{1}[a-zA-Z_0-9]*");
    final static Pattern separatorPattern = Pattern.compile(",");
    final static Pattern colonPattern = Pattern.compile("\\:");
    final static LinkedList<String> blockWordsWithoutArgs = new LinkedList<String>(Arrays.asList("else", "try", "finally"));
    final static LinkedList<String> blockWordsWithArgs = new LinkedList<String>(Arrays.asList("if", "elif", "while", "def", "class", "catch"));
    //final static Pattern keywordPattern = Pattern.compile("if ");
    final private String filename;

    private LinkedList<LexingException> exceptions = new LinkedList<LexingException>();
    private LinkedList<Token> tokens = new LinkedList<Token>();
    public Lexer(String filename) {
        this.filename = filename;
    }

    public Lexer(){this.filename = null;}

    public LinkedList<Token> read(){
        String st = null;
        int line_num = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            while ((st = br.readLine()) != null) {
                try {
                    tokens.addAll(this.readLine(st, line_num));
                    line_num++;
                } catch (LexingException e) {
                    exceptions.add(e);
                }
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return sort(tokens);
    }

    public boolean isWithoutError(){
        return this.exceptions.isEmpty();
    }

    public LinkedList<LexingException> getExceptions(){return this.exceptions;}

    public void clear(){
        tokens.clear();
        exceptions.clear();
    }

    public LinkedList<Token> getTokens(){
        return tokens;
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
    
    public LinkedList<Token> read(String[] code) {
        LinkedList<Token> tokens = new LinkedList<Token>();
        for(int lineNum = 0; lineNum < code.length; ++lineNum){
            try {
                LinkedList<Token> cur = this.readLine(code[lineNum], lineNum);
                if (cur.size() > 1)
                    tokens.addAll(this.readLine(code[lineNum], lineNum));
            }
            catch (LexingException e) {
                exceptions.add(e);
            }
        }
        return sort(tokens);
    }

    public LinkedList<Token> read(String code) throws MissingEndOfStringException {
        String[] splitCode = code.split(System.lineSeparator());
        return this.read(splitCode);
    }

    public LinkedList<Token> readLine(String st, int lineNum) throws LexingException {
        LinkedList<Token> tokens = new LinkedList<Token>();
        int indentation = 0;
        int i = 0;
        int count = 0;
        while(i < st.length() && st.substring(i, i+1).equals(" ")){
            count++;
            if (count == 4) {
                tokens.add(new Token(TokenType.INDENTATION, "\t", lineNum, i));
                count = 0;
                indentation++;
            }
            i++;
        }
        boolean strIsExpected = false;
        int startStr = -1;
        for(i = 0; i < st.length(); ++i){
            if (st.charAt(i) == '\"'){
                if (strIsExpected){
                    tokens.add(new Token(TokenType.STRING, st.substring(startStr+1, i), lineNum, startStr));
                    st = st.substring(0, startStr) + " ".repeat(i-startStr+1) + st.substring(i+1);
                    strIsExpected = false;
                }
                else{
                    startStr = i;
                    strIsExpected = true;
                }

            }
            if (st.charAt(i) == '#'){
                if (!strIsExpected){
                    st = st.substring(0, st.indexOf("#"));
                    break;
                }
            }
        }

        if (strIsExpected)
            throw new MissingEndOfStringException(st, lineNum, startStr);

        if (count != 0){
            throw new IndentationException(st, lineNum, indentation+count);
        }

        st = st.substring(indentation * 4);

        for(i = 0; i < blockWordsWithArgs.size(); ++i){
            String word = blockWordsWithArgs.get(i);
            if (((word.length() + 1) <= st.length())) {
                if (st.substring(0, word.length() + 1).equals(word + " ")) {
                    int start = indentation * 4;
                    tokens.add(new Token(TokenType.BLOCKWORD, word, lineNum, start));
                    st = " " + st.substring(word.length(), st.length());
                }
            }
        }

        for(i = 0; i < blockWordsWithoutArgs.size(); ++i){
            String word = blockWordsWithoutArgs.get(i);
            if (st.length() == word.length()){
                if (st.equals(word)){
                    int start = indentation * 4;
                    tokens.add(new Token(TokenType.BLOCKWORD, word, lineNum, start));
                    st = " " + st.substring(word.length(), st.length());
                }
            }
        }

        Matcher intMatcher = intPattern.matcher(st);
        Matcher floatMatcher = floatPattern.matcher(st);
        Matcher paranthesisMatcher = parenthesisPattern.matcher(st);
        Matcher assignmentMatcher = assignmentPattern.matcher(st);
        Matcher variableMatcher = variablePattern.matcher(st);
        Matcher separatorMatcher = separatorPattern.matcher(st);
        Matcher listMatcher = listPattern.matcher(st);
        Matcher dictMatcher = dictPattern.matcher(st);
        Matcher colonMatcher = colonPattern.matcher(st);

        while (listMatcher.find()){
            int start = indentation * 4 + listMatcher.start();
            tokens.add(new Token(TokenType.LIST, listMatcher.group(), lineNum, start));
        }

        while (dictMatcher.find()){
            int start = indentation * 4 + dictMatcher.start();
            tokens.add(new Token(TokenType.DICT, dictMatcher.group(), lineNum, start));
        }

        while (colonMatcher.find()){
            int start = indentation * 4 + colonMatcher.start();
            tokens.add(new Token(TokenType.COLON, colonMatcher.group(), lineNum, start));
        }

        while (intMatcher.find()){
            int start = indentation * 4 + intMatcher.start();
            tokens.add(new Token(TokenType.INT, intMatcher.group(), lineNum, start));
        }

        while (floatMatcher.find()){
            int start = indentation * 4 + floatMatcher.start();
            String value = floatMatcher.group();
            tokens.add(new Token(TokenType.FLOAT, value, lineNum, start));
            st = st.substring(0, start) + " ".repeat(value.length()) + st.substring(start+value.length());
        }

        Matcher operationMatcher = operationPattern.matcher(st);
        while (operationMatcher.find()){
            int start = indentation * 4 + operationMatcher.start();
            tokens.add(new Token(TokenType.OPERATION,
                    operationMatcher.group(), lineNum, start));
        }

        while (paranthesisMatcher.find()){
            int start = indentation * 4 + paranthesisMatcher.start();
            tokens.add(new Token(TokenType.PARENTHESIS,
                    paranthesisMatcher.group(), lineNum, start));
        }

        while (assignmentMatcher.find()){
            int start = indentation * 4 + assignmentMatcher.start();
            tokens.add(new Token(TokenType.ASSIGNMENT, " = ", lineNum, start));
        }

        while (variableMatcher.find()){
            int start = indentation * 4 + variableMatcher.start();
            tokens.add(new Token(TokenType.VARIABLE, variableMatcher.group(), lineNum, start));
        }

        while (separatorMatcher.find()){
            int start = indentation * 4 + separatorMatcher.start();
            tokens.add(new Token(TokenType.SEPARATOR, separatorMatcher.group(), lineNum, start));
        }

        tokens.add(new Token(TokenType.NEWLINE, "", lineNum, indentation * 4 + st.length() - 1));
        return tokens;
    }
}
