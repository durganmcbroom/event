package com.durganmcbroom.event.fsm

import com.durganmcbroom.event.api.Event
import java.time.Instant
import java.util.function.Predicate

public open class TimedEventState(
    override val name: String,
    override val ref: FSM.Reference,
    override val exits: List<Transition>,
) : PredicateEventState {
    private lateinit var last: Instant

    override fun onEnter() {
        last = Instant.now()
    }

    override fun <T : Event> accept(event: T): Unit = find(event)?.run {
        this@run.accept(if (this@run is TimedTransition<*>) TimedEvent(event, last) else event)
        last = Instant.now()
    } ?: Unit

    override fun <T : Event> find(event: T): PredicateTransition? =
        exits.filterIsInstance<TimedTransition<*>>().find {
            it.type.isAssignableFrom(event::class.java)
        } ?: exits.filterIsInstance<TypedPredicateTransition<*>>().find {
            it.type.isAssignableFrom(event::class.java)
        }

    public data class TimedEvent<T : Event>(
        public val data: T,
        public val instant: Instant
    ) : Event
}

public class TimedTransition<T : Event>(
    to: EventState,
    reference: FSM.Reference,
    public val type: Class<T>,
    predicate: Predicate<TimedEventState.TimedEvent<T>>,
) : PredicateTransition(to, reference, Predicate {
    it is TimedEventState.TimedEvent<*> && type.isInstance(it.data) && predicate.test(it as TimedEventState.TimedEvent<T>)
})

