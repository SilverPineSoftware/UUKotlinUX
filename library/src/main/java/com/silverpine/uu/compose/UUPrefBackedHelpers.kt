package com.silverpine.uu.compose

import androidx.compose.runtime.MutableState
import com.silverpine.uu.core.UUPrefs

/**
 * Helper function to create a [MutableState] backed by [UUPrefs] for Boolean values.
 *
 * This is a convenience function that creates a [UUPrefBackedMutableBoolean] instance.
 * The state value is automatically persisted to [UUPrefs] when changed, and loaded from
 * [UUPrefs] when initialized.
 *
 * @param prefs The [UUPrefs] instance to use for persistence.
 * @param key The key to use for storing and retrieving the value in [UUPrefs].
 * @param defaultValue The default value to use if no value exists in [UUPrefs] for the given [key].
 * @return A [MutableState] that persists Boolean values to [UUPrefs].
 *
 * @sample
 * ```
 * val prefs = UUPrefs.getDefault()
 * val isEnabled = uuPrefBackedMutableBoolean(prefs, "feature_enabled", false)
 *
 * Switch(
 *     checked = isEnabled.value,
 *     onCheckedChange = { isEnabled.value = it }
 * )
 * ```
 */
fun uuPrefBackedMutableBoolean(prefs: UUPrefs, key: String, defaultValue: Boolean = false): MutableState<Boolean> =
    UUPrefBackedMutableBoolean(prefs, key, defaultValue)

/**
 * Helper function to create a [MutableState] backed by [UUPrefs] for Int values.
 *
 * This is a convenience function that creates a [UUPrefBackedMutableInt] instance.
 * The state value is automatically persisted to [UUPrefs] when changed, and loaded from
 * [UUPrefs] when initialized.
 *
 * @param prefs The [UUPrefs] instance to use for persistence.
 * @param key The key to use for storing and retrieving the value in [UUPrefs].
 * @param defaultValue The default value to use if no value exists in [UUPrefs] for the given [key].
 * @return A [MutableState] that persists Int values to [UUPrefs].
 *
 * @sample
 * ```
 * val prefs = UUPrefs.getDefault()
 * val fontSize = uuPrefBackedMutableInt(prefs, "font_size", 14)
 *
 * Text(
 *     text = "Hello",
 *     fontSize = fontSize.value.sp
 * )
 * ```
 */
fun uuPrefBackedMutableInt(prefs: UUPrefs, key: String, defaultValue: Int = 0): MutableState<Int> =
    UUPrefBackedMutableInt(prefs, key, defaultValue)

/**
 * Helper function to create a [MutableState] backed by [UUPrefs] for Long values.
 *
 * This is a convenience function that creates a [UUPrefBackedMutableLong] instance.
 * The state value is automatically persisted to [UUPrefs] when changed, and loaded from
 * [UUPrefs] when initialized.
 *
 * @param prefs The [UUPrefs] instance to use for persistence.
 * @param key The key to use for storing and retrieving the value in [UUPrefs].
 * @param defaultValue The default value to use if no value exists in [UUPrefs] for the given [key].
 * @return A [MutableState] that persists Long values to [UUPrefs].
 *
 * @sample
 * ```
 * val prefs = UUPrefs.getDefault()
 * val timestamp = uuPrefBackedMutableLong(prefs, "last_sync_time", 0L)
 *
 * Text(
 *     text = "Last sync: ${Date(timestamp.value)}"
 * )
 * ```
 */
fun uuPrefBackedMutableLong(prefs: UUPrefs, key: String, defaultValue: Long = 0L): MutableState<Long> =
    UUPrefBackedMutableLong(prefs, key, defaultValue)

/**
 * Helper function to create a [MutableState] backed by [UUPrefs] for Float values.
 *
 * This is a convenience function that creates a [UUPrefBackedMutableFloat] instance.
 * The state value is automatically persisted to [UUPrefs] when changed, and loaded from
 * [UUPrefs] when initialized.
 *
 * @param prefs The [UUPrefs] instance to use for persistence.
 * @param key The key to use for storing and retrieving the value in [UUPrefs].
 * @param defaultValue The default value to use if no value exists in [UUPrefs] for the given [key].
 * @return A [MutableState] that persists Float values to [UUPrefs].
 *
 * @sample
 * ```
 * val prefs = UUPrefs.getDefault()
 * val brightness = uuPrefBackedMutableFloat(prefs, "screen_brightness", 0.5f)
 *
 * Slider(
 *     value = brightness.value,
 *     onValueChange = { brightness.value = it },
 *     valueRange = 0f..1f
 * )
 * ```
 */
fun uuPrefBackedMutableFloat(prefs: UUPrefs, key: String, defaultValue: Float = 0f): MutableState<Float> =
    UUPrefBackedMutableFloat(prefs, key, defaultValue)

/**
 * Helper function to create a [MutableState] backed by [UUPrefs] for Double values.
 *
 * This is a convenience function that creates a [UUPrefBackedMutableDouble] instance.
 * The state value is automatically persisted to [UUPrefs] when changed, and loaded from
 * [UUPrefs] when initialized.
 *
 * @param prefs The [UUPrefs] instance to use for persistence.
 * @param key The key to use for storing and retrieving the value in [UUPrefs].
 * @param defaultValue The default value to use if no value exists in [UUPrefs] for the given [key].
 * @return A [MutableState] that persists Double values to [UUPrefs].
 *
 * @sample
 * ```
 * val prefs = UUPrefs.getDefault()
 * val precision = uuPrefBackedMutableDouble(prefs, "calculation_precision", 0.0001)
 *
 * Slider(
 *     value = precision.value,
 *     onValueChange = { precision.value = it },
 *     valueRange = 0.0..1.0
 * )
 * ```
 */
fun uuPrefBackedMutableDouble(prefs: UUPrefs, key: String, defaultValue: Double = 0.0): MutableState<Double> =
    UUPrefBackedMutableDouble(prefs, key, defaultValue)

/**
 * Helper function to create a [MutableState] backed by [UUPrefs] for String values.
 *
 * This is a convenience function that creates a [UUPrefBackedMutableString] instance.
 * The state value is automatically persisted to [UUPrefs] when changed, and loaded from
 * [UUPrefs] when initialized.
 *
 * @param prefs The [UUPrefs] instance to use for persistence.
 * @param key The key to use for storing and retrieving the value in [UUPrefs].
 * @param defaultValue The default value to use if no value exists in [UUPrefs] for the given [key].
 * @return A [MutableState] that persists String values to [UUPrefs].
 *
 * @sample
 * ```
 * val prefs = UUPrefs.getDefault()
 * val userName = uuPrefBackedMutableString(prefs, "user_name", "")
 *
 * TextField(
 *     value = userName.value,
 *     onValueChange = { userName.value = it },
 *     label = { Text("Username") }
 * )
 * ```
 */
fun uuPrefBackedMutableString(prefs: UUPrefs, key: String, defaultValue: String = ""): MutableState<String> =
    UUPrefBackedMutableString(prefs, key, defaultValue)

/**
 * Helper function to create a [MutableState] backed by [UUPrefs] for Set<String> values.
 *
 * This is a convenience function that creates a [UUPrefBackedMutableStringSet] instance.
 * The state value is automatically persisted to [UUPrefs] when changed, and loaded from
 * [UUPrefs] when initialized.
 *
 * @param prefs The [UUPrefs] instance to use for persistence.
 * @param key The key to use for storing and retrieving the value in [UUPrefs].
 * @param defaultValue The default value to use if no value exists in [UUPrefs] for the given [key].
 * @return A [MutableState] that persists Set<String> values to [UUPrefs].
 *
 * @sample
 * ```
 * val prefs = UUPrefs.getDefault()
 * val tags = uuPrefBackedMutableStringSet(prefs, "user_tags", emptySet())
 *
 * tags.value.forEach { tag ->
 *     Chip(
 *         label = { Text(tag) },
 *         onDismiss = { tags.value = tags.value - tag }
 *     )
 * }
 * ```
 */
fun uuPrefBackedMutableStringSet(prefs: UUPrefs, key: String, defaultValue: Set<String> = emptySet()): MutableState<Set<String>> =
    UUPrefBackedMutableStringSet(prefs, key, defaultValue)

/**
 * Helper function to create a [MutableState] backed by [UUPrefs] for Enum values.
 *
 * This is a convenience function that creates a [UUPrefBackedMutableEnum] instance.
 * The state value is automatically persisted to [UUPrefs] when changed, and loaded from
 * [UUPrefs] when initialized.
 *
 * @param T The enum type being managed.
 * @param prefs The [UUPrefs] instance to use for persistence.
 * @param key The key to use for storing and retrieving the value in [UUPrefs].
 * @param enumClass The enum class for type [T].
 * @param defaultValue The default value to use if no value exists in [UUPrefs] for the given [key],
 *                    or if the stored value cannot be converted to the enum type.
 * @return A [MutableState] that persists Enum values to [UUPrefs].
 *
 * @sample
 * ```
 * enum class Theme { LIGHT, DARK, SYSTEM }
 *
 * val prefs = UUPrefs.getDefault()
 * val theme = uuPrefBackedMutableEnum(prefs, "app_theme", Theme::class.java, Theme.SYSTEM)
 *
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
fun <T : Enum<T>> uuPrefBackedMutableEnum(prefs: UUPrefs, key: String, enumClass: Class<T>, defaultValue: T): MutableState<T> =
    UUPrefBackedMutableEnum(prefs, key, enumClass, defaultValue)

/**
 * Helper function to create a [MutableState] backed by [UUPrefs] for ByteArray values.
 *
 * This is a convenience function that creates a [UUPrefBackedMutableData] instance.
 * The state value is automatically persisted to [UUPrefs] when changed, and loaded from
 * [UUPrefs] when initialized.
 *
 * @param prefs The [UUPrefs] instance to use for persistence.
 * @param key The key to use for storing and retrieving the value in [UUPrefs].
 * @param defaultValue The default value to use if no value exists in [UUPrefs] for the given [key].
 * @return A [MutableState] that persists ByteArray values to [UUPrefs].
 *
 * @sample
 * ```
 * val prefs = UUPrefs.getDefault()
 * val encryptedData = uuPrefBackedMutableData(prefs, "encrypted_token", ByteArray(0))
 *
 * if (encryptedData.value.isNotEmpty()) {
 *     Text("Data loaded: ${encryptedData.value.size} bytes")
 * }
 * ```
 */
fun uuPrefBackedMutableData(prefs: UUPrefs, key: String, defaultValue: ByteArray = ByteArray(0)): MutableState<ByteArray> =
    UUPrefBackedMutableData(prefs, key, defaultValue)

