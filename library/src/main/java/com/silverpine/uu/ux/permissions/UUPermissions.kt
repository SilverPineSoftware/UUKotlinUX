package com.silverpine.uu.ux.permissions

import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import com.silverpine.uu.ux.UUActivityLauncher

private const val PREFS_NAME = "com.silverpine.uu.ux.UUPermissions"

object UUPermissions:
    UUActivityLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>(ActivityResultContracts.RequestMultiplePermissions()),
    UUPermissionProvider
{
    private val prefs by lazy {
        requireActivity().getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE)
    }

    private var completionBlock: ((Map<String, UUPermissionStatus>) -> Unit)? = null

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // UUActivityLauncher
    ////////////////////////////////////////////////////////////////////////////////////////////////

    override fun handleLaunchResult(result: Map<String, @JvmSuppressWildcards Boolean>)
    {
        val statusResults: MutableMap<String, UUPermissionStatus> = mutableMapOf()

        result.forEach()
        { permission, granted ->
            setHasRequestedPermission(permission)
            statusResults[permission] = if (granted) UUPermissionStatus.GRANTED else deniedStatus(permission)
        }

        val block = completionBlock ?: return
        completionBlock = null
        block(statusResults)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // UUPermissionProvider
    ////////////////////////////////////////////////////////////////////////////////////////////////

    override fun getPermissionStatus(permission: String): UUPermissionStatus = runCatching()
    {
        // Check if permission has ever been requested
        val hasEverRequested = hasEverRequestedPermission(permission).getOrElse { return@runCatching UUPermissionStatus.NEVER_ASKED }
        
        if (!hasEverRequested)
        {
            UUPermissionStatus.NEVER_ASKED
        }
        else
        {
            // Check if permission is currently granted
            val isGranted = hasPermission(permission).getOrElse { return@runCatching UUPermissionStatus.DENIED }
            
            if (isGranted)
            {
                UUPermissionStatus.GRANTED
            }
            else
            {
                deniedStatus(permission)
            }
        }
    }.getOrDefault(UUPermissionStatus.UNDETERMINED)

    override fun requestPermissions(permissions: Array<String>, completion: (Map<String, UUPermissionStatus>) -> Unit)
    {
        val permissionStatuses = getPermissionStatusMultiple(permissions)

        // If all permissions are granted already, return success immediately
        if (permissionStatuses.all { it.value.isGranted })
        {
            completion(permissionStatuses)
            return
        }

        completionBlock = completion
        launch(permissions)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Private Methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private fun prefKey(permission: String): String
    {
        return "UU_HAS_REQUESTED_$permission"
    }

    private fun hasEverRequestedPermission(permission: String): Result<Boolean> = runCatching()
    {
        prefs.getBoolean(prefKey(permission), false)
    }

    private fun setHasRequestedPermission(permission: String)
    {
        prefs.edit(commit = true)
        {
            putBoolean(prefKey(permission), true)
        }
    }

    private fun hasPermission(permission: String): Result<Boolean> = runCatching()
    {
        (ContextCompat.checkSelfPermission(
            requireActivity(),
            permission
        ) == PackageManager.PERMISSION_GRANTED)
    }

    private fun canRequestPermission(permission: String): Result<Boolean> = runCatching()
    {
        !hasEverRequestedPermission(permission).getOrThrow() || shouldShowRationale(permission).getOrThrow()
    }

    private fun shouldShowRationale(permission: String): Result<Boolean> = runCatching()
    {
        requireActivity().shouldShowRequestPermissionRationale(permission)
    }

    private fun deniedStatus(permission: String): UUPermissionStatus
    {
        val canRequest = canRequestPermission(permission).getOrElse { return UUPermissionStatus.DENIED }

        return if (canRequest)
        {
            UUPermissionStatus.DENIED
        }
        else
        {
            UUPermissionStatus.PERMANENTLY_DENIED
        }
    }
}