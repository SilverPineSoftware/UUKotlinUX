package com.silverpine.uu.ux.permissions

/**
 * Represents the current status of a permission request in the UU framework.
 * 
 * This enum provides a comprehensive way to track permission states and determine
 * the appropriate actions that can be taken based on the current status.
 */
enum class UUPermissionStatus
{
    /**
     * Unknown status. This is a fallback when an exception occurs while obtaining the permission status.
     */
    UNDETERMINED,
    
    /**
     * The permission has never been requested from the user.
     * This is the initial state before any permission request is made.
     */
    NEVER_ASKED,
    
    /**
     * The permission has been granted by the user.
     */
    GRANTED,
    
    /**
     * The permission was denied by the user, but can still be requested again.
     */
    DENIED,
    
    /**
     * The permission was permanently denied by the user.
     * The user selected "Don't ask again" or the permission is restricted by device policy.
     * The app cannot request this permission again and must direct the user to settings.
     */
    PERMANENTLY_DENIED;

    /**
     * Indicates whether the permission can be requested from the user.
     * 
     * @return `true` if the permission can be requested (NEVER_ASKED or DENIED),
     *         `false` otherwise
     */
    val canRequest: Boolean
        get() = this == NEVER_ASKED || this == DENIED

    /**
     * Indicates whether the permission has been granted.
     * 
     * @return `true` if the permission is granted, `false` otherwise
     */
    val isGranted: Boolean
        get() = this == GRANTED

    /**
     * Indicates whether the permission has been permanently denied.
     * 
     * @return `true` if the permission is permanently denied, `false` otherwise
     */
    val isPermanentlyDenied: Boolean
        get() = this == PERMANENTLY_DENIED
}
