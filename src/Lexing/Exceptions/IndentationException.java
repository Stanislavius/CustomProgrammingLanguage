package Lexing.Exceptions;

public class IndentationException  extends LexingException{
    public IndentationException(String line, int num, int pos){
        super(line, num, pos);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Wrong indent at ");
        sb.append(num);
        sb.append(" line, position is ");
        sb.append(pos);
        return sb.toString();
    }
}
