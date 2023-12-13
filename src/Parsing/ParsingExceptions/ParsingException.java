package Parsing.ParsingExceptions;

import Lexing.Token;

public class ParsingException extends Exception {
    protected String line;
    protected int lineNum;
    protected int posNum;
    protected String message = "Error in ";
    public ParsingException(Token t) {
        this.line = t.getLine();
        this.lineNum = t.getLineNum();
        this.posNum = t.getPos();
    }

    public ParsingException(String line, int lineNum, int posNum) {
        this.line = line;
        this.lineNum = lineNum;
        this.posNum = posNum;
    }

    public ParsingException(Token t, String message) {
        this.line = t.getLine();
        this.lineNum = t.getLineNum();
        this.posNum = t.getPos();
        this.message = message;
    }

    public ParsingException(String line, int lineNum, int posNum, String message) {
        this.line = line;
        this.lineNum = lineNum;
        this.posNum = posNum;
        this.message = message;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Line: \n");
        sb.append(line);
        sb.append("\n");
        sb.append(message);
        sb.append(lineNum);
        sb.append(" ");
        sb.append(posNum);
        return sb.toString();
    }

    public String getTestingRepresentation(){
        return this.getClass().getSimpleName() + ": " + lineNum + " " + posNum;
    }

}
