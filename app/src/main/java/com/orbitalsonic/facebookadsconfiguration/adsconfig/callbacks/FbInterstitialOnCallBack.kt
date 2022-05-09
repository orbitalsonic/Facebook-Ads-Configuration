package com.orbitalsonic.facebookadsconfiguration.adsconfig.callbacks

interface FbInterstitialOnCallBack {
    fun onInterstitialDisplayed()
    fun onInterstitialDismissed()
    fun onError()
    fun onAdLoaded()
    fun onAdClicked()
    fun onLoggingImpression()
}