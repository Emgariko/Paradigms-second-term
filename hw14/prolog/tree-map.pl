% delay & review

map_build([], null) :- true, !.
map_build([(K, V) | T], TreeMap) :- map_build(T, Res), map_put(Res, K, V, TreeMap), !.

map_put(TT, Key, Value, Result) :- not(map_get(TT, Key, Val)), !, rand_int(10000000, Y), putt(TT, Key, Y, Value, Result).
map_put(TT, Key, Value, Result) :- 
		 map_get(TT, Key, Val), !, map_remove(TT, Key, R1), rand_int(10000000, Y), putt(R1, Key, Y, Value, Result).

map_ceilingKey(Map, Key, CeilingKey) :- split(Map, R1, R2, Key), find_left(R2, CeilingKey).
find_left(T, RES) :- T = node(X, XX, Y, L, R), not(L = null), find_left(L, RES). 
find_left(T, RES) :- T = node(RES, XX, Y, L, R), L = null, !.

putt(T, X, Y, Value, R) :- split(T, RES1, RES2, X), NODE = node(X, Value, Y, null, null),
		merge(RES1, NODE, R1), merge(R1, RES2, R).

map_get(T, Key, Value) :- T = node(X, XX, Y, L, R), X < Key, !, map_get(R, Key, Value).
map_get(T, Key, Value) :- T = node(X, XX, Y, L, R), X = Key, XX = Value, !.
map_get(T, Key, Value) :- T = node(X, XX, Y, L, R), X > Key, !, map_get(L, Key, Value).

%map_remove(T, KEY, R) :- split(T, R1, R2, KEY), KEY1 is KEY + 1, split(R2, R3, R4, KEY1), merge(R1, R4, R).
map_remove(T, K, Res) :- map_get(T, K, V), !, del(T, K, Res).
map_remove(T, K, Res) :- not(map_get(T, K, V)), Res = T, !.
del(T, Key, Res) :- T = node(X, XX, Y, L, R), X < Key, !, del(R, Key, Res1), Res = node(X, XX, Y, L, Res1).
del(T, Key, Res) :- T = node(X, XX, Y, L, R), X = Key, !, merge(L, R, Res).
%del(T, Key, Res) :- T = node(X, XX, Y, L, R), not(map_get(T, 
del(T, Key, Res) :- T = node(X, XX, Y, L, R), X > Key, !, del(L, Key, Res1), Res = node(X, XX, Y, Res1, R).

split(null, null, null, K) :- !.
split(T, RES1, RES2, K) :- T = node(X, XX, Y, L, R), X < K, !, split(R, RES3, RES4, K), RES1 = node(X, XX, Y, L, RES3), RES2 = RES4.
split(T, RES1, RES2, K) :- T = node(X, XX, Y, L, R), X >= K, !, split(L, RES3, RES4, K), RES1 = RES3, RES2 = node(X, XX, Y, RES4, R).

merge(null, T2, T2) :- !.
merge(T1, null, T1) :- !.
merge(T1, T2, RES) :- T1 = node(X1, XX1, Y1, L1, R1), T2 = node(X2, XX2, Y2, L2, R2), Y1 > Y2, !, merge(R1, T2, R3), RES = node(X1, XX1, Y1, L1, R3).
merge(T1, T2, RES) :- T1 = node(X1, XX1, Y1, L1, R1), T2 = node(X2, XX2, Y2, L2, R2), Y1 =< Y2, !, merge(T1, L2, R3), RES = node(X2, XX2, Y2, R3, R2). 
