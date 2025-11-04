package com.silverpine.uu.compose

import com.silverpine.uu.core.UUPrefs

/**
 * A [androidx.compose.runtime.MutableState] implementation that delegates Set&lt;String&gt; value persistence to [UUPrefs].
 *
 * This class provides a convenient way to bind a Compose [androidx.compose.runtime.MutableState] to a Set&lt;String&gt; value
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
 * val tags = UUPrefBackedMutableStringSet(
 *     prefs = prefs,
 *     key = "user_tags",
 *     defaultValue = emptySet()
 * )
 *
 * // Use in Compose
 * tags.value.forEach { tag ->
 *     Chip(
 *         onClick = { tags.value = tags.value - tag },
 *         label = { Text(tag) }
 *     )
 * }
 * ```
 */
class UUPrefBackedMutableStringSet(
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
    defaultValue: Set<String>
): UUDelegatedMutableState<Set<String>>(
    defaultValue = defaultValue,
    getter =
        {
            prefs.getStringSet(key, defaultValue) ?: defaultValue
        },
    setter =
        { value ->
            prefs.putStringSet(key, value)
        }
)

