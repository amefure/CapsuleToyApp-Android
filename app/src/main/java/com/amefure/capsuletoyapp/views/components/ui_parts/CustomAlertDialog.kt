package com.amefure.capsuletoyapp.views.components.ui_parts

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

/** アラートタイプ */
enum class AlertType {
    /** お知らせ */
    NOTIFY {
        override fun title(): String = "お知らせ"
    },
    /** 確認 */
    CONFIRM {
        override fun title(): String = "確認"
    },
    /** 成功 */
    SUCCESS {
        override fun title(): String = "成功"
    },
    /** 失敗 */
    FAILED {
        override fun title(): String = "Error"
    };
    abstract fun title(): String
}

@Composable
fun CustomAlertDialog(
    showFlag: Boolean,
    type: AlertType = AlertType.SUCCESS,
    rightTitle: String = "OK",
    rightAction: () -> Unit,
    cancelAction: (() -> Unit)? = null,
    message: String
) {
    if (showFlag) {
        AlertDialog(
            onDismissRequest = cancelAction ?: rightAction,
            confirmButton = {
                TextButton(
                    onClick = rightAction
                ) {
                    if (type == AlertType.CONFIRM) {
                        CustomText(
                            rightTitle,
                            color = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        CustomText(rightTitle)
                    }
                }
            },
            dismissButton = {
                if (type == AlertType.CONFIRM) {
                    TextButton(
                        onClick = cancelAction ?: rightAction,
                    ) {
                        CustomText("キャンセル")
                    }
                }
            },
            title = { CustomText(type.title()) },
            text = {
                CustomText(
                    text = message,
                    maxLines = 4
                )
            }
        )
    }
}