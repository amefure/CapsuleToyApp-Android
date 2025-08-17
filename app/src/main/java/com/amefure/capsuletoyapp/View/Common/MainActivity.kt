package com.amefure.capsuletoyapp.View.Common

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.amefure.capsuletoyapp.Models.Enum.AppScreen
import com.amefure.capsuletoyapp.View.MyData.MyDataScreen
import com.amefure.capsuletoyapp.View.Series.SeriesDetailScreen
import com.amefure.capsuletoyapp.View.Series.SeriesListScreen
import com.amefure.capsuletoyapp.View.Settings.SettingsScreen
import com.amefure.capsuletoyapp.ui.theme.CapsuleToyAppTheme

/** アプリのエントリーポイント */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // EdgeToEdgeの有効化
        enableEdgeToEdge()
        setContent {
            CapsuleToyAppTheme {
                RootNavContent()
            }
        }
    }
}

@Composable
private fun RootNavContent() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            NavigationBar {
                AppScreen.Tab.entries.forEach { tab ->
                    //　最新の画面ルート情報を取得(変化したら再コンポーズされる)
                    val currentDestination = navController
                        .currentBackStackEntryAsState().value?.destination

                    NavigationBarItem(
                        // 選択状態は現在のタブ状態で識別
                        selected = currentDestination?.route == tab.route(),
                        onClick = {
                            navController.navigate(tab.route()) {
                                // バックスタックが無限に積み上がらないように制御
                                popUpTo(navController.graph.findStartDestination().id) {
                                    // popUpToで消された画面の状態を保存しておく
                                    saveState = true
                                }
                                // 今いる画面と同じルートに遷移しようとしたら、新しいインスタンスを積まずに再利用する
                                launchSingleTop = true
                                // saveStateで保存された状態があればそれを復元する
                                restoreState = true
                            }
                        },
                        label = { Text(tab.title) },
                        icon = { /* アイコンを追加したければここに */ }
                    )
                }
            }
        }
    ) { innerPadding ->
        TabBarBottomWithNav(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun TabBarBottomWithNav(
    navController: NavHostController,
    modifier: Modifier
) {
    NavHost(
        navController = navController,
        startDestination = AppScreen.Tab.Series.route(),
        modifier = modifier
    ) {
        composable(
            route = AppScreen.Tab.Series.route()
        ) {
            SeriesListScreen(onItemClick = { id ->
                navController.navigate("detail/$id")
            }, onSettingsClick = {
                navController.navigate(AppScreen.Tab.Settings.route())
            })
        }

        composable(
            route = AppScreen.SeriesDetail.route(),
            arguments = listOf(navArgument(AppScreen.SeriesDetail.ARG_ITEM_ID) { type = NavType.IntType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getInt(AppScreen.SeriesDetail.ARG_ITEM_ID) ?: 0
            SeriesDetailScreen(itemId = itemId, onBack = { navController.popBackStack() })
        }

        composable(
            route = AppScreen.Tab.MyData.route(),
        ) {
            MyDataScreen(onBack = { navController.popBackStack() })
        }

        composable(
            route = AppScreen.Tab.Settings.route()
        ) {
            SettingsScreen(onBack = { navController.popBackStack() })
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun RootNavContentPreview() {
    CapsuleToyAppTheme {
        RootNavContent()
    }
}