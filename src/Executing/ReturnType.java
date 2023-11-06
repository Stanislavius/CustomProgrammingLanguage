package Executing;

public class ReturnType<T> {
    T value;
    ReturnTypes type;

    public ReturnType(T value, ReturnTypes type) {
        this.value = value;
        this.type = type;
    }

    public T execute() {
        return value;
    }

    public ReturnTypes getType() {
        return type;
    }

    public T getValue() {
        return value;
    }

    public String toString() {
        return value.toString();
    }
}
