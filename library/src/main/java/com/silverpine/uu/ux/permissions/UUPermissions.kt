package com.silverpine.uu.ux.permissions

import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.edit

private const val PREFS_NAME = "com.silverpine.uu.ux.UUPermissions"

object UUPermissions: UUPermissionProvider
{
    private var activity: ComponentActivity? = null
    private var launcher:  ActivityResultLauncher<Array<String>>? = null

    fun init(activity: ComponentActivity)
    {
        this.activity = activity
        this.launcher = activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
            this::handlePermissionResults
        )
    }

    private fun requireActivity(): ComponentActivity
    {
        val activity = this.activity
        if (activity == null)
        {
            throw RuntimeException("UUPermissions not initialized. Must call UUPermissions.init(context) on app startup.")
        }

        return activity
    }

    private val prefs by lazy {
        requireActivity().getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE)
    }

    /*private val multiplePermissionsLauncher by lazy {
        requireActivity().registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
            this::handlePermissionResults
        )
    }*/

    private var requestMultipleCompletion: ((Map<String, UUPermissionStatus>) -> Unit)? = null

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

        requestMultipleCompletion = completion
        launcher?.launch(permissions)
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
}