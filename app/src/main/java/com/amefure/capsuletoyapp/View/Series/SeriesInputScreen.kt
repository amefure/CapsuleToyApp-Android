package com.amefure.capsuletoyapp.View.Series

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.amefure.capsuletoyapp.ViewModel.SeriesViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.amefure.capsuletoyapp.View.Components.Layout.HeaderView
import com.amefure.capsuletoyapp.View.Components.UIParts.CustomText
import com.amefure.capsuletoyapp.View.Components.UIParts.TextSize
import com.amefure.capsuletoyapp.View.Components.UIParts.ThemaTextFiled
import com.amefure.capsuletoyapp.ui.theme.ExGold

@Composable
fun SeriesInputScreen(
    itemId: Int,
    navController: NavHostController,
    viewModel: SeriesViewModel = hiltViewModel(),
) {
    var name by rememberSaveable { mutableStateOf("") }
    var count: Int? by rememberSaveable { mutableStateOf(null) }
    var amount: Int? by rememberSaveable { mutableStateOf(null) }
    var memo by rememberSaveable { mutableStateOf("") }

    Column(
        Modifier
            .padding(20.dp)
    ) {
        var showDialog by remember { mutableStateOf(false) }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("OK")
                    }
                },
                title = { Text("通知") },
                text = { Text("保存が完了しました。") }
            )
        }

        HeaderView(
            title = if (itemId == 0) "シリーズ登録" else "シリーズ更新",
            leftOnClick = { navController.popBackStack() },
            leftImageVector = Icons.AutoMirrored.Filled.ArrowBack,
            leftContentDescription = "画面を戻る",
            rightOnClick =
                {
                    viewModel.addSeries(
                        name = name,
                        count = count ?: 0,
                        amount = amount,
                        memo = memo,
                        capsuleToys = emptyList(),
                        locations = emptyList(),
                        categories = emptyList(),
                    )
                    showDialog = true
                    navController.popBackStack()
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

                ThemaTextFiled(
                    value = amount?.toString() ?: "",
                    onValueChange = {
                        amount = if (it.isEmpty()) null else it.toIntOrNull() ?: 0
                    },
                    placeholder = "例：300円",
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    singleLine = true,
                )

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

                ThemaTextFiled(
                    value = count?.toString() ?: "",
                    onValueChange = {
                        count = if (it.isEmpty()) null else it.toIntOrNull() ?: 0
                    },
                    placeholder = "例：6種類",
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    singleLine = true,
                )
            }
        }

        CustomText(
            text = "シリーズ名",
            textSize = TextSize.S,
            fontWeight = FontWeight.Bold,
        )
        Spacer(
            modifier = Modifier
                .padding(vertical = 8.dp)
        )

        ThemaTextFiled(
            value = name,
            onValueChange = {
                name = it
            },
            placeholder = "例：△△シリーズ",
            singleLine = true,
        )

        Spacer(
            Modifier.fillMaxWidth()
                .padding(16.dp)
        )

        CustomText(
            text = "MEMO",
            textSize = TextSize.S,
            fontWeight = FontWeight.Bold,
        )
        Spacer(
            modifier = Modifier
                .padding(vertical = 8.dp)
        )

        ThemaTextFiled(
            value = memo,
            onValueChange = {
                memo = it
            },
        )

        Spacer(
            Modifier.fillMaxWidth()
                .padding(16.dp)
        )

        CustomText(
            text = "※ ガチャガチャのアイテムはシリーズを登録した後に、一覧からそのシリーズをタップすることで追加できます。"
        )
    }
}
