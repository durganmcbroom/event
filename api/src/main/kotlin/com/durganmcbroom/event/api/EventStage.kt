package com.durganmcbroom.event.api

public interface EventStage {
    public val next: EventStage?

    public fun apply(event: Event) : Event = next?.apply(event) ?: event
}