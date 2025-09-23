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
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import com.amefure.capsuletoyapp.models.enum.SettingItems
import com.amefure.capsuletoyapp.ui.theme.CapsuleToyAppTheme
import com.amefure.capsuletoyapp.ui.theme.ExRed
import com.amefure.capsuletoyapp.viewModels.RootEnvironment
import com.amefure.capsuletoyapp.views.components.uiParts.AlertType
import com.amefure.capsuletoyapp.views.components.uiParts.CustomAlertDialog
import com.amefure.capsuletoyapp.views.components.uiParts.CustomText
import com.amefure.capsuletoyapp.views.mydata.MyDataScreen
import com.amefure.capsuletoyapp.views.series.SeriesDetailScreen
import com.amefure.capsuletoyapp.views.series.SeriesListScreen
import com.amefure.capsuletoyapp.views.series.ToyDetailScreen
import com.amefure.capsuletoyapp.views.series.input.CategoryInputScreen
import com.amefure.capsuletoyapp.views.series.input.LocationInputScreen
import com.amefure.capsuletoyapp.views.series.input.SeriesInputScreen
import com.amefure.capsuletoyapp.views.series.input.ToyInputScreen
import com.amefure.capsuletoyapp.views.settings.CustomWebView
import com.amefure.capsuletoyapp.views.settings.SettingsScreen
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.AndroidEntryPoint

/** アプリのエントリーポイント */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // AdMobの有効化
        MobileAds.initialize(this)

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
                    // 最新の画面ルート情報を取得(変化したら再コンポーズされる)
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
                        label = {
                            CustomText(
                                tab.title,
                                color = if (currentDestination?.route == tab.route()) ExRed else Color.Gray,
                            )
                        },
                        icon = {
                            val color = if (currentDestination?.route == tab.route()) ExRed else Color.Gray
                            Icon(
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .rotate(90f),
                                imageVector = tab.icon,
                                contentDescription = tab.title,
                                tint = color,
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            // ボタンタップ後の選択背景を透明化
                            // タップ時の波紋(リップル)は非表示にならない
                            indicatorColor = Color.Transparent,
                        ),
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
            arguments = listOf(
                navArgument(AppScreen.ToyInput.ARG_SERIES_ID) { type = NavType.LongType },
                navArgument(AppScreen.ToyInput.ARG_TOY_ID) { type = NavType.LongType },
            ),
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
            val seriesId: Long = backStackEntry.arguments?.getLong(AppScreen.ToyInput.ARG_SERIES_ID) ?: 0
            val toyId: Long = backStackEntry.arguments?.getLong(AppScreen.ToyInput.ARG_TOY_ID) ?: 0
            ToyInputScreen(seriesId, toyId, navController)
        }

        /** カプセルトイ詳細 */
        composable(
            route = AppScreen.ToyDetail.route(),
            arguments = listOf(
                navArgument(AppScreen.ToyDetail.ARG_SERIES_ID) { type = NavType.LongType },
                navArgument(AppScreen.ToyDetail.ARG_TOY_ID) { type = NavType.LongType },
            ),
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
            val seriesId: Long = backStackEntry.arguments?.getLong(AppScreen.ToyInput.ARG_SERIES_ID) ?: 0
            val toyId: Long = backStackEntry.arguments?.getLong(AppScreen.ToyInput.ARG_TOY_ID) ?: 0
            ToyDetailScreen(seriesId, toyId, navController)
        }

        composable(route = AppScreen.Tab.MyData.route()) {
            MyDataScreen(navController)
        }

        composable(route = AppScreen.Tab.Settings.route()) {
            SettingsScreen(navController)
        }

        composable(
            route = AppScreen.WebView.route(),
            arguments = listOf(navArgument(AppScreen.WebView.ARG_SETTING_ITEM) { type = NavType.StringType }),
        ) { backStackEntry ->
            val itemName: String = backStackEntry.arguments?.getString(AppScreen.WebView.ARG_SETTING_ITEM).orEmpty()
            val item = SettingItems.valueOf(itemName)
            CustomWebView(item, navController)
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
