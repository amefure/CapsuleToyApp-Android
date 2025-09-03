package com.amefure.capsuletoyapp.views.MyData

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.amefure.capsuletoyapp.views.Components.Layout.HeaderView
import com.amefure.capsuletoyapp.views.Components.UIParts.DataEmptyView

@Composable
fun MyDataScreen(
    navController: NavHostController,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        HeaderView(
            title = "MyData",
            leftImageVector = null,
            rightImageVector = null,
        )

        DataEmptyView()
    }
}