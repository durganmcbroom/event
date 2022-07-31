package com.durganmcbroom.event.fsm


import com.durganmcbroom.event.api.Event
import org.junit.jupiter.api.Test
import java.lang.Thread.sleep
import java.time.Duration
import java.time.Instant

class FSMTests {
    @Test
    fun `Basic test`() {
        fun bounding(x: Int, y: Int, top: Int, left: Int, bottom: Int, right: Int): Boolean {
            return y in (top + 1) until bottom && left < x && right > x
        }

        val fsm = MutableFSM(true).apply {
            //STATES
            val initial = of("Starting state")
            val inBox = of("Mouse inside box")
            val clicked = of("Mouse clicked in box")
            val outsideNotClicked = of("Mouse outside box but not unfocused")

            // Variables - not really part of the example
            val x = 10
            val y = 10
            val w = 100
            val h = 100

            // Transitions between states.
            (initial transitionsTo inBox).with<MouseMoveData> {
                bounding(it.x, it.y, x, y, x + w, y + h)
            }

            (inBox transitionsTo initial).with<MouseMoveData> {
                !bounding(it.x, it.y, x, y, x + w, y + h)
            }

            (inBox transitionsTo clicked).with<MouseActionData> {
                it.key == 1 && it.state
            }

            (clicked transitionsTo inBox).with<MouseActionData> {
                it.key != 1 && it.state
            }

            (clicked transitionsTo clicked).with<KeyboardActionData> {
                println("Event happened")
                true
            }

            (clicked transitionsTo outsideNotClicked).with<MouseMoveData>() {
                !bounding(it.x, it.y, x, y, x + w, y + h)
            }

            (outsideNotClicked transitionsTo clicked).with<MouseMoveData> {
                bounding(it.x, it.y, x, y, x + w, y + h)
            }

            (outsideNotClicked transitionsTo initial).with<MouseActionData> {
                it.state
            }
        }

        println(fsm.current.name)

        fsm.accept(MouseMoveData(50, 50, 11, 1))
        fsm.accept(MouseMoveData(500, 500, 11, 1))
        fsm.accept(MouseMoveData(60, 60, 11, 1))
        fsm.accept(MouseActionData(1, true))
        fsm.accept(MouseMoveData(500, 500, 11, 1))
        fsm.accept(KeyboardActionData(1, true))
        fsm.accept(MouseActionData(1, true))
    }

    @Test
    fun testWithTiming() {
        val fsm = MutableFSM(true).apply {
            val initialTimed = timedOf("Initial")
            val second = of("First timed one")
            val lastOne = of("Last")

            (initialTimed transitionsTo second).withTime<KeyboardActionData> {
                println(Duration.between(it.instant, Instant.now()).toMillis())
                Duration.between(it.instant, Instant.now()).toMillis() <= 1000
            }

            (second transitionsTo lastOne).with<Event> {
                println("Event")
                true
            }

            (second transitionsTo initialTimed).with<Event> {
                false
            }
        }

        sleep(500)
        fsm.accept(KeyboardActionData(1, true))
        fsm.accept(TestEventOne())
        fsm.accept(TestEventOne())
    }

    @Test
    fun testTimingOutState() {
        val fsm = MutableFSM(true).apply {
            val initial = of("Initial")
            val timingOut = timingOutOf(initial, 1000, "Some name")

            (initial transitionsTo timingOut).with()
        }

        fsm.accept(TestEventOne())

        sleep(2000)
    }
}


