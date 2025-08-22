package com.amefure.capsuletoyapp.View.Series


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.amefure.capsuletoyapp.Models.Enum.AppScreen
import com.amefure.capsuletoyapp.View.Components.Layout.HeaderView
import com.amefure.capsuletoyapp.ViewModel.SeriesListScreenViewModel
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.amefure.capsuletoyapp.R
import com.amefure.capsuletoyapp.View.Components.UIParts.CustomText
import com.amefure.capsuletoyapp.View.Components.UIParts.DataEmptyView

@Composable
fun SeriesListScreen(
    navController: NavHostController,
    viewModel: SeriesListScreenViewModel = hiltViewModel(),
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

        if (viewModel.series.isEmpty()) {
            DataEmptyView()
        } else {
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
                                navController.navigate(AppScreen.SeriesDetail.route(series.series.id))
                            }
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(8.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.no_image),
                            contentDescription = "サンプル画像",
                            modifier = Modifier.size(80.dp),
                            contentScale = ContentScale.Fit
                        )
                        CustomText(
                            text = series.series.name,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

    }
}