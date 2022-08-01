package com.durganmcbroom.event.api.test

import com.durganmcbroom.event.api.MutableSubscriberCallback
import kotlin.test.Test

class EventDispatchTests {
    @Test
    fun `Test event dispatching`() {
        val subscriber = MutableSubscriberCallback<TestEvent>()

        val dispatcher = TestEventDispatcher(subscriber)

        subscriber.subscribe {
            println(it.value)
        }

        dispatcher.dispatch()
    }
}