package com.amefure.capsuletoyapp.View.Extension

import androidx.compose.material3.AlertDialog
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
    closeAction: () -> Unit,
    message: @Composable () -> Unit
) {
    if (showFlag) {
        AlertDialog(
            onDismissRequest = closeAction,
            confirmButton = {
                TextButton(
                    onClick = closeAction
                ) {
                    CustomText("OK")
                }
            },
            dismissButton = {
                if (type == AlertType.CONFIRM) CustomText("キャンセル")
            },
            title = { CustomText(type.title()) },
            text = message
        )
    }
}