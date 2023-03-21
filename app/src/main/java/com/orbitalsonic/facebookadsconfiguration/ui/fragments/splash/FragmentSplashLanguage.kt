package com.orbitalsonic.facebookadsconfiguration.ui.fragments.splash

import com.orbitalsonic.facebookadsconfiguration.R
import com.orbitalsonic.facebookadsconfiguration.databinding.FragmentSplashLanguageBinding
import com.orbitalsonic.facebookadsconfiguration.ui.activities.SplashActivity
import com.orbitalsonic.facebookadsconfiguration.ui.fragments.base.BaseFragment

class FragmentSplashLanguage : BaseFragment<FragmentSplashLanguageBinding>(R.layout.fragment_splash_language) {

    override fun onViewCreatedOneTime() {
        binding.mbContinueLanguage.setOnClickListener { onContinueClick() }
        showNativeAd()
    }

    override fun onViewCreatedEverytime() {}


    /**
     * Add Service in Manifest first
     */

    private fun onContinueClick() {
        if (isAdded) {
            diComponent.sharedPreferenceUtils.showFirstScreen = false
            (activity as SplashActivity).nextActivity()
            diComponent.fbInterstitialAds.showInterstitialAds()
        }
    }

    private fun showNativeAd() {
        if (isAdded) {
            diComponent.fbPreLoadNativeAds.showNativeMedium(
                activity,
                binding.adsPlaceHolder
            )
        }
    }

    override fun navIconBackPressed() {}

    override fun onBackPressed() {}
}