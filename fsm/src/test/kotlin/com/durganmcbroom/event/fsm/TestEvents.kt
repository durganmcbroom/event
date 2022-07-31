package com.durganmcbroom.event.fsm

import com.durganmcbroom.event.api.Event
import com.durganmcbroom.event.api.EventCallback
import com.durganmcbroom.event.api.EventDispatcher

//val testEventOne: Class<TestEventOneDispatcher> = TestEventOneDispatcher::class.java
//val testEventTwo: Class<TestEventTwoDispatcher> = TestEventTwoDispatcher::class.java
//
//abstract class SuperDispatcher: EventDispatcher<TestEventOne>
//
//class TestEventOneDispatcher(override val callback: EventCallback<TestEventOne>) : SuperDispatcher() {
//}
//
//class TestEventTwoDispatcher(override val callback: EventCallback<TestEventTwo>) : EventDispatcher<TestEventTwo> {
//}


class TestEventTwo(
    val int: Int
) : Event

class TestEventOne : Event
class MouseMoveData(val x: Int, val y: Int, val dx: Int, val dy: Int) : Event
class MouseActionData(val key: Int, val state: Boolean) : Event
class KeyboardActionData(val key: Int, state: Boolean) : Event