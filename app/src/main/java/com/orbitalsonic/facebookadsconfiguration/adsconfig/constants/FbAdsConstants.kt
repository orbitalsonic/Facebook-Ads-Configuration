package com.orbitalsonic.facebookadsconfiguration.adsconfig.constants

import com.facebook.ads.InterstitialAd
import com.facebook.ads.NativeAd
import com.facebook.ads.NativeBannerAd

object FbAdsConstants {

    var interstitialAd: InterstitialAd? = null
    var preLoadNativeMedium: NativeAd? = null
    var preLoadNativeSmall: NativeBannerAd? = null

    var isInterLoading = false
    var isNativeMediumLoading = false
    var isNativeSmallLoading = false

    fun reset() {
        interstitialAd = null

        preLoadNativeMedium?.destroy()
        preLoadNativeMedium = null

        preLoadNativeSmall?.destroy()
        preLoadNativeSmall = null

        isInterLoading = false
        isNativeMediumLoading = false
        isNativeSmallLoading = false
    }
}