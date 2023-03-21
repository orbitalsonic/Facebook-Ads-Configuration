package com.orbitalsonic.facebookadsconfiguration.adsconfig.callbacks

interface FbInterstitialOnCallBack {
    fun onInterstitialDisplayed()
    fun onInterstitialDismissed()
    fun onError(adError:String)
    fun onAdLoaded()
    fun onAdClicked()
    fun onLoggingImpression()
    fun onPreloaded()
}