import java.io.IOException;
import java.util.LinkedList;

/*
Grammar:
file: [statements]

statements: statement+

statement: |sum
           |(sum)

sum:
        | sum '+' term
        | sum '-' term
        | term

term:
        | term '*' factor
        | term '/' factor
        | factor

factor:
        | INT
        | -factor

INT     : [0-9]+
*/
public class Main {
    public static void main(String[] args) {
        String filename = args[0];
        Lexer l = new Lexer(filename);
        Parser parser = new Parser();
        try {
            LinkedList<Token> tokens = l.read();
            LinkedList<ParsedTree> ps = parser.parse(tokens);
            LinkedList<String> result = Executor.execute(ps);
            for (String to_print: result){
                System.out.println(to_print);
            }

        }
        catch(Exception e){
            System.out.println(e.getStackTrace());
        }

    }
}
