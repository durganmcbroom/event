package com.durganmcbroom.event.fsm

import com.durganmcbroom.event.api.Event
import java.util.function.Predicate

public open class TypedPredicateEventState(
    name: String? = null,
    override val ref: FSM.Reference,
    override val exits: List<Transition>,
) : PredicateEventState {
    public override val name: String = name ?: "unnamed@${System.identityHashCode(this)}"

    override fun <T : Event> find(event: T): Transition? =
        exits.filterIsInstance<TypedPredicateTransition<*>>().find { it.type.isAssignableFrom(event::class.java) } ?: exits.firstOrNull()
}

public class TypedPredicateTransition<T : Event>(
    to: EventState, ref: FSM.Reference, public val type: Class<T>,

    predicate: Predicate<T>
) : PredicateTransition(to, ref, predicate as Predicate<Event>) {
    override fun accept(event: Event) {
        if (type.isAssignableFrom(event::class.java)) super.accept(event)
    }
}