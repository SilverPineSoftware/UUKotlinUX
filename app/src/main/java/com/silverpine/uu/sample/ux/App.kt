package com.silverpine.uu.sample.ux

import android.app.Application
import com.silverpine.uu.core.UUResources

class App: Application()
{
    override fun onCreate()
    {
        super.onCreate()

        UUResources.init(applicationContext)
    }
}