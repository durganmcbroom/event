package com.durganmcbroom.event.fsm

import com.durganmcbroom.event.api.Event
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ThreadFactory
import java.util.concurrent.TimeUnit

public class TimingOutEventState(
    name: String,
    ref: FSM.Reference,
    exits: List<Transition>,
    private val timeoutExit: Transition,
    private val timeout: Long
) : TimedEventState(name, ref, exits) {
    override fun onEnter() {
        submit(timeout) {
            if (ref.isCurrent(this)) timeoutExit.accept(object : Event {})
        }

        super.onEnter()
    }

    private companion object {
        val defaultThreadFactory: ThreadFactory = Executors.defaultThreadFactory()
        val pool: ScheduledExecutorService = Executors.newScheduledThreadPool(2) {
            val thread = defaultThreadFactory.newThread(it)
            thread.isDaemon = true
            thread
        }

        fun submit(time: Long, block: () -> Unit) {
            pool.schedule(block, time, TimeUnit.MILLISECONDS)
        }
    }
}