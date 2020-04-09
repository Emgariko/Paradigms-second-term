package expression;

public interface OperationExecutor<T> {
    boolean overflowCheckerMode = false;
    T add(T c, T d);
    T mul(T c, T d);
    T sub(T c, T d);
    T div(T c, T d);
    T neg(T c);
    T parseConst(String s);
    T min(T c, T d);

    T max(T c, T d);

    T count(T evaluate);
}
