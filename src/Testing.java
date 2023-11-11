import Executing.Executor;
import Lexing.Lexer;
import Lexing.Token;
import Parsing.ParsedTokens.ParsedAbstractStatement;
import Parsing.Parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class Testing {
    final static String[] testFiles = {"Arithmetic_int.txt", "Arithmetic_float.txt"};
    public static void main(String[] args){
        for(int i = 0; i < testFiles.length; ++i){
            LinkedList<TestCase> testCases = TestCase.readFile("tests/" + testFiles[i]);
            for (int j = 0; j < testCases.size(); ++j){
                Lexer l = new Lexer();
                LinkedList<Token> tokens = l.read(testCases.get(i).getInput());
                Parser parser = new Parser();
                //LinkedList<ParsedAbstractStatement> ps = parser.parse(tokens);
                /*String result = Executor.execute(ps);
                if (result.equals(testCases.get(i).getOutput())){
                    System.out.println("Test " + j + " from " + testFiles[i] + " file is passed");
                }
                else{
                    System.out.println("Test " + j + " from " + testFiles[i] + " file is failed");
                    System.out.println(testCases.get(i).getTitle());
                    System.out.println(testCases.get(i).getCommentary());
                    System.out.println("Desired output is " + testCases.get(i).getOutput() + "got " + result + " instead");
                }
                Executor.clearVariables();

                 */
            }
        }
    }
}

class TestCase{
    String title;
    String input;
    String output;
    String commentary;
    public TestCase(String title, String input, String output, String commentary){
        this.title = title;
        this.input = input;
        this.output = output;
        this.commentary = commentary;
    }

    public String getInput() {return input;}
    public String getOutput() {return output;}
    public String getTitle(){return title;}
    public String getCommentary(){return commentary;};

    public static LinkedList<TestCase> readFile(String filepath){
        LinkedList<TestCase> tests = new LinkedList<TestCase>();
            try {
                BufferedReader br = new BufferedReader(new FileReader(filepath));
                String testName;
                while (((testName = br.readLine()) != null)){
                    String inputString = br.readLine().substring("input = ".length());
                    String outputString = br.readLine().substring("output = ".length());
                    String commentaryString = br.readLine().substring("commentary = ".length());
                    tests.add(new TestCase(testName, inputString, outputString, commentaryString));
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

        return tests;
    }

}
