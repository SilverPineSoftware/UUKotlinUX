package com.silverpine.uu.compose

import com.silverpine.uu.core.UUPrefs

/**
 * A [androidx.compose.runtime.MutableState] implementation that delegates Long value persistence to [UUPrefs].
 *
 * This class provides a convenient way to bind a Compose [androidx.compose.runtime.MutableState] to a Long value
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
 * val timestamp = UUPrefBackedMutableLong(
 *     prefs = prefs,
 *     key = "last_sync_time",
 *     defaultValue = 0L
 * )
 *
 * // Use in Compose
 * Text(
 *     text = "Last sync: ${Date(timestamp.value)}"
 * )
 * ```
 */
class UUPrefBackedMutableLong(
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
    defaultValue: Long = 0L
): UUDelegatedMutableState<Long>(
    defaultValue = defaultValue,
    getter =
        {
            prefs.getLong(key, defaultValue)
        },
    setter =
        { value ->
            prefs.putLong(key, value)
        }
)

