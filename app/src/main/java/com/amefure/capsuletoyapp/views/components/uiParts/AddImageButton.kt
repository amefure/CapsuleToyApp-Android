package com.amefure.capsuletoyapp.views.components.uiParts

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.amefure.capsuletoyapp.ui.theme.ExGold
import com.amefure.capsuletoyapp.viewModels.interfaces.CameraInterface

@Composable
fun AddImageButton(
    viewModel: CameraInterface,
) {
    val thumbnail = viewModel.thumbnail

    // カメラ起動ランチャー
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
    ) { success ->
        if (success) {
            viewModel.onCameraCaptured()
        }
    }

    // ギャラリー起動ランチャー
    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ) { uri: Uri? ->
        uri?.let {
            viewModel.onGalleryImageSelected(it)
        }
    }

    thumbnail?.let { bitmap ->
        Button(
            onClick = {
                viewModel.expanded = true
            },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .aspectRatio(1f),
            contentPadding = PaddingValues(0.dp),
        ) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "撮影した写真",
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop,
            )
        }
    } ?: run {
        OutlinedButton(
            onClick = {
                viewModel.expanded = true
            },
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(2.dp, ExGold),
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .aspectRatio(1f),
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "画像追加",
                tint = ExGold,
            )
        }
    }

    DropdownMenu(
        expanded = viewModel.expanded,
        onDismissRequest = { viewModel.expanded = false },
    ) {
        DropdownMenuItem(
            text = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    CustomText("カメラを起動する")
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Camera",
                    )
                }
            },
            onClick = {
                viewModel.expanded = false
                // カメラ起動
                viewModel.photoUri?.let { cameraLauncher.launch(it) }
            },
        )

        DropdownMenuItem(
            text = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    CustomText("写真から選択する")
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = "Gallery",
                    )
                }
            },
            onClick = {
                viewModel.expanded = false
                // ギャラリー起動
                viewModel.photoUri?.let { imageLauncher.launch("image/*") }
            },
        )
    }
}
