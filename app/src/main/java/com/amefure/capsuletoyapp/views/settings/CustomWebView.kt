package com.amefure.capsuletoyapp.views.settings

import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.amefure.capsuletoyapp.models.enum.SettingItems
import com.amefure.capsuletoyapp.views.components.layout.HeaderView

@Composable
fun CustomWebView(
    settingItems: SettingItems,
    navController: NavHostController,
) {
    Column {
        HeaderView(
            title = settingItems.title,
            leftOnClick = { navController.popBackStack() },
            leftImageVector = Icons.AutoMirrored.Filled.ArrowBack,
            leftContentDescription = "画面を戻る",
        )

        settingItems.url?.let {
            AndroidView(
                modifier = Modifier
                    .padding(vertical = 8.dp),
                factory = { context ->
                    WebView(context).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                        )
                        settings.javaScriptEnabled = false
                        webViewClient = WebViewClient()
                        loadUrl(settingItems.url)
                    }
                },
            )
        }
    }
}
