package Executing;

public class ReturnValue<T> {
    T value;
    ReturnType type;

    public ReturnValue(T value, ReturnType type) {
        this.value = value;
        this.type = type;
    }

    public T execute() {
        return value;
    }

    public ReturnType getType() {
        return type;
    }

    public T getValue() {
        return value;
    }

    public String toString() {
        return value.toString();
    }
}
