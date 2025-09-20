package com.amefure.capsuletoyapp.views.series

import android.graphics.Bitmap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.amefure.capsuletoyapp.R
import com.amefure.capsuletoyapp.models.domain.entity.CapsuleToy
import com.amefure.capsuletoyapp.models.enum.AppScreen
import com.amefure.capsuletoyapp.ui.theme.ExWhite
import com.amefure.capsuletoyapp.viewModels.ToyDetailScreenViewModel
import com.amefure.capsuletoyapp.views.components.layout.HeaderView
import com.amefure.capsuletoyapp.views.components.uiParts.AlertType
import com.amefure.capsuletoyapp.views.components.uiParts.CustomAlertDialog
import com.amefure.capsuletoyapp.views.components.uiParts.CustomText
import com.amefure.capsuletoyapp.views.components.uiParts.TextSize
import com.amefure.capsuletoyapp.views.components.uiParts.ThemeIconButton
import com.amefure.capsuletoyapp.views.components.uiParts.WhiteBackStackView

@Composable
fun ToyDetailScreen(
    seriesId: Long,
    toyId: Long,
    navController: NavHostController,
    viewModel: ToyDetailScreenViewModel = hiltViewModel(),
) {
    // Compositionされたタイミングで実行する
    LaunchedEffect(Unit) {
        viewModel.fetchSingleToy(toyId)
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
                viewModel.deleteToy()
                viewModel.showSuccessAlert()
            },
            cancelAction = {
                viewModel.closeConfirmAlert()
            },
            message = "このデータを削除しますか？\n削除すると登録した画像なども削除されます。",
        )

        HeaderView(
            title = viewModel.toy?.name ?: "アイテム名",
            leftOnClick = { navController.popBackStack() },
            leftImageVector = Icons.AutoMirrored.Filled.ArrowBack,
            leftContentDescription = "画面を戻る",
            rightOnClick =
            {
                navController.navigate(AppScreen.ToyInput.route(seriesId, toyId))
            },
            rightImageVector = Icons.Filled.Edit,
            rightContentDescription = "シリーズ更新画面遷移",
        )

        // 画像表示とフラグ表示セクション
        ImageAndFragSection(
            viewModel.fetchImage(viewModel.toy?.imagePath),
            viewModel,
        )

        CustomText(
            text = "アイテム名",
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
            CustomText(viewModel.toy?.name.orEmpty())
        }

        Spacer(
            modifier = Modifier
                .padding(vertical = 8.dp),
        )

        // MEMOセクション
        if (!viewModel.toy?.memo.isNullOrEmpty()) {
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
                CustomText(viewModel.toy?.memo!!)
            }
        }

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
    }
}

/**
 * 画像表示と金額種類数表示セクション
 */
@Composable
private fun ImageAndFragSection(
    bitmap: Bitmap?,
    viewModel: ToyDetailScreenViewModel,
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

        viewModel.toy?.let {
            GetAndSecretSection(
                toy = it,
                viewModel = viewModel,
                modifier = Modifier
                    .weight(1f),
            )
        }
    }
}

@Composable
private fun GetAndSecretSection(
    toy: CapsuleToy,
    viewModel: ToyDetailScreenViewModel,
    modifier: Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        CustomText(
            text = "GET",
            textSize = TextSize.S,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(vertical = 8.dp),
        )

        ThemeIconButton(
            onClick = {
            },
            imageVector = Icons.Filled.Check,
            contentDescription = "所持済みフラグ",
            containerColor = if (toy.isOwned) MaterialTheme.colorScheme.primary else Color.Gray,
            baseSize = 40.dp,
        )

        if (toy.isOwned) {
            toy.isGetAt?.let { date ->
                CustomText(
                    text = "GET DATE",
                    textSize = TextSize.S,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(vertical = 8.dp),
                )

                CustomText(viewModel.convertDate(date.time).toString())
            }
        }

        Spacer(
            modifier = Modifier
                .padding(vertical = 8.dp),
        )

        CustomText(
            text = "SECRET",
            textSize = TextSize.S,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(vertical = 8.dp),
        )

        ThemeIconButton(
            onClick = { },
            imageVector = Icons.Filled.Check,
            contentDescription = "シークレットフラグ",
            containerColor = if (toy.isSecret) MaterialTheme.colorScheme.primary else Color.Gray,
            baseSize = 40.dp,
        )
    }
}
