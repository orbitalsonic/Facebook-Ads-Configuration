package com.orbitalsonic.facebookadsconfiguration.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.orbitalsonic.facebookadsconfiguration.R
import com.orbitalsonic.facebookadsconfiguration.utils.GeneralUtils.AD_TAG
import com.orbitalsonic.facebookadsconfiguration.utils.GeneralUtils.IS_APP_PURCHASED
import com.orbitalsonic.facebookadsconfiguration.utils.GeneralUtils.isInternetConnected
import com.orbitalsonic.facebookadsconfiguration.utils.RemoteConfigConstants.isMainBannerActive
import com.orbitalsonic.facebookadsconfiguration.utils.RemoteConfigConstants.isMainInterstitialActive
import com.orbitalsonic.facebookadsconfiguration.utils.RemoteConfigConstants.isMainNativeActive
import com.orbitalsonic.facebookadsconfiguration.utils.RemoteConfigConstants.isSplashInterstitialActive
import com.orbitalsonic.facebookadsconfiguration.utils.RemoteConfigConstants.isSplashNativeActive
import com.orbitalsonic.facebookadsconfiguration.utils.RemoteConfigConstants.is_main_banner_active
import com.orbitalsonic.facebookadsconfiguration.utils.RemoteConfigConstants.is_main_interstitial_active
import com.orbitalsonic.facebookadsconfiguration.utils.RemoteConfigConstants.is_main_native_active
import com.orbitalsonic.facebookadsconfiguration.utils.RemoteConfigConstants.is_splash_interstitial_active
import com.orbitalsonic.facebookadsconfiguration.utils.RemoteConfigConstants.is_splash_native_active
import com.orbitalsonic.facebookadsconfiguration.utils.RemoteConfigConstants.remoteCounter
import com.orbitalsonic.facebookadsconfiguration.utils.RemoteConfigConstants.remote_counter_key
import com.orbitalsonic.facebookadsconfiguration.utils.RemoteConfigConstants.totalCount
import com.orbitalsonic.facebookadsconfiguration.adsconfig.FbBannerAds
import com.orbitalsonic.facebookadsconfiguration.adsconfig.FbInterstitialAds
import com.orbitalsonic.facebookadsconfiguration.adsconfig.callbacks.FbInterstitialOnCallBack
import com.orbitalsonic.facebookadsconfiguration.adsconfig.callbacks.FbNativeCallBack
import com.orbitalsonic.facebookadsconfiguration.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    private val mHandler = Handler(Looper.getMainLooper())
    private val adsRunner = Runnable { checkAdvertisement() }
    private var isInterstitialLoadOrFailed = false
    private var isNativeLoadedOrFailed = false
    private var mCounter: Int = 0

    private lateinit var remoteConfig: FirebaseRemoteConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)

        binding.btnNext.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()

            showInterstitial()

        }

        if (isInternetConnected(this)) {
            initRemoteConfig()
        } else {
            binding.adsPlaceHolder.visibility = View.GONE
            isInterstitialLoadOrFailed = true
            isNativeLoadedOrFailed = true
        }

    }


    private fun loadAds() {

        Log.d(AD_TAG, "Call FB Splash Interstitial")
        FbInterstitialAds(this).loadInterstitialAd(getString(R.string.fb_interstitial_splash_ids),
            isSplashInterstitialActive,
            IS_APP_PURCHASED,
            isInternetConnected(this),
            object : FbInterstitialOnCallBack {

                override fun onInterstitialDisplayed() {

                }

                override fun onInterstitialDismissed() {

                }

                override fun onError(adError: String) {
                    isInterstitialLoadOrFailed = true
                }

                override fun onAdLoaded() {
                    isInterstitialLoadOrFailed = true
                }

                override fun onAdClicked() {

                }

                override fun onLoggingImpression() {

                }

            })


        Log.d(AD_TAG, "Call FB Native")
        FbBannerAds(this).loadNativeMedium(
            binding.adsPlaceHolder,
            getString(R.string.fb_native_splash_ids),
            isSplashNativeActive,
            IS_APP_PURCHASED,
            isInternetConnected(this),
            object : FbNativeCallBack {

                override fun onError(adError: String) {
                    isNativeLoadedOrFailed = true
                }

                override fun onAdLoaded() {
                    isNativeLoadedOrFailed = true
                }

                override fun onAdClicked() {

                }

                override fun onLoggingImpression() {

                }

                override fun onMediaDownloaded() {
                }

            })

    }

    private fun showInterstitial() {
        FbInterstitialAds(this).showInterstitialAds()
    }

    private fun initRemoteConfig() {
        remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 2
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)

        fetchRemoteValues()
    }

    private fun fetchRemoteValues() {
        remoteConfig.fetchAndActivate().addOnCompleteListener {
            updateRemoteValues()
        }

    }

    private fun checkAdvertisement() {
        if (mCounter < 12) {
            try {
                mCounter++
                if (isInterstitialLoadOrFailed && isNativeLoadedOrFailed) {
                    binding.loadingProgress.visibility = View.GONE
                    binding.btnNext.visibility = View.VISIBLE
                }

            } catch (e: Exception) {
                Log.e(AD_TAG, "${e.message}")
            }

            mHandler.removeCallbacks { adsRunner }
            mHandler.postDelayed(
                adsRunner,
                (1000)
            )
        } else {
            binding.loadingProgress.visibility = View.GONE
            binding.btnNext.visibility = View.VISIBLE
        }

    }

    override fun onStop() {
        super.onStop()
        mHandler.removeCallbacks(adsRunner)
    }

    override fun onResume() {
        super.onResume()
        mHandler.post(adsRunner)
    }

    private fun updateRemoteValues() {
        /**
         * Interstitial Ads Active Keys
         */
        isSplashInterstitialActive = remoteConfig[is_splash_interstitial_active].asBoolean()
        isMainInterstitialActive = remoteConfig[is_main_interstitial_active].asBoolean()


        /**
         * Native Ads Active Keys
         */
        isSplashNativeActive = remoteConfig[is_splash_native_active].asBoolean()
        isMainNativeActive = remoteConfig[is_main_native_active].asBoolean()

        /**
         * Banner Ads Active Keys
         */
        isMainBannerActive = remoteConfig[is_main_banner_active].asBoolean()


        /**
         * Other Keys
         */
        val clickCount = remoteConfig[remote_counter_key].asLong().toInt()
        remoteCounter = if (clickCount > 0) {
            clickCount
        } else {
            2
        }

        totalCount = remoteCounter

        if (!isSplashInterstitialActive) {
            isInterstitialLoadOrFailed = true
        }

        if (!isSplashNativeActive) {
            isNativeLoadedOrFailed = true
        }


        loadAds()
        mCounter = 0

        val remoteTag = "RemoteConfigurationsTesting"
        Log.d(
            remoteTag,
            "is_splash_interstitial_active: ${remoteConfig[is_splash_interstitial_active].asBoolean()}"
        )

        Log.d(
            remoteTag,
            "remote_counter_key: ${remoteConfig[remote_counter_key].asLong().toInt()}"
        )

    }


}