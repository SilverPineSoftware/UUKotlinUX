package com.silverpine.uu.ux.permissions

/**
 * Interface for checking and requesting Android runtime permissions with detailed status reporting.
 *
 * This interface provides a clean abstraction for requesting multiple permissions
 * and receiving comprehensive status information for each permission requested.
 *
 * @see UUPermissionStatus for available permission states
 *
 */
interface UUPermissionProvider
{
    /**
     * Gets the current status of a specific permission.
     *
     * @param permission The permission string to check (e.g., "android.permission.CAMERA")
     * @return The current [com.silverpine.uu.ux.permissions.UUPermissionStatus] of the permission
     */
    fun getPermissionStatus(permission: String): UUPermissionStatus

    /**
     * Requests multiple Android runtime permissions and provides detailed status for each.
     *
     * This method handles the complete permission request flow, including:
     * - Checking if permissions are already granted
     * - Launching the system permission dialog if needed
     * - Tracking permission request history
     * - Determining if permissions can be requested again
     *
     * @param permissions Array of permission strings to request (e.g., "android.permission.CAMERA")
     * @param completion Callback invoked when permission request is complete.
     *                   The map contains each requested permission as a key with its corresponding
     *                   [UUPermissionStatus] as the value.
     *
     * @see UUPermissionStatus for possible status values:
     * - [UUPermissionStatus.GRANTED] - Permission was granted
     * - [UUPermissionStatus.DENIED] - Permission was denied but can be requested again
     * - [UUPermissionStatus.PERMANENTLY_DENIED] - Permission was permanently denied
     * - [UUPermissionStatus.NEVER_ASKED] - Permission was never requested (shouldn't occur in completion)
     * - [UUPermissionStatus.UNDETERMINED] - Status could not be determined
     *
     * @sample
     * ```kotlin
     * permissionRequester.requestPermissions(
     *     arrayOf("android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE")
     * ) { results ->
     *     results.forEach { (permission, status) ->
     *         when (status) {
     *             UUPermissionStatus.GRANTED -> {
     *                 // Permission granted, proceed with functionality
     *                 startCamera()
     *             }
     *             UUPermissionStatus.DENIED -> {
     *                 // Permission denied, can ask again
     *                 showPermissionRationale(permission)
     *             }
     *             UUPermissionStatus.PERMANENTLY_DENIED -> {
     *                 // Permission permanently denied, direct to settings
     *                 openAppSettings()
     *             }
     *             else -> {
     *                 // Handle other cases
     *             }
     *         }
     *     }
     * }
     * ```
     */
    fun requestPermissions(permissions: Array<String>, completion: (Map<String, UUPermissionStatus>)->Unit)
}

/**
 * Gets the current status of multiple permissions in a single call.
 *
 * This extension function provides a convenient way to check the status of multiple
 * permissions at once, returning a map where each permission string is associated
 * with its corresponding [UUPermissionStatus].
 *
 * @param permissions Array of permission strings to check
 * @return Map where keys are permission strings and values are their corresponding status
 */
fun UUPermissionProvider.getPermissionStatusMultiple(permissions: Array<String>): Map<String, UUPermissionStatus>
{
    return permissions.associate()
    { permission ->
        permission to getPermissionStatus(permission)
    }
}

/**
 * Checks if all specified permissions have been granted.
 *
 * This extension function provides a convenient way to verify that all permissions
 * in the given array have been granted by the user. It internally uses
 * [getPermissionStatusMultiple] to check the status of all permissions and returns
 * `true` only if every permission has a status of [UUPermissionStatus.GRANTED].
 *
 * @param permissions Array of permission strings to check
 * @return `true` if all permissions are granted, `false` if any permission is not granted
 *
 * @sample
 * ```kotlin
 * val permissions = arrayOf("android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE")
 * if (permissionChecker.areAllPermissionsGranted(permissions)) {
 *     // All permissions are granted, proceed with functionality
 * } else {
 *     // Some permissions are not granted, request them
 * }
 * ```
 */
fun UUPermissionProvider.areAllPermissionsGranted(permissions: Array<String>): Boolean
{
    return getPermissionStatusMultiple(permissions).all { it.value.isGranted }
}

/**
 * Checks if any of the specified permissions have been permanently denied.
 *
 * This extension function provides a convenient way to verify if any permissions
 * in the given array have been permanently denied by the user. It internally uses
 * [getPermissionStatusMultiple] to check the status of all permissions and returns
 * `true` if any permission has a status of [UUPermissionStatus.PERMANENTLY_DENIED].
 *
 * Permanently denied permissions occur when the user selects "Don't ask again" or
 * when the permission is restricted by device policy. These permissions cannot be
 * requested again and typically require the user to manually enable them in settings.
 *
 * @param permissions Array of permission strings to check
 * @return `true` if any permission is permanently denied, `false` if no permissions are permanently denied
 *
 * @sample
 * ```kotlin
 * val permissions = arrayOf("android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE")
 * if (permissionChecker.areAnyPermissionsPermanentlyDenied(permissions)) {
 *     // Some permissions are permanently denied, direct user to settings
 *     showSettingsDialog()
 * } else {
 *     // No permissions are permanently denied, can request them normally
 *     requestPermissions(permissions)
 * }
 * ```
 */
fun UUPermissionProvider.areAnyPermissionsPermanentlyDenied(permissions: Array<String>): Boolean
{
    return getPermissionStatusMultiple(permissions).any { it.value.isPermanentlyDenied }
}

/**
 * Computes a combined permission status from a map of individual permission statuses.
 *
 * This extension property provides a single, prioritized status representing the overall
 * state of multiple permissions. The combined status is determined using the following priority:
 *
 * 1. **All Granted** - If all permissions are [UUPermissionStatus.GRANTED], returns [UUPermissionStatus.GRANTED]
 * 2. **Any Permanently Denied** - If any permission is [UUPermissionStatus.PERMANENTLY_DENIED],
 *    returns [UUPermissionStatus.PERMANENTLY_DENIED] (highest priority for non-granted states)
 * 3. **All Never Asked** - If all permissions are [UUPermissionStatus.NEVER_ASKED],
 *    returns [UUPermissionStatus.NEVER_ASKED]
 * 4. **Default** - Otherwise returns [UUPermissionStatus.DENIED]
 *
 * This is useful for determining the overall state of a permission group when you need
 * a single status value rather than handling individual permission states.
 *
 * @return A single [UUPermissionStatus] representing the combined state of all permissions
 *
 * @sample
 * ```kotlin
 * val statuses = mapOf(
 *     "android.permission.CAMERA" to UUPermissionStatus.GRANTED,
 *     "android.permission.WRITE_EXTERNAL_STORAGE" to UUPermissionStatus.GRANTED
 * )
 * val combined = statuses.uuCombinedStatus
 * // Returns: UUPermissionStatus.GRANTED
 *
 * val mixedStatuses = mapOf(
 *     "android.permission.CAMERA" to UUPermissionStatus.GRANTED,
 *     "android.permission.LOCATION" to UUPermissionStatus.PERMANENTLY_DENIED
 * )
 * val mixedCombined = mixedStatuses.uuCombinedStatus
 * // Returns: UUPermissionStatus.PERMANENTLY_DENIED (because any permanently denied takes priority)
 * ```
 */
val Map<String, UUPermissionStatus>.uuCombinedStatus: UUPermissionStatus
    get()
    {
        return if (values.all { it == UUPermissionStatus.GRANTED })
        {
            UUPermissionStatus.GRANTED
        }
        // If any required permissions are permanently denied, then instruct the user to go to settings
        else if (values.any { it == UUPermissionStatus.PERMANENTLY_DENIED })
        {
            UUPermissionStatus.PERMANENTLY_DENIED
        }
        else if (values.all { it == UUPermissionStatus.NEVER_ASKED })
        {
            UUPermissionStatus.NEVER_ASKED
        }
        else
        {
            // Default to denied
            UUPermissionStatus.DENIED
        }
    }

/**
 * Gets a combined status for multiple permissions in a single call.
 *
 * This extension function provides a convenient way to check the overall state of multiple
 * permissions and receive a single combined status value. It internally uses
 * [getPermissionStatusMultiple] to retrieve individual statuses and then applies
 * [uuCombinedStatus] to compute the combined result.
 *
 * The combined status uses a priority system:
 * - If all permissions are granted, returns [UUPermissionStatus.GRANTED]
 * - If any permission is permanently denied, returns [UUPermissionStatus.PERMANENTLY_DENIED]
 * - If all permissions are never asked, returns [UUPermissionStatus.NEVER_ASKED]
 * - Otherwise, returns [UUPermissionStatus.DENIED]
 *
 * This is particularly useful when you need a single status value to make UI decisions
 * (e.g., show settings dialog, enable/disable a feature, etc.) rather than handling
 * individual permission states.
 *
 * @param permissions Array of permission strings to check
 * @return A single [UUPermissionStatus] representing the combined state of all permissions
 *
 * @sample
 * ```kotlin
 * val permissions = arrayOf(
 *     "android.permission.CAMERA",
 *     "android.permission.WRITE_EXTERNAL_STORAGE"
 * )
 * val combined = permissionProvider.combinedPermissionStatus(permissions)
 *
 * when (combined) {
 *     UUPermissionStatus.GRANTED -> {
 *         // All permissions granted, proceed with functionality
 *         startFeature()
 *     }
 *     UUPermissionStatus.PERMANENTLY_DENIED -> {
 *         // Some permissions permanently denied, direct to settings
 *         showSettingsDialog()
 *     }
 *     UUPermissionStatus.NEVER_ASKED -> {
 *         // Permissions never requested, request them now
 *         requestPermissions(permissions) { /* handle results */ }
 *     }
 *     else -> {
 *         // Some permissions denied but can be requested again
 *         requestPermissions(permissions) { /* handle results */ }
 *     }
 * }
 * ```
 */
fun UUPermissionProvider.combinedPermissionStatus(permissions: Array<String>): UUPermissionStatus
{
    return getPermissionStatusMultiple(permissions).uuCombinedStatus
}