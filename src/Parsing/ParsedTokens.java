package Parsing;

import Lexing.Token;
import Lexing.TokenTypes;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

public class ParsedTokens {
    Token token = null;
    static final HashSet<String> firstPriority = new HashSet<String>(Arrays.asList("*", "/"));
    static final HashSet<String> secondPriority = new HashSet<String>(Arrays.asList("+", "-"));
    LinkedList<ParsedTokens> children = new LinkedList<ParsedTokens>();

    public ParsedTokens(Token t) {
        this.token = t;
    }

    public ParsedTokens(ParsedTokens left, ParsedTokens right, Token t) {
        children.add(left);
        children.add(right);
        this.token = t;
    }

    public ParsedTokens(LinkedList<ParsedTokens> tokens, Token token) throws ParsingException {
        this.token = token;
        LinkedList<ParsedTokens> arg = new LinkedList<ParsedTokens>();
        int level = 0;
        for (int i = 0; i < tokens.size(); ++i) {
            if (tokens.get(i).getType() == TokenTypes.PARENTHESIS) {
                if (tokens.get(i).getValue().equals("(")) level++;
                else level--;
            }
            if (level == 0 && tokens.get(i).getType() == TokenTypes.SEPARATOR) {
                ParsedTokens pt = new ParsedTokens(arg);
                children.add(pt);
                arg.clear();
            } else {
                arg.add(tokens.get(i));
            }
        }
        ParsedTokens pt = new ParsedTokens(arg);
        children.add(pt); //divide into args
    }

    public void divideByOperands(LinkedList<ParsedTokens> operands, HashSet<String> operations) throws ParsingException {
        for (int i = 0; i < operands.size(); ++i) {
            if (operands.get(i).isSingle() && operands.get(i).getType().equals(TokenTypes.ARITHMETIC)) {
                if (operations.contains(operands.get(i).getValue())) {
                    setRight(new ParsedTokens(new LinkedList<ParsedTokens>(operands.subList(i + 1, operands.size()))));
                    if (i != 0)
                        setLeft(new ParsedTokens(new LinkedList<ParsedTokens>(operands.subList(0, i))));
                    token = operands.get(i).token;
                    break;
                }
            }
        }
    }

    public ParsedTokens(LinkedList<ParsedTokens> tokens) throws ParsingException {
        if (tokens.size() == 1) {
            this.ifSizeIsOne(tokens);
        } else {
            if (tokens.get(1).getType() == TokenTypes.ASSIGNMENT) {
                this.token = tokens.get(1).getToken();
                setLeftAndRight(tokens.get(0), new ParsedTokens(new LinkedList<ParsedTokens>(tokens.subList(2, tokens.size()))));
            } else {
                LinkedList<ParsedTokens> operands = this.getOperands(tokens);
                Iterator iter = operands.iterator();
                while (iter.hasNext()) {
                    ParsedTokens pt = (ParsedTokens) iter.next();
                    if (pt.getType().equals(TokenTypes.PARENTHESIS)) iter.remove();
                }
                //now we should have operands without any parentheses
                if (operands.size() == 1) {
                    this.ifSizeIsOne(operands);
                } else {
                    if (token == null) {
                        divideByOperands(operands, secondPriority);
                    }
                    if (token == null) {
                        divideByOperands(operands, firstPriority);
                    }

                }
            }
        }
    }

    public void ifSizeIsOne(LinkedList<ParsedTokens> tokens) {
        if (tokens.get(0).isSingle())
            this.token = tokens.get(0).getToken();
        else {
            children = tokens.get(0).getChildren();
            this.token = tokens.get(0).getToken();
        }
    }

    public LinkedList<ParsedTokens> getOperands(LinkedList<ParsedTokens> tokens) throws ParsingException {
        int i = 0;
        LinkedList<ParsedTokens> operands = new LinkedList<ParsedTokens>();
        boolean function_is_expected = false;
        int function_inx = -1;
        while (i < tokens.size()) {
            if (tokens.get(i).getType() == TokenTypes.FUNCTION) {
                if (tokens.get(i).operandsCount() == 0) {
                    function_is_expected = true;
                    function_inx = i;
                } else {
                    operands.add(tokens.get(i));
                }

                i++;
            } else {
                if (tokens.get(i).getType() == TokenTypes.PARENTHESIS) {
                    int balance = 1;
                    int start = i;
                    while (i < tokens.size() - 1) {
                        i = i + 1;
                        if (tokens.get(i).getType() == TokenTypes.PARENTHESIS) {
                            if (tokens.get(i).getValue().equals("(")) {
                                balance++;
                            } else {
                                balance--;
                            }
                            if (balance == 0)
                                break;
                        }
                    }
                    int end = i - 1;
                    if (balance == 0) {
                        if (function_is_expected) {
                            operands.add(new ParsedTokens(new LinkedList<ParsedTokens>(tokens.subList(start + 1, end + 1)),
                                    tokens.get(function_inx).getToken()));
                            function_is_expected = false;
                            function_inx = -1;
                        } else
                            operands.add(new ParsedTokens(new LinkedList<ParsedTokens>(tokens.subList(start + 1, end + 1))));
                    } else {
                        throw new ParenthesesException(tokens.get(start).token);
                    }
                } else {
                    if (function_is_expected) throw new FunctionStartException(tokens.get(function_inx).getToken());
                    else operands.add(tokens.get(i));
                }
                i++;
            }
        }
        return operands;
    }

    public LinkedList<ParsedTokens> getChildren() {
        return this.children;
    }

    public boolean isSingle() {
        return (children.isEmpty());
    }

    public boolean hasLeft() {
        return (children.size() > 1);
    }

    public Token getToken() {
        return this.token;
    }

    public ParsedTokens getRight() {
        if (children.isEmpty()) return null;
        else return this.children.get(0);
    }

    public ParsedTokens getLeft() {
        if (children.size() < 2) return null;
        else return this.children.get(1);
    }

    public int getLine() {
        return this.token.getLine();
    }

    public int operandsCount() {
        return children.size();
    }

    public int getPos() {
        return this.token.getPos();
    }

    public String getValue() {
        return this.token.getValue();
    }

    public void setLeft(ParsedTokens left) {
        if (children.size() == 1) children.add(left);
        else children.set(1, left);
    }

    public void setRight(ParsedTokens right) {
        if (children.isEmpty()) children.add(right);
        else children.set(0, right);
    }

    public void setLeftAndRight(ParsedTokens left, ParsedTokens right) {
        this.setRight(right);
        this.setLeft(left);
    }

    public TokenTypes getType() {
        return this.token.getType();
    }

    public String toString() {
        if (this.token.getType() == TokenTypes.FUNCTION) {
            StringBuilder st = new StringBuilder();
            st.append(token.getValue());
            st.append("(");
            for (int i = 0; i < children.size(); ++i) {
                st.append(children.get(i).toString());
                st.append(" ");
            }
            return st.toString();
        } else {
            String l = "";
            if (getLeft() != null)
                l = "(" + getLeft().toString();
            String r = "";
            if (getRight() != null) {
                r = getRight().toString() + ")";
            }
            return l + this.token.getValue() + r;
        }
    }
}
