package com.durganmcbroom.event.fsm

import com.durganmcbroom.event.api.Event
import com.durganmcbroom.event.api.EventStage

public class FSMStage(override val next: EventStage?, private val fsm: FSM) : EventStage {
    override fun apply(event: Event): Event {
        fsm.accept(event)
        return super.apply(event)
    }
}