package com.silverpine.uu.compose

import com.silverpine.uu.core.UUPrefs

/**
 * A [androidx.compose.runtime.MutableState] implementation that delegates Float value persistence to [UUPrefs].
 *
 * This class provides a convenient way to bind a Compose [androidx.compose.runtime.MutableState] to a Float value
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
 * val brightness = UUPrefBackedMutableFloat(
 *     prefs = prefs,
 *     key = "screen_brightness",
 *     defaultValue = 0.5f
 * )
 *
 * // Use in Compose
 * Slider(
 *     value = brightness.value,
 *     onValueChange = { brightness.value = it },
 *     valueRange = 0f..1f
 * )
 * ```
 */
class UUPrefBackedMutableFloat(
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
    defaultValue: Float
): UUDelegatedMutableState<Float>(
    defaultValue = defaultValue,
    getter =
        {
            prefs.getFloat(key, defaultValue)
        },
    setter =
        { value ->
            prefs.putFloat(key, value)
        }
)

