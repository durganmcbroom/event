package com.durganmcbroom.event.api

public fun interface EventCallback<in T: Event> {
    public fun accept(event : T)
}
