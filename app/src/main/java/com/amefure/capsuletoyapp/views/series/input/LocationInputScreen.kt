package com.amefure.capsuletoyapp.views.series.input

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.amefure.capsuletoyapp.models.domain.entity.Location
import com.amefure.capsuletoyapp.viewModels.LocationInputScreenViewModel
import com.amefure.capsuletoyapp.views.components.layout.HeaderView
import com.amefure.capsuletoyapp.views.components.uiParts.AlertType
import com.amefure.capsuletoyapp.views.components.uiParts.CustomAlertDialog
import com.amefure.capsuletoyapp.views.components.uiParts.ThemeInputBox
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun LocationInputScreen(
    seriesId: Long,
    navController: NavController,
    viewModel: LocationInputScreenViewModel = hiltViewModel(),
) {
    CustomAlertDialog(
        showFlag = viewModel.showValidationDialog,
        type = AlertType.FAILED,
        rightAction = { viewModel.closeValidationAlert() },
        message = "場所名は必須入力です。",
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background),
    ) {
        HeaderView(
            title = "ガチャガチャ設置場所登録",
            leftOnClick = { navController.popBackStack() },
            leftImageVector = Icons.AutoMirrored.Filled.ArrowBack,
            leftContentDescription = "画面を戻る",
            rightOnClick =
            {
                val location = viewModel.createLocation(
                    seriesId = seriesId,
                ) ?: return@HeaderView
                // 入力した値を戻り先にセット
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set(Location.KEY, location)

                navController.popBackStack()
            },
            rightImageVector = Icons.Filled.Check,
            rightContentDescription = "カテゴリ登録",
        )

        Spacer(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        ThemeInputBox(
            title = "場所名",
            value = viewModel.locationName,
            placeholder = "例：イオンモール〇〇店 3F",
            onValueChange = {
                viewModel.locationName = it
            },
            singleLine = false,
        )

        Spacer(modifier = Modifier.height(16.dp))

        MapScreen(viewModel = viewModel)
    }
}

/** Google Map 機能 */
@Composable
private fun MapScreen(
    viewModel: LocationInputScreenViewModel,
) {
// 地図の初期位置
    val cameraPositionState = rememberCameraPositionState {
        // zoom=15fで拡大して表示
        position = CameraPosition.fromLatLngZoom(viewModel.initialLatLng, 15f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(
            // 右下に表示される「＋ / －」のズームボタンを有効化するか
            zoomControlsEnabled = false,
            // 現在値ボタンを表示するかどうか
            myLocationButtonEnabled = true,
        ),
        properties = MapProperties(
            mapType = MapType.NORMAL,
            // 現在値アイコン(青い丸)を表示するか
            isMyLocationEnabled = true,
        ),
        onMapClick = { latLng ->
            viewModel.onMapClick(latLng)
        },
        onMapLoaded = {
            // Mapを読み込み終えてから現在値へ移動させる
            // 読み込み前に移動させようとするとNullPointerExceptionになる
            cameraPositionState.move(
                CameraUpdateFactory.newLatLngZoom(viewModel.initialLatLng, 15f)
            )
        }
    ) {
        viewModel.selectedLatLng?.let { latLng ->
            Marker(
                state = MarkerState(position = latLng),
                title = "選択した場所",
                snippet = "緯度: ${latLng.latitude}, 経度: ${latLng.longitude}",
            )
        }
    }
}
