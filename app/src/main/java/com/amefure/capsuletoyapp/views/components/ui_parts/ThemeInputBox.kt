package com.amefure.capsuletoyapp.views.components.ui_parts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

/**　サブラベル付き入力ボックス */
@Composable
fun ThemeInputBox(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    isNumberOnly: Boolean = false,
    singleLine: Boolean = true,
) {
    Column {
        CustomText(
            text = title,
            textSize = TextSize.S,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(8.dp))

        ThemeTextFiled(
            value = value,
            onValueChange = onValueChange,
            placeholder = placeholder,
            keyboardOptions = if (isNumberOnly) {
                KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                )
            } else {
                KeyboardOptions.Default
            },
            singleLine = singleLine,
        )
    }
}