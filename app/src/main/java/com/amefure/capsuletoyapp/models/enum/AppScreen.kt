package com.amefure.capsuletoyapp.models.enum

sealed class AppScreen {
    abstract fun route(): String
    abstract val title: String

    /** タブ画面 */
    sealed class Tab : AppScreen() {

        data object Series : Tab() {
            override fun route() = "series"
            override val title = "ガチャガチャ"
        }

        data object MyData : Tab() {
            override fun route() = "my_data"
            override val title = "MyData"
        }

        data object Settings : Tab() {
            override fun route() = "settings"
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
        const val ARG_ITEM_ID = "itemId"
        override fun route() = "toy_input/{${ARG_ITEM_ID}}"
        fun route(seriesId: Long) = "toy_input/$seriesId"
        override val title = "ガチャガチャ登録"
    }
}
