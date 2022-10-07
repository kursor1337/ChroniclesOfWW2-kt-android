package com.kursor.chroniclesofww2.presentation.ui.activities

import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.objects.Tools
import com.phelat.navigationresult.FragmentResultActivity
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.nativeads.NativeAd
import com.yandex.mobile.ads.nativeads.NativeAdLoadListener
import com.yandex.mobile.ads.nativeads.NativeAdLoader
import com.yandex.mobile.ads.nativeads.NativeAdRequestConfiguration
import com.yandex.mobile.ads.nativeads.template.NativeBannerView
import com.yandex.mobile.ads.nativeads.template.appearance.BannerAppearance
import com.yandex.mobile.ads.nativeads.template.appearance.NativeTemplateAppearance
import com.yandex.mobile.ads.nativeads.template.appearance.TextAppearance
import kotlin.math.roundToInt


class MainActivity : FragmentResultActivity() {
    override fun getNavHostFragmentId(): Int = R.id.nav_host_fragment_container

    private lateinit var analytics: FirebaseAnalytics

    private lateinit var nativeAdLoader: NativeAdLoader

    private var currentAd: NativeAd? = null

    fun getNativeAdRequestConfig(height: Int, width: Int) =
        NativeAdRequestConfiguration.Builder("R-M-1970923-1")
            .setParameters(
                mapOf(
                    "preferable-width" to width.toString(),
                    "preferable-height" to height.toString()
                )
            ).build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val nativeAdView = findViewById<NativeBannerView>(R.id.native_ad_view).apply {
            applyAppearance(
                NativeTemplateAppearance.Builder()
                    .withBannerAppearance(
                        BannerAppearance.Builder()
                            .setBackgroundColor(Color.GRAY).build()
                    )
                    .withTitleAppearance(
                        TextAppearance.Builder()
                            .setTextColor(Color.BLUE).build()
                    ).build()
            )
        }

        analytics = Firebase.analytics
        nativeAdLoader = NativeAdLoader(this)
        nativeAdLoader.setNativeAdLoadListener(object : NativeAdLoadListener {
            override fun onAdLoaded(nativeAd: NativeAd) {
                Log.i("YandexAds", "Load Succeeded")
                currentAd = nativeAd
                nativeAdView.setAd(nativeAd)
            }

            override fun onAdFailedToLoad(adRequestError: AdRequestError) {
                Log.e("YandexAds", "Load Failed ${adRequestError.description}")
            }
        })

        nativeAdLoader.loadAd(
            getNativeAdRequestConfig(
                convertDpToPx(60),
                Tools.getScreenWidth()
            )
        )
    }

    private fun convertDpToPx(dp: Int): Int {
        return (dp * (resources.displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
    }
}