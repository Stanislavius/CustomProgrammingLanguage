package Lexing.Exceptions;

public class UnrecognizedTokenException extends LexingException{
    public UnrecognizedTokenException(String line, int num, int pos) {
        super(line, num, pos);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Unrecognized token at ");
        sb.append(num);
        sb.append(" line, position ");
        sb.append(pos);
        return sb.toString();
    }
}
