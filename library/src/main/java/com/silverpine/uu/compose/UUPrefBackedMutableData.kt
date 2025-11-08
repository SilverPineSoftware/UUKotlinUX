package com.silverpine.uu.compose

import com.silverpine.uu.core.UUPrefs

/**
 * A [androidx.compose.runtime.MutableState] implementation that delegates ByteArray value persistence to [UUPrefs].
 *
 * This class provides a convenient way to bind a Compose [androidx.compose.runtime.MutableState] to a ByteArray value
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
 * val encryptedData = UUPrefBackedMutableData(
 *     prefs = prefs,
 *     key = "encrypted_token",
 *     defaultValue = ByteArray(0)
 * )
 *
 * // Use in Compose
 * if (encryptedData.value.isNotEmpty()) {
 *     Text("Data loaded: ${encryptedData.value.size} bytes")
 * }
 * ```
 */
class UUPrefBackedMutableData(
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
    defaultValue: ByteArray = ByteArray(0)
): UUDelegatedMutableState<ByteArray>(
    defaultValue = defaultValue,
    getter =
        { default ->
            prefs.getData(key, default) ?: default
        },
    setter =
        { value ->
            prefs.putData(key, value)
        }
)

