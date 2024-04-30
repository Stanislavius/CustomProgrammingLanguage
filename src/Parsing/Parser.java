package Parsing;

import Lexing.Token;
import Lexing.TokenType;
import Parsing.ParsedTokens.*;
import Parsing.ParsingExceptions.*;

import java.util.*;
import java.util.logging.Logger;

import static Parsing.ParsingLogger.createParsingLogger;
import static java.lang.Math.abs;

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
    LinkedList<AbstractStatementPT> parsedLines = new LinkedList<AbstractStatementPT>();

    public boolean isWithoutError(){
        return this.exceptions.isEmpty();
    }

    public LinkedList<ParsingException> getExceptions(){return this.exceptions;}

    public void clear(){
        parsedLines.clear();
        exceptions.clear();
    }

    public LinkedList<AbstractStatementPT> getParsedLines(){
        return parsedLines;
    }

    public LinkedList<AbstractStatementPT> parse (LinkedList<Token> tokens){
        LinkedList<LinkedList<Token>> lines;
        try {
            lines = divideByLines(tokens);
        }
        catch (ParsingException e){
            exceptions.add(e);
            return null;
        }
        try {
                logger.fine("Start parsing");
            for(int i = 0; i < lines.size(); ++i){
                if (!lines.get(i).isEmpty()) {
                    logger.fine("Start parsing " + i + " line \r\n" + ParsingLogger.tokensToString(lines.get(i)));
                    parsedLines.add(parseLine(lines.get(i)));
                    logger.info(i + " line is parsed \r\n" + parsedLines.getLast().toString());
                }
                else
                    logger.info("line " + i + " is empty, pass");

            }
            parsedLines = processBlocks(parsedLines);
        }
        catch (ParsingException e){
            exceptions.add(e);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Parsing is finished, parsed program as follows: \n");
        for(int i = 0; i < parsedLines.size(); ++i) {
            sb.append(parsedLines.get(i).toString());
        }
        logger.finest(sb.toString());
        return parsedLines;
    }

    public LinkedList<LinkedList<Token>> divideByLines(LinkedList<Token> tokens) throws ParsingException {
        LinkedList<LinkedList<Token>> lines = new LinkedList<LinkedList<Token>>();
        LinkedList<Token> line = new LinkedList<Token>();
        for (Token token : tokens) {
            if (token.getType().equals(TokenType.NEWLINE)) {
                boolean hasBody = false;
                for (int i = 0; i< line.size(); ++i){
                    if (line.get(i).getType() != TokenType.INDENTATION && line.get(i).getType() != TokenType.NEWLINE) {
                        hasBody = true;
                        break;
                    }
                }
                if (hasBody == false && line.size() != 0)
                    throw new EmptyLineException(line.getFirst());
                if (line.size() != 0) {
                    lines.add(line);
                    line = new LinkedList<Token>();
                }
            } else
                line.add(token);
        }
        return lines;
    }

    public AbstractStatementPT parseLine(LinkedList<Token> line) throws ParsingException{ //call it to parse whole line
        int indent = 0;
        AbstractStatementPT statement;
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
                    ParsedToken left = parseExpressionTokens(new LinkedList<Token>(line.subList(0, i)));
                    if (left.getParsedType() == ParsedTokenType.VALUE){
                        if (!(left.getClass() == VariablePT.class)){
                            throw new CannotAssignToLiteralException(left.getToken());
                        }
                    }
                    return new AssigmentStatementPT(line.get(i),
                            indent,
                            left,
                            parseExpressionTokens(new LinkedList<Token>(line.subList(i+1, line.size()))));
                }
            return new StatementPT(indent, parseExpressionTokens(line));
        }
    }

    public AbstractStatementPT parseLineWithBlockword(int indent, LinkedList<Token> line) throws ParsingException {
        for(int i = 0; i < line.size(); ++i){
            if (line.get(i).getType() == TokenType.ASSIGNMENT)
                throw new UsingKeywordAsVariableException(line.get(0));
        }

        if (line.get(0).getValue().equals("if")){
            if (line.size() <= 1)
                throw new NoConditionException(line.get(0));
            return new ConditionalStatementPT(line.get(0),
                    indent,
                    parseExpressionTokens(new LinkedList<Token>(line.subList(1, line.size()))));
        }
        if (line.get(0).getValue().equals("elif")){
            if (line.size() <= 1)
                throw new NoConditionException(line.get(0));
            return new ConditionalStatementPT(line.get(0),
                    indent,
                    parseExpressionTokens(new LinkedList<Token>(line.subList(1, line.size()))));
        }

        if (line.get(0).getValue().equals("try")){
            return new TryStatementPT(line.get(0),
                    indent);
        }

        if (line.get(0).getValue().equals("except")){
            return new ExceptStatementPT(line.get(0),
                    indent, parseExceptArgs(line));
        }

        if (line.get(0).getValue().equals("finally")){
            return new FinallyStatementPT(line.get(0),
                    indent);
        }

        if (line.get(0).getValue().equals("else")){
            return new ConditionalStatementPT(line.get(0),
                    indent);
        }
        if (line.get(0).getValue().equals("while")){
            if (line.size() <= 1)
                throw new NoConditionException(line.get(0));
            return new ConditionalStatementPT(line.get(0),
                    indent,
                    parseExpressionTokens(new LinkedList<Token>(line.subList(1, line.size()))));
        }
        if (line.get(0).getValue().equals("def")){
            if (line.size() < 4)
                throw new NoFunctionSignatureException(line.get(0));
            return new FunctionDefinitionPT(line.get(0),
                    indent, line.get(1),
                    parseArgsTokens(new LinkedList<Token>(line.subList(2, line.size()))));
        }

        if (line.get(0).getValue().equals("class")){
            if (line.size() <= 1)
                throw new NoClassNameException(line.get(0));
            return new ClassDefinitionPT(line.get(0),
                    indent, line.get(1));
        }

        line.removeFirst();
        // rest is processed as expression if needed
        return null;
    }

    private LinkedList<VariablePT> parseExceptArgs(LinkedList<Token> line) {
        return null; // TODO
    }

    public LinkedList<VariablePT> parseArgsTokens(LinkedList<Token> line) throws ParsingException {
        LinkedList<VariablePT> args = new LinkedList<VariablePT>();
        for (int i = 1; i < line.size()-1; ++i) {
            if (line.get(i).getType() == TokenType.PARENTHESIS) {

            }
            if (line.get(i).getType() == TokenType.SEPARATOR) {

            } else {
                args.add(new VariablePT(line.get(i)));
            }
        }
        // can process exceptions here
        return args;
    }

    final static LinkedList<String> blockWordsWithoutArgs = new LinkedList<String>(Arrays.asList("else", "try", "finally", "except"));
    final static LinkedList<String> blockWordsWithArgs = new LinkedList<String>(Arrays.asList("if", "elif", "while", "def", "class", "except"));

    public ParsedToken parseExpressionTokens(LinkedList<Token> line) throws ParsingException {
        for(int i = 0; i < line.size(); ++i){
            if (line.get(i).getType() == TokenType.BLOCKWORD)
                throw new UsingKeywordAsVariableException(line.get(0));
        }
        LinkedList<ParsedToken> parsedLine = new LinkedList<ParsedToken>();
        for(int i = 0; i < line.size(); ++i){
            parsedLine.add(new ParsedToken(line.get(i)));
        }
        for(int i = 0; i < parsedLine.size(); ++i){
            if (parsedLine.get(i).getType() == TokenType.VARIABLE){
                if (blockWordsWithArgs.contains(parsedLine.get(i).getValue()) || blockWordsWithoutArgs.contains((parsedLine.get(i).getValue())))
                    throw new UsingKeywordAsVariableException(parsedLine.get(i).getToken());
                parsedLine.set(i, new VariablePT(parsedLine.get(i).getToken()));
            }
            if (parsedLine.get(i).getType() == TokenType.STRING){
                parsedLine.set(i, new StringPT(parsedLine.get(i).getToken()));
            }
            if (parsedLine.get(i).getType() == TokenType.INT){
                parsedLine.set(i, new IntPT(parsedLine.get(i).getToken()));
            }
            if (parsedLine.get(i).getType() == TokenType.FLOAT){
                parsedLine.set(i, new FloatPT(parsedLine.get(i).getToken()));
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
                    return new BinaryPT(operands.get(i).getToken(), parseExpression(left), parseExpression(right));
                }
                else{
                    return new UnaryPT(operands.get(i).getToken(), parseExpression(right));
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
                        op = new UnaryPT(q.get(j).getToken(), op);
                        j--;
                    }
                    newOperands.add(q.get(0));
                    newOperands.add(op);
                    q.clear();
                }
                else {
                    if (q.isEmpty() == false) {
                        if (operands.get(i).getParsedType() != ParsedTokenType.UNKNOWN_OPERATION) {
                            if (newOperands.size() == 0 || newOperands.getLast().getParsedType() == ParsedTokenType.UNKNOWN_OPERATION)
                                newOperands.add(new UnaryPT(q.get(0).getToken(), operands.get(i)));
                            else{
                                newOperands.add(q.get(0));
                                newOperands.add(operands.get(i));
                            }
                        } else {
                            newOperands.add(q.get(0));
                        }
                    }
                    else {
                        newOperands.add(operands.get(i));
                    }
                    q.clear();
                }
            }

        }
        return newOperands;
    }

    public FunctionCallPT parseFunctionCall(Token head, LinkedList<ParsedToken> line) throws ParsingException {
        LinkedList<ParsedToken> values = new LinkedList<ParsedToken>();
        LinkedList<LinkedList<ParsedToken>> args = separateArgs(line);
        for(int i = 0; i < args.size(); ++i){
            values.add(parseExpression(args.get(i)));
        }
        return new FunctionCallPT(head, values);
    }

    public ListPT parseList(Token head, LinkedList<ParsedToken> line) throws ParsingException {
        LinkedList<ParsedToken> values = new LinkedList<ParsedToken>();
        LinkedList<LinkedList<ParsedToken>> args = separateArgs(line);
        for(int i = 0; i < args.size(); ++i){
            values.add(parseExpression(args.get(i)));
        }
        return new ListPT(head, values);
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

    public DictPT parseDict(Token head, LinkedList<ParsedToken> line) throws ParsingException {
        LinkedList<ParsedToken> keys = new LinkedList<ParsedToken>();
        LinkedList<ParsedToken> values = new LinkedList<ParsedToken>();
        LinkedList<LinkedList<ParsedToken>> args = separateArgs(line);
        for(int i = 0; i < args.size(); ++i){
            LinkedList<ParsedToken> curPair = parseColon(args.get(i));
            keys.add(curPair.get(0));
            values.add(curPair.get(1));
        }

        return new DictPT(head, keys, values);
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
                        result = new BinaryPT(processedStructure, left, right);
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
        if (balance != 0) {
            if (processedStructure.getValue().equals("("))
                throw new ParenthesesException(processedStructure);
            if (processedStructure.getValue().equals("["))
                throw new BracketIsNotClosedException(processedStructure);
        }

        return operands;
    }

    private ParsedToken parseListCall(LinkedList<ParsedToken> operand) throws ParsingException {
        if (operand.size() > 1)
            //throw new IndexException(operand.get(0).getToken());
            return parseExpression(operand);
        else
            return operand.get(0);
    }

    private ParsedToken parseDictCall(LinkedList<ParsedToken> operand) {
        return null; //TODO
    }

    public ParsedToken parseExpression(LinkedList<ParsedToken> line) throws ParsingException {
        if (line.isEmpty()) {
            return null;
        }
        if (line.size() == 1){
            if (line.get(0).getClass() == ParsedToken.class
                    && line.get(0).getType() == TokenType.OPERATION){
                throw new NoOperandException(line.get(0).getToken());
            }
        }
        else{
            for(int i = 0; i < line.size() - 1; ++i){
                if (line.get(i).getType() == TokenType.OPERATION && line.get(i+1).getType() == TokenType.OPERATION){
                    if (line.get(i).getClass() == ParsedToken.class && line.get(i+1).getClass() == ParsedToken.class)
                        if (!line.get(i+1).getValue().equals("+") && !line.get(i+1).getValue().equals("-"))
                            throw new NoOperandException(line.get(i).getToken());
                }
                if(line.get(i).getParsedType() == ParsedTokenType.VALUE)
                    if(line.get(i+1).getParsedType() == ParsedTokenType.VALUE)
                        throw new NoOperationException((line.get(i+1).getToken()));
            }
            ParsedToken last = line.getLast();
            if (last.getClass() == ParsedToken.class && last.getType() == TokenType.OPERATION){
                throw new NoOperandException(last.getToken());
            }
        }
        /* if (line.size() != 1){
            for(int i = 0; i < line.size()-1; ++i){

                if(line.get(i).getParsedType() == ParsedTokenType.VALUE ||
                        line.get(i).getParsedType() == ParsedTokenType.BINARY_OPERATION
                        || line.get(i).getParsedType() == ParsedTokenType.UNARY_OPERATION)
                    if(line.get(i+1).getParsedType() == ParsedTokenType.VALUE ||
                            line.get(i+1).getParsedType() == ParsedTokenType.BINARY_OPERATION
                            || line.get(i+1).getParsedType() == ParsedTokenType.UNARY_OPERATION){
                        throw new NoOperationException((line.get(i+1).getToken()));
                    }
                 maybe that's can be done, but for now i will go with another:
            }
        } */

        if (line.size() == 1) {
            return line.getFirst();
        }


        //checks
        LinkedList<ParsedToken> operands = processStructures(line);
        operands = groupByMembership(operands);
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
                if (!found && i != operands.size() - 1 && operands.get(i + 1).getType() != TokenType.STRING &&
                        operands.get(i + 1).getValue().equals(".") &&
                        operands.get(i + 1).getParsedType() == ParsedTokenType.UNKNOWN_OPERATION) {
                    result.add(new BinaryPT(operands.get(i + 1).getToken(), operands.get(i), operands.get(i + 2)));
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

    public LinkedList<AbstractStatementPT> processBlocks(LinkedList<AbstractStatementPT> tokens) throws ParsingException {
        int lowerIndent = 0;
        int lowerIndentExpected = 0;
        for (AbstractStatementPT curLine: tokens){
            if (curLine.getIndentationLevel() > lowerIndent)
                lowerIndent = curLine.getIndentationLevel();
        }

        int maxIndent = 0;
        for(int i = 0; i < tokens.size(); ++i){
            if (tokens.get(i).getIndentationLevel() > maxIndent)
                throw new UnexpectedIndentException(tokens.get(i).getLine(), i, 0);
            if (tokens.get(i).getType() == TokenType.BLOCKWORD)
                maxIndent += 1;
            else if (tokens.get(i).getIndentationLevel() < maxIndent)
                maxIndent = tokens.get(i).getIndentationLevel();
        }

        for(int i = 0; i < tokens.size(); ++i){
            if (tokens.get(i).getType() == TokenType.BLOCKWORD){
                if (i == tokens.size() - 1)
                    throw new NoBodyException(tokens.get(i).getLine(), tokens.get(i).getLineNum(), tokens.get(i).getLineLength());
                if (tokens.get(i).getIndentationLevel() + 1 != tokens.get(i+1).getIndentationLevel())
                    throw new NoBodyException(tokens.get(i+1).getLine(), tokens.get(i+1).getLineNum(), 1);
            }
        }

        BlockPT block = null;
        while (lowerIndent != 0) {
            StatementWithBlockPT head = null;
            Iterator iter = tokens.iterator();
            while (iter.hasNext()) {
                AbstractStatementPT curLine = (AbstractStatementPT) iter.next();
                if (curLine.getType() == TokenType.BLOCKWORD && curLine.getIndentationLevel() == lowerIndent - 1) {
                    if (head == null) {
                        head = (StatementWithBlockPT)curLine;
                        block = new BlockPT(curLine.getToken());
                    }
                    else {
                        head.setToDo(block);
                        head = (StatementWithBlockPT)curLine;
                        block = new BlockPT(curLine.getToken());
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
            ConditionalStatementPT head1 = null;
            while (iter.hasNext()) {
                AbstractStatementPT curLine = (AbstractStatementPT) iter.next();
                if (curLine.getType() == TokenType.BLOCKWORD && curLine.getIndentationLevel() == lowerIndent - 1) {
                    if (curLine.getValue().equals("if") || curLine.getValue().equals("while"))
                        head1 = (ConditionalStatementPT) curLine;
                    if (curLine.getValue().equals("elif")) {
                        iter.remove();
                        if (head1 == null)
                            throw new UnexpectedBlockWordException(curLine.getToken());
                        ((ConditionalStatementPT)head1).append((ConditionalStatementPT)curLine);
                    }
                    if (curLine.getValue().equals("else")) {
                        iter.remove();
                        if (head1 == null)
                            throw new UnexpectedBlockWordException(curLine.getToken());
                        ((ConditionalStatementPT)head1).append((ConditionalStatementPT)curLine);
                        head1 = null;
                    }
                    //TODO IF BLOCKWORDS ORDER IS NOT PRESERVED, THROW EXCEPTION
                }

            }

            iter = tokens.iterator();
            TryStatementPT headTry = null;
            while (iter.hasNext()) {
                AbstractStatementPT curLine = (AbstractStatementPT) iter.next();
                if (curLine.getType() == TokenType.BLOCKWORD && curLine.getIndentationLevel() == lowerIndent - 1) {
                    if (curLine.getValue().equals("try"))
                        headTry = (TryStatementPT) curLine;
                    if (curLine.getValue().equals("except")) {
                        iter.remove();
                        assert curLine instanceof ExceptStatementPT;
                        if (head1 == null)
                            throw new UnexpectedBlockWordException(curLine.getToken());
                        headTry.addExcept((ExceptStatementPT) curLine);
                    }
                    if (curLine.getValue().equals("finally")) {
                        iter.remove();
                        assert curLine instanceof FinallyStatementPT;
                        if (head1 == null)
                            throw new UnexpectedBlockWordException(curLine.getToken());
                        headTry.setFinallyStatement((FinallyStatementPT) curLine);
                        headTry = null;
                    }
                    //TODO IF BLOCKWORDS ORDER IS NOT PRESERVED, THROW EXCEPTION
                }

            }


            lowerIndent--;
        }
        return tokens;
    }

}
