package com.silverpine.uu.compose

import com.silverpine.uu.core.UUPrefs

/**
 * A [androidx.compose.runtime.MutableState] implementation that delegates Int value persistence to [UUPrefs].
 *
 * This class provides a convenient way to bind a Compose [androidx.compose.runtime.MutableState] to an Int value
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
 * val fontSize = UUPrefBackedMutableInt(
 *     prefs = prefs,
 *     key = "font_size",
 *     defaultValue = 14
 * )
 *
 * // Use in Compose
 * Text(
 *     text = "Hello",
 *     fontSize = fontSize.value.sp
 * )
 * ```
 */
class UUPrefBackedMutableInt(
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
    defaultValue: Int = 0
): UUDelegatedMutableState<Int>(
    defaultValue = defaultValue,
    getter =
        {
            prefs.getInt(key, defaultValue)
        },
    setter =
        { value ->
            prefs.putInt(key, value)
        }
)

