package Lexing;

public class Token {
    final private TokenTypes type;
    final private String value;
    final private int line;
    final private int start_pos;

    public Token(TokenTypes type, String value, int line, int start_pos) {
        this.type = type;
        this.value = value;
        this.line = line;
        this.start_pos = start_pos;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(type);
        sb.append(" ");
        sb.append(value);
        sb.append(" ");
        sb.append(line);
        sb.append(" ");
        sb.append(start_pos);
        return sb.toString();
    }

    public TokenTypes getType() {
        return this.type;
    }

    public String getValue() {
        return this.value;
    }

    public int getLine() {
        return this.line;
    }

    public int getPos() {
        return this.start_pos;
    }
}
