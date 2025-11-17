package com.silverpine.uu.sample.ux

import android.app.Application
import com.silverpine.uu.core.UUResources
import com.silverpine.uu.logging.UUConsoleLogWriter
import com.silverpine.uu.logging.UULog
import com.silverpine.uu.logging.UULogger

class App: Application()
{
    override fun onCreate()
    {
        super.onCreate()

        UULog.setLogger(UULogger(UUConsoleLogWriter()))
        UUResources.init(applicationContext)
    }
}

val Any.LOG_TAG: String
    get()
    {
        return javaClass.name
    }
