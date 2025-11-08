package com.silverpine.uu.compose.test

import com.silverpine.uu.compose.UUPrefBackedMutableBoolean
import com.silverpine.uu.compose.UUPrefBackedMutableData
import com.silverpine.uu.compose.UUPrefBackedMutableDouble
import com.silverpine.uu.compose.UUPrefBackedMutableEnum
import com.silverpine.uu.compose.UUPrefBackedMutableFloat
import com.silverpine.uu.compose.UUPrefBackedMutableInt
import com.silverpine.uu.compose.UUPrefBackedMutableLong
import com.silverpine.uu.compose.UUPrefBackedMutableString
import com.silverpine.uu.compose.UUPrefBackedMutableStringSet
import com.silverpine.uu.compose.uuPrefBackedMutableBoolean
import com.silverpine.uu.compose.uuPrefBackedMutableData
import com.silverpine.uu.compose.uuPrefBackedMutableDouble
import com.silverpine.uu.compose.uuPrefBackedMutableEnum
import com.silverpine.uu.compose.uuPrefBackedMutableFloat
import com.silverpine.uu.compose.uuPrefBackedMutableInt
import com.silverpine.uu.compose.uuPrefBackedMutableLong
import com.silverpine.uu.compose.uuPrefBackedMutableString
import com.silverpine.uu.compose.uuPrefBackedMutableStringSet
import com.silverpine.uu.core.UUPrefs
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension

/**
 * JUnit 5 unit tests for UUPrefBackedMutableState classes.
 *
 * These tests verify that the UUPrefBacked classes correctly:
 * - Initialize with default values when no value exists in UUPrefs
 * - Load values from UUPrefs when they exist
 * - Persist values to UUPrefs when they are set
 * - Maintain state correctly as MutableState implementations
 */
@ExtendWith(MockitoExtension::class)
class UUPrefBackedMutableStateTests
{
    private fun createMockUUPrefs(): UUPrefs
    {
        return Mockito.mock(UUPrefs::class.java)
    }

    // ==================== UUPrefBackedMutableBoolean Tests ====================

    @Test
    fun `UUPrefBackedMutableBoolean uses default value when no value exists`()
    {
        // Given: UUPrefs with no existing value
        val prefs = createMockUUPrefs()
        val key = "test_boolean_key"
        val defaultValue = false

        Mockito.`when`(prefs.getBoolean(key, defaultValue)).thenReturn(defaultValue)

        // When: Creating UUPrefBackedMutableBoolean
        val state = UUPrefBackedMutableBoolean(prefs, key, defaultValue)

        // Then: Should use default value
        assertEquals(defaultValue, state.value)
        Mockito.verify(prefs, Mockito.times(1)).getBoolean(key, defaultValue)
    }

    @Test
    fun `UUPrefBackedMutableBoolean loads existing value from UUPrefs`()
    {
        // Given: UUPrefs with existing value
        val prefs = createMockUUPrefs()
        val key = "test_boolean_key"
        val defaultValue = false
        val existingValue = true

        Mockito.`when`(prefs.getBoolean(key, defaultValue)).thenReturn(existingValue)

        // When: Creating UUPrefBackedMutableBoolean
        val state = UUPrefBackedMutableBoolean(prefs, key, defaultValue)

        // Then: Should load existing value
        assertEquals(existingValue, state.value)
        Mockito.verify(prefs, Mockito.times(1)).getBoolean(key, defaultValue)
    }

    @Test
    fun `UUPrefBackedMutableBoolean persists value when set`()
    {
        // Given: UUPrefBackedMutableBoolean
        val prefs = createMockUUPrefs()
        val key = "test_boolean_key"
        val defaultValue = false
        val newValue = true

        Mockito.`when`(prefs.getBoolean(key, defaultValue)).thenReturn(defaultValue)

        val state = UUPrefBackedMutableBoolean(prefs, key, defaultValue)

        // When: Setting a new value
        state.value = newValue

        // Then: Should persist to UUPrefs
        assertEquals(newValue, state.value)
        Mockito.verify(prefs, Mockito.times(1)).putBoolean(key, newValue)
    }

    @Test
    fun `UUPrefBackedMutableBoolean supports destructuring`()
    {
        // Given: UUPrefBackedMutableBoolean
        val prefs = createMockUUPrefs()
        val key = "test_boolean_key"
        val defaultValue = false

        Mockito.`when`(prefs.getBoolean(key, defaultValue)).thenReturn(defaultValue)

        val state = UUPrefBackedMutableBoolean(prefs, key, defaultValue)

        // When: Destructuring
        val (value, update) = state

        // Then: Should work correctly
        assertEquals(defaultValue, value)
        update(true)
        assertEquals(true, state.value)
        Mockito.verify(prefs, Mockito.times(1)).putBoolean(key, true)
    }

    // ==================== UUPrefBackedMutableInt Tests ====================

    @Test
    fun `UUPrefBackedMutableInt uses default value when no value exists`()
    {
        // Given: UUPrefs with no existing value
        val prefs = createMockUUPrefs()
        val key = "test_int_key"
        val defaultValue = 0

        Mockito.`when`(prefs.getInt(key, defaultValue)).thenReturn(defaultValue)

        // When: Creating UUPrefBackedMutableInt
        val state = UUPrefBackedMutableInt(prefs, key, defaultValue)

        // Then: Should use default value
        assertEquals(defaultValue, state.value)
        Mockito.verify(prefs, Mockito.times(1)).getInt(key, defaultValue)
    }

    @Test
    fun `UUPrefBackedMutableInt loads existing value from UUPrefs`()
    {
        // Given: UUPrefs with existing value
        val prefs = createMockUUPrefs()
        val key = "test_int_key"
        val defaultValue = 0
        val existingValue = 42

        Mockito.`when`(prefs.getInt(key, defaultValue)).thenReturn(existingValue)

        // When: Creating UUPrefBackedMutableInt
        val state = UUPrefBackedMutableInt(prefs, key, defaultValue)

        // Then: Should load existing value
        assertEquals(existingValue, state.value)
        Mockito.verify(prefs, Mockito.times(1)).getInt(key, defaultValue)
    }

    @Test
    fun `UUPrefBackedMutableInt persists value when set`()
    {
        // Given: UUPrefBackedMutableInt
        val prefs = createMockUUPrefs()
        val key = "test_int_key"
        val defaultValue = 0
        val newValue = 100

        Mockito.`when`(prefs.getInt(key, defaultValue)).thenReturn(defaultValue)

        val state = UUPrefBackedMutableInt(prefs, key, defaultValue)

        // When: Setting a new value
        state.value = newValue

        // Then: Should persist to UUPrefs
        assertEquals(newValue, state.value)
        Mockito.verify(prefs, Mockito.times(1)).putInt(key, newValue)
    }

    // ==================== UUPrefBackedMutableLong Tests ====================

    @Test
    fun `UUPrefBackedMutableLong uses default value when no value exists`()
    {
        // Given: UUPrefs with no existing value
        val prefs = createMockUUPrefs()
        val key = "test_long_key"
        val defaultValue = 0L

        Mockito.`when`(prefs.getLong(key, defaultValue)).thenReturn(defaultValue)

        // When: Creating UUPrefBackedMutableLong
        val state = UUPrefBackedMutableLong(prefs, key, defaultValue)

        // Then: Should use default value
        assertEquals(defaultValue, state.value)
        Mockito.verify(prefs, Mockito.times(1)).getLong(key, defaultValue)
    }

    @Test
    fun `UUPrefBackedMutableLong loads existing value from UUPrefs`()
    {
        // Given: UUPrefs with existing value
        val prefs = createMockUUPrefs()
        val key = "test_long_key"
        val defaultValue = 0L
        val existingValue = 123456789L

        Mockito.`when`(prefs.getLong(key, defaultValue)).thenReturn(existingValue)

        // When: Creating UUPrefBackedMutableLong
        val state = UUPrefBackedMutableLong(prefs, key, defaultValue)

        // Then: Should load existing value
        assertEquals(existingValue, state.value)
        Mockito.verify(prefs, Mockito.times(1)).getLong(key, defaultValue)
    }

    @Test
    fun `UUPrefBackedMutableLong persists value when set`()
    {
        // Given: UUPrefBackedMutableLong
        val prefs = createMockUUPrefs()
        val key = "test_long_key"
        val defaultValue = 0L
        val newValue = 987654321L

        Mockito.`when`(prefs.getLong(key, defaultValue)).thenReturn(defaultValue)

        val state = UUPrefBackedMutableLong(prefs, key, defaultValue)

        // When: Setting a new value
        state.value = newValue

        // Then: Should persist to UUPrefs
        assertEquals(newValue, state.value)
        Mockito.verify(prefs, Mockito.times(1)).putLong(key, newValue)
    }

    // ==================== UUPrefBackedMutableFloat Tests ====================

    @Test
    fun `UUPrefBackedMutableFloat uses default value when no value exists`()
    {
        // Given: UUPrefs with no existing value
        val prefs = createMockUUPrefs()
        val key = "test_float_key"
        val defaultValue = 0.0f

        Mockito.`when`(prefs.getFloat(key, defaultValue)).thenReturn(defaultValue)

        // When: Creating UUPrefBackedMutableFloat
        val state = UUPrefBackedMutableFloat(prefs, key, defaultValue)

        // Then: Should use default value
        assertEquals(defaultValue, state.value, 0.001f)
        Mockito.verify(prefs, Mockito.times(1)).getFloat(key, defaultValue)
    }

    @Test
    fun `UUPrefBackedMutableFloat loads existing value from UUPrefs`()
    {
        // Given: UUPrefs with existing value
        val prefs = createMockUUPrefs()
        val key = "test_float_key"
        val defaultValue = 0.0f
        val existingValue = 3.14159f

        Mockito.`when`(prefs.getFloat(key, defaultValue)).thenReturn(existingValue)

        // When: Creating UUPrefBackedMutableFloat
        val state = UUPrefBackedMutableFloat(prefs, key, defaultValue)

        // Then: Should load existing value
        assertEquals(existingValue, state.value, 0.001f)
        Mockito.verify(prefs, Mockito.times(1)).getFloat(key, defaultValue)
    }

    @Test
    fun `UUPrefBackedMutableFloat persists value when set`()
    {
        // Given: UUPrefBackedMutableFloat
        val prefs = createMockUUPrefs()
        val key = "test_float_key"
        val defaultValue = 0.0f
        val newValue = 2.71828f

        Mockito.`when`(prefs.getFloat(key, defaultValue)).thenReturn(defaultValue)

        val state = UUPrefBackedMutableFloat(prefs, key, defaultValue)

        // When: Setting a new value
        state.value = newValue

        // Then: Should persist to UUPrefs
        assertEquals(newValue, state.value, 0.001f)
        Mockito.verify(prefs, Mockito.times(1)).putFloat(key, newValue)
    }

    // ==================== UUPrefBackedMutableString Tests ====================

    @Test
    fun `UUPrefBackedMutableString uses default value when no value exists`()
    {
        // Given: UUPrefs with no existing value
        val prefs = createMockUUPrefs()
        val key = "test_string_key"
        val defaultValue = ""

        Mockito.`when`(prefs.getString(key, defaultValue)).thenReturn(null)

        // When: Creating UUPrefBackedMutableString
        val state = UUPrefBackedMutableString(prefs, key, defaultValue)

        // Then: Should use default value
        assertEquals(defaultValue, state.value)
        Mockito.verify(prefs, Mockito.times(1)).getString(key, defaultValue)
    }

    @Test
    fun `UUPrefBackedMutableString uses default value when UUPrefs returns null`()
    {
        // Given: UUPrefs returns null
        val prefs = createMockUUPrefs()
        val key = "test_string_key"
        val defaultValue = "default"

        Mockito.`when`(prefs.getString(key, defaultValue)).thenReturn(null)

        // When: Creating UUPrefBackedMutableString
        val state = UUPrefBackedMutableString(prefs, key, defaultValue)

        // Then: Should use default value
        assertEquals(defaultValue, state.value)
    }

    @Test
    fun `UUPrefBackedMutableString loads existing value from UUPrefs`()
    {
        // Given: UUPrefs with existing value
        val prefs = createMockUUPrefs()
        val key = "test_string_key"
        val defaultValue = ""
        val existingValue = "Hello, World!"

        Mockito.`when`(prefs.getString(key, defaultValue)).thenReturn(existingValue)

        // When: Creating UUPrefBackedMutableString
        val state = UUPrefBackedMutableString(prefs, key, defaultValue)

        // Then: Should load existing value
        assertEquals(existingValue, state.value)
        Mockito.verify(prefs, Mockito.times(1)).getString(key, defaultValue)
    }

    @Test
    fun `UUPrefBackedMutableString persists value when set`()
    {
        // Given: UUPrefBackedMutableString
        val prefs = createMockUUPrefs()
        val key = "test_string_key"
        val defaultValue = ""
        val newValue = "Updated Value"

        Mockito.`when`(prefs.getString(key, defaultValue)).thenReturn(null)

        val state = UUPrefBackedMutableString(prefs, key, defaultValue)

        // When: Setting a new value
        state.value = newValue

        // Then: Should persist to UUPrefs
        assertEquals(newValue, state.value)
        Mockito.verify(prefs, Mockito.times(1)).putString(key, newValue)
    }

    @Test
    fun `UUPrefBackedMutableString handles empty string`()
    {
        // Given: UUPrefs with empty string
        val prefs = createMockUUPrefs()
        val key = "test_string_key"
        val defaultValue = "default"

        Mockito.`when`(prefs.getString(key, defaultValue)).thenReturn("")

        // When: Creating UUPrefBackedMutableString
        val state = UUPrefBackedMutableString(prefs, key, defaultValue)

        // Then: Should use empty string (not default)
        assertEquals("", state.value)
    }

    // ==================== UUPrefBackedMutableStringSet Tests ====================

    @Test
    fun `UUPrefBackedMutableStringSet uses default value when no value exists`()
    {
        // Given: UUPrefs with no existing value
        val prefs = createMockUUPrefs()
        val key = "test_string_set_key"
        val defaultValue = emptySet<String>()

        Mockito.`when`(prefs.getStringSet(key, defaultValue)).thenReturn(null)

        // When: Creating UUPrefBackedMutableStringSet
        val state = UUPrefBackedMutableStringSet(prefs, key, defaultValue)

        // Then: Should use default value
        assertEquals(defaultValue, state.value)
        Mockito.verify(prefs, Mockito.times(1)).getStringSet(key, defaultValue)
    }

    @Test
    fun `UUPrefBackedMutableStringSet uses default value when UUPrefs returns null`()
    {
        // Given: UUPrefs returns null
        val prefs = createMockUUPrefs()
        val key = "test_string_set_key"
        val defaultValue = setOf("default1", "default2")

        Mockito.`when`(prefs.getStringSet(key, defaultValue)).thenReturn(null)

        // When: Creating UUPrefBackedMutableStringSet
        val state = UUPrefBackedMutableStringSet(prefs, key, defaultValue)

        // Then: Should use default value
        assertEquals(defaultValue, state.value)
    }

    @Test
    fun `UUPrefBackedMutableStringSet loads existing value from UUPrefs`()
    {
        // Given: UUPrefs with existing value
        val prefs = createMockUUPrefs()
        val key = "test_string_set_key"
        val defaultValue = emptySet<String>()
        val existingValue = setOf("tag1", "tag2", "tag3")

        Mockito.`when`(prefs.getStringSet(key, defaultValue)).thenReturn(existingValue)

        // When: Creating UUPrefBackedMutableStringSet
        val state = UUPrefBackedMutableStringSet(prefs, key, defaultValue)

        // Then: Should load existing value
        assertEquals(existingValue, state.value)
        assertEquals(3, state.value.size)
        assertTrue(state.value.contains("tag1"))
        assertTrue(state.value.contains("tag2"))
        assertTrue(state.value.contains("tag3"))
        Mockito.verify(prefs, Mockito.times(1)).getStringSet(key, defaultValue)
    }

    @Test
    fun `UUPrefBackedMutableStringSet persists value when set`()
    {
        // Given: UUPrefBackedMutableStringSet
        val prefs = createMockUUPrefs()
        val key = "test_string_set_key"
        val defaultValue = emptySet<String>()
        val newValue = setOf("new1", "new2")

        Mockito.`when`(prefs.getStringSet(key, defaultValue)).thenReturn(null)

        val state = UUPrefBackedMutableStringSet(prefs, key, defaultValue)

        // When: Setting a new value
        state.value = newValue

        // Then: Should persist to UUPrefs
        assertEquals(newValue, state.value)
        assertEquals(2, state.value.size)
        Mockito.verify(prefs, Mockito.times(1)).putStringSet(key, newValue)
    }

    @Test
    fun `UUPrefBackedMutableStringSet handles empty set`()
    {
        // Given: UUPrefs with empty set
        val prefs = createMockUUPrefs()
        val key = "test_string_set_key"
        val defaultValue = setOf("default")

        Mockito.`when`(prefs.getStringSet(key, defaultValue)).thenReturn(emptySet())

        // When: Creating UUPrefBackedMutableStringSet
        val state = UUPrefBackedMutableStringSet(prefs, key, defaultValue)

        // Then: Should use empty set (not default)
        assertEquals(emptySet<String>(), state.value)
        assertTrue(state.value.isEmpty())
    }

    // ==================== UUPrefBackedMutableEnum Tests ====================

    enum class TestTheme {
        LIGHT,
        DARK,
        SYSTEM
    }

    @Test
    fun `UUPrefBackedMutableEnum uses default value when no value exists`()
    {
        // Given: UUPrefs with no existing value
        val prefs = createMockUUPrefs()
        val key = "test_enum_key"
        val enumClass = TestTheme::class.java
        val defaultValue = TestTheme.SYSTEM

        Mockito.`when`(prefs.getEnum(key, enumClass, defaultValue)).thenReturn(null)

        // When: Creating UUPrefBackedMutableEnum
        val state = UUPrefBackedMutableEnum(prefs, key, enumClass, defaultValue)

        // Then: Should use default value
        assertEquals(defaultValue, state.value)
        Mockito.verify(prefs, Mockito.times(1)).getEnum(key, enumClass, defaultValue)
    }

    @Test
    fun `UUPrefBackedMutableEnum uses default value when UUPrefs returns null`()
    {
        // Given: UUPrefs returns null
        val prefs = createMockUUPrefs()
        val key = "test_enum_key"
        val enumClass = TestTheme::class.java
        val defaultValue = TestTheme.LIGHT

        Mockito.`when`(prefs.getEnum(key, enumClass, defaultValue)).thenReturn(null)

        // When: Creating UUPrefBackedMutableEnum
        val state = UUPrefBackedMutableEnum(prefs, key, enumClass, defaultValue)

        // Then: Should use default value
        assertEquals(defaultValue, state.value)
    }

    @Test
    fun `UUPrefBackedMutableEnum loads existing value from UUPrefs`()
    {
        // Given: UUPrefs with existing value
        val prefs = createMockUUPrefs()
        val key = "test_enum_key"
        val enumClass = TestTheme::class.java
        val defaultValue = TestTheme.SYSTEM
        val existingValue = TestTheme.DARK

        Mockito.`when`(prefs.getEnum(key, enumClass, defaultValue)).thenReturn(existingValue)

        // When: Creating UUPrefBackedMutableEnum
        val state = UUPrefBackedMutableEnum(prefs, key, enumClass, defaultValue)

        // Then: Should load existing value
        assertEquals(existingValue, state.value)
        Mockito.verify(prefs, Mockito.times(1)).getEnum(key, enumClass, defaultValue)
    }

    @Test
    fun `UUPrefBackedMutableEnum persists value when set`()
    {
        // Given: UUPrefBackedMutableEnum
        val prefs = createMockUUPrefs()
        val key = "test_enum_key"
        val enumClass = TestTheme::class.java
        val defaultValue = TestTheme.SYSTEM
        val newValue = TestTheme.LIGHT

        Mockito.`when`(prefs.getEnum(key, enumClass, defaultValue)).thenReturn(null)

        val state = UUPrefBackedMutableEnum(prefs, key, enumClass, defaultValue)

        // When: Setting a new value
        state.value = newValue

        // Then: Should persist to UUPrefs
        assertEquals(newValue, state.value)
        Mockito.verify(prefs, Mockito.times(1)).putEnum(key, newValue)
    }

    @Test
    fun `UUPrefBackedMutableEnum persists multiple updates`()
    {
        // Given: UUPrefBackedMutableEnum
        val prefs = createMockUUPrefs()
        val key = "test_enum_key"
        val enumClass = TestTheme::class.java
        val defaultValue = TestTheme.SYSTEM

        Mockito.`when`(prefs.getEnum(key, enumClass, defaultValue)).thenReturn(null)

        val state = UUPrefBackedMutableEnum(prefs, key, enumClass, defaultValue)

        // When: Setting multiple values
        state.value = TestTheme.LIGHT
        state.value = TestTheme.DARK
        state.value = TestTheme.SYSTEM

        // Then: Should persist each update
        assertEquals(TestTheme.SYSTEM, state.value)
        Mockito.verify(prefs, Mockito.times(1)).putEnum(key, TestTheme.LIGHT)
        Mockito.verify(prefs, Mockito.times(1)).putEnum(key, TestTheme.DARK)
        Mockito.verify(prefs, Mockito.times(1)).putEnum(key, TestTheme.SYSTEM)
    }

    // ==================== UUPrefBackedMutableDouble Tests ====================

    @Test
    fun `UUPrefBackedMutableDouble uses default value when no value exists`()
    {
        // Given: UUPrefs with no existing value
        val prefs = createMockUUPrefs()
        val key = "test_double_key"
        val defaultValue = 0.0

        Mockito.`when`(prefs.getDouble(key, defaultValue)).thenReturn(defaultValue)

        // When: Creating UUPrefBackedMutableDouble
        val state = UUPrefBackedMutableDouble(prefs, key, defaultValue)

        // Then: Should use default value
        assertEquals(defaultValue, state.value, 0.001)
        Mockito.verify(prefs, Mockito.times(1)).getDouble(key, defaultValue)
    }

    @Test
    fun `UUPrefBackedMutableDouble loads existing value from UUPrefs`()
    {
        // Given: UUPrefs with existing value
        val prefs = createMockUUPrefs()
        val key = "test_double_key"
        val defaultValue = 0.0
        val existingValue = 3.141592653589793

        Mockito.`when`(prefs.getDouble(key, defaultValue)).thenReturn(existingValue)

        // When: Creating UUPrefBackedMutableDouble
        val state = UUPrefBackedMutableDouble(prefs, key, defaultValue)

        // Then: Should load existing value
        assertEquals(existingValue, state.value, 0.001)
        Mockito.verify(prefs, Mockito.times(1)).getDouble(key, defaultValue)
    }

    @Test
    fun `UUPrefBackedMutableDouble persists value when set`()
    {
        // Given: UUPrefBackedMutableDouble
        val prefs = createMockUUPrefs()
        val key = "test_double_key"
        val defaultValue = 0.0
        val newValue = 2.718281828459045

        Mockito.`when`(prefs.getDouble(key, defaultValue)).thenReturn(defaultValue)

        val state = UUPrefBackedMutableDouble(prefs, key, defaultValue)

        // When: Setting a new value
        state.value = newValue

        // Then: Should persist to UUPrefs
        assertEquals(newValue, state.value, 0.001)
        Mockito.verify(prefs, Mockito.times(1)).putDouble(key, newValue)
    }

    @Test
    fun `UUPrefBackedMutableDouble persists multiple updates`()
    {
        // Given: UUPrefBackedMutableDouble
        val prefs = createMockUUPrefs()
        val key = "test_double_key"
        val defaultValue = 0.0

        Mockito.`when`(prefs.getDouble(key, defaultValue)).thenReturn(defaultValue)

        val state = UUPrefBackedMutableDouble(prefs, key, defaultValue)

        // When: Setting multiple values
        state.value = 1.5
        state.value = 2.5
        state.value = 3.5

        // Then: Should persist each update
        assertEquals(3.5, state.value, 0.001)
        Mockito.verify(prefs, Mockito.times(1)).putDouble(key, 1.5)
        Mockito.verify(prefs, Mockito.times(1)).putDouble(key, 2.5)
        Mockito.verify(prefs, Mockito.times(1)).putDouble(key, 3.5)
    }

    // ==================== UUPrefBackedMutableData Tests ====================

    @Test
    fun `UUPrefBackedMutableData uses default value when no value exists`()
    {
        // Given: UUPrefs with no existing value
        val prefs = createMockUUPrefs()
        val key = "test_data_key"
        val defaultValue = ByteArray(0)

        Mockito.`when`(prefs.getData(key, defaultValue)).thenReturn(null)

        // When: Creating UUPrefBackedMutableData
        val state = UUPrefBackedMutableData(prefs, key, defaultValue)

        // Then: Should use default value
        assertTrue(state.value.contentEquals(defaultValue))
        Mockito.verify(prefs, Mockito.times(1)).getData(key, defaultValue)
    }

    @Test
    fun `UUPrefBackedMutableData uses default value when UUPrefs returns null`()
    {
        // Given: UUPrefs returns null
        val prefs = createMockUUPrefs()
        val key = "test_data_key"
        val defaultValue = byteArrayOf(1, 2, 3)

        Mockito.`when`(prefs.getData(key, defaultValue)).thenReturn(null)

        // When: Creating UUPrefBackedMutableData
        val state = UUPrefBackedMutableData(prefs, key, defaultValue)

        // Then: Should use default value
        assertTrue(state.value.contentEquals(defaultValue))
    }

    @Test
    fun `UUPrefBackedMutableData loads existing value from UUPrefs`()
    {
        // Given: UUPrefs with existing value
        val prefs = createMockUUPrefs()
        val key = "test_data_key"
        val defaultValue = ByteArray(0)
        val existingValue = byteArrayOf(10, 20, 30, 40, 50)

        Mockito.`when`(prefs.getData(key, defaultValue)).thenReturn(existingValue)

        // When: Creating UUPrefBackedMutableData
        val state = UUPrefBackedMutableData(prefs, key, defaultValue)

        // Then: Should load existing value
        assertTrue(state.value.contentEquals(existingValue))
        assertEquals(5, state.value.size)
        Mockito.verify(prefs, Mockito.times(1)).getData(key, defaultValue)
    }

    @Test
    fun `UUPrefBackedMutableData persists value when set`()
    {
        // Given: UUPrefBackedMutableData
        val prefs = createMockUUPrefs()
        val key = "test_data_key"
        val defaultValue = ByteArray(0)
        val newValue = byteArrayOf(100, 101, 102)

        Mockito.`when`(prefs.getData(key, defaultValue)).thenReturn(null)

        val state = UUPrefBackedMutableData(prefs, key, defaultValue)

        // When: Setting a new value
        state.value = newValue

        // Then: Should persist to UUPrefs
        assertTrue(state.value.contentEquals(newValue))
        Mockito.verify(prefs, Mockito.times(1)).putData(key, newValue)
    }

    @Test
    fun `UUPrefBackedMutableData handles empty byte array`()
    {
        // Given: UUPrefs with empty byte array
        val prefs = createMockUUPrefs()
        val key = "test_data_key"
        val defaultValue = byteArrayOf(1, 2, 3)

        Mockito.`when`(prefs.getData(key, defaultValue)).thenReturn(ByteArray(0))

        // When: Creating UUPrefBackedMutableData
        val state = UUPrefBackedMutableData(prefs, key, defaultValue)

        // Then: Should use empty byte array (not default)
        assertTrue(state.value.contentEquals(ByteArray(0)))
        assertTrue(state.value.isEmpty())
    }

    @Test
    fun `UUPrefBackedMutableData persists multiple updates`()
    {
        // Given: UUPrefBackedMutableData
        val prefs = createMockUUPrefs()
        val key = "test_data_key"
        val defaultValue = ByteArray(0)

        Mockito.`when`(prefs.getData(key, defaultValue)).thenReturn(null)

        val state = UUPrefBackedMutableData(prefs, key, defaultValue)

        // When: Setting multiple values
        val value1 = byteArrayOf(1, 2, 3)
        val value2 = byteArrayOf(4, 5, 6)
        val value3 = byteArrayOf(7, 8, 9)
        state.value = value1
        state.value = value2
        state.value = value3

        // Then: Should persist each update
        assertTrue(state.value.contentEquals(value3))
        Mockito.verify(prefs, Mockito.times(1)).putData(key, value1)
        Mockito.verify(prefs, Mockito.times(1)).putData(key, value2)
        Mockito.verify(prefs, Mockito.times(1)).putData(key, value3)
    }

    // ==================== Multiple Updates Tests ====================

    @Test
    fun `UUPrefBackedMutableBoolean persists multiple updates`()
    {
        // Given: UUPrefBackedMutableBoolean
        val prefs = createMockUUPrefs()
        val key = "test_boolean_key"
        val defaultValue = false

        Mockito.`when`(prefs.getBoolean(key, defaultValue)).thenReturn(defaultValue)

        val state = UUPrefBackedMutableBoolean(prefs, key, defaultValue)

        // When: Setting multiple values
        state.value = true
        state.value = false
        state.value = true

        // Then: Should persist each update
        assertEquals(true, state.value)
        // Verify calls were made (at least 2 for true, 1 for false)
        Mockito.verify(prefs, Mockito.atLeast(2)).putBoolean(key, true)
        Mockito.verify(prefs, Mockito.atLeast(1)).putBoolean(key, false)
    }

    @Test
    fun `UUPrefBackedMutableInt persists multiple updates`()
    {
        // Given: UUPrefBackedMutableInt
        val prefs = createMockUUPrefs()
        val key = "test_int_key"
        val defaultValue = 0

        Mockito.`when`(prefs.getInt(key, defaultValue)).thenReturn(defaultValue)

        val state = UUPrefBackedMutableInt(prefs, key, defaultValue)

        // When: Setting multiple values
        state.value = 10
        state.value = 20
        state.value = 30

        // Then: Should persist each update
        assertEquals(30, state.value)
        Mockito.verify(prefs, Mockito.times(1)).putInt(key, 10)
        Mockito.verify(prefs, Mockito.times(1)).putInt(key, 20)
        Mockito.verify(prefs, Mockito.times(1)).putInt(key, 30)
    }

    // ==================== Helper Functions Tests ====================

    @Test
    fun `uuPrefBackedMutableBoolean creates UUPrefBackedMutableBoolean`()
    {
        // Given: UUPrefs
        val prefs = createMockUUPrefs()
        val key = "test_boolean_key"
        val defaultValue = false

        Mockito.`when`(prefs.getBoolean(key, defaultValue)).thenReturn(defaultValue)

        // When: Creating state using helper function
        val state = uuPrefBackedMutableBoolean(prefs, key, defaultValue)

        // Then: Should create UUPrefBackedMutableBoolean instance
        assertTrue(state is UUPrefBackedMutableBoolean)
        assertEquals(defaultValue, state.value)
    }

    @Test
    fun `uuPrefBackedMutableInt creates UUPrefBackedMutableInt`()
    {
        // Given: UUPrefs
        val prefs = createMockUUPrefs()
        val key = "test_int_key"
        val defaultValue = 42

        Mockito.`when`(prefs.getInt(key, defaultValue)).thenReturn(defaultValue)

        // When: Creating state using helper function
        val state = uuPrefBackedMutableInt(prefs, key, defaultValue)

        // Then: Should create UUPrefBackedMutableInt instance
        assertTrue(state is UUPrefBackedMutableInt)
        assertEquals(defaultValue, state.value)
    }

    @Test
    fun `uuPrefBackedMutableLong creates UUPrefBackedMutableLong`()
    {
        // Given: UUPrefs
        val prefs = createMockUUPrefs()
        val key = "test_long_key"
        val defaultValue = 123456789L

        Mockito.`when`(prefs.getLong(key, defaultValue)).thenReturn(defaultValue)

        // When: Creating state using helper function
        val state = uuPrefBackedMutableLong(prefs, key, defaultValue)

        // Then: Should create UUPrefBackedMutableLong instance
        assertTrue(state is UUPrefBackedMutableLong)
        assertEquals(defaultValue, state.value)
    }

    @Test
    fun `uuPrefBackedMutableFloat creates UUPrefBackedMutableFloat`()
    {
        // Given: UUPrefs
        val prefs = createMockUUPrefs()
        val key = "test_float_key"
        val defaultValue = 3.14f

        Mockito.`when`(prefs.getFloat(key, defaultValue)).thenReturn(defaultValue)

        // When: Creating state using helper function
        val state = uuPrefBackedMutableFloat(prefs, key, defaultValue)

        // Then: Should create UUPrefBackedMutableFloat instance
        assertTrue(state is UUPrefBackedMutableFloat)
        assertEquals(defaultValue, state.value, 0.001f)
    }

    @Test
    fun `uuPrefBackedMutableDouble creates UUPrefBackedMutableDouble`()
    {
        // Given: UUPrefs
        val prefs = createMockUUPrefs()
        val key = "test_double_key"
        val defaultValue = 3.141592653589793

        Mockito.`when`(prefs.getDouble(key, defaultValue)).thenReturn(defaultValue)

        // When: Creating state using helper function
        val state = uuPrefBackedMutableDouble(prefs, key, defaultValue)

        // Then: Should create UUPrefBackedMutableDouble instance
        assertTrue(state is UUPrefBackedMutableDouble)
        assertEquals(defaultValue, state.value, 0.001)
    }

    @Test
    fun `uuPrefBackedMutableString creates UUPrefBackedMutableString`()
    {
        // Given: UUPrefs
        val prefs = createMockUUPrefs()
        val key = "test_string_key"
        val defaultValue = "default"

        Mockito.`when`(prefs.getString(key, defaultValue)).thenReturn(defaultValue)

        // When: Creating state using helper function
        val state = uuPrefBackedMutableString(prefs, key, defaultValue)

        // Then: Should create UUPrefBackedMutableString instance
        assertTrue(state is UUPrefBackedMutableString)
        assertEquals(defaultValue, state.value)
    }

    @Test
    fun `uuPrefBackedMutableStringSet creates UUPrefBackedMutableStringSet`()
    {
        // Given: UUPrefs
        val prefs = createMockUUPrefs()
        val key = "test_string_set_key"
        val defaultValue = setOf("tag1", "tag2")

        Mockito.`when`(prefs.getStringSet(key, defaultValue)).thenReturn(defaultValue)

        // When: Creating state using helper function
        val state = uuPrefBackedMutableStringSet(prefs, key, defaultValue)

        // Then: Should create UUPrefBackedMutableStringSet instance
        assertTrue(state is UUPrefBackedMutableStringSet)
        assertEquals(defaultValue, state.value)
    }

    @Test
    fun `uuPrefBackedMutableEnum creates UUPrefBackedMutableEnum`()
    {
        // Given: UUPrefs
        val prefs = createMockUUPrefs()
        val key = "test_enum_key"
        val enumClass = TestTheme::class.java
        val defaultValue = TestTheme.SYSTEM

        Mockito.`when`(prefs.getEnum(key, enumClass, defaultValue)).thenReturn(defaultValue)

        // When: Creating state using helper function
        val state = uuPrefBackedMutableEnum(prefs, key, enumClass, defaultValue)

        // Then: Should create UUPrefBackedMutableEnum instance
        assertTrue(state is UUPrefBackedMutableEnum<*>)
        assertEquals(defaultValue, state.value)
    }

    @Test
    fun `uuPrefBackedMutableData creates UUPrefBackedMutableData`()
    {
        // Given: UUPrefs
        val prefs = createMockUUPrefs()
        val key = "test_data_key"
        val defaultValue = byteArrayOf(1, 2, 3)

        Mockito.`when`(prefs.getData(key, defaultValue)).thenReturn(defaultValue)

        // When: Creating state using helper function
        val state = uuPrefBackedMutableData(prefs, key, defaultValue)

        // Then: Should create UUPrefBackedMutableData instance
        assertTrue(state is UUPrefBackedMutableData)
        assertTrue(state.value.contentEquals(defaultValue))
    }

    @Test
    fun `uuPrefBackedMutableBoolean uses default parameter value`()
    {
        // Given: UUPrefs
        val prefs = createMockUUPrefs()
        val key = "test_boolean_key"

        Mockito.`when`(prefs.getBoolean(key, false)).thenReturn(false)

        // When: Creating state using helper function without defaultValue
        val state = uuPrefBackedMutableBoolean(prefs, key)

        // Then: Should use default parameter value (false)
        assertEquals(false, state.value)
    }

    @Test
    fun `uuPrefBackedMutableInt uses default parameter value`()
    {
        // Given: UUPrefs
        val prefs = createMockUUPrefs()
        val key = "test_int_key"

        Mockito.`when`(prefs.getInt(key, 0)).thenReturn(0)

        // When: Creating state using helper function without defaultValue
        val state = uuPrefBackedMutableInt(prefs, key)

        // Then: Should use default parameter value (0)
        assertEquals(0, state.value)
    }

    @Test
    fun `uuPrefBackedMutableLong uses default parameter value`()
    {
        // Given: UUPrefs
        val prefs = createMockUUPrefs()
        val key = "test_long_key"

        Mockito.`when`(prefs.getLong(key, 0L)).thenReturn(0L)

        // When: Creating state using helper function without defaultValue
        val state = uuPrefBackedMutableLong(prefs, key)

        // Then: Should use default parameter value (0L)
        assertEquals(0L, state.value)
    }

    @Test
    fun `uuPrefBackedMutableFloat uses default parameter value`()
    {
        // Given: UUPrefs
        val prefs = createMockUUPrefs()
        val key = "test_float_key"

        Mockito.`when`(prefs.getFloat(key, 0f)).thenReturn(0f)

        // When: Creating state using helper function without defaultValue
        val state = uuPrefBackedMutableFloat(prefs, key)

        // Then: Should use default parameter value (0f)
        assertEquals(0f, state.value, 0.001f)
    }

    @Test
    fun `uuPrefBackedMutableDouble uses default parameter value`()
    {
        // Given: UUPrefs
        val prefs = createMockUUPrefs()
        val key = "test_double_key"

        Mockito.`when`(prefs.getDouble(key, 0.0)).thenReturn(0.0)

        // When: Creating state using helper function without defaultValue
        val state = uuPrefBackedMutableDouble(prefs, key)

        // Then: Should use default parameter value (0.0)
        assertEquals(0.0, state.value, 0.001)
    }

    @Test
    fun `uuPrefBackedMutableString uses default parameter value`()
    {
        // Given: UUPrefs
        val prefs = createMockUUPrefs()
        val key = "test_string_key"

        Mockito.`when`(prefs.getString(key, "")).thenReturn(null)

        // When: Creating state using helper function without defaultValue
        val state = uuPrefBackedMutableString(prefs, key)

        // Then: Should use default parameter value ("")
        assertEquals("", state.value)
    }

    @Test
    fun `uuPrefBackedMutableStringSet uses default parameter value`()
    {
        // Given: UUPrefs
        val prefs = createMockUUPrefs()
        val key = "test_string_set_key"

        Mockito.`when`(prefs.getStringSet(key, emptySet())).thenReturn(null)

        // When: Creating state using helper function without defaultValue
        val state = uuPrefBackedMutableStringSet(prefs, key)

        // Then: Should use default parameter value (emptySet())
        assertEquals(emptySet<String>(), state.value)
    }

    @Test
    fun `uuPrefBackedMutableData uses default parameter value`()
    {
        // Given: UUPrefs
        val prefs = createMockUUPrefs()
        val key = "test_data_key"

        Mockito.`when`(prefs.getData(key, ByteArray(0))).thenReturn(null)

        // When: Creating state using helper function without defaultValue
        val state = uuPrefBackedMutableData(prefs, key)

        // Then: Should use default parameter value (ByteArray(0))
        assertTrue(state.value.contentEquals(ByteArray(0)))
    }
}

