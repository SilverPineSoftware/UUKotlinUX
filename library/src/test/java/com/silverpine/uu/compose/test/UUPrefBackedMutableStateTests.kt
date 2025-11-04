package com.silverpine.uu.compose.test

import com.silverpine.uu.compose.UUPrefBackedMutableBoolean
import com.silverpine.uu.compose.UUPrefBackedMutableFloat
import com.silverpine.uu.compose.UUPrefBackedMutableInt
import com.silverpine.uu.compose.UUPrefBackedMutableLong
import com.silverpine.uu.compose.UUPrefBackedMutableString
import com.silverpine.uu.compose.UUPrefBackedMutableStringSet
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
}

