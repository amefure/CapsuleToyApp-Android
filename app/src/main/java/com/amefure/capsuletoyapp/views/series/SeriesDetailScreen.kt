package com.amefure.capsuletoyapp.views.series

import android.graphics.Bitmap
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.outlined.Contrast
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.amefure.capsuletoyapp.R
import com.amefure.capsuletoyapp.models.enum.AppScreen
import com.amefure.capsuletoyapp.ui.theme.ExRed
import com.amefure.capsuletoyapp.ui.theme.ExWhite
import com.amefure.capsuletoyapp.viewModels.SeriesDetailScreenViewModel
import com.amefure.capsuletoyapp.views.components.layout.HeaderView
import com.amefure.capsuletoyapp.views.components.uiParts.AlertType
import com.amefure.capsuletoyapp.views.components.uiParts.BannerAdView
import com.amefure.capsuletoyapp.views.components.uiParts.CustomAlertDialog
import com.amefure.capsuletoyapp.views.components.uiParts.CustomText
import com.amefure.capsuletoyapp.views.components.uiParts.TextSize
import com.amefure.capsuletoyapp.views.components.uiParts.ThemeIconButton
import com.amefure.capsuletoyapp.views.components.uiParts.WhiteBackStackView
import com.google.android.gms.ads.AdSize
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun SeriesDetailScreen(
    seriesId: Long,
    navController: NavHostController,
    viewModel: SeriesDetailScreenViewModel = hiltViewModel(),
) {
    // Compositionされたタイミングで実行する
    LaunchedEffect(Unit) {
        viewModel.fetchSingleSeries(seriesId)
    }
    val scrollState = rememberScrollState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
    ) {
        CustomAlertDialog(
            showFlag = viewModel.showSuccessDialog,
            rightAction = {
                viewModel.closeSuccessAlert()
                navController.popBackStack()
            },
            message = "削除しました。",
        )

        CustomAlertDialog(
            showFlag = viewModel.showConfirmDialog,
            type = AlertType.CONFIRM,
            rightTitle = "削除",
            rightAction = {
                viewModel.closeConfirmAlert()
                viewModel.deleteSeries()
                viewModel.showSuccessAlert()
            },
            cancelAction = {
                viewModel.closeConfirmAlert()
            },
            message = "このデータを削除しますか？\n削除すると登録した画像なども削除されます。",
        )

        HeaderView(
            title = viewModel.series?.series?.name ?: "シリーズ詳細",
            leftOnClick = { navController.popBackStack() },
            leftImageVector = Icons.AutoMirrored.Filled.ArrowBack,
            leftContentDescription = "画面を戻る",
            rightOnClick =
            {
                navController.navigate(AppScreen.SeriesInput.updateRoute(seriesId))
            },
            rightImageVector = Icons.Filled.Edit,
            rightContentDescription = "シリーズ更新画面遷移",
        )

        // 画像表示と金額種類数表示セクション
        ImageAndAmountSection(
            viewModel.fetchImage(viewModel.series?.series?.imagePath),
            (viewModel.series?.series?.amount ?: 0).toString(),
            (viewModel.series?.series?.count ?: 0).toString(),
        )

        // カテゴリセクション
        CategoriesSection(viewModel)

        Spacer(
            modifier = Modifier
                .padding(vertical = 8.dp),
        )

        // 総数 / 所持数セクション
        TotalCountSection(viewModel)

        Spacer(
            modifier = Modifier
                .padding(vertical = 8.dp),
        )

        // MEMOセクション
        if (!viewModel.series?.series?.memo.isNullOrEmpty()) {
            CustomText(
                text = "MEMO",
                textSize = TextSize.S,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth(),
            )

            Spacer(
                modifier = Modifier
                    .padding(vertical = 8.dp),
            )
            WhiteBackStackView {
                CustomText(viewModel.series?.series?.memo!!)
            }
        }

        Spacer(
            modifier = Modifier
                .padding(vertical = 8.dp),
        )

        // 位置情報セクション
        LocationsSection(viewModel)

        Spacer(
            modifier = Modifier
                .padding(vertical = 8.dp),
        )

        ToysSection(
            seriesId,
            navController,
            viewModel,
        )

        Spacer(
            modifier = Modifier
                .padding(vertical = 8.dp),
        )

        Button(
            onClick = {
                viewModel.showConfirmAlert()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = ExWhite,
            ),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(
                2.dp,
                color = MaterialTheme.colorScheme.primary,
            ),
            modifier = Modifier
                .width(200.dp)
                .height(50.dp),
        ) {
            CustomText(
                "削除する",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
            )
        }

        BannerAdView(
            size = AdSize.LARGE_BANNER,
            modifier = Modifier
                .padding(vertical = 16.dp),
        )
    }
}

/**
 * 画像表示と金額種類数表示セクション
 */
@Composable
private fun ImageAndAmountSection(
    bitmap: Bitmap?,
    amount: String,
    count: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
    ) {
        bitmap?.let {
            Button(
                onClick = {
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .aspectRatio(1f),
                contentPadding = PaddingValues(0.dp),
            ) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "撮影した写真",
                    modifier = Modifier
                        .fillMaxSize()
                        .aspectRatio(1f),
                    contentScale = ContentScale.Crop,
                )
            }
        } ?: run {
            Image(
                painter = painterResource(id = R.drawable.no_image),
                contentDescription = "サンプル画像",
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .aspectRatio(1f),
                contentScale = ContentScale.Fit,
            )
        }

        Spacer(
            modifier = Modifier
                .padding(horizontal = 8.dp),
        )

        Column(
            modifier = Modifier
                .weight(1f),
        ) {
            CustomText(
                text = "金額",
                textSize = TextSize.S,
                fontWeight = FontWeight.Bold,
            )

            Spacer(
                modifier = Modifier
                    .height(5.dp),
            )

            WhiteBackStackView {
                Spacer(
                    modifier = Modifier
                        .width(12.dp), // TextSize.SSと同等
                )

                CustomText(
                    text = amount,
                    textSize = TextSize.L,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(horizontal = 5.dp),
                )

                CustomText(
                    text = "円",
                    textSize = TextSize.SS,
                )
            }

            Spacer(
                modifier = Modifier
                    .padding(vertical = 8.dp),
            )

            CustomText(
                text = "種類数",
                textSize = TextSize.S,
                fontWeight = FontWeight.Bold,
            )

            Spacer(
                modifier = Modifier
                    .height(5.dp),
            )
            WhiteBackStackView {
                CustomText(
                    text = "全",
                    textSize = TextSize.SS,
                )

                CustomText(
                    text = count,
                    textSize = TextSize.L,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(horizontal = 5.dp),
                )
                CustomText(
                    text = "種",
                    textSize = TextSize.SS,
                )
            }
        }
    }
}

@Composable
private fun CategoriesSection(
    viewModel: SeriesDetailScreenViewModel,
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
    ) {
        items(items = viewModel.series?.categories ?: emptyList()) { category ->
            CustomText(
                text = category.name,
                textSize = TextSize.S,
                color = ExWhite,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(8.dp),
                        clip = false,
                    )
                    .background(category.color, RoundedCornerShape(8.dp))
                    .height(40.dp)
                    .padding(10.dp),
            )
        }
    }
}

/** 総数 / 所持数セクション */
@Composable
private fun TotalCountSection(
    viewModel: SeriesDetailScreenViewModel,
) {
    CustomText(
        text = "所持数",
        textSize = TextSize.S,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth(),
    )

    Spacer(
        modifier = Modifier
            .padding(vertical = 8.dp),
    )

    WhiteBackStackView {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
        ) {
            val isOwnedCount = viewModel.fetchIsOwnedCount()
            items(count = viewModel.fetchTotalCount()) { index ->
                val color = if (index + 1 <= isOwnedCount) ExRed else ExRed.copy(alpha = 0.3f)
                Icon(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .scale(1.4f)
                        .rotate(90f),
                    imageVector = Icons.Outlined.Contrast,
                    contentDescription = "所持数アイコン",
                    tint = color,
                )
            }
        }
    }
}

/** ローケーションセクション */
@Composable
private fun LocationsSection(
    viewModel: SeriesDetailScreenViewModel,
) {
    CustomText(
        text = "設置場所",
        textSize = TextSize.S,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth(),
    )

    Spacer(
        modifier = Modifier
            .padding(vertical = 8.dp),
    )

    var selectedTab by remember { mutableStateOf(LocationTab.MAP) }

    SegmentedLocationPicker(
        selectedTab = selectedTab,
        onOptionSelected = { selectedTab = it },
    )

    Spacer(
        modifier = Modifier
            .padding(vertical = 8.dp),
    )

    when (selectedTab) {
        LocationTab.MAP -> {
            // 地図の初期位置
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(viewModel.initialLatLng, 15f)
            }

            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = false,
                    myLocationButtonEnabled = true,
                ),
                properties = MapProperties(
                    mapType = MapType.NORMAL,
                    isMyLocationEnabled = true,
                ),
                onMapLoaded = {
                    viewModel.series?.locations?.firstNotNullOfOrNull { it.getLatLng() }?.let {
                        // 位置情報が登録されていれば最初に登録されている位置情報を地図の初期表示位置にする
                        cameraPositionState.move(
                            CameraUpdateFactory.newLatLngZoom(it.latLng, 15f),
                        )
                    }
                },
            ) {
                viewModel.series?.locations?.mapNotNull { it.getLatLng() }?.forEach {
                    Marker(
                        state = MarkerState(position = it.latLng),
                        title = it.name,
                        snippet = "緯度: ${it.latLng.latitude}, 経度: ${it.latLng.longitude}",
                    )
                }
            }
        }
        LocationTab.LIST -> {
            viewModel.series?.locations?.forEach { location ->
                WhiteBackStackView(
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Row {
                        Icon(
                            modifier = Modifier.padding(horizontal = 4.dp),
                            imageVector = if (location.getLatLng() == null) Icons.Filled.Map else Icons.Outlined.Map,
                            contentDescription = "位置情報登録ずみ",
                            tint = ExRed,
                        )

                        CustomText(location.name)
                    }
                }
            }
        }
    }
}

/** ローケーションタブ */
private enum class LocationTab(
    val title: String,
    val icon: ImageVector,
) {
    MAP("Location Map", Icons.Outlined.Map),
    LIST("Location List", Icons.AutoMirrored.Filled.ListAlt),
}

/** セグメントロケーションピッカー */
@Composable
private fun SegmentedLocationPicker(
    tabs: List<LocationTab> = LocationTab.entries,
    selectedTab: LocationTab,
    onOptionSelected: (LocationTab) -> Unit,
) {
    val selectedIndex = tabs.indexOf(selectedTab)

    BoxWithConstraints(
        modifier = Modifier
            .height(40.dp)
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(8.dp),
                clip = false,
            )
            .clip(RoundedCornerShape(16))
            .background(Color.White),
    ) {
        // タブのサイズ数に応じてインジケータの横幅を計算
        val tabWidth = maxWidth / tabs.size
        val indicatorOffset by animateDpAsState(
            targetValue = selectedIndex * tabWidth,
            label = "IndicatorOffset",
        )

        // 背面でアニメーション移動するインジケーターView
        Box(
            modifier = Modifier
                .offset(x = indicatorOffset)
                .width(tabWidth)
                .fillMaxHeight()
                .background(ExRed, RoundedCornerShape(16)),
        )

        // タブアイコンView
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            tabs.forEach { tab ->
                val isSelected = tab == selectedTab
                val iconTint by animateColorAsState(
                    targetValue = if (isSelected) Color.White else ExRed,
                    label = "IconTint",
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onOptionSelected(tab) },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = tab.title,
                        tint = iconTint,
                    )
                }
            }
        }
    }
}

@Composable
private fun ToysSection(
    seriesId: Long,
    navController: NavHostController,
    viewModel: SeriesDetailScreenViewModel,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        CustomText(
            text = "ガチャガチャアイテム一覧",
            textSize = TextSize.S,
            fontWeight = FontWeight.Bold,
        )

        ThemeIconButton(
            onClick = {
                navController.navigate(AppScreen.ToyInput.route(seriesId))
            },
            imageVector = Icons.Filled.Add,
            contentDescription = "ガチャガチャアイテム登録画面へ遷移",
        )
    }

    Spacer(
        modifier = Modifier
            .padding(vertical = 8.dp),
    )

    Column {
        viewModel.series?.capsuleToys.orEmpty().chunked(2).forEach { rowItems -> // 2列ずつに分割
            Row(modifier = Modifier.fillMaxWidth()) {
                rowItems.forEach { toy ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(8.dp)
                            .background(Color.White, RoundedCornerShape(8.dp))
                            .shadow(8.dp, RoundedCornerShape(8.dp), clip = false)
                            .shadow(8.dp, RoundedCornerShape(8.dp), clip = false)
                            .clickable {
                                navController.navigate(AppScreen.ToyDetail.route(seriesId, toy.id))
                            },
                        contentAlignment = Alignment.Center,
                    ) {
                        ThemeIconButton(
                            onClick = {},
                            imageVector = Icons.Filled.Check,
                            contentDescription = "所持済みフラグ",
                            containerColor = if (toy.isOwned) MaterialTheme.colorScheme.primary else Color.Gray,
                            baseSize = 40.dp,
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .offset(x = (-12).dp, y = (-12).dp)
                                .zIndex(1f),
                        )

                        toy.imagePath?.let {
                            val bitmap = viewModel.fetchImage(it)?.asImageBitmap()
                            bitmap?.let {
                                Image(
                                    bitmap = it,
                                    contentDescription = "撮影した写真",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .aspectRatio(1f)
                                        .zIndex(0f),
                                    contentScale = ContentScale.Crop,
                                )
                            }
                        } ?: run {
                            Image(
                                painter = painterResource(id = R.drawable.no_image),
                                contentDescription = "サンプル画像",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .zIndex(0f),
                                contentScale = ContentScale.Fit,
                            )
                        }

                        CustomText(
                            toy.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White.copy(0.5f))
                                .height(40.dp)
                                // Box内の配置位置
                                .align(Alignment.BottomEnd)
                                // View内のテキスト配置位置
                                .wrapContentSize(Alignment.Center),
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
                // もし要素が1個だけなら、もう1列を空Boxで埋める
                if (rowItems.size < 2) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(4.dp),
                    )
                }
            }
        }
    }
}
