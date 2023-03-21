package com.orbitalsonic.facebookadsconfiguration.helpers.utils

import com.orbitalsonic.facebookadsconfiguration.adsconfig.constants.FbAdsConstants
import com.orbitalsonic.facebookadsconfiguration.helpers.firebase.RemoteConstants

object CleanMemory {

    fun clean() {
        RemoteConstants.reset()
        FbAdsConstants.reset()
    }

}