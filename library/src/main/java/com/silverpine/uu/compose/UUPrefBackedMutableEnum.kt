package com.silverpine.uu.compose

import com.silverpine.uu.core.UUPrefs

/**
 * A [androidx.compose.runtime.MutableState] implementation that delegates Enum value persistence to [UUPrefs].
 *
 * This class provides a convenient way to bind a Compose [androidx.compose.runtime.MutableState] to an Enum value
 * stored in [UUPrefs]. When the state value changes, it is automatically persisted to
 * [UUPrefs], and when initialized, the value is loaded from [UUPrefs].
 *
 * @param T The enum type being managed.
 * @param prefs The [UUPrefs] instance to use for persistence.
 * @param key The key to use for storing and retrieving the value in [UUPrefs].
 * @param enumClass The enum class for type [T].
 * @param defaultValue The default value to use if no value exists in [UUPrefs] for the given [key],
 *                    or if the stored value cannot be converted to the enum type.
 *
 * @sample
 * ```
 * enum class Theme { LIGHT, DARK, SYSTEM }
 *
 * // Initialize with UUPrefs
 * val prefs = UUPrefs.getDefault()
 * val theme = UUPrefBackedMutableEnum(
 *     prefs = prefs,
 *     key = "app_theme",
 *     enumClass = Theme::class.java,
 *     defaultValue = Theme.SYSTEM
 * )
 *
 * // Use in Compose
 * DropdownMenu(
 *     expanded = expanded,
 *     onDismissRequest = { expanded = false }
 * ) {
 *     Theme.values().forEach { themeOption ->
 *         DropdownMenuItem(
 *             text = { Text(themeOption.name) },
 *             onClick = {
 *                 theme.value = themeOption
 *                 expanded = false
 *             }
 *         )
 *     }
 * }
 * ```
 */
class UUPrefBackedMutableEnum<T : Enum<T>>(
    /**
     * The [UUPrefs] instance to use for persistence.
     */
    prefs: UUPrefs,
    /**
     * The key to use for storing and retrieving the value in [UUPrefs].
     */
    key: String,
    /**
     * The enum class for type [T].
     */
    enumClass: Class<T>,
    /**
     * The default value to use if no value exists in [UUPrefs] for the given [key],
     * or if the stored value cannot be converted to the enum type.
     */
    defaultValue: T
): UUDelegatedMutableState<T>(
    defaultValue = defaultValue,
    getter =
        { default ->
            prefs.getEnum(key, enumClass, default) ?: default
        },
    setter =
        { value ->
            prefs.putEnum(key, value)
        }
)

