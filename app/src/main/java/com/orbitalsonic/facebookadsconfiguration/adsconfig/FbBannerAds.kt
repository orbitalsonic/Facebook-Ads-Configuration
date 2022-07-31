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
import com.orbitalsonic.facebookadsconfiguration.GeneralUtils.AD_TAG
import com.orbitalsonic.facebookadsconfiguration.GeneralUtils.isInternetConnected
import com.orbitalsonic.facebookadsconfiguration.R
import com.orbitalsonic.facebookadsconfiguration.adsconfig.callbacks.FbBannerCallBack
import com.orbitalsonic.facebookadsconfiguration.adsconfig.callbacks.FbNativeCallBack


class FbBannerAds(activity: Activity) {

    private var adViewBanner: AdView? = null
    private val mActivity: Activity = activity
    private var nativeMedium: NativeAd? = null
    private var nativeSmall: NativeBannerAd? = null
    private var fbBannerCallBack: FbBannerCallBack? = null
    private var fbNativeCallBack: FbNativeCallBack? = null

    init {
        AudienceNetworkAds.initialize(activity)
    }

    fun loadBannerAds(
        adsContainerLayout: LinearLayout,
        adsHolder: LinearLayout,
        shimmerFrameLayout: FrameLayout,
        fbBannerIds: String,
        isRemoteConfigActive: Boolean,
        isAppPurchased: Boolean,
        mListener: FbBannerCallBack
    ) {
        fbBannerCallBack = mListener
        if (isInternetConnected(mActivity) && !isAppPurchased && isRemoteConfigActive) {
            adViewBanner = AdView(mActivity, fbBannerIds, AdSize.BANNER_HEIGHT_90)
            adViewBanner?.let {
                adsHolder.addView(it)
                it.loadAd()
                it.loadAd(it.buildLoadAdConfig().withAdListener(object : AdListener {

                    override fun onError(p0: Ad?, p1: AdError?) {
                        Log.i(AD_TAG, "FB Banner onError")
                        fbBannerCallBack?.onError(p1?.errorMessage.toString())
                        adsContainerLayout.visibility = View.GONE
                    }

                    override fun onAdLoaded(p0: Ad?) {
                        Log.i(AD_TAG, "FB Banner onAdLoaded")
                        shimmerFrameLayout.visibility = View.GONE
                        fbBannerCallBack?.onAdLoaded()
                    }

                    override fun onAdClicked(p0: Ad?) {
                        Log.i(AD_TAG, "FB Banner onAdClicked")
                        fbBannerCallBack?.onAdClicked()
                    }

                    override fun onLoggingImpression(p0: Ad?) {
                        Log.i(AD_TAG, "FB Banner onLoggingImpression")
                        fbBannerCallBack?.onLoggingImpression()
                    }

                }).build())

            }

        } else {
            adsContainerLayout.visibility = View.GONE
        }

    }

    fun destroyBannerAds() {
        adViewBanner?.destroy()
    }

    fun loadNativeMedium(
        adsContainerLayout: LinearLayout,
        nativeAdLayout: NativeAdLayout,
        shimmerFrameLayout: FrameLayout,
        fbNativeMediumIds: String,
        isRemoteConfigActive: Boolean,
        isAppPurchased: Boolean,
        mListener: FbNativeCallBack
    ) {
        fbNativeCallBack = mListener

        if (isInternetConnected(mActivity) && !isAppPurchased && isRemoteConfigActive) {
            nativeMedium = NativeAd(mActivity, fbNativeMediumIds)
            nativeMedium?.let {
                it.loadAd(
                    it.buildLoadAdConfig()
                        .withAdListener(object : NativeAdListener {
                            override fun onMediaDownloaded(ad: Ad) {
                                fbNativeCallBack?.onMediaDownloaded()
                                Log.e(AD_TAG, "FB Native ad finished downloading all assets.")
                            }

                            override fun onError(ad: Ad, adError: AdError) {
                                adsContainerLayout.visibility = View.GONE
                                fbNativeCallBack?.onError(adError.errorMessage)
                                Log.e(AD_TAG, "FB Native ad failed to load: " + adError.errorMessage)
                            }

                            override fun onAdLoaded(ad: Ad) {
                                shimmerFrameLayout.visibility = View.GONE
                                fbNativeCallBack?.onAdLoaded()
                                Log.d(AD_TAG, "FB Native ad is loaded and ready to be displayed!")
                                if (nativeMedium != ad) {
                                    return
                                }
                                // Inflate Native Ad into Container
                                inflateNativeMedium(it, nativeAdLayout)
                            }

                            override fun onAdClicked(ad: Ad) {
                                fbNativeCallBack?.onAdClicked()
                                Log.d(AD_TAG, "FB Native ad clicked!")
                            }

                            override fun onLoggingImpression(ad: Ad) {
                                fbNativeCallBack?.onLoggingImpression()
                                Log.d(AD_TAG, "FB Native ad impression logged!")
                            }
                        })
                        .build()
                )
            }

        } else {
            adsContainerLayout.visibility = View.GONE
        }


    }

    fun loadNativeSmallAd(
        adsContainerLayout: LinearLayout,
        nativeAdLayout: NativeAdLayout,
        shimmerFrameLayout: FrameLayout,
        fbNativeSmallIds: String,
        isRemoteConfigActive: Boolean,
        isAppPurchased: Boolean,
        mListener: FbNativeCallBack
    ) {
        fbNativeCallBack = mListener

        if (isInternetConnected(mActivity) && !isAppPurchased && isRemoteConfigActive) {
            nativeSmall = NativeBannerAd(mActivity, fbNativeSmallIds)
            nativeSmall?.let {
                it.loadAd(
                    it.buildLoadAdConfig()
                        .withAdListener(object : NativeAdListener {
                            override fun onMediaDownloaded(ad: Ad) {
                                fbNativeCallBack?.onMediaDownloaded()
                                Log.e(AD_TAG, "FB Native ad finished downloading all assets.")
                            }

                            override fun onError(ad: Ad, adError: AdError) {
                                adsContainerLayout.visibility = View.GONE
                                fbNativeCallBack?.onError(adError.errorMessage)
                                Log.e(AD_TAG, "FB Native ad failed to load: " + adError.errorMessage)
                            }

                            override fun onAdLoaded(ad: Ad) {
                                shimmerFrameLayout.visibility = View.GONE
                                fbNativeCallBack?.onAdLoaded()
                                Log.d(AD_TAG, "FB Native ad is loaded and ready to be displayed!")
                                if (nativeSmall != ad) {
                                    return
                                }
                                // Inflate Native Ad into Container
                                inflateNativeSmall(it, nativeAdLayout)
                            }

                            override fun onAdClicked(ad: Ad) {
                                fbNativeCallBack?.onAdClicked()
                                Log.d(AD_TAG, "FB Native ad clicked!")
                            }

                            override fun onLoggingImpression(ad: Ad) {
                                fbNativeCallBack?.onLoggingImpression()
                                Log.d(AD_TAG, "FB Native ad impression logged!")
                            }
                        })
                        .build()
                )
            }

        } else {
            adsContainerLayout.visibility = View.GONE
        }

    }

    private fun inflateNativeMedium(nativeAd: NativeAd, nativeAdLayout: NativeAdLayout) {
        nativeAd.unregisterView()

        // Add the Ad view into the ad container.
        val inflater = LayoutInflater.from(mActivity)
        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
        val adViewNativeM: ConstraintLayout =
            inflater.inflate(R.layout.fb_native_medium, nativeAdLayout, false) as ConstraintLayout
        nativeAdLayout.addView(adViewNativeM)
        // Add the AdOptionsView
        val adChoicesContainer: LinearLayout = adViewNativeM.findViewById(R.id.ad_choices_container)
        val adOptionsView = AdOptionsView(mActivity, nativeAd, nativeAdLayout)
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

    private fun inflateNativeSmall(nativeBannerAd: NativeBannerAd, nativeAdLayout: NativeAdLayout) {
        // Unregister last ad
        nativeBannerAd.unregisterView()

        // Add the Ad view into the ad container.
        // Add the Ad view into the ad container.
        val inflater = LayoutInflater.from(mActivity)
        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
        val adViewNativeB: ConstraintLayout =
            inflater.inflate(R.layout.fb_native_small, nativeAdLayout, false) as ConstraintLayout
        nativeAdLayout.addView(adViewNativeB)

        // Add the AdChoices icon
        val adChoicesContainer: LinearLayout =
            adViewNativeB.findViewById(R.id.ad_choices_container)
        val adOptionsView =
            AdOptionsView(mActivity, nativeBannerAd, nativeAdLayout)
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