% Configurator Interface predicates
set_is_stateful(Switch, IsStateful) :- (IsStateful -> (assert(stateful(Switch))) ; (retract(stateful(Switch)))).

% Client Interface predicates
toggle_state(T) :- (stateful(T) ->
    ((on(T) -> (T <- turnOff, \+retract(on(T))) ; (T <- turnOn, assert(on(T))))) ; 
    ((T <- turnOn), (on(T) -> (\+retract(on(T))) ; (assert(on(T)))))).



