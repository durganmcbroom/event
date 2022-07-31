package com.durganmcbroom.event.fsm

import com.durganmcbroom.event.api.Event
import com.durganmcbroom.event.api.EventCallback
import java.lang.ref.SoftReference
import kotlin.properties.Delegates
import java.util.logging.*

public open class FSM(
    first: EventState,
    private val debug: Boolean = false
) : EventCallback<Event> {
    private val logger = Logger.getLogger("com.durganmcbroom.event.fsm")
    internal var current: EventState by Delegates.observable(first) { _, old, new ->
        new.onEnter()
        if (debug) logger.log(Level.INFO, "State changed from '${old.name}' to '${new.name}'")
    }

    override fun accept(event: Event) {
        current.accept(event)
    }

    public class Reference(
        fsm: FSM
    ) : SoftReference<FSM>(fsm) {
        @Synchronized
        public fun set(state: EventState): Unit = let { get()?.current = state }

        public fun isCurrent(state: EventState): Boolean {
            val current = this.get()?.current ?: return false

            return state.ref.get() == this.get() ||
                    state.name == current.name
        }
    }
}

