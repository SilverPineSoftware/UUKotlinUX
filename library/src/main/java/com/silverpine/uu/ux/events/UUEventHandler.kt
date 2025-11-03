package com.silverpine.uu.ux.events

import android.app.Activity
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.silverpine.uu.ux.uuOpenSystemSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Interface for handling events in the UU event system.
 *
 * Handlers are installed via [install] or [UUEventBus.installHandler] and will receive
 * events of the types specified in [eventClasses]. Handlers process events in the order
 * they were installed, and processing stops when a handler returns `true` (indicating
 * the event was handled).
 *
 * @see UUEventBus
 * @see UUEvent
 */
interface UUEventHandler
{
    /**
     * The list of event classes this handler processes.
     * This is defined at compile time and used by the parameterless install() method.
     */
    val eventClasses: List<Class<out UUEvent>>
    
    /**
     * Handles an event.
     *
     * @param event The event to handle
     * @return `true` if the event was handled (processing will stop for this event),
     *         `false` if the event was not handled (processing continues to the next handler)
     */
    fun handleEvent(event: UUEvent): Boolean
}

/**
 * Install this handler for all event classes defined in [UUEventHandler.eventClasses].
 * The handler will be registered with [UUEventBus] and will receive events of the
 * types specified in [eventClasses].
 */
fun UUEventHandler.install()
{
    eventClasses.forEach { eventClass ->
        UUEventBus.installHandler(eventClass, this)
    }
}

/**
 * Uninstall this handler from all event classes defined in [UUEventHandler.eventClasses].
 * The handler will be removed from [UUEventBus] and will no longer receive events.
 */
fun UUEventHandler.uninstall()
{
    eventClasses.forEach { eventClass ->
        UUEventBus.uninstallHandler(eventClass, this)
    }
}

/**
 * Composable that automatically installs and uninstalls an event handler.
 * The handler is installed when this composable enters composition and uninstalled
 * when it leaves composition.
 */
@Composable
fun UUEventHandler.register()
{
    DisposableEffect(this)
    {
        install()
        onDispose {
            uninstall()
        }
    }
}

/**
 * Event handler for navigation events.
 * Handles [UUNavigationBackEvent] and [UUNavigationRouteEvent] by manipulating
 * the provided navigation back stack.
 *
 * @param backStack The navigation back stack to manipulate
 */
open class UUNavigationEventHandler(protected val backStack: NavBackStack<NavKey>) : UUEventHandler
{
    override val eventClasses: List<Class<out UUEvent>> = listOf(
        UUNavigationBackEvent::class.java,
        UUNavigationRouteEvent::class.java
    )

    override fun handleEvent(event: UUEvent): Boolean
    {
        when (event)
        {
            is UUNavigationRouteEvent -> backStack.add(event.route)
            is UUNavigationBackEvent -> backStack.removeLastOrNull()
        }

        return false
    }
}

/**
 * Event handler for activity-related events.
 * Handles [UUOpenSystemSettingsEvent] by opening the Android system settings.
 *
 * @param activity The activity to use for opening system settings
 */
open class UUActivityEventHandler(private val activity: Activity): UUEventHandler
{
    override val eventClasses: List<Class<out UUEvent>> = listOf(
        UUOpenSystemSettingsEvent::class.java
    )

    override fun handleEvent(event: UUEvent): Boolean
    {
        if (event is UUOpenSystemSettingsEvent)
        {
            activity.uuOpenSystemSettings()
            return true
        }

        return false
    }
}


/**
 * Event handler for drawer state events.
 * Handles [UUOpenDrawerStateEvent] by opening the drawer state asynchronously.
 *
 * @param scope The coroutine scope to launch the drawer open operation
 * @param drawerState The drawer state to open
 */
class UUDrawerStateEventHandler(
    private val scope: CoroutineScope,
    private val drawerState: DrawerState): UUEventHandler
{
    override val eventClasses: List<Class<out UUEvent>> = listOf(
        UUOpenDrawerStateEvent::class.java
    )

    override fun handleEvent(event: UUEvent): Boolean
    {
        scope.launch { drawerState.open() }
        return true
    }
}