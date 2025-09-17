package com.amefure.capsuletoyapp.views.common

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.amefure.capsuletoyapp.models.enum.AppScreen
import com.amefure.capsuletoyapp.ui.theme.CapsuleToyAppTheme
import com.amefure.capsuletoyapp.viewModels.RootEnvironment
import com.amefure.capsuletoyapp.views.components.uiParts.AlertType
import com.amefure.capsuletoyapp.views.components.uiParts.CustomAlertDialog
import com.amefure.capsuletoyapp.views.mydata.MyDataScreen
import com.amefure.capsuletoyapp.views.series.SeriesDetailScreen
import com.amefure.capsuletoyapp.views.series.SeriesListScreen
import com.amefure.capsuletoyapp.views.series.input.CategoryInputScreen
import com.amefure.capsuletoyapp.views.series.input.LocationInputScreen
import com.amefure.capsuletoyapp.views.series.input.SeriesInputScreen
import com.amefure.capsuletoyapp.views.series.input.ToyInputScreen
import com.amefure.capsuletoyapp.views.settings.SettingsScreen
import dagger.hilt.android.AndroidEntryPoint

/** アプリのエントリーポイント */
@AndroidEntryPoint
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
private fun RootNavContent(
    rootEnvironment: RootEnvironment = hiltViewModel(),
) {
    val navController = rememberNavController()

    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
    ) { permissions ->
        val fineLocation = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseLocation = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

        if (fineLocation && coarseLocation) {
            // Toast.makeText(context, "許可されました", Toast.LENGTH_SHORT).show()
        } else {
            // 否認されていた場合は警告アラートを出す
            rootEnvironment.showPermissionAlertDialog()
        }
    }

    LaunchedEffect(Unit) {
        // 位置情報のパーミッション許可がされていないなら申請を出す
        if (!rootEnvironment.isGrantedLocationPermission()) {
            launcher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                ),
            )
        }
    }

    CustomAlertDialog(
        showFlag = rootEnvironment.isShowPermissionAlertDialog,
        type = AlertType.CONFIRM,
        rightTitle = "設定を開く",
        rightAction = {
            // 設定アプリを開く
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", context.packageName, null)
            }
            context.startActivity(intent)
            rootEnvironment.closePermissionAlertDialog()
        },
        message = "位置情報が有効にされていないため一部機能が使用できません。",
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                AppScreen.Tab.entries.forEach { tab ->
                    // 　最新の画面ルート情報を取得(変化したら再コンポーズされる)
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
                        icon = { /* アイコンを追加したければここに */ },
                    )
                }
            }
        },
    ) { innerPadding ->
        TabBarBottomWithNav(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Composable
private fun TabBarBottomWithNav(
    navController: NavHostController,
    modifier: Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = AppScreen.Tab.Series.route(),
        modifier = modifier,
    ) {
        /** シリーズ一覧 */
        composable(
            route = AppScreen.Tab.Series.route(),
        ) {
            SeriesListScreen(navController)
        }

        /** シリーズ詳細 */
        composable(
            route = AppScreen.SeriesDetail.route(),
            arguments = listOf(navArgument(AppScreen.SeriesDetail.ARG_ITEM_ID) { type = NavType.LongType }),
        ) { backStackEntry ->
            val seriesId: Long = backStackEntry.arguments?.getLong(AppScreen.SeriesDetail.ARG_ITEM_ID) ?: 0
            SeriesDetailScreen(seriesId, navController)
        }

        /** シリーズ登録・更新 */
        composable(
            route = AppScreen.SeriesInput.route(),
            arguments = listOf(navArgument(AppScreen.SeriesDetail.ARG_ITEM_ID) { type = NavType.LongType }),
            enterTransition = {
                slideInVertically(
                    // 下からスライド
                    initialOffsetY = { fullHeight -> fullHeight },
                ) + fadeIn()
            },
            exitTransition = {
                slideOutVertically(
                    // 下にスライド
                    targetOffsetY = { fullHeight -> fullHeight },
                ) + fadeOut()
            },
        ) { backStackEntry ->
            val seriesId: Long = backStackEntry.arguments?.getLong(AppScreen.SeriesDetail.ARG_ITEM_ID) ?: 0
            SeriesInputScreen(seriesId, navController)
        }

        /** カテゴリ登録・更新 */
        composable(
            route = AppScreen.CategoryInput.route(),
            enterTransition = {
                slideInVertically(
                    // 下からスライド
                    initialOffsetY = { fullHeight -> fullHeight },
                ) + fadeIn()
            },
            exitTransition = {
                slideOutVertically(
                    // 下にスライド
                    targetOffsetY = { fullHeight -> fullHeight },
                ) + fadeOut()
            },
        ) {
            CategoryInputScreen(navController)
        }

        /** ロケーション登録・更新 */
        composable(
            route = AppScreen.LocationInput.route(),
            arguments = listOf(navArgument(AppScreen.LocationInput.ARG_ITEM_ID) { type = NavType.LongType }),
            enterTransition = {
                slideInVertically(
                    // 下からスライド
                    initialOffsetY = { fullHeight -> fullHeight },
                ) + fadeIn()
            },
            exitTransition = {
                slideOutVertically(
                    // 下にスライド
                    targetOffsetY = { fullHeight -> fullHeight },
                ) + fadeOut()
            },
        ) { backStackEntry ->
            val seriesId: Long = backStackEntry.arguments?.getLong(AppScreen.LocationInput.ARG_ITEM_ID) ?: 0
            LocationInputScreen(seriesId, navController)
        }

        /** カプセルトイ登録・更新 */
        composable(
            route = AppScreen.ToyInput.route(),
            arguments = listOf(navArgument(AppScreen.ToyInput.ARG_ITEM_ID) { type = NavType.LongType }),
            enterTransition = {
                slideInVertically(
                    // 下からスライド
                    initialOffsetY = { fullHeight -> fullHeight },
                ) + fadeIn()
            },
            exitTransition = {
                slideOutVertically(
                    // 下にスライド
                    targetOffsetY = { fullHeight -> fullHeight },
                ) + fadeOut()
            },
        ) { backStackEntry ->
            val seriesId: Long = backStackEntry.arguments?.getLong(AppScreen.ToyInput.ARG_ITEM_ID) ?: 0
            ToyInputScreen(seriesId, navController)
        }

        composable(route = AppScreen.Tab.MyData.route()) {
            MyDataScreen(navController)
        }

        composable(route = AppScreen.Tab.Settings.route()) {
            SettingsScreen(navController)
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
