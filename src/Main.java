import Executing.Executor;
import Lexing.Exceptions.LexingException;
import Lexing.Lexer;
import Lexing.Token;
import Parsing.ParsedTokens.AbstractStatementPT;
import Parsing.Parser;
import Parsing.ParsingExceptions.ParsingException;

import java.util.LinkedList;

public class Main {
    public static void main(String[] args) {
        String filename = args[0];
        Lexer lexer = new Lexer(filename);
        Parser parser = new Parser();
        try {
            LinkedList<Token> tokens = lexer.read();
            if (lexer.isWithoutError()) {
                LinkedList<AbstractStatementPT> ps = parser.parse(tokens);
                if (parser.isWithoutError()) {
                    String result = Executor.execute(ps);
                    System.out.println(result);
                }
                else{
                    LinkedList<ParsingException> exceptions = parser.getExceptions();
                    for(int i = 0; i < exceptions.size(); ++i){
                        System.out.println(exceptions.get(i));
                    }
                }
            }
            else{
                LinkedList<LexingException> exceptions = lexer.getExceptions();
                for(int i = 0; i < exceptions.size(); ++i){
                    System.out.println(exceptions.get(i));
                }
                System.exit(0);
            }

        }
        catch(Exception e){
            System.out.println(e.getStackTrace());
            System.out.println(e.toString());
        }

    }
}
