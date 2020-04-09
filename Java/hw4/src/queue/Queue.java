package queue;

import java.util.function.Function;
import java.util.function.Predicate;

// Inv:
// size >= 0, forall i = 0, ..., size - 1 q[i] != null
// FIFO(first in(in tale), first out(from head)),
// q[0] - queue head, q[sz - 1] - queue tale
public interface Queue {
    // Pre: element != null
    // Post: q' = [e_0, ..., e_(size - 1), element], size' = size + 1
    public void enqueue(Object element);

    // Pre: size > 0
    // Post: R = q[0], immutable
    public Object element();

    // Pre: size > 0
    // Post: R = q[0], size' = size - 1, q' = [e_1, ..., e_(size - 1)]
    public Object dequeue();

    // Pre: true
    // Post: R = sz, immutable
    public int size();

    // Pre: true
    // Post: R = bool(sz == 0), immutable
    public boolean isEmpty();

    // Pre: true
    // Post: q' = [], sz' = 0
    public void clear();

    // Pre : a != null
    // Post : R = [e_i0, ..., e_i(k - 1)], \forall t \in [0..k - 1] \exist j \in [0..sz - 1]: i_t = j,
    // \forall j \ in [0..sz - 1]: a(e_j) == true \exist t \in [0..k - 1]: j = i_t
    // immutable
    Queue filter(Predicate a);

    // Pre : f != null, \forall i f(e_i) != null
    // Post : R = [f(e_0), ..., f(e_(sz - 1))]
    // immutable
    Queue map(Function f);

}
