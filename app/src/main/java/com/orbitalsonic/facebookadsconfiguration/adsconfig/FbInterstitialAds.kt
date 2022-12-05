package com.orbitalsonic.facebookadsconfiguration.adsconfig

import android.app.Activity
import android.util.Log
import com.facebook.ads.Ad
import com.facebook.ads.AdError
import com.facebook.ads.InterstitialAd
import com.facebook.ads.InterstitialAdListener
import com.orbitalsonic.facebookadsconfiguration.utils.GeneralUtils.AD_TAG
import com.orbitalsonic.facebookadsconfiguration.adsconfig.callbacks.FbInterstitialOnCallBack


class FbInterstitialAds(private val mActivity: Activity) {
    companion object{
        private var mInterstitialAd: InterstitialAd? = null
    }

    var isLoadingAd = false

    // load and show strategy
    fun loadInterstitialAd(
        fbInterstitialIds: String,
        isAdActive: Boolean,
        isAppPurchased: Boolean,
        isInternetConnected:Boolean,
        mListener: FbInterstitialOnCallBack
    ) {
        if (isInternetConnected && !isAppPurchased && isAdActive) {
            if (mInterstitialAd == null && !isLoadingAd) {
                isLoadingAd = true
                mInterstitialAd = InterstitialAd(
                    mActivity,
                    fbInterstitialIds
                )
                mInterstitialAd?.let{
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
                                    isLoadingAd = false
                                    mInterstitialAd = null
                                    mListener.onInterstitialDismissed()
                                    Log.d(AD_TAG, "FB Interstitial ad dismissed.")
                                }

                                override fun onError(ad: Ad, adError: AdError) {
                                    // Ad error callback
                                    isLoadingAd = false
                                    mListener.onError(adError.errorMessage)
                                    mInterstitialAd = null
                                    Log.e(AD_TAG, "FB Interstitial ad failed to load: " + adError.errorMessage)
                                }

                                override fun onAdLoaded(ad: Ad) {
                                    // Interstitial ad is loaded and ready to be displayed
                                    isLoadingAd = false
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

            }
        }else{
            Log.e(AD_TAG, "Internet not Connected or App is Purchased or ad is not active from Firebase")
            mListener.onError("Internet not Connected or App is Purchased or ad is not active from Firebase")
        }

    }

    // load show and load strategy
    fun loadAndLoadInterstitialAd(
        fbInterstitialIds: String,
        isAdActive: Boolean,
        isAppPurchased: Boolean,
        isInternetConnected:Boolean,
        mListener: FbInterstitialOnCallBack
    ) {
        if (isInternetConnected && !isAppPurchased && isAdActive) {
            if (mInterstitialAd == null && !isLoadingAd) {
                isLoadingAd = true
                mInterstitialAd = InterstitialAd(
                    mActivity,
                    fbInterstitialIds
                )
                mInterstitialAd?.let{
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
                                    isLoadingAd = false
                                    mListener.onInterstitialDismissed()
                                    Log.d(AD_TAG, "FB Interstitial ad dismissed.")
                                    mInterstitialAd?.loadAd()
                                }

                                override fun onError(ad: Ad, adError: AdError) {
                                    // Ad error callback
                                    isLoadingAd = false
                                    mListener.onError(adError.errorMessage)
                                    mInterstitialAd = null
                                    Log.e(AD_TAG, "FB Interstitial ad failed to load: " + adError.errorMessage)
                                }

                                override fun onAdLoaded(ad: Ad) {
                                    // Interstitial ad is loaded and ready to be displayed
                                    isLoadingAd = false
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

            }
        }else{
            Log.e(AD_TAG, "Internet not Connected or App is Purchased or ad is not active from Firebase")
            mListener.onError("Internet not Connected or App is Purchased or ad is not active from Firebase")
        }

    }

    fun destroyInterstitialAds(){
        mInterstitialAd?.destroy()
    }

    fun dismissInterstitialLoaded(){
        mInterstitialAd = null
    }


    fun showInterstitialAds(){
        mInterstitialAd?.let {
            if (it.isAdLoaded){
                it.show()
            }
        }
    }

    fun isInterstitialAdsLoaded():Boolean{
       return mInterstitialAd?.isAdLoaded ?:false
    }


}