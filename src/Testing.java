import java.util.LinkedList;

public class Testing {
    public static void main(String[] args){
        String[] code = {
                "1 + 2",
                "(27 * 4)*(12 - 4*5) + (21 - (56 * 43))",
                "(54 + 12) * (12 + 1) + (324 - 2) +- (12*4)*4",
                "(((1)+(2)))"
        };
        String[] expected_output = {"3", "-3251", "988", "3"};
        Lexer l = new Lexer();
        Parser parser = new Parser();
        LinkedList<Token> tokens = l.read(code);
        System.out.println("Lexer is done");
        LinkedList<ParsedTree> ps = parser.parse(tokens);
        System.out.println("Parsing is done");
        LinkedList<String> result = Executor.execute(ps);
        System.out.println("Executing is done");
        for (int i = 0; i < result.size(); ++i){
            System.out.println(i + " " + result.get(i).equals(expected_output[i]));
            assert result.get(i).equals(expected_output[i]);
        }
        System.out.println("All tests are passed");

    }
}
