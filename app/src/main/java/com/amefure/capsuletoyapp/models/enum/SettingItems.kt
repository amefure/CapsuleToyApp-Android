package com.amefure.capsuletoyapp.models.enum

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.ui.graphics.vector.ImageVector

enum class SettingItems(
    val title: String,
    val icon: ImageVector,
    val url: String?,
) {
    CONTACT(
        title = "アプリの不具合はこちら",
        icon = Icons.Filled.NearMe,
        url = "https://appdev-room.com/contact",

    ),
    PRIVACY_POLICY(
        title = "利用規約とプライバシーポリシー",
        icon = Icons.Filled.Newspaper,
        url = "https://appdev-room.com/app-terms-of-service",
    ),
}
