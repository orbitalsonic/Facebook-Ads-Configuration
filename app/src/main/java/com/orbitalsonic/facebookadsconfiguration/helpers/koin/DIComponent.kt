package com.orbitalsonic.facebookadsconfiguration.helpers.koin

import com.orbitalsonic.facebookadsconfiguration.adsconfig.FbBannerAds
import com.orbitalsonic.facebookadsconfiguration.adsconfig.FbInterstitialAds
import com.orbitalsonic.facebookadsconfiguration.adsconfig.FbNativeAds
import com.orbitalsonic.facebookadsconfiguration.adsconfig.FbPreloadNativeAds
import com.orbitalsonic.facebookadsconfiguration.helpers.firebase.RemoteConfiguration
import com.orbitalsonic.facebookadsconfiguration.helpers.managers.InternetManager
import com.orbitalsonic.facebookadsconfiguration.helpers.preferences.SharedPreferenceUtils
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DIComponent : KoinComponent {

    // Utils
    val sharedPreferenceUtils by inject<SharedPreferenceUtils>()

    // Managers
    val internetManager by inject<InternetManager>()

    // Remote Configuration
    val remoteConfiguration by inject<RemoteConfiguration>()

    // Ads
    val fbBannerAds by inject<FbBannerAds>()
    val fbNativeAds by inject<FbNativeAds>()
    val fbPreLoadNativeAds by inject<FbPreloadNativeAds>()
    val fbInterstitialAds by inject<FbInterstitialAds>()
}