package com.silverpine.uu.compose

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

/**
 * A [MutableState] implementation that delegates value retrieval and persistence to external functions.
 *
 * This class is useful when you need to integrate Compose state management with external data sources
 * such as SharedPreferences, ViewModels, data stores, or other persistence mechanisms. It maintains
 * an internal [MutableState] for Compose reactivity while delegating the actual storage and retrieval
 * to the provided getter and setter functions.
 *
 * @param T The type of value being managed.
 * @param defaultValue The default value to use if the getter cannot retrieve a value.
 * @param getter A function that retrieves the current value from the external source. It receives
 *               the [defaultValue] as a parameter and should return the current value, or the
 *               [defaultValue] if no value exists.
 * @param setter A function that persists the new value to the external source. This function is
 *               called whenever the [value] property is set.
 *
 * @sample
 * ```
 * // Example: Delegating to SharedPreferences
 * val preferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
 * val state = UUDelegatedMutableState(
 *     defaultValue = false,
 *     getter = { default -> preferences.getBoolean("key", default) },
 *     setter = { value -> preferences.edit().putBoolean("key", value).apply() }
 * )
 *
 * // Use in Compose
 * Checkbox(
 *     checked = state.value,
 *     onCheckedChange = { state.value = it }
 * )
 * ```
 */
open class UUDelegatedMutableState<T>(
    /**
     * The default value to use if the getter cannot retrieve a value.
     */
    defaultValue: T,
    /**
     * A function that retrieves the current value from the external source.
     */
    getter: (T)->T,
    /**
     * A function that persists the new value to the external source.
     */
    private val setter: (T)->Unit
) : MutableState<T>
{
    private val _state = mutableStateOf(getter(defaultValue))

    /**
     * The current value of this state.
     *
     * Getting this property returns the current value from the internal state.
     * Setting this property updates the internal state and calls the [setter] function
     * to persist the new value to the external source.
     */
    override var value: T
        get() = _state.value
        set(newValue)
        {
            _state.value = newValue
            setter(newValue)
        }

    /**
     * Provides destructuring support for the state value.
     *
     * Allows usage like: `val (currentValue, updateValue) = state`
     * where `currentValue` is the current state value and `updateValue` is a function
     * to update it.
     *
     * @return The current value of this state.
     */
    override fun component1(): T = value

    /**
     * Provides destructuring support for the state update function.
     *
     * Allows usage like: `val (currentValue, updateValue) = state`
     * where `currentValue` is the current state value and `updateValue` is a function
     * to update it.
     *
     * @return A function that updates the state value when called.
     */
    override fun component2(): (T) -> Unit = { newValue -> value = newValue }
}
