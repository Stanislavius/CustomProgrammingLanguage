package Executing.ExecutionTokens.Builtin.Types;

import Lexing.Token;

public class ErrorType extends ObjectType {
    static ClassType type;
    public static void createType()
    {
        type = new ClassType();
        type.setMember("__name__", new StringType("error"));
    }
    public ErrorType() {
        this.setMember("__class__", type);
    }

    public ObjectType execute() {
        return this;
    }
}
