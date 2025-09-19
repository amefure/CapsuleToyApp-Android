package com.amefure.capsuletoyapp.views.series.input

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.amefure.capsuletoyapp.viewModels.ToyInputScreenViewModel
import com.amefure.capsuletoyapp.views.components.layout.HeaderView
import com.amefure.capsuletoyapp.views.components.uiParts.AddImageButton
import com.amefure.capsuletoyapp.views.components.uiParts.CustomAlertDialog
import com.amefure.capsuletoyapp.views.components.uiParts.CustomText
import com.amefure.capsuletoyapp.views.components.uiParts.TextSize
import com.amefure.capsuletoyapp.views.components.uiParts.ThemeIconButton
import com.amefure.capsuletoyapp.views.components.uiParts.ThemeInputBox

@Composable
fun ToyInputScreen(
    seriesId: Long,
    navController: NavHostController,
    viewModel: ToyInputScreenViewModel = hiltViewModel(),
) {
    // Compositionされたタイミングで実行する
    // 1回だけ発火して欲しいのでKeyは不変とする
    LaunchedEffect(Unit) {
        viewModel.preparePhotoUri()
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background),
    ) {
        CustomAlertDialog(
            showFlag = viewModel.isShowValidationDialog,
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

        HeaderView(
            title = "ガチャガチャアイテム登録",
            leftOnClick = { navController.popBackStack() },
            leftImageVector = Icons.AutoMirrored.Filled.ArrowBack,
            leftContentDescription = "画面を戻る",
            rightOnClick =
            {
                viewModel.createCapsuleToy(
                    seriesId = seriesId,
                )
            },
            rightImageVector = Icons.Filled.Check,
            rightContentDescription = "ガチャガチャ登録",
        )

        InputImageAndAmountSection(viewModel)

        ThemeInputBox(
            title = "アイテム名",
            value = viewModel.name,
            onValueChange = {
                viewModel.name = it
            },
            placeholder = "例：△△シリーズ",
        )

        Spacer(
            modifier = Modifier
                .padding(vertical = 8.dp),
        )

        ThemeInputBox(
            title = "MEMO",
            value = viewModel.memo,
            onValueChange = {
                viewModel.memo = it
            },
            singleLine = false,
        )
    }
}

@Composable
private fun InputImageAndAmountSection(
    viewModel: ToyInputScreenViewModel,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
    ) {
        AddImageButton(viewModel)

        Spacer(
            modifier = Modifier
                .padding(horizontal = 8.dp),
        )

        GetAndSecretSection(
            modifier = Modifier
                .weight(1f),
        )
    }
}

@Composable
private fun GetAndSecretSection(modifier: Modifier) {
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
            contentDescription = "",
        )

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
            onClick = {
            },
            imageVector = Icons.Filled.Check,
            contentDescription = "",
        )
    }
}
