package com.amefure.capsuletoyapp.View.Series

import android.graphics.drawable.Icon
import android.widget.ScrollView
import android.widget.Scroller
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.amefure.capsuletoyapp.Models.Enum.AppScreen
import com.amefure.capsuletoyapp.View.Components.HeaderView
import com.amefure.capsuletoyapp.ViewModel.SeriesViewModel

@Composable
fun SeriesListScreen(
    navController: NavHostController,
    viewModel: SeriesViewModel = hiltViewModel(),
) {
    // Compositionされたタイミングで実行する
    LaunchedEffect(Unit) {
        viewModel.fetchAllSeries()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        HeaderView(
            title = "ガチャガチャシリーズ一覧",
            leftImageVector = null,
            rightOnClick = {
                navController.navigate(AppScreen.SeriesInput.inputRoute())
            },
            rightImageVector = Icons.Filled.Add,
            rightContentDescription = "新規シリーズ画面へ遷移"
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            items(items = viewModel.series) { series ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(AppScreen.SeriesInput.updateRoute(series.series.id.toInt()))
                        }
                        .padding(8.dp)
                ) {
                    Text(
                        text = series.series.name,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.navigate(AppScreen.Tab.Settings.route())
            }
        ) {
            Text("Go to Settings")
        }
    }
}