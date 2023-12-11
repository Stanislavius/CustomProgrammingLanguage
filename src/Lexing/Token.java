package Lexing;

public class Token {
    final private TokenType type;
    final private String value;
    final private int lineNum;
    final private String line;
    final private int startPos;

    public Token(TokenType type, String value, int lineNum, int startPos, String line) {
        this.type = type;
        this.value = value;
        this.lineNum = lineNum;
        this.startPos = startPos;
        this.line = line;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(type);
        sb.append(" ");
        sb.append(value);
        sb.append(" ");
        sb.append(lineNum);
        sb.append(" ");
        sb.append(startPos);
        return sb.toString();
    }

    public final TokenType getType() {
        return this.type;
    }

    public final String getValue() {
        return this.value;
    }

    public final int getLineNum() {
        return this.lineNum;
    }

    public final int getPos() {
        return this.startPos;
    }
    public final String getLine(){return this.line;}
}
