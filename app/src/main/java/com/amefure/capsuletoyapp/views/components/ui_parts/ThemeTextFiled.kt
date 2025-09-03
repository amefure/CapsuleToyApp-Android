package com.amefure.capsuletoyapp.views.components.ui_parts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.amefure.capsuletoyapp.ui.theme.ExText

/**
 * 背景が白色角丸影ありのテキスト入力ボックス
 */
@Composable
fun ThemeTextFiled(
    value: String,
    onValueChange: (String) -> Unit,
    label: String? = null,
    placeholder: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = false,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = if (label != null) {
            @Composable
            {
                CustomText(
                    label,
                    fontWeight = FontWeight.Bold,
                    color = ExText.copy(alpha = 0.5f)
                )
            }
        } else null,
        placeholder = if (placeholder != null) {
            @Composable
            {
                CustomText(
                    placeholder,
                    fontWeight = FontWeight.Bold,
                    color = ExText.copy(alpha = 0.5f)
                )
            }
        } else null,
        keyboardOptions = keyboardOptions,
        singleLine = singleLine,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),
        shape = RoundedCornerShape(8.dp),
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