package com.durganmcbroom.event.api

import kotlin.reflect.KClass

public object DispatchRegistry {
    private val dispatchers: MutableMap<KClass<out EventDispatcher<*>>, EventDispatcher<*>> = HashMap()
    private val providers: MutableList<DispatchProvider> = ArrayList()

    public fun register(provider: DispatchProvider) {
        providers.add(provider)
    }

    public fun <E : Event> register(type: KClass<out EventDispatcher<E>>): EventDispatcher<E> = (providers
        .firstNotNullOfOrNull { it.provide(type, RegistryCallback()) }
        ?.also { dispatchers[it::class] = it } as? EventDispatcher<E>)
        ?: throw IllegalArgumentException("Unknown type: '$type', no registered provider was able to provide an acceptable type.")

    public fun <E : Event> get(
        type: KClass<out EventDispatcher<E>>,
        attemptRegistry: Boolean = false
    ): EventDispatcher<E>? = dispatchers[type] as? EventDispatcher<E>
        ?: if (attemptRegistry) register(type) else null

    @JvmOverloads
    public fun <E : Event> subscribeTo(
        type: KClass<out EventDispatcher<E>>,
        attemptRegistry: Boolean = false,
        cb: EventCallback<E>,
    ) {
        val dispatcher = get(type, attemptRegistry)
            ?: throw IllegalArgumentException("Failed to get dispatcher: '$type'.")

        check(dispatcher.callback is RegistryCallback) { "Dispatcher callback must be a registry callback!" }

        val callback = dispatcher.callback as RegistryCallback

        callback.delegates.add(cb)
    }

    private class RegistryCallback<T : Event> : EventCallback<T> {
        val delegates: MutableList<EventCallback<T>> = ArrayList()

        override fun accept(event: T) = delegates.forEach { it.accept(event) }
    }
}