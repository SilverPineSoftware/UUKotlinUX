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
import com.silverpine.uu.ux.permissions.combinedPermissionStatus
import com.silverpine.uu.ux.permissions.getPermissionStatusMultiple
import com.silverpine.uu.ux.permissions.uuCombinedStatus
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension

/**
 * JUnit 5 unit tests for UUPermissions class using Mockito.
 * 
 * These tests mock all Android dependencies and test the business logic
 * without requiring a real Android device or emulator.
 */
@ExtendWith(MockitoExtension::class)
class UUPermissionsTests
{
    private data class Quad<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

    private fun buildActivityWithPrefs(): Quad<ComponentActivity, Context, SharedPreferences, SharedPreferences.Editor>
    {
        val activity = Mockito.mock(ComponentActivity::class.java)
        val context = Mockito.mock(Context::class.java)
        val prefs = Mockito.mock(SharedPreferences::class.java)
        val editor = Mockito.mock(SharedPreferences.Editor::class.java)

        Mockito.lenient().`when`(activity.applicationContext).thenReturn(context)
        Mockito.lenient().`when`(activity.getSharedPreferences(Mockito.anyString(), Mockito.anyInt())).thenReturn(prefs)
        Mockito.lenient().`when`(prefs.edit()).thenReturn(editor)
        Mockito.lenient().`when`(editor.putBoolean(Mockito.anyString(), Mockito.anyBoolean())).thenReturn(editor)
        Mockito.lenient().`when`(editor.commit()).thenReturn(true)

        @Suppress("UNCHECKED_CAST")
        val launcher = Mockito.mock(ActivityResultLauncher::class.java) as ActivityResultLauncher<Array<String>>
        Mockito.lenient().`when`(activity.registerForActivityResult(
            Mockito.any<ActivityResultContract<Array<String>, Map<String, Boolean>>>(),
            Mockito.any<ActivityResultCallback<Map<String, Boolean>>>()
        )).thenReturn(launcher)

        return Quad(activity, context, prefs, editor)
    }

    private fun obtainSingletonPrefs(): SharedPreferences
    {
        val field = UUPermissions::class.java.getDeclaredField("prefs\$delegate")
        field.isAccessible = true
        @Suppress("UNCHECKED_CAST")
        val lazyDelegate = field.get(UUPermissions) as kotlin.Lazy<SharedPreferences>
        return lazyDelegate.value
    }

    @Test
    fun `getPermissionStatus returns GRANTED when permission is granted`()
    {
        val permission = "android.permission.CAMERA"
        val (activity, _, prefs, _) = buildActivityWithPrefs()

        Mockito.mockStatic(ContextCompat::class.java).use { cc ->
            cc.`when`<Int> { ContextCompat.checkSelfPermission(activity, permission) }.thenReturn(PackageManager.PERMISSION_GRANTED)

            UUPermissions.init(activity)
            val sp = obtainSingletonPrefs()
            Mockito.reset(sp)
            Mockito.lenient().`when`(sp.getBoolean("UU_HAS_REQUESTED_$permission", false)).thenReturn(true)
            val permissions: UUPermissionProvider = UUPermissions
            val status = permissions.getPermissionStatus(permission)

        // Then: Should return GRANTED
        assertEquals(UUPermissionStatus.GRANTED, status)
        assertTrue(status.isGranted)
        assertFalse(status.canRequest)
        }
    }

    @Test
    fun `getPermissionStatus returns NEVER_ASKED when permission never requested`()
    {
        val permission = "android.permission.ACCESS_FINE_LOCATION"
        val (activity, _, prefs, _) = buildActivityWithPrefs()

        Mockito.mockStatic(ContextCompat::class.java).use { _ ->
            UUPermissions.init(activity)
            val sp = obtainSingletonPrefs()
            Mockito.reset(sp)
            Mockito.lenient().`when`(sp.getBoolean("UU_HAS_REQUESTED_$permission", false)).thenReturn(false)
            val permissions: UUPermissionProvider = UUPermissions
            val status = permissions.getPermissionStatus(permission)

        // Then: Should return NEVER_ASKED
        assertEquals(UUPermissionStatus.NEVER_ASKED, status)
        assertFalse(status.isGranted)
        assertTrue(status.canRequest)
        }
    }

    @Test
    fun `getPermissionStatus returns DENIED when permission denied but can request again`()
    {
        val permission = "android.permission.CAMERA"
        val (activity, _, prefs, _) = buildActivityWithPrefs()

        Mockito.mockStatic(ContextCompat::class.java).use { cc ->
            cc.`when`<Int> { ContextCompat.checkSelfPermission(activity, permission) }.thenReturn(PackageManager.PERMISSION_DENIED)
            Mockito.lenient().`when`(prefs.getBoolean("UU_HAS_REQUESTED_$permission", false)).thenReturn(true)
            Mockito.lenient().`when`(activity.shouldShowRequestPermissionRationale(permission)).thenReturn(true)

            UUPermissions.init(activity)
            val permissions: UUPermissionProvider = UUPermissions
            val status = permissions.getPermissionStatus(permission)

        // Then: Should return DENIED
        assertEquals(UUPermissionStatus.DENIED, status)
        assertFalse(status.isGranted)
        assertTrue(status.canRequest)
        }
    }

    @Test
    fun `getPermissionStatus returns PERMANENTLY_DENIED when permission permanently denied`()
    {
        val permission = "android.permission.CAMERA"
        val (activity, _, prefs, _) = buildActivityWithPrefs()

        Mockito.mockStatic(ContextCompat::class.java).use { cc ->
            cc.`when`<Int> { ContextCompat.checkSelfPermission(activity, permission) }.thenReturn(PackageManager.PERMISSION_DENIED)
            Mockito.lenient().`when`(prefs.getBoolean("UU_HAS_REQUESTED_$permission", false)).thenReturn(true)
            Mockito.lenient().`when`(activity.shouldShowRequestPermissionRationale(permission)).thenReturn(false)

            UUPermissions.init(activity)
            val permissions: UUPermissionProvider = UUPermissions
            val status = permissions.getPermissionStatus(permission)

        // Then: Should return PERMANENTLY_DENIED
        assertEquals(UUPermissionStatus.PERMANENTLY_DENIED, status)
        assertFalse(status.isGranted)
        assertFalse(status.canRequest)
        assertTrue(status.isPermanentlyDenied)
        }
    }

    @Test
    fun `getPermissionStatusMultiple returns correct statuses for multiple permissions`()
    {
        // Given: Multiple permissions with different states
        val perms = arrayOf(
            "android.permission.CAMERA",
            "android.permission.ACCESS_FINE_LOCATION"
        )
        val (activity, _, prefs, _) = buildActivityWithPrefs()

        Mockito.mockStatic(ContextCompat::class.java).use { cc ->
            cc.`when`<Int> { ContextCompat.checkSelfPermission(activity, "android.permission.CAMERA") }.thenReturn(PackageManager.PERMISSION_GRANTED)

            UUPermissions.init(activity)
            val sp = obtainSingletonPrefs()
            Mockito.reset(sp)
            Mockito.lenient().`when`(sp.getBoolean("UU_HAS_REQUESTED_android.permission.CAMERA", false)).thenReturn(true)
            Mockito.lenient().`when`(sp.getBoolean("UU_HAS_REQUESTED_android.permission.ACCESS_FINE_LOCATION", false)).thenReturn(false)
            val permissions: UUPermissionProvider = UUPermissions
            val statuses = permissions.getPermissionStatusMultiple(perms)

        // Then: Each permission should have correct status
        assertEquals(UUPermissionStatus.GRANTED, statuses["android.permission.CAMERA"])
        assertEquals(UUPermissionStatus.NEVER_ASKED, statuses["android.permission.ACCESS_FINE_LOCATION"])
        assertEquals(2, statuses.size)
        }
    }

    @Test
    fun `areAllPermissionsGranted returns true when all permissions are granted`()
    {
        // Given: All permissions are granted
        val perms = arrayOf(
            "android.permission.CAMERA",
            "android.permission.ACCESS_FINE_LOCATION"
        )
        val (activity, _, prefs, _) = buildActivityWithPrefs()

        Mockito.mockStatic(ContextCompat::class.java).use { cc ->
            perms.forEach { permission ->
                cc.`when`<Int> { ContextCompat.checkSelfPermission(activity, permission) }.thenReturn(PackageManager.PERMISSION_GRANTED)
            }

            UUPermissions.init(activity)
            val sp = obtainSingletonPrefs()
            Mockito.reset(sp)
            perms.forEach { permission ->
                Mockito.lenient().`when`(sp.getBoolean("UU_HAS_REQUESTED_$permission", false)).thenReturn(true)
            }
            val permissions: UUPermissionProvider = UUPermissions
            val allGranted = permissions.areAllPermissionsGranted(perms)

        // Then: Should return true
        assertTrue(allGranted)
        }
    }

    @Test
    fun `areAllPermissionsGranted returns false when some permissions are not granted`()
    {
        // Given: Mix of granted and non-granted permissions
        val perms = arrayOf(
            "android.permission.CAMERA",
            "android.permission.ACCESS_FINE_LOCATION"
        )
        val (activity, _, prefs, _) = buildActivityWithPrefs()

        Mockito.mockStatic(ContextCompat::class.java).use { cc ->
            cc.`when`<Int> { ContextCompat.checkSelfPermission(activity, "android.permission.CAMERA") }.thenReturn(PackageManager.PERMISSION_GRANTED)
            Mockito.lenient().`when`(prefs.getBoolean("UU_HAS_REQUESTED_android.permission.CAMERA", false)).thenReturn(true)
            cc.`when`<Int> { ContextCompat.checkSelfPermission(activity, "android.permission.ACCESS_FINE_LOCATION") }.thenReturn(PackageManager.PERMISSION_DENIED)
            Mockito.lenient().`when`(prefs.getBoolean("UU_HAS_REQUESTED_android.permission.ACCESS_FINE_LOCATION", false)).thenReturn(false)

            UUPermissions.init(activity)
            val permissions: UUPermissionProvider = UUPermissions
            val allGranted = permissions.areAllPermissionsGranted(perms)

        // Then: Should return false
        assertFalse(allGranted)
        }
    }

    @Test
    fun `areAnyPermissionsPermanentlyDenied returns true when any permission is permanently denied`()
    {
        // Given: One permission is permanently denied
        val perms = arrayOf(
            "android.permission.CAMERA",
            "android.permission.ACCESS_FINE_LOCATION"
        )
        val (activity, _, prefs, _) = buildActivityWithPrefs()

        Mockito.mockStatic(ContextCompat::class.java).use { cc ->
            cc.`when`<Int> { ContextCompat.checkSelfPermission(activity, "android.permission.CAMERA") }.thenReturn(PackageManager.PERMISSION_GRANTED)
            UUPermissions.init(activity)
            val sp = obtainSingletonPrefs()
            Mockito.reset(sp)
            Mockito.lenient().`when`(sp.getBoolean("UU_HAS_REQUESTED_android.permission.CAMERA", false)).thenReturn(true)
            cc.`when`<Int> { ContextCompat.checkSelfPermission(activity, "android.permission.ACCESS_FINE_LOCATION") }.thenReturn(PackageManager.PERMISSION_DENIED)
            Mockito.lenient().`when`(sp.getBoolean("UU_HAS_REQUESTED_android.permission.ACCESS_FINE_LOCATION", false)).thenReturn(true)
            Mockito.lenient().`when`(activity.shouldShowRequestPermissionRationale("android.permission.ACCESS_FINE_LOCATION")).thenReturn(false)

            val permissions: UUPermissionProvider = UUPermissions
            val anyPermanentlyDenied = permissions.areAnyPermissionsPermanentlyDenied(perms)

        // Then: Should return true
        assertTrue(anyPermanentlyDenied)
        }
    }

    @Test
    fun `areAnyPermissionsPermanentlyDenied returns false when no permissions are permanently denied`()
    {
        // Given: No permissions are permanently denied
        val perms = arrayOf(
            "android.permission.CAMERA",
            "android.permission.ACCESS_FINE_LOCATION"
        )
        val (activity, _, prefs, _) = buildActivityWithPrefs()

        Mockito.mockStatic(ContextCompat::class.java).use { cc ->
            cc.`when`<Int> { ContextCompat.checkSelfPermission(activity, "android.permission.CAMERA") }.thenReturn(PackageManager.PERMISSION_GRANTED)
            Mockito.lenient().`when`(prefs.getBoolean("UU_HAS_REQUESTED_android.permission.CAMERA", false)).thenReturn(true)
            cc.`when`<Int> { ContextCompat.checkSelfPermission(activity, "android.permission.ACCESS_FINE_LOCATION") }.thenReturn(PackageManager.PERMISSION_DENIED)
            Mockito.lenient().`when`(prefs.getBoolean("UU_HAS_REQUESTED_android.permission.ACCESS_FINE_LOCATION", false)).thenReturn(true)
            Mockito.lenient().`when`(activity.shouldShowRequestPermissionRationale("android.permission.ACCESS_FINE_LOCATION")).thenReturn(true)

            UUPermissions.init(activity)
            val permissions: UUPermissionProvider = UUPermissions
            val anyPermanentlyDenied = permissions.areAnyPermissionsPermanentlyDenied(perms)

        // Then: Should return false
        assertFalse(anyPermanentlyDenied)
        }
    }

    @Test
    fun `requestPermissions with empty array calls completion immediately`()
    {
        // Given: Empty permissions array
        val perms = emptyArray<String>()
        val (activity, _, _, _) = buildActivityWithPrefs()

        Mockito.mockStatic(ContextCompat::class.java).use { _ ->
            UUPermissions.init(activity)
            val permissions: UUPermissionProvider = UUPermissions

            var completionCalled = false
            var resultStatuses: Map<String, UUPermissionStatus>? = null
            permissions.requestPermissions(perms) { statuses ->
                completionCalled = true
                resultStatuses = statuses
            }

            assertTrue(completionCalled)
            assertNotNull(resultStatuses)
            assertTrue(resultStatuses?.isEmpty() == true)
        }
    }

    @Test
    fun `requestPermissions calls completion immediately when all permissions are granted`()
    {
        // Given: All permissions are already granted
        val perms = arrayOf(
            "android.permission.CAMERA",
            "android.permission.ACCESS_FINE_LOCATION"
        )
        val (activity, _, prefs, _) = buildActivityWithPrefs()

        Mockito.mockStatic(ContextCompat::class.java).use { cc ->
            cc.`when`<Int> { ContextCompat.checkSelfPermission(activity, "android.permission.CAMERA") }.thenReturn(PackageManager.PERMISSION_GRANTED)
            Mockito.lenient().`when`(prefs.getBoolean("UU_HAS_REQUESTED_android.permission.CAMERA", false)).thenReturn(true)
            cc.`when`<Int> { ContextCompat.checkSelfPermission(activity, "android.permission.ACCESS_FINE_LOCATION") }.thenReturn(PackageManager.PERMISSION_GRANTED)
            Mockito.lenient().`when`(prefs.getBoolean("UU_HAS_REQUESTED_android.permission.ACCESS_FINE_LOCATION", false)).thenReturn(true)

            UUPermissions.init(activity)
            val permissions: UUPermissionProvider = UUPermissions

            var completionCalled = false
            var resultStatuses: Map<String, UUPermissionStatus>? = null
            permissions.requestPermissions(perms) { statuses ->
                completionCalled = true
                resultStatuses = statuses
            }

            assertTrue(completionCalled)
            assertNotNull(resultStatuses)
            assertEquals(2, resultStatuses?.size)
            assertEquals(UUPermissionStatus.GRANTED, resultStatuses?.get("android.permission.CAMERA"))
            assertEquals(UUPermissionStatus.GRANTED, resultStatuses?.get("android.permission.ACCESS_FINE_LOCATION"))
        }
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
        val permission = "android.permission.CAMERA"
        val (activity, _, prefs, _) = buildActivityWithPrefs()

        Mockito.mockStatic(ContextCompat::class.java).use { cc ->
            cc.`when`<Int> { ContextCompat.checkSelfPermission(activity, permission) }.thenThrow(RuntimeException("Test exception"))
            Mockito.lenient().`when`(prefs.getBoolean("UU_HAS_REQUESTED_$permission", false)).thenReturn(true)

            UUPermissions.init(activity)
            val permissions: UUPermissionProvider = UUPermissions
            val status = permissions.getPermissionStatus(permission)

        // Then: Should return DENIED (fallback for hasPermission exception)
        assertEquals(UUPermissionStatus.DENIED, status)
        }
    }

    @Test
    fun `getPermissionStatus handles SharedPreferences exception gracefully`()
    {
        val permission = "android.permission.CAMERA"
        val (activity, _, prefs, _) = buildActivityWithPrefs()

        Mockito.mockStatic(ContextCompat::class.java).use { cc ->
            cc.`when`<Int> { ContextCompat.checkSelfPermission(activity, permission) }.thenReturn(PackageManager.PERMISSION_DENIED)
            Mockito.lenient().`when`(activity.shouldShowRequestPermissionRationale(permission)).thenReturn(false)

            UUPermissions.init(activity)
            val sp = obtainSingletonPrefs()
            Mockito.reset(sp)
            Mockito.lenient().`when`(sp.getBoolean("UU_HAS_REQUESTED_$permission", false)).thenThrow(RuntimeException("Test exception"))
            val permissions: UUPermissionProvider = UUPermissions
            val status = permissions.getPermissionStatus(permission)

            assertEquals(UUPermissionStatus.NEVER_ASKED, status)
        }
    }

    @Test
    fun `getPermissionStatus handles shouldShowRationale exception gracefully`()
    {
        val permission = "android.permission.CAMERA"
        val (activity, _, prefs, _) = buildActivityWithPrefs()

        Mockito.mockStatic(ContextCompat::class.java).use { cc ->
            cc.`when`<Int> { ContextCompat.checkSelfPermission(activity, permission) }.thenReturn(PackageManager.PERMISSION_DENIED)
            Mockito.lenient().`when`(activity.shouldShowRequestPermissionRationale(permission)).thenThrow(RuntimeException("Test exception"))

            UUPermissions.init(activity)
            val sp = obtainSingletonPrefs()
            Mockito.reset(sp)
            Mockito.lenient().`when`(sp.getBoolean("UU_HAS_REQUESTED_$permission", false)).thenReturn(true)
            val permissions: UUPermissionProvider = UUPermissions
            val status = permissions.getPermissionStatus(permission)

        // Then: Should return DENIED (fallback for shouldShowRationale exception in canRequestPermission)
        assertEquals(UUPermissionStatus.DENIED, status)
        }
    }

    @Test
    fun `permission status consistency across multiple calls`()
    {
        val permission = "android.permission.CAMERA"
        val (activity, _, prefs, _) = buildActivityWithPrefs()

        Mockito.mockStatic(ContextCompat::class.java).use { cc ->
            cc.`when`<Int> { ContextCompat.checkSelfPermission(activity, permission) }.thenReturn(PackageManager.PERMISSION_GRANTED)
            Mockito.lenient().`when`(prefs.getBoolean("UU_HAS_REQUESTED_$permission", false)).thenReturn(true)

            UUPermissions.init(activity)
            val permissions: UUPermissionProvider = UUPermissions
            val status1 = permissions.getPermissionStatus(permission)
            val status2 = permissions.getPermissionStatus(permission)
            val status3 = permissions.getPermissionStatus(permission)

        // Then: All calls should return the same status
        assertEquals(status1, status2)
        assertEquals(status2, status3)
        assertEquals(status1, status3)
        }
    }

    @Test
    fun `getPermissionStatus handles invalid permission gracefully`()
    {
        val invalidPermission = "invalid.permission.name"
        val (activity, _, prefs, _) = buildActivityWithPrefs()

        Mockito.mockStatic(ContextCompat::class.java).use { _ ->
            Mockito.lenient().`when`(prefs.getBoolean("UU_HAS_REQUESTED_$invalidPermission", false)).thenReturn(false)

            UUPermissions.init(activity)
            val permissions: UUPermissionProvider = UUPermissions
            val status = permissions.getPermissionStatus(invalidPermission)

        // Then: Should return a valid status (not crash)
        assertNotNull(status)
        assertTrue(status in UUPermissionStatus.entries.toTypedArray())
        assertEquals(UUPermissionStatus.NEVER_ASKED, status)
        }
    }

    // ==================== uuCombinedStatus Tests ====================

    @Test
    fun `uuCombinedStatus returns GRANTED when all permissions are granted`()
    {
        // Given: All permissions are granted
        val statuses = mapOf(
            "android.permission.CAMERA" to UUPermissionStatus.GRANTED,
            "android.permission.WRITE_EXTERNAL_STORAGE" to UUPermissionStatus.GRANTED,
            "android.permission.ACCESS_FINE_LOCATION" to UUPermissionStatus.GRANTED
        )

        // When: Computing combined status
        val combined = statuses.uuCombinedStatus

        // Then: Should return GRANTED
        assertEquals(UUPermissionStatus.GRANTED, combined)
        assertTrue(combined.isGranted)
    }

    @Test
    fun `uuCombinedStatus returns PERMANENTLY_DENIED when any permission is permanently denied`()
    {
        // Given: Mix of statuses with one permanently denied
        val statuses = mapOf(
            "android.permission.CAMERA" to UUPermissionStatus.GRANTED,
            "android.permission.WRITE_EXTERNAL_STORAGE" to UUPermissionStatus.PERMANENTLY_DENIED,
            "android.permission.ACCESS_FINE_LOCATION" to UUPermissionStatus.DENIED
        )

        // When: Computing combined status
        val combined = statuses.uuCombinedStatus

        // Then: Should return PERMANENTLY_DENIED (has priority)
        assertEquals(UUPermissionStatus.PERMANENTLY_DENIED, combined)
        assertTrue(combined.isPermanentlyDenied)
        assertFalse(combined.canRequest)
    }

    @Test
    fun `uuCombinedStatus returns NEVER_ASKED when all permissions are never asked`()
    {
        // Given: All permissions are never asked
        val statuses = mapOf(
            "android.permission.CAMERA" to UUPermissionStatus.NEVER_ASKED,
            "android.permission.WRITE_EXTERNAL_STORAGE" to UUPermissionStatus.NEVER_ASKED
        )

        // When: Computing combined status
        val combined = statuses.uuCombinedStatus

        // Then: Should return NEVER_ASKED
        assertEquals(UUPermissionStatus.NEVER_ASKED, combined)
        assertTrue(combined.canRequest)
        assertFalse(combined.isGranted)
    }

    @Test
    fun `uuCombinedStatus returns DENIED when mixed statuses with no permanently denied`()
    {
        // Given: Mix of statuses without permanently denied
        val statuses = mapOf(
            "android.permission.CAMERA" to UUPermissionStatus.GRANTED,
            "android.permission.WRITE_EXTERNAL_STORAGE" to UUPermissionStatus.DENIED,
            "android.permission.ACCESS_FINE_LOCATION" to UUPermissionStatus.NEVER_ASKED
        )

        // When: Computing combined status
        val combined = statuses.uuCombinedStatus

        // Then: Should return DENIED (default for mixed non-permanently-denied states)
        assertEquals(UUPermissionStatus.DENIED, combined)
        assertTrue(combined.canRequest)
        assertFalse(combined.isGranted)
        assertFalse(combined.isPermanentlyDenied)
    }

    @Test
    fun `uuCombinedStatus returns DENIED when all permissions are denied`()
    {
        // Given: All permissions are denied
        val statuses = mapOf(
            "android.permission.CAMERA" to UUPermissionStatus.DENIED,
            "android.permission.WRITE_EXTERNAL_STORAGE" to UUPermissionStatus.DENIED
        )

        // When: Computing combined status
        val combined = statuses.uuCombinedStatus

        // Then: Should return DENIED
        assertEquals(UUPermissionStatus.DENIED, combined)
        assertTrue(combined.canRequest)
        assertFalse(combined.isGranted)
    }

    @Test
    fun `uuCombinedStatus handles empty map`()
    {
        // Given: Empty map
        val statuses = emptyMap<String, UUPermissionStatus>()

        // When: Computing combined status
        val combined = statuses.uuCombinedStatus

        // Then: Should return GRANTED (all values are granted when empty)
        assertEquals(UUPermissionStatus.GRANTED, combined)
        assertTrue(combined.isGranted)
    }

    @Test
    fun `uuCombinedStatus prioritizes PERMANENTLY_DENIED over other non-granted states`()
    {
        // Given: Mix with permanently denied taking priority
        val statuses = mapOf(
            "android.permission.CAMERA" to UUPermissionStatus.NEVER_ASKED,
            "android.permission.WRITE_EXTERNAL_STORAGE" to UUPermissionStatus.PERMANENTLY_DENIED,
            "android.permission.ACCESS_FINE_LOCATION" to UUPermissionStatus.DENIED,
            "android.permission.RECORD_AUDIO" to UUPermissionStatus.NEVER_ASKED
        )

        // When: Computing combined status
        val combined = statuses.uuCombinedStatus

        // Then: Should return PERMANENTLY_DENIED (has highest priority for non-granted)
        assertEquals(UUPermissionStatus.PERMANENTLY_DENIED, combined)
    }

    // ==================== combinedPermissionStatus Tests ====================

    @Test
    fun `combinedPermissionStatus returns GRANTED when all permissions are granted`()
    {
        // Given: All permissions are granted
        val perms = arrayOf(
            "android.permission.CAMERA",
            "android.permission.WRITE_EXTERNAL_STORAGE"
        )
        val (activity, _, _, _) = buildActivityWithPrefs()

        Mockito.mockStatic(ContextCompat::class.java).use { cc ->
            perms.forEach { permission ->
                cc.`when`<Int> { ContextCompat.checkSelfPermission(activity, permission) }.thenReturn(PackageManager.PERMISSION_GRANTED)
            }

            UUPermissions.init(activity)
            val sp = obtainSingletonPrefs()
            Mockito.reset(sp)
            perms.forEach { permission ->
                Mockito.lenient().`when`(sp.getBoolean("UU_HAS_REQUESTED_$permission", false)).thenReturn(true)
            }
            val permissions: UUPermissionProvider = UUPermissions
            val combined = permissions.combinedPermissionStatus(perms)

            // Then: Should return GRANTED
            assertEquals(UUPermissionStatus.GRANTED, combined)
            assertTrue(combined.isGranted)
        }
    }

    @Test
    fun `combinedPermissionStatus returns PERMANENTLY_DENIED when any permission is permanently denied`()
    {
        // Given: Mix with one permanently denied
        val perms = arrayOf(
            "android.permission.CAMERA",
            "android.permission.ACCESS_FINE_LOCATION"
        )
        val (activity, _, _, _) = buildActivityWithPrefs()

        Mockito.mockStatic(ContextCompat::class.java).use { cc ->
            cc.`when`<Int> { ContextCompat.checkSelfPermission(activity, "android.permission.CAMERA") }.thenReturn(PackageManager.PERMISSION_GRANTED)
            cc.`when`<Int> { ContextCompat.checkSelfPermission(activity, "android.permission.ACCESS_FINE_LOCATION") }.thenReturn(PackageManager.PERMISSION_DENIED)

            UUPermissions.init(activity)
            val sp = obtainSingletonPrefs()
            Mockito.reset(sp)
            Mockito.lenient().`when`(sp.getBoolean("UU_HAS_REQUESTED_android.permission.CAMERA", false)).thenReturn(true)
            Mockito.lenient().`when`(sp.getBoolean("UU_HAS_REQUESTED_android.permission.ACCESS_FINE_LOCATION", false)).thenReturn(true)
            Mockito.lenient().`when`(activity.shouldShowRequestPermissionRationale("android.permission.ACCESS_FINE_LOCATION")).thenReturn(false)

            val permissions: UUPermissionProvider = UUPermissions
            val combined = permissions.combinedPermissionStatus(perms)

            // Then: Should return PERMANENTLY_DENIED
            assertEquals(UUPermissionStatus.PERMANENTLY_DENIED, combined)
            assertTrue(combined.isPermanentlyDenied)
            assertFalse(combined.canRequest)
        }
    }

    @Test
    fun `combinedPermissionStatus returns NEVER_ASKED when all permissions are never asked`()
    {
        // Given: All permissions never asked
        val perms = arrayOf(
            "android.permission.CAMERA",
            "android.permission.ACCESS_FINE_LOCATION"
        )
        val (activity, _, _, _) = buildActivityWithPrefs()

        Mockito.mockStatic(ContextCompat::class.java).use { _ ->
            UUPermissions.init(activity)
            val sp = obtainSingletonPrefs()
            Mockito.reset(sp)
            perms.forEach { permission ->
                Mockito.lenient().`when`(sp.getBoolean("UU_HAS_REQUESTED_$permission", false)).thenReturn(false)
            }
            val permissions: UUPermissionProvider = UUPermissions
            val combined = permissions.combinedPermissionStatus(perms)

            // Then: Should return NEVER_ASKED
            assertEquals(UUPermissionStatus.NEVER_ASKED, combined)
            assertTrue(combined.canRequest)
            assertFalse(combined.isGranted)
        }
    }

    @Test
    fun `combinedPermissionStatus returns DENIED when mixed statuses`()
    {
        // Given: Mix of granted and denied permissions
        val perms = arrayOf(
            "android.permission.CAMERA",
            "android.permission.ACCESS_FINE_LOCATION"
        )
        val (activity, _, _, _) = buildActivityWithPrefs()

        Mockito.mockStatic(ContextCompat::class.java).use { cc ->
            cc.`when`<Int> { ContextCompat.checkSelfPermission(activity, "android.permission.CAMERA") }.thenReturn(PackageManager.PERMISSION_GRANTED)
            cc.`when`<Int> { ContextCompat.checkSelfPermission(activity, "android.permission.ACCESS_FINE_LOCATION") }.thenReturn(PackageManager.PERMISSION_DENIED)

            UUPermissions.init(activity)
            val sp = obtainSingletonPrefs()
            Mockito.reset(sp)
            Mockito.lenient().`when`(sp.getBoolean("UU_HAS_REQUESTED_android.permission.CAMERA", false)).thenReturn(true)
            Mockito.lenient().`when`(sp.getBoolean("UU_HAS_REQUESTED_android.permission.ACCESS_FINE_LOCATION", false)).thenReturn(true)
            Mockito.lenient().`when`(activity.shouldShowRequestPermissionRationale("android.permission.ACCESS_FINE_LOCATION")).thenReturn(true)

            val permissions: UUPermissionProvider = UUPermissions
            val combined = permissions.combinedPermissionStatus(perms)

            // Then: Should return DENIED (default for mixed states)
            assertEquals(UUPermissionStatus.DENIED, combined)
            assertTrue(combined.canRequest)
            assertFalse(combined.isGranted)
        }
    }

    @Test
    fun `combinedPermissionStatus handles empty array`()
    {
        // Given: Empty permissions array
        val perms = emptyArray<String>()
        val (activity, _, _, _) = buildActivityWithPrefs()

        Mockito.mockStatic(ContextCompat::class.java).use { _ ->
            UUPermissions.init(activity)
            val permissions: UUPermissionProvider = UUPermissions
            val combined = permissions.combinedPermissionStatus(perms)

            // Then: Should return GRANTED (empty map means all granted)
            assertEquals(UUPermissionStatus.GRANTED, combined)
            assertTrue(combined.isGranted)
        }
    }

    @Test
    fun `combinedPermissionStatus uses uuCombinedStatus internally`()
    {
        // Given: Multiple permissions with different states
        val perms = arrayOf(
            "android.permission.CAMERA",
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.RECORD_AUDIO"
        )
        val (activity, _, _, _) = buildActivityWithPrefs()

        Mockito.mockStatic(ContextCompat::class.java).use { cc ->
            cc.`when`<Int> { ContextCompat.checkSelfPermission(activity, "android.permission.CAMERA") }.thenReturn(PackageManager.PERMISSION_GRANTED)
            cc.`when`<Int> { ContextCompat.checkSelfPermission(activity, "android.permission.ACCESS_FINE_LOCATION") }.thenReturn(PackageManager.PERMISSION_DENIED)
            cc.`when`<Int> { ContextCompat.checkSelfPermission(activity, "android.permission.RECORD_AUDIO") }.thenReturn(PackageManager.PERMISSION_DENIED)

            UUPermissions.init(activity)
            val sp = obtainSingletonPrefs()
            Mockito.reset(sp)
            Mockito.lenient().`when`(sp.getBoolean("UU_HAS_REQUESTED_android.permission.CAMERA", false)).thenReturn(true)
            Mockito.lenient().`when`(sp.getBoolean("UU_HAS_REQUESTED_android.permission.ACCESS_FINE_LOCATION", false)).thenReturn(false)
            Mockito.lenient().`when`(sp.getBoolean("UU_HAS_REQUESTED_android.permission.RECORD_AUDIO", false)).thenReturn(true)
            Mockito.lenient().`when`(activity.shouldShowRequestPermissionRationale("android.permission.RECORD_AUDIO")).thenReturn(true)

            val permissions: UUPermissionProvider = UUPermissions
            val statuses = permissions.getPermissionStatusMultiple(perms)
            val combinedFromStatuses = statuses.uuCombinedStatus
            val combinedFromFunction = permissions.combinedPermissionStatus(perms)

            // Then: Both should return the same value
            assertEquals(combinedFromStatuses, combinedFromFunction)
            assertEquals(UUPermissionStatus.DENIED, combinedFromFunction)
        }
    }
}
