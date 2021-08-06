node(L, R, K, V, H, C, node(L, R, K, V, H, C)).

rotate_impl(Cnt, Height, T, H, C) :-
    get_height(T, H1),
    max(H1, Height, H2),
    get_cnt(T, C1),
    H is H2 + 1,
    C is Cnt + C1 + 1.

left_rotate(node(T1, node(T2, T3, K1, V1, _, _), K, V, _, _), R) :-
    update_height(T1, T2, Height),
    update_cnt(T1, T2, Cnt),
    node(node(T1, T2, K, V, Height, Cnt), T3, K1, V1, H5, C5, R),
    rotate_impl(Cnt, Height, T3, H5, C5).

right_rotate(node(node(T1, T2, K1, V1, _, _), T3, K, V, _, _), R) :-
    update_height(T2, T3, Height),
    update_cnt(T2, T3, Cnt),
    node(T1, node(T2, T3, K, V, Height, Cnt), K1, V1, H5, C5, R),
    rotate_impl(Cnt, Height, T1, H5, C5).

left_right_rotate(node(T1, T2, K, V, H, C), R) :-
    left_rotate(T1, R1),
    right_rotate(node(R1, T2, K, V, H, C), R).

right_left_rotate(node(T1, T2, K, V, H, C), R) :-
    right_rotate(T2, R1),
    left_rotate(node(T1, R1, K, V, H, C), R).

get_height(node(_, _, _, _, H, _), H) :- !.
get_height(null, 0).

get_cnt(node(_, _, _, _, _, C), C) :- !.
get_cnt(null, 0).

get_balance(node(L, R, _, _, _, _), R1) :-
    get_height(L, R2),
    get_height(R, R3),
    R1 is R2 - R3.

max(X, Y, X) :- X >= Y.
max(X, Y, Y) :- X < Y.

update_height(L, R, H1) :-
    get_height(L, R2),
    get_height(R, R3), !,
    max(R2, R3, R4),
    H1 is R4 + 1.

update_height(_, _, 0).

update_cnt(L, R, C1) :-
    get_cnt(L, R2),
    get_cnt(R, R3), !,
    C1 is R2 + R3 + 1.

update_cnt(_, _, 0).

get_key(node(_, _, K, _, _, _), K).
get_left(node(L, _, _, _, _, _), L).
get_right(node(_, R, _, _, _, _), R).
get_height(node(_, _, _, _, H, _), H).

rotate_by_balance(Root, RootLeft, _, Key, Balance, R1) :-
    Balance > 1,
    get_key(RootLeft, LeftKey),
    Key < LeftKey, !,
    right_rotate(Root, R1).

rotate_by_balance(Root, RootLeft, _, Key, Balance, R1) :-
    Balance > 1,
    get_key(RootLeft, LeftKey),
    Key > LeftKey, !,
    left_right_rotate(Root, R1).

rotate_by_balance(Root, _, RootRight, Key, Balance, R1) :-
    Balance < -1,
    get_key(RootRight, RightKey),
    Key > RightKey, !,
    left_rotate(Root, R1).

rotate_by_balance(Root, _, RootRight, Key, Balance, R1) :-
    Balance < -1,
    get_key(RootRight, RightKey),
    Key < RightKey, !,
    right_left_rotate(Root, R1).

rotate_by_balance(Root, _, _, _, _, Root).

map_put(null, KEY, VALUE, node(null, null, KEY, VALUE, 1, 1)) :- !.
map_put(node(L, R, KEY, _, H, C), KEY, V, node(L, R, KEY, V, H, C)) :- !.

map_put_balance(Root, KEY, R) :-
    get_balance(Root, Balance),
    get_left(Root, RootLeft),
    get_right(Root, RootRight),
    rotate_by_balance(Root, RootLeft, RootRight, KEY, Balance, R).

map_put(node(L, R, K, V, _, _), KEY, VALUE, R3) :-
    KEY < K,
    map_put(L, KEY, VALUE, L1),
    update_height(L1, R, H1),
    update_cnt(L1, R, C1),
    node(L1, R, K, V, H1, C1, R2),
    map_put_balance(R2, KEY, R3).

map_put(node(L, R, K, V, _, _), KEY, VALUE, R3) :-
    KEY > K,
    map_put(R, KEY, VALUE, RES),
    update_height(L, RES, H1),
    update_cnt(L, RES, C1),
    node(L, RES, K, V, H1, C1, R2),
    map_put_balance(R2, KEY, R3).

map_build([], null) :- !.
map_build([(K, V) | L], TreeMap) :-
    map_build(L, T),
    map_put(T, K, V, TreeMap).

map_get(node(_, _, K, V, _, _), K, V) :- !.
map_get(node(L, _, K, _, _, _), Key, Value) :-
    Key < K, !,
    map_get(L, Key, Value).

map_get(node(_, R, _, _, _, _), Key, Value) :-
    map_get(R, Key, Value).

count_children(null, 0) :- !.
count_children(_, 1).
count_children(L, R, C) :-
    count_children(L, L1),
    count_children(R, R1),
    C is L1 + R1.

find_min(node(null, _, K, V, _, _), K, V) :- !.
find_min(node(L, _, _, _, _, _), K, V) :-
    find_min(L, K, V).

rem_rotate(Root, RootBalance, Left, _, R1) :-
    RootBalance > 1,
    get_balance(Left, LeftBalance),
    LeftBalance >= 0, !,
    right_rotate(Root, R1).

rem_rotate(Root, RootBalance, Left, _, R1) :-
    RootBalance > 1,
    get_balance(Left, LeftBalance),
    LeftBalance < 0, !,
    left_right_rotate(Root, R1).

rem_rotate(Root, RootBalance, _, Right, R1) :-
    RootBalance < -1,
    get_balance(Right, RightBalance),
    0 >= RightBalance, !,
    left_rotate(Root, R1).

rem_rotate(Root, RootBalance, _, Right, R1) :-
    RootBalance < -1,
    get_balance(Right, RightBalance),
    RightBalance > 0, !,
    right_left_rotate(Root, R1).

rem_rotate(Root, _, _, _, Root).

remove_current(0, _, null).
remove_current(1, node(L, null, _, _, _, _), L).
remove_current(1, node(null, R, _, _, _, _), R).
remove_current(2, node(L, R, _, _, H, C), Result) :-
    find_min(R, NewK, NewV),
    map_remove(R, NewK, R1),
    node(L, R1, NewK, NewV, H, C, Result).

map_remove_impl(null, null) :- !.
map_remove_impl(node(Left, Right, Key, Value, _, _), Result) :-
    update_height(Left, Right, H1),
    update_cnt(Left, Right, C1),
    node(Left, Right, Key, Value, H1, C1, R2),
    get_balance(R2, Balance),
    rem_rotate(R2, Balance, Left, Right, Result).

map_remove(null, _, null) :- !.
map_remove(node(L, R, K, V, H, C), K, Result) :-
    count_children(L, R, Children),
    remove_current(Children, node(L, R, K, V, H, C), R1),
    map_remove_impl(R1, Result).

map_remove(node(L, R, K, V, H, _), Key, node(Result, R, K, V, H, C1)) :-
    Key < K,
    map_remove(L, Key, Result),
    update_cnt(Result, R, C1).

map_remove(node(L, R, K, V, H, _), Key, node(L, Result, K, V, H, C1)) :-
    Key > K,
    map_remove(R, Key, Result),
    update_cnt(L, Result, C1).

less(null, _, 0).
less(node(L, R, RootKey, _, _, _), Key, Size) :-
    RootKey < Key, !,
    get_cnt(L, S1),
    less(R, Key, S2),
    Size is S1 + 1 + S2.

less(node(L, _, RootKey, _, _, _), Key, Size) :-
    RootKey > Key, !,
    less(L, Key, Size).

less(node(L, R, _, _, _, _), Key, Size) :-
    get_cnt(L, S1),
    less(R, Key, S2),
    Size is S1 + S2.

map_headMapSize(Map, K, Size) :-
    less(Map, K, Size).

map_tailMapSize(Map, K, Size) :-
    map_headMapSize(Map, K, S),
    get_cnt(Map, C),
    Size is C - S.