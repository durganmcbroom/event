package com.durganmcbroom.event.api.test

import com.durganmcbroom.event.api.Event
import com.durganmcbroom.event.api.EventCallback
import com.durganmcbroom.event.api.EventDispatcher
import java.util.UUID
import kotlin.reflect.KClass

class TestEvent(
    val value: String
) : Event

class TestEventDispatcher(override val callback: EventCallback<TestEvent>) : EventDispatcher<TestEvent> {
    fun dispatch() {
        callback.accept(TestEvent("Testing @${UUID.randomUUID()}"))
    }
}

