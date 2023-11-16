package Parsing.ParsedTokens;

import Lexing.Token;

public class ParsedMembership extends ParsedToken{
    ParsedToken object;
    ParsedToken member;


    public ParsedMembership(Token t, ParsedToken object, ParsedToken member) {
        super(t);
        this.object = object;
        this.member = member;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(object.toString());
        sb.append(token.getValue());
        sb.append(member.toString());
        return sb.toString();
    }

    public ParsedTokenType getParsedType(){
        if (member.getParsedType() == ParsedTokenType.FUNCTION_CALL)
            return ParsedTokenType.MEMBERSHIP_FUNCTION_CALL;
        if (member.getParsedType() == ParsedTokenType.VARIABLE)
            return ParsedTokenType.MEMBERSHIP_VARIABLE;
        return null;
    }

    public ParsedToken getObject() {
        return object;
    }

    public ParsedToken getMember() {
        return member;
    }

}
