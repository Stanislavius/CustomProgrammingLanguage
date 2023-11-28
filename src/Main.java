import Executing.Executor;
import Lexing.Exceptions.LexingException;
import Lexing.Lexer;
import Lexing.Token;
import Parsing.ParsedTokens.ParsedAbstractStatement;
import Parsing.ParsedTokens.ParsedToken;
import Parsing.Parser;

import java.util.LinkedList;

public class Main {
    public static void main(String[] args) {
        String filename = args[0];
        Lexer l = new Lexer(filename);
        Parser parser = new Parser();
        try {
            LinkedList<Token> tokens = l.read();
            if (l.isWithoutError()) {
                LinkedList<ParsedAbstractStatement> ps = parser.parse(tokens);
                String result = Executor.execute(ps);
                System.out.println(result);
            }
            else{
                LinkedList<LexingException> exceptions = l.getExceptions();
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
