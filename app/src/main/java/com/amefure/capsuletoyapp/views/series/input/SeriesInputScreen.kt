package com.amefure.capsuletoyapp.views.series.input

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.amefure.capsuletoyapp.models.domain.entity.Category
import com.amefure.capsuletoyapp.models.domain.entity.Location
import com.amefure.capsuletoyapp.models.enum.AppScreen
import com.amefure.capsuletoyapp.ui.theme.ExGold
import com.amefure.capsuletoyapp.ui.theme.ExRed
import com.amefure.capsuletoyapp.ui.theme.ExWhite
import com.amefure.capsuletoyapp.viewModels.SeriesInputScreenViewModel
import com.amefure.capsuletoyapp.views.components.layout.HeaderView
import com.amefure.capsuletoyapp.views.components.uiParts.AlertType
import com.amefure.capsuletoyapp.views.components.uiParts.CustomAlertDialog
import com.amefure.capsuletoyapp.views.components.uiParts.CustomText
import com.amefure.capsuletoyapp.views.components.uiParts.TextSize
import com.amefure.capsuletoyapp.views.components.uiParts.ThemeIconButton
import com.amefure.capsuletoyapp.views.components.uiParts.ThemeInputBox
import com.amefure.capsuletoyapp.views.components.uiParts.WhiteBackStackView

@Composable
fun SeriesInputScreen(
    seriesId: Long,
    navController: NavHostController,
    viewModel: SeriesInputScreenViewModel = hiltViewModel(),
) {
    // Compositionされたタイミングで実行する
    // 1回だけ発火して欲しいのでKeyは不変とする
    LaunchedEffect(Unit) {
        viewModel.preparePhotoUri()
        viewModel.fetchSingleSeries(seriesId)
    }

    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    val category = savedStateHandle
        ?.getStateFlow<Category?>(Category.KEY, null)
        ?.collectAsState()
        ?.value

    val location = savedStateHandle
        ?.getStateFlow<Location?>(Location.KEY, null)
        ?.collectAsState()
        ?.value

    // 値が変わるたびにViewModel側に登録
    LaunchedEffect(category) {
        category?.let {
            viewModel.addCategory(it)
        }
    }

    // 値が変わるたびにViewModel側に登録
    LaunchedEffect(location) {
        location?.let {
            viewModel.addLocation(it)
        }
    }

    Column(
        Modifier
            .padding(16.dp),
    ) {
        CustomAlertDialog(
            showFlag = viewModel.showSuccessDialog,
            rightAction = {
                viewModel.closeSuccessAlert()
                navController.popBackStack()
            },
            message = if (seriesId == 0L) {
                "「${viewModel.name}」を登録しました。"
            } else {
                "更新しました。"
            },
        )

        CustomAlertDialog(
            showFlag = viewModel.showValidationDialog,
            type = AlertType.FAILED,
            rightAction = { viewModel.closeValidationAlert() },
            message = "名前と種類は必須入力です。",
        )

        HeaderView(
            title = if (seriesId == 0L) "シリーズ登録" else "シリーズ更新",
            leftOnClick = { navController.popBackStack() },
            leftImageVector = Icons.AutoMirrored.Filled.ArrowBack,
            leftContentDescription = "画面を戻る",
            rightOnClick =
            {
                viewModel.createOrUpdateSeries(
                    capsuleToys = emptyList(),
                )
            },
            rightImageVector = Icons.Filled.Check,
            rightContentDescription = "シリーズ登録",
        )

        // サムネイル & 金額 & 種類数
        InputImageAndAmountSection(viewModel)

        ThemeInputBox(
            title = "シリーズ名",
            value = viewModel.name,
            onValueChange = {
                viewModel.name = it
            },
            placeholder = "例：△△シリーズ",
        )

        Spacer(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
        )

        // カテゴリ
        InputCategoriesSection(viewModel, navController)

        Spacer(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
        )

        // ガチャガチャ設置位置情報
        InputLocationSection(
            seriesId = seriesId,
            viewModel = viewModel,
            navController = navController
        )

        Spacer(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
        )

        ThemeInputBox(
            title = "MEMO",
            value = viewModel.memo,
            onValueChange = {
                viewModel.memo = it
            },
            singleLine = false,
        )

        Spacer(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
        )

        // 更新画面の場合は表示しない
        if (seriesId == 0L) {
            CustomText(
                text = "※ ガチャガチャのアイテムはシリーズを登録した後に、一覧からそのシリーズをタップすることで追加できます。",
                textSize = TextSize.S,
                maxLines = 3,
            )
        }
    }
}

@Composable
private fun InputImageAndAmountSection(
    viewModel: SeriesInputScreenViewModel
) {

    val thumbnail = viewModel.thumbnail

    // カメラ起動ランチャー
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
    ) { success ->
        if (success) {
            viewModel.onCameraCaptured()
        }
    }

    // ギャラリー起動ランチャー
    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ) { uri: Uri? ->
        uri?.let {
            viewModel.onGalleryImageSelected(it)
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
    ) {
        thumbnail?.let { bitmap ->
            Button(
                onClick = {
                    viewModel.expanded = true
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
            OutlinedButton(
                onClick = {
                    viewModel.expanded = true
                },
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(2.dp, ExGold),
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .aspectRatio(1f),
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "画像追加",
                    tint = ExGold,
                )
            }
        }

        DropdownMenu(
            expanded = viewModel.expanded,
            onDismissRequest = { viewModel.expanded = false },
        ) {
            DropdownMenuItem(
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        CustomText("カメラを起動する")
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Camera",
                        )
                    }
                },
                onClick = {
                    viewModel.expanded = false
                    // カメラ起動
                    viewModel.photoUri?.let { cameraLauncher.launch(it) }
                },
            )

            DropdownMenuItem(
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        CustomText("写真から選択する")
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = "Gallery",
                        )
                    }
                },
                onClick = {
                    viewModel.expanded = false
                    // ギャラリー起動
                    viewModel.photoUri?.let { imageLauncher.launch("image/*") }
                },
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
            ThemeInputBox(
                title = "金額",
                value = viewModel.amount?.toString() ?: "",
                onValueChange = {
                    viewModel.amount = if (it.isEmpty()) null else it.toIntOrNull() ?: 0
                },
                placeholder = "例：300円",
                isNumberOnly = true,
            )

            Spacer(
                modifier = Modifier
                    .padding(vertical = 8.dp),
            )

            ThemeInputBox(
                title = "種類数",
                value = viewModel.count?.toString() ?: "",
                onValueChange = {
                    viewModel.count = if (it.isEmpty()) null else it.toIntOrNull() ?: 0
                },
                placeholder = "例：6種類",
                isNumberOnly = true,
            )
        }
    }
}

@Composable
private fun InputCategoriesSection(
    viewModel: SeriesInputScreenViewModel,
    navController: NavHostController
) {
    CustomText(
        text = "カテゴリ",
        textSize = TextSize.S,
        fontWeight = FontWeight.Bold,
    )

    Spacer(modifier = Modifier.height(8.dp))

    LazyRow {
        items(items = viewModel.categories) { category ->
            Box(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .padding(vertical = 12.dp),
            ) {
                // カテゴリ名のUI
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
                        .padding(10.dp)
                        .align(Alignment.CenterStart),
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 12.dp, y = (-16).dp),
                ) {
                    ThemeIconButton(
                        onClick = {
                            viewModel.removeCategory(category)
                        },
                        imageVector = Icons.Default.Delete,
                        contentDescription = "削除",
                        baseSize = 36.dp,
                        iconSize = 20.dp,
                    )
                }
            }
        }
        item {
            OutlinedButton(
                onClick = { navController.navigate(AppScreen.CategoryInput.route()) },
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(2.dp, ExGold),
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .padding(vertical = 12.dp)
                    .width(70.dp)
                    .height(40.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "カテゴリ追加",
                    tint = ExGold,
                )
            }
        }
    }
}

@Composable
private fun InputLocationSection(
    seriesId: Long,
    viewModel: SeriesInputScreenViewModel,
    navController: NavHostController
) {
    CustomText(
        text = "ガチャガチャ設置場所",
        textSize = TextSize.S,
        fontWeight = FontWeight.Bold,
    )

    Spacer(
        Modifier
            .fillMaxWidth()
            .padding(8.dp),
    )

    LazyColumn(
        contentPadding = PaddingValues(2.dp)
    ) {
        items(viewModel.locations) { location ->
            WhiteBackStackView(
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    Icon(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        imageVector = if (location.getLatLng() == null) Icons.Filled.Map else Icons.Outlined.Map,
                        contentDescription = "位置情報登録ずみ",
                        tint = ExGold,
                    )

                    CustomText(location.name)
                }

                IconButton(
                    onClick = {
                        viewModel.removeLocation(location)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "位置情報削除",
                        tint = ExRed,
                    )
                }
            }
        }
    }

    OutlinedButton(
        onClick = { navController.navigate(AppScreen.LocationInput.route(seriesId)) },
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(2.dp, ExGold),
        modifier = Modifier
            .padding(vertical = 12.dp)
            .fillMaxWidth()
            .height(40.dp),
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "位置情報追加",
            tint = ExGold,
        )
    }
}