package com.amefure.capsuletoyapp.View.Components.UIParts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 背景が白色角丸影ありのテキストView
 */
@Composable
fun ThemaText(
    text: String,
    textSize: TextSize = TextSize.M,
    color: Color = MaterialTheme.colorScheme.onBackground,
    fontWeight: FontWeight = FontWeight.Normal,
    maxLines: Int = 1
) {
    val fontSize = when (textSize) {
        TextSize.SS -> 12.sp
        TextSize.S  -> 14.sp
        TextSize.MS -> 15.sp
        TextSize.M  -> 17.sp
        TextSize.ML -> 18.sp
        TextSize.L  -> 20.sp
    }

    Text(
        text = text,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        softWrap = true,
        style = TextStyle(
            fontSize = fontSize,
            color = color,
            fontWeight = fontWeight
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(8.dp),
                clip = false
            )
            .background(Color.White, RoundedCornerShape(8.dp)),
    )
}


