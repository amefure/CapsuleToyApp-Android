package com.amefure.capsuletoyapp.View.Series

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.amefure.capsuletoyapp.Models.Enum.AppScreen
import com.amefure.capsuletoyapp.View.Components.Layout.HeaderView
import com.amefure.capsuletoyapp.View.Components.UIParts.CustomText
import com.amefure.capsuletoyapp.View.Components.UIParts.TextSize
import com.amefure.capsuletoyapp.View.Components.UIParts.ThemaText
import com.amefure.capsuletoyapp.ui.theme.ExGold

@Composable
fun SeriesDetailScreen(
    itemId: Int,
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        HeaderView(
            title = if (itemId == 0) "シリーズ登録" else "シリーズ更新",
            leftOnClick = { navController.popBackStack() },
            leftImageVector = Icons.AutoMirrored.Filled.ArrowBack,
            leftContentDescription = "画面を戻る",
            rightOnClick =
                {
                   navController.navigate(AppScreen.SeriesInput.updateRoute(itemId))
                },
            rightImageVector = Icons.Filled.Edit,
            rightContentDescription = "シリーズ更新画面",
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {

            OutlinedButton (
                onClick = { /* TODO */ },
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(2.dp, ExGold),
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .aspectRatio(1f),
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "画像追加",
                    tint = ExGold,
                )
            }

            Spacer(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
            ) {

                CustomText(
                    text = "金額",
                    textSize = TextSize.S,
                    fontWeight = FontWeight.Bold,
                )

                Spacer(
                    modifier = Modifier
                        .height(5.dp)
                )

                ThemaText(
                    text = "金額",
                    textSize = TextSize.S,
                    fontWeight = FontWeight.Bold,
                )

                Spacer(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                )

                CustomText(
                    text = "種類数",
                    textSize = TextSize.S,
                    fontWeight = FontWeight.Bold,
                )

                Spacer(
                    modifier = Modifier
                        .height(5.dp)
                )

                ThemaText(
                    text = "種類数",
                    textSize = TextSize.S,
                    fontWeight = FontWeight.Bold,
                )
            }
        }

        CustomText(
            text = "シリーズ名",
            textSize = TextSize.S,
            fontWeight = FontWeight.Bold,
        )
        Spacer(
            modifier = Modifier
                .padding(vertical = 8.dp)
        )
        ThemaText(
            text = "シリーズ名",
            textSize = TextSize.S,
            fontWeight = FontWeight.Bold,
        )
    }
}

