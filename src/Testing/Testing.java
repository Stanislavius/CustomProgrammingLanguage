package Testing;

import Executing.Executor;
import Lexing.Lexer;
import Lexing.Token;
import Parsing.ParsedTokens.ParsedAbstractStatement;
import Parsing.Parser;

import java.util.LinkedList;
import java.util.logging.Logger;

public class Testing {
    static Logger logger;
    final static String[] testFiles = {"Arithmetic_int.txt",
            "Arithmetic_float.txt",
            "Arithmetic_parentheses.txt",
            "conditional.txt",
            "function_def.txt",
            "variables.txt",
            "string.txt",
            "list.txt"};
    public static void main(String[] args){
        logger = TestingLogger.createTestingLogger();
        LinkedList<Integer[]> stats = new LinkedList<Integer[]>();
        for(int i = 0; i < testFiles.length; ++i){
            stats.add(runTestFile("tests/" + testFiles[i]));
        }

        int passed_total = 0;
        int error_total = 0;
        int wrong_total = 0;
        int total_cases = 0;
        for(int i = 0; i < stats.size(); ++i){
            logger.finest("Passed " + stats.get(i)[1] + " from " + stats.get(i)[0] + " in file " + testFiles[i]);
            total_cases += stats.get(i)[0];
            passed_total += stats.get(i)[1];
            wrong_total += stats.get(i)[2];
            error_total += stats.get(i)[3];
        }
        logger.info("In general passed " + passed_total + " out of " + total_cases);
        logger.info("In general wrong result in " + wrong_total + " out of " + total_cases);
        logger.info("In general error occurred in  " + error_total + " out of " + total_cases);
    }

    public static Integer[] runTestFile(String filename){
        LinkedList<TestCase> testCases = TestCase.readFile(filename);
        int counter = 0;
        Integer[] stats = {testCases.size(), 0, 0, 0};
        for (int j = 0; j < testCases.size(); ++j){
            try {
                Lexer l = new Lexer();
                LinkedList<Token> tokens = l.read(testCases.get(j).getInput());
                Parser parser = new Parser();
                LinkedList<ParsedAbstractStatement> ps = parser.parse(tokens);
                String result = Executor.execute(ps);
                if (result.equals(testCases.get(j).getOutput())) {
                    logger.info("Test " + j + " from " + filename + " file is passed");
                    counter++;
                    stats[1] += 1;
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append(String.format("Test %d from %s, file is failed \n", j, filename));
                    sb.append(String.format("Input = < %s > \n %s \n",
                            testCases.get(j).getInputOneString(),
                            testCases.get(j).getCommentary()));
                    sb.append(String.format("Desired output is %s got %s instead.",
                            testCases.get(j).getOutput(),
                            result));
                    logger.severe(sb.toString());
                    stats[2] += 1;
                }
            }
            catch (Exception e){
                logger.severe("Test " + j + " throws exception");
                stats[3] += 1;
            }

            Executor.clearVariables();
        }
        logger.info(String.format("Finished file %s, got %d out of %d tests passed.", filename, counter, testCases.size()));
        return stats;
    }
}

