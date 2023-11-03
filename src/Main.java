import java.io.IOException;
import java.util.LinkedList;

/*
Grammar:
file: [statements]

statements: statement+

statement: sum |
           mul |
           sub |
           div |

sum: INT + INT
mul: INT * INT
sub: INT - INT
div: INT / INT

INT     : [0-9]+
*/
public class Main {
    public static void main(String[] args) {
        String filename = args[0];
        Lexer l = new Lexer(filename);
        try {
            LinkedList<ExpressionToken> tokens = l.read();
            for (ExpressionToken token : tokens) System.out.println(token);
            System.out.println("Execution");
            for (ExpressionToken token: tokens){
                int result = 0;
                switch (token.getOp()){
                    case ("+"):
                        result = token.getArg1() + token.getArg2();
                        break;
                    case ("-"):
                        result = token.getArg1() - token.getArg2();
                        break;
                    case ("*"):
                        result = token.getArg1() * token.getArg2();
                        break;
                    case ("/"):
                        result = token.getArg1() / token.getArg2();
                        break;

                }
                System.out.println(result);
            }
        }
        catch(Exception e){
            System.out.println(e.getStackTrace());
        }

    }
}
