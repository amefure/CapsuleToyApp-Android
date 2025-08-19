package com.amefure.capsuletoyapp.View.Series

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.amefure.capsuletoyapp.ViewModel.SeriesViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun SeriesInputScreen(
    itemId: Int,
    navController: NavHostController,
    viewModel: SeriesViewModel = hiltViewModel(),
) {
    var name by rememberSaveable { mutableStateOf("") }
    var count by rememberSaveable { mutableStateOf(0) }
    var amount by rememberSaveable { mutableStateOf(0) }
    var memo by rememberSaveable { mutableStateOf("") }

    Column {
        Row {
            Button(
                onClick = {
                    viewModel.addSeries(
                        name = name,
                        count = count,
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

        TextField(
            value = name,
            onValueChange = {
                name = it
            },
            modifier = Modifier.fillMaxWidth()
        )


        TextField(
            value = count.toString(),
            onValueChange = {
                count = it.toIntOrNull() ?: 0
            },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = amount.toString(),
            onValueChange = {
                amount = it.toIntOrNull() ?: 0
            },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = memo,
            onValueChange = {
                memo = it
            },
            modifier = Modifier.fillMaxWidth()
        )

    }
}
