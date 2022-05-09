package com.orbitalsonic.facebookadsconfiguration

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.orbitalsonic.facebookadsconfiguration.adsconfig.FbBannerAds
import com.orbitalsonic.facebookadsconfiguration.adsconfig.FbSplashInterstitialAds
import com.orbitalsonic.facebookadsconfiguration.adsconfig.callbacks.FbInterstitialOnCallBack
import com.orbitalsonic.facebookadsconfiguration.adsconfig.callbacks.FbNativeCallBack
import com.orbitalsonic.facebookadsconfiguration.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var fbSplashInterstitial: FbSplashInterstitialAds
    private lateinit var fbBannerAds: FbBannerAds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)

        binding.btnNext.setOnClickListener {
            binding.btnNext.isEnabled = false
            intentMethod()
        }
        initAds()

    }

    private fun initAds(){
        fbSplashInterstitial = FbSplashInterstitialAds(this)
        fbBannerAds = FbBannerAds(this)


        // call splash interstitial
            fbSplashInterstitial.loadInterstitialAd(getString(R.string.facebook_interstitial_splash_ids),
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



        // call native ads
        fbBannerAds.loadNativeMedium(binding.adsContainerLayout,
            binding.fbPlaceHolder,
            binding.loadingLayout,
            getString(R.string.facebook_splash_native_ids),
            true,
            false,
            object : FbNativeCallBack {
                override fun onError(adError: String) { }

                override fun onAdLoaded() {}

                override fun onAdClicked() {}

                override fun onLoggingImpression() {}

                override fun onMediaDownloaded() {}

            })

    }


    private fun intentMethod() {
        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        finish()

        fbSplashInterstitial.showInterstitialAds()
    }


    override fun onDestroy() {
        super.onDestroy()
        fbSplashInterstitial.destroyInterstitialAds()

    }



}