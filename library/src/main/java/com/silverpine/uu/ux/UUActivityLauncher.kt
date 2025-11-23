package com.silverpine.uu.ux

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import com.silverpine.uu.logging.UULog

private const val LOG_TAG = "UUActivityLauncher"

abstract class UUActivityLauncher<Input, Output>(
    private val contract: ActivityResultContract<Input, Output>
)
{
    private var activity: ComponentActivity? = null
    private var launcher:  ActivityResultLauncher<Input>? = null

    open fun init(activity: ComponentActivity)
    {
        this.activity = activity
        this.launcher = activity.registerForActivityResult(contract, this::handleLaunchResult)
    }

    abstract fun handleLaunchResult(result: Output)

    fun launch(input: Input)
    {
        if (launcher == null)
        {
            UULog.warn(LOG_TAG, "launcher is null! Call init(activity) before first use.")
        }

        launcher?.launch(input)
    }

    protected fun requireActivity(): ComponentActivity
    {
        val activity = this.activity
        if (activity == null)
        {
            throw RuntimeException("${javaClass.name} not initialized. Must call ${javaClass.name}.init(activity) prior to first use.")
        }

        return activity
    }
}