package Executing.ExecutionTokens.Builtin.Types;

public class VoidType extends ObjectType {
    static ClassType type;
    static VoidType voidObject;
    public static void createType()
    {
        type = new ClassType();
        type.setMember("__name__", new StringType("void"));
        voidObject = new VoidType();
    }
    public VoidType() {
        this.setMember("__class__", type);
    }

    public String toString(){
        return "void";
    }

}
