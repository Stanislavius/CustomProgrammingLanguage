package Testing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

class TestCase {
    static final String CASE_SEPARATOR  = "#TESTCASE";
    static final String DEFAULT_OUTPUT  = "void";
    static final String DEFAULT_TYPE  = "output";
    static final String DEFAULT_COMMENTARY  = "No commentary provided.";
    static final String DEFAULT_TITLE  = "Test";
    String[] input;
    String output;
    String commentary;
    String title;
    String type = "output"; // 0 - output, 1 - LexingError, 2 - ParsingError
    LinkedList<String> expectedLexerOutput;
    LinkedList<String> expectedParserOutput;
    public TestCase(String[] input, HashMap<String, String> params) {
        this.input = input;
        output = params.getOrDefault("output", DEFAULT_OUTPUT);
        commentary = params.getOrDefault("commentary", DEFAULT_COMMENTARY);
        type = params.getOrDefault("type", DEFAULT_TYPE);
        title = params.getOrDefault("title", DEFAULT_TITLE);
    }

    public TestCase(String[] input, HashMap<String, String> params, HashMap<String, LinkedList<String>> multiLineParams) {
        this.input = input;
        output = params.getOrDefault("output", DEFAULT_OUTPUT);
        commentary = params.getOrDefault("commentary", DEFAULT_COMMENTARY);
        type = params.getOrDefault("type", DEFAULT_TYPE);
        title = params.getOrDefault("title", DEFAULT_TITLE);
        expectedLexerOutput = multiLineParams.getOrDefault("lexingOutput", null);
        expectedParserOutput = multiLineParams.getOrDefault("parsingOutput", null);
    }

    public String[] getInput() {
        return input;
    }

    public String getOutput() {
        return output;
    }

    public String getTitle() {
        return title;
    }

    public String getCommentary() {
        return commentary;
    }

    public boolean hasExpectedLexerOutput(){
        return !(this.expectedLexerOutput == null);
    }

    public boolean hasExpectedParserOutput(){
        return !(this.expectedParserOutput == null);
    }

    public LinkedList<String> getExpectedLexerOutput(){
        return this.expectedLexerOutput;
    }

    public LinkedList<String> getExpectedParserOutput(){
        return this.expectedParserOutput;
    }

    public static LinkedList<TestCase> readFile(String filepath) {
        LinkedList<TestCase> tests = new LinkedList<TestCase>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(filepath));
            HashMap<String, String> params = new HashMap<String, String>();
            LinkedList<String> inputStrings = new LinkedList<String>();
            HashMap<String, LinkedList<String>> multiLineParams = new HashMap<String, LinkedList<String>>();
            String line;
            LinkedList<String> lines = new LinkedList<String>();
            boolean reading = false;
            String paramName = null;
            br.readLine();
            while (((line = br.readLine()) != null)) {
                if (line.startsWith("#<")) {
                    if (line.equals("#<")) {
                        paramName = "input";
                    }
                    else
                        paramName = line.substring(2);
                    reading = true;
                    continue;
                }
                if (line.startsWith("#>")) {
                    if (line.equals("#>")) {
                        inputStrings = lines;
                    }
                    else
                        multiLineParams.put(paramName, lines);
                    lines = new LinkedList<String>();
                    reading = false;
                    continue;
                }
                if (reading){
                    lines.add(line);
                }
                else
                    try {
                        if (line.contains("=")) {
                            String param = line.substring(1, line.indexOf("=") - 1);
                            String value = line.substring(line.indexOf("=") + 2);
                            params.put(param, value);
                        }
                    }
                    catch (Exception e){
                        System.out.println(line);
                        e.printStackTrace();
                    }
                if (line.equals(CASE_SEPARATOR)) {
                    tests.add(new TestCase(inputStrings.toArray(new String[0]), params, multiLineParams));
                    params.clear();
                    inputStrings.clear();
                    multiLineParams.clear();
                    reading = false;
                }
            }

            if (inputStrings.size() > 0){
                tests.add(new TestCase(inputStrings.toArray(new String[0]), params, multiLineParams));
                params.clear();
                inputStrings.clear();
                multiLineParams.clear();
                reading = false;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return tests;
    }

    public String getInputOneString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length; ++i)
            sb.append(input[i] + "\n");
        return sb.toString();
    }

    public String getType(){
        return type;
    }
}
