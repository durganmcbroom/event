package com.durganmcbroom.event.api

public interface EventDispatcher<in T: Event> {
    public val callback: EventCallback<T>
}