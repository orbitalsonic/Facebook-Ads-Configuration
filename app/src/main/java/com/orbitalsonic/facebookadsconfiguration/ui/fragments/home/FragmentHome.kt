package com.orbitalsonic.facebookadsconfiguration.ui.fragments.home

import com.orbitalsonic.facebookadsconfiguration.R
import com.orbitalsonic.facebookadsconfiguration.adsconfig.FbBannerAds
import com.orbitalsonic.facebookadsconfiguration.adsconfig.callbacks.FbBannerCallBack
import com.orbitalsonic.facebookadsconfiguration.adsconfig.callbacks.FbNativeCallBack
import com.orbitalsonic.facebookadsconfiguration.databinding.FragmentHomeBinding
import com.orbitalsonic.facebookadsconfiguration.helpers.firebase.RemoteConstants
import com.orbitalsonic.facebookadsconfiguration.helpers.listeners.DebounceListener.setDebounceClickListener
import com.orbitalsonic.facebookadsconfiguration.ui.activities.MainActivity
import com.orbitalsonic.facebookadsconfiguration.ui.fragments.base.BaseFragment

class FragmentHome : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    /**
     * Don't use AdmobBannerAds in DI
     */
    private val fbBannerAds by lazy { FbBannerAds() }

    override fun onViewCreatedOneTime() {
        binding.mbClickHome.setDebounceClickListener {
            navigateTo(R.id.fragmentHome, R.id.action_fragmentHome_to_fragmentSample)
            (activity as MainActivity).checkCounter()
        }

        loadAds()
    }

    override fun onViewCreatedEverytime() {}

    override fun navIconBackPressed() {
        onBackPressed()
    }

    override fun onBackPressed() {

    }

    private fun loadAds() {
        fbBannerAds.loadBannerAds(
            activity,
            binding.adsBannerPlaceHolder,
            getResString(R.string.fb_banner_main_ids),
            RemoteConstants.rcvBannerHome,
            diComponent.sharedPreferenceUtils.isAppPurchased,
            diComponent.internetManager.isInternetConnected,
            object : FbBannerCallBack {
                override fun onError(adError: String) {}
                override fun onAdLoaded() {}
                override fun onAdClicked() {}
                override fun onLoggingImpression() {}

            }
        )

        diComponent.fbNativeAds.loadNativeSmallAd(
            activity,
            binding.adsNativePlaceHolder,
            getResString(R.string.fb_native_main_ids),
            RemoteConstants.rcvNativeHome,
            diComponent.sharedPreferenceUtils.isAppPurchased,
            diComponent.internetManager.isInternetConnected,
            object : FbNativeCallBack {
                override fun onError(adError: String) {}
                override fun onAdLoaded() {}
                override fun onAdClicked() {}
                override fun onLoggingImpression() {}
                override fun onMediaDownloaded() {}
                override fun onPreloaded() {}
            }
        )
    }
}