package com.amefure.capsuletoyapp.views.series.input

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.amefure.capsuletoyapp.views.components.layout.HeaderView
import com.amefure.capsuletoyapp.views.components.ui_parts.CustomText
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.amefure.capsuletoyapp.models.Domain.Entity.Category
import com.amefure.capsuletoyapp.views.components.ui_parts.AlertType
import com.amefure.capsuletoyapp.views.components.ui_parts.CustomAlertDialog
import com.amefure.capsuletoyapp.views.components.ui_parts.TextSize
import com.amefure.capsuletoyapp.view_models.CategoryInputScreenViewModel
import com.amefure.capsuletoyapp.ui.theme.ExGold
import com.amefure.capsuletoyapp.ui.theme.ExWhite
import com.amefure.capsuletoyapp.views.components.ui_parts.ThemeInputBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryInputScreen(
    navController: NavHostController,
    viewModel: CategoryInputScreenViewModel = hiltViewModel(),
) {
    var selectedColor by remember { mutableStateOf(ExGold) }

    CustomAlertDialog(
        showFlag = viewModel.showValidationDialog,
        type = AlertType.FAILED,
        rightAction = { viewModel.closeValidationAlert() },
        message = "カテゴリ名は必須入力です。"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        HeaderView(
            title = "カテゴリ登録",
            leftOnClick = { navController.popBackStack() },
            leftImageVector = Icons.AutoMirrored.Filled.ArrowBack,
            leftContentDescription = "画面を戻る",
            rightOnClick =
                {
                    val category = viewModel.createCategory(
                        color = selectedColor
                    ) ?: return@HeaderView
                    // 入力した値を戻り先にセット
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(Category.KEY, category)

                    navController.popBackStack()
                },
            rightImageVector = Icons.Filled.Check,
            rightContentDescription = "カテゴリ登録",
        )

        Spacer(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        CustomText(
            text = "Preview",
            textSize = TextSize.S,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 色のプレビュー
        CustomText(
            viewModel.categoryName.ifEmpty { "カテゴリラベル名" },
            color = ExWhite,
            modifier = Modifier
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(8.dp),
                    clip = false
                ).background(selectedColor, RoundedCornerShape(8.dp))
                .height(40.dp)
                .padding(10.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        ThemeInputBox(
            title = "カテゴリラベル名",
            value = viewModel.categoryName,
            onValueChange = {
                viewModel.categoryName = it
            },
            singleLine = false
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomText(
            text = "ラベルカラー",
            textSize = TextSize.S,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        RGBColorPicker(
            initialColor = selectedColor,
            onColorChanged = { selectedColor = it }
        )
    }
}

@Composable
private fun RGBColorPicker(
    modifier: Modifier = Modifier,
    initialColor: Color,
    onColorChanged: (Color) -> Unit
) {
    // rememberSaveableではColor型は保存できないのでrememberにする
    var red by remember { mutableFloatStateOf(initialColor.red) }
    var green by remember { mutableFloatStateOf(initialColor.green) }
    var blue by remember { mutableFloatStateOf(initialColor.blue) }

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // R
        CustomText("Red: ${(red * 255).toInt()}")
        Slider(
            value = red,
            onValueChange = {
                red = it
                onColorChanged(Color(red, green, blue))
            },
            valueRange = 0f..1f,
            colors = SliderDefaults.colors(
                thumbColor = Color.Red,
                activeTrackColor = Color.Red
            )
        )

        // G
        CustomText("Green: ${(green * 255).toInt()}")
        Slider(
            value = green,
            onValueChange = {
                green = it
                onColorChanged(Color(red, green, blue))
            },
            valueRange = 0f..1f,
            colors = SliderDefaults.colors(
                thumbColor = Color.Green,
                activeTrackColor = Color.Green
            )
        )

        // B
        CustomText("Blue: ${(blue * 255).toInt()}")
        Slider(
            value = blue,
            onValueChange = {
                blue = it
                onColorChanged(Color(red, green, blue))
            },
            valueRange = 0f..1f,
            colors = SliderDefaults.colors(
                thumbColor = Color.Blue,
                activeTrackColor = Color.Blue
            )
        )
    }
}
