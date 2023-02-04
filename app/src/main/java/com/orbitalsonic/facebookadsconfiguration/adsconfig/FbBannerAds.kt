package com.orbitalsonic.facebookadsconfiguration.adsconfig


import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.facebook.ads.*
import com.orbitalsonic.facebookadsconfiguration.utils.GeneralUtils.AD_TAG
import com.orbitalsonic.facebookadsconfiguration.R
import com.orbitalsonic.facebookadsconfiguration.adsconfig.callbacks.FbBannerCallBack
import com.orbitalsonic.facebookadsconfiguration.adsconfig.callbacks.FbNativeCallBack


class FbBannerAds(private val mActivity: Activity) {

    private var adViewBanner: AdView? = null
    private var nativeMedium: NativeAd? = null
    private var nativeSmall: NativeBannerAd? = null


    fun loadBannerAds(
        adsPlaceHolder:FrameLayout,
        fbBannerIds: String,
        isAdActive: Boolean,
        isAppPurchased: Boolean,
        isInternetConnected:Boolean,
        mListener: FbBannerCallBack
    ) {
        if (isInternetConnected && !isAppPurchased && isAdActive) {
            adsPlaceHolder.visibility = View.VISIBLE
            adViewBanner = AdView(mActivity, fbBannerIds, AdSize.BANNER_HEIGHT_50)
            adViewBanner?.let {
                it.loadAd(it.buildLoadAdConfig().withAdListener(object : AdListener {

                    override fun onError(p0: Ad?, p1: AdError?) {
                        Log.e(AD_TAG, "FB Banner onError")
                        mListener.onError(p1?.errorMessage.toString())
                        adsPlaceHolder.visibility = View.GONE
                    }

                    override fun onAdLoaded(p0: Ad?) {
                        Log.d(AD_TAG, "FB Banner onAdLoaded")
                        displayBannerAd(adsPlaceHolder)
                        mListener.onAdLoaded()
                    }

                    override fun onAdClicked(p0: Ad?) {
                        Log.d(AD_TAG, "FB Banner onAdClicked")
                        mListener.onAdClicked()
                    }

                    override fun onLoggingImpression(p0: Ad?) {
                        Log.d(AD_TAG, "FB Banner onLoggingImpression")
                        mListener.onLoggingImpression()
                    }

                }).build())

            }

        } else {
            adsPlaceHolder.visibility = View.GONE
            Log.e(AD_TAG, "Internet not Connected or App is Purchased or ad is not active from Firebase")
            mListener.onError("Internet not Connected or App is Purchased or ad is not active from Firebase")
        }

    }

    private fun displayBannerAd(adsPlaceHolder: FrameLayout) {
        try {
            if (adViewBanner != null) {
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
    }

    fun loadNativeMedium(
        adsPlaceHolder:FrameLayout,
        fbNativeMediumIds: String,
        isAdActive: Boolean,
        isAppPurchased: Boolean,
        isInternetConnected:Boolean,
        mListener: FbNativeCallBack
    ) {
        if (isInternetConnected && !isAppPurchased && isAdActive) {
            adsPlaceHolder.visibility = View.VISIBLE
            nativeMedium = NativeAd(mActivity, fbNativeMediumIds)
            nativeMedium?.let {
                it.loadAd(
                    it.buildLoadAdConfig()
                        .withAdListener(object : NativeAdListener {
                            override fun onMediaDownloaded(ad: Ad) {
                                mListener.onMediaDownloaded()
                                Log.d(AD_TAG, "FB Native ad finished downloading all assets.")
                            }

                            override fun onError(ad: Ad, adError: AdError) {
                                adsPlaceHolder.visibility = View.GONE
                                mListener.onError(adError.errorMessage)
                                Log.e(AD_TAG, "FB Native ad failed to load: " + adError.errorMessage)
                            }

                            override fun onAdLoaded(ad: Ad) {
                                mListener.onAdLoaded()
                                Log.d(AD_TAG, "FB Native ad is loaded and ready to be displayed!")
                                if (nativeMedium != ad) {
                                    return
                                }
                                // Inflate Native Ad into Container
                                inflateNativeMedium(it, adsPlaceHolder)
                            }

                            override fun onAdClicked(ad: Ad) {
                                mListener.onAdClicked()
                                Log.d(AD_TAG, "FB Native ad clicked!")
                            }

                            override fun onLoggingImpression(ad: Ad) {
                                mListener.onLoggingImpression()
                                Log.d(AD_TAG, "FB Native ad impression logged!")
                            }
                        })
                        .build()
                )
            }

        } else {
            adsPlaceHolder.visibility = View.GONE
            Log.e(AD_TAG, "Internet not Connected or App is Purchased or ad is not active from Firebase")
            mListener.onError("Internet not Connected or App is Purchased or ad is not active from Firebase")

        }


    }

    fun loadNativeSmallAd(
        adsPlaceHolder:FrameLayout,
        fbNativeSmallIds: String,
        isAdActive: Boolean,
        isAppPurchased: Boolean,
        isInternetConnected:Boolean,
        mListener: FbNativeCallBack
    ) {

        if (isInternetConnected && !isAppPurchased && isAdActive) {
            adsPlaceHolder.visibility = View.VISIBLE
            nativeSmall = NativeBannerAd(mActivity, fbNativeSmallIds)
            nativeSmall?.let {
                it.loadAd(
                    it.buildLoadAdConfig()
                        .withAdListener(object : NativeAdListener {
                            override fun onMediaDownloaded(ad: Ad) {
                                mListener.onMediaDownloaded()
                                Log.d(AD_TAG, "FB Native ad finished downloading all assets.")
                            }

                            override fun onError(ad: Ad, adError: AdError) {
                                adsPlaceHolder.visibility = View.GONE
                                mListener.onError(adError.errorMessage)
                                Log.e(AD_TAG, "FB Native ad failed to load: " + adError.errorMessage)
                            }

                            override fun onAdLoaded(ad: Ad) {
                                mListener.onAdLoaded()
                                Log.d(AD_TAG, "FB Native ad is loaded and ready to be displayed!")
                                if (nativeSmall != ad) {
                                    return
                                }
                                // Inflate Native Ad into Container
                                inflateNativeSmall(it, adsPlaceHolder)
                            }

                            override fun onAdClicked(ad: Ad) {
                                mListener.onAdClicked()
                                Log.d(AD_TAG, "FB Native ad clicked!")
                            }

                            override fun onLoggingImpression(ad: Ad) {
                                mListener.onLoggingImpression()
                                Log.d(AD_TAG, "FB Native ad impression logged!")
                            }
                        })
                        .build()
                )
            }

        } else {
            adsPlaceHolder.visibility = View.GONE
            Log.e(AD_TAG, "Internet not Connected or App is Purchased or ad is not active from Firebase")
            mListener.onError("Internet not Connected or App is Purchased or ad is not active from Firebase")

        }

    }

    private fun inflateNativeMedium(nativeAd: NativeAd, fbNativeContainer: FrameLayout) {
        nativeAd.unregisterView()

        // Add the Ad view into the ad container.
        val inflater = LayoutInflater.from(mActivity)

        val adView =  NativeAdLayout(mActivity)

        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
        val adViewNativeM: ConstraintLayout =
            inflater.inflate(R.layout.fb_native_medium, adView, false) as ConstraintLayout
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

    }

    private fun inflateNativeSmall(nativeBannerAd: NativeBannerAd, fbNativeContainer: FrameLayout) {
        // Unregister last ad
        nativeBannerAd.unregisterView()

        // Add the Ad view into the ad container.
        // Add the Ad view into the ad container.
        val inflater = LayoutInflater.from(mActivity)

        val adView =  NativeAdLayout(mActivity)
        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
        val adViewNativeB: ConstraintLayout =
            inflater.inflate(R.layout.fb_native_small, adView, false) as ConstraintLayout
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
    }

}