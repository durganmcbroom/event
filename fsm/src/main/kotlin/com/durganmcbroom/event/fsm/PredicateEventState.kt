package com.durganmcbroom.event.fsm

import com.durganmcbroom.event.api.Event
import java.util.function.Consumer
import java.util.function.Predicate

public interface PredicateEventState : EventState {
    public val exits: List<Transition>

    override fun <T : Event> accept(event: T): Unit = find(event)?.accept(event) ?: Unit

    public fun <T : Event> find(event: T): Transition?
}

public open class PredicateTransition(
    to: EventState, ref: FSM.Reference,
    private val predicate: Predicate<Event>
) : Transition(to, ref), Consumer<Event> {
    override fun accept(event: Event) {
        if (predicate.test(event)) super.accept(event)
    }
}

