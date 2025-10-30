package com.silverpine.uu.ux

import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.edit

private const val PREFS_NAME = "com.silverpine.uu.ux.UUPermissions"

enum class UUPermissionStatus
{
    UNDETERMINED,
    NEVER_ASKED,
    GRANTED,
    DENIED,
    PERMANENTLY_DENIED;

    val canRequest: Boolean
        get() = this == NEVER_ASKED || this == DENIED

    val isGranted: Boolean
        get() = this == GRANTED

    val isPermanentlyDenied: Boolean
        get() = this == PERMANENTLY_DENIED
}

interface UUPermissionsProvider
{
    fun checkPermissionStatus(permission: String): UUPermissionStatus

    fun requestPermissions(permissions: Array<String>, completion: (Map<String, UUPermissionStatus>)->Unit)
}

fun UUPermissionsProvider.checkPermissionStatusMultiple(permissions: Array<String>): Map<String, UUPermissionStatus>
{
    return permissions.associate()
    { permission ->
        permission to checkPermissionStatus(permission)
    }
}

class UUPermissions(private val activity: ComponentActivity): UUPermissionsProvider
{
    private val prefs = activity.getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE)
    private var requestMultipleCompletion: ((Map<String, UUPermissionStatus>) -> Unit)? = null

    private val multiplePermissionsLauncher by lazy()
    {
        activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions(), this::handlePermissionResults)
    }

    private fun handlePermissionResults(results: Map<String, Boolean>)
    {
        val statusResults: MutableMap<String, UUPermissionStatus> = mutableMapOf()

        results.forEach()
        { permission, granted ->
            setHasRequestedPermission(permission)
            statusResults[permission] = if (granted) UUPermissionStatus.GRANTED else deniedStatus(permission)
        }

        val block = requestMultipleCompletion ?: return
        requestMultipleCompletion = null
        block(statusResults)
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

    override fun checkPermissionStatus(permission: String): UUPermissionStatus = runCatching()
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

    private fun hasPermission(permission: String): Result<Boolean> = runCatching()
    {
        (ContextCompat.checkSelfPermission(
            activity,
            permission
        ) == PackageManager.PERMISSION_GRANTED)
    }

    private fun canRequestPermission(permission: String): Result<Boolean> = runCatching()
    {
        !hasEverRequestedPermission(permission).getOrThrow() || shouldShowRationale(permission).getOrThrow()
    }

    override fun requestPermissions(permissions: Array<String>, completion: (Map<String, UUPermissionStatus>) -> Unit)
    {
        val permissionStatuses = checkPermissionStatusMultiple(permissions)

        // If all permissions are granted already, return success immediately
        if (permissionStatuses.all { it.value.isGranted })
        {
            completion(permissionStatuses)
            return
        }

        requestMultipleCompletion = completion
        multiplePermissionsLauncher.launch(permissions)
    }

    private fun prefKey(permission: String): String
    {
        return "HAS_REQUESTED_$permission"
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

    private fun shouldShowRationale(permission: String): Result<Boolean> = runCatching()
    {
        activity.shouldShowRequestPermissionRationale(permission)
    }
}