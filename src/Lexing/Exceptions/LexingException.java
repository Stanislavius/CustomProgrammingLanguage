package Lexing.Exceptions;

public class LexingException extends Exception{
    String line;
    int num;
    int pos;
    public LexingException(String line, int num, int pos){
        this.line = line;
        this.num = num;
        this.pos = pos;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Error at ");
        sb.append(num);
        sb.append(" line, position is ");
        sb.append(pos);
        return sb.toString();
    }

    public String getTestingRepresentation(){
        return this.getClass().getSimpleName() + ": " + num + " " + pos;
    }
}
