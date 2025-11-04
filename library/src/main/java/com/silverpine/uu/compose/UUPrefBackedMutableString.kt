package com.silverpine.uu.compose

import com.silverpine.uu.core.UUPrefs

/**
 * A [androidx.compose.runtime.MutableState] implementation that delegates String value persistence to [UUPrefs].
 *
 * This class provides a convenient way to bind a Compose [androidx.compose.runtime.MutableState] to a String value
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
 * val userName = UUPrefBackedMutableString(
 *     prefs = prefs,
 *     key = "user_name",
 *     defaultValue = ""
 * )
 *
 * // Use in Compose
 * TextField(
 *     value = userName.value,
 *     onValueChange = { userName.value = it },
 *     label = { Text("Username") }
 * )
 * ```
 */
class UUPrefBackedMutableString(
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
    defaultValue: String = ""
): UUDelegatedMutableState<String>(
    defaultValue = defaultValue,
    getter =
        {
            prefs.getString(key, defaultValue) ?: defaultValue
        },
    setter =
        { value ->
            prefs.putString(key, value)
        }
)

