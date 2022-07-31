package com.durganmcbroom.event.api

import kotlin.reflect.KClass

public abstract class TypedEventStage<T : Event>(private val type: KClass<T>) : EventStage {
    public constructor(type: Class<T>) : this(type.kotlin)

    final override fun apply(event: Event): Event {
        val e = if (type.isInstance(event)) applyType(event as T) else event

        return next?.apply(e) ?: e
    }

    public abstract fun applyType(event: T): Event
}