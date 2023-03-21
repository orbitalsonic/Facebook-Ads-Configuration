package com.orbitalsonic.facebookadsconfiguration.adsconfig


import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.facebook.ads.*
import com.orbitalsonic.facebookadsconfiguration.R
import com.orbitalsonic.facebookadsconfiguration.adsconfig.callbacks.FbNativeCallBack
import com.orbitalsonic.facebookadsconfiguration.adsconfig.constants.FbAdsConstants.isNativeMediumLoading
import com.orbitalsonic.facebookadsconfiguration.adsconfig.constants.FbAdsConstants.isNativeSmallLoading
import com.orbitalsonic.facebookadsconfiguration.adsconfig.constants.FbAdsConstants.preLoadNativeMedium
import com.orbitalsonic.facebookadsconfiguration.adsconfig.constants.FbAdsConstants.preLoadNativeSmall
import com.orbitalsonic.facebookadsconfiguration.helpers.firebase.FirebaseUtils.recordException


class FbPreloadNativeAds {
    private val AD_TAG = "AdsInformation"

    fun loadNativeMedium(
        activity: Activity?,
        fbNativeMediumIds: String,
        adEnable: Int,
        isAppPurchased: Boolean,
        isInternetConnected: Boolean,
        fbNativeCallBack: FbNativeCallBack
    ) {
        activity?.let { mActivity ->
            try {
                if (isInternetConnected && adEnable != 0 && !isAppPurchased && !isNativeMediumLoading && fbNativeMediumIds.isNotEmpty()) {
                    isNativeMediumLoading = true
                    if (preLoadNativeMedium == null) {
                        if (!mActivity.isDestroyed && !mActivity.isFinishing) {
                            preLoadNativeMedium = NativeAd(mActivity, fbNativeMediumIds)
                            preLoadNativeMedium?.let {
                                it.loadAd(
                                    it.buildLoadAdConfig()
                                        .withAdListener(object : NativeAdListener {
                                            override fun onMediaDownloaded(ad: Ad) {
                                                fbNativeCallBack.onMediaDownloaded()
                                                Log.d(AD_TAG, "FB Native ad finished downloading all assets.")
                                            }

                                            override fun onError(ad: Ad, adError: AdError) {
                                                isNativeMediumLoading = false
                                                fbNativeCallBack.onError(adError.errorMessage)
                                                Log.e(
                                                    AD_TAG,
                                                    "FB Native ad failed to load: " + adError.errorMessage
                                                )
                                            }

                                            override fun onAdLoaded(ad: Ad) {
                                                isNativeMediumLoading = false
                                                fbNativeCallBack.onAdLoaded()
                                                Log.d(AD_TAG, "FB Native ad is loaded and ready to be displayed!")


                                            }

                                            override fun onAdClicked(ad: Ad) {
                                                fbNativeCallBack.onAdClicked()
                                                Log.d(AD_TAG, "FB Native ad clicked!")
                                            }

                                            override fun onLoggingImpression(ad: Ad) {
                                                fbNativeCallBack.onLoggingImpression()
                                                Log.d(AD_TAG, "FB Native ad impression logged!")
                                                preLoadNativeMedium = null
                                            }
                                        })
                                        .build()
                                )
                            }
                        }
                    } else {
                        isNativeMediumLoading = false
                        Log.e(AD_TAG, "Native is already loaded")
                        fbNativeCallBack.onPreloaded()
                    }

                } else {
                    Log.e(AD_TAG, "adEnable = $adEnable, isAppPurchased = $isAppPurchased, isInternetConnected = $isInternetConnected")
                    fbNativeCallBack.onError("adEnable = $adEnable, isAppPurchased = $isAppPurchased, isInternetConnected = $isInternetConnected")
                }

            } catch (ex: Exception) {
                isNativeMediumLoading = false
                Log.e(AD_TAG, "${ex.message}")
                fbNativeCallBack.onError("${ex.message}")

            }
        }
    }

    fun showNativeMedium(
        activity: Activity?,
        adsPlaceHolder: FrameLayout
    ) {
        preLoadNativeMedium?.let {
            adsPlaceHolder.visibility = View.VISIBLE
            inflateNativeMedium(activity, it, adsPlaceHolder)
        } ?: kotlin.run {
            adsPlaceHolder.visibility = View.GONE
        }
    }

    private fun inflateNativeMedium(activity: Activity?, nativeAd: NativeAd, fbNativeContainer: FrameLayout) {
        activity?.let { mActivity ->
            try {
                nativeAd.unregisterView()

                // Add the Ad view into the ad container.
                val inflater = LayoutInflater.from(mActivity)

                val adView = NativeAdLayout(mActivity)

                // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
                val adViewNativeM: ConstraintLayout =
                    inflater.inflate(R.layout.fb_native_medium, adView, false) as ConstraintLayout

                val viewGroup: ViewGroup? = adViewNativeM.parent as ViewGroup?
                viewGroup?.removeView(adViewNativeM)

                fbNativeContainer.removeAllViews()
                fbNativeContainer.addView(adViewNativeM)
                // Add the AdOptionsView
                val adChoicesContainer: LinearLayout = adViewNativeM.findViewById(R.id.ad_choices_container)
                val adOptionsView = AdOptionsView(mActivity, nativeAd, adView)
                adChoicesContainer.removeAllViews()
                adChoicesContainer.addView(adOptionsView, 0)

                // Create native UI using the ad metadata.
                val nativeAdIcon: MediaView = adViewNativeM.findViewById(R.id.native_ad_icon)
                val nativeAdTitle = adViewNativeM.findViewById<TextView>(R.id.native_ad_title)
                val nativeAdMedia: MediaView = adViewNativeM.findViewById(R.id.native_ad_media)
                val nativeAdSocialContext =
                    adViewNativeM.findViewById<TextView>(R.id.native_ad_social_context)
                val nativeAdBody = adViewNativeM.findViewById<TextView>(R.id.native_ad_body)
                val sponsoredLabel = adViewNativeM.findViewById<TextView>(R.id.native_ad_sponsored_label)
                val nativeAdCallToAction: Button = adViewNativeM.findViewById(R.id.native_ad_call_to_action)

                // Set the Text.
                nativeAdTitle.text = nativeAd.advertiserName
                nativeAdBody.text = nativeAd.adBodyText
                nativeAdSocialContext.text = nativeAd.adSocialContext
                nativeAdCallToAction.visibility =
                    if (nativeAd.hasCallToAction()) View.VISIBLE else View.INVISIBLE
                nativeAdCallToAction.text = nativeAd.adCallToAction
                sponsoredLabel.text = nativeAd.sponsoredTranslation

                // Create a list of clickable views
                val clickableViews: MutableList<View> = ArrayList()
                clickableViews.add(nativeAdTitle)
                clickableViews.add(nativeAdCallToAction)

                // Register the Title and CTA button to listen for clicks.
                nativeAd.registerViewForInteraction(
                    adViewNativeM, nativeAdMedia, nativeAdIcon, clickableViews
                )
            } catch (ex: Exception) {
                ex.recordException("inflateNativeMedium")
            }
        }
    }

    fun loadNativeSmallAd(
        activity: Activity?,
        fbNativeSmallIds: String,
        adEnable: Int,
        isAppPurchased: Boolean,
        isInternetConnected: Boolean,
        fbNativeCallBack: FbNativeCallBack
    ) {

        activity?.let { mActivity ->
            try {
                if (isInternetConnected && adEnable != 0 && !isAppPurchased && !isNativeSmallLoading && fbNativeSmallIds.isNotEmpty()) {
                    isNativeSmallLoading = true
                    if (preLoadNativeSmall == null) {
                        if (!mActivity.isDestroyed && !mActivity.isFinishing) {
                            preLoadNativeSmall = NativeBannerAd(mActivity, fbNativeSmallIds)
                            preLoadNativeSmall?.let {
                                it.loadAd(
                                    it.buildLoadAdConfig()
                                        .withAdListener(object : NativeAdListener {
                                            override fun onMediaDownloaded(ad: Ad) {
                                                fbNativeCallBack.onMediaDownloaded()
                                                Log.d(AD_TAG, "FB Native ad finished downloading all assets.")
                                            }

                                            override fun onError(ad: Ad, adError: AdError) {
                                                isNativeSmallLoading = false
                                                fbNativeCallBack.onError(adError.errorMessage)
                                                Log.e(
                                                    AD_TAG,
                                                    "FB Native ad failed to load: " + adError.errorMessage
                                                )
                                            }

                                            override fun onAdLoaded(ad: Ad) {
                                                isNativeSmallLoading = false
                                                fbNativeCallBack.onAdLoaded()
                                                Log.d(AD_TAG, "FB Native ad is loaded and ready to be displayed!")
                                            }

                                            override fun onAdClicked(ad: Ad) {
                                                fbNativeCallBack.onAdClicked()
                                                Log.d(AD_TAG, "FB Native ad clicked!")
                                            }

                                            override fun onLoggingImpression(ad: Ad) {
                                                fbNativeCallBack.onLoggingImpression()
                                                Log.d(AD_TAG, "FB Native ad impression logged!")
                                                preLoadNativeSmall = null
                                            }
                                        })
                                        .build()
                                )
                            }
                        }
                    } else {
                        isNativeSmallLoading = false
                        Log.e(AD_TAG, "Native is already loaded")
                        fbNativeCallBack.onPreloaded()
                    }

                } else {
                    Log.e(AD_TAG, "adEnable = $adEnable, isAppPurchased = $isAppPurchased, isInternetConnected = $isInternetConnected")
                    fbNativeCallBack.onError("adEnable = $adEnable, isAppPurchased = $isAppPurchased, isInternetConnected = $isInternetConnected")
                }

            } catch (ex: Exception) {
                isNativeSmallLoading = false
                Log.e(AD_TAG, "${ex.message}")
                fbNativeCallBack.onError("${ex.message}")

            }
        }

    }

    fun showNativeSmallAd(
        activity: Activity?,
        adsPlaceHolder: FrameLayout
    ) {
        preLoadNativeSmall?.let {
            adsPlaceHolder.visibility = View.VISIBLE
            inflateNativeSmall(activity, it, adsPlaceHolder)
        } ?: kotlin.run {
            adsPlaceHolder.visibility = View.GONE
        }
    }

    private fun inflateNativeSmall(activity: Activity?, nativeBannerAd: NativeBannerAd, fbNativeContainer: FrameLayout) {
        activity?.let { mActivity ->
            try {
                // Unregister last ad
                nativeBannerAd.unregisterView()

                // Add the Ad view into the ad container.
                // Add the Ad view into the ad container.
                val inflater = LayoutInflater.from(mActivity)

                val adView = NativeAdLayout(mActivity)
                // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
                val adViewNativeB: ConstraintLayout =
                    inflater.inflate(R.layout.fb_native_small, adView, false) as ConstraintLayout

                val viewGroup: ViewGroup? = adViewNativeB.parent as ViewGroup?
                viewGroup?.removeView(adViewNativeB)

                fbNativeContainer.removeAllViews()
                fbNativeContainer.addView(adViewNativeB)

                // Add the AdChoices icon
                val adChoicesContainer: LinearLayout =
                    adViewNativeB.findViewById(R.id.ad_choices_container)

                val adOptionsView =
                    AdOptionsView(mActivity, nativeBannerAd, adView)
                adChoicesContainer.removeAllViews()
                adChoicesContainer.addView(adOptionsView, 0)

                // Create native UI using the ad metadata.
                val nativeAdTitle: TextView = adViewNativeB.findViewById(R.id.native_ad_title)
                val nativeAdSocialContext: TextView =
                    adViewNativeB.findViewById(R.id.native_ad_social_context)
                val sponsoredLabel: TextView = adViewNativeB.findViewById(R.id.native_ad_sponsored_label)
                val nativeAdIconView: MediaView = adViewNativeB.findViewById(R.id.native_icon_view)
                val nativeAdCallToAction: Button = adViewNativeB.findViewById(R.id.native_ad_call_to_action)

                // Set the Text.
                nativeAdCallToAction.text = nativeBannerAd.adCallToAction
                nativeAdCallToAction.visibility =
                    if (nativeBannerAd.hasCallToAction()) View.VISIBLE else View.INVISIBLE
                nativeAdTitle.text = nativeBannerAd.advertiserName
                nativeAdSocialContext.text = nativeBannerAd.adSocialContext
                sponsoredLabel.text = nativeBannerAd.sponsoredTranslation

                // Register the Title and CTA button to listen for clicks.
                // Create a list of clickable views
                val clickableViews: MutableList<View> = ArrayList()
                clickableViews.add(nativeAdTitle)
                clickableViews.add(nativeAdCallToAction)

                // Register the Title and CTA button to listen for clicks.
                nativeBannerAd.registerViewForInteraction(
                    adViewNativeB, nativeAdIconView, clickableViews
                )
            } catch (ex: Exception) {
                ex.recordException("inflateNativeMedium")
            }
        }

    }

}