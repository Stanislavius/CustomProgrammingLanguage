package Lexing.Exceptions;

public class MissingEndOfStringException extends LexingException{
    public MissingEndOfStringException(String line, int num, int pos){
        super(line, num, pos);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Quotes at ");
        sb.append(num);
        sb.append(" line, position is ");
        sb.append(pos);
        sb.append(" is not closed");
        return sb.toString();
    }
}
