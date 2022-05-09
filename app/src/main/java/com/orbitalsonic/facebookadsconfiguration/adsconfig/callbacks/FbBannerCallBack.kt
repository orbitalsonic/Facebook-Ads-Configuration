package com.orbitalsonic.facebookadsconfiguration.adsconfig.callbacks

interface FbBannerCallBack {
    fun onError(adError:String)
    fun onAdLoaded()
    fun onAdClicked()
    fun onLoggingImpression()
}