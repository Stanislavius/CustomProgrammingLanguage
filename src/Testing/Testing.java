package Testing;

import Executing.Executor;
import Lexing.Exceptions.LexingException;
import Lexing.Lexer;
import Lexing.Token;
import Parsing.ParsedTokens.ParsedAbstractStatement;
import Parsing.Parser;
import Parsing.ParsingExceptions.ParsingException;

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
            "list.txt",
            "class_def.txt",
            "general.txt",
            "dict.txt"};
    final static String DEFAULT_TESTSPATH = "tests/";
    static String testsPath;

    public static void main(String[] args) {
        if (args.length > 0)
            testsPath = args[0];
        else
            testsPath = DEFAULT_TESTSPATH;
        logger = TestingLogger.createTestingLogger();
        LinkedList<Integer[]> stats = new LinkedList<Integer[]>();
        for (int i = 0; i < testFiles.length; ++i) {
            stats.add(runTestFile(testsPath + testFiles[i]));
        }

        int passed_total = 0;
        int error_total = 0;
        int wrong_total = 0;
        int total_cases = 0;
        for (int i = 0; i < stats.size(); ++i) {
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

    public static Integer[] runTestFile(String filename) {
        LinkedList<TestCase> testCases = TestCase.readFile(filename);
        int counter = 0;
        Integer[] stats = {testCases.size(), 0, 0, 0};
        for (int j = 0; j < testCases.size(); ++j) {
            int result = runTest(testCases.get(j));
            if (result == 0) {
                logger.info("Test " + j + " from " + filename + " file is passed");
                counter++;
                stats[1] += 1;
            } else if (result == 1) {
                StringBuilder sb = new StringBuilder();
                sb.append(String.format("Test %s from %s, file is failed \n", testCases.get(j).getTitle(), filename));
                logger.severe(sb.toString());
                stats[2] += 1;
            } else if (result == 2) {
                logger.severe("Test " + testCases.get(j).getTitle() + " throws exception");
                stats[3] += 1;
            } else if (result == -1) {
                logger.severe("ERROR IN TEST");
                System.exit(0);
            }

            Executor.clearVariables();
        }
        logger.info(String.format("Finished file %s, got %d out of %d tests passed.", filename, counter, testCases.size()));
        return stats;
    }

    public static int runTest(TestCase test) {
        switch (test.getType()) {
            case "output":
                return runPositiveTest(test);
            case "LexingError":
                return runNegativeLexing(test);
            case "ParsingError":
                return runNegativeParsing(test);
            case "ExucutionError":
                return runNegativeExecution(test);
            default:
                System.out.printf("Cannot recognize type %s%n", test.getType());
                System.exit(-1);
                return -1;
        }

    }

    public static int runPositiveTest(TestCase test) {
        try {
            Lexer l = new Lexer();
            LinkedList<Token> tokens = l.read(test.getInput());
            Parser parser = new Parser();
            LinkedList<ParsedAbstractStatement> ps = parser.parse(tokens);
            String result = Executor.execute(ps);
            if (result.equals(test.getOutput()))
                return 0;
            else
                return 1;
        } catch (Exception e) {
            return 2;
        }
    }

    public static int runNegativeParsing(TestCase test) {
        try {
            Lexer l = new Lexer();
            LinkedList<Token> tokens = l.read(test.getInput());
            Parser parser = new Parser();
            parser.parse(tokens);
            if (parser.isWithoutError())
                return 1;
            else {
                LinkedList<ParsingException> exceptions = parser.getExceptions();
                if (exceptions.get(0).getTestingRepresentation().equals(test.getOutput())) {
                    return 0;
                } else
                    return 1;
            }

        } catch (Exception e) {
            return 2;
        }
    }

    public static int runNegativeExecution(TestCase test) {
        try {
            Lexer l = new Lexer();
            LinkedList<Token> tokens = l.read(test.getInput());
            Parser parser = new Parser();
            LinkedList<ParsedAbstractStatement> ps = parser.parse(tokens);
            String result = Executor.execute(ps);
            if (result.equals(test.getOutput()))
                return 0;
        } catch (Exception e) {

        }
        return -1;
    }

    public static int runNegativeLexing(TestCase test) {
        try {
            Lexer l = new Lexer();
            LinkedList<Token> tokens = l.read(test.getInput());
            if (l.isWithoutError())
                return 1;
            else {
                LinkedList<LexingException> exceptions = l.getExceptions();
                if (exceptions.get(0).getTestingRepresentation().equals(test.getOutput())) {
                    return 0;
                } else
                    return 1;
            }

        } catch (Exception e) {
            return 2;
        }
    }
}

