package com.amefure.capsuletoyapp.models.enum

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddChart
import androidx.compose.material.icons.outlined.Contrast
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class AppScreen {
    abstract fun route(): String
    abstract val title: String

    /** タブ画面 */
    sealed class Tab : AppScreen() {

        abstract val icon: ImageVector

        data object Series : Tab() {
            override fun route() = "series"
            override val title = "ガチャガチャ"
            override val icon = Icons.Outlined.Contrast
        }

        // TODO：MyDataは未実装のため未使用
        data object MyData : Tab() {
            override fun route() = "my_data"
            override val title = "MyData"
            override val icon = Icons.Outlined.AddChart
        }

        data object Settings : Tab() {
            override fun route() = "settings"
            override val title = "Setting"
            override val icon = Icons.Outlined.Settings
        }

        companion object {
            // TODO：MyDataは未実装のため除外
            val entries = listOf(Series, Settings)
        }
    }

    /** 通常画面 */
    data object SeriesDetail : AppScreen() {
        const val ARG_ITEM_ID = "itemId"

        override fun route() = "detail/{$ARG_ITEM_ID}"
        fun route(seriesId: Long) = "detail/$seriesId"
        override val title = "詳細画面"
    }

    data object SeriesInput : AppScreen() {
        const val ARG_ITEM_ID = "itemId"

        override fun route() = "input/{$ARG_ITEM_ID}"
        fun inputRoute() = "input/0"
        fun updateRoute(seriesId: Long) = "input/$seriesId"
        override val title = "登録・更新画面"
    }

    data object CategoryInput : AppScreen() {
        override fun route() = "category_input"
        override val title = "カテゴリ登録"
    }

    data object LocationInput : AppScreen() {
        const val ARG_ITEM_ID = "itemId"
        override fun route() = "location_input/{${ARG_ITEM_ID}}"
        fun route(seriesId: Long) = "location_input/$seriesId"
        override val title = "ロケーション登録"
    }

    data object ToyInput : AppScreen() {
        const val ARG_SERIES_ID = "seriesId"
        const val ARG_TOY_ID = "toyId"
        override fun route() = "toy_input/{${ARG_SERIES_ID}}/{${ARG_TOY_ID}}"
        fun route(seriesId: Long, toyId: Long = 0) = "toy_input/$seriesId/$toyId"
        override val title = "ガチャガチャ登録"
    }

    data object ToyDetail : AppScreen() {
        const val ARG_SERIES_ID = "seriesId"
        const val ARG_TOY_ID = "toyId"
        override fun route() = "toy_detail/{${ARG_SERIES_ID}}/{${ARG_TOY_ID}}"
        fun route(seriesId: Long, toyId: Long = 0) = "toy_detail/$seriesId/$toyId"
        override val title = "ガチャガチャ詳細"
    }

    data object WebView : AppScreen() {
        const val ARG_SETTING_ITEM = "settingItem"
        override fun route() = "web_view/{${ARG_SETTING_ITEM}}"
        fun route(settingItemName: String) = "web_view/$settingItemName"
        override val title = "WebView"
    }
}
