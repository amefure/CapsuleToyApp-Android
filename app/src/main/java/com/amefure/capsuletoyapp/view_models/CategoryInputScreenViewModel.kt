package com.amefure.capsuletoyapp.view_models

import androidx.annotation.MainThread
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import com.amefure.capsuletoyapp.models.Domain.Entity.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class CategoryInputScreenViewModel @Inject constructor(
) : ViewModel() {

    /** UI連動プロパティ */
    public var categoryName by mutableStateOf("")

    public var showValidationDialog by mutableStateOf(false)
        private set


    @MainThread
    public fun showValidationAlert() {
        showValidationDialog = true
    }
    @MainThread
    public fun closeValidationAlert() {
        showValidationDialog = false
    }


    public fun createCategory(
        color: Color
    ): Category? {
        if (categoryName.isEmpty()) {
            showValidationAlert()
            return null
        }

        val category = Category(
            name = categoryName,
            colorArgb = color.toArgb()
        )
        return category
    }
}


