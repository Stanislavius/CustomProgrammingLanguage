package Lexing;

public class Token {
    final private TokenType type;
    final private String value;
    final private int lineNum;
    final private int startPos;

    public Token(TokenType type, String value, int line, int startPos) {
        this.type = type;
        this.value = value;
        this.lineNum = line;
        this.startPos = startPos;
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
}
