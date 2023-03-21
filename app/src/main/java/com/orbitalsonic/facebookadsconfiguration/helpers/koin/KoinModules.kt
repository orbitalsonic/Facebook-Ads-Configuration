package com.orbitalsonic.facebookadsconfiguration.helpers.koin

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.orbitalsonic.facebookadsconfiguration.adsconfig.*
import com.orbitalsonic.facebookadsconfiguration.helpers.firebase.RemoteConfiguration
import com.orbitalsonic.facebookadsconfiguration.helpers.preferences.SharedPreferenceUtils
import com.orbitalsonic.facebookadsconfiguration.helpers.managers.InternetManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private val managerModules = module {
    single { InternetManager(androidContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager) }
}

private val utilsModules = module {
    single { SharedPreferenceUtils(androidContext().getSharedPreferences("app_preferences", Application.MODE_PRIVATE)) }
}

private val firebaseModule = module {
    single { RemoteConfiguration(get()) }
}

private val adsModule = module {
    single { FbInterstitialAds() }
    single { FbPreloadNativeAds() }
    factory { FbNativeAds() }
    factory { FbBannerAds() }
}

val modulesList = listOf(utilsModules, managerModules, firebaseModule, adsModule)