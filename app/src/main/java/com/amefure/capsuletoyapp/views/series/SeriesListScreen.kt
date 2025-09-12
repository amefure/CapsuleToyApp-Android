package com.amefure.capsuletoyapp.views.series

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.amefure.capsuletoyapp.R
import com.amefure.capsuletoyapp.models.enum.AppScreen
import com.amefure.capsuletoyapp.ui.theme.ExWhite
import com.amefure.capsuletoyapp.viewModels.SeriesListScreenViewModel
import com.amefure.capsuletoyapp.views.components.layout.HeaderView
import com.amefure.capsuletoyapp.views.components.uiParts.CustomText
import com.amefure.capsuletoyapp.views.components.uiParts.DataEmptyView
import com.amefure.capsuletoyapp.views.components.uiParts.TextSize

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
            .padding(16.dp),
    ) {
        HeaderView(
            title = "ガチャガチャシリーズ一覧",
            leftImageVector = null,
            rightOnClick = {
                navController.navigate(AppScreen.SeriesInput.inputRoute())
            },
            rightImageVector = Icons.Filled.Add,
            rightContentDescription = "新規シリーズ画面へ遷移",
        )

        if (viewModel.series.isEmpty()) {
            DataEmptyView()
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
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
                                shape = RoundedCornerShape(12.dp),
                            )
                            .padding(8.dp),
                    ) {
                        series.series.imagePath?.let {
                            val bitmap = viewModel.fetchImage(it)?.asImageBitmap()
                            bitmap?.let {
                                Image(
                                    bitmap = it,
                                    contentDescription = "撮影した写真",
                                    modifier = Modifier
                                        .size(80.dp)
                                        .aspectRatio(1f),
                                    contentScale = ContentScale.Crop,
                                )
                            }
                        } ?: run {
                            Image(
                                painter = painterResource(id = R.drawable.no_image),
                                contentDescription = "サンプル画像",
                                modifier = Modifier.size(80.dp),
                                contentScale = ContentScale.Fit,
                            )
                        }

                        Column {
                            CustomText(
                                text = series.series.name,
                                fontWeight = FontWeight.Bold,
                            )

                            LazyRow {
                                items(items = series.categories) { category ->
                                    CustomText(
                                        text = category.name,
                                        textSize = TextSize.MS,
                                        color = ExWhite,
                                        modifier = Modifier
                                            .padding(horizontal = 4.dp)
                                            .height(30.dp)
                                            .shadow(
                                                elevation = 8.dp,
                                                shape = RoundedCornerShape(8.dp),
                                                clip = false,
                                            ).background(category.color, RoundedCornerShape(8.dp))
                                            .padding(5.dp),
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
