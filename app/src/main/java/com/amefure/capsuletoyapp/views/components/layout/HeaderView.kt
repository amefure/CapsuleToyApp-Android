package com.amefure.capsuletoyapp.views.components.layout

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.amefure.capsuletoyapp.views.components.ui_parts.CustomText
import com.amefure.capsuletoyapp.views.components.ui_parts.ThemeIconButton


@Composable
fun HeaderView(
    title: String? = null,
    leftOnClick: () -> Unit = {},
    leftImageVector: ImageVector? = Icons.AutoMirrored.Filled.ArrowBack,
    leftContentDescription: String? = "画面を戻る",
    rightOnClick: () -> Unit = {},
    rightImageVector: ImageVector? = null,
    rightContentDescription: String? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (leftImageVector != null) {
            ThemeIconButton(
                onClick = leftOnClick,
                imageVector = leftImageVector,
                contentDescription = leftContentDescription,
            )
        } else {
            Spacer(
                modifier = Modifier
                    .size(50.dp)
            )
        }

        Spacer(
            modifier = Modifier
                .weight(1f)
        )

        if (title != null) {
            CustomText(
                text = title,
                fontWeight = FontWeight.Bold,
            )
        }

        Spacer(
            modifier = Modifier
                .weight(1f)
        )
        if (rightImageVector != null) {
            ThemeIconButton(
                onClick = rightOnClick,
                imageVector = rightImageVector,
                contentDescription = rightContentDescription,
            )
        } else {
            Spacer(
                modifier = Modifier
                    .size(50.dp)
            )
        }
    }
}