package com.amefure.capsuletoyapp.views.components.uiParts

import android.annotation.SuppressLint
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * テーマアイコンボタン(丸)
 */
@Composable
fun ThemeIconButton(
    onClick: () -> Unit,
    imageVector: ImageVector,
    contentDescription: String?,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    baseSize: Dp = 50.dp,
    iconSize: Dp = 24.dp,
    @SuppressLint("ModifierParameter")
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(
            // 背景色
            containerColor = containerColor,
            // テキスト/Iconの色
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        modifier = modifier
            .size(baseSize)
            // 影を2重でかけて濃くする
            .shadow(8.dp, CircleShape, clip = false)
            .shadow(8.dp, CircleShape, clip = false)
            .clip(CircleShape),
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            modifier = Modifier
                .size(iconSize),
        )
    }
}
