package com.silverpine.uu.ux.events

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch

/**
 * Global event bus for the UU event system.
 *
 * Events are posted via [post] and processed by installed [UUEventHandler] implementations.
 * Handlers are installed via [installHandler] and can process events based on their [UUEventHandler.eventClasses].
 *
 * The event bus uses a [SharedFlow] with a buffer capacity of 64 events, dropping the oldest
 * events when the buffer overflows.
 *
 * @see UUEvent
 * @see UUEventHandler
 */
object UUEventBus
{
    private val _events = MutableSharedFlow<UUEvent>(
        extraBufferCapacity = 64,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    /**
     * Shared flow of all events posted to the event bus.
     * Subscribers can collect from this flow to receive events.
     *
     * For type-safe subscription to specific event types, use [subscribe].
     */
    val events: SharedFlow<UUEvent> = _events.asSharedFlow()

    private val handlers = mutableMapOf<Class<out UUEvent>, MutableList<UUEventHandler>>()
    private val collectionScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var collectionJob: kotlinx.coroutines.Job? = null

    init
    {
        startEventCollection()
    }

    /**
     * Post an event to the event bus.
     * Events are processed by handlers in the order they were installed.
     * Processing stops when a handler returns true (indicating the event was handled).
     */
    fun post(event: UUEvent)
    {
        _events.tryEmit(event)
    }

    /**
     * Install a handler for a specific event type.
     * The handler will only receive events of the specified type.
     *
     * @param eventClass The event type class this handler should process
     * @param handler The handler to install
     *
     * Example:
     * ```kotlin
     * UUEventBus.installHandler(UUNavBackEvent::class.java) { event ->
     *     // Handle navigation back
     *     true
     * }
     * ```
     */
    fun installHandler(eventClass: Class<out UUEvent>, handler: UUEventHandler)
    {
        synchronized(handlers)
        {
            handlers.getOrPut(eventClass) { mutableListOf() }.add(handler)
        }
    }

    /**
     * Install a handler for a specific event type using reified generics.
     * The handler will only receive events of the specified type.
     *
     * @param handler The handler to install
     *
     * Example:
     * ```kotlin
     * UUEventBus.installHandler<UUNavBackEvent> { event ->
     *     // Handle navigation back
     *     true
     * }
     * ```
     */
    inline fun <reified T : UUEvent> installHandler(handler: UUEventHandler)
    {
        installHandler(T::class.java, handler)
    }

    /**
     * Uninstall a handler for a specific event type.
     *
     * @param eventClass The event type class
     * @param handler The handler to uninstall
     */
    fun uninstallHandler(eventClass: Class<out UUEvent>, handler: UUEventHandler)
    {
        synchronized(handlers)
        {
            handlers[eventClass]?.remove(handler)
            if (handlers[eventClass].isNullOrEmpty())
            {
                handlers.remove(eventClass)
            }
        }
    }

    /**
     * Uninstall a handler using reified generics.
     */
    inline fun <reified T : UUEvent> uninstallHandler(handler: UUEventHandler)
    {
        uninstallHandler(T::class.java, handler)
    }

    /**
     * Returns a Flow filtered to only emit events of the specified type.
     * Provides type-safe subscription to specific event types.
     *
     * @param T The event type to subscribe to (must be a subtype of UUEvent)
     * @return A Flow that only emits events of type T
     *
     * Example:
     * ```kotlin
     * UUEventBus.subscribe<UUNavBackEvent> { event ->
     *     // event is automatically typed as UUNavBackEvent
     *     handleNavBack(event)
     * }
     * ```
     */
    inline fun <reified T : UUEvent> subscribe(): Flow<T>
    {
        return events.filterIsInstance<T>()
    }

    private fun startEventCollection()
    {
        if (collectionJob?.isActive == true)
        {
            return
        }

        collectionJob = collectionScope.launch()
        {
            events.collect()
            { event ->
                processEvent(event)
            }
        }
    }

    private fun processEvent(event: UUEvent)
    {
        // Get handlers for the specific event type and its supertypes
        // A handler registered for a base class should also handle derived events
        val eventHandlers = synchronized(handlers)
        {
            val eventClass = event::class.java
            handlers.filterKeys { handlerEventClass ->
                // Include handlers where the handler's event class is a superclass of the event,
                // or where the handler is registered for the exact event type
                handlerEventClass.isAssignableFrom(eventClass)
            }
                .values
                .flatten()
                .toList()  // Create a copy to avoid holding lock during processing
        }

        // Process handlers in order, stopping at first success
        for (handler in eventHandlers)
        {
            if (handler.handleEvent(event))
            {
                break
            }
        }
    }
}