package com.orbitalsonic.facebookadsconfiguration.adsconfig.callbacks

interface FbNativeCallBack {
    fun onError(adError:String)
    fun onAdLoaded()
    fun onAdClicked()
    fun onLoggingImpression()
    fun onMediaDownloaded()
    fun onPreloaded()
}