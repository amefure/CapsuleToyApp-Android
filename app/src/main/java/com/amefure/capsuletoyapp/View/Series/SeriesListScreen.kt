package com.amefure.capsuletoyapp.View.Series

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.amefure.capsuletoyapp.Models.Domain.Entity.Series
import com.amefure.capsuletoyapp.Models.Enum.AppScreen
import com.amefure.capsuletoyapp.ViewModel.SeriesViewModel

@Composable
fun SeriesListScreen(
    navController: NavHostController,
    viewModel: SeriesViewModel = hiltViewModel(),
) {

    SideEffect {
        viewModel.fetchAllSeries()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Home Screen" + viewModel.series.value?.size, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        (1..5).forEach { id ->
            Text(
                text = "Item $id",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        viewModel.addSeries(
                            name = (id + 10).toString(),
                            count = 5,
                            amount = 100,
                            memo = "メモ",
                            capsuleToys = emptyList(),
                            locations = emptyList(),
                            categories = emptyList(),
                        )
                        // navController.navigate(AppScreen.SeriesDetail.route(id))
                    }
                    .padding(8.dp)
            )
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