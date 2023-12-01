package Executing.ExecutionTokens.Builtin.Types;

import Executing.Executor;
import Lexing.Token;

import java.util.LinkedList;

public class ErrorType extends ObjectType {
    static ClassType type;
    public static void createType()
    {
        type = new ClassType();
        type.setMember("__name__", new StringType("error"));
        type.setMember("__str__", new FunctionType("__str__", new SourceFunction(){
            public ObjectType execute(LinkedList<ObjectType> args){
                ErrorType error = ((ErrorType)(args.get(0)));
                return new StringType(error.toString());
            }
        }
        ));

        Executor.setVariable("error", type);
    }
    public ErrorType() {
        this.setMember("__class__", type);
        this.setMember("message", new StringType("error"));
    }

    public ErrorType(String message){
        this.setMember("__class__", type);
        this.setMember("message", new StringType(message));
    }

    public ErrorType(StringType message){
        this.setMember("__class__", type);
        this.setMember("message", message);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.getMember("message").toString());
        if (this.contains("line")){
            sb.append(": ");
            sb.append((this.getMember("line").toString()));
            if (this.contains("position")){
                sb.append(" ");
                sb.append((this.getMember("position").toString()));
            }
        }
        return sb.toString();
    }

    public void setMessage(String message){
        this.setMember("message", new StringType(message));
    }

    public void setLine(int line){
        this.setMember("line", new IntType(line));
    }

    public void setPosition(int position){
        this.setMember("position", new IntType(position));
    }

    public ObjectType execute() {
        return this;
    }
}
