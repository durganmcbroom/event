package com.durganmcbroom.event.api.test

import com.durganmcbroom.event.api.DispatchRegistry
import kotlin.test.Test

class EventDispatchTests {
    @Test
    fun `Test event dispatching`() {
        DispatchRegistry.register(TestEventDispatchProvider())

        DispatchRegistry.subscribeTo(TestEventDispatcher::class, true) {
            println(it.value)
        }

        (DispatchRegistry.get(TestEventDispatcher::class) as TestEventDispatcher).dispatch()
    }
}