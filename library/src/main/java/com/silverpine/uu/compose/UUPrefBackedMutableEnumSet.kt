package com.silverpine.uu.compose

import com.silverpine.uu.core.UUPrefs

/**
 * A [androidx.compose.runtime.MutableState] implementation that delegates [Set] of Enum value persistence to [UUPrefs].
 *
 * This class provides a convenient way to bind a Compose [androidx.compose.runtime.MutableState] to a [Set] of Enum
 * values stored in [UUPrefs]. Values are persisted via [UUPrefs.getEnumSet] and [UUPrefs.putEnumSet]. When the state
 * value changes, it is automatically persisted; when initialized, the value is loaded from [UUPrefs].
 *
 * @param T The enum type whose set is being managed.
 * @param prefs The [UUPrefs] instance to use for persistence.
 * @param key The key to use for storing and retrieving the value in [UUPrefs].
 * @param enumClass The enum class for type [T].
 * @param defaultValue The default value to use if no value exists in [UUPrefs] for the given [key],
 *                    or if the stored set is null.
 *
 * @sample
 * ```
 * enum class Category { SPORTS, NEWS, ENTERTAINMENT }
 *
 * // Initialize with UUPrefs
 * val prefs = UUPrefs.getDefault()
 * val selectedCategories = UUPrefBackedMutableEnumSet(
 *     prefs = prefs,
 *     key = "selected_categories",
 *     enumClass = Category::class.java,
 *     defaultValue = emptySet()
 * )
 *
 * // Use in Compose
 * Category.values().forEach { category ->
 *     FilterChip(
 *         selected = category in selectedCategories.value,
 *         onClick = {
 *             selectedCategories.value = if (category in selectedCategories.value)
 *                 selectedCategories.value - category
 *             else
 *                 selectedCategories.value + category
 *         },
 *         label = { Text(category.name) }
 *     )
 * }
 * ```
 */
class UUPrefBackedMutableEnumSet<T : Enum<T>>(
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
     * The default value to use if no value exists in [UUPrefs] for the given [key], or if the stored set is null.
     */
    defaultValue: Set<T> = emptySet()
): UUDelegatedMutableState<Set<T>>(
    defaultValue = defaultValue,
    getter =
        { default ->
            prefs.getEnumSet(key, enumClass, default) ?: default
        },
    setter =
        { value ->
            prefs.putEnumSet(key, value)
        }
)
