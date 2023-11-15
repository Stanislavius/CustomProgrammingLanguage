package Lexing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
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
    final static Pattern assignmentPattern = Pattern.compile("(?<!\\=)[=](?!\\=)");
    final static Pattern comparisonPattern = Pattern.compile("[=]{2}|[<]|[>]");
    //final static Pattern function_pattern = Pattern.compile("[a-zA-Z]+\\(.*\\)");
    final static Pattern functionPattern = Pattern.compile("[a-zA-Z]{1}[a-zA-Z0-9]+\\(");
    final static Pattern listPattern = Pattern.compile("[\\[\\]}]");
    final static Pattern variablePattern = Pattern.compile("[a-zA-Z]+(?!\\()\\b");
    final static Pattern separatorPattern = Pattern.compile(",");
    final static Pattern memberPattern = Pattern.compile("\\.");
    final static Pattern colonPattern = Pattern.compile("\\:");
    final static LinkedList<String> blockWords = new LinkedList<String>(Arrays.asList("if", "elif", "else", "while", "def"));
    //final static Pattern keywordPattern = Pattern.compile("if ");
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
            LinkedList<Token> cur = this.readLine(code[lineNum], lineNum);
            if (cur.size() > 1)
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
        if (count != 0){
            //throw error
        }
        st = st.substring(indentation * 4);
        for(i = 0; i < blockWords.size(); ++i){
            String word = blockWords.get(i);
            if (st.length() == "else".length()){
                if (word.equals("else") && st.equals("else")){
                    int start = indentation * 4;
                    tokens.add(new Token(TokenType.BLOCKWORD, word, lineNum, start));
                    st = " " + st.substring(word.length(), st.length());
                }
            }
            else {
                if (((word.length() + 1) <= st.length())) {
                        if (st.substring(0, word.length() + 1).equals(word + " ")) {
                            int start = indentation * 4;
                            tokens.add(new Token(TokenType.BLOCKWORD, word, lineNum, start));
                            st = " " + st.substring(word.length(), st.length());
                        }
                }
            }

        }

        Matcher arithmeticMatcher = arithmeticPattern.matcher(st);
        Matcher intMatcher = intPattern.matcher(st);
        Matcher floatMatcher = floatPattern.matcher(st);
        Matcher paranthesisMatcher = parenthesisPattern.matcher(st);
        Matcher functionMatcher = functionPattern.matcher(st);
        Matcher assignmentMatcher = assignmentPattern.matcher(st);
        Matcher variableMatcher = variablePattern.matcher(st);
        Matcher separatorMatcher = separatorPattern.matcher(st);
        Matcher comparisonMatcher = comparisonPattern.matcher(st);
        Matcher listMatcher = listPattern.matcher(st);
        Matcher memberMatcher = memberPattern.matcher(st);
        Matcher colonMatcher = colonPattern.matcher(st);

        while (listMatcher.find()){
            int start = indentation * 4 + listMatcher.start();
            tokens.add(new Token(TokenType.LIST, listMatcher.group(), lineNum, start));
        }

        while (memberMatcher.find()){
            int start = indentation * 4 + memberMatcher.start();
            tokens.add(new Token(TokenType.MEMBER, memberMatcher.group(), lineNum, start));
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
            tokens.add(new Token(TokenType.FLOAT, floatMatcher.group(), lineNum, start));
        }

        while (arithmeticMatcher.find()){
            int start = indentation * 4 + arithmeticMatcher.start();
            tokens.add(new Token(TokenType.ARITHMETIC,
                    arithmeticMatcher.group(), lineNum, start));
        }

        while (paranthesisMatcher.find()){
            int start = indentation * 4 + paranthesisMatcher.start();
            tokens.add(new Token(TokenType.PARENTHESIS,
                    paranthesisMatcher.group(), lineNum, start));
        }

        while (functionMatcher.find()){
            int start = indentation * 4 + functionMatcher.start();
            String function_name = functionMatcher.group();
            function_name = function_name.substring(0, function_name.indexOf("("));
            tokens.add(new Token(TokenType.FUNCTION,
                    function_name, lineNum, start));
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

        while (comparisonMatcher.find()){
            int start = indentation * 4 + comparisonMatcher.start();
            tokens.add(new Token(TokenType.COMPARISON, comparisonMatcher.group(), lineNum, start));
        }

        tokens.add(new Token(TokenType.NEWLINE, "", lineNum, indentation * 4 + st.length() - 1));
        return tokens;
    }
}
