package com.durganmcbroom.event.fsm

import com.durganmcbroom.event.api.Event
import java.util.function.Consumer
import java.util.function.Predicate

public class MutableFSM(
    debug: Boolean = false
) : FSM(StatePlaceholder(), debug) {
    public val ref: Reference = Reference(this)
    private var nameId = 0

    public constructor(debug: Boolean = false, configure: MutableFSM.() -> Unit) : this(debug) {
        configure()
    }

    private fun nextName(): String = "unnamed@${nameId++}"

    public fun of(name: String = nextName()): MutableEventState =
        of(TypedPredicateEventState(name, ref, ArrayList()))

    public fun timedOf(name: String = nextName()): MutableEventState =
        of(TimedEventState(name, ref, ArrayList()))

    public fun timingOutOf(to: EventState, timeOut: Long, name: String = nextName()): MutableEventState =
        of(TimingOutEventState(name, ref, ArrayList(), Transition(to, ref), timeOut))

    public fun of(delegate: PredicateEventState): MutableEventState =
        MutableEventState(delegate).also { if (current is StatePlaceholder) current = it }

    public fun <T : EventState> of(delegate: T): T = delegate.also { if (current is StatePlaceholder) current = it }

    public fun transition(to: EventState): TransitionProvider = TransitionProvider(to)

    public inner class MutableEventState(
        private val delegate: PredicateEventState = TypedPredicateEventState(nextName(), ref, ArrayList()),
    ) : EventState by delegate, MutableList<Transition> by delegate.exits as? MutableList<Transition>
        ?: throw IllegalStateException("Event state must be mutable!") {

        public fun transitionsTo(state: EventState): TransitionProvider = FromTransitionProvider(state)

        private inner class FromTransitionProvider(
            to: EventState
        ) : TransitionProvider(to) {
            override fun with(): Transition =
                super.with().also { add(it) }

            override fun <T : Event> with(
                type: Class<T>,
                predicate: Predicate<T>
            ): Transition =
                super.with(type, predicate).also { add(it) }

            override fun <T : Event> withTime(
                type: Class<T>,
                predicate: Predicate<TimedEventState.TimedEvent<T>>
            ): TimedTransition<T> =
                super.withTime(type, predicate).also { add(it) }
        }
    }

    public open inner class TransitionProvider internal constructor(
        private val to: EventState
    ) {
        public open fun with(): Transition = Transition(to, ref)

        public open fun <T : Event> with(
            type: Class<T>,
            predicate: Predicate<T>
        ): Transition = TypedPredicateTransition(to, ref, type, predicate)

        public open fun <T : Event> withTime(
            type: Class<T>,
            predicate: Predicate<TimedEventState.TimedEvent<T>>
        ): TimedTransition<T> = TimedTransition(to, ref, type, predicate)

        public open fun <T: Event> withEvent(
            consumer: Consumer<Event>
        ) : EventTransition = EventTransition(to, ref, consumer)
    }

    private class StatePlaceholder : EventState {
        override val ref: Nothing
            get() = throw UnsupportedOperationException("This placeholder cannot have a reference to an FSM.")
        override val name: String = "placeholder"

        override fun <T : Event> accept(event: T): Nothing =
            throw UnsupportedOperationException("The placeholder should not receive events.")
    }
}

public inline fun <reified T : Event> MutableFSM.TransitionProvider.with(p: Predicate<T>): Transition =
    with(T::class.java, p)

public inline fun <reified T : Event> MutableFSM.TransitionProvider.withTime(p: Predicate<TimedEventState.TimedEvent<T>>): TimedTransition<T> =
    withTime(T::class.java, p)

public infix fun MutableFSM.MutableEventState.transitionsTo(state: EventState): MutableFSM.TransitionProvider =
    transitionsTo(state)
