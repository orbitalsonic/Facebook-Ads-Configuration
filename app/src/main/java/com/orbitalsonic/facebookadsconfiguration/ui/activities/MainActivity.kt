package com.orbitalsonic.facebookadsconfiguration.ui.activities

import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.orbitalsonic.facebookadsconfiguration.R
import com.orbitalsonic.facebookadsconfiguration.adsconfig.callbacks.FbInterstitialOnCallBack
import com.orbitalsonic.facebookadsconfiguration.databinding.ActivityMainBinding
import com.orbitalsonic.facebookadsconfiguration.helpers.firebase.RemoteConstants.rcvInterMain
import com.orbitalsonic.facebookadsconfiguration.helpers.firebase.RemoteConstants.rcvRemoteCounter
import com.orbitalsonic.facebookadsconfiguration.helpers.firebase.RemoteConstants.totalCount
import com.orbitalsonic.facebookadsconfiguration.helpers.utils.CleanMemory

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    /**
     *  No need to setContentView()
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initNavController()
    }


    private fun initNavController() {
        navController =
            (supportFragmentManager.findFragmentById(binding.navHostFragmentContainer.id) as NavHostFragment).navController
        appBarConfiguration =
            AppBarConfiguration(setOf(R.id.fragmentHome))
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun checkCounter() {
        try {
            if (diComponent.fbInterstitialAds.isInterstitialAdsLoaded()) {
                showInterstitialAd()
                totalCount += 1
            } else {
                if (totalCount >= rcvRemoteCounter) {
                    totalCount = 1
                    loadInterstitialAd()
                } else {
                    totalCount += 1
                }
            }
        } catch (e: Exception) {
            Log.d("AdsInformation", "${e.message}")
        }
    }


    fun loadInterstitialAd() {
        diComponent.fbInterstitialAds.loadInterstitialAd(
            this,
            getString(R.string.fb_inter_main_ids),
            rcvInterMain,
            diComponent.sharedPreferenceUtils.isAppPurchased,
            diComponent.internetManager.isInternetConnected,
            object : FbInterstitialOnCallBack {
                override fun onInterstitialDisplayed() {}
                override fun onInterstitialDismissed() {}
                override fun onError(adError: String) {}
                override fun onAdLoaded() {}
                override fun onAdClicked() {}
                override fun onLoggingImpression() {}
                override fun onPreloaded() {}
            }
        )
    }

    fun showInterstitialAd() {
        diComponent.fbInterstitialAds.showInterstitialAds()
    }


    /**
     *  Call 'CleanMemory.clean()' to avoid memory leaks.
     *  This destroys all the resources
     */

    override fun onDestroy() {
        CleanMemory.clean()
        super.onDestroy()
    }
}