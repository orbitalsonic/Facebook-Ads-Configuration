package com.orbitalsonic.facebookadsconfiguration

import android.app.Application
import com.facebook.ads.AdSettings
import com.facebook.ads.AudienceNetworkAds
import com.orbitalsonic.facebookadsconfiguration.helpers.koin.modulesList
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AudienceNetworkAds.initialize(this)
        /**
         * after run application
         * search addTestDevice in your android studio logcat
         * and add test device here
         * after uninstall the app test device id changed
         */
        AdSettings.addTestDevice("96bb0dff-837d-4352-b348-d8b616c6a49c")
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@MainApplication)
            modules(modulesList)
        }
    }
}