import Executing.Executor;
import Lexing.Lexer;
import Lexing.Token;
import Parsing.ParsedTokens;
import Parsing.Parser;

import java.util.LinkedList;

public class Main {
    public static void main(String[] args) {
        String filename = args[0];
        Lexer l = new Lexer(filename);
        Parser parser = new Parser();
        try {
            LinkedList<Token> tokens = l.read();
            LinkedList<ParsedTokens> ps = parser.parse(tokens);
            String result = Executor.execute(ps);
            System.out.println(result);

        }
        catch(Exception e){
            System.out.println(e.getStackTrace());
        }

    }
}
