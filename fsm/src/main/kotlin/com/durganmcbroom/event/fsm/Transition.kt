package com.durganmcbroom.event.fsm

import com.durganmcbroom.event.api.Event

public open class Transition(
    private val to: EventState,
    private val ref: FSM.Reference,
) {
    public open fun accept(event: Event): Unit = ref.set(to)
}