package com.durganmcbroom.event.api

public class EventPipeline(
    public val first: EventStage
) : EventCallback<Event> {
    override fun accept(event: Event) {
        first.apply(event)
    }
}