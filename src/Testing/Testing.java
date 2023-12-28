package Testing;

import Executing.Executor;
import Lexing.Exceptions.LexingException;
import Lexing.Lexer;
import Lexing.Token;
import Parsing.ParsedTokens.AbstractStatementPT;
import Parsing.Parser;
import Parsing.ParsingExceptions.ParsingException;

import java.util.LinkedList;
import java.util.logging.Logger;

public class Testing {
    static Logger logger;
    final static String[] testFiles = {"Int.txt",
            "Float.txt",
            "Parentheses.txt",
            "conditional.txt",
            "function_def.txt",
            "variables.txt",
            "string.txt",
            "list.txt",
            "class_def.txt",
            "general.txt",
            "dict.txt",
            "ExecutionErrors_testing.txt",
            "Try_except_blocks.txt",
            "examples.txt"};
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

        int passedTotal = 0;
        int errorTotal = 0;
        int wrongTotal = 0;
        int totalCases = 0;
        int lexingWrongTotal = 0;
        int parsingWrongTotal = 0;

        for (int i = 0; i < stats.size(); ++i) {
            logger.finest("Passed " + stats.get(i)[1] + " from " + stats.get(i)[0] + " in file " + testFiles[i]);
            totalCases += stats.get(i)[0];
            passedTotal += stats.get(i)[1];
            wrongTotal += stats.get(i)[2];
            errorTotal += stats.get(i)[3];
            lexingWrongTotal += stats.get(i)[4];
            parsingWrongTotal += stats.get(i)[5];
        }
        logger.info("In general passed " + passedTotal + " out of " + totalCases);
        logger.info("In general wrong result in " + wrongTotal + " out of " + totalCases);
        logger.info("In general error occurred in  " + errorTotal + " out of " + totalCases);
        logger.info("In general lexing result is wrong in " + lexingWrongTotal + " out of " + totalCases);
        logger.info("In general parsing result is wrong in " + parsingWrongTotal + " out of " + totalCases);
    }

    public static Integer[] runTestFile(String filename) {
        LinkedList<TestCase> testCases = TestCase.readFile(filename);
        int counter = 0;
        Integer[] stats = {testCases.size(), 0, 0, 0, 0, 0};
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
            }
            else if (result == 3) {
                logger.severe("Test " + testCases.get(j).getTitle() + " output of lexer is wrong");
                stats[4] += 1;
            }
            else if (result == 4) {
                logger.severe("Test " + testCases.get(j).getTitle() + " output of parser is wrong");
                stats[5] += 1;
            }
            else if (result == -1) {
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
            default:
                System.out.printf("Cannot recognize type %s%n", test.getType());
                System.exit(-1);
                return -1;
        }

    }

    public static boolean isLexerOutputEqual(LinkedList<Token> tokens, LinkedList<String> expected){
        if (!(tokens.size() == expected.size()))
            return false;
        for (int i = 0; i < tokens.size(); ++i)
            if (!(tokens.get(i).toString().equals(expected.get(i).substring(1))))
                return false;
        return true;
    }

    public static boolean isParserOutputEqual(LinkedList<AbstractStatementPT> ps, LinkedList<String> expected){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < ps.size(); ++i){
            sb.append(ps.get(i).toString());
        }
        String output = sb.toString();
        sb = new StringBuilder();
        for(int i = 0; i < expected.size(); ++i){
            sb.append(expected.get(i).substring(1));
            sb.append("\n");
        }
        return output.equals(sb.toString());
    }

    public static int runPositiveTest(TestCase test) {
        //0 - test passed, 1 - wrong, 2 - error, 3 - lexer output is wrong, 4 - parser output is wrong
        try {
            Lexer l = new Lexer();
            LinkedList<Token> tokens = l.read(test.getInput());
            if (test.hasExpectedLexerOutput())
                if (!isLexerOutputEqual(tokens, test.getExpectedLexerOutput()))
                    return 3;
            Parser parser = new Parser();
            LinkedList<AbstractStatementPT> ps = parser.parse(tokens);
            if (test.hasExpectedParserOutput())
                if (!isParserOutputEqual(ps, test.getExpectedParserOutput()))
                    return 4;
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
            if (test.hasExpectedLexerOutput())
                if (!isLexerOutputEqual(tokens, test.getExpectedLexerOutput()))
                    return 3;
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
