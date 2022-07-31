package com.durganmcbroom.event.fsm

import com.durganmcbroom.event.api.Event

public interface EventState {
    public val ref: FSM.Reference
    public val name: String

    public fun onEnter() {}

    public fun <T : Event> accept(event: T)
}
