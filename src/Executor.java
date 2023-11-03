import java.util.LinkedList;

public class Executor {
    public static LinkedList<String> execute(LinkedList<ParsedTree> program){
        LinkedList<String> result = new LinkedList<String>();
        for (ParsedTree line: program){
            //System.out.println(line.toString());
            result.add(line.execute() + "");
        }
        return result;
    }
}
