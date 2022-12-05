package com.orbitalsonic.facebookadsconfiguration.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.orbitalsonic.facebookadsconfiguration.R
import com.orbitalsonic.facebookadsconfiguration.utils.GeneralUtils.IS_APP_PURCHASED
import com.orbitalsonic.facebookadsconfiguration.utils.GeneralUtils.isInternetConnected
import com.orbitalsonic.facebookadsconfiguration.utils.RemoteConfigConstants.isMainBannerActive
import com.orbitalsonic.facebookadsconfiguration.utils.RemoteConfigConstants.isMainInterstitialActive
import com.orbitalsonic.facebookadsconfiguration.utils.RemoteConfigConstants.isMainNativeActive
import com.orbitalsonic.facebookadsconfiguration.utils.RemoteConfigConstants.remoteCounter
import com.orbitalsonic.facebookadsconfiguration.utils.RemoteConfigConstants.totalCount
import com.orbitalsonic.facebookadsconfiguration.adsconfig.FbBannerAds
import com.orbitalsonic.facebookadsconfiguration.adsconfig.FbInterstitialAds
import com.orbitalsonic.facebookadsconfiguration.adsconfig.callbacks.FbBannerCallBack
import com.orbitalsonic.facebookadsconfiguration.adsconfig.callbacks.FbInterstitialOnCallBack
import com.orbitalsonic.facebookadsconfiguration.adsconfig.callbacks.FbNativeCallBack
import com.orbitalsonic.facebookadsconfiguration.databinding.ActivityMainBinding
import com.orbitalsonic.facebookadsconfiguration.utils.GeneralUtils

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        loadAds()

        binding.btnShow.setOnClickListener {
            checkCounter()
        }
    }

    private fun checkCounter(){
        try {
            if (FbInterstitialAds(this).isInterstitialAdsLoaded()){
                showInterstitial()
                totalCount += 1
            }else{
                if (totalCount == remoteCounter) {
                    totalCount = 1
                    loadInterstitialAds()
                }else{
                    totalCount += 1
                }
            }
        }catch (e:Exception){
            Log.d(GeneralUtils.AD_TAG,"${e.message}")
        }
    }

    private fun loadAds() {
        Log.d(GeneralUtils.AD_TAG, "Call FB Native")
        FbBannerAds(this).loadNativeSmallAd(
            binding.nativePlaceHolder,
            getString(R.string.fb_native_main_ids),
            isMainNativeActive,
            IS_APP_PURCHASED,
            isInternetConnected(this),
            object : FbNativeCallBack {
                override fun onError(adError: String) {
                }

                override fun onAdLoaded() {
                }

                override fun onAdClicked() {
                }

                override fun onLoggingImpression() {
                }

                override fun onMediaDownloaded() {
                }


            })

        Log.d(GeneralUtils.AD_TAG, "Call FF Banner")
        FbBannerAds(this).loadBannerAds(
            binding.bannerPlaceHolder,
            getString(R.string.fb_banner_main_ids),
            isMainBannerActive,
            IS_APP_PURCHASED,
            isInternetConnected(this),
            object : FbBannerCallBack {
                override fun onError(adError: String) {

                }

                override fun onAdLoaded() {

                }

                override fun onAdClicked() {

                }

                override fun onLoggingImpression() {

                }


            })

    }

    private fun loadInterstitialAds() {

        Log.d(GeneralUtils.AD_TAG, "Call FB Main Interstitial")
        FbInterstitialAds(this).loadInterstitialAd(getString(R.string.fb_interstitial_main_ids),
            isMainInterstitialActive,
            IS_APP_PURCHASED,
            isInternetConnected(this),
            object : FbInterstitialOnCallBack {
                override fun onInterstitialDisplayed() {

                }

                override fun onInterstitialDismissed() {

                }

                override fun onError(adError: String) {

                }

                override fun onAdLoaded() {

                }

                override fun onAdClicked() {

                }

                override fun onLoggingImpression() {

                }


            })

    }

    private fun showInterstitial(){
        FbInterstitialAds(this).showInterstitialAds()
    }

    override fun onDestroy() {
        super.onDestroy()
        FbInterstitialAds(this).dismissInterstitialLoaded()
    }

}