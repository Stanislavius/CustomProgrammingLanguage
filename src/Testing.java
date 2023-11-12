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
    final static String[] testFiles = {"Arithmetic_int.txt",
            "Arithmetic_float.txt",
            "Arithmetic_parentheses.txt",
            "conditional.txt",
            "function_def.txt",
            "variables.txt"};
    public static void main(String[] args){
        TestingLogger logger = new TestingLogger();
        LinkedList<Integer> passed = new LinkedList<Integer>();
        LinkedList<Integer> total = new LinkedList<Integer>();
        LinkedList<TestCase> wrong = new LinkedList<TestCase>();
        LinkedList<TestCase> failed = new LinkedList<TestCase>();
        for(int i = 0; i < testFiles.length; ++i){
            LinkedList<TestCase> testCases = TestCase.readFile("tests/" + testFiles[i]);
            int counter = 0;
            total.add(testCases.size());
            for (int j = 0; j < testCases.size(); ++j){
                try {
                    Lexer l = new Lexer();
                    LinkedList<Token> tokens = l.read(testCases.get(j).getInput());
                    Parser parser = new Parser(false);
                    LinkedList<ParsedAbstractStatement> ps = parser.parse(tokens);
                    String result = Executor.execute(ps);
                    if (result.equals(testCases.get(j).getOutput())) {
                        logger.info("Test " + j + " from " + testFiles[i] + " file is passed");
                        counter++;
                    } else {
                        wrong.add(testCases.get(j));
                        logger.info("Test " + j + " from " + testFiles[i] + " file is failed");
                        logger.info(testCases.get(j).getTitle());
                        logger.info(testCases.get(j).getCommentary());
                        logger.info("Desired output is " + testCases.get(j).getOutput() + " got " + result + " instead");
                    }
                }
                catch (Exception e){
                    failed.add(testCases.get(j));
                    logger.info("Test " + j + " throws exception");
                }

                Executor.clearVariables();
            }
            passed.add(counter);
        }

        logger.info("WRONG: ");
        for(int i = 0; i < wrong.size(); ++ i){
            logger.info(wrong.get(i).getInputOneString());
        }
        logger.info("FAILED: ");
        for(int i = 0; i < failed.size(); ++ i){
            logger.info(failed.get(i).getInputOneString());
        }

        int passed_total = 0;
        int total_cases = 0;
        for(int i = 0; i < passed.size(); ++i){
            logger.info("Passed " + passed.get(i) + " from " + total.get(i) + " in file " + testFiles[i]);
            passed_total += passed.get(i);
            total_cases += total.get(i);
        }
        logger.info("In general passed " + passed_total + " out of " + total_cases);
    }
}

class TestCase{
    String title;
    String[] input;
    String output;
    String commentary;
    public TestCase(String title, String[] input, String output, String commentary){
        this.title = title;
        this.input = input;
        this.output = output;
        this.commentary = commentary;
    }

    public String[] getInput() {return input;}
    public String getOutput() {return output;}
    public String getTitle(){return title;}
    public String getCommentary(){return commentary;};

    public static LinkedList<TestCase> readFile(String filepath){
        LinkedList<TestCase> tests = new LinkedList<TestCase>();
            try {
                BufferedReader br = new BufferedReader(new FileReader(filepath));
                String testName;
                while (((testName = br.readLine()) != null)){
                    LinkedList<String> inputStrings = new LinkedList<String>();
                    br.readLine();
                    br.readLine();
                    String last = null;
                    while (true){
                        last = br.readLine();
                        if (last.equals(">")){
                            break;
                        }
                        else
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

    public String getInputOneString(){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < input.length; ++i)
            sb.append(input[i] + "\n");
        return sb.toString();
    }

}
