package com.durganmcbroom.event.api

import kotlin.reflect.KClass

public class EventCallbackStage<T : Event>(
    type: KClass<T>,
    override val next: EventStage? = null,
    private val cb: EventCallback<T>
) : TypedEventStage<T>(type) {
    @JvmOverloads
    public constructor(type: Class<T>, next: EventStage? = null, cb: EventCallback<T>) : this(type.kotlin, next, cb)

    override fun applyType(event: T): Event = event.apply { cb.accept(event) }
}