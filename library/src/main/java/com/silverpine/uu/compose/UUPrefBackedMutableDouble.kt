package com.silverpine.uu.compose

import com.silverpine.uu.core.UUPrefs

/**
 * A [androidx.compose.runtime.MutableState] implementation that delegates Double value persistence to [UUPrefs].
 *
 * This class provides a convenient way to bind a Compose [androidx.compose.runtime.MutableState] to a Double value
 * stored in [UUPrefs]. When the state value changes, it is automatically persisted to
 * [UUPrefs], and when initialized, the value is loaded from [UUPrefs].
 *
 * @param prefs The [UUPrefs] instance to use for persistence.
 * @param key The key to use for storing and retrieving the value in [UUPrefs].
 * @param defaultValue The default value to use if no value exists in [UUPrefs] for the given [key].
 *
 * @sample
 * ```
 * // Initialize with UUPrefs
 * val prefs = UUPrefs.getDefault()
 * val precision = UUPrefBackedMutableDouble(
 *     prefs = prefs,
 *     key = "calculation_precision",
 *     defaultValue = 0.0001
 * )
 *
 * // Use in Compose
 * Slider(
 *     value = precision.value,
 *     onValueChange = { precision.value = it },
 *     valueRange = 0.0..1.0
 * )
 * ```
 */
class UUPrefBackedMutableDouble(
    /**
     * The [UUPrefs] instance to use for persistence.
     */
    prefs: UUPrefs,
    /**
     * The key to use for storing and retrieving the value in [UUPrefs].
     */
    key: String,
    /**
     * The default value to use if no value exists in [UUPrefs] for the given [key].
     */
    defaultValue: Double = 0.0
): UUDelegatedMutableState<Double>(
    defaultValue = defaultValue,
    getter =
        {
            prefs.getDouble(key, defaultValue)
        },
    setter =
        { value ->
            prefs.putDouble(key, value)
        }
)

