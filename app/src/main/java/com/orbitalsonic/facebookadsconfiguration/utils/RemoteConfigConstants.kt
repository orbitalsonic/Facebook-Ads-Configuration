package com.orbitalsonic.facebookadsconfiguration.utils

object RemoteConfigConstants {

    /**
     * Interstitial Ads Active Keys
     */
    const val is_splash_interstitial_active = "is_splash_interstitial_active"
    const val is_main_interstitial_active = "is_main_interstitial_active"

    /**
     * Native Ads Active Keys
     */
    const val is_splash_native_active = "is_splash_native_active"
    const val is_main_native_active = "is_main_native_active"

    /**
     * Banner Ads Active Keys
     */
    const val is_main_banner_active = "is_main_banner_active"

    /**
     * Other Keys
     */
    const val remote_counter_key = "remote_counter"

    /**
     * Active Controls
     */
    var isSplashInterstitialActive = true
    var isMainInterstitialActive = true
    var isSplashNativeActive = true
    var isMainNativeActive = true
    var isMainBannerActive = true

    var remoteCounter = 2
    var totalCount = 1

}