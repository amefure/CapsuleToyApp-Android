package com.amefure.capsuletoyapp.views.mydata

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MyDataScreen(
    navController: NavHostController,
) {

//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
    MapScreen()
//        HeaderView(
//            title = "MyData",
//            leftImageVector = null,
//            rightImageVector = null,
//        )

        //DataEmptyView()
//    }
}
@Composable
fun MapScreen() {

    val skyTree = LatLng(35.710063, 139.8107)

    // 地図の初期位置
    val cameraPositionState = rememberCameraPositionState {
        // zoom=15fで拡大して表示
        position = CameraPosition.fromLatLngZoom(skyTree, 15f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(zoomControlsEnabled = false),
        properties = MapProperties(mapType = MapType.NORMAL)
    ) {
        // マーカーを追加
        Marker(
            state = MarkerState(position = skyTree),
            title = "東京スカイツリー",
            snippet = "東京都墨田区押上一丁目1番2号"
        )
    }
}
