package com.silverpine.uu.compose

import com.silverpine.uu.core.UUPrefs

/**
 * A [androidx.compose.runtime.MutableState] implementation that delegates Boolean value persistence to [UUPrefs].
 *
 * This class provides a convenient way to bind a Compose [androidx.compose.runtime.MutableState] to a Boolean value
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
 * val isEnabled = UUPrefBackedMutableBoolean(
 *     prefs = prefs,
 *     key = "feature_enabled",
 *     defaultValue = false
 * )
 *
 * // Use in Compose
 * Switch(
 *     checked = isEnabled.value,
 *     onCheckedChange = { isEnabled.value = it }
 * )
 * ```
 */
class UUPrefBackedMutableBoolean(
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
    defaultValue: Boolean
): UUDelegatedMutableState<Boolean>(
    defaultValue = defaultValue,
    getter =
        {
            prefs.getBoolean(key, defaultValue)
        },
    setter =
        { value ->
            prefs.putBoolean(key, value)
        }
)