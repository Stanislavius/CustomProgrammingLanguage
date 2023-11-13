package Testing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

class TestCase {
    String title;
    String[] input;
    String output;
    String commentary;

    public TestCase(String title, String[] input, String output, String commentary) {
        this.title = title;
        this.input = input;
        this.output = output;
        this.commentary = commentary;
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

    ;

    public static LinkedList<TestCase> readFile(String filepath) {
        LinkedList<TestCase> tests = new LinkedList<TestCase>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(filepath));
            String testName;
            while (((testName = br.readLine()) != null)) {
                LinkedList<String> inputStrings = new LinkedList<String>();
                br.readLine();
                br.readLine();
                String last = null;
                while (true) {
                    last = br.readLine();
                    if (last.equals(">")) {
                        break;
                    } else
                        inputStrings.add(last);
                }
                String outputString = br.readLine().substring("output = ".length());
                String commentaryString = br.readLine().substring("commentary = ".length());
                br.readLine();
                tests.add(new TestCase(testName, inputStrings.toArray(new String[0]), outputString, commentaryString));
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
