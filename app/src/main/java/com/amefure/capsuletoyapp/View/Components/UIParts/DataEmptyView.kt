package com.amefure.capsuletoyapp.View.Components.UIParts

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.amefure.capsuletoyapp.R

@Composable
fun DataEmptyView() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth(),
    ) {

        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(id = R.drawable.appicon_notext),
            contentDescription = "アプリアイコン",
            modifier = Modifier.size(180.dp),
            contentScale = ContentScale.Fit
        )

        Text(
            text = "データが存在しません",
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}