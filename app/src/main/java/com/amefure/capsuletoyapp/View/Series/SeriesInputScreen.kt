package com.amefure.capsuletoyapp.View.Series

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.amefure.capsuletoyapp.ViewModel.SeriesViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType

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

    Column {
        Row {
            Button(
                onClick = {
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
                }
            ) {
                Text("Add")
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                OutlinedTextField(
                    value = if (amount == null) "" else amount.toString(),
                    onValueChange = {
                        if (it.isEmpty()) {
                            amount = null
                        } else {
                            amount = it.toIntOrNull() ?: 0
                        }
                    },
                    label = { Text("金額") },
                    placeholder = { Text("例：300円") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    singleLine = true,
                )

                OutlinedTextField(
                    value = if (count == null) "" else count.toString(),
                    onValueChange = {
                        if (it.isEmpty()) {
                            count = null
                        } else {
                            count = it.toIntOrNull() ?: 0
                        }
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

        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
            },
            label = { Text("シリーズ名") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = memo,
            onValueChange = {
                memo = it
            },
            modifier = Modifier.fillMaxWidth()
        )

        Text("※ ガチャガチャのアイテムはシリーズを登録した後に、一覧からそのシリーズをタップすることで追加できます。")

    }
}
