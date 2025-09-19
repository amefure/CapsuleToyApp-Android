package com.amefure.capsuletoyapp.views.series.input

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import java.time.Instant
import java.time.ZoneId

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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
        ) {
            // 画像追加ボタン
            AddImageButton(viewModel)

            Spacer(
                modifier = Modifier
                    .padding(horizontal = 8.dp),
            )

            // Get & Secretフラグ
            GetAndSecretSection(
                viewModel = viewModel,
                modifier = Modifier
                    .weight(1f),
            )
        }

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
private fun GetAndSecretSection(
    viewModel: ToyInputScreenViewModel,
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
                viewModel.isOwned = !viewModel.isOwned
            },
            imageVector = Icons.Filled.Check,
            contentDescription = "",
            containerColor = if (viewModel.isOwned) MaterialTheme.colorScheme.primary else Color.Gray,
        )

        if (viewModel.isOwned) {
            MaterialDatePickerSample()
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
            onClick = {
                viewModel.isSecret = !viewModel.isSecret
            },
            imageVector = Icons.Filled.Check,
            contentDescription = "",
            containerColor = if (viewModel.isSecret) MaterialTheme.colorScheme.primary else Color.Gray,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaterialDatePickerSample() {
    val openDialog = remember { mutableStateOf(false) }
    val selectedDate = remember { mutableStateOf<Long?>(null) }
    val datePickerState = rememberDatePickerState()

    Column(
        modifier = Modifier
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(
            onClick = {
                openDialog.value = true
            },
        ) {
            val text: String = if (selectedDate.value == null) {
                "日付を選択"
            } else {
                Instant.ofEpochMilli(selectedDate.value!!)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                    .toString()
            }

            CustomText(text)
        }
    }

    if (openDialog.value) {
        DatePickerDialog(
            onDismissRequest = { openDialog.value = false },
            confirmButton = {
                Button(
                    onClick = {
                        selectedDate.value = datePickerState.selectedDateMillis
                        openDialog.value = false
                    },
                ) {
                    CustomText("OK")
                }
            },
            dismissButton = {
                Button(onClick = { openDialog.value = false }) {
                    CustomText("キャンセル")
                }
            },
        ) {
            DatePicker(
                state = datePickerState,
                showModeToggle = true,
            )
        }
    }
}
