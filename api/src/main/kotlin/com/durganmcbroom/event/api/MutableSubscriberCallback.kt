package com.durganmcbroom.event.api

public class MutableSubscriberCallback<T: Event>(
    private val delegate: MutableList<EventCallback<T>> = ArrayList()
) : SubscriberCallback<T>(delegate) {
    public fun subscribe(cb: EventCallback<T>) {
        delegate.add(cb)
    }
}