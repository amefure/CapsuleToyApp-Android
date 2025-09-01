package com.amefure.capsuletoyapp.View.Series.Input

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.amefure.capsuletoyapp.Models.Domain.Entity.Category
import com.amefure.capsuletoyapp.Models.Enum.AppScreen
import com.amefure.capsuletoyapp.View.Components.Layout.HeaderView
import com.amefure.capsuletoyapp.View.Components.UIParts.ThemaIconButton
import com.amefure.capsuletoyapp.View.Extension.CustomText
import com.amefure.capsuletoyapp.View.Extension.TextSize
import com.amefure.capsuletoyapp.View.Components.UIParts.ThemaTextFiled
import com.amefure.capsuletoyapp.View.Extension.AlertType
import com.amefure.capsuletoyapp.View.Extension.CustomAlertDialog
import com.amefure.capsuletoyapp.ViewModel.SeriesInputScreenViewModel
import com.amefure.capsuletoyapp.ui.theme.ExGold
import com.amefure.capsuletoyapp.ui.theme.ExWhite

@Composable
fun SeriesInputScreen(
    seriesId: Long,
    navController: NavHostController,
    viewModel: SeriesInputScreenViewModel = hiltViewModel(),
) {

    // Compositionされたタイミングで実行する
    // 1回だけ発火して欲しいのでKeyは不変とする
    LaunchedEffect(Unit) {
        viewModel.fetchSingleSeries(seriesId)
    }

    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    val category = savedStateHandle
        ?.getStateFlow<Category?>(Category.KEY, null)
        ?.collectAsState()
        ?.value

    // 値が変わるたびにViewModel側に登録
    LaunchedEffect(category) {
        category?.let {
            viewModel.addCategory(it)
        }
    }

    val thumbnail = viewModel.thumbnail

    // ActivityResultLauncher を Compose で記憶
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val bitmap = result.data?.extras?.get("data") as? Bitmap
            viewModel.onImageCaptured(bitmap)
        }
    }

    Column(
        Modifier
            .padding(16.dp)
    ) {

        CustomAlertDialog(
            showFlag = viewModel.showSuccessDialog,
            closeAction = {
                viewModel.closeSuccessAlert()
                navController.popBackStack()
            },
            message = {
                if (seriesId == 0L) {
                    CustomText("「${viewModel.name}」を登録しました。")
                } else {
                    CustomText("更新しました。")
                }
            }
        )

        CustomAlertDialog(
            showFlag = viewModel.showValidationDialog,
            type = AlertType.FAILED,
            closeAction = { viewModel.closeValidationAlert() },
            message = { CustomText("名前と種類は必須入力です。") }
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
                        locations = emptyList()
                    )
                },
            rightImageVector = Icons.Filled.Check,
            rightContentDescription = "シリーズ登録",
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {

            thumbnail?.let { bitmap ->
                Button(
                    onClick = {
                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        launcher.launch(intent)
                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .aspectRatio(1f),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "撮影した写真",
                        modifier = Modifier
                            .fillMaxSize()
                            .aspectRatio(1f),
                        contentScale = ContentScale.Crop
                    )
                }
            } ?: run {
                OutlinedButton (
                    onClick = {
                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        launcher.launch(intent)
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

            Spacer(
                modifier = Modifier
                .padding(horizontal = 8.dp)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
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
                        .padding(vertical = 8.dp)
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

        ThemeInputBox(
            title =  "シリーズ名",
            value = viewModel.name,
            onValueChange = {
                viewModel.name = it
            },
            placeholder = "例：△△シリーズ"
        )

        Spacer(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        CustomText(
            text = "カテゴリ",
            textSize = TextSize.S,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow{
            items(items = viewModel.categories) { category ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .padding(vertical = 12.dp)
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
                                clip = false
                            )
                            .background(category.color, RoundedCornerShape(8.dp))
                            .height(40.dp)
                            .padding(10.dp)
                            .align(Alignment.CenterStart)
                    )

                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = 12.dp, y = (-16).dp)
                    ) {
                        ThemaIconButton(
                            onClick = {
                                viewModel.removeCategory(category)
                            },
                            imageVector = Icons.Default.Delete,
                            contentDescription = "削除",
                            baseSize = 36.dp,
                            iconSize = 20.dp
                        )
                    }
                }
            }
            item {
                OutlinedButton (
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

        Spacer(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )


        ThemeInputBox(
            title = "MEMO",
            value = viewModel.memo,
            onValueChange = {
                viewModel.memo = it
            },
            singleLine = false
        )

        Spacer(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // 更新画面の場合は表示しない
        if (seriesId == 0L) {
            CustomText(
                text = "※ ガチャガチャのアイテムはシリーズを登録した後に、一覧からそのシリーズをタップすることで追加できます。",
                textSize = TextSize.S,
                maxLines = 3
            )
        }
    }
}

/**　サブラベル付き入力ボックス */
@Composable
fun ThemeInputBox(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    isNumberOnly: Boolean = false,
    singleLine: Boolean = true,
) {
    Column {
        CustomText(
            text = title,
            textSize = TextSize.S,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(8.dp))

        ThemaTextFiled(
            value = value,
            onValueChange = onValueChange,
            placeholder = placeholder,
            keyboardOptions = if (isNumberOnly) {
                KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                )
            } else {
                KeyboardOptions.Default
            },
            singleLine = singleLine,
        )
    }
}