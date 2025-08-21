package com.amefure.capsuletoyapp.View.Components.UIParts

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
import androidx.compose.ui.unit.dp


/**
 * テーマアイコンボタン(丸)
 */
@Composable
fun ThemaIconButton(
    onClick: () -> Unit,
    imageVector: ImageVector,
    contentDescription: String?,
) {
    IconButton (
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,   // 背景色
            contentColor = MaterialTheme.colorScheme.onPrimary    // テキスト/Iconの色
        ),
        modifier = Modifier
            .size(50.dp)
            // 影を2重でかけて濃くする
            .shadow(8.dp, CircleShape, clip = false)
            .shadow(8.dp, CircleShape, clip = false)
            .clip(CircleShape)
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
        )
    }
}