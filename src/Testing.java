import Executing.Executor;
import Lexing.Lexer;
import Lexing.Token;
import Parsing.ParsedTokens;
import Parsing.Parser;

import java.util.LinkedList;

public class Testing {
    public static void main(String[] args){
        LinkedList<TestCase> testCases = new LinkedList<TestCase>();
        testCases.add(new TestCase("1", "1"));
        testCases.add(new TestCase("1 + 3", "4"));
        testCases.add(new TestCase("1 - 3", "-2"));
        testCases.add(new TestCase("1 * 3", "3"));
        testCases.add(new TestCase("3 / 3", "1"));
        testCases.add(new TestCase("(1+4)", "5"));
        testCases.add(new TestCase("(1 + 3)*(4+4)", "32"));
        testCases.add(new TestCase("4*(1+5)", "24"));
        testCases.add(new TestCase("float(4)", "4.0"));
        testCases.add(new TestCase("int(25.0)", "25"));
        testCases.add(new TestCase("4.0+4", "8.0"));
        testCases.add(new TestCase("s = 4 \r\n s", "4"));
        testCases.add(new TestCase("a = 2\r\ns = 34 + a \r\n s", "36"));
        testCases.add(new TestCase("abs(-4)", "4"));
        testCases.add(new TestCase("abs(-4.0)", "4.0"));
        testCases.add(new TestCase("abs(-4.0)", "4.0"));
        Lexer l = new Lexer();
        Parser parser = new Parser();
        int i = 0;
        for(i = 0; i < testCases.size(); ++i){
            LinkedList<Token> tokens = l.read(testCases.get(i).getInput());
            LinkedList<ParsedTokens> ps = parser.parse(tokens);
            LinkedList<String> result = Executor.execute(ps);
            String[] splitOutput = testCases.get(i).getOutput().split(System.lineSeparator());
            if (result.size() != splitOutput.length){
                System.out.println("Test " + i + " failed!");
            }
            else{
                int j = 0;
                for (j = 0; j < result.size(); ++j){
                    if(!result.get(j).equals(splitOutput[j])){
                        System.out.println("Test " + i + " failed!");
                        break;
                    }
                }
                if(j == result.size()) System.out.println("Test " + i + " is passed!");
            }
            Executor.clearVariables();
        }
        if (i == testCases.size())
            System.out.println("All tests are passed");

    }
}

class TestCase{
    String input;
    String output;
    String commentary;
    public TestCase(String input, String output){
        this.input = input;
        this.output = output;
        //this.commentary = commentary;
    }

    public String getInput() {return input;}
    public String getOutput() {return output;}

}
