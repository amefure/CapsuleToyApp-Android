package com.amefure.capsuletoyapp.view_models

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.annotation.MainThread
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class RootEnvironment @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    public var isShowPermissionAlertDialog by mutableStateOf(false)
        private set


    @MainThread
    public fun showPermissionAlertDialog() {
        isShowPermissionAlertDialog = true
    }
    @MainThread
    public fun closePermissionAlertDialog() {
        isShowPermissionAlertDialog = false
    }


    /** 位置情報関係のパーミッション申請が許可済みかどうか */
    fun isGrantedLocationPermission(): Boolean {
        val fine = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarse = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return fine || coarse
    }

}