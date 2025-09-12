package com.amefure.capsuletoyapp.views.series

import android.graphics.Bitmap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.amefure.capsuletoyapp.models.Enum.AppScreen
import com.amefure.capsuletoyapp.ui.theme.ExGold
import com.amefure.capsuletoyapp.ui.theme.ExWhite
import com.amefure.capsuletoyapp.view_models.SeriesDetailScreenViewModel
import com.amefure.capsuletoyapp.views.components.layout.HeaderView
import com.amefure.capsuletoyapp.views.components.ui_parts.AlertType
import com.amefure.capsuletoyapp.views.components.ui_parts.CustomAlertDialog
import com.amefure.capsuletoyapp.views.components.ui_parts.CustomText
import com.amefure.capsuletoyapp.views.components.ui_parts.TextSize
import com.amefure.capsuletoyapp.views.components.ui_parts.WhiteBackStackView

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

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
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
            viewModel.fetchImage(),
            (viewModel.series?.series?.amount ?: 0).toString(),
            (viewModel.series?.series?.count ?: 0).toString(),
        )

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
                        ).background(category.color, RoundedCornerShape(8.dp))
                        .height(40.dp)
                        .padding(10.dp),
                )
            }
        }

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
            OutlinedButton(
                onClick = { /* TODO */ },
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
