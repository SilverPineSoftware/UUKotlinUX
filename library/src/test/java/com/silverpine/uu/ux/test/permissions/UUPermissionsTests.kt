package com.silverpine.uu.ux.test.permissions

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.content.ContextCompat
import com.silverpine.uu.ux.permissions.UUPermissionProvider
import com.silverpine.uu.ux.permissions.UUPermissionStatus
import com.silverpine.uu.ux.permissions.UUPermissions
import com.silverpine.uu.ux.permissions.areAllPermissionsGranted
import com.silverpine.uu.ux.permissions.areAnyPermissionsPermanentlyDenied
import com.silverpine.uu.ux.permissions.getPermissionStatusMultiple
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.MockedStatic
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * JUnit 5 unit tests for UUPermissions class using Mockito.
 * 
 * These tests mock all Android dependencies and test the business logic
 * without requiring a real Android device or emulator.
 */
@ExtendWith(MockitoExtension::class)
class UUPermissionsTests
{
    @Mock
    private lateinit var mockActivity: ComponentActivity

    @Mock
    private lateinit var mockContext: Context

    @Mock
    private lateinit var mockSharedPreferences: SharedPreferences

    @Mock
    private lateinit var mockEditor: SharedPreferences.Editor

    private lateinit var permissions: UUPermissionProvider
    private lateinit var contextCompatMock: MockedStatic<ContextCompat>

    @BeforeEach
    fun setUp()
    {
        // Setup mock behavior with lenient stubbing
        Mockito.lenient().`when`(mockActivity.applicationContext).thenReturn(mockContext)
        Mockito.lenient().`when`(mockActivity.getSharedPreferences(Mockito.anyString(), Mockito.anyInt())).thenReturn(mockSharedPreferences)
        Mockito.lenient().`when`(mockSharedPreferences.edit()).thenReturn(mockEditor)
        Mockito.lenient().`when`(mockEditor.putBoolean(Mockito.anyString(), Mockito.anyBoolean())).thenReturn(mockEditor)
        Mockito.lenient().`when`(mockEditor.commit()).thenReturn(true)

        // Mock registerForActivityResult to return a mock launcher
        @Suppress("UNCHECKED_CAST")
        val mockLauncher = Mockito.mock(ActivityResultLauncher::class.java) as ActivityResultLauncher<Array<String>>
        Mockito.lenient().`when`(mockActivity.registerForActivityResult(
            Mockito.any<ActivityResultContract<Array<String>, Map<String, Boolean>>>(),
            Mockito.any<ActivityResultCallback<Map<String, Boolean>>>()
        )).thenReturn(mockLauncher)

        // Mock ContextCompat static methods
        contextCompatMock = Mockito.mockStatic(ContextCompat::class.java)

        UUPermissions.init(mockActivity)

        // Create UUPermissions instance
        permissions = UUPermissions //(mockActivity)
    }

    @AfterEach
    fun tearDown()
    {
        contextCompatMock.close()
    }

    @Test
    fun `getPermissionStatus returns GRANTED when permission is granted`()
    {
        // Given: Permission is granted
        val permission = "android.permission.CAMERA"
        contextCompatMock.`when`<Int> { ContextCompat.checkSelfPermission(mockActivity, permission) }.thenReturn(PackageManager.PERMISSION_GRANTED)
        Mockito.lenient().`when`(mockSharedPreferences.getBoolean("UU_HAS_REQUESTED_$permission", false)).thenReturn(true) // Has been requested before

        // When: Getting permission status
        val status = permissions.getPermissionStatus(permission)

        // Then: Should return GRANTED
        assertEquals(UUPermissionStatus.GRANTED, status)
        assertTrue(status.isGranted)
        assertFalse(status.canRequest)
    }

    @Test
    fun `getPermissionStatus returns NEVER_ASKED when permission never requested`()
    {
        // Given: Permission never requested
        val permission = "android.permission.ACCESS_FINE_LOCATION"
        Mockito.lenient().`when`(mockSharedPreferences.getBoolean("UU_HAS_REQUESTED_$permission", false)).thenReturn(false) // Never requested

        // When: Getting permission status
        val status = permissions.getPermissionStatus(permission)

        // Then: Should return NEVER_ASKED
        assertEquals(UUPermissionStatus.NEVER_ASKED, status)
        assertFalse(status.isGranted)
        assertTrue(status.canRequest)
    }

    @Test
    fun `getPermissionStatus returns DENIED when permission denied but can request again`()
    {
        // Given: Permission denied but can request again
        val permission = "android.permission.CAMERA"
        contextCompatMock.`when`<Int> { ContextCompat.checkSelfPermission(mockActivity, permission) }.thenReturn(PackageManager.PERMISSION_DENIED)
        Mockito.lenient().`when`(mockSharedPreferences.getBoolean("UU_HAS_REQUESTED_$permission", false)).thenReturn(true) // Has been requested before
        Mockito.lenient().`when`(mockActivity.shouldShowRequestPermissionRationale(permission)).thenReturn(true) // Can request again

        // When: Getting permission status
        val status = permissions.getPermissionStatus(permission)

        // Then: Should return DENIED
        assertEquals(UUPermissionStatus.DENIED, status)
        assertFalse(status.isGranted)
        assertTrue(status.canRequest)
    }

    @Test
    fun `getPermissionStatus returns PERMANENTLY_DENIED when permission permanently denied`()
    {
        // Given: Permission permanently denied
        val permission = "android.permission.CAMERA"
        contextCompatMock.`when`<Int> { ContextCompat.checkSelfPermission(mockActivity, permission) }.thenReturn(PackageManager.PERMISSION_DENIED)
        Mockito.lenient().`when`(mockSharedPreferences.getBoolean("UU_HAS_REQUESTED_$permission", false)).thenReturn(true) // Has been requested before
        Mockito.lenient().`when`(mockActivity.shouldShowRequestPermissionRationale(permission)).thenReturn(false) // Cannot request again

        // When: Getting permission status
        val status = permissions.getPermissionStatus(permission)

        // Then: Should return PERMANENTLY_DENIED
        assertEquals(UUPermissionStatus.PERMANENTLY_DENIED, status)
        assertFalse(status.isGranted)
        assertFalse(status.canRequest)
        assertTrue(status.isPermanentlyDenied)
    }

    @Test
    fun `getPermissionStatusMultiple returns correct statuses for multiple permissions`()
    {
        // Given: Multiple permissions with different states
        val perms = arrayOf(
            "android.permission.CAMERA",
            "android.permission.ACCESS_FINE_LOCATION"
        )

        // Mock CAMERA as granted
        contextCompatMock.`when`<Int> { ContextCompat.checkSelfPermission(mockActivity, "android.permission.CAMERA") }.thenReturn(PackageManager.PERMISSION_GRANTED)
        Mockito.`when`(mockSharedPreferences.getBoolean("UU_HAS_REQUESTED_android.permission.CAMERA", false)).thenReturn(true)

        // Mock ACCESS_FINE_LOCATION as never asked
        Mockito.`when`(mockSharedPreferences.getBoolean("UU_HAS_REQUESTED_android.permission.ACCESS_FINE_LOCATION", false)).thenReturn(false)

        // When: Getting status for multiple permissions
        val statuses = permissions.getPermissionStatusMultiple(perms)

        // Then: Each permission should have correct status
        assertEquals(UUPermissionStatus.GRANTED, statuses["android.permission.CAMERA"])
        assertEquals(UUPermissionStatus.NEVER_ASKED, statuses["android.permission.ACCESS_FINE_LOCATION"])
        assertEquals(2, statuses.size)
    }

    @Test
    fun `areAllPermissionsGranted returns true when all permissions are granted`()
    {
        // Given: All permissions are granted
        val perms = arrayOf(
            "android.permission.CAMERA",
            "android.permission.ACCESS_FINE_LOCATION"
        )

        perms.forEach { permission ->
            contextCompatMock.`when`<Int> { ContextCompat.checkSelfPermission(mockActivity, permission) }.thenReturn(PackageManager.PERMISSION_GRANTED)
            Mockito.lenient().`when`(mockSharedPreferences.getBoolean("UU_HAS_REQUESTED_$permission", false)).thenReturn(true)
        }

        // When: Checking if all permissions are granted
        val allGranted = permissions.areAllPermissionsGranted(perms)

        // Then: Should return true
        assertTrue(allGranted)
    }

    @Test
    fun `areAllPermissionsGranted returns false when some permissions are not granted`()
    {
        // Given: Mix of granted and non-granted permissions
        val perms = arrayOf(
            "android.permission.CAMERA",
            "android.permission.ACCESS_FINE_LOCATION"
        )

        // CAMERA is granted
        contextCompatMock.`when`<Int> { ContextCompat.checkSelfPermission(mockActivity, "android.permission.CAMERA") }.thenReturn(PackageManager.PERMISSION_GRANTED)
        Mockito.lenient().`when`(mockSharedPreferences.getBoolean("UU_HAS_REQUESTED_android.permission.CAMERA", false)).thenReturn(true)

        // ACCESS_FINE_LOCATION is not granted
        contextCompatMock.`when`<Int> { ContextCompat.checkSelfPermission(mockActivity, "android.permission.ACCESS_FINE_LOCATION") }.thenReturn(PackageManager.PERMISSION_DENIED)
        Mockito.lenient().`when`(mockSharedPreferences.getBoolean("UU_HAS_REQUESTED_android.permission.ACCESS_FINE_LOCATION", false)).thenReturn(false)

        // When: Checking if all permissions are granted
        val allGranted = permissions.areAllPermissionsGranted(perms)

        // Then: Should return false
        assertFalse(allGranted)
    }

    @Test
    fun `areAnyPermissionsPermanentlyDenied returns true when any permission is permanently denied`()
    {
        // Given: One permission is permanently denied
        val perms = arrayOf(
            "android.permission.CAMERA",
            "android.permission.ACCESS_FINE_LOCATION"
        )

        // CAMERA is granted
        contextCompatMock.`when`<Int> { ContextCompat.checkSelfPermission(mockActivity, "android.permission.CAMERA") }.thenReturn(PackageManager.PERMISSION_GRANTED)
        Mockito.lenient().`when`(mockSharedPreferences.getBoolean("UU_HAS_REQUESTED_android.permission.CAMERA", false)).thenReturn(true)

        // ACCESS_FINE_LOCATION is permanently denied
        contextCompatMock.`when`<Int> { ContextCompat.checkSelfPermission(mockActivity, "android.permission.ACCESS_FINE_LOCATION") }.thenReturn(PackageManager.PERMISSION_DENIED)
        Mockito.lenient().`when`(mockSharedPreferences.getBoolean("UU_HAS_REQUESTED_android.permission.ACCESS_FINE_LOCATION", false)).thenReturn(true)
        Mockito.lenient().`when`(mockActivity.shouldShowRequestPermissionRationale("android.permission.ACCESS_FINE_LOCATION")).thenReturn(false)

        // When: Checking if any permissions are permanently denied
        val anyPermanentlyDenied = permissions.areAnyPermissionsPermanentlyDenied(perms)

        // Then: Should return true
        assertTrue(anyPermanentlyDenied)
    }

    @Test
    fun `areAnyPermissionsPermanentlyDenied returns false when no permissions are permanently denied`()
    {
        // Given: No permissions are permanently denied
        val perms = arrayOf(
            "android.permission.CAMERA",
            "android.permission.ACCESS_FINE_LOCATION"
        )

        // CAMERA is granted
        contextCompatMock.`when`<Int> { ContextCompat.checkSelfPermission(mockActivity, "android.permission.CAMERA") }.thenReturn(PackageManager.PERMISSION_GRANTED)
        Mockito.lenient().`when`(mockSharedPreferences.getBoolean("UU_HAS_REQUESTED_android.permission.CAMERA", false)).thenReturn(true)

        // ACCESS_FINE_LOCATION is denied but can request again
        contextCompatMock.`when`<Int> { ContextCompat.checkSelfPermission(mockActivity, "android.permission.ACCESS_FINE_LOCATION") }.thenReturn(PackageManager.PERMISSION_DENIED)
        Mockito.lenient().`when`(mockSharedPreferences.getBoolean("UU_HAS_REQUESTED_android.permission.ACCESS_FINE_LOCATION", false)).thenReturn(true)
        Mockito.lenient().`when`(mockActivity.shouldShowRequestPermissionRationale("android.permission.ACCESS_FINE_LOCATION")).thenReturn(true)

        // When: Checking if any permissions are permanently denied
        val anyPermanentlyDenied = permissions.areAnyPermissionsPermanentlyDenied(perms)

        // Then: Should return false
        assertFalse(anyPermanentlyDenied)
    }

    @Test
    fun `requestPermissions with empty array calls completion immediately`()
    {
        // Given: Empty permissions array
        val perms = emptyArray<String>()

        val latch = CountDownLatch(1)
        var completionCalled = false
        var resultStatuses: Map<String, UUPermissionStatus>? = null

        // When: Requesting permissions
        permissions.requestPermissions(perms) { statuses ->
            completionCalled = true
            resultStatuses = statuses
            latch.countDown()
        }

        // Then: Completion should be called immediately with empty map
        assertTrue(latch.await(1, TimeUnit.SECONDS))
        assertTrue(completionCalled)
        assertNotNull(resultStatuses)
        assertTrue(resultStatuses?.isEmpty() == true)
    }

    @Test
    fun `requestPermissions calls completion immediately when all permissions are granted`()
    {
        // Given: All permissions are already granted
        val perms = arrayOf(
            "android.permission.CAMERA",
            "android.permission.ACCESS_FINE_LOCATION"
        )

        // Both permissions are granted
        contextCompatMock.`when`<Int> { ContextCompat.checkSelfPermission(mockActivity, "android.permission.CAMERA") }.thenReturn(PackageManager.PERMISSION_GRANTED)
        Mockito.lenient().`when`(mockSharedPreferences.getBoolean("UU_HAS_REQUESTED_android.permission.CAMERA", false)).thenReturn(true)

        contextCompatMock.`when`<Int> { ContextCompat.checkSelfPermission(mockActivity, "android.permission.ACCESS_FINE_LOCATION") }.thenReturn(PackageManager.PERMISSION_GRANTED)
        Mockito.lenient().`when`(mockSharedPreferences.getBoolean("UU_HAS_REQUESTED_android.permission.ACCESS_FINE_LOCATION", false)).thenReturn(true)

        var completionCalled = false
        var resultStatuses: Map<String, UUPermissionStatus>? = null

        // When: Requesting permissions
        permissions.requestPermissions(perms) { statuses ->
            completionCalled = true
            resultStatuses = statuses
        }

        // Then: Completion should be called immediately (synchronously)
        assertTrue(completionCalled)
        assertNotNull(resultStatuses)

        // Should have results for both permissions
        assertEquals(2, resultStatuses?.size)
        assertEquals(UUPermissionStatus.GRANTED, resultStatuses?.get("android.permission.CAMERA"))
        assertEquals(UUPermissionStatus.GRANTED, resultStatuses?.get("android.permission.ACCESS_FINE_LOCATION"))
    }

    @Test
    fun `permission status properties work correctly`()
    {
        // Test GRANTED status
        val grantedStatus = UUPermissionStatus.GRANTED
        assertTrue(grantedStatus.isGranted)
        assertFalse(grantedStatus.canRequest)
        assertFalse(grantedStatus.isPermanentlyDenied)

        // Test NEVER_ASKED status
        val neverAskedStatus = UUPermissionStatus.NEVER_ASKED
        assertFalse(neverAskedStatus.isGranted)
        assertTrue(neverAskedStatus.canRequest)
        assertFalse(neverAskedStatus.isPermanentlyDenied)

        // Test DENIED status
        val deniedStatus = UUPermissionStatus.DENIED
        assertFalse(deniedStatus.isGranted)
        assertTrue(deniedStatus.canRequest)
        assertFalse(deniedStatus.isPermanentlyDenied)

        // Test PERMANENTLY_DENIED status
        val permanentlyDeniedStatus = UUPermissionStatus.PERMANENTLY_DENIED
        assertFalse(permanentlyDeniedStatus.isGranted)
        assertFalse(permanentlyDeniedStatus.canRequest)
        assertTrue(permanentlyDeniedStatus.isPermanentlyDenied)

        // Test UNDETERMINED status
        val undeterminedStatus = UUPermissionStatus.UNDETERMINED
        assertFalse(undeterminedStatus.isGranted)
        assertFalse(undeterminedStatus.canRequest)
        assertFalse(undeterminedStatus.isPermanentlyDenied)
    }

    @Test
    fun `getPermissionStatus handles exceptions gracefully`()
    {
        // Given: An exception occurs during permission check
        val permission = "android.permission.CAMERA"
        contextCompatMock.`when`<Int> { ContextCompat.checkSelfPermission(mockActivity, permission) }.thenThrow(RuntimeException("Test exception"))
        Mockito.lenient().`when`(mockSharedPreferences.getBoolean("UU_HAS_REQUESTED_$permission", false)).thenReturn(true)

        // When: Getting permission status
        val status = permissions.getPermissionStatus(permission)

        // Then: Should return DENIED (fallback for hasPermission exception)
        assertEquals(UUPermissionStatus.DENIED, status)
    }

    @Test
    fun `getPermissionStatus handles SharedPreferences exception gracefully`()
    {
        // Given: An exception occurs during SharedPreferences access
        val permission = "android.permission.CAMERA"
        Mockito.lenient().`when`(mockSharedPreferences.getBoolean("UU_HAS_REQUESTED_$permission", false)).thenThrow(RuntimeException("Test exception"))

        // When: Getting permission status
        val status = permissions.getPermissionStatus(permission)

        // Then: Should return NEVER_ASKED (fallback for SharedPreferences exception)
        assertEquals(UUPermissionStatus.NEVER_ASKED, status)
    }

    @Test
    fun `getPermissionStatus handles shouldShowRationale exception gracefully`()
    {
        // Given: An exception occurs during shouldShowRationale check
        val permission = "android.permission.CAMERA"
        contextCompatMock.`when`<Int> { ContextCompat.checkSelfPermission(mockActivity, permission) }.thenReturn(PackageManager.PERMISSION_DENIED)
        Mockito.lenient().`when`(mockSharedPreferences.getBoolean("UU_HAS_REQUESTED_$permission", false)).thenReturn(true)
        Mockito.lenient().`when`(mockActivity.shouldShowRequestPermissionRationale(permission)).thenThrow(RuntimeException("Test exception"))

        // When: Getting permission status
        val status = permissions.getPermissionStatus(permission)

        // Then: Should return DENIED (fallback for shouldShowRationale exception in canRequestPermission)
        assertEquals(UUPermissionStatus.DENIED, status)
    }

    @Test
    fun `permission status consistency across multiple calls`()
    {
        // Given: A specific permission
        val permission = "android.permission.CAMERA"
        contextCompatMock.`when`<Int> { ContextCompat.checkSelfPermission(mockActivity, permission) }.thenReturn(PackageManager.PERMISSION_GRANTED)
        Mockito.lenient().`when`(mockSharedPreferences.getBoolean("UU_HAS_REQUESTED_$permission", false)).thenReturn(true)

        // When: Getting status multiple times
        val status1 = permissions.getPermissionStatus(permission)
        val status2 = permissions.getPermissionStatus(permission)
        val status3 = permissions.getPermissionStatus(permission)

        // Then: All calls should return the same status
        assertEquals(status1, status2)
        assertEquals(status2, status3)
        assertEquals(status1, status3)
    }

    @Test
    fun `getPermissionStatus handles invalid permission gracefully`()
    {
        // Given: An invalid permission string
        val invalidPermission = "invalid.permission.name"
        Mockito.lenient().`when`(mockSharedPreferences.getBoolean("UU_HAS_REQUESTED_$invalidPermission", false)).thenReturn(false)

        // When: Getting permission status
        val status = permissions.getPermissionStatus(invalidPermission)

        // Then: Should return a valid status (not crash)
        assertNotNull(status)
        assertTrue(status in UUPermissionStatus.entries.toTypedArray())
        assertEquals(UUPermissionStatus.NEVER_ASKED, status)
    }
}
