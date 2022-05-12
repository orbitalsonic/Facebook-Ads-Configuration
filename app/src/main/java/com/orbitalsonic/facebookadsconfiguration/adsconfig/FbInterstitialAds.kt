package com.orbitalsonic.facebookadsconfiguration.adsconfig

import android.app.Activity
import android.util.Log
import com.facebook.ads.Ad
import com.facebook.ads.AdError
import com.facebook.ads.InterstitialAd
import com.facebook.ads.InterstitialAdListener
import com.orbitalsonic.facebookadsconfiguration.GeneralUtils.AD_TAG
import com.orbitalsonic.facebookadsconfiguration.GeneralUtils.isInternetConnected
import com.orbitalsonic.facebookadsconfiguration.adsconfig.callbacks.FbInterstitialOnCallBack


class FbInterstitialAds(activity: Activity) {
    private var mInterstitialAd: InterstitialAd? = null
    private val mActivity: Activity = activity
    private var fbInterstitialOnCallBack: FbInterstitialOnCallBack? = null
    var isLoadingAd = false

    // load show and load strategy
    fun loadInterstitialAd(
        fbInterstitialIds: String,
        isRemoteConfigActive: Boolean,
        isAppPurchased: Boolean,
        mListener: FbInterstitialOnCallBack
    ) {
        fbInterstitialOnCallBack = mListener
        if (isInternetConnected(mActivity) && !isAppPurchased && isRemoteConfigActive) {
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
                                    fbInterstitialOnCallBack?.onInterstitialDisplayed()
                                    Log.d(AD_TAG, "FB Interstitial ad displayed.")
                                }

                                override fun onInterstitialDismissed(ad: Ad) {
                                    // Interstitial dismissed callback
                                    isLoadingAd = false
                                    fbInterstitialOnCallBack?.onInterstitialDismissed()
                                    Log.d(AD_TAG, "FB Interstitial ad dismissed.")
                                    mInterstitialAd?.loadAd()
                                }

                                override fun onError(ad: Ad, adError: AdError) {
                                    // Ad error callback
                                    isLoadingAd = false
                                    fbInterstitialOnCallBack?.onError()
                                    mInterstitialAd = null
                                    Log.e(AD_TAG, "FB Interstitial ad failed to load: " + adError.errorMessage)
                                }

                                override fun onAdLoaded(ad: Ad) {
                                    // Interstitial ad is loaded and ready to be displayed
                                    isLoadingAd = false
                                    fbInterstitialOnCallBack?.onAdLoaded()
                                    Log.d(AD_TAG, "FB Interstitial ad is loaded and ready to be displayed!")
                                }

                                override fun onAdClicked(ad: Ad) {
                                    // Ad clicked callback
                                    fbInterstitialOnCallBack?.onAdClicked()
                                    Log.d(AD_TAG, "FB Interstitial ad clicked!")
                                }

                                override fun onLoggingImpression(ad: Ad) {
                                    // Ad impression logged callback
                                    fbInterstitialOnCallBack?.onLoggingImpression()
                                    Log.d(AD_TAG, "FB Interstitial ad impression logged!")
                                }
                            })
                            .build()
                    )
                }

            }
        }

    }

    fun destroyInterstitialAds(){
        mInterstitialAd?.destroy()
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