package com.orbitalsonic.facebookadsconfiguration.adsconfig

import android.app.Activity
import android.util.Log
import com.facebook.ads.Ad
import com.facebook.ads.AdError
import com.facebook.ads.InterstitialAd
import com.facebook.ads.InterstitialAdListener
import com.orbitalsonic.facebookadsconfiguration.adsconfig.callbacks.FbInterstitialOnCallBack
import com.orbitalsonic.facebookadsconfiguration.adsconfig.constants.FbAdsConstants.interstitialAd
import com.orbitalsonic.facebookadsconfiguration.adsconfig.constants.FbAdsConstants.isInterLoading


class FbInterstitialAds {
    private val AD_TAG = "AdsInformation"

    // load and show strategy
    fun loadInterstitialAd(
        activity: Activity?,
        fbInterstitialIds: String,
        adEnable: Int,
        isAppPurchased: Boolean,
        isInternetConnected: Boolean,
        mListener: FbInterstitialOnCallBack
    ) {
        activity?.let { mActivity ->
            if (isInternetConnected && adEnable != 0 && !isAppPurchased && !isInterLoading && fbInterstitialIds.isNotEmpty()) {
                if (interstitialAd == null) {
                    interstitialAd = InterstitialAd(
                        mActivity,
                        fbInterstitialIds
                    )
                    interstitialAd?.let {
                        it.loadAd(
                            it.buildLoadAdConfig()
                                .withAdListener(object : InterstitialAdListener {
                                    override fun onInterstitialDisplayed(ad: Ad) {
                                        // Interstitial ad displayed callback
                                        mListener.onInterstitialDisplayed()
                                        Log.d(AD_TAG, "FB Interstitial ad displayed.")
                                    }

                                    override fun onInterstitialDismissed(ad: Ad) {
                                        // Interstitial dismissed callback
                                        isInterLoading = false
                                        interstitialAd = null
                                        mListener.onInterstitialDismissed()
                                        Log.d(AD_TAG, "FB Interstitial ad dismissed.")
                                    }

                                    override fun onError(ad: Ad, adError: AdError) {
                                        // Ad error callback
                                        isInterLoading = false
                                        mListener.onError(adError.errorMessage)
                                        interstitialAd = null
                                        Log.e(AD_TAG, "FB Interstitial ad failed to load: " + adError.errorMessage)
                                    }

                                    override fun onAdLoaded(ad: Ad) {
                                        // Interstitial ad is loaded and ready to be displayed
                                        isInterLoading = false
                                        mListener.onAdLoaded()
                                        Log.d(AD_TAG, "FB Interstitial ad is loaded and ready to be displayed!")
                                    }

                                    override fun onAdClicked(ad: Ad) {
                                        // Ad clicked callback
                                        mListener.onAdClicked()
                                        Log.d(AD_TAG, "FB Interstitial ad clicked!")
                                    }

                                    override fun onLoggingImpression(ad: Ad) {
                                        // Ad impression logged callback
                                        mListener.onLoggingImpression()
                                        Log.d(AD_TAG, "FB Interstitial ad impression logged!")
                                    }
                                })
                                .build()
                        )
                    }
                } else {
                    Log.d(AD_TAG, "admob Interstitial onPreloaded")
                    mListener.onPreloaded()
                }

            } else {
                Log.e(AD_TAG, "adEnable = $adEnable, isAppPurchased = $isAppPurchased, isInternetConnected = $isInternetConnected")
                mListener.onError("adEnable = $adEnable, isAppPurchased = $isAppPurchased, isInternetConnected = $isInternetConnected")
            }
        }
    }

    // load show and load strategy
    fun loadAndLoadInterstitialAd(
        activity: Activity?,
        fbInterstitialIds: String,
        adEnable: Int,
        isAppPurchased: Boolean,
        isInternetConnected: Boolean,
        mListener: FbInterstitialOnCallBack
    ) {

        activity?.let { mActivity ->
            if (isInternetConnected && adEnable != 0 && !isAppPurchased && !isInterLoading && fbInterstitialIds.isNotEmpty()) {
                if (interstitialAd == null) {
                    interstitialAd = InterstitialAd(
                        mActivity,
                        fbInterstitialIds
                    )
                    interstitialAd?.let {
                        it.loadAd(
                            it.buildLoadAdConfig()
                                .withAdListener(object : InterstitialAdListener {
                                    override fun onInterstitialDisplayed(ad: Ad) {
                                        // Interstitial ad displayed callback
                                        mListener.onInterstitialDisplayed()
                                        Log.d(AD_TAG, "FB Interstitial ad displayed.")
                                    }

                                    override fun onInterstitialDismissed(ad: Ad) {
                                        // Interstitial dismissed callback
                                        isInterLoading = false
                                        interstitialAd?.loadAd()
                                        mListener.onInterstitialDismissed()
                                        Log.d(AD_TAG, "FB Interstitial ad dismissed.")
                                    }

                                    override fun onError(ad: Ad, adError: AdError) {
                                        // Ad error callback
                                        isInterLoading = false
                                        mListener.onError(adError.errorMessage)
                                        interstitialAd = null
                                        Log.e(AD_TAG, "FB Interstitial ad failed to load: " + adError.errorMessage)
                                    }

                                    override fun onAdLoaded(ad: Ad) {
                                        // Interstitial ad is loaded and ready to be displayed
                                        isInterLoading = false
                                        mListener.onAdLoaded()
                                        Log.d(AD_TAG, "FB Interstitial ad is loaded and ready to be displayed!")
                                    }

                                    override fun onAdClicked(ad: Ad) {
                                        // Ad clicked callback
                                        mListener.onAdClicked()
                                        Log.d(AD_TAG, "FB Interstitial ad clicked!")
                                    }

                                    override fun onLoggingImpression(ad: Ad) {
                                        // Ad impression logged callback
                                        mListener.onLoggingImpression()
                                        Log.d(AD_TAG, "FB Interstitial ad impression logged!")
                                    }
                                })
                                .build()
                        )
                    }
                } else {
                    Log.d(AD_TAG, "admob Interstitial onPreloaded")
                    mListener.onPreloaded()
                }

            } else {
                Log.e(AD_TAG, "adEnable = $adEnable, isAppPurchased = $isAppPurchased, isInternetConnected = $isInternetConnected")
                mListener.onError("adEnable = $adEnable, isAppPurchased = $isAppPurchased, isInternetConnected = $isInternetConnected")
            }
        }

    }

    fun destroyInterstitialAds() {
        interstitialAd?.destroy()
        interstitialAd = null
    }

    fun showInterstitialAds() {
        interstitialAd?.let {
            if (it.isAdLoaded) {
                it.show()
            }
        }
    }

    fun isInterstitialAdsLoaded(): Boolean {
        return interstitialAd?.isAdLoaded ?: false
    }


}