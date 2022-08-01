package com.durganmcbroom.event.api

public open class SubscriberCallback<T : Event>(
    delegate: List<EventCallback<T>> = listOf()
) : EventCallback<T>, List<EventCallback<T>> by delegate {
    override fun accept(event: T): Unit = forEach { it.accept(event) }
}