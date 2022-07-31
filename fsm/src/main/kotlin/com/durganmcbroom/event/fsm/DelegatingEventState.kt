package com.durganmcbroom.event.fsm

import com.durganmcbroom.event.api.Event

public abstract class DelegatingEventState(
    override val name: String,
    override val ref: FSM.Reference
) : EventState {
    public override fun <T : Event> accept(event: T): Unit = getFor(event)?.accept(event) ?: Unit

    public abstract fun <T : Event> getFor(event: T): Transition?
}