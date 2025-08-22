package com.amefure.capsuletoyapp.View.Series

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.amefure.capsuletoyapp.Models.Enum.AppScreen
import com.amefure.capsuletoyapp.View.Components.Layout.HeaderView
import com.amefure.capsuletoyapp.View.Components.UIParts.CustomText
import com.amefure.capsuletoyapp.View.Components.UIParts.TextSize
import com.amefure.capsuletoyapp.View.Components.UIParts.WhiteBackStackView
import com.amefure.capsuletoyapp.ViewModel.SeriesDetailScreenViewModel
import com.amefure.capsuletoyapp.ui.theme.ExGold

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

    var showSuccessDialog by rememberSaveable { mutableStateOf(false) }
    var showConfirmDialog by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        if (showSuccessDialog) {
            AlertDialog(
                onDismissRequest = {
                    showSuccessDialog = false
                    navController.popBackStack()
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showSuccessDialog = false
                            navController.popBackStack()
                        }
                    ) {
                        CustomText("OK")
                    }
                },
                title = { CustomText("成功") },
                text = { CustomText("削除しました。") }
            )
        }

        if (showConfirmDialog) {
            AlertDialog(
                onDismissRequest = { showConfirmDialog = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showConfirmDialog = false
                            viewModel.deleteSeries()
                            showSuccessDialog = true
                        }
                    ) {
                        CustomText(
                            "削除",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showConfirmDialog = false
                        }
                    ) {
                        CustomText("キャンセル")
                    }
                },
                title = { CustomText("お知らせ") },
                text = {
                    CustomText(
                        "このデータを削除しますか？\n削除すると登録した画像なども削除されます。",
                        maxLines = 4
                    )
                }
            )
        }

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
            (viewModel.series?.series?.amount ?: 0).toString(),
            (viewModel.series?.series?.count ?: 0).toString(),
        )

        Button (
            onClick = {
                showConfirmDialog = true
            }
        ) {
            CustomText(
                "削除する"
            )
        }
    }
}

/**
 * 画像表示と金額種類数表示セクション
 */
@Composable
private fun ImageAndAmountSection(
    amount: String,
    count: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {

        OutlinedButton (
            onClick = { /* TODO */ },
            shape = RoundedCornerShape(12.dp),
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

        Spacer(
            modifier = Modifier
                .padding(horizontal = 8.dp)
        )

        Column(
            modifier = Modifier
                .weight(1f)
        ) {

            CustomText(
                text = "金額",
                textSize = TextSize.S,
                fontWeight = FontWeight.Bold,
            )

            Spacer(
                modifier = Modifier
                    .height(5.dp)
            )

            WhiteBackStackView {

                Spacer(
                    modifier = Modifier
                        .width(12.dp) // TextSize.SSと同等
                )

                CustomText(
                    text = amount,
                    textSize = TextSize.L,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                )

                CustomText(
                    text = "円",
                    textSize = TextSize.SS,
                )
            }


            Spacer(
                modifier = Modifier
                    .padding(vertical = 8.dp)
            )

            CustomText(
                text = "種類数",
                textSize = TextSize.S,
                fontWeight = FontWeight.Bold,
            )

            Spacer(
                modifier = Modifier
                    .height(5.dp)
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
                        .padding(horizontal = 5.dp)
                )
                CustomText(
                    text = "種",
                    textSize = TextSize.SS,
                )
            }
        }
    }
}
