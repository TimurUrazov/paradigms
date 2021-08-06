init(MAX_N) :- N is MAX_N + 1, linear_eratosthenes(2, N).

primes_size(0).
not_primes(1).
inner_loop(I, J, N) :-
    primes_size(R),
    J < R + 1,
    primes(J, R1),
    MUL is R1 * I,
    MUL < N + 1,
    lp(I, R2),
    R1 < R2 + 1,
    assert(not_primes(MUL)),
    assert(lp(MUL, R1)),
    I1 is J + 1,
    inner_loop(I, I1, N).

linear_eratosthenes(I, N) :-
    not_primes(I),
    inner_loop(I, 1, N).

linear_eratosthenes(I, N) :-
    not not_primes(I),
    assert(lp(I, I)),
    primes_size(R),
    R1 is R + 1,
    retract(primes_size(R)),
    assert(primes_size(R1)),
    assert(primes(R1, I)),
    inner_loop(I, 1, N).

linear_eratosthenes(I, N) :-
    I < N,
    I1 is I + 1,
    linear_eratosthenes(I1, N).

composite(X) :- not_primes(X).
prime(X) :- not composite(X).

is_sorted_primes_list([]) :- !.
is_sorted_primes_list([H]) :- prime(H), !.
is_sorted_primes_list([H, H1 | T]) :-
    H < H1 + 1,
    prime(H),
    is_sorted_primes_list([H1 | T]).

find_by_primes(1, []) :- !.
find_by_primes(N, [H]) :- N is H, !.
find_by_primes(N, [H | T]) :-
    find_by_primes(N1, T),
    N is N1 * H.

prime_divisors(1, []).
prime_divisors(N, [N]) :- prime(N), !.
prime_divisors(N, [H | T]) :-
    number(N),
    !,
    N > 1,
    lp(N, H),
    N1 is N / H,
    prime_divisors(N1, T).

prime_divisors(N, [H | T]) :-
    is_sorted_primes_list([H | T]),
    find_by_primes(N, [H | T]).

prime_index(P, N) :- primes(N, P).