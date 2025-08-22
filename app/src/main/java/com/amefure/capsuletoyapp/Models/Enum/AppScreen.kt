package com.amefure.capsuletoyapp.Models.Enum

sealed class AppScreen {
    abstract fun route(): String
    abstract val title: String

    /** タブ画面 */
    sealed class Tab : AppScreen() {

        data object Series : Tab() {
            override fun route() = "Series"
            override val title = "ガチャガチャ"
        }

        data object MyData : Tab() {
            override fun route() = "MyData"
            override val title = "MyData"
        }

        data object Settings : Tab() {
            override fun route() = "Settings"
            override val title = "Setting"
        }

        companion object {
            val entries = listOf(Series, MyData, Settings)
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
}

