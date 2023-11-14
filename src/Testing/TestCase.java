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
    String[] input;
    String output;
    String commentary;
    String type = "output"; // 0 - output, 1 - LexerError, 2 - ParsingError, 3 - ExecutionError

    public TestCase(String[] input, HashMap<String, String> params) {
        this.input = input;
        output = params.getOrDefault("output", DEFAULT_OUTPUT);
        commentary = params.getOrDefault("commentary", DEFAULT_COMMENTARY);
        type = params.getOrDefault("type", DEFAULT_TYPE);
    }

    public String[] getInput() {
        return input;
    }

    public String getOutput() {
        return output;
    }

    public String getCommentary() {
        return commentary;
    }


    public static LinkedList<TestCase> readFile(String filepath) {
        LinkedList<TestCase> tests = new LinkedList<TestCase>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(filepath));
            HashMap<String, String> params = new HashMap<String, String>();
            LinkedList<String> inputStrings = new LinkedList<String>();
            String line;
            boolean codeReading = false;
            br.readLine();
            while (((line = br.readLine()) != null)) {
                if (line.equals("<")) {
                    codeReading = true;
                    continue;
                }
                if (line.equals(">")){
                    codeReading = false;
                    continue;
                }
                if (codeReading)
                    inputStrings.add(line);
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
                    tests.add(new TestCase(inputStrings.toArray(new String[0]), params));
                    params.clear();
                    inputStrings.clear();
                }
            }

            if (inputStrings.size() > 0){
                tests.add(new TestCase(inputStrings.toArray(new String[0]), params));
                params.clear();
                inputStrings.clear();
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

}
