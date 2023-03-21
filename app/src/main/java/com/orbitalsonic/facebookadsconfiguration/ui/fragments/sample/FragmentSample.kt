package com.orbitalsonic.facebookadsconfiguration.ui.fragments.sample

import com.orbitalsonic.facebookadsconfiguration.R
import com.orbitalsonic.facebookadsconfiguration.adsconfig.callbacks.FbNativeCallBack
import com.orbitalsonic.facebookadsconfiguration.databinding.FragmentSampleBinding
import com.orbitalsonic.facebookadsconfiguration.helpers.firebase.RemoteConstants
import com.orbitalsonic.facebookadsconfiguration.ui.fragments.base.BaseFragment

class FragmentSample : BaseFragment<FragmentSampleBinding>(R.layout.fragment_sample) {

    override fun onViewCreatedOneTime() {
        loadAds()
    }

    override fun onViewCreatedEverytime() {}

    private fun loadAds() {
        diComponent.fbNativeAds.loadNativeMedium(
            activity,
            binding.adsPlaceHolder,
            getResString(R.string.fb_native_splash_ids),
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

    override fun navIconBackPressed() {
        onBackPressed()
    }

    override fun onBackPressed() {
        popFrom(R.id.fragmentSample)
    }
}