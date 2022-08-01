package com.durganmcbroom.event.api.test

import com.durganmcbroom.event.api.*
import kotlin.test.Test

class EventStageTests {
    @Test
    fun `Test event stages`() {
        val firstStage = object : TypedEventStage<TestEvent>(TestEvent::class) {
            override val next: EventStage
                get() = EventCallbackStage(TestEvent::class) {
                    println(it.value)
                }

            override fun applyType(event: TestEvent): Event {
                println(event.value)

                return TestEvent("Hello, this is different?")
            }
        }

        val pipe = EventPipeline(firstStage)

        val subscriber = MutableSubscriberCallback<TestEvent>()

        val dispatcher = TestEventDispatcher(subscriber)

        subscriber.subscribe(pipe)

        dispatcher.dispatch()
    }
}