package com.amefure.capsuletoyapp.views.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.amefure.capsuletoyapp.models.enum.AppScreen
import com.amefure.capsuletoyapp.models.enum.SettingItems
import com.amefure.capsuletoyapp.ui.theme.ExRed
import com.amefure.capsuletoyapp.views.components.layout.HeaderView
import com.amefure.capsuletoyapp.views.components.uiParts.BannerAdView
import com.amefure.capsuletoyapp.views.components.uiParts.CustomText

@Composable
fun SettingsScreen(
    navController: NavHostController,
) {
    Column {
        HeaderView(
            title = "設定",
            leftImageVector = null,
            leftContentDescription = null,
        )

        Column(
            modifier = Modifier
                // .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(16.dp),
        ) {
            SettingItems.entries.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(AppScreen.WebView.route(item.name))
                        }
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(12.dp),
                        )
                        .padding(16.dp),

                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        tint = ExRed,
                        modifier = Modifier
                            .padding(horizontal = 8.dp),
                    )
                    CustomText(item.title)
                }
            }

            Spacer(
                modifier = Modifier
                    .weight(1f),
            )

            BannerAdView()
        }
    }
}
