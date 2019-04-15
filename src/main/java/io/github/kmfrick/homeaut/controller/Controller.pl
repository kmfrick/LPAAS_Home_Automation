toggle_state(Switch) :- 
    stateful(Switch) -> (on(Switch) -> turn_off(Switch) ; turn_on(Switch)) ; toggle(Switch).

stateful(Switch) :- false.
toggle(Switch) :- class('io.github.kmfrick.homeaut.controller.Controller') <- salute.