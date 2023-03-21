package com.orbitalsonic.facebookadsconfiguration.adsconfig


import android.app.Activity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.facebook.ads.*
import com.orbitalsonic.facebookadsconfiguration.adsconfig.callbacks.FbBannerCallBack

class FbBannerAds {

    private var adViewBanner: AdView? = null
    private val AD_TAG = "AdsInformation"

    fun loadBannerAds(
        activity: Activity?,
        adsPlaceHolder: FrameLayout,
        fbBannerIds: String,
        adEnable: Int,
        isAppPurchased: Boolean,
        isInternetConnected: Boolean,
        fbBannerCallBack: FbBannerCallBack
    ) {
        activity?.let { mActivity ->
            try {
                if (isInternetConnected && adEnable != 0 && !isAppPurchased && fbBannerIds.isNotEmpty()) {
                    if (!mActivity.isDestroyed && !mActivity.isFinishing) {
                        adsPlaceHolder.visibility = View.VISIBLE
                        /***
                         * AdSize.BANNER_HEIGHT_50
                         * AdSize.BANNER_HEIGHT_90
                         */
                        adViewBanner = AdView(mActivity, fbBannerIds, AdSize.BANNER_HEIGHT_90)
                        adViewBanner?.let {
                            it.loadAd(it.buildLoadAdConfig().withAdListener(object : AdListener {
                                override fun onError(p0: Ad?, p1: AdError?) {
                                    Log.e(AD_TAG, "FB Banner onError")
                                    fbBannerCallBack.onError(p1?.errorMessage.toString())
                                    adsPlaceHolder.visibility = View.GONE
                                }

                                override fun onAdLoaded(p0: Ad?) {
                                    Log.d(AD_TAG, "FB Banner onAdLoaded")
                                    fbBannerCallBack.onAdLoaded()
                                    displayBannerAd(adsPlaceHolder)
                                }

                                override fun onAdClicked(p0: Ad?) {
                                    Log.d(AD_TAG, "FB Banner onAdClicked")
                                    fbBannerCallBack.onAdClicked()
                                }

                                override fun onLoggingImpression(p0: Ad?) {
                                    Log.d(AD_TAG, "FB Banner onLoggingImpression")
                                    fbBannerCallBack.onLoggingImpression()
                                }

                            }).build())
                        }
                    }
                } else {
                    adsPlaceHolder.removeAllViews()
                    adsPlaceHolder.visibility = View.GONE
                    Log.e(AD_TAG, "adEnable = $adEnable, isAppPurchased = $isAppPurchased, isInternetConnected = $isInternetConnected")
                    fbBannerCallBack.onError("adEnable = $adEnable, isAppPurchased = $isAppPurchased, isInternetConnected = $isInternetConnected")

                }
            } catch (ex: Exception) {
                adsPlaceHolder.removeAllViews()
                adsPlaceHolder.visibility = View.GONE
                Log.e(AD_TAG, "${ex.message}")
                fbBannerCallBack.onError("${ex.message}")
            }
        }
    }

    private fun displayBannerAd(adsPlaceHolder: FrameLayout) {
        try {
            if (adViewBanner != null) {
                val viewGroup: ViewGroup? = adViewBanner?.parent as ViewGroup?
                viewGroup?.removeView(adViewBanner)

                adsPlaceHolder.removeAllViews()
                adsPlaceHolder.addView(adViewBanner)
            } else {
                adsPlaceHolder.removeAllViews()
                adsPlaceHolder.visibility = View.GONE
            }
        } catch (ex: Exception) {
            Log.e(AD_TAG, "inflateBannerAd: ${ex.message}")
        }
    }

    fun destroyBannerAds() {
        adViewBanner?.destroy()
        adViewBanner = null
    }

}