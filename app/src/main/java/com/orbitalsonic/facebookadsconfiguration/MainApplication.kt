package com.orbitalsonic.facebookadsconfiguration

import android.app.Application
import com.facebook.ads.AdSettings
import com.facebook.ads.AudienceNetworkAds

class MainApplication:Application() {

    override fun onCreate() {
        super.onCreate()
        AudienceNetworkAds.initialize(this)
        /**
         * after run application
         * search addTestDevice in your android studio logcat
         * and add test device here
         * after uninstall the app test device id changed
         */
        AdSettings.addTestDevice("3596a42d-0d02-4624-83a8-daaeafdec280")
    }

}