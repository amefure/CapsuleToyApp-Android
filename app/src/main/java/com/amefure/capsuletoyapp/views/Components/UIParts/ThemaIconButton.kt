package com.amefure.capsuletoyapp.views.Components.UIParts

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


/**
 * テーマアイコンボタン(丸)
 */
@Composable
fun ThemaIconButton(
    onClick: () -> Unit,
    imageVector: ImageVector,
    contentDescription: String?,
    baseSize: Dp = 50.dp,
    iconSize: Dp = 24.dp
) {
    IconButton (
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(
            // 背景色
            containerColor = MaterialTheme.colorScheme.primary,
            // テキスト/Iconの色
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = Modifier
            .size(baseSize)
            // 影を2重でかけて濃くする
            .shadow(8.dp, CircleShape, clip = false)
            .shadow(8.dp, CircleShape, clip = false)
            .clip(CircleShape)
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            modifier = Modifier
                .size(iconSize)
        )
    }
}