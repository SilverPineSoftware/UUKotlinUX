package com.silverpine.uu.ux.files

import android.content.Context
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import com.silverpine.uu.ux.files.UUFiles
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension

/**
 * JUnit 5 unit tests for UUFiles class using Mockito.
 * 
 * These tests mock all Android dependencies and test the business logic
 * without requiring a real Android device or emulator.
 */
@ExtendWith(MockitoExtension::class)
class UUFilesTests
{
    private fun buildActivityWithLauncher(): Pair<ComponentActivity, ActivityResultLauncher<String>>
    {
        val activity = Mockito.mock(ComponentActivity::class.java)
        val context = Mockito.mock(Context::class.java)
        
        Mockito.lenient().`when`(activity.applicationContext).thenReturn(context)
        
        @Suppress("UNCHECKED_CAST")
        val launcher = Mockito.mock(ActivityResultLauncher::class.java) as ActivityResultLauncher<String>
        
        Mockito.lenient().`when`(activity.registerForActivityResult(
            Mockito.any<ActivityResultContract<String, Uri?>>(),
            Mockito.any<ActivityResultCallback<Uri?>>()
        )).thenReturn(launcher)

        return Pair(activity, launcher)
    }

    @Test
    fun `requestFile stores completion block and calls launch`()
    {
        // Given: Activity and launcher are set up
        val (activity, launcher) = buildActivityWithLauncher()
        val filter = "image/*"
        
        UUFiles.init(activity)

        // When: requestFile is called
        var completionCalled = false
        var receivedContext: Context? = null
        var receivedUri: Uri? = null
        UUFiles.requestFile(filter) { context, uri ->
            completionCalled = true
            receivedContext = context
            receivedUri = uri
        }

        // Then: launch should be called with the filter
        Mockito.verify(launcher).launch(filter)
        
        // Completion should not be called yet (only when handleLaunchResult is called)
        assertFalse(completionCalled)
    }

    @Test
    fun `handleLaunchResult calls completion block with non-null result`()
    {
        // Given: Activity is initialized and requestFile was called
        val (activity, _) = buildActivityWithLauncher()
        val testUri = Mockito.mock(Uri::class.java)
        
        UUFiles.init(activity)

        var completionCalled = false
        var receivedContext: Context? = null
        var receivedUri: Uri? = null
        UUFiles.requestFile("image/*") { context, uri ->
            completionCalled = true
            receivedContext = context
            receivedUri = uri
        }

        // When: handleLaunchResult is called with a non-null result
        UUFiles.handleLaunchResult(testUri)

        // Then: Completion should be called with context and result
        assertTrue(completionCalled)
        assertNotNull(receivedContext)
        assertNotNull(receivedUri)
        assertEquals(activity.applicationContext, receivedContext)
        assertEquals(testUri, receivedUri)
    }

    @Test
    fun `handleLaunchResult calls completion block with null result`()
    {
        // Given: Activity is initialized and requestFile was called
        val (activity, _) = buildActivityWithLauncher()
        
        UUFiles.init(activity)

        var completionCalled = false
        var receivedContext: Context? = null
        var receivedUri: Uri? = null
        UUFiles.requestFile("image/*") { context, uri ->
            completionCalled = true
            receivedContext = context
            receivedUri = uri
        }

        // When: handleLaunchResult is called with a null result
        UUFiles.handleLaunchResult(null)

        // Then: Completion should be called with context and null URI
        assertTrue(completionCalled)
        assertNotNull(receivedContext)
        assertEquals(activity.applicationContext, receivedContext)
        assertNull(receivedUri)
    }

    @Test
    fun `handleLaunchResult handles null completion block gracefully`()
    {
        // Given: Activity is initialized but no requestFile was called
        val (activity, _) = buildActivityWithLauncher()
        
        UUFiles.init(activity)

        // When: handleLaunchResult is called without a prior request
        val testUri = Mockito.mock(Uri::class.java)
        
        // Then: Should not throw an exception
        UUFiles.handleLaunchResult(testUri)
    }

    @Test
    fun `handleLaunchResult clears completion block after calling it`()
    {
        // Given: Activity is initialized and requestFile was called
        val (activity, _) = buildActivityWithLauncher()
        val testUri = Mockito.mock(Uri::class.java)
        
        UUFiles.init(activity)

        var callCount = 0
        UUFiles.requestFile("image/*") { _, _ ->
            callCount++
        }

        // When: handleLaunchResult is called
        UUFiles.handleLaunchResult(testUri)

        // Then: Completion should be called once
        assertEquals(1, callCount)

        // When: handleLaunchResult is called again
        UUFiles.handleLaunchResult(testUri)

        // Then: Completion should not be called again (block was cleared)
        assertEquals(1, callCount)
    }

    @Test
    fun `multiple sequential requests work correctly`()
    {
        // Given: Activity is initialized
        val (activity, launcher) = buildActivityWithLauncher()
        val firstUri = Mockito.mock(Uri::class.java)
        val secondUri = Mockito.mock(Uri::class.java)
        
        UUFiles.init(activity)

        // When: First request is made
        var firstCallCount = 0
        var firstReceivedContext: Context? = null
        var firstReceivedUri: Uri? = null
        UUFiles.requestFile("image/*") { context, uri ->
            firstCallCount++
            firstReceivedContext = context
            firstReceivedUri = uri
        }

        // Then: First launch should be called
        Mockito.verify(launcher).launch("image/*")

        // When: First result is handled
        UUFiles.handleLaunchResult(firstUri)

        // Then: First completion should be called
        assertEquals(1, firstCallCount)
        assertNotNull(firstReceivedContext)
        assertEquals(activity.applicationContext, firstReceivedContext)
        assertEquals(firstUri, firstReceivedUri)

        // When: Second request is made
        var secondCallCount = 0
        var secondReceivedContext: Context? = null
        var secondReceivedUri: Uri? = null
        UUFiles.requestFile("application/*") { context, uri ->
            secondCallCount++
            secondReceivedContext = context
            secondReceivedUri = uri
        }

        // Then: Second launch should be called
        Mockito.verify(launcher).launch("application/*")

        // When: Second result is handled
        UUFiles.handleLaunchResult(secondUri)

        // Then: Second completion should be called
        assertEquals(1, secondCallCount)
        assertNotNull(secondReceivedContext)
        assertEquals(activity.applicationContext, secondReceivedContext)
        assertEquals(secondUri, secondReceivedUri)

        // First completion should still only be called once
        assertEquals(1, firstCallCount)
    }

    @Test
    fun `init registers activity result launcher`()
    {
        // Given: Activity
        val (activity, launcher) = buildActivityWithLauncher()

        // When: init is called
        UUFiles.init(activity)

        // Then: registerForActivityResult should be called
        Mockito.verify(activity).registerForActivityResult(
            Mockito.any<ActivityResultContract<String, Uri?>>(),
            Mockito.any<ActivityResultCallback<Uri?>>()
        )
    }

    @Test
    fun `requestFile works with different filter types`()
    {
        // Given: Activity is initialized
        val (activity, launcher) = buildActivityWithLauncher()
        
        UUFiles.init(activity)

        // When: requestFile is called with different filters
        val filters = arrayOf("image/*", "application/*", "video/*", "*/*")
        
        filters.forEach { filter ->
            UUFiles.requestFile(filter) { _, _ -> }
            
            // Then: launch should be called with the correct filter
            Mockito.verify(launcher).launch(filter)
        }
    }

    @Test
    fun `handleLaunchResult with null result after null result`()
    {
        // Given: Activity is initialized and requestFile was called
        val (activity, _) = buildActivityWithLauncher()
        
        UUFiles.init(activity)

        var callCount = 0
        var receivedContext: Context? = null
        var receivedUri: Uri? = null
        UUFiles.requestFile("image/*") { context, uri ->
            callCount++
            receivedContext = context
            receivedUri = uri
        }

        // When: handleLaunchResult is called with null
        UUFiles.handleLaunchResult(null)

        // Then: Completion should be called with context and null URI
        assertEquals(1, callCount)
        assertNotNull(receivedContext)
        assertEquals(activity.applicationContext, receivedContext)
        assertNull(receivedUri)

        // When: Another request is made and result is null again
        UUFiles.requestFile("image/*") { context, uri ->
            callCount++
            receivedContext = context
            receivedUri = uri
        }

        UUFiles.handleLaunchResult(null)

        // Then: Completion should be called again with context and null URI
        assertEquals(2, callCount)
        assertNotNull(receivedContext)
        assertEquals(activity.applicationContext, receivedContext)
        assertNull(receivedUri)
    }

    @Test
    fun `handleLaunchResult passes application context when activity is initialized`()
    {
        // Given: Activity is initialized with application context
        val (activity, _) = buildActivityWithLauncher()
        val testUri = Mockito.mock(Uri::class.java)
        
        UUFiles.init(activity)

        var completionCalled = false
        var receivedContext: Context? = null
        var receivedUri: Uri? = null
        
        UUFiles.requestFile("image/*") { context, uri ->
            completionCalled = true
            receivedContext = context
            receivedUri = uri
        }

        // When: handleLaunchResult is called
        UUFiles.handleLaunchResult(testUri)

        // Then: Context should be the application context from the activity
        assertTrue(completionCalled)
        assertNotNull(receivedContext)
        assertEquals(activity.applicationContext, receivedContext)
        assertNotNull(receivedUri)
        assertEquals(testUri, receivedUri)
    }
}