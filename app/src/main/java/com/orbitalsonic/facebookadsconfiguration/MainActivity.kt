package com.orbitalsonic.facebookadsconfiguration

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.orbitalsonic.facebookadsconfiguration.adsconfig.FbBannerAds
import com.orbitalsonic.facebookadsconfiguration.adsconfig.FbInterstitialAds
import com.orbitalsonic.facebookadsconfiguration.adsconfig.callbacks.FbBannerCallBack
import com.orbitalsonic.facebookadsconfiguration.adsconfig.callbacks.FbInterstitialOnCallBack
import com.orbitalsonic.facebookadsconfiguration.adsconfig.callbacks.FbNativeCallBack
import com.orbitalsonic.facebookadsconfiguration.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var fbInterstitialAds: FbInterstitialAds
    private lateinit var fbBannerAds: FbBannerAds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initAds()
        binding.btnShow.setOnClickListener {
            fbInterstitialAds.showInterstitialAds()
        }
    }

    private fun initAds(){
        fbInterstitialAds = FbInterstitialAds(this)
        fbBannerAds = FbBannerAds(this)

        // load interstitial ads
        fbInterstitialAds.loadInterstitialAd(getString(R.string.facebook_interstitial_main_ids),
            true,
            false,object: FbInterstitialOnCallBack {
                override fun onInterstitialDisplayed() {}

                override fun onInterstitialDismissed() {}

                override fun onError() {
                }

                override fun onAdLoaded() {
                }

                override fun onAdClicked() {}

                override fun onLoggingImpression() {}

            })

        // load small native

        fbBannerAds.loadNativeSmallAd(binding.nativeAdsContainerLayout,
            binding.fbPlaceHolder,
            binding.nativeLoadingLayout,
            getString(R.string.facebook_small_native_ids),
            true,
            false,
            object : FbNativeCallBack {
                override fun onError(adError: String) { }

                override fun onAdLoaded() {}

                override fun onAdClicked() {}

                override fun onLoggingImpression() {}

                override fun onMediaDownloaded() {}

            })


        // load small Banner

        fbBannerAds.loadBannerAds(binding.bannerAdsContainerLayout,
            binding.bannerPlaceHolder,
            binding.bannerLoadingLayout,
            getString(R.string.facebook_banner_ids),
            true,
            false,
            object : FbBannerCallBack {
                override fun onError(adError: String) { }

                override fun onAdLoaded() {}

                override fun onAdClicked() {}

                override fun onLoggingImpression() {}

            })
    }

}