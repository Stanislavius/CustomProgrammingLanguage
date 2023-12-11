package Executing.ExecutionExceptions;

import Lexing.Token;

public class WrongNumberOfArgumentsException extends ExecutionException {
    int needed;
    int provided;

    public WrongNumberOfArgumentsException(Token t, int needed, int provided) {
        super(t);
        this.needed = needed;
        this.provided = provided;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Function ");
        sb.append(errorToken.getValue());
        sb.append(" expects ");
        sb.append(needed);
        sb.append(" arguments, ");
        sb.append(provided);
        sb.append(" is given in line");
        sb.append(errorToken.getLineNum());
        sb.append(", position is ");
        sb.append(errorToken.getPos());
        return sb.toString();
    }
}
