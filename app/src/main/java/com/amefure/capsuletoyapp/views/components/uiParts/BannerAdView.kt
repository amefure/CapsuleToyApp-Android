package com.amefure.capsuletoyapp.views.components.uiParts

import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.amefure.capsuletoyapp.BuildConfig
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun BannerAdView() {
    AndroidView(
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.BANNER)
                if (BuildConfig.DEBUG) {
                    BuildConfig.ADMOB_BANNER_ID_TEST
                } else {
                    BuildConfig.ADMOB_BANNER_ID_PROD
                }
                adUnitId = adUnitId
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}
