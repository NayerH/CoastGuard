:- include('KB.pl').

goal(S):-
	ids(S,1).

goal2(S):-
	station(Y,X),
	in(Y,X,0,[],S).

ids(X,L):-
	(call_with_depth_limit(goal2(X),L,R), number(R));
	(call_with_depth_limit(goal2(X),L,R),
	R=depth_limit_exceeded,
	L1 is L+1, 
	ids(X,L1)).

in(Y,X,Onboard,SH,S):-
    (agent_loc(Y,X),
	 Onboard = 0,
	 ships_loc(L),
	 same_length(L, SH),
	 L = [L1 | L2],
	 member(L1, SH),
	 (L2 = []; member(L2, SH)),
	 S = s0).

in(Y,X,Onboard,SH,result(A,S)):-
   ((A = drop,
     station(Y,X),
     Onboard = 0,
     (in(Y, X, 1, SH, S); 
      (capacity(2), in(Y, X, 2, SH, S))));
   
    (A = pickup,
	ships_loc([L1 | L2]),
	((L1 = [Y, X], \+ member(L1, SH), SHOld = [L1 | SH]);
	 (L2 = [[Y, X]], \+ member(L2, SH), SHOld = [L2 | SH])),
	capacity(C),
	Onboard =< C,
	OnboardOld is Onboard - 1,
	OnboardOld >= 0,
	in(Y, X, OnboardOld, SHOld, S));
    
    (A = down, 
     grid(_, Y1),
     Y1 > Y,
	 Y >= 1,
	 Yold is Y-1,
	 in(Yold, X, Onboard, SH, S));
    
     (A = right, 
     grid(X1, _),
     X1 > X,
     X >= 1,
     Xold is X-1,
     in(Y, Xold, Onboard, SH, S));

	 (A = up, 
	 grid(_, Y1),
     Yold is Y+1,
	 Yold < Y1,
	 Yold >= 1,
	 in(Yold, X, Onboard, SH, S));

	 (A = left, 
     Xold is X+1,
	 Xold >= 1,
     grid(X1, _),
	 Xold < X1,
	 in(Y, Xold, Onboard, SH, S)) ).