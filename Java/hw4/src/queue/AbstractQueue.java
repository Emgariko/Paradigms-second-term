package queue;


import java.util.function.Function;
import java.util.function.Predicate;

public abstract class AbstractQueue implements Queue {
    protected int sz;

    @Override
    public int size() {
        return sz;
    }

    public abstract AbstractQueue getEmptyQueue();

    @Override
    public AbstractQueue filter(Predicate a) {
        AbstractQueue res = getEmptyQueue();
        int n = this.size();
        for (int i = 0; i < n; i++) {
            Object cur = this.dequeue();
            if (a.test(cur)) {
                res.enqueue(cur);
            }
            this.enqueue(cur);
        }
        return res;
    }

    @Override
    public AbstractQueue map(Function f) {
        AbstractQueue res = getEmptyQueue();
        int n = this.size();
        for (int i = 0; i < n; i++) {
            Object cur = this.dequeue();
            res.enqueue(f.apply(cur));
            this.enqueue(cur);
        }
        return res;
    }

    @Override
    public boolean isEmpty() {
        return sz == 0;
    }
}
