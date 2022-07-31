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

        DispatchRegistry.register(TestEventDispatchProvider())

        DispatchRegistry.subscribeTo(TestEventDispatcher::class, true, pipe)

        (DispatchRegistry.get(TestEventDispatcher::class) as TestEventDispatcher).dispatch()
    }
}