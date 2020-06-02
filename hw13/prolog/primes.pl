init(MAX_N) :- sift(2, MAX_N).

divides(X, Y) :- 0 is X mod Y.
divides_check(N, X) :- X * X < N + 1, divides(N, X), !.
divides_check(N, X) :- X * X < N + 1, X1 is X + 1, divides_check(N, X1), !.

summator(A, B, R) :- number(R), number(A), B is R - A.
summator(A, B, R) :- number(R), number(B), A is R - B.
summator(A, B, R) :- number(A), number(B), R is A + B.

concat([], B, B).
concat([H | T], B, [H | R]) :- concat(T, B, R).

sift_loop(L, R, K) :- R1 is R + 1, L < R1, LK is L + K, assert(composite(L)), sift_loop(LK, R, K), !.
sift_table(L, R) :- LL is L * L, R1 is R + 1, LL < R1, not(composite(L)), !, not(sift_loop(LL, R, L)), L1 is L + 1, sift_table(L1, R), !.
sift_table(L, R) :- LL is L * L, R1 is R + 1, LL < R1, composite(L), !, L1 is L + 1, sift_table(L1, R), !.

sift(L, R) :- sift_table(L, R).
composite(1).
prime(X) :- not(composite(X)).

divisor_loop(L, N, R) :- N1 is N + 1, L < N1, prime(L), divides(N, L), R is L, !. 
divisor_loop(L, N, R) :- N1 is N + 1, L < N1, prime(L), not(divides(N, L)), L1 is L + 1, divisor_loop(L1, N, R), !.
divisor_loop(L, N, R) :- N1 is N + 1, L < N1, composite(L), L1 is L + 1, divisor_loop(L1, N, R), !.
divisor(N, R) :- divisor_loop(2, N, RES), R is RES, !.
divisor(N, R) :- prime(N), R is N, !.

prime_divisors(1, []) :- true, !.
prime_divisors(N, [H]) :- number(N), prime(N), H is N, !.
prime_divisors(N, [H | T]) :- number(N), divisor(N, R), H is R, NDR is N / R, prime_divisors(NDR, T), !.

first([H | T], R) :- R is H.
sorted([T]) :- true, !.
sorted([H | T]) :- sorted(T), first(T, R), H =< R.
prime_divisors(N, [H]) :- number(H), N is H, !.
prime_divisors(N, [H | T]) :- concat([H], T, LIST), sorted(LIST), number(H), prime_divisors(R1, T), N is R1 * H, !.

palindrome(R) :- reverse(R, R1), R1 = R.
tolist(N, K, [H]) :- N < K, H is N, !.
tolist(N, K, [H | T]) :- NMK is N mod K, H is NMK, NDK is div(N, K), tolist(NDK, K, T), !.
palindrome_number(N, K) :- tolist(N, K, R), palindrome(R).

prime_palindrome(N, K) :- prime(N), palindrome_number(N, K), !.
