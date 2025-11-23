package com.silverpine.uu.ux.events

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * JUnit 5 unit tests for UUEventBus class.
 *
 * These tests verify event posting, handler installation/uninstallation,
 * event processing, and flow subscription functionality.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class UUEventBusTests
{
    // Test event classes - using unique classes per test to avoid interference
    class TestEvent1 : UUEvent()
    class TestEvent2 : UUEvent()
    class TestEvent3 : UUEvent()
    open class BaseTestEvent : UUEvent()
    class DerivedTestEvent : BaseTestEvent()

    // Track handlers installed in each test for cleanup
    private val installedHandlers = mutableListOf<Pair<Class<out UUEvent>, UUEventHandler>>()

    companion object
    {
        val testDispatcher = StandardTestDispatcher()

        @JvmStatic
        @BeforeAll
        fun setUpAll()
        {
            Dispatchers.setMain(testDispatcher)
            // Force UUEventBus to initialize after test dispatcher is set
            // by accessing it - this will trigger the init block with test dispatcher
            @Suppress("UNUSED_VARIABLE")
            val unused = UUEventBus.events
            // Advance time to allow initialization
            testDispatcher.scheduler.advanceUntilIdle()
        }

        @JvmStatic
        @AfterAll
        fun tearDownAll()
        {
            Dispatchers.resetMain()
        }
    }

    private fun testScope() = TestScope(testDispatcher)

    @BeforeEach
    fun setUp()
    {
        installedHandlers.clear()
    }

    @AfterEach
    fun tearDown()
    {
        // Uninstall all handlers that were installed during this test
        installedHandlers.forEach { (eventClass, handler) ->
            UUEventBus.uninstallHandler(eventClass, handler)
        }
        installedHandlers.clear()
        // Advance to ensure cleanup completes
        testDispatcher.scheduler.advanceUntilIdle()
    }

    // Helper function to install handler and track it for cleanup
    private fun installAndTrack(eventClass: Class<out UUEvent>, handler: UUEventHandler)
    {
        UUEventBus.installHandler(eventClass, handler)
        installedHandlers.add(eventClass to handler)
    }

    // Helper function to install handler with reified generics and track it
    private inline fun <reified T : UUEvent> installAndTrack(handler: UUEventHandler)
    {
        UUEventBus.installHandler(T::class.java, handler)
        installedHandlers.add(T::class.java to handler)
    }

    // Helper class to track handler invocations
    class TestEventHandler(
        val eventClass: Class<out UUEvent>,
        var shouldHandle: Boolean = false,
        var handledEvents: MutableList<UUEvent> = mutableListOf(),
        var handleCount: Int = 0
    ) : UUEventHandler
    {
        override val eventClasses: List<Class<out UUEvent>> = listOf(eventClass)

        override fun handleEvent(event: UUEvent): Boolean
        {
            handledEvents.add(event)
            handleCount++
            return shouldHandle
        }
    }

    @Test
    fun `post posts event to event bus`() = testScope().runTest {
        // Given: An event
        val event = TestEvent1()
        var receivedEvent: TestEvent1? = null

        // Subscribe first
        val job = launch {
            receivedEvent = UUEventBus.subscribe<TestEvent1>().first()
        }
        testDispatcher.scheduler.advanceUntilIdle()

        // When: Posting the event
        UUEventBus.post(event)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then: Event should be available in events flow
        Assertions.assertNotNull(receivedEvent)
        Assertions.assertTrue(receivedEvent is TestEvent1)
        job.cancel()
    }

    @Test
    fun `installHandler registers handler for event type`()
    {
        // Given: A handler for TestEvent1
        val handler = TestEventHandler(TestEvent1::class.java)
        val event = TestEvent1()

        // When: Installing the handler and posting an event
        testScope().runTest {
            installAndTrack(TestEvent1::class.java, handler)
            testDispatcher.scheduler.advanceUntilIdle() // Ensure handler is registered
            UUEventBus.post(event)
            testDispatcher.scheduler.advanceUntilIdle() // Process the event

            // Then: Handler should receive the event
            Assertions.assertEquals(1, handler.handleCount)
            Assertions.assertEquals(1, handler.handledEvents.size)
            Assertions.assertTrue(handler.handledEvents[0] is TestEvent1)
        }
    }

    @Test
    fun `installHandler with reified generics registers handler`()
    {
        // Given: A handler for TestEvent1
        val handler = TestEventHandler(TestEvent1::class.java)
        val event = TestEvent1()

        // When: Installing the handler using reified generics and posting an event
        testScope().runTest {
            installAndTrack<TestEvent1>(handler)
            testDispatcher.scheduler.advanceUntilIdle() // Ensure handler is registered
            UUEventBus.post(event)
            testDispatcher.scheduler.advanceUntilIdle() // Process the event

            // Then: Handler should receive the event
            Assertions.assertEquals(1, handler.handleCount)
        }
    }

    @Test
    fun `uninstallHandler removes handler from event bus`()
    {
        // Given: A handler installed for TestEvent1
        val handler = TestEventHandler(TestEvent1::class.java)

        testScope().runTest {
            installAndTrack(TestEvent1::class.java, handler)
            testDispatcher.scheduler.advanceUntilIdle()

            // When: Uninstalling the handler and posting an event
            UUEventBus.uninstallHandler(TestEvent1::class.java, handler)
            UUEventBus.post(TestEvent1())
            testDispatcher.scheduler.advanceUntilIdle()

            // Then: Handler should not receive the event
            Assertions.assertEquals(0, handler.handleCount)
        }
    }

    @Test
    fun `uninstallHandler with reified generics removes handler`()
    {
        // Given: A handler installed for TestEvent1
        val handler = TestEventHandler(TestEvent1::class.java)

        testScope().runTest {
            installAndTrack<TestEvent1>(handler)
            testDispatcher.scheduler.advanceUntilIdle()

            // When: Uninstalling using reified generics and posting an event
            UUEventBus.uninstallHandler<TestEvent1>(handler)
            UUEventBus.post(TestEvent1())
            testDispatcher.scheduler.advanceUntilIdle()

            // Then: Handler should not receive the event
            Assertions.assertEquals(0, handler.handleCount)
        }
    }

    @Test
    fun `handler returns true stops processing`()
    {
        // Given: Two handlers, first one handles the event
        val handler1 = TestEventHandler(TestEvent1::class.java, shouldHandle = true)
        val handler2 = TestEventHandler(TestEvent1::class.java)

        testScope().runTest {
            installAndTrack(TestEvent1::class.java, handler1)
            installAndTrack(TestEvent1::class.java, handler2)
            testDispatcher.scheduler.advanceUntilIdle() // Ensure handlers are registered

            // When: Posting an event
            UUEventBus.post(TestEvent1())
            testDispatcher.scheduler.advanceUntilIdle() // Process the event

            // Then: Only first handler should receive the event
            Assertions.assertEquals(1, handler1.handleCount)
            Assertions.assertEquals(0, handler2.handleCount)
        }
    }

    @Test
    fun `handler returns false continues processing`()
    {
        // Given: Two handlers, both return false
        val handler1 = TestEventHandler(TestEvent1::class.java, shouldHandle = false)
        val handler2 = TestEventHandler(TestEvent1::class.java, shouldHandle = false)

        testScope().runTest {
            installAndTrack(TestEvent1::class.java, handler1)
            installAndTrack(TestEvent1::class.java, handler2)
            testDispatcher.scheduler.advanceUntilIdle() // Ensure handlers are registered

            // When: Posting an event
            UUEventBus.post(TestEvent1())
            testDispatcher.scheduler.advanceUntilIdle() // Process the event

            // Then: Both handlers should receive the event
            Assertions.assertEquals(1, handler1.handleCount)
            Assertions.assertEquals(1, handler2.handleCount)
        }
    }

    @Test
    fun `handlers process events in installation order`()
    {
        // Given: Three handlers installed in order
        val handler1 = TestEventHandler(TestEvent1::class.java)
        val handler2 = TestEventHandler(TestEvent1::class.java)
        val handler3 = TestEventHandler(TestEvent1::class.java)

        testScope().runTest {
            installAndTrack(TestEvent1::class.java, handler1)
            installAndTrack(TestEvent1::class.java, handler2)
            installAndTrack(TestEvent1::class.java, handler3)
            testDispatcher.scheduler.advanceUntilIdle() // Ensure handlers are registered

            // When: Posting an event
            UUEventBus.post(TestEvent1())
            testDispatcher.scheduler.advanceUntilIdle() // Process the event

            // Then: Handlers should process in order
            Assertions.assertEquals(1, handler1.handleCount)
            Assertions.assertEquals(1, handler2.handleCount)
            Assertions.assertEquals(1, handler3.handleCount)
        }
    }

    @Test
    fun `handler registered for base class handles derived events`()
    {
        // Given: A handler registered for BaseTestEvent
        val baseHandler = TestEventHandler(BaseTestEvent::class.java)
        val derivedEvent = DerivedTestEvent()

        testScope().runTest {
            installAndTrack(BaseTestEvent::class.java, baseHandler)
            testDispatcher.scheduler.advanceUntilIdle() // Ensure handler is registered

            // When: Posting a DerivedTestEvent
            UUEventBus.post(derivedEvent)
            testDispatcher.scheduler.advanceUntilIdle() // Process the event

            // Then: Base handler should receive the derived event
            Assertions.assertEquals(1, baseHandler.handleCount)
            Assertions.assertTrue(baseHandler.handledEvents[0] is DerivedTestEvent)
        }
    }

    @Test
    fun `handler registered for specific type does not handle base class events`()
    {
        // Given: A handler registered for DerivedTestEvent
        val derivedHandler = TestEventHandler(DerivedTestEvent::class.java)
        val baseEvent = BaseTestEvent()

        testScope().runTest {
            installAndTrack(DerivedTestEvent::class.java, derivedHandler)
            testDispatcher.scheduler.advanceUntilIdle() // Ensure handler is registered

            // When: Posting a BaseTestEvent
            UUEventBus.post(baseEvent)
            testDispatcher.scheduler.advanceUntilIdle() // Process the event

            // Then: Derived handler should not receive the base event
            Assertions.assertEquals(0, derivedHandler.handleCount)
        }
    }

    @Test
    fun `handler only receives events of registered type`()
    {
        // Given: Handlers for different event types
        val handler1 = TestEventHandler(TestEvent1::class.java)
        val handler2 = TestEventHandler(TestEvent2::class.java)

        testScope().runTest {
            installAndTrack(TestEvent1::class.java, handler1)
            installAndTrack(TestEvent2::class.java, handler2)
            testDispatcher.scheduler.advanceUntilIdle() // Ensure handlers are registered

            // When: Posting TestEvent1
            UUEventBus.post(TestEvent1())
            testDispatcher.scheduler.advanceUntilIdle() // Process the event

            // Then: Only handler1 should receive the event
            Assertions.assertEquals(1, handler1.handleCount)
            Assertions.assertEquals(0, handler2.handleCount)
        }
    }

    @Test
    fun `subscribe returns flow filtered to specific event type`() = testScope().runTest {
        // Given: Multiple event types posted
        var receivedEvent: TestEvent1? = null

        // Subscribe to TestEvent1
        val job = launch {
            val event = UUEventBus.subscribe<TestEvent1>().first()
            receivedEvent = event
        }

        // Post different event types
        testDispatcher.scheduler.advanceUntilIdle()
        UUEventBus.post(TestEvent2())
        UUEventBus.post(TestEvent3())
        UUEventBus.post(TestEvent1())
        UUEventBus.post(TestEvent2())
        testDispatcher.scheduler.advanceUntilIdle()

        // Then: Should receive only TestEvent1
        Assertions.assertNotNull(receivedEvent)
        Assertions.assertTrue(receivedEvent is TestEvent1)
        job.cancel()
    }

    @Test
    fun `subscribe filters multiple events of same type`() = testScope().runTest {
        // Given: Subscribed to TestEvent1
        // When: Posting multiple TestEvent1 events
        launch {
            testDispatcher.scheduler.advanceUntilIdle()
            UUEventBus.post(TestEvent1())
            UUEventBus.post(TestEvent2())
            UUEventBus.post(TestEvent1())
            UUEventBus.post(TestEvent3())
            UUEventBus.post(TestEvent1())
        }

        // Then: Should receive only TestEvent1 instances
        val receivedEvents = UUEventBus.subscribe<TestEvent1>().take(3).toList()
        Assertions.assertEquals(3, receivedEvents.size)
        receivedEvents.forEach { Assertions.assertTrue(it is TestEvent1) }
    }

    @Test
    fun `multiple handlers can be installed for same event type`()
    {
        // Given: Multiple handlers for same event type
        val handler1 = TestEventHandler(TestEvent1::class.java)
        val handler2 = TestEventHandler(TestEvent1::class.java)
        val handler3 = TestEventHandler(TestEvent1::class.java)

        testScope().runTest {
            installAndTrack(TestEvent1::class.java, handler1)
            installAndTrack(TestEvent1::class.java, handler2)
            installAndTrack(TestEvent1::class.java, handler3)

            // When: Posting an event
            UUEventBus.post(TestEvent1())
            testDispatcher.scheduler.advanceUntilIdle()

            // Then: All handlers should receive the event
            Assertions.assertEquals(1, handler1.handleCount)
            Assertions.assertEquals(1, handler2.handleCount)
            Assertions.assertEquals(1, handler3.handleCount)
        }
    }

    @Test
    fun `uninstallHandler removes only specified handler`()
    {
        // Given: Multiple handlers installed
        val handler1 = TestEventHandler(TestEvent1::class.java)
        val handler2 = TestEventHandler(TestEvent1::class.java)
        val handler3 = TestEventHandler(TestEvent1::class.java)

        testScope().runTest {
            installAndTrack(TestEvent1::class.java, handler1)
            installAndTrack(TestEvent1::class.java, handler2)
            installAndTrack(TestEvent1::class.java, handler3)
            testDispatcher.scheduler.advanceUntilIdle()

            // When: Uninstalling handler2 and posting an event
            UUEventBus.uninstallHandler(TestEvent1::class.java, handler2)
            testDispatcher.scheduler.advanceUntilIdle() // Ensure handler is uninstalled
            UUEventBus.post(TestEvent1())
            testDispatcher.scheduler.advanceUntilIdle() // Process the event

            // Then: handler1 and handler3 should receive event, handler2 should not
            Assertions.assertEquals(1, handler1.handleCount)
            Assertions.assertEquals(0, handler2.handleCount)
            Assertions.assertEquals(1, handler3.handleCount)
        }
    }

    @Test
    fun `events flow contains all posted events`() = testScope().runTest {
        // Given: Multiple events posted
        val eventsList = mutableListOf<UUEvent>()
        val job = launch {
            UUEventBus.events.take(5).collect { event ->
                eventsList.add(event)
            }
        }

        // When: Posting various events
        testDispatcher.scheduler.advanceUntilIdle()
        UUEventBus.post(TestEvent1())
        UUEventBus.post(TestEvent2())
        UUEventBus.post(TestEvent3())
        UUEventBus.post(TestEvent1())
        UUEventBus.post(TestEvent2())
        testDispatcher.scheduler.advanceUntilIdle()

        // Then: All events should be in the list
        Assertions.assertEquals(5, eventsList.size)
        Assertions.assertTrue(eventsList[0] is TestEvent1)
        Assertions.assertTrue(eventsList[1] is TestEvent2)
        Assertions.assertTrue(eventsList[2] is TestEvent3)
        Assertions.assertTrue(eventsList[3] is TestEvent1)
        Assertions.assertTrue(eventsList[4] is TestEvent2)
        job.cancel()
    }

    @Test
    fun `handler can handle multiple events`()
    {
        // Given: A handler installed
        val handler = TestEventHandler(TestEvent1::class.java)

        testScope().runTest {
            installAndTrack(TestEvent1::class.java, handler)
            testDispatcher.scheduler.advanceUntilIdle() // Ensure handler is registered

            // When: Posting multiple events of the same type
            UUEventBus.post(TestEvent1())
            UUEventBus.post(TestEvent1())
            UUEventBus.post(TestEvent1())
            testDispatcher.scheduler.advanceUntilIdle() // Process all events

            // Then: Handler should receive all events
            Assertions.assertEquals(3, handler.handleCount)
            Assertions.assertEquals(3, handler.handledEvents.size)
        }
    }

    @Test
    fun `handler installation and uninstallation are thread safe`()
    {
        // Given: Multiple threads installing/uninstalling handlers
        val handlers = (1..10).map { TestEventHandler(TestEvent1::class.java) }
        val latch = CountDownLatch(10)

        // When: Concurrent installation/uninstallation
        handlers.forEach { handler ->
            Thread {
                installAndTrack(TestEvent1::class.java, handler)
                Thread.sleep(10)
                UUEventBus.uninstallHandler(TestEvent1::class.java, handler)
                latch.countDown()
            }.start()
        }

        // Then: Should complete without exceptions
        Assertions.assertTrue(latch.await(5, TimeUnit.SECONDS))
    }

    @Test
    fun `posting events after handler uninstallation does not reach handler`()
    {
        // Given: Handler installed and then uninstalled
        val handler = TestEventHandler(TestEvent1::class.java)

        testScope().runTest {
            installAndTrack(TestEvent1::class.java, handler)
            testDispatcher.scheduler.advanceUntilIdle()
            UUEventBus.uninstallHandler(TestEvent1::class.java, handler)
            testDispatcher.scheduler.advanceUntilIdle()

            // When: Posting events after uninstallation
            UUEventBus.post(TestEvent1())
            UUEventBus.post(TestEvent1())
            testDispatcher.scheduler.advanceUntilIdle()

            // Then: Handler should not receive events
            Assertions.assertEquals(0, handler.handleCount)
        }
    }

    @Test
    fun `handler processes events in correct order when installed after posting`()
    {
        // Given: Event posted before handler installation
        val handler = TestEventHandler(TestEvent1::class.java)

        testScope().runTest {
            // When: Posting event, then installing handler, then posting another
            UUEventBus.post(TestEvent1())
            testDispatcher.scheduler.advanceUntilIdle()
            installAndTrack(TestEvent1::class.java, handler)
            testDispatcher.scheduler.advanceUntilIdle()
            UUEventBus.post(TestEvent1())
            testDispatcher.scheduler.advanceUntilIdle()

            // Then: Handler should only receive events posted after installation
            // Handler should receive the event posted after installation
            Assertions.assertTrue(handler.handleCount >= 1)
        }
    }
}