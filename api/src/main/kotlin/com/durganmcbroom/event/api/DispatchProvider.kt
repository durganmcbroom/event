package com.durganmcbroom.event.api

import kotlin.reflect.KClass

public interface DispatchProvider {
    public fun provide(type: KClass<out EventDispatcher<*>>, cb: EventCallback<Event>) : EventDispatcher<*>?
}