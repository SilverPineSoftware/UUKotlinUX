package com.silverpine.uu.ux.events

import androidx.navigation3.runtime.NavKey

/**
 * Base class for all events in the UU event system.
 * Events are posted to [UUEventBus] and processed by [UUEventHandler] implementations.
 *
 * Subclass this to create custom event types for your application.
 */
open class UUEvent

/**
 * Event posted to navigate back in the navigation stack.
 * When this event is posted, handlers should call the appropriate navigation back method.
 */
object UUNavigationBackEvent: UUEvent()

/**
 * Event posted to navigate to a specific route.
 *
 * @param route The navigation route to navigate to
 */
data class UUNavigationRouteEvent(val route: NavKey): UUEvent()

/**
 * Event posted to open the system settings.
 * Typically handled by [UUActivityEventHandler] which opens the Android system settings.
 */
object UUOpenSystemSettingsEvent: UUEvent()

/**
 * Event posted to open a drawer state (e.g., navigation drawer).
 * Typically handled by [UUDrawerStateEventHandler] which opens the drawer.
 */
object UUOpenDrawerStateEvent: UUEvent()
