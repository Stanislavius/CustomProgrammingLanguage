package Parsing;

import Lexing.Exceptions.LexingException;
import Lexing.Token;
import Lexing.TokenType;
import Parsing.ParsedTokens.*;
import Parsing.ParsingExceptions.ParenthesesException;
import Parsing.ParsingExceptions.ParsingException;

import java.util.*;
import java.util.logging.Logger;

import static Parsing.ParsingLogger.createParsingLogger;

public class Parser {
    static Logger logger = createParsingLogger();
    static final HashSet<String> SECOND_PRIORITY = new HashSet<String>(Arrays.asList("+", "-"));
    static final HashSet<String> FIRST_PRIORITY = new HashSet<String>(Arrays.asList("*", "/"));
    static final HashSet<String> COMPARISON_OPERATIONS = new HashSet<String>(Arrays.asList("==", "<", ">"));
    static final HashSet<String> MEMBER_OPERATIONS = new HashSet<String>(Arrays.asList("."));
    static final HashSet<String> STRUCTURE_OPENED = new HashSet<String>(Arrays.asList("[", "{", "("));
    static final HashSet<String> STRUCTURE_CLOSED = new HashSet<String>(Arrays.asList("]", "}", ")"));
    static final HashSet<TokenType> STRUCTURE_TYPES = new HashSet(Arrays.asList(TokenType.LIST, TokenType.DICT, TokenType.PARENTHESIS));

    LinkedList<ParsingException> exceptions = new LinkedList<ParsingException>();
    LinkedList<ParsedAbstractStatement> parsedLines = new LinkedList<ParsedAbstractStatement>();

    public boolean isWithoutError(){
        return this.exceptions.isEmpty();
    }

    public LinkedList<ParsingException> getExceptions(){return this.exceptions;}

    public void clear(){
        parsedLines.clear();
        exceptions.clear();
    }

    public LinkedList<ParsedAbstractStatement> getParsedLines(){
        return parsedLines;
    }

    public LinkedList<ParsedAbstractStatement> parse (LinkedList<Token> tokens){
        LinkedList<LinkedList<Token>> lines = divideByLines(tokens);
        try {
                logger.fine("Start parsing");
            for(int i = 0; i < lines.size(); ++i){
                    logger.fine("Start parsing " + i + " line \r\n"+ParsingLogger.tokensToString(lines.get(i)));
                parsedLines.add(parseLine(lines.get(i)));
                    logger.info(i + " line is parsed \r\n" + parsedLines.getLast().toString());
            }
            parsedLines = processBlocks(parsedLines);
        }
        catch (ParsingException e){
            exceptions.add(e);
        }
        logger.finest("Parsing is finished, parsed program as follows. ");
        for(int i = 0; i < parsedLines.size(); ++i)
            logger.finest(parsedLines.get(i).toString());
        return parsedLines;
    }

    public LinkedList<LinkedList<Token>> divideByLines(LinkedList<Token> tokens){
        LinkedList<LinkedList<Token>> lines = new LinkedList<LinkedList<Token>>();
        LinkedList<Token> line = new LinkedList<Token>();
        for (Token token : tokens) {
            if (token.getType().equals(TokenType.NEWLINE)) {
                lines.add(line);
                line = new LinkedList<Token>();
            } else
                line.add(token);
        }
        return lines;
    }

    public ParsedAbstractStatement parseLine(LinkedList<Token> line) throws ParsingException{ //call it to parse whole line
        int indent = 0;
        ParsedAbstractStatement statement;
        while (indent < line.size() && line.get(indent).getType() == TokenType.INDENTATION) //count and remove indentation
            indent++;
        for(int i = 0; i < indent; ++i)
            line.removeFirst(); // checked indent and removed it from list of tokens
        if (line.get(0).getType() == TokenType.BLOCKWORD) {
            return parseLineWithBlockword(indent, line);
        }
        else{
            //if not BLOCKWORD then expression or assignment
            for(int i = 0; i < line.size(); ++i)
                if (line.get(i).getType() == TokenType.ASSIGNMENT){
                    return new ParsedAssigmentStatement(line.get(i),
                            indent,
                            parseExpressionTokens(new LinkedList<Token>(line.subList(0, i))),
                            parseExpressionTokens(new LinkedList<Token>(line.subList(i+1, line.size()))));
                }
            return new ParsedStatement(indent, parseExpressionTokens(line));
        }
    }

    public ParsedAbstractStatement parseLineWithBlockword(int indent, LinkedList<Token> line) throws ParsingException {
        if (line.get(0).getValue().equals("if")){
            return new ParsedConditionalStatement(line.get(0),
                    indent,
                    parseExpressionTokens(new LinkedList<Token>(line.subList(1, line.size()))));
        }
        if (line.get(0).getValue().equals("elif")){
            return new ParsedConditionalStatement(line.get(0),
                    indent,
                    parseExpressionTokens(new LinkedList<Token>(line.subList(1, line.size()))));
        }
        if (line.get(0).getValue().equals("else")){
            return new ParsedConditionalStatement(line.get(0),
                    indent);
        }
        if (line.get(0).getValue().equals("while")){
            return new ParsedConditionalStatement(line.get(0),
                    indent,
                    parseExpressionTokens(new LinkedList<Token>(line.subList(1, line.size()))));
        }
        if (line.get(0).getValue().equals("def")){
            return new ParsedFunctionDefinition(line.get(0),
                    indent, line.get(1),
                    parseArgsTokens(new LinkedList<Token>(line.subList(2, line.size()))));
        }

        if (line.get(0).getValue().equals("class")){
            return new ParsedClassDefinition(line.get(0),
                    indent, line.get(1));
        }

        line.removeFirst();
        // rest is processed as expression if needed
        return null;
    }

    public LinkedList<ParsedVariable> parseArgsTokens(LinkedList<Token> line) throws ParsingException {
        LinkedList<ParsedVariable> args = new LinkedList<ParsedVariable>();
        for (int i = 1; i < line.size()-1; ++i) {
            if (line.get(i).getType() == TokenType.PARENTHESIS) {

            }
            if (line.get(i).getType() == TokenType.SEPARATOR) {

            } else {
                args.add(new ParsedVariable(line.get(i)));
            }
        }
        // can process exceptions here
        return args;
    }

    public ParsedToken parseExpressionTokens(LinkedList<Token> line) throws ParsingException {
        LinkedList<ParsedToken> parsedLine = new LinkedList<ParsedToken>();
        for(int i = 0; i < line.size(); ++i){
            parsedLine.add(new ParsedToken(line.get(i)));
        }
        for(int i = 0; i < parsedLine.size(); ++i){
            if (parsedLine.get(i).getType() == TokenType.VARIABLE){
                parsedLine.set(i, new ParsedVariable(parsedLine.get(i).getToken()));
            }
            if (parsedLine.get(i).getType() == TokenType.STRING){
                parsedLine.set(i, new ParsedStringToken(parsedLine.get(i).getToken()));
            }
            if (parsedLine.get(i).getType() == TokenType.INT){
                parsedLine.set(i, new ParsedIntToken(parsedLine.get(i).getToken()));
            }
            if (parsedLine.get(i).getType() == TokenType.FLOAT){
                parsedLine.set(i, new ParsedFloatToken(parsedLine.get(i).getToken()));
            }
        } // we can do that much for now

        return parseExpression(parsedLine);
    }

    public ParsedToken divideByBinaryOperands(LinkedList<ParsedToken> operands, HashSet<String> operations) throws ParsingException {
        ParsedToken result = null;
        for (int i = operands.size()-1; i >= 0; --i) {
            if (operations.contains(operands.get(i).getValue()) && operands.get(i).getParsedType() == ParsedTokenType.UNKNOWN_OPERATION) {
                LinkedList<ParsedToken> right = new LinkedList<ParsedToken>(operands.subList(i + 1, operands.size()));
                if (i != 0) {
                    LinkedList<ParsedToken> left = new LinkedList<ParsedToken>(operands.subList(0, i));
                    return new ParsedBinaryExpression(operands.get(i).getToken(), parseExpression(left), parseExpression(right));
                }
                else{
                    return new ParsedUnaryExpression(operands.get(i).getToken(), parseExpression(right));
                }
            }
        }
        return null;
    }

    public LinkedList<ParsedToken> divideByUnaryOperands(LinkedList<ParsedToken> operands) throws ParsingException {
        ParsedToken result = null;
        HashSet<String> unaryOperations = new HashSet<String>(Arrays.asList("-", "+"));
        Iterator iter = operands.iterator();
        LinkedList<ParsedToken> newOperands = new LinkedList<ParsedToken>();
        LinkedList<ParsedToken> q = new LinkedList<ParsedToken>();
        for (int i = 0; i < operands.size(); ++i) {
            if (operands.get(i).getParsedType() == ParsedTokenType.UNKNOWN_OPERATION && unaryOperations.contains(operands.get(i).getValue())){
                q.add(operands.get(i));
            }
            else {
                if (q.size() > 1){
                    ParsedToken op = operands.get(i);
                    int j = q.size() - 1;
                    while (j >= 1){
                        op = new ParsedUnaryExpression(q.get(j).getToken(), op);
                        j--;
                    }
                    newOperands.add(q.get(0));
                    newOperands.add(op);
                    q.clear();
                }
                else {
                    if (q.isEmpty() == false)
                        newOperands.add(q.get(0));
                    newOperands.add(operands.get(i));
                    q.clear();
                }
            }

        }
        return newOperands;
    }

    public ParsedFunctionCall parseFunctionCall(Token head, LinkedList<ParsedToken> line) throws ParsingException {
        LinkedList<ParsedToken> values = new LinkedList<ParsedToken>();
        LinkedList<LinkedList<ParsedToken>> args = separateArgs(line);
        for(int i = 0; i < args.size(); ++i){
            values.add(parseExpression(args.get(i)));
        }
        return new ParsedFunctionCall(head, values);
    }

    public ParsedListToken parseList(Token head, LinkedList<ParsedToken> line) throws ParsingException {
        LinkedList<ParsedToken> values = new LinkedList<ParsedToken>();
        LinkedList<LinkedList<ParsedToken>> args = separateArgs(line);
        for(int i = 0; i < args.size(); ++i){
            values.add(parseExpression(args.get(i)));
        }
        return new ParsedListToken(head, values);
    }

    public LinkedList<LinkedList<ParsedToken>> separateArgs(LinkedList<ParsedToken> line){
        LinkedList<LinkedList<ParsedToken>> args = new LinkedList<LinkedList<ParsedToken>>();
        LinkedList<ParsedToken> arg = new LinkedList<ParsedToken>();
        int balance = 1;
        for (int i = 0; i < line.size(); ++i) {
            if (STRUCTURE_TYPES.contains(line.get(i).getType())) {
                if (STRUCTURE_OPENED.contains(line.get(i).getValue()))
                    balance++;
                else
                    balance--;
            }
            if (line.get(i).getType() == TokenType.SEPARATOR && balance == 1) {
                args.add(arg);
                arg = new LinkedList<ParsedToken>();
            } else {
                arg.add(line.get(i));
            }
        }
        if (!arg.isEmpty())
            args.add(arg);
        return args;
    }

    public LinkedList<ParsedToken> parseColon(LinkedList<ParsedToken> line) throws ParsingException {
        LinkedList<ParsedToken> args = new LinkedList<ParsedToken> ();
        LinkedList<ParsedToken> arg = new LinkedList<ParsedToken>();
        for (int i = 0; i < line.size(); ++i) {
            if (line.get(i).getType() == TokenType.COLON){
                args.add(parseExpression(arg));
                arg = new LinkedList<ParsedToken>();
            } else {
                arg.add(line.get(i));
            }
        }
        if (!arg.isEmpty())
            args.add(parseExpression(arg));
        return args;
    }

    public ParsedDictToken parseDict(Token head, LinkedList<ParsedToken> line) throws ParsingException {
        LinkedList<ParsedToken> keys = new LinkedList<ParsedToken>();
        LinkedList<ParsedToken> values = new LinkedList<ParsedToken>();
        LinkedList<LinkedList<ParsedToken>> args = separateArgs(line);
        for(int i = 0; i < args.size(); ++i){
            LinkedList<ParsedToken> curPair = parseColon(args.get(i));
            keys.add(curPair.get(0));
            values.add(curPair.get(1));
        }

        return new ParsedDictToken(head, keys, values);
    }

    public LinkedList<ParsedToken> processStructures(LinkedList<ParsedToken> line) throws ParsingException {
        Iterator iter = line.iterator();
        LinkedList<ParsedToken> operands = new LinkedList<ParsedToken>();
        LinkedList<ParsedToken> operand = new LinkedList<ParsedToken>();
        int balance = 0;
        boolean processStructure = false;
        Token processedStructure = null;
        while (iter.hasNext()){
            ParsedToken current = (ParsedToken)iter.next();
            Token curToken = current.getToken();
            if (STRUCTURE_TYPES.contains(curToken.getType())){
                if (STRUCTURE_OPENED.contains(curToken.getValue())){
                    if (balance == 0)
                        processedStructure = curToken;
                    balance++;
                    processStructure = true;
                }
                else if (STRUCTURE_CLOSED.contains(curToken.getValue())){
                    balance--;
                }
                operand.add(current);
                if (balance == 0){
                    operand.removeLast();
                    operand.removeFirst();
                    ParsedToken result = null;
                    if (operands.size() == 0 || operands.getLast().getParsedType() == ParsedTokenType.UNKNOWN_OPERATION){
                        switch (processedStructure.getType()) {
                            case PARENTHESIS:
                                result = parseExpression(operand);
                                break;
                            case DICT:
                                result = parseDict(processedStructure, operand);
                                break;
                            case LIST:
                                result = parseList(processedStructure, operand);
                        }

                    }
                    else {
                        ParsedToken left = operands.removeLast();
                        ParsedToken right = null;
                        switch (processedStructure.getType()) {
                            case PARENTHESIS:
                                right = parseFunctionCall(processedStructure, operand);
                                break;
                            case DICT:
                                right = parseDictCall(operand);
                                break;
                            case LIST:
                                right = parseListCall(operand);
                        }
                        result = new ParsedBinaryExpression(processedStructure, left, right);
                    }
                    if (result != null)
                        operands.add(result);
                    operand = new LinkedList<ParsedToken>();
                    processStructure = false;
                    processedStructure = null;
                }
            }
            else{
                if (processStructure)
                    operand.add(current);
                else
                    operands.add(current);
            }
        }
        if (balance != 0)
            throw new ParenthesesException(processedStructure);
        return operands;
    }

    private ParsedToken parseListCall(LinkedList<ParsedToken> operand) {
        return null; //TODO
    }

    private ParsedToken parseDictCall(LinkedList<ParsedToken> operand) {
        return null; //TODO
    }

    public ParsedToken parseExpression(LinkedList<ParsedToken> line) throws ParsingException {
        if (line.isEmpty()) {
            return null;
        }
        if (line.size() == 1) {
            return line.getFirst();
        }
        //checks

        LinkedList<ParsedToken> operands = groupByMembership(line);
        operands = processStructures(operands);
        ParsedToken result = null;
        operands = divideByUnaryOperands(operands);
        if (operands.size() == 1)
            return operands.get(0);
        result = divideByBinaryOperands(operands, SECOND_PRIORITY);
        if (result != null) return result;
        result = divideByBinaryOperands(operands, FIRST_PRIORITY);
        if (result != null) return result;
        result = divideByBinaryOperands(operands, COMPARISON_OPERATIONS);
        if (result != null) return result;
        return operands.get(0);
    }

    public LinkedList<ParsedToken> groupByMembership(LinkedList<ParsedToken> operands) throws ParsingException {
        LinkedList<ParsedToken> result = new LinkedList<ParsedToken>();
        boolean found = true;
        while (found) {
            found = false;
            for (int i = 0; i < operands.size(); ++i) {
                if (!found && i != operands.size() - 1 && operands.get(i + 1).getType() != TokenType.STRING && operands.get(i + 1).getValue().equals(".")) {
                    result.add(new ParsedBinaryExpression(operands.get(i + 1).getToken(), operands.get(i), operands.get(i + 2)));
                    i = i + 2;
                    found = true;
                } else {
                    result.add(operands.get(i));
                }
            }
            operands = result;
            result = new LinkedList<ParsedToken>();
        }
        return operands;
    }

    public LinkedList<ParsedAbstractStatement> processBlocks(LinkedList<ParsedAbstractStatement> tokens){
        int lowerIndent = 0;
        for (ParsedAbstractStatement curLine: tokens){
            if (curLine.getIndentationLevel() > lowerIndent)
                lowerIndent = curLine.getIndentationLevel();
        }
        ParsedBlock block = null;
        while (lowerIndent != 0) {
            ParsedStatementWithBlock head = null;
            Iterator iter = tokens.iterator();
            while (iter.hasNext()) {
                ParsedAbstractStatement curLine = (ParsedAbstractStatement) iter.next();
                if (curLine.getType() == TokenType.BLOCKWORD && curLine.getIndentationLevel() == lowerIndent - 1) {
                    if (head == null) {
                        head = (ParsedStatementWithBlock)curLine;
                        block = new ParsedBlock(curLine.getToken());
                    }
                    else {
                        head.setToDo(block);
                        head = (ParsedStatementWithBlock)curLine;
                        block = new ParsedBlock(curLine.getToken());
                    }
                }
                else {
                    if (head != null) {
                        if (curLine.getIndentationLevel() == lowerIndent) {
                            block.addStatement(curLine);
                            iter.remove();
                        }
                        else{
                            head.setToDo(block);
                            head = null;
                            block = null;
                        }
                    }
                }
            }

            if (block != null) {
                head.setToDo(block);
                head = null;
                block = null;
            }

            //now finding if elif else
            iter = tokens.iterator();
            ParsedConditionalStatement head1 = null;
            while (iter.hasNext()) {
                ParsedAbstractStatement curLine = (ParsedAbstractStatement) iter.next();
                if (curLine.getType() == TokenType.BLOCKWORD && curLine.getIndentationLevel() == lowerIndent - 1) {
                    if (curLine.getValue().equals("if") || curLine.getValue().equals("while"))
                        head1 = (ParsedConditionalStatement) curLine;
                    if (curLine.getValue().equals("elif")) {
                        iter.remove();
                        ((ParsedConditionalStatement)head1).append((ParsedConditionalStatement)curLine);
                    }
                    if (curLine.getValue().equals("else")) {
                        iter.remove();
                        ((ParsedConditionalStatement)head1).append((ParsedConditionalStatement)curLine);
                        head1 = null;
                    }

                }

            }
            lowerIndent--;
        }
        return tokens;
    }
}
