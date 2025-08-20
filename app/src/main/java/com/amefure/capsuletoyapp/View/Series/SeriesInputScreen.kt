package com.amefure.capsuletoyapp.View.Series

import android.widget.ImageButton
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.amefure.capsuletoyapp.ViewModel.SeriesViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.amefure.capsuletoyapp.View.Components.HeaderView
import com.amefure.capsuletoyapp.View.Components.ThemaIconButton
import com.amefure.capsuletoyapp.ui.theme.ExGold
import com.amefure.capsuletoyapp.ui.theme.ExRed

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
        HeaderView(
            title = "シリーズ登録",
            leftOnClick = { navController.popBackStack() },
            leftImageVector = Icons.AutoMirrored.Filled.ArrowBack,
            leftContentDescription = "画面を戻る",
            rightOnClick =
                {
                    viewModel.addSeries(
                        name = name,
                        count = count ?: 0,
                        amount = 100,
                        memo = "メモ",
                        capsuleToys = emptyList(),
                        locations = emptyList(),
                        categories = emptyList(),
                    )
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
                OutlinedTextField(
                    value = amount?.toString() ?: "",
                    onValueChange = {
                        amount = if (it.isEmpty()) null else it.toIntOrNull() ?: 0
                    },
                    label = { Text("金額") },
                    placeholder = { Text("例：300円") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    singleLine = true,
                )

                OutlinedTextField(
                    value = count?.toString() ?: "",
                    onValueChange = {
                        count = if (it.isEmpty()) null else it.toIntOrNull() ?: 0
                    },
                    label = { Text("種類") },
                    placeholder = { Text("例：6種類") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    singleLine = true,
                )
            }
        }

        TextField(
            value = name,
            onValueChange = {
                name = it
            },
            label = { Text("TextField") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            Modifier.fillMaxWidth()
                .padding(16.dp)
        )

        OutlinedTextField(
            value = memo,
            onValueChange = {
                memo = it
            },
            label = { Text("OutlinedTextField") },
            modifier = Modifier.fillMaxWidth()
        )

        Text("※ ガチャガチャのアイテムはシリーズを登録した後に、一覧からそのシリーズをタップすることで追加できます。")

    }
}
