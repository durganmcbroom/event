package com.durganmcbroom.event.fsm

import com.durganmcbroom.event.api.Event
import java.util.function.Consumer

public class EventTransition(to: EventState, ref: FSM.Reference, private val consumer: Consumer<Event>) : Transition(to, ref) {
    override fun accept(event: Event) {
        consumer.accept(event)
        super.accept(event)
    }
}


