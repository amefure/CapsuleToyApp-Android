package com.amefure.capsuletoyapp.View.Series

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.amefure.capsuletoyapp.View.Components.Layout.HeaderView
import com.amefure.capsuletoyapp.View.Extension.CustomText
import com.amefure.capsuletoyapp.View.Extension.TextSize
import com.amefure.capsuletoyapp.View.Components.UIParts.ThemaTextFiled
import com.amefure.capsuletoyapp.View.Extension.AlertType
import com.amefure.capsuletoyapp.View.Extension.CustomAlertDialog
import com.amefure.capsuletoyapp.ViewModel.SeriesInputScreenViewModel
import com.amefure.capsuletoyapp.ui.theme.ExGold

@Composable
fun SeriesInputScreen(
    seriesId: Long,
    navController: NavHostController,
    viewModel: SeriesInputScreenViewModel = hiltViewModel(),
) {

    // Compositionされたタイミングで実行する
    LaunchedEffect(Unit) {
        viewModel.fetchSingleSeries(seriesId)
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
                        seriesId = seriesId,
                        capsuleToys = emptyList(),
                        locations = emptyList(),
                        categories = emptyList(),
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

            OutlinedButton (
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
            Modifier.fillMaxWidth()
                .padding(16.dp)
        )

        CustomText(
            text = "カテゴリ",
            textSize = TextSize.S,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton (
            onClick = { /* TODO */ },
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(2.dp, ExGold),
            modifier = Modifier
                .width(70.dp)
                .height(40.dp),
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "カテゴリ追加",
                tint = ExGold,
            )
        }

        Spacer(
            Modifier.fillMaxWidth()
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
            Modifier.fillMaxWidth()
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
private fun ThemeInputBox(
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
